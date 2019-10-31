/*******************************************************************************
 * Copyright (c) 2017, 2019 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.sso.provider.jwt;

import org.eclipse.kapua.sso.JwtProcessor;
import org.eclipse.kapua.sso.exception.SsoJwtException;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.MalformedClaimException;
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

public abstract class AbstractJwtProcessor implements JwtProcessor {

    private static final String JWKS_URI_WELL_KNOWN_KEY = "jwks_uri";
    private Map<URI, Processor> processors = new HashMap<>();
    private String[] audiences;
    private String[] expectedIssuers;
    private Duration cacheTimeout;
    /**
     * @param audiences
     * @param expectedIssuers
     * @param cacheTimeout
     * @deprecated This is the old constructor of the JwtProcessor class, not used anymore.
     */
    @Deprecated
    public AbstractJwtProcessor(final List<String> audiences, final List<String> expectedIssuers, final Duration cacheTimeout) {
        this.audiences = audiences.toArray(new String[audiences.size()]);
        this.expectedIssuers = expectedIssuers.toArray(new String[expectedIssuers.size()]);
        this.cacheTimeout = cacheTimeout;
    }

    public AbstractJwtProcessor(final Duration cacheTimeout) throws SsoJwtException {
        List<String> audiences = getJwtAudiences();
        List<String> expectedIssuers = getJwtExpectedIssuers();
        this.expectedIssuers = expectedIssuers.toArray(new String[expectedIssuers.size()]);
        this.audiences = audiences.toArray(new String[audiences.size()]);
        this.cacheTimeout = cacheTimeout;
    }

    private static URI extractIssuer(final String jwt) throws SsoJwtException {

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
            throw new SsoJwtException(e);
        }
    }

    @Override
    public boolean validate(final String jwt) throws SsoJwtException {
        final URI issuer = extractIssuer(jwt);
        return lookupProcessor(issuer)
                .orElseThrow(() -> new IllegalStateException("Unable to auto-discover JWT endpoint"))
                .validate(jwt);
    }

    @Override
    public JwtContext process(final String jwt) throws SsoJwtException {
        final URI issuer = extractIssuer(jwt);
        return lookupProcessor(issuer)
                .orElseThrow(() -> new IllegalStateException("Unable to auto-discover JWT endpoint"))
                .process(jwt);
    }

    @Override
    public void close() throws Exception {
    }

    protected abstract String getOpenIdConfPath(final URI issuer);

    protected abstract List<String> getJwtExpectedIssuers() throws SsoJwtException;

    protected abstract List<String> getJwtAudiences();

    private Optional<Processor> lookupProcessor(final URI issuer) throws SsoJwtException {

        Processor processor = processors.get(issuer);

        // test if validator is expired

        if (processor != null && processor.isExpired(cacheTimeout)) {
            processors.remove(issuer);
            processor = null;
        }

        if (processor == null) {

            // create new instance

            final Optional<URI> uri = JwtUtils.retrieveJwtUri(JWKS_URI_WELL_KNOWN_KEY, getOpenIdConfPath(issuer));
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

        public boolean validate(final String jwt) {
            try {
                process(jwt);
                return true;
            } catch (SsoJwtException e) {
                return false;
            }
        }

        public JwtContext process(final String jwt) throws SsoJwtException {
            try {
                lastUsed = Instant.now();
                return consumer.process(jwt);
            } catch (InvalidJwtException ije) {
                throw new SsoJwtException(ije);
            }
        }

        public boolean isExpired(final Duration timeout) {
            return lastUsed.plus(timeout).isBefore(Instant.now());
        }
    }

}
