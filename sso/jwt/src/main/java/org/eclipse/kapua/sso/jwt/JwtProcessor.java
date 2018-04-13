/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.sso.jwt;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;

public class JwtProcessor implements AutoCloseable {

    private static final String OPEN_ID_CONFIGURATION_WELL_KNOWN_PATH = ".well-known/openid-configuration";
    private static final String JWKS_URI_WELL_KNOWN_KEY = "jwks_uri";

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
            } catch (InvalidJwtException e) {
                return false;
            }
        }

        public JwtContext process(final String jwt) throws InvalidJwtException {
            lastUsed = Instant.now();
            return consumer.process(jwt);
        }

        public boolean isExpired(final Duration timeout) {
            return lastUsed.plus(timeout).isBefore(Instant.now());
        }
    }

    private Map<URI, Processor> processors = new HashMap<>();

    private String[] audiences;
    private String[] expectedIssuers;
    private Duration cacheTimeout;

    public JwtProcessor(final List<String> audiences, final List<String> expectedIssuers, final Duration cacheTimeout) {
        this.audiences = audiences.toArray(new String[audiences.size()]);
        this.expectedIssuers = expectedIssuers.toArray(new String[expectedIssuers.size()]);
        this.cacheTimeout = cacheTimeout;
    }

    public boolean validate(final String jwt) throws Exception {
        final URI issuer = extractIssuer(jwt);

        return lookupProcessor(issuer)
                .orElseThrow(() -> new IllegalStateException("Unable to auto-discover JWT endpoint"))
                .validate(jwt);
    }

    public JwtContext process(final String jwt) throws Exception {
        final URI issuer = extractIssuer(jwt);

        return lookupProcessor(issuer)
                .orElseThrow(() -> new IllegalStateException("Unable to auto-discover JWT endpoint"))
                .process(jwt);
    }

    @Override
    public void close() throws Exception {
    }

    private static URI extractIssuer(final String jwt) throws InvalidJwtException, MalformedClaimException {

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
    }

    private static Optional<URI> retrieveJwksUri(final URI issuer) throws IOException, URISyntaxException {

        final JsonObject jsonObject;

        // Read .well-known resource

        try (final InputStream stream = new URL(issuer.toString() + "/" + OPEN_ID_CONFIGURATION_WELL_KNOWN_PATH).openStream()) {
            // Parse json response
            jsonObject = Json.createReader(stream).readObject();
        }

        // Get and clean jwks_uri property

        final JsonValue jwksUriJsonValue = jsonObject.get(JWKS_URI_WELL_KNOWN_KEY);

        // test result

        if (jwksUriJsonValue instanceof JsonString) {
            return Optional.of(new URI(((JsonString) jwksUriJsonValue).getString()));
        }

        // return

        return Optional.empty();
    }

    private Optional<Processor> lookupProcessor(final URI issuer) throws IOException, URISyntaxException {

        Processor processor = processors.get(issuer);

        // test if validator is expired

        if (processor != null && processor.isExpired(cacheTimeout)) {
            processors.remove(issuer);
            processor = null;
        }

        if (processor == null) {

            // create new instance

            final Optional<URI> uri = retrieveJwksUri(issuer);
            if (!uri.isPresent()) {
                return Optional.empty();
            }
            processor = new Processor(uri.get(), audiences, expectedIssuers);
            processors.put(uri.get(), processor);
        }

        // return result

        return Optional.of(processor);
    }

}
