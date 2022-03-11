/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.plugin.sso.openid.provider.jwt;

import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.plugin.sso.openid.exception.jwt.OpenIDJwtException;
import org.eclipse.kapua.plugin.sso.openid.exception.jwt.OpenIDJwtExtractionException;
import org.eclipse.kapua.plugin.sso.openid.exception.jwt.OpenIDJwtProcessException;
import org.eclipse.kapua.plugin.sso.openid.provider.OpenIDUtils;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSetting;
import org.eclipse.kapua.plugin.sso.openid.provider.setting.OpenIDSettingKeys;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.ReservedClaimNames;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class represents an abstract JwtProcessor.
 * Each OpenID provider must provide its own JwtProcessor concrete implementation by extending this class.
 */
public abstract class AbstractJwtProcessor implements JwtProcessor {

    private static final String JWKS_URI_WELL_KNOWN_KEY = "jwks_uri";
    private Map<URI, Processor> processors = new HashMap<>();
    private String[] audiences;
    private String[] expectedIssuers;
    private Duration timeout;  // the JwtProcessor expiration time.

    /**
     * Constructs and AbstractJwtProcessor with the given expiration time.
     *
     * @throws OpenIDJwtException if the concrete implementation of {@link #getJwtExpectedIssuers()
     *                            getJwtExpectedIssuers} method throws such exception.
     */
    public AbstractJwtProcessor() throws OpenIDException {
        List<String> audiences = getJwtAudiences();
        List<String> expectedIssuers = getJwtExpectedIssuers();
        this.expectedIssuers = expectedIssuers.toArray(new String[expectedIssuers.size()]);
        this.audiences = audiences.toArray(new String[audiences.size()]);
        this.timeout = Duration.ofHours(OpenIDSetting.getInstance().getInt(OpenIDSettingKeys.SSO_OPENID_JWT_PROCESSOR_TIMEOUT, 1));
    }

    /**
     * Extract the JWT Issuer URI.
     *
     * @param jwt the String containing the JWT.
     * @return the URI of the JWT issuer.
     * @throws OpenIDJwtException if an InvalidJwtException or a MalformedClaimException is catch inside the method.
     */
    private static URI extractIssuer(final String jwt) throws OpenIDJwtException {

        try {
            // Parse JWT without validation
            final JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setSkipAllValidators()
                    .setDisableRequireSignature()
                    .setSkipSignatureVerification()
                    .build();

            final JwtContext jwtContext = jwtConsumer.process(jwt);

            // Resolve Json Web Key Set URI by the issuer
            String issuer = jwtContext.getJwtClaims().getIssuer();
            if (issuer.endsWith("/")) {
                issuer = issuer.substring(0, issuer.length() - 1);
            }

            return URI.create(issuer);
        } catch (InvalidJwtException | MalformedClaimException e) {
            throw new OpenIDJwtExtractionException(e, jwt);
        }
    }

    /**
     * @throws OpenIDJwtException if the JWT issuer extraction or the JWT Processor lookup fail
     */
    @Override
    public boolean validate(final String jwt) throws OpenIDException {
        final URI issuer = extractIssuer(jwt);
        return lookupProcessor(issuer)
                .orElseThrow(() -> new IllegalStateException("Unable to auto-discover JWT endpoint"))
                .validate(jwt);
    }

    /**
     * @throws OpenIDJwtException if the JWT issuer extraction or the JWT Processor lookup fail
     */
    @Override
    public JwtContext process(final String jwt) throws OpenIDException {
        final URI issuer = extractIssuer(jwt);
        return lookupProcessor(issuer)
                .orElseThrow(() -> new IllegalStateException("Unable to auto-discover JWT endpoint"))
                .process(jwt);
    }

    /**
     * @return 'sub' (according to the official OpenID Connect 1.0 specification)
     */
    @Override
    public String getExternalIdClaimName() {
        return ReservedClaimNames.SUBJECT;
    }

    /**
     * @return 'preferred_username' (according to the official OpenID Connect 1.0 specification)
     */
    @Override
    public String getExternalUsernameClaimName() {
        return OpenIDSetting.getInstance().getString(OpenIDSettingKeys.SSO_OPENID_CLAIMS_EXTERNAL_USERNAME_KEY, "preferred_username");
    }

