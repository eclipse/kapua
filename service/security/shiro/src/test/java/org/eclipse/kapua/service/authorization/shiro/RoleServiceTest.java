package org.eclipse.kapua.service.authorization.shiro;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.role.shiro.RoleCreatorImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePredicates;
import org.eclipse.kapua.service.authorization.role.shiro.RoleQueryImpl;
import org.eclipse.kapua.test.KapuaTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoleServiceTest extends KapuaTest {

    public static String DEFAULT_FILTER = "athz_*.sql";
    public static String DROP_FILTER = "athz_*_drop.sql";

    KapuaEid scope = new KapuaEid(BigInteger.valueOf(random.nextLong()));

    // Database fixtures

    @BeforeClass
    public static void beforeClass() throws KapuaException {
        enableH2Connection();
        scriptSession(AuthorizationEntityManagerFactory.getInstance(), DEFAULT_FILTER);
    }

    @AfterClass
    public static void afterClass() throws KapuaException {
        scriptSession(AuthorizationEntityManagerFactory.getInstance(), DROP_FILTER);
    }

    // Tests

    @Test
    public void testCreate()
            throws Exception {

        KapuaSecurityUtils.doPriviledge(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);

            RolePermission rolePermissionCreator = new RolePermissionImpl(scope, "testDomain", Actions.read, scope);

            Set<RolePermission> rolePermissionsCreator = new HashSet<>();
            rolePermissionsCreator.add(rolePermissionCreator);

            RoleCreator roleCreator = new RoleCreatorImpl(scope);
            roleCreator.setName("test-" + new Date().getTime());
            roleCreator.setRolePermissions(rolePermissionsCreator);

            //
            // Create
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

            Set<RolePermission> rolePermissions = role.getRolePermissions();
            assertNotNull(rolePermissions);
            assertEquals(1, rolePermissions.size());

            RolePermission rolePermission = rolePermissions.iterator().next();
            assertNotNull(rolePermission);
            assertNotNull(rolePermission.getId());
            assertNotNull(rolePermission.getCreatedBy());
            assertNotNull(rolePermission.getCreatedOn());
            assertEquals(role.getId(), rolePermission.getRoleId());
            assertEquals(rolePermissionCreator.getPermission().getDomain(), rolePermission.getPermission().getDomain());
            assertEquals(rolePermissionCreator.getPermission().getAction(), rolePermission.getPermission().getAction());
            assertEquals(rolePermissionCreator.getPermission().getTargetScopeId(), rolePermission.getPermission().getTargetScopeId());

            return null;
        });
    }

    @Test
    public void testUpdate()
            throws Exception {

        KapuaSecurityUtils.doPriviledge(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);

            RolePermission rolePermissionCreator1 = new RolePermissionImpl(scope, "testDomain", Actions.read, scope);
            RolePermission rolePermissionCreator3 = new RolePermissionImpl(scope, "testDomain", Actions.delete, scope);

            Set<RolePermission> rolePermissionsCreator = new HashSet<>();
            rolePermissionsCreator.add(rolePermissionCreator1);
            rolePermissionsCreator.add(rolePermissionCreator3);

            RoleCreator roleCreator = new RoleCreatorImpl(scope);
            roleCreator.setName("test-" + new Date().getTime());
            roleCreator.setRolePermissions(rolePermissionsCreator);

            Role role = roleService.create(roleCreator);

            assertNotNull(role);
            assertEquals(roleCreator.getRolePermissions().size(), role.getRolePermissions().size());

            //
            // Update
            role.setName("updated-" + new Date().getTime());
            role.getRolePermissions().add(new RolePermissionImpl(scope, "testDomain", Actions.write, scope));
            role.getRolePermissions().add(new RolePermissionImpl(scope, "testDomain", Actions.read, scope));
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

            Set<RolePermission> rolePermissions = role.getRolePermissions();
            Set<RolePermission> rolePermissionsUpdated = roleUpdated1.getRolePermissions();
            assertFalse(rolePermissions == rolePermissionsUpdated);
            assertNotNull(rolePermissions);
            assertNotNull(rolePermissionsUpdated);
            assertEquals(rolePermissions.size(), rolePermissionsUpdated.size());

            for (RolePermission rolePermission : rolePermissions) {
                for (RolePermission rolePermissionUpdated : rolePermissionsUpdated) {

                    if (rolePermission.getPermission().equals(rolePermissionUpdated.getPermission())) {

                        // if (Actions.write.equals(rolePermission.getPermission().getAction())) {
                        // assertNotEquals(rolePermission.getCreatedBy(), rolePermissionUpdated.getCreatedBy());
                        // assertNotEquals(rolePermission.getCreatedOn(), rolePermissionUpdated.getCreatedOn());
                        // assertNotEquals(rolePermission.getRoleId(), rolePermissionUpdated.getRoleId());
                        //
                        // } else {
                        // assertEquals(rolePermission.getCreatedBy(), rolePermissionUpdated.getCreatedBy());
                        // assertEquals(rolePermission.getCreatedOn(), rolePermissionUpdated.getCreatedOn());
                        // assertEquals(rolePermission.getRoleId(), rolePermissionUpdated.getRoleId());
                        // }

                        assertEquals(rolePermission.getPermission().getDomain(), rolePermissionUpdated.getPermission().getDomain());
                        assertEquals(rolePermission.getPermission().getAction(), rolePermissionUpdated.getPermission().getAction());
                        assertEquals(rolePermission.getPermission().getTargetScopeId(), rolePermissionUpdated.getPermission().getTargetScopeId());
                    }
                }
            }

            //
            // Update delete permission
            roleUpdated1.getRolePermissions().remove(new RolePermissionImpl(scope, "testDomain", Actions.delete, scope));
            Role roleUpdated2 = roleService.update(roleUpdated1);

            //
            // Assert
            assertNotNull(roleUpdated1);
            assertEquals(roleUpdated1.getScopeId(), roleUpdated2.getScopeId());
            assertEquals(roleUpdated1.getScopeId(), roleUpdated2.getScopeId());
            assertEquals(roleUpdated1.getName(), roleUpdated2.getName());
            assertEquals(roleUpdated1.getCreatedBy(), roleUpdated2.getCreatedBy());
            assertEquals(roleUpdated1.getCreatedOn(), roleUpdated2.getCreatedOn());
            assertEquals(roleUpdated1.getModifiedBy(), roleUpdated2.getModifiedBy());
            // assertNotEquals(roleUpdated1.getModifiedOn(), roleUpdated2.getModifiedOn());

            assertEquals(roleUpdated1.getRolePermissions().size(), roleUpdated2.getRolePermissions().size());

            for (RolePermission rp : roleUpdated2.getRolePermissions()) {
                assertNotEquals(new RolePermissionImpl(scope, "testDomain", Actions.delete, scope), rp);
            }

            return null;
        });
    }

    @Test
    public void testFind()
            throws Exception {

        KapuaSecurityUtils.doPriviledge(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);

            RolePermission rolePermissionCreator = new RolePermissionImpl(scope, "testDomain", Actions.read, scope);

            Set<RolePermission> rolePermissionsCreator = new HashSet<>();
            rolePermissionsCreator.add(rolePermissionCreator);

            RoleCreator roleCreator = new RoleCreatorImpl(scope);
            roleCreator.setName("test-" + new Date().getTime());
            roleCreator.setRolePermissions(rolePermissionsCreator);
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

            Set<RolePermission> rolePermissions = role.getRolePermissions();
            Set<RolePermission> rolePermissionsFound = roleFound.getRolePermissions();
            assertFalse(rolePermissions == rolePermissionsFound);
            assertNotNull(rolePermissions);
            assertNotNull(rolePermissionsFound);
            assertEquals(rolePermissions.size(), rolePermissionsFound.size());

            RolePermission rolePermission = rolePermissions.iterator().next();
            RolePermission rolePermissionFound = rolePermissionsFound.iterator().next();
            assertFalse(rolePermission == rolePermissionFound);
            assertNotNull(rolePermission);
            assertNotNull(rolePermissionFound);
            assertEquals(rolePermission.getCreatedBy(), rolePermissionFound.getCreatedBy());
            assertEquals(rolePermission.getCreatedOn(), rolePermissionFound.getCreatedOn());
            assertEquals(rolePermission.getRoleId(), rolePermissionFound.getRoleId());
            assertEquals(rolePermission.getPermission().getDomain(), rolePermissionFound.getPermission().getDomain());
            assertEquals(rolePermission.getPermission().getAction(), rolePermissionFound.getPermission().getAction());
            assertEquals(rolePermission.getPermission().getTargetScopeId(), rolePermissionFound.getPermission().getTargetScopeId());

            return null;
        });
    }

    @Test
    public void testQueryAndCount()
            throws Exception {

        KapuaSecurityUtils.doPriviledge(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);

            RolePermission rolePermissionCreator = new RolePermissionImpl(scope, "testDomain", Actions.read, scope);

            Set<RolePermission> rolePermissionsCreator = new HashSet<>();
            rolePermissionsCreator.add(rolePermissionCreator);

            RoleCreator roleCreator = new RoleCreatorImpl(scope);
            roleCreator.setName("test-" + new Date().getTime());
            roleCreator.setRolePermissions(rolePermissionsCreator);

            Role role = roleService.create(roleCreator);

            assertNotNull(role);
            assertNotNull(role.getId());

            //
            // Query
            RoleQuery query = new RoleQueryImpl(scope);
            query.setPredicate(new AttributePredicate<String>(RolePredicates.ROLE_NAME, role.getName()));
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

            Set<RolePermission> rolePermissions = role.getRolePermissions();
            Set<RolePermission> rolePermissionsFound = roleFound.getRolePermissions();
            assertFalse(rolePermissions == rolePermissionsFound);
            assertNotNull(rolePermissions);
            assertNotNull(rolePermissionsFound);
            assertEquals(rolePermissions.size(), rolePermissionsFound.size());

            RolePermission rolePermission = rolePermissions.iterator().next();
            RolePermission rolePermissionFound = rolePermissionsFound.iterator().next();
            assertFalse(rolePermission == rolePermissionFound);
            assertNotNull(rolePermission);
            assertNotNull(rolePermissionFound);
            assertEquals(rolePermission.getCreatedBy(), rolePermissionFound.getCreatedBy());
            assertEquals(rolePermission.getCreatedOn(), rolePermissionFound.getCreatedOn());
            assertEquals(rolePermission.getRoleId(), rolePermissionFound.getRoleId());
            assertEquals(rolePermission.getPermission().getDomain(), rolePermissionFound.getPermission().getDomain());
            assertEquals(rolePermission.getPermission().getAction(), rolePermissionFound.getPermission().getAction());
            assertEquals(rolePermission.getPermission().getTargetScopeId(), rolePermissionFound.getPermission().getTargetScopeId());

            return null;
        });
    }

    @Test
    public void testDelete()
            throws Exception {

        KapuaSecurityUtils.doPriviledge(() -> {
            KapuaLocator locator = KapuaLocator.getInstance();
            RoleService roleService = locator.getService(RoleService.class);

            RolePermission rolePermissionCreator = new RolePermissionImpl(scope, "testDomain", Actions.read, scope);

            Set<RolePermission> rolePermissionsCreator = new HashSet<>();
            rolePermissionsCreator.add(rolePermissionCreator);

            RoleCreator roleCreator = new RoleCreatorImpl(scope);
            roleCreator.setName("test-" + new Date().getTime());
            roleCreator.setRolePermissions(rolePermissionsCreator);
            Role role = roleService.create(roleCreator);

            assertNotNull(role);
            assertNotNull(role.getId());
            assertEquals(roleCreator.getScopeId(), role.getScopeId());

            Role roleFound = roleService.find(scope, role.getId());
            assertNotNull(roleFound);

            //
            // Delete
            roleService.delete(scope, role.getId());

            //
            // Assert
            roleFound = roleService.find(scope, role.getId());
            assertNull(roleFound);
            return null;
        });
    }
}
