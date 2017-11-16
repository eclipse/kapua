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
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.certificate.Certificate;
import org.eclipse.kapua.service.certificate.CertificateFactory;
import org.eclipse.kapua.service.certificate.CertificatePredicates;
import org.eclipse.kapua.service.certificate.CertificateQuery;
import org.eclipse.kapua.service.certificate.CertificateService;
import org.eclipse.kapua.service.certificate.util.CertificateUtils;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link AccessTokenCredentials} credential matcher implementation
 *
 * @since 1.0
 */
public class AccessTokenCredentialsMatcher implements CredentialsMatcher {

    private static Logger logger = LoggerFactory.getLogger(AccessTokenCredentialsMatcher.class);

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        //
        // Token data
        String jwt = (String) authenticationToken.getCredentials();

        //
        // Info data
        SessionAuthenticationInfo info = (SessionAuthenticationInfo) authenticationInfo;
        AccessToken infoCredential = info.getAccessToken();

        //
        // Match token with info
        boolean credentialMatch = false;
        if (jwt.equals(infoCredential.getTokenId())) {
            KapuaAuthenticationSetting settings = KapuaAuthenticationSetting.getInstance();
            try {
                String issuer = settings.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_SESSION_JWT_ISSUER);
                KapuaLocator locator = KapuaLocator.getInstance();

                CertificateService certificateService = locator.getService(CertificateService.class);
                CertificateFactory certificateFactory = locator.getFactory(CertificateFactory.class);
                CertificateQuery certificateQuery = certificateFactory.newQuery(null);
                certificateQuery.setPredicate(new AttributePredicate<>(CertificatePredicates.USAGE_NAME, "JWT"));
                certificateQuery.setLimit(1);

                Certificate certificate = KapuaSecurityUtils.doPrivileged(() -> certificateService.query(certificateQuery)).getItem(0);

                //
                // Set validator
                JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                        .setVerificationKey(CertificateUtils.stringToCertificate(certificate.getCertificate()).getPublicKey()) // Set public key
                        .setExpectedIssuer(issuer) // Set expected issuer
                        .setRequireIssuedAt() // Set require reserved claim: iat
                        .setRequireExpirationTime() // Set require reserved claim: exp
                        .setRequireSubject() // // Set require reserved claim: sub
                        .build();

                //
                // This validates JWT
                jwtConsumer.processToClaims(jwt);

                credentialMatch = true;

                // FIXME: if true cache token password for authentication performance improvement
            } catch (InvalidJwtException | KapuaException e) {
                logger.error("Error while validating JWT access token", e);
            }
        }

        return credentialMatch;
    }
}
