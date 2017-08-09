/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.BrokerDomain;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialFactoryImpl;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoFactoryImpl;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.datastore.DatastoreDomain;
import org.eclipse.kapua.service.device.management.commons.DeviceManagementDomain;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionDomain;
import org.eclipse.kapua.service.device.registry.internal.DeviceDomain;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.UserFactoryImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Creator of Accounts, Users, Permissions that are used in ACL tests
 */
public class AclCreator {

    private static final KapuaId SYS_ID = new KapuaEid(BigInteger.ONE);

    private static final KapuaId ROOT_SCOPE_ID = new KapuaEid(BigInteger.ONE);

    /**
     * Credential service.
     */
    private CredentialService credentialService;

    /**
     * User service.
     */
    private UserService userService;

    /**
     * Accessinfo service.
     */
    private AccessInfoService accessInfoService;

    /**
     * Account service.
     */
    private AccountService accountService;

    /**
     * Account factory.
     */
    private AccountFactory accountFactory;

    /**
     * Constructor with all support services.
     *
     * @param accountService
     * @param accountFactory
     * @param userService
     * @param accessInfoService
     * @param credentialService
     */
    public AclCreator(AccountService accountService,
            AccountFactory accountFactory,
            UserService userService,
            AccessInfoService accessInfoService,
            CredentialService credentialService) {
        this.accountService = accountService;
        this.accountFactory = accountFactory;
        this.userService = userService;
        this.accessInfoService = accessInfoService;
        this.credentialService = credentialService;
    }

