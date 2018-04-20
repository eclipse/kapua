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
package org.eclipse.kapua.service.authentication.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapua Shiro Authenticator.<br>
 * This authenticator provide more significantly exception message in a multi-realm configuration.<br>
 * The code is derived from the original {@link ModularRealmAuthenticator} because the <b>default Shiro implementation doesn't support detailed messages in a multirealm configuration.</b>
 *
 * since 1.0
 * 
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
                        String msg = "Realm [" + realm
                                + "] threw an exception during a multi-realm authentication attempt:";
                        logger.debug(msg, t);
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