    @Override
    public void close() throws Exception {
    }

    /**
     * Retrieve the list of JWT expected issuers.
     *
     * @return the list of String containing the JWT issuers.
     * @throws OpenIDException if the JWT issuers cannot be retrieved
     */
    protected abstract List<String> getJwtExpectedIssuers() throws OpenIDException;

    /**
     * Retrieve the list of JWT audiences, which represent the OpenID Connect Relying Parties
     * (for more information see 'aud' here: https://openid.net/specs/openid-connect-core-1_0.html#IDToken ).
     *
     * @return the list of String containing the JWT issuers.
     * @throws OpenIDException if the JWT audiences cannot be retrieved.
     */
    protected abstract List<String> getJwtAudiences() throws OpenIDException;

    /**
     * Retrieve a {@link Processor Processor}.
     *
     * @param issuer the URI of a JWT issuer.
     * @return an Optional with a {@link Processor Processor} if everything is fine, otherwise an empty Optional.
     * @throws OpenIDException if it fails to retrieve a URI when the processor is null or expired.
     */
    private Optional<Processor> lookupProcessor(final URI issuer) throws OpenIDException {

        Processor processor = processors.get(issuer);

        // test if validator is expired
        if (processor != null && processor.isExpired(timeout)) {
            processors.remove(issuer);
            processor = null;
        }

        if (processor == null) {

            // create new instance

            final Optional<URI> uri = OpenIDUtils.getConfigUri(JWKS_URI_WELL_KNOWN_KEY, OpenIDUtils.getOpenIdConfPath(issuer));
            if (!uri.isPresent()) {
                return Optional.empty();
            }
            processor = new Processor(uri.get(), audiences, expectedIssuers);
            processors.put(uri.get(), processor);
        }

        // return result

        return Optional.of(processor);
    }

    private static class Processor {

        private Instant lastUsed = Instant.now();
        private JwtConsumer consumer;

        /**
         * Processor constructor.
         *
         * @param jwksUri         a URI to retrieve JWKs.
         * @param audiences       a list of JWT audiences.
         * @param expectedIssuers a list of JWT issuers.
         */
        public Processor(final URI jwksUri, String[] audiences, String[] expectedIssuers) {
            final HttpsJwksVerificationKeyResolver resolver = new HttpsJwksVerificationKeyResolver(new HttpsJwks(jwksUri.toString()));

            this.consumer = new JwtConsumerBuilder()
                    .setVerificationKeyResolver(resolver) // Set resolver key
                    .setRequireIssuedAt() // Set require reserved claim: iat
                    .setRequireExpirationTime() // Set require reserved claim: exp
                    .setRequireSubject() // // Set require reserved claim: sub
                    .setExpectedIssuers(true, expectedIssuers)
                    .setExpectedAudience(audiences)
                    .build();
        }

        /**
         * Validate a JWT.
         *
         * @param jwt a JWT in the form of a String.
         * @return <tt>true</tt> if the validation succeeds, <tt>false</tt> otherwise.
         */
        public boolean validate(final String jwt) {
            try {
                process(jwt);
                return true;
            } catch (OpenIDJwtProcessException e) {
                return false;
            }
        }

        /**
         * Process a JWT.
         *
         * @param jwt a JWT in the form of a String.
         * @return a {@link JwtContext} object.
         * @throws OpenIDJwtProcessException if an {@link InvalidJwtException} is caught.
         */
        public JwtContext process(final String jwt) throws OpenIDJwtProcessException {
            try {
                lastUsed = Instant.now();
                return consumer.process(jwt);
            } catch (InvalidJwtException ije) {
                throw new OpenIDJwtProcessException(ije, jwt);
            }
        }

        /**
         * Check if this Processor is expired.
         *
         * @param timeout the JwtProcessor expiration time.
         * @return <tt>true</tt> if the Processor is expired, <tt>false</tt> otherwise.
         */
        public boolean isExpired(final Duration timeout) {
            return lastUsed.plus(timeout).isBefore(Instant.now());
        }
    }

}
