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
package org.eclipse.kapua.security.registration.simple;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.BrokerService;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.security.registration.RegistrationProcessor;
import org.eclipse.kapua.security.registration.simple.setting.SimpleSettingKeys;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoDomain;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.datastore.DatastoreService;
import org.eclipse.kapua.service.device.management.DeviceManagementService;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.UserType;
import org.jose4j.jwt.consumer.JwtContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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

        private int maximumNumberOfUsers;

        private int maximumNumberOfDevices;

        public Settings(KapuaId rootAccount) {
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
         * @param maximumNumberOfChildUsers The number of child users to allow
         */
        public void setMaximumNumberOfUsers(int maximumNumberOfChildUsers) {
            this.maximumNumberOfUsers = maximumNumberOfChildUsers;
        }

        public int getMaximumNumberOfUsers() {
            return maximumNumberOfUsers;
        }

        public void setMaximumNumberOfDevices(int maximumNumberOfDevices) {
            this.maximumNumberOfDevices = maximumNumberOfDevices;
        }

        public int getMaximumNumberOfDevices() {
            return maximumNumberOfDevices;
        }

        public static Optional<SimpleRegistrationProcessor.Settings> loadSimpleSettings(AbstractKapuaSetting<SimpleSettingKeys> settings) {
            try {
                String accountName = settings.getString(SimpleSettingKeys.SIMPLE_ROOT_ACCOUNT);
                if (accountName != null && !accountName.isEmpty()) {
                    return loadFrom(accountName).map(rootAccount -> applySimpleSettings(rootAccount, settings));
                }
                return Optional.empty();
            } catch (KapuaException e) {
                throw new RuntimeException("Failed to load root account ID", e);
            }
        }

        private static Optional<KapuaId> loadFrom(String accountName) throws KapuaException {
            User user = KapuaSecurityUtils.doPrivileged(() -> KapuaLocator.getInstance().getService(UserService.class).findByName(accountName));

            if (user != null) {
                return Optional.of(user).map(User::getScopeId);
            }
            logger.warn("Failed to load ID of '{}'. Entry not found.", accountName);
            return Optional.empty();
        }

        private static SimpleRegistrationProcessor.Settings applySimpleSettings(KapuaId rootAccount, AbstractKapuaSetting<SimpleSettingKeys> kapuaSettings) {
            Settings settings = new SimpleRegistrationProcessor.Settings(rootAccount);
            settings.setMaximumNumberOfUsers(kapuaSettings.getInt(SimpleSettingKeys.SIMPLE_MAX_NUMBER_OF_CHILD_USERS, 2));
            return settings;
        }

    }

    private final AccountService accountService = KapuaLocator.getInstance().getService(AccountService.class);
    private final AccountFactory accountFactory = KapuaLocator.getInstance().getFactory(AccountFactory.class);

    private final CredentialService credentialService = KapuaLocator.getInstance().getService(CredentialService.class);
    private final CredentialFactory credentialFactory = KapuaLocator.getInstance().getFactory(CredentialFactory.class);

    private final DeviceRegistryService deviceRegistryService = KapuaLocator.getInstance().getService(DeviceRegistryService.class);

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
     * @param claimName the claim to use as account name
     * @param settings  the settings for the processor
     */
    public SimpleRegistrationProcessor(String claimName, Settings settings) {
        this.claimName = claimName;
        this.settings = settings;
    }

    @Override
    public void close() {
    }

    @Override
    public Optional<User> createUser(JwtContext context) throws Exception {
        KapuaSession session = new KapuaSession(null, settings.getRootAccount(), settings.getRootAccount());

        KapuaSession oldSession = KapuaSecurityUtils.getSession();
        KapuaSecurityUtils.setSession(session);
        try {
            return KapuaSecurityUtils.doPrivileged(() -> internalCreateUser(context));
        } finally {
            KapuaSecurityUtils.setSession(oldSession);
        }
    }

    private Optional<User> internalCreateUser(JwtContext context) throws Exception {

        String name = context.getJwtClaims().getClaimValue(claimName, String.class);
        if (name == null || name.isEmpty()) {
            return Optional.empty();
        }

        String email = context.getJwtClaims().getClaimValue("email", String.class);
        if (email == null || email.isEmpty()) {
            return Optional.empty();
        }

        String displayName = context.getJwtClaims().getClaimValue("name", String.class);

        String subject = context.getJwtClaims().getSubject();

        // define account

        AccountCreator accountCreator = accountFactory.newCreator(settings.getRootAccount(), name);
        accountCreator.setOrganizationEmail(email);
        accountCreator.setOrganizationName(name);

        // create account

        Account account = accountService.create(accountCreator);

        // set the resource limits for the UserService of this account

        Map<String, Object> userValues = new HashMap<>(2);
        customizeUserLimits(userValues);
        userService.setConfigValues(account.getId(), account.getScopeId(), userValues);

        // set the resource limits for the UserService of this account

        Map<String, Object> deviceRegistryValues = new HashMap<>(2);
        customizeDeviceRegistryLimits(deviceRegistryValues);
        deviceRegistryService.setConfigValues(account.getId(), account.getScopeId(), deviceRegistryValues);

        // user user

        User user = createUser(name, email, displayName, subject, account);

        // user broker user

        createBrokerUser(name, account);

        // return result

        return Optional.of(user);
    }

    private User createUser(String name, String email, String displayName, String subject, Account account) throws KapuaException {

        // define

        UserCreator userCreator = userFactory.newCreator(account.getId(), name);
        userCreator.setUserType(UserType.EXTERNAL);
        userCreator.setExternalId(subject);
        userCreator.setEmail(email);
        userCreator.setDisplayName(displayName);

        // create

        User user = userService.create(userCreator);

        // assign login permissions

        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(user.getScopeId());
        accessInfoCreator.setUserId(user.getId());

        Set<Permission> permissions = new HashSet<>();
        permissions.add(permissionFactory.newPermission(new AccessInfoDomain(), Actions.read, user.getScopeId()));

        permissions.addAll(permissionFactory.newPermissions(AccountService.ACCOUNT_DOMAIN, user.getScopeId(), Actions.read));
        permissions.addAll(permissionFactory.newPermissions(CredentialService.CREDENTIAL_DOMAIN, user.getScopeId(), Actions.read, Actions.write, Actions.delete));
        permissions.addAll(permissionFactory.newPermissions(DatastoreService.DATASTORE_DOMAIN, user.getScopeId(), Actions.read));
        permissions.addAll(permissionFactory.newPermissions(DeviceRegistryService.DEVICE_DOMAIN, user.getScopeId(), Actions.read, Actions.write, Actions.delete));
        permissions.addAll(permissionFactory.newPermissions(DeviceConnectionService.DEVICE_CONNECTION_DOMAIN, user.getScopeId(), Actions.read));
        permissions.addAll(permissionFactory.newPermissions(DeviceEventService.DEVICE_EVENT_DOMAIN, user.getScopeId(), Actions.read, Actions.write));
        permissions.addAll(permissionFactory.newPermissions(DeviceManagementService.DEVICE_MANAGEMENT_DOMAIN, user.getScopeId(), Actions.read, Actions.write, Actions.execute));
        permissions.addAll(permissionFactory.newPermissions(GroupService.GROUP_DOMAIN, user.getScopeId(), Actions.read));
        permissions.addAll(permissionFactory.newPermissions(RoleService.ROLE_DOMAIN, user.getScopeId(), Actions.read));
        permissions.addAll(permissionFactory.newPermissions(UserService.USER_DOMAIN, user.getScopeId(), Actions.read));

        accessInfoCreator.setPermissions(permissions);

        accessInfoService.create(accessInfoCreator);

        // return result

        return user;
    }

    private User createBrokerUser(String baseName, Account account) throws KapuaException {

        // define

        UserCreator userCreator = userFactory.newCreator(account.getId(), baseName + "-broker");
        userCreator.setUserType(UserType.INTERNAL); // FIXME: need to find out why this isn't DEVICE but INTERNAL
        userCreator.setDisplayName("Gateway User");

        // create

        User user = userService.create(userCreator);

        // assign permissions

        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(user.getScopeId());
        accessInfoCreator.setUserId(user.getId());

        Set<Permission> permissions = new HashSet<>();
        permissions.add(permissionFactory.newPermission(BrokerService.BROKER_DOMAIN, Actions.connect, user.getScopeId()));

        accessInfoCreator.setPermissions(permissions);

        accessInfoService.create(accessInfoCreator);

        // Create default password

        CredentialCreator credential = credentialFactory.newCreator(account.getId(), user.getId(), CredentialType.PASSWORD, baseName + "-password", CredentialStatus.ENABLED, null);
        credentialService.create(credential);

        return user;
    }

    protected void customizeUserLimits(Map<String, Object> values) {
        Objects.requireNonNull(values);

        setChildEntityLimits(values, settings.getMaximumNumberOfUsers());
    }

    protected void customizeDeviceRegistryLimits(Map<String, Object> values) {
        Objects.requireNonNull(values);

        setChildEntityLimits(values, settings.getMaximumNumberOfDevices());
    }

    protected static void setChildEntityLimits(Map<String, Object> values, int limit) {
        Objects.requireNonNull(values);

        if (limit < 0) {
            values.put("maxNumberChildEntities", limit);
            values.put("infiniteChildEntities", false);
        } else {
            values.put("maxNumberChildEntities", Integer.MAX_VALUE);
            values.put("infiniteChildEntities", true);
        }
    }

}
