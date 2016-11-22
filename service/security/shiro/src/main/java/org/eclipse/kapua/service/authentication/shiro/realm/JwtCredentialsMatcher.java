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

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.shiro.JwtCredentialsImpl;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwt.MalformedClaimException;
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

                JwtConsumer firstPassJwtConsumer = new JwtConsumerBuilder()
                        .setSkipAllValidators()
                        .setDisableRequireSignature()
                        .setSkipSignatureVerification()
                        .build();

                JwtContext jwtContext = firstPassJwtConsumer.process(jwt);
                HttpsJwks httpsJkws = new HttpsJwks(jwtContext.getJwtClaims().getIssuer() + ".well-known/jwks.json");
                HttpsJwksVerificationKeyResolver httpsJwksKeyResolver = new HttpsJwksVerificationKeyResolver(httpsJkws);

                //
                // Set validator
                JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                        .setVerificationKeyResolver(httpsJwksKeyResolver) // Set resolver key
                        .setRequireIssuedAt() // Set require reserved claim: iat
                        .setRequireExpirationTime() // Set require reserved claim: exp
                        .setRequireSubject() // // Set require reserved claim: sub
                        .build();

                //
                // This validates JWT
                jwtConsumer.processToClaims(jwt);

                credentialMatch = true;

                // FIXME: if true cache token password for authentication performance improvement
            } catch (InvalidJwtException | MalformedClaimException e) {
                logger.error("Error while validating JWT credentials", e);
            }
        }

        return credentialMatch;
    }
}
