/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Kapua Shiro Authenticator.
 * <p>
 * This authenticator provide more significantly exception message in a multi-realm configuration.<br>
 * The code is derived from the original {@link ModularRealmAuthenticator} because the
 * <b>default Shiro implementation doesn't support detailed messages in a multirealm configuration.</b>
 *
 * @since 1.0.0
 */
public class KapuaAuthenticator extends ModularRealmAuthenticator {

    private static final Logger logger = LoggerFactory.getLogger(KapuaAuthenticator.class);

    @Override
    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
        AuthenticationStrategy strategy = getAuthenticationStrategy();
        AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);
        if (logger.isTraceEnabled()) {
            logger.trace("Iterating through {} realms for PAM authentication", realms.size());
        }

        List<Throwable> exceptionList = new ArrayList<>();
        boolean loginSucceeded = false;
        boolean supportedRealmFound = false;
        for (Realm realm : realms) {
            aggregate = strategy.beforeAttempt(realm, token, aggregate);
            if (realm.supports(token)) {
                supportedRealmFound = true;
                logger.trace("Attempting to authenticate token [{}] using realm [{}]", token, realm);
                AuthenticationInfo info = null;
                Throwable t = null;
                try {
                    info = realm.getAuthenticationInfo(token);
                    loginSucceeded = true;
                } catch (Exception exception) {
                    t = exception;
                    if (logger.isDebugEnabled()) {
                        logger.debug("Realm [{}] threw an exception during a multi-realm authentication attempt:", realm, t);
                    }
                }
                aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);
                exceptionList.add(t);
            } else {
                logger.debug("Realm [{}] does not support token {}.  Skipping realm.", realm, token);
            }
        }
        // modified behavior from the ModularRealmAuthenticator to provide a more significantly exception message to the user if the login fails
        if (supportedRealmFound && !loginSucceeded) {
            // if there is no realm able to authenticate the AuthenticationToken (but at least one realm for this AuthenticationToken was found) lets check the exceptions thrown by the logins
            if (exceptionList.size() <= 0) {
                // login failed and we have no exception to show so throw a ShiroException?
                // TODO move the error message to the message bundle
                throw new ShiroException("Internal Error!");
            }

            if (exceptionList.get(0) instanceof AuthenticationException) {
                throw (AuthenticationException) exceptionList.get(0);
            } else {
                throw new AuthenticationException(exceptionList.get(0));
            }
        } else {
            // otherwise if at least one login succeeded lets proceed with the standard ModularRealmAuthenticator
            aggregate = strategy.afterAllAttempts(token, aggregate);
        }
        return aggregate;
    }

}
