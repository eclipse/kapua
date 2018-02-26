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
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authentication.AccessTokenCredentials;
import org.eclipse.kapua.service.authentication.shiro.exceptions.JwtCertificateNotFoundException;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.certificate.Certificate;
import org.eclipse.kapua.service.certificate.CertificateFactory;
import org.eclipse.kapua.service.certificate.CertificatePredicates;
import org.eclipse.kapua.service.certificate.CertificateQuery;
import org.eclipse.kapua.service.certificate.CertificateService;
import org.eclipse.kapua.service.certificate.CertificateStatus;
import org.eclipse.kapua.service.certificate.util.CertificateUtils;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link AccessTokenCredentials} {@link CredentialsMatcher} implementation.
 *
 * @since 1.0.0
 */
public class AccessTokenCredentialsMatcher implements CredentialsMatcher {

    private static final Logger LOG = LoggerFactory.getLogger(AccessTokenCredentialsMatcher.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final CertificateService CERTIFICATE_SERVICE = LOCATOR.getService(CertificateService.class);
    private static final CertificateFactory CERTIFICATE_FACTORY = LOCATOR.getFactory(CertificateFactory.class);

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

                CertificateQuery certificateQuery = CERTIFICATE_FACTORY.newQuery(null);
                certificateQuery.setPredicate(
                        new AndPredicate(
                                new AttributePredicate<>(CertificatePredicates.USAGE_NAME, "JWT"),
                                new AttributePredicate<>(CertificatePredicates.STATUS, CertificateStatus.VALID)
                        )
                );
                certificateQuery.setSortCriteria(new FieldSortCriteria(CertificatePredicates.CREATED_BY, FieldSortCriteria.SortOrder.DESCENDING));
                certificateQuery.setIncludeInherited(true);
                certificateQuery.setLimit(1);

                Certificate certificate = KapuaSecurityUtils.doPrivileged(() -> CERTIFICATE_SERVICE.query(certificateQuery)).getFirstItem();

                if (certificate == null) {
                    throw new JwtCertificateNotFoundException();
                }

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
                LOG.error("Error while validating JWT access token", e);
            }
        }

        return credentialMatch;
    }
}
