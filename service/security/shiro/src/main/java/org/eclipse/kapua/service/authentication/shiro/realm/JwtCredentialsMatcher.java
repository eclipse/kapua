/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.realm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.shiro.JwtCredentialsImpl;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link UsernamePasswordCredentials} credential matcher implementation
 * 
 * @since 1.0
 * 
 */
public class JwtCredentialsMatcher implements CredentialsMatcher {

    private static Logger logger = LoggerFactory.getLogger(JwtCredentialsMatcher.class);

    // FIXME: Add a time-to-live cache???
    private static final Map<String, URI> ISSUER_JWKSURI_CACHE = new HashMap<>();

    private static final String OPEN_ID_CONFIGURATION_WELL_KNOWN_PATH = "/.well-known/openid-configuration";
    private static final String JWKS_URI_WELL_KNOWN_KEY = "jwks_uri";

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {

        JwtCredentialsImpl token = (JwtCredentialsImpl) authenticationToken;
        String jwt = token.getJwt();

        //
        // Info data
        LoginAuthenticationInfo info = (LoginAuthenticationInfo) authenticationInfo;
        Credential infoCredential = (Credential) info.getCredentials();

        //
        // Match token with info
        boolean credentialMatch = false;
        if (jwt.equals(infoCredential.getCredentialKey())) {
            try {

                URI jwksUri = resolveJwksUri(jwt);
                HttpsJwks httpsJkws = new HttpsJwks(jwksUri.toString());
                HttpsJwksVerificationKeyResolver httpsJwksKeyResolver = new HttpsJwksVerificationKeyResolver(httpsJkws);

                //
                // Set validator
                KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();
                List<String> audiences = setting.getList(String.class, KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_AUDIENCE_ALLOWED);

                JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                        .setVerificationKeyResolver(httpsJwksKeyResolver) // Set resolver key
                        .setRequireIssuedAt() // Set require reserved claim: iat
                        .setRequireExpirationTime() // Set require reserved claim: exp
                        .setRequireSubject() // // Set require reserved claim: sub
                        .setExpectedAudience(audiences.toArray(new String[audiences.size()]))
                        .build();

                //
                // This validates JWT
                jwtConsumer.processToClaims(jwt);

                credentialMatch = true;

                // FIXME: if true cache token password for authentication performance improvement
            } catch (InvalidJwtException e) {
                logger.error("Error while validating JWT credentials", e);
            }
        }

        return credentialMatch;
    }

    private URI resolveJwksUri(String jwt) {

        URI uri;
        URLConnection urlConnection = null;
        BufferedReader inputReader = null;
        try {
            //
            // Parse JWT without validation
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setSkipAllValidators()
                    .setDisableRequireSignature()
                    .setSkipSignatureVerification()
                    .build();
            JwtContext jwtContext = jwtConsumer.process(jwt);

            //
            // Resolve Json Web Key Set URI by the issuer
            String issuer = jwtContext.getJwtClaims().getIssuer();
            if (issuer.endsWith("/")) {
                issuer = issuer.substring(0, issuer.length() - 1);
            }

            // Check against cache
            if (ISSUER_JWKSURI_CACHE.containsKey(issuer)) {
                uri = ISSUER_JWKSURI_CACHE.get(issuer);
            } else {
                // Read .well-known file
                URL url = new URL(issuer + OPEN_ID_CONFIGURATION_WELL_KNOWN_PATH);
                urlConnection = url.openConnection();
                inputReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                JsonReader jsonReader = Json.createReader(inputReader);

                // Parse json response
                JsonObject jsonObject = jsonReader.readObject();
                inputReader.close();

                // Get and clean jwks_uri property
                JsonValue jwksUriJsonValue = jsonObject.get(JWKS_URI_WELL_KNOWN_KEY);

                if (jwksUriJsonValue != null) {
                    String jwksUriString = jwksUriJsonValue.toString().replaceAll("\"", "");
                    uri = new URI(jwksUriString);
                    ISSUER_JWKSURI_CACHE.put(issuer, uri);
                } else {
                    throw KapuaException.internalError("Cannot get the 'jwks_key' property from the .well-known file:" + jsonObject.toString());
                }
            }
        } catch (Exception e) {
            throw KapuaRuntimeException.internalError(e, "Cannot het Json Web Key Set URI");
        } finally {
            if (inputReader != null) {
                try {
                    inputReader.close();
                } catch (IOException e) {
                    logger.error("Cannot close inputReader", e);
                }
            }
        }

        return uri;
    }
}
