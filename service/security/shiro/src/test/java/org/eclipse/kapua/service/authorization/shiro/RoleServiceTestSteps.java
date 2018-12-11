/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleAttributes;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionQuery;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.role.shiro.RoleFactoryImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionServiceImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RoleServiceImpl;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of Gherkin steps used in DomainRegistryService.feature scenarios.
 */
@ScenarioScoped
public class RoleServiceTestSteps extends AbstractAuthorizationServiceTest {

    private static final TestDomain TEST_DOMAIN = new TestDomain();

    // Various Role related service references
    RoleCreator roleCreator;
    RoleService roleService;
    RoleFactory roleFactory;
    RolePermissionCreator rolePermissionCreator;
    RolePermissionService rolePermissionService;
    RolePermissionFactory rolePermissionFactory;
    PermissionFactory permissionFactory;

    // Currently executing scenario.
    Scenario scenario;

    // Test data scratchpads
    CommonTestData commonData;
    RoleServiceTestData roleData;

    @Inject
    public RoleServiceTestSteps(RoleServiceTestData roleData, CommonTestData commonData) {
        this.roleData = roleData;
        this.commonData = commonData;
    }

    // Setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario)
            throws Exception {
        this.scenario = scenario;

        // Instantiate all the services and factories that are required by the tests
        roleService = new RoleServiceImpl();
        roleFactory = new RoleFactoryImpl();
        rolePermissionService = new RolePermissionServiceImpl();
        rolePermissionFactory = new RolePermissionFactoryImpl();
        permissionFactory = new PermissionFactoryImpl();

        // Clean up the database. A clean slate is needed for truly independent
        // test case executions!
        dropDatabase();
        setupDatabase();

        // Clean up the test data scratchpads
        commonData.clearData();
        commonData.scenario = this.scenario;
        roleData.clearData();
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************
    @When("^I configure role$")
    public void setConfigurationValue(List<CucConfig> cucConfigs)
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            Map<String, Object> valueMap = new HashMap<>();
            KapuaEid scopeId = null;
            KapuaEid parentScopeId = null;

            for (CucConfig config : cucConfigs) {
                config.addConfigToMap(valueMap);
                scopeId = new KapuaEid(BigInteger.valueOf(Long.valueOf(config.getScopeId())));
                parentScopeId = new KapuaEid(BigInteger.valueOf(Long.valueOf(config.getParentScopeId())));
            }
            try {
                commonData.primeException();
                roleService.setConfigValues(scopeId, parentScopeId, valueMap);
            } catch (KapuaException ex) {
                commonData.verifyException(ex);
            }

            return null;
        });
    }

    @Given("^I create the following role(?:|s)$")
    public void createAListOfRoles(List<CucRole> roles)
            throws Exception {

        commonData.primeException();
        for (CucRole tmpRole : roles) {
            tmpRole.doParse();
            roleData.permissions = new HashSet<>();
            if ((tmpRole.getActions() != null) && (tmpRole.getActions().size() > 0)) {
                for (Actions tmpAct : tmpRole.getActions()) {
                    roleData.permissions.add(permissionFactory.newPermission(TEST_DOMAIN, tmpAct, tmpRole.getScopeId()));
                }
            }
            roleCreator = roleFactory.newCreator(tmpRole.getScopeId());
            roleCreator.setName(tmpRole.getName());
            roleCreator.setPermissions(roleData.permissions);
            KapuaSecurityUtils.doPrivileged(() -> {
                try {
                    roleData.role = roleService.create(roleCreator);
                } catch (KapuaException ex) {
                    commonData.verifyException(ex);
                }
                return null;
            });

            if (commonData.exceptionCaught) {
                break;
            }
        }
    }

    @Given("^I create the following role permission(?:|s)$")
    public void createAListOfRolePermissions(List<CucRolePermission> perms)
            throws Exception {

        assertNotNull(roleData.role);
        assertNotNull(roleData.role.getId());
        assertNotNull(perms);
        assertFalse(perms.isEmpty());

        TestDomain tmpDom = new TestDomain();
        tmpDom.setName("test_domain");

        commonData.primeException();
        for (CucRolePermission tmpCPerm : perms) {
            tmpCPerm.doParse();
            assertNotNull(tmpCPerm.getScopeId());
            assertNotNull(tmpCPerm.getAction());

            tmpDom.setScopeId(tmpCPerm.getScopeId());

            rolePermissionCreator = rolePermissionFactory.newCreator(tmpCPerm.getScopeId());
            rolePermissionCreator.setRoleId(roleData.role.getId());
            rolePermissionCreator.setPermission(permissionFactory.newPermission(tmpDom, tmpCPerm.getAction(), tmpCPerm.getTargetScopeId()));

            KapuaSecurityUtils.doPrivileged(() -> {
                try {
                    roleData.rolePermission = rolePermissionService.create(rolePermissionCreator);
                } catch (KapuaException ex) {
                    commonData.verifyException(ex);
                    return null;
                }
                return null;
            });
        }
    }

    @When("^I update the last created role name to \"(.+)\"$")
    public void updateRoleNameTo(String name)
            throws Exception {
        roleData.role.setName(name);
        Thread.sleep(100);
        KapuaSecurityUtils.doPrivileged(() -> {
            try {
                commonData.primeException();
                roleService.update(roleData.role);
            } catch (KapuaException ex) {
                commonData.verifyException(ex);
            }
            return null;
        });
    }

    @When("^I examine the permissions for the last role$")
    public void findPermissionsForTheLastCreatedRole()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            roleData.permissionList = rolePermissionService.findByRoleId(
                    roleData.role.getScopeId(), roleData.role.getId());
            return null;
        });
    }

    @When("^I search for the last created role$")
    public void findLastCreatedRole()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            roleData.roleFound = roleService.find(
                    roleData.role.getScopeId(), roleData.role.getId());
            return null;
        });
    }

    @When("^I search for the last created role permission$")
    public void findLastCreatedRolePermission()
            throws KapuaException {
        roleData.rolePermissionFound = null;
        KapuaSecurityUtils.doPrivileged(() -> {
            roleData.rolePermissionFound = rolePermissionService.find(
                    roleData.rolePermission.getScopeId(), roleData.rolePermission.getId());
            return null;
        });
    }

    @When("^I search for a random role ID$")
    public void searchForRandomId()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            roleData.roleFound = roleService.find(ROOT_SCOPE_ID, generateId());
            return null;
        });
    }

    @When("^I delete the last created role$")
    public void deleteLastCreatedRole()
            throws Exception {
        KapuaSecurityUtils.doPrivileged(() -> {
            try {
                commonData.primeException();
                roleService.delete(roleData.role.getScopeId(), roleData.role.getId());
            } catch (KapuaException ex) {
                commonData.verifyException(ex);
            }
            return null;
        });
    }

    @When("^I delete the last created role permission$")
    public void deleteLastCreatedRolePermission()
            throws Exception {
        KapuaSecurityUtils.doPrivileged(() -> {
            try {
                commonData.primeException();
                rolePermissionService.delete(
                        roleData.rolePermission.getScopeId(), roleData.rolePermission.getId());
            } catch (KapuaException ex) {
                commonData.verifyException(ex);
            }
            return null;
        });
    }

    @When("^I count the roles in scope (\\d+)$")
    public void countRolesInScope(Integer scope)
            throws KapuaException {
        assertNotNull(scope);
        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(scope));
        assertNotNull(tmpId);

        RoleQuery tmpQuery = roleFactory.newQuery(tmpId);
        KapuaSecurityUtils.doPrivileged(() -> {
            commonData.count = roleService.count(tmpQuery);
            return null;
        });
    }

    @When("^I count the permission in scope (\\d+)$")
    public void countRolePermissionsInScope(Integer scope)
            throws KapuaException {
        assertNotNull(scope);
        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(scope));
        assertNotNull(tmpId);

        RolePermissionQuery tmpQuery = rolePermissionFactory.newQuery(tmpId);
        KapuaSecurityUtils.doPrivileged(() -> {
            commonData.count = rolePermissionService.count(tmpQuery);
            return null;
        });
    }

    @When("^I query for the role \"(.+)\" in scope (\\d+)$")
    public void queryForRoleInScope(String name, Integer scope)
            throws KapuaException {
        assertNotNull(scope);
        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(scope));
        assertNotNull(tmpId);
        assertNotNull(name);
        assertNotEquals(0, name.length());

        RoleQuery tmpQuery = roleFactory.newQuery(tmpId);
        tmpQuery.setPredicate(tmpQuery.attributePredicate(RoleAttributes.NAME, name));
        KapuaSecurityUtils.doPrivileged(() -> {
            roleData.roleList = roleService.query(tmpQuery);
            return null;
        });

        assertNotNull(roleData.roleList);
        if (roleData.roleList.getSize() > 1) {
            fail("Query returned too many results!");
        }

        if (!roleData.roleList.isEmpty()) {
            roleData.roleFound = roleData.roleList.getFirstItem();
        }

        commonData.count = roleData.roleList.getSize();
    }

    @Then("^I find no roles$")
    public void chackThatNothingWasFound() {
        assertNull(roleData.roleFound);
    }

    @Then("^I find no permissions$")
    public void checkThatNoPermissionWasFound() {
        assertNull(roleData.rolePermissionFound);
    }

    @Then("^The role matches the creator$")
    public void checkLastRoleAgainstCreator()
            throws KapuaException {

        assertNotNull(roleData.role);
        assertNotNull(roleCreator);
        assertEquals(roleCreator.getScopeId(), roleData.role.getScopeId());
        assertEquals(roleCreator.getName(), roleData.role.getName());
        assertNotNull(roleData.role.getCreatedBy());
        assertNotNull(roleData.role.getCreatedOn());
        assertNotNull(roleData.role.getModifiedBy());
        assertNotNull(roleData.role.getModifiedOn());
    }

    @Then("^The permissions match$")
    public void checkPermissionsAgainstRole() {

        boolean found = false;
        assertNotNull(roleData.permissions);
        assertNotNull(roleData.permissionList);
        assertEquals(roleData.permissions.size(), roleData.permissionList.getSize());
        if (roleData.permissions.size() > 0) {
            for (RolePermission tmpRolePerm : roleData.permissionList.getItems()) {
                found = false;
                for (Permission tmpPerm : roleData.permissions) {
                    if (tmpPerm.getAction() == tmpRolePerm.getPermission().getAction()) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found);
            }
        }
    }

    @Then("^I find the following actions$")
    public void checkPermissionsAgainstList(List<CucRole> roles) {

        assertNotNull(roles);
        assertEquals(1, roles.size());
        CucRole tmpRole = roles.get(0);
        assertNotNull(tmpRole);
        tmpRole.doParse();
        Set<Actions> actLst = tmpRole.getActions();
        assertNotNull(actLst);
        assertNotNull(roleData.permissionList);
        assertNotEquals(0, roleData.permissionList.getSize());
        assertEquals(actLst.size(), roleData.permissionList.getSize());
        for (RolePermission tmpPerm : roleData.permissionList.getItems()) {
            assertTrue(actLst.contains(tmpPerm.getPermission().getAction()));
        }
    }

    @Then("^The correct role entry was found$")
    public void verifyThatRolesMatch() {
        assertNotNull(roleData.role);
        assertNotNull(roleData.roleFound);
        assertEquals(roleData.role.getScopeId(), roleData.roleFound.getScopeId());
        assertEquals(roleData.role.getScopeId(), roleData.roleFound.getScopeId());
        assertEquals(roleData.role.getName(), roleData.roleFound.getName());
        assertEquals(roleData.role.getCreatedBy(), roleData.roleFound.getCreatedBy());
        assertEquals(roleData.role.getCreatedOn(), roleData.roleFound.getCreatedOn());
        assertEquals(roleData.role.getModifiedBy(), roleData.roleFound.getModifiedBy());
        assertEquals(roleData.role.getModifiedOn(), roleData.roleFound.getModifiedOn());
    }

    @Then("^The correct role permission entry was found$")
    public void verifyThatRolePermissionsMatch() {
        assertNotNull(roleData.rolePermission);
        assertNotNull(roleData.rolePermissionFound);
        assertEquals(roleData.rolePermission.getScopeId(), roleData.rolePermissionFound.getScopeId());
        assertEquals(roleData.rolePermission.getScopeId(), roleData.rolePermissionFound.getScopeId());
        assertEquals(roleData.rolePermission.getCreatedBy(), roleData.rolePermissionFound.getCreatedBy());
        assertEquals(roleData.rolePermission.getCreatedOn(), roleData.rolePermissionFound.getCreatedOn());
    }

    @Then("^The role was successfully updated$")
    public void checkRoleForUpdates() {
        assertNotNull(roleData.role);
        assertNotNull(roleData.roleFound);
        assertEquals(roleData.role.getScopeId(), roleData.roleFound.getScopeId());
        assertEquals(roleData.role.getScopeId(), roleData.roleFound.getScopeId());
        assertEquals(roleData.role.getName(), roleData.roleFound.getName());
        assertEquals(roleData.role.getCreatedBy(), roleData.roleFound.getCreatedBy());
        assertEquals(roleData.role.getCreatedOn(), roleData.roleFound.getCreatedOn());
        assertEquals(roleData.role.getModifiedBy(), roleData.roleFound.getModifiedBy());
        assertNotEquals(roleData.role.getModifiedOn(), roleData.roleFound.getModifiedOn());
    }

    @Then("^The role factory returns sane results$")
    public void performRoleFactorySanityChecks() {
        assertNotNull(roleFactory.newEntity(ROOT_SCOPE_ID));
        assertNotNull(roleFactory.newCreator(ROOT_SCOPE_ID));
        assertNotNull(roleFactory.newQuery(ROOT_SCOPE_ID));
        assertNotNull(roleFactory.newListResult());
        assertNotNull(roleFactory.newRolePermission());
    }

    @Then("^The role permission factory returns sane results$")
    public void performRolePermissionFactorySanityChecks() {
        assertNotNull(rolePermissionFactory.newEntity(ROOT_SCOPE_ID));
        assertNotNull(rolePermissionFactory.newCreator(ROOT_SCOPE_ID));
        assertNotNull(rolePermissionFactory.newQuery(ROOT_SCOPE_ID));
        assertNotNull(rolePermissionFactory.newListResult());
    }

    @Then("^The role comparator does its job$")
    public void checkRoleEqualityMethod() {
        Role role1 = roleFactory.newEntity(ROOT_SCOPE_ID);
        Role role2 = roleFactory.newEntity(ROOT_SCOPE_ID);
        Integer miscObj = 10;

        assertNotNull(role1);
        assertNotNull(role2);
        assertNotNull(miscObj);

        assertTrue(role1.equals(role1));
        assertFalse(role1.equals(null));
        assertFalse(role1.equals(miscObj));
        assertTrue(role1.equals(role2));
        role2.setName("test_name_2");
        assertFalse(role1.equals(role2));
        role1.setName("test_name_1");
        assertFalse(role1.equals(role2));
        role2.setName("test_name_1");
        assertTrue(role1.equals(role2));
    }

    @Then("^The role permission comparator does its job$")
    public void checkRolePermissionEqualityMethod() {
        RolePermission perm1 = rolePermissionFactory.newEntity(ROOT_SCOPE_ID);
        RolePermission perm2 = rolePermissionFactory.newEntity(ROOT_SCOPE_ID);
        Integer miscObj = 1;
        Permission tmpPermission1 = permissionFactory.newPermission(TEST_DOMAIN, Actions.read, ROOT_SCOPE_ID);
        Permission tmpPermission2 = permissionFactory.newPermission(TEST_DOMAIN, Actions.write, ROOT_SCOPE_ID);
        KapuaId tmpRoleId1 = generateId();
        KapuaId tmpRoleId2 = generateId();

        assertNotNull(perm1);
        assertNotNull(perm2);
        assertTrue(perm1.equals(perm1));
        assertFalse(perm1.equals(null));
        assertFalse(perm1.equals(miscObj));
        assertTrue(perm1.equals(perm2));

        perm2.setPermission(tmpPermission2);
        assertFalse(perm1.equals(perm2));

        perm1.setPermission(tmpPermission1);
        assertFalse(perm1.equals(perm2));

        perm2.setPermission(tmpPermission1);
        assertTrue(perm1.equals(perm2));

        perm2.setRoleId(tmpRoleId1);
        assertFalse(perm1.equals(perm2));
        perm1.setRoleId(tmpRoleId1);
        assertTrue(perm1.equals(perm2));
        perm2.setRoleId(tmpRoleId2);
        assertFalse(perm1.equals(perm2));
    }

    @Then("^The role permission object constructors are sane$")
    public void checkRolePermissionConstructors() {

        Permission tmpPermission = permissionFactory.newPermission(TEST_DOMAIN, Actions.read, ROOT_SCOPE_ID);
        KapuaId tmpRoleId = generateId();

        assertNotNull(tmpRoleId);
        RolePermission perm1 = new RolePermissionImpl(ROOT_SCOPE_ID);
        assertNotNull(perm1);
        RolePermission perm2 = new RolePermissionImpl(perm1);
        assertNotNull(perm2);
        assertTrue(perm1.equals(perm2));
        RolePermission perm3 = new RolePermissionImpl(ROOT_SCOPE_ID, tmpPermission);
        assertNotNull(perm3);
        assertEquals(perm3.getPermission(), tmpPermission);
        perm1.setRoleId(tmpRoleId);
        assertEquals(tmpRoleId, perm1.getRoleId());
        perm1.setPermission(tmpPermission);
        assertEquals(perm1.getPermission(), tmpPermission);
    }
}
