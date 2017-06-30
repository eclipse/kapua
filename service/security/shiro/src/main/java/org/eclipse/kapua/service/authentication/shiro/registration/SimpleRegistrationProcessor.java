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

import org.eclipse.kapua.KapuaException;
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
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSetting;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSettingKeys;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.UserType;
import org.eclipse.kapua.service.user.internal.UserDomain;
import org.jose4j.jwt.consumer.JwtContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A processor which creates a simple account and user setup
 * <p>
 * This processor creates a new account based on the SSO claim and creates a single
 * user for this account.
 * </p>
 * <p>
 * It is possible to define the root account which all accounts will be part of using
 * the {@link Settings} class.
 * </p>
 */
public class SimpleRegistrationProcessor implements RegistrationProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRegistrationProcessor.class);

    public static class Settings {

        private KapuaId rootAccount;

        private int maximumNumberOfChildUsers;

        public Settings(final KapuaId rootAccount) {
            this.rootAccount = rootAccount;
        }

        public KapuaId getRootAccount() {
            return rootAccount;
        }

        /**
         * Set the maximum number of child users for the newly created account
         * <p>
         * If this value is negative then there will be no limit on the number of child users.
         * </p>
         * 
         * @param maximumNumberOfChildUsers
         *            The number of child users to allow
         */
        public void setMaximumNumberOfChildUsers(int maximumNumberOfChildUsers) {
            this.maximumNumberOfChildUsers = maximumNumberOfChildUsers;
        }

        public int getMaximumNumberOfChildUsers() {
            return maximumNumberOfChildUsers;
        }

        public static Optional<SimpleRegistrationProcessor.Settings> loadSimpleSettings(final KapuaAuthorizationSetting settings) {
            try {
                final String accountName = settings.getString(KapuaAuthorizationSettingKeys.AUTO_REGISTRATION_SIMPLE_ROOT_ACCOUNT);
                if (accountName != null && !accountName.isEmpty()) {
                    return loadFrom(accountName).map(rootAccount -> applySimpleSettings(rootAccount, settings));
                }
                return empty();
            } catch (final KapuaException e) {
                throw new RuntimeException("Failed to load root account ID", e);
            }
        }

        private static Optional<KapuaId> loadFrom(final String accountName) throws KapuaException {
            final User user = KapuaSecurityUtils.doPrivileged(() -> KapuaLocator.getInstance().getService(UserService.class).findByName(accountName));

            if (user != null) {
                return Optional.of(user).map(User::getScopeId);
            }
            logger.warn("Failed to load ID of '{}'. Entry not found.", accountName);
            return Optional.empty();
        }

        private static SimpleRegistrationProcessor.Settings applySimpleSettings(final KapuaId rootAccount, final KapuaAuthorizationSetting kapuaSettings) {
            final Settings settings = new SimpleRegistrationProcessor.Settings(rootAccount);
            settings.setMaximumNumberOfChildUsers(kapuaSettings.getInt(KapuaAuthorizationSettingKeys.AUTO_REGISTRATION_SIMPLE_MAX_NUMBER_OF_CHILD_USERS, 1));
            return settings;
        }

    }

    private final AccountService accountService = KapuaLocator.getInstance().getService(AccountService.class);
    private final AccountFactory accountFactory = KapuaLocator.getInstance().getFactory(AccountFactory.class);

    private final UserService userService = KapuaLocator.getInstance().getService(UserService.class);
    private final UserFactory userFactory = KapuaLocator.getInstance().getFactory(UserFactory.class);

    private final AccessInfoService accessInfoService = KapuaLocator.getInstance().getService(AccessInfoService.class);
    private final AccessInfoFactory accessInfoFactory = KapuaLocator.getInstance().getFactory(AccessInfoFactory.class);

    private final PermissionFactory permissionFactory = KapuaLocator.getInstance().getFactory(PermissionFactory.class);

    private final String claimName;
    private final Settings settings;

    /**
     * Create a new simple registration processor
     * 
     * @param claimName
     *            the claim to use as account name
     * @param settings
     *            the settings for the processor
     */
    public SimpleRegistrationProcessor(final String claimName, final Settings settings) {
        this.claimName = claimName;
        this.settings = settings;
    }

    @Override
    public Optional<User> createUser(final JwtContext context) throws Exception {
        final KapuaSession session = new KapuaSession(null, settings.getRootAccount(), settings.getRootAccount());

        final KapuaSession oldSession = KapuaSecurityUtils.getSession();
        KapuaSecurityUtils.setSession(session);
        try {
            return KapuaSecurityUtils.doPrivileged(() -> internalCreateUser(context));
        } finally {
            KapuaSecurityUtils.setSession(oldSession);
        }
    }

    private Optional<User> internalCreateUser(final JwtContext context) throws Exception {

        final String name = context.getJwtClaims().getClaimValue(claimName, String.class);
        if (name == null || name.isEmpty()) {
            return empty();
        }

        final String email = context.getJwtClaims().getClaimValue("email", String.class);
        if (email == null || email.isEmpty()) {
            return empty();
        }

        final String displayName = context.getJwtClaims().getClaimValue("name", String.class);

        final String subject = context.getJwtClaims().getSubject();

        // define account

        final AccountCreator accountCreator = accountFactory.newCreator(settings.getRootAccount(), name);
        accountCreator.setOrganizationEmail(email);
        accountCreator.setOrganizationName(name);

        // create account

        final Account account = accountService.create(accountCreator);

        // set the resource limits for the UserService of this account

        final Map<String, Object> values = new HashMap<>(2);
        customizeUserLimits(values);
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

        return Optional.of(user);
    }

    protected void customizeUserLimits(final Map<String, Object> values) {
        if (settings.getMaximumNumberOfChildUsers() < 0) {
            values.put("maxNumberChildEntities", settings.getMaximumNumberOfChildUsers());
            values.put("infiniteChildEntities", false);
        } else {
            values.put("maxNumberChildEntities", Integer.MAX_VALUE);
            values.put("infiniteChildEntities", true);
        }
    }

}
