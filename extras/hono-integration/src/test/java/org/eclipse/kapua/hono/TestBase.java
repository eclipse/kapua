/*******************************************************************************
 * Copyright (c) 2018 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.hono;

import org.eclipse.kapua.broker.BrokerService;
import org.eclipse.kapua.broker.core.BrokerJAXBContextProvider;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.steps.DBHelper;
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
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.device.management.DeviceManagementService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.steps.PermissionData;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.UserFactoryImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class TestBase {

    private static DBHelper dbHelper = new DBHelper();

    private static AccountService accountService;
    private static UserService userService;
    private static CredentialService credentialService;
    private static DeviceRegistryService deviceRegistryService;
    private static AccessInfoService accessInfoService;

    protected TestBase() {}

    @BeforeClass
    public static void before() throws Exception {
        dbHelper.setup();

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();

            JAXBContextProvider brokerProvider = new BrokerJAXBContextProvider();
            XmlUtil.setContextProvider(brokerProvider);
            userService = locator.getService(UserService.class);

            Map<String, Object> valueMap = new HashMap();
            valueMap.put("infiniteChildEntities", true);
            valueMap.put("maxNumberChildEntities", 5);
            valueMap.put("lockoutPolicy.enabled", false);
            valueMap.put("lockoutPolicy.maxFailures", 3);
            valueMap.put("lockoutPolicy.resetAfter", 300);
            valueMap.put("lockoutPolicy.lockDuration", 3);

            accountService = locator.getService(AccountService.class);
            deviceRegistryService = locator.getService(DeviceRegistryService.class);
            credentialService = locator.getService(CredentialService.class);
            deviceRegistryService = locator.getService(DeviceRegistryService.class);
            accessInfoService = locator.getService(AccessInfoService.class);

            // Create account
            AccountFactory accountFactory = locator.getFactory(AccountFactory.class);
            AccountCreator accountCreator = accountFactory.newCreator(KapuaId.ONE, "test-tenant");
            accountCreator.setOrganizationName("Test Tenant");
            accountCreator.setOrganizationEmail("tenant@example.com");
            Account account = accountService.create(accountCreator);

            userService.setConfigValues(account.getId(), account.getScopeId(), valueMap);

            // Create user
            UserCreator userCreator = new UserFactoryImpl().newCreator(account.getId(), "test-user");
            Properties userProps = new Properties();
            userProps.setProperty("client-id", "my-device");
            userCreator.setEntityAttributes(userProps);
            User user = userService.create(userCreator);

            // Create credentials
            CredentialCreator credentialCreator;
            credentialCreator = new CredentialFactoryImpl().newCreator(
                    account.getId(),
                    user.getId(),
                    CredentialType.PASSWORD,
                    "verysecret",
                    CredentialStatus.ENABLED,
                    null);
            credentialService.create(credentialCreator);

            // Permissions
            List<PermissionData> permissionList = new ArrayList<>();
            permissionList.add(new PermissionData(BrokerService.BROKER_DOMAIN, Actions.connect, (KapuaEid) user.getScopeId()));
            permissionList.add(new PermissionData(BrokerService.BROKER_DOMAIN, Actions.write, (KapuaEid) user.getScopeId()));
            permissionList.add(new PermissionData(BrokerService.BROKER_DOMAIN, Actions.read, (KapuaEid) user.getScopeId()));
            permissionList.add(new PermissionData(BrokerService.BROKER_DOMAIN, Actions.delete, (KapuaEid) user.getScopeId()));

            permissionList.add(new PermissionData(DeviceManagementService.DEVICE_MANAGEMENT_DOMAIN, Actions.write, (KapuaEid) user.getScopeId()));

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
                Permission permission = permissionFactory.newPermission(domain, action, targetScopeId);
                permissions.add(permission);
            }
            accessInfoCreator.setPermissions(permissions);

            accessInfoService.create(accessInfoCreator);

            // Create device

            DeviceFactory deviceFactory = locator.getFactory(DeviceFactory.class);
            DeviceCreator deviceCreator = deviceFactory.newCreator(account.getScopeId(), "test-device");
            deviceCreator.setDisplayName("Test Device");
            Device device = deviceRegistryService.create(deviceCreator);

        });

    }

    @AfterClass
    public static void after() throws Exception {
        dbHelper.deleteAll();
    }
}