    /**
     * Configure user service with reasonable default values.
     *
     * @param accId   account id
     * @param scopeId scope id
     */
    private void configureUserService(KapuaId accId, KapuaId scopeId) {

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("infiniteChildEntities", true);
        valueMap.put("maxNumberChildEntities", 5);
        valueMap.put("lockoutPolicy.enabled", false);
        valueMap.put("lockoutPolicy.maxFailures", 3);
        valueMap.put("lockoutPolicy.resetAfter", 300);
        valueMap.put("lockoutPolicy.lockDuration", 3);

        try {
            userService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Configure account service with reasonable default values.
     *
     * @param accId   account id
     * @param scopeId scope id
     */
    private void configureAccountService(KapuaId accId, KapuaId scopeId) {

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("infiniteChildEntities", true);
        valueMap.put("maxNumberChildEntities", 5);

        try {
            userService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates permissions for user with specified account. Permissions are created in privileged mode.
     *
     * @param permissionList list of permissions for user, if targetScopeId is not set user scope that is
     *                       specified as account
     * @param user           user for whom permissions are set
     * @param account        account in which user is defined
     * @throws Exception
     */
    private void createPermissions(List<PermissionData> permissionList, User user, Account account)
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            try {
                accessInfoService.create(accessInfoCreatorCreator(permissionList, user, account));
            } catch (KapuaException ke) {
                ke.printStackTrace();
                //skip
            }

            return null;
        });
    }

    /**
     * Create accessInfoCreator instance with data about user permissions.
     * If target scope is not defined in permission list use account scope.
     *
     * @param permissionList list of all permissions
     * @param user           user for which permissions are set
     * @param account        that user belongs to
     * @return AccessInfoCreator instance for creating user permissions
     */
    private AccessInfoCreator accessInfoCreatorCreator(List<PermissionData> permissionList,
            User user, Account account) {

        PermissionFactory permissionFactory = new PermissionFactoryImpl();
        AccessInfoCreator accessInfoCreator = new AccessInfoFactoryImpl().newCreator(account.getId());
        accessInfoCreator.setUserId(user.getId());
        accessInfoCreator.setScopeId(user.getScopeId());
        Set<Permission> permissions = new HashSet<>();
        for (PermissionData permissionData : permissionList) {
            Actions action = permissionData.getAction();
            KapuaEid targetScopeId = permissionData.getTargetScopeId();
            if (targetScopeId == null) {
                targetScopeId = (KapuaEid) account.getId();
            }
            Domain domain = permissionData.getDomain();
            Permission permission = permissionFactory.newPermission(domain,
                    action, targetScopeId);
            permissions.add(permission);
        }
        accessInfoCreator.setPermissions(permissions);

        return accessInfoCreator;
    }

    public void attachUserCredentials(Account account, User user) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            CredentialCreator credentialCreator;
            credentialCreator = new CredentialFactoryImpl().newCreator(account.getId(), user.getId(), CredentialType.PASSWORD, "kapua-password", CredentialStatus.ENABLED, null);
            try {
                credentialService.create(credentialCreator);
            } catch (KapuaException ke) {
                // skip
            }

            return null;
        });
    }

    public void attachUserCredentials(Account account, User user, String password) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            CredentialCreator credentialCreator;
            credentialCreator = new CredentialFactoryImpl().newCreator(account.getId(), user.getId(), CredentialType.PASSWORD, password, CredentialStatus.ENABLED, null);
            try {
                credentialService.create(credentialCreator);
            } catch (KapuaException ke) {
                // skip
            }

            return null;
        });
    }

    public User createUser(Account account, String name) throws KapuaException {
        configureUserService(account.getId(), SYS_ID);
        UserCreator userCreator = new UserFactoryImpl().newCreator(account.getId(), name);
        return userService.create(userCreator);
    }

    Account createAccount(String name, String orgName, String orgEmail) throws KapuaException {
        configureAccountService(ROOT_SCOPE_ID, SYS_ID);
        AccountCreator accountCreator = accountFactory.newCreator(ROOT_SCOPE_ID, name);
        accountCreator.setOrganizationName(orgName);
        accountCreator.setOrganizationEmail(orgEmail);
        return accountService.create(accountCreator);
    }

    void attachBrokerPermissions(Account account, User user) throws Exception {
        List<PermissionData> permissionList = new ArrayList<>();
        permissionList.add(new PermissionData(new BrokerDomain(), Actions.connect, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new BrokerDomain(), Actions.write, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new BrokerDomain(), Actions.read, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new BrokerDomain(), Actions.delete, (KapuaEid) user.getScopeId()));
        createPermissions(permissionList, user, account);
    }

    void attachDevicePermissions(Account account, User user) throws Exception {
        List<PermissionData> permissionList = new ArrayList<>();
        permissionList.add(new PermissionData(new DeviceManagementDomain(), Actions.write, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new BrokerDomain(), Actions.connect, (KapuaEid) user.getScopeId()));
        createPermissions(permissionList, user, account);
    }

    void attachDataViewPermissions(Account account, User user) throws Exception {
        List<PermissionData> permissionList = new ArrayList<>();
        permissionList.add(new PermissionData(new DatastoreDomain(), Actions.read, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new BrokerDomain(), Actions.connect, (KapuaEid) user.getScopeId()));
        createPermissions(permissionList, user, account);
    }

    void attachDataManagePermissions(Account account, User user) throws Exception {
        List<PermissionData> permissionList = new ArrayList<>();
        permissionList.add(new PermissionData(new DatastoreDomain(), Actions.write, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new BrokerDomain(), Actions.connect, (KapuaEid) user.getScopeId()));
        createPermissions(permissionList, user, account);
    }

    public void attachFullPermissions(Account account, User user) throws Exception {
        List<PermissionData> permissionList = new ArrayList<>();
        permissionList.add(new PermissionData(new BrokerDomain(), Actions.connect, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new BrokerDomain(), Actions.write, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new BrokerDomain(), Actions.read, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new BrokerDomain(), Actions.delete, (KapuaEid) user.getScopeId()));

        permissionList.add(new PermissionData(new DeviceConnectionDomain(), Actions.read, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new DeviceConnectionDomain(), Actions.write, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new DeviceConnectionDomain(), Actions.delete, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new DeviceConnectionDomain(), Actions.connect, (KapuaEid) user.getScopeId()));

        permissionList.add(new PermissionData(new DeviceDomain(), Actions.read, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new DeviceDomain(), Actions.write, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new DeviceDomain(), Actions.delete, (KapuaEid) user.getScopeId()));
        permissionList.add(new PermissionData(new DeviceDomain(), Actions.connect, (KapuaEid) user.getScopeId()));

        createPermissions(permissionList, user, account);
    }
}
