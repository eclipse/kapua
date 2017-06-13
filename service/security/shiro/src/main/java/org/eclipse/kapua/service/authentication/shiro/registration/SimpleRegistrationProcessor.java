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
package org.eclipse.kapua.service.authentication.shiro.registration;

import static java.util.Optional.empty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.internal.AccountDomain;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.UserType;
import org.eclipse.kapua.service.user.internal.UserDomain;
import org.jose4j.jwt.consumer.JwtContext;

public class SimpleRegistrationProcessor implements RegistrationProcessor {

    private final AccountService accountService = KapuaLocator.getInstance().getService(AccountService.class);
    private final AccountFactory accountFactory = KapuaLocator.getInstance().getFactory(AccountFactory.class);

    private final UserService userService = KapuaLocator.getInstance().getService(UserService.class);
    private final UserFactory userFactory = KapuaLocator.getInstance().getFactory(UserFactory.class);

    private final AccessInfoService accessInfoService = KapuaLocator.getInstance().getService(AccessInfoService.class);
    private final AccessInfoFactory accessInfoFactory = KapuaLocator.getInstance().getFactory(AccessInfoFactory.class);

    private final PermissionFactory permissionFactory = KapuaLocator.getInstance().getFactory(PermissionFactory.class);

    private final String claimName;
    private final KapuaId rootAccount;

    public SimpleRegistrationProcessor(final String claimName, final KapuaId rootAccount) {
        this.claimName = claimName;
        this.rootAccount = rootAccount;
    }

    @Override
    public Optional<User> createUser(final JwtContext context) throws Exception {
        final KapuaSession session = new KapuaSession(null, rootAccount, rootAccount);

        final KapuaSession oldSession = KapuaSecurityUtils.getSession();
        KapuaSecurityUtils.setSession(session);
        try {
            return KapuaSecurityUtils.doPrivileged(() -> internalCreateUser(context));
        } finally {
            KapuaSecurityUtils.setSession(oldSession);
        }
    }

    private Optional<User> internalCreateUser(JwtContext context) throws Exception {

        final String name = context.getJwtClaims().getClaimValue(claimName, String.class);
        if (name == null || name.isEmpty()) {
            return empty();
        }

        System.out.println(context.getJwtClaims().getRawJson());

        final String email = context.getJwtClaims().getClaimValue("email", String.class);
        if (email == null || email.isEmpty()) {
            return empty();
        }

        final String displayName = context.getJwtClaims().getClaimValue("name", String.class);

        final String subject = context.getJwtClaims().getSubject();

        // define account

        final AccountCreator accountCreator = accountFactory.newCreator(rootAccount, name);
        accountCreator.setOrganizationEmail(email);
        accountCreator.setOrganizationName(name);

        // create account

        final Account account = accountService.create(accountCreator);

        // set the resource limits for the UserService of this account

        final Map<String, Object> values = new HashMap<>(2);
        values.put("maxNumberChildEntities", 1);
        values.put("infiniteChildEntities", false);
        userService.setConfigValues(account.getId(), account.getScopeId(), values);

        // define user

        final UserCreator userCreator = userFactory.newCreator(account.getId(), name);
        userCreator.setUserType(UserType.EXTERNAL);
        userCreator.setExternalId(subject);
        userCreator.setEmail(email);
        userCreator.setDisplayName(displayName);

        // create user

        final User user = userService.create(userCreator);

        // assign login permissions

        final AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(user.getScopeId());
        accessInfoCreator.setUserId(user.getId());

        final Set<Permission> permissions = new HashSet<>(2);
        permissions.add(permissionFactory.newPermission(new AccountDomain(), Actions.read, user.getScopeId()));
        permissions.add(permissionFactory.newPermission(new UserDomain(), Actions.read, user.getScopeId()));
        accessInfoCreator.setPermissions(permissions);

        accessInfoService.create(accessInfoCreator);

        // return result

        return Optional.ofNullable(user);
    }

}
