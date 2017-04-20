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
import org.jose4j.jwt.consumer.InvalidJwtException;
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
        if (jwt.equals(infoCredential.getCredentialKey())) {
            try {
                //
                // This validates JWT
                JwtHelper.processJwt(jwt);

                return true;

                // FIXME: if true cache token password for authentication performance improvement
            } catch (InvalidJwtException e) {
                logger.error("Error while validating JWT credentials", e);
            }
        }

        return false;
    }

}
