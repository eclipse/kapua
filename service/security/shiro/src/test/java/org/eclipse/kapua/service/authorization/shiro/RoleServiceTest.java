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
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.shiro;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.role.shiro.RoleCreatorImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePredicates;
import org.eclipse.kapua.service.authorization.role.shiro.RoleQueryImpl;
import org.eclipse.kapua.test.KapuaTest;
import org.eclipse.kapua.test.ResourceLimitsConfig;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoleServiceTest extends KapuaTest {

    public static final String DROP_FILTER = "athz_*_drop.sql";

    private static final Domain TEST_DOMAIN = new TestDomain();

    KapuaEid scope = new KapuaEid(IdGenerator.generate());

    // Database fixtures

    @BeforeClass
    public static void beforeClass() throws KapuaException {
        enableH2Connection();
    }

    @AfterClass
    public static void afterClass() throws KapuaException {
        // scriptSession(AuthorizationEntityManagerFactory.getInstance(), DROP_FILTER);
    }

    @Before
    public void before() {
        // Setup JAXB context
        XmlUtil.setContextProvider(new ShiroJAXBContextProvider());
    }

    // Tests

    @Test
    public void testCreate()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();

            // Create permission
            PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
            Permission permission = permissionFactory.newPermission(TEST_DOMAIN, Actions.read, scope);
            assertNotNull(permission);
            Set<Permission> permissions = new HashSet<>();
            permissions.add(permission);

            // Create role
            RoleCreator roleCreator = new RoleCreatorImpl(scope);
            roleCreator.setName("test-" + random.nextLong());
            roleCreator.setPermissions(permissions);

            //
            // Create
            RoleService roleService = locator.getService(RoleService.class);
            ResourceLimitsConfig resourceLimits = new ResourceLimitsConfig(scope.getId(), BigInteger.ONE);
            resourceLimits.addConfig("infiniteChildEntities", Boolean.TRUE);
            resourceLimits.addConfig("maxNumberChildEntities", Integer.valueOf(5));
            resourceLimits.setServiceConfig(roleService);
            Role role = roleService.create(roleCreator);

            //
            // Assert
            assertNotNull(role);
            assertNotNull(role.getId());
            assertEquals(roleCreator.getScopeId(), role.getScopeId());
            assertEquals(roleCreator.getName(), role.getName());
            assertNotNull(role.getCreatedBy());
            assertNotNull(role.getCreatedOn());
            assertNotNull(role.getModifiedBy());
            assertNotNull(role.getModifiedOn());

            RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
            RolePermissionListResult rolePermissionsListResult = rolePermissionService.findByRoleId(role.getScopeId(), role.getId());
            List<RolePermission> rolePermissions = rolePermissionsListResult.getItems();
            assertNotNull(rolePermissions);
            assertEquals(1, rolePermissions.size());

            RolePermission rolePermission = rolePermissions.iterator().next();
            assertNotNull(rolePermission);
            assertNotNull(rolePermission.getId());
            assertNotNull(rolePermission.getCreatedBy());
            assertNotNull(rolePermission.getCreatedOn());
            assertEquals(role.getId(), rolePermission.getRoleId());
            assertNotNull(rolePermission.getPermission());
            assertEquals(permission.getDomain(), rolePermission.getPermission().getDomain());
            assertEquals(permission.getAction(), rolePermission.getPermission().getAction());
            assertEquals(permission.getTargetScopeId(), rolePermission.getPermission().getTargetScopeId());

            return null;
        });
    }

    @Test
    public void testUpdate()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();

            // Create permission
            PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
            Permission permission1 = permissionFactory.newPermission(TEST_DOMAIN, Actions.read, scope);
            Permission permission3 = permissionFactory.newPermission(TEST_DOMAIN, Actions.delete, scope);

            Set<Permission> permissions = new HashSet<>();
            permissions.add(permission1);
            permissions.add(permission3);

            RoleCreator roleCreator = new RoleCreatorImpl(scope);
            roleCreator.setName("test-" + random.nextLong());
            roleCreator.setPermissions(permissions);

            RoleService roleService = locator.getService(RoleService.class);
            ResourceLimitsConfig resourceLimits = new ResourceLimitsConfig(scope.getId(), BigInteger.ONE);
            resourceLimits.addConfig("infiniteChildEntities", Boolean.TRUE);
            resourceLimits.addConfig("maxNumberChildEntities", Integer.valueOf(5));
            resourceLimits.setServiceConfig(roleService);
            Role role = roleService.create(roleCreator);

            RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
            RolePermissionListResult rolePermissionsListResult = rolePermissionService.findByRoleId(role.getScopeId(), role.getId());
            List<RolePermission> rolePermissions = rolePermissionsListResult.getItems();

            assertNotNull(role);
            assertEquals(roleCreator.getPermissions().size(), rolePermissions.size());

            //
            // Update
            role.setName("updated-" + random.nextLong());
            Thread.sleep(50); // Added some delay to make sure the modification time-stamps are really different
            Role roleUpdated1 = roleService.update(role);

            //
            // Assert
            assertNotNull(roleUpdated1);
            assertEquals(role.getScopeId(), roleUpdated1.getScopeId());
            assertEquals(role.getScopeId(), roleUpdated1.getScopeId());
            assertEquals(role.getName(), roleUpdated1.getName());
            assertEquals(role.getCreatedBy(), roleUpdated1.getCreatedBy());
            assertEquals(role.getCreatedOn(), roleUpdated1.getCreatedOn());
            assertEquals(role.getModifiedBy(), roleUpdated1.getModifiedBy());
            assertNotEquals(role.getModifiedOn(), roleUpdated1.getModifiedOn());

            return null;
        });
    }

    @Test
    public void testFind()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();

            // Create permission
            PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
            Permission permission = permissionFactory.newPermission(TEST_DOMAIN, Actions.read, scope);

            Set<Permission> permissions = new HashSet<>();
            permissions.add(permission);

            // Create Role
            RoleCreator roleCreator = new RoleCreatorImpl(scope);
            roleCreator.setName("test-" + random.nextLong());
            roleCreator.setPermissions(permissions);

            RoleService roleService = locator.getService(RoleService.class);
            ResourceLimitsConfig resourceLimits = new ResourceLimitsConfig(scope.getId(), BigInteger.ONE);
            resourceLimits.addConfig("infiniteChildEntities", Boolean.TRUE);
            resourceLimits.addConfig("maxNumberChildEntities", Integer.valueOf(5));
            resourceLimits.setServiceConfig(roleService);
            Role role = roleService.create(roleCreator);

            assertNotNull(role);
            assertNotNull(role.getId());
            assertEquals(roleCreator.getScopeId(), role.getScopeId());

            //
            // Find
            Role roleFound = roleService.find(scope, role.getId());

            //
            // Assert
            assertNotNull(roleFound);
            assertEquals(role.getScopeId(), roleFound.getScopeId());
            assertEquals(role.getScopeId(), roleFound.getScopeId());
            assertEquals(role.getName(), roleFound.getName());
            assertEquals(role.getCreatedBy(), roleFound.getCreatedBy());
            assertEquals(role.getCreatedOn(), roleFound.getCreatedOn());
            assertEquals(role.getModifiedBy(), roleFound.getModifiedBy());
            assertEquals(role.getModifiedOn(), roleFound.getModifiedOn());
            return null;
        });
    }

    @Test
    public void testQueryAndCount()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();

            // Create permission
            PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
            Permission permission = permissionFactory.newPermission(TEST_DOMAIN, Actions.read, scope);

            Set<Permission> permissions = new HashSet<>();
            permissions.add(permission);

            // Create role
            RoleCreator roleCreator = new RoleCreatorImpl(scope);
            roleCreator.setName("test-" + random.nextLong());
            roleCreator.setPermissions(permissions);

            RoleService roleService = locator.getService(RoleService.class);
            ResourceLimitsConfig resourceLimits = new ResourceLimitsConfig(scope.getId(), BigInteger.ONE);
            resourceLimits.addConfig("infiniteChildEntities", Boolean.TRUE);
            resourceLimits.addConfig("maxNumberChildEntities", Integer.valueOf(5));
            resourceLimits.setServiceConfig(roleService);
            Role role = roleService.create(roleCreator);

            assertNotNull(role);
            assertNotNull(role.getId());

            //
            // Query
            RoleQuery query = new RoleQueryImpl(scope);
            query.setPredicate(new AttributePredicate<String>(RolePredicates.NAME, role.getName()));
            RoleListResult rolesFound = roleService.query(query);
            long rolesCount = roleService.count(query);

            //
            // Assert
            assertNotNull(rolesFound);
            assertEquals(1, rolesCount);
            assertEquals(1, rolesFound.getSize());

            Role roleFound = rolesFound.getItem(0);
            assertNotNull(roleFound);
            assertEquals(role.getScopeId(), roleFound.getScopeId());
            assertEquals(role.getScopeId(), roleFound.getScopeId());
            assertEquals(role.getName(), roleFound.getName());
            assertEquals(role.getCreatedBy(), roleFound.getCreatedBy());
            assertEquals(role.getCreatedOn(), roleFound.getCreatedOn());
            assertEquals(role.getModifiedBy(), roleFound.getModifiedBy());
            assertEquals(role.getModifiedOn(), roleFound.getModifiedOn());

            RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
            RolePermissionListResult rolePermissionsListResult = rolePermissionService.findByRoleId(role.getScopeId(), role.getId());
            List<RolePermission> rolePermissions = rolePermissionsListResult.getItems();
            assertNotNull(rolePermissions);
            assertEquals(permissions.size(), rolePermissions.size());

            return null;
        });
    }

    @Test
    public void testDelete()
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();

            // Create permission
            PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
            Permission permission = permissionFactory.newPermission(TEST_DOMAIN, Actions.read, scope);

            Set<Permission> permissions = new HashSet<>();
            permissions.add(permission);

            // Create role
            RoleCreator roleCreator = new RoleCreatorImpl(scope);
            roleCreator.setName("test-" + random.nextLong());
            roleCreator.setPermissions(permissions);

            RoleService roleService = locator.getService(RoleService.class);
            ResourceLimitsConfig resourceLimits = new ResourceLimitsConfig(scope.getId(), BigInteger.ONE);
            resourceLimits.addConfig("infiniteChildEntities", Boolean.TRUE);
            resourceLimits.addConfig("maxNumberChildEntities", Integer.valueOf(5));
            resourceLimits.setServiceConfig(roleService);
            Role role = roleService.create(roleCreator);

            assertNotNull(role);
            assertNotNull(role.getId());
            assertEquals(roleCreator.getScopeId(), role.getScopeId());

            Role roleFound = roleService.find(scope, role.getId());
            assertNotNull(roleFound);

            RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
            RolePermissionListResult rolePermissionsListResult = rolePermissionService.findByRoleId(role.getScopeId(), role.getId());
            List<RolePermission> rolePermissions = rolePermissionsListResult.getItems();
            assertNotNull(rolePermissions);
            assertEquals(permissions.size(), rolePermissions.size());

            //
            // Delete
            roleService.delete(scope, role.getId());

            //
            // Assert
            roleFound = roleService.find(scope, role.getId());
            assertNull(roleFound);

            rolePermissionsListResult = rolePermissionService.findByRoleId(role.getScopeId(), role.getId());
            rolePermissions = rolePermissionsListResult.getItems();
            assertNotNull(rolePermissions);
            assertEquals(0, rolePermissions.size());

            return null;
        });
    }
}
