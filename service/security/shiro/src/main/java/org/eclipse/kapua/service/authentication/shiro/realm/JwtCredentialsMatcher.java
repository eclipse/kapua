/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.shiro.JwtCredentialsImpl;
import org.eclipse.kapua.sso.jwt.JwtProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link JwtCredentialsMatcher} credential matcher implementation
 *
 * @since 1.0
 *
 */
public class JwtCredentialsMatcher implements CredentialsMatcher {

    private static final Logger logger = LoggerFactory.getLogger(JwtCredentialsMatcher.class);
    private JwtProcessor jwtProcessor;

    public JwtCredentialsMatcher(final JwtProcessor jwtProcessor) {
        this.jwtProcessor = jwtProcessor;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {

        final String jwt = ((JwtCredentialsImpl) authenticationToken).getJwt();
        if (jwt == null) {
            // we don't have a JWT
            return false;
        }

        // check for correct credentials type

        final Object credentialsValue = authenticationInfo.getCredentials();
        if (!(credentialsValue instanceof Credential)) {
            return false;
        }

        // extract credentials

        final Credential credentials = (Credential) credentialsValue;

        // Match token with info

        if (!jwt.equals(credentials.getCredentialKey())) {
            return false;
        }

        try {
            // validate the JWT
            return this.jwtProcessor.validate(jwt);
        } catch (Exception e) {
            logger.error("Error while validating JWT credentials", e);
        }

        return false;
    }

}
