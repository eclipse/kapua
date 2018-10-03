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

import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialAttributes;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.shiro.UsernamePasswordCredentialsImpl;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.realm.AuthenticatingRealm;

/**
 * {@link UsernamePasswordCredentials} based {@link AuthenticatingRealm} implementation.
 * <p>
 * since 1.0
 */
public class UserPassAuthenticatingRealm extends LockoutPolicyAuthenticatingRealm {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AccountService ACCOUNT_SERVICE = LOCATOR.getService(AccountService.class);
    private static final UserService USER_SERVICE = LOCATOR.getService(UserService.class);
    private static final CredentialService CREDENTIAL_SERVICE = LOCATOR.getService(CredentialService.class);
    private static final CredentialFactory CREDENTIAL_FACTORY = LOCATOR.getFactory(CredentialFactory.class);

    /**
     * Realm name
     */
    private static final String REALM_NAME = "userPassAuthenticatingRealm";

    /**
     * Constructor
     */
    public UserPassAuthenticatingRealm() {
        setName(REALM_NAME);

        CredentialsMatcher credentialsMather = new UserPassCredentialsMatcher();
        setCredentialsMatcher(credentialsMather);
    }

    @Override
    protected LoginAuthenticationInfo buildAuthenticationInfo(AuthenticationToken authenticationToken) {

        //
        // Get the associated user by name
        final User user;
        try {
            user = KapuaSecurityUtils.doPrivileged(() -> USER_SERVICE.findByName(authenticationToken.getPrincipal().toString()));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Error while find user!", e);
        }

        final Credential credential;
        // FIXME: manage multiple credentials and multiple credentials type
        try {
            credential = KapuaSecurityUtils.doPrivileged(() -> {
                CredentialQuery query = CREDENTIAL_FACTORY.newQuery(user.getScopeId());
                AndPredicate andPredicate = new AndPredicateImpl()
                        .and(new AttributePredicateImpl<>(CredentialAttributes.USER_ID, user.getId()))
                        .and(new AttributePredicateImpl<>(CredentialAttributes.CREDENTIAL_TYPE, CredentialType.PASSWORD));
                query.setPredicate(andPredicate);
                CredentialListResult credentialList = CREDENTIAL_SERVICE.query(query);
                if (credentialList != null && !credentialList.isEmpty() && credentialList.getSize() == 1) {
                    return credentialList.getFirstItem();
                } else {
                    return null;
                }
            });
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Error while find credentials!", e);
        }

        //
        // Find account
        final Account account;
        try {
            account = KapuaSecurityUtils.doPrivileged(() -> ACCOUNT_SERVICE.find(user.getScopeId()));
        } catch (AuthenticationException ae) {
            throw ae;
        } catch (Exception e) {
            throw new ShiroException("Error while find account!", e);
        }

        return new LoginAuthenticationInfo(getName(), account, user, credential);
    }

    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof UsernamePasswordCredentialsImpl;
    }
}
