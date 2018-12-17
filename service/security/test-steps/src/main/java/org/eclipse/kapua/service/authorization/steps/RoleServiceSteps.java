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
package org.eclipse.kapua.service.authorization.steps;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.common.CucConfig;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authorization.AuthorizationJAXBContextProvider;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleAttributes;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionQuery;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.role.shiro.RoleFactoryImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionServiceImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RoleServiceImpl;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.test.MockedLocator;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of Gherkin steps used in DomainRegistryService.feature scenarios.
 */
@ScenarioScoped
public class RoleServiceSteps extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(AccessInfoServiceSteps.class);
    private static final TestDomain TEST_DOMAIN = new TestDomain();

    // Various Role related service references
    private RoleService roleService;
    private RoleFactory roleFactory;
    private RolePermissionService rolePermissionService;
    private RolePermissionFactory rolePermissionFactory;
    private PermissionFactory permissionFactory;

    @Inject
    public RoleServiceSteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
        this.database = dbHelper;
    }

    /**
     * Setup DI with Google Guice DI.
     * Create mocked and non mocked service under test and bind them with Guice.
     * It is based on custom MockedLocator locator that is meant for sevice unit tests.
     */
    private void setupDI() {

        MockedLocator mockedLocator = (MockedLocator) KapuaLocator.getInstance();

        AbstractModule module = new AbstractModule() {

            @Override
            protected void configure() {

                // Inject mocked Authorization Service method checkPermission
                AuthorizationService mockedAuthorization = Mockito.mock(AuthorizationService.class);
                try {
                    Mockito.doNothing().when(mockedAuthorization).checkPermission(Matchers.any(Permission.class));
                } catch (KapuaException e) {
                    // skip
                }
                bind(AuthorizationService.class).toInstance(mockedAuthorization);
                // Inject mocked Permission Factory
                bind(PermissionFactory.class).toInstance(Mockito.mock(PermissionFactory.class));
                // Set KapuaMetatypeFactory for Metatype configuration
                bind(KapuaMetatypeFactory.class).toInstance(new KapuaMetatypeFactoryImpl());

                // Inject actual account related services
                bind(AuthorizationEntityManagerFactory.class).toInstance(AuthorizationEntityManagerFactory.getInstance());
                bind(RoleService.class).toInstance(new RoleServiceImpl());
                bind(RoleFactory.class).toInstance(new RoleFactoryImpl());
                bind(RolePermissionService.class).toInstance(new RolePermissionServiceImpl());
                bind(RolePermissionFactory.class).toInstance(new RolePermissionFactoryImpl());
                bind(PermissionFactory.class).toInstance(new PermissionFactoryImpl());
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }

    // Setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario){

        if (isUnitTest()) {
            setupDI();
        }

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();
        roleService = locator.getService(RoleService.class);
        roleFactory = locator.getFactory(RoleFactory.class);
        rolePermissionService = locator.getService(RolePermissionService.class);
        rolePermissionFactory = locator.getFactory(RolePermissionFactory.class);
        permissionFactory = locator.getFactory(PermissionFactory.class);

        if (isUnitTest()) {
            // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
            // All operations on database are performed using system user.
            // Only for unit tests. Integration tests assume that a real logon is performed.
            KapuaSession kapuaSession = new KapuaSession(null, SYS_SCOPE_ID, SYS_USER_ID);
            KapuaSecurityUtils.setSession(kapuaSession);
        }

        XmlUtil.setContextProvider(new AuthorizationJAXBContextProvider());
    }

    @After
    public void afterScenario() {

        // Clean up the database
        try {
            logger.info("Logging out in cleanup");
            if (isIntegrationTest()) {
                database.deleteAll();
                SecurityUtils.getSubject().logout();
            } else {
                database.dropAll();
                database.close();
            }
            KapuaSecurityUtils.clearSession();
        } catch (Exception e) {
            logger.error("Failed to log out in @After", e);
        }
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************
    @When("^I configure the role service$")
    public void setConfigurationValue(List<CucConfig> cucConfigs)
            throws Exception {

        Map<String, Object> valueMap = new HashMap<>();
        KapuaId scopeId;
        KapuaId parentScopeId;
        Account tmpAccount = (Account) stepData.get("LastAccount");

        if (tmpAccount != null) {
            scopeId = tmpAccount.getId();
            parentScopeId = tmpAccount.getScopeId();
        } else {

            scopeId = SYS_SCOPE_ID;
            parentScopeId = SYS_SCOPE_ID;
        }

        for (CucConfig config : cucConfigs) {
            config.addConfigToMap(valueMap);
            if (config.getScopeId() != null) {
                scopeId = getKapuaId(config.getScopeId());
            }
            if (config.getParentId() != null) {
                parentScopeId = getKapuaId(config.getParentId());
            }
        }
        try {
            primeException();
            roleService.setConfigValues(scopeId, parentScopeId, valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I create the following role(?:|s)$")
    public void createAListOfRoles(List<CucRole> roles)
            throws Exception {

        RoleCreator roleCreator = null;
        Set<Permission> permissions;
        Role role = null;

        stepData.remove("RoleCreator");
        stepData.remove("Role");

        primeException();
        for (CucRole tmpRole : roles) {
            tmpRole.doParse();
            permissions = new HashSet<>();
            if ((tmpRole.getActions() != null) && (tmpRole.getActions().size() > 0)) {
                for (Actions tmpAct : tmpRole.getActions()) {
                    permissions.add(permissionFactory.newPermission(TEST_DOMAIN, tmpAct, tmpRole.getScopeId()));
                }
            }
            roleCreator = roleFactory.newCreator(tmpRole.getScopeId());
            roleCreator.setName(tmpRole.getName());
            roleCreator.setPermissions(permissions);
            try {
                role = roleService.create(roleCreator);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }

        stepData.put("RoleCreator", roleCreator);
        stepData.put("Role", role);
    }

    @Given("^I create the following role permission(?:|s)$")
    public void createAListOfRolePermissions(List<CucRolePermission> perms)
            throws Exception {

        Role role = (Role) stepData.get("Role");
        RolePermission rolePermission = null;

        TestDomain tmpDom = new TestDomain();
        tmpDom.setName("test_domain");

        stepData.remove("RolePermission");

        primeException();
        for (CucRolePermission tmpCPerm : perms) {
            tmpCPerm.doParse();
            assertNotNull(tmpCPerm.getScopeId());
            assertNotNull(tmpCPerm.getAction());

            tmpDom.setScopeId(tmpCPerm.getScopeId());

            RolePermissionCreator rolePermissionCreator = rolePermissionFactory.newCreator(tmpCPerm.getScopeId());
            rolePermissionCreator.setRoleId(role.getId());
            rolePermissionCreator.setPermission(permissionFactory.newPermission(tmpDom, tmpCPerm.getAction(), tmpCPerm.getTargetScopeId()));

            try {
                rolePermission = rolePermissionService.create(rolePermissionCreator);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }

        stepData.put("RolePermission", rolePermission);
    }

    @When("^I update the last created role name to \"(.+)\"$")
    public void updateRoleNameTo(String name)
            throws Exception {

        Role role = (Role) stepData.get("Role");
        role.setName(name);
        Thread.sleep(100);

        try {
            primeException();
            role = roleService.update(role);
        } catch (KapuaException ex) {
            verifyException(ex);
        }

        stepData.put("Role", role);
    }

    @When("^I examine the permissions for the last role$")
    public void findPermissionsForTheLastCreatedRole()
            throws Exception {

        Role role = (Role) stepData.get("Role");

        stepData.remove("PermissionList");

        primeException();
        try {
            RolePermissionListResult permissionList = rolePermissionService.findByRoleId(
                    role.getScopeId(), role.getId());
            stepData.put("PermissionList", permissionList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created role$")
    public void findLastCreatedRole()
            throws Exception {

        Role role = (Role) stepData.get("Role");

        stepData.remove("RoleFound");

        primeException();
        try {
            Role roleFound = roleService.find(role.getScopeId(), role.getId());
            stepData.put("RoleFound", roleFound);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created role permission$")
    public void findLastCreatedRolePermission()
            throws Exception {

        RolePermission rolePermission = (RolePermission) stepData.get("RolePermission");
        stepData.remove("RolePermissionFound");

        primeException();
        try {
            RolePermission rolePermissionFound = rolePermissionService.find(
                    rolePermission.getScopeId(), rolePermission.getId());
            stepData.put("RolePermissionFound", rolePermissionFound);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for a random role ID$")
    public void searchForRandomId()
            throws Exception {

        primeException();
        try {
            Role roleFound = roleService.find(SYS_SCOPE_ID, getKapuaId());
            stepData.put("RoleFound", roleFound);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last created role$")
    public void deleteLastCreatedRole()
            throws Exception {

        Role role = (Role) stepData.get("Role");

        primeException();
        try {
            roleService.delete(role.getScopeId(), role.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last created role permission$")
    public void deleteLastCreatedRolePermission()
            throws Exception {

        RolePermission rolePermission = (RolePermission) stepData.get("RolePermission");

        primeException();
        try {
            rolePermissionService.delete(rolePermission.getScopeId(), rolePermission.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the roles in scope (\\d+)$")
    public void countRolesInScope(int scope)
            throws Exception {

        KapuaId tmpId = getKapuaId(scope);
        RoleQuery tmpQuery = roleFactory.newQuery(tmpId);

        stepData.remove("Count");

        primeException();
        try {
            long count = roleService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the permission in scope (\\d+)$")
    public void countRolePermissionsInScope(Integer scope)
            throws Exception {

        KapuaId tmpId = getKapuaId(scope);
        RolePermissionQuery tmpQuery = rolePermissionFactory.newQuery(tmpId);

        stepData.remove("Count");

        primeException();
        try {
            long count = rolePermissionService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for the role \"(.+)\" in scope (\\d+)$")
    public void queryForRoleInScope(String name, Integer scope)
            throws Exception {

        KapuaId tmpId = getKapuaId(scope);
        RoleQuery tmpQuery = roleFactory.newQuery(tmpId);
        tmpQuery.setPredicate(new AttributePredicateImpl<>(RoleAttributes.NAME, name));

        stepData.remove("RoleList");
        stepData.remove("RoleFound");
        stepData.remove("Count");

        primeException();
        try {
            RoleListResult roleList = roleService.query(tmpQuery);
            stepData.put("RoleList", roleList);
            stepData.put("RoleFound", roleList.getFirstItem());
            stepData.put("Count", roleList.getSize());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^I find no roles$")
    public void chackThatNothingWasFound() {
        assertNull(stepData.get("RoleFound"));
    }

    @Then("^I find no permissions$")
    public void checkThatNoPermissionWasFound() {
        assertNull(stepData.get("RolePermissionFound"));
    }

    @Then("^The role matches the creator$")
    public void checkLastRoleAgainstCreator() {

        Role role = (Role) stepData.get("Role");
        RoleCreator roleCreator = (RoleCreator) stepData.get("RoleCreator");

        assertNotNull(role);
        assertNotNull(roleCreator);
        assertEquals(roleCreator.getScopeId(), role.getScopeId());
        assertEquals(roleCreator.getName(), role.getName());
        assertNotNull(role.getCreatedBy());
        assertNotNull(role.getCreatedOn());
        assertNotNull(role.getModifiedBy());
        assertNotNull(role.getModifiedOn());
    }

    @Then("^The permissions match$")
    public void checkPermissionsAgainstRole() {

        Set<Permission> permissions = (Set<Permission>) stepData.get("Permissions");
        RolePermissionListResult permissionList = (RolePermissionListResult) stepData.get("PermissionList");

        boolean found;
        assertNotNull(permissions);
        assertNotNull(permissionList);
        assertEquals(permissions.size(), permissionList.getSize());
        if (permissions.size() > 0) {
            for (RolePermission tmpRolePerm : permissionList.getItems()) {
                found = false;
                for (Permission tmpPerm : permissions) {
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

        RolePermissionListResult permissionList = (RolePermissionListResult) stepData.get("PermissionList");

        assertNotNull(roles);
        assertEquals(1, roles.size());
        CucRole tmpRole = roles.get(0);
        assertNotNull(tmpRole);
        tmpRole.doParse();
        Set<Actions> actLst = tmpRole.getActions();
        assertNotNull(actLst);
        assertNotNull(permissionList);
        assertNotEquals(0, permissionList.getSize());
        assertEquals(actLst.size(), permissionList.getSize());
        for (RolePermission tmpPerm : permissionList.getItems()) {
            assertTrue(actLst.contains(tmpPerm.getPermission().getAction()));
        }
    }

    @Then("^The correct role entry was found$")
    public void verifyThatRolesMatch() {

        Role role = (Role) stepData.get("Role");
        Role roleFound = (Role) stepData.get("RoleFound");

        assertNotNull(role);
        assertNotNull(roleFound);
        assertEquals(role.getScopeId(), roleFound.getScopeId());
        assertEquals(role.getScopeId(), roleFound.getScopeId());
        assertEquals(role.getName(), roleFound.getName());
        assertEquals(role.getCreatedBy(), roleFound.getCreatedBy());
        assertEquals(role.getCreatedOn(), roleFound.getCreatedOn());
        assertEquals(role.getModifiedBy(), roleFound.getModifiedBy());
        assertEquals(role.getModifiedOn(), roleFound.getModifiedOn());
    }

    @Then("^The correct role permission entry was found$")
    public void verifyThatRolePermissionsMatch() {

        RolePermission rolePermission = (RolePermission) stepData.get("RolePermission");
        RolePermission rolePermissionFound = (RolePermission) stepData.get("RolePermissionFound");

        assertNotNull(rolePermission);
        assertNotNull(rolePermissionFound);
        assertEquals(rolePermission.getScopeId(), rolePermissionFound.getScopeId());
        assertEquals(rolePermission.getScopeId(), rolePermissionFound.getScopeId());
        assertEquals(rolePermission.getCreatedBy(), rolePermissionFound.getCreatedBy());
        assertEquals(rolePermission.getCreatedOn(), rolePermissionFound.getCreatedOn());
    }

    @Then("^The role was successfully updated$")
    public void checkRoleForUpdates() {

        Role role = (Role) stepData.get("Role");
        Role roleFound = (Role) stepData.get("RoleFound");

        assertNotNull(role);
        assertNotNull(roleFound);
        assertEquals(role.getScopeId(), roleFound.getScopeId());
        assertEquals(role.getScopeId(), roleFound.getScopeId());
        assertEquals(role.getName(), roleFound.getName());
        assertEquals(role.getCreatedBy(), roleFound.getCreatedBy());
        assertEquals(role.getCreatedOn(), roleFound.getCreatedOn());
        assertEquals(role.getModifiedBy(), roleFound.getModifiedBy());
        assertNotEquals(role.getModifiedOn(), roleFound.getModifiedOn());
    }

    @Then("^The role factory returns sane results$")
    public void performRoleFactorySanityChecks() {

        assertNotNull(roleFactory.newEntity(SYS_SCOPE_ID));
        assertNotNull(roleFactory.newCreator(SYS_SCOPE_ID));
        assertNotNull(roleFactory.newQuery(SYS_SCOPE_ID));
        assertNotNull(roleFactory.newListResult());
        assertNotNull(roleFactory.newRolePermission());
    }

    @Then("^The role permission factory returns sane results$")
    public void performRolePermissionFactorySanityChecks() {

        assertNotNull(rolePermissionFactory.newEntity(SYS_SCOPE_ID));
        assertNotNull(rolePermissionFactory.newCreator(SYS_SCOPE_ID));
        assertNotNull(rolePermissionFactory.newQuery(SYS_SCOPE_ID));
        assertNotNull(rolePermissionFactory.newListResult());
    }

    @Then("^The role comparator does its job$")
    public void checkRoleEqualityMethod() {

        Role role1 = roleFactory.newEntity(SYS_SCOPE_ID);
        Role role2 = roleFactory.newEntity(SYS_SCOPE_ID);
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
        RolePermission perm1 = rolePermissionFactory.newEntity(SYS_SCOPE_ID);
        RolePermission perm2 = rolePermissionFactory.newEntity(SYS_SCOPE_ID);
        Integer miscObj = 1;
        Permission tmpPermission1 = permissionFactory.newPermission(TEST_DOMAIN, Actions.read, SYS_SCOPE_ID);
        Permission tmpPermission2 = permissionFactory.newPermission(TEST_DOMAIN, Actions.write, SYS_SCOPE_ID);
        KapuaId tmpRoleId1 = getKapuaId();
        KapuaId tmpRoleId2 = getKapuaId();

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

        Permission tmpPermission = permissionFactory.newPermission(TEST_DOMAIN, Actions.read, SYS_SCOPE_ID);
        KapuaId tmpRoleId = getKapuaId();

        assertNotNull(tmpRoleId);
        RolePermission perm1 = new RolePermissionImpl(SYS_SCOPE_ID);
        assertNotNull(perm1);
        RolePermission perm2 = new RolePermissionImpl(perm1);
        assertNotNull(perm2);
        assertTrue(perm1.equals(perm2));
        RolePermission perm3 = new RolePermissionImpl(SYS_SCOPE_ID, tmpPermission);
        assertNotNull(perm3);
        assertEquals(perm3.getPermission(), tmpPermission);
        perm1.setRoleId(tmpRoleId);
        assertEquals(tmpRoleId, perm1.getRoleId());
        perm1.setPermission(tmpPermission);
        assertEquals(perm1.getPermission(), tmpPermission);
    }
}
