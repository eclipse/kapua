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
import org.eclipse.kapua.qa.common.cucumber.CucConfig;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.qa.common.cucumber.CucDomain;
import org.eclipse.kapua.qa.common.cucumber.CucGroup;
import org.eclipse.kapua.qa.common.cucumber.CucRole;
import org.eclipse.kapua.qa.common.cucumber.CucRolePermission;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoAttributes;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionQuery;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoCreatorImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoFactoryImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoServiceImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionCreatorImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionServiceImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRoleCreatorImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRoleFactoryImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRoleImpl;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRoleServiceImpl;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;
import org.eclipse.kapua.service.authorization.domain.DomainRegistryService;
import org.eclipse.kapua.service.authorization.domain.DomainAttributes;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainFactoryImpl;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainRegistryServiceImpl;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupAttributes;
import org.eclipse.kapua.service.authorization.group.GroupCreator;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.GroupQuery;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.group.shiro.GroupFactoryImpl;
import org.eclipse.kapua.service.authorization.group.shiro.GroupServiceImpl;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
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
import org.eclipse.kapua.service.authorization.role.shiro.RoleImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionFactoryImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionServiceImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RoleServiceImpl;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationServiceImpl;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.qa.common.MockedLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Implementation of Gherkin steps used to test miscellaneous Shiro 
// authorization functionality.

@ScenarioScoped
public class AuthorizationServiceSteps extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServiceSteps.class);
    private static final TestDomain TEST_DOMAIN = new TestDomain();

    // Various Authorization service references
    private PermissionFactory permissionFactory;
    private AccessInfoService accessInfoService;
    private AccessInfoFactory accessInfoFactory;
    private AccessPermissionService accessPermissionService;
    private AccessPermissionFactory accessPermissionFactory;
    private AccessRoleService accessRoleService;
    private AccessRoleFactory accessRoleFactory;
    private DomainRegistryService domainRegistryService;
    private DomainFactory domainFactory;
    private GroupService groupService ;
    private GroupFactory groupFactory ;
    private RoleService roleService;
    private RoleFactory roleFactory;
    private RolePermissionService rolePermissionService;
    private RolePermissionFactory rolePermissionFactory;

    @Inject
    public AuthorizationServiceSteps(StepData stepData, DBHelper dbHelper) {

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

                // Set KapuaMetatypeFactory for Metatype configuration
                bind(KapuaMetatypeFactory.class).toInstance(new KapuaMetatypeFactoryImpl());

                // Inject actual account related services
                bind(AuthorizationEntityManagerFactory.class).toInstance(AuthorizationEntityManagerFactory.getInstance());

                bind(AuthorizationService.class).toInstance(new AuthorizationServiceImpl());
                bind(AccessInfoService.class).toInstance(new AccessInfoServiceImpl());
                bind(AccessInfoFactory.class).toInstance(new AccessInfoFactoryImpl());
                bind(AccessPermissionService.class).toInstance(new AccessPermissionServiceImpl());
                bind(AccessPermissionFactory.class).toInstance(new AccessPermissionFactoryImpl());
                bind(AccessRoleService.class).toInstance(new AccessRoleServiceImpl());
                bind(AccessRoleFactory.class).toInstance(new AccessRoleFactoryImpl());
                bind(DomainRegistryService.class).toInstance(new DomainRegistryServiceImpl());
                bind(DomainFactory.class).toInstance(new DomainFactoryImpl());
                bind(GroupService.class).toInstance(new GroupServiceImpl());
                bind(GroupFactory.class).toInstance(new GroupFactoryImpl());
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

    // Database setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario) {

        if (isUnitTest()) {
            setupDI();
        }

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();
        accessInfoService = locator.getService(AccessInfoService.class);
        accessInfoFactory = locator.getFactory(AccessInfoFactory.class);
        accessPermissionService = locator.getService(AccessPermissionService.class);
        accessPermissionFactory = locator.getFactory(AccessPermissionFactory.class);
        accessRoleService = locator.getService(AccessRoleService.class);
        accessRoleFactory = locator.getFactory(AccessRoleFactory.class);
        domainRegistryService = locator.getService(DomainRegistryService.class);
        domainFactory = locator.getFactory(DomainFactory.class);
        groupService = locator.getService(GroupService.class);
        groupFactory = locator.getFactory(GroupFactory.class);
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

        XmlUtil.setContextProvider(new TestJAXBContextProvider());
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

    // Cucumber test steps

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

    @When("^I configure the role service for the account with the id (\\d+)$")
    public void setRoleServiceConfig(int accountId, List<CucConfig> cucConfigs)
            throws Exception {

        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId = getKapuaId(accountId);

        for (CucConfig config : cucConfigs) {
            config.addConfigToMap(valueMap);
        }

        primeException();
        try {
            roleService.setConfigValues(accId, SYS_SCOPE_ID, valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I create the following role(?:|s)$")
    public void createAListOfRoles(List<CucRole> roles)
            throws Exception {

        Domain domain = (Domain) stepData.get("Domain");
        RoleCreator roleCreator = null;
        Set<Permission> permissions;
        Role role = null;

        stepData.remove("Permissions");
        stepData.remove("RoleCreator");
        stepData.remove("Role");

        primeException();
        for (CucRole tmpRole : roles) {
            tmpRole.doParse();
            permissions = new HashSet<>();
            if ((tmpRole.getActions() != null) && (tmpRole.getActions().size() > 0)) {
                for (Actions tmpAct : tmpRole.getActions()) {
                    permissions.add(permissionFactory.newPermission(domain.getDomain(), tmpAct, tmpRole.getScopeId()));
                }
            }
            roleCreator = roleFactory.newCreator(tmpRole.getScopeId());
            roleCreator.setName(tmpRole.getName());
            roleCreator.setPermissions(permissions);
            try {
                role = roleService.create(roleCreator);
                stepData.put("Permissions", permissions);
                stepData.put("RoleCreator", roleCreator);
                stepData.put("Role", role);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @Given("^I create the following role permission(?:|s)$")
    public void createAListOfRolePermissions(List<CucRolePermission> perms)
            throws Exception {

        Role role = (Role) stepData.get("Role");
        Domain domain = (Domain) stepData.get("Domain");
        RolePermission rolePermission = null;

        stepData.remove("RolePermission");

        primeException();
        for (CucRolePermission tmpCPerm : perms) {
            tmpCPerm.doParse();
            assertNotNull(tmpCPerm.getScopeId());
            assertNotNull(tmpCPerm.getAction());

            domain.setScopeId(tmpCPerm.getScopeId());

            RolePermissionCreator rolePermissionCreator = rolePermissionFactory.newCreator(tmpCPerm.getScopeId());
            rolePermissionCreator.setRoleId(role.getId());
            rolePermissionCreator.setPermission(permissionFactory.newPermission(domain.getDomain(), tmpCPerm.getAction(), tmpCPerm.getTargetScopeId()));

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

        Role role = new RoleImpl((Role) stepData.get("Role"));
        role.setName(name);
        Thread.sleep(200);

        try {
            primeException();
            roleService.update(role);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
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
            Long count = roleService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the role permissions in scope (\\d+)$")
    public void countRolePermissionsInScope(Integer scope)
            throws Exception {

        KapuaId tmpId = getKapuaId(scope);
        RolePermissionQuery tmpQuery = rolePermissionFactory.newQuery(tmpId);

        stepData.remove("Count");

        primeException();
        try {
            Long count = rolePermissionService.count(tmpQuery);
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
            stepData.put("Count", Long.valueOf(roleList.getSize()));
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^The role was found$")
    public void chackThatSomethingWasFound() {
        assertNotNull(stepData.get("RoleFound"));
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
        assertEquals(role.getId(), roleFound.getId());
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
        assertEquals(rolePermission.getId(), rolePermissionFound.getId());
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
        assertEquals(role.getId(), roleFound.getId());
        assertEquals(role.getScopeId(), roleFound.getScopeId());
        assertNotEquals(role.getName(), roleFound.getName());
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

    @Given("^I create the domain(?:|s)$")
    public void createAListOfDomains(List<CucDomain> domains)
            throws Exception {

        DomainCreator domainCreator = null;
        Domain domain = null;

        stepData.remove("DomainCreator");
        stepData.remove("Domain");
        stepData.remove("DomainId");

        primeException();
        for (CucDomain tmpDom : domains) {
            tmpDom.doParse();

            domainCreator = domainFactory.newCreator(tmpDom.getName());
            if (tmpDom.getActionSet() != null) {
                domainCreator.setActions(tmpDom.getActionSet());
            }
            stepData.put("DomainCreator", domainCreator);

            try {
                domain = domainRegistryService.create(domainCreator);
                stepData.put("Domain", domain);
                if (domain != null) {
                    stepData.put("DomainId", domain.getId());
                }
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @Given("^I select the domain \"(.+)\"$")
    public void selectExistingDomain(String name)
            throws Exception {

        DomainQuery query = domainFactory.newQuery(KapuaId.ANY);
        query.setPredicate(new AttributePredicateImpl<>(DomainAttributes.NAME, name));

        try {
            primeException();
            DomainListResult domains = domainRegistryService.query(query);
            stepData.put("Domain", domains.getFirstItem());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created domain$")
    public void findDomainByRememberedId()
            throws Exception {

        KapuaId domainId = (KapuaId) stepData.get("DomainId");
        stepData.remove("Domain");

        try {
            primeException();
            Domain domain = domainRegistryService.find(null, domainId);
            stepData.put("Domain", domain);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last created domain$")
    public void deleteLastCreatedDomain()
            throws Exception {

        KapuaId domainId = (KapuaId) stepData.get("DomainId");

        try {
            primeException();
            domainRegistryService.delete(null, domainId);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I try to delete domain with a random ID$")
    public void deleteRandomDomainId()
            throws Exception {

        try {
            primeException();
            domainRegistryService.delete(null, getKapuaId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the domain entries in the database$")
    public void countDomainEntries()
            throws Exception {

        stepData.remove("Count");

        try {
            primeException();
            DomainQuery query = domainFactory.newQuery(null);
            Long count = domainRegistryService.count(query);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for domains with the name \"(.+)\"$")
    public void queryForNamedDomain(String name)
            throws Exception {

        DomainQuery query = domainFactory.newQuery(null);
        query.setPredicate(new AttributePredicateImpl<>(DomainAttributes.NAME, name));

        stepData.remove("DomainList");
        stepData.remove("Count");

        try {
            primeException();
            DomainListResult domainList = domainRegistryService.query(query);
            stepData.put("DomainList", domainList);
            stepData.put("Count", Long.valueOf(domainList.getSize()));
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^This is the initial count$")
    public void setInitialCount() {

        Long startCount = (Long) stepData.get("Count");
        stepData.put("InitialCount", startCount);
    }

    @Then("^A domain was created$")
    public void checkDomainNotNull() {

        Domain domain = (Domain) stepData.get("Domain");
        assertNotNull(domain);
    }

    @Then("^There is no domain$")
    public void checkDomainIsNull() {

        Domain domain = (Domain) stepData.get("Domain");
        assertNull(domain);
    }

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Domain object equals function.
    // It must be noted that full coverage should be impossible, since the function tests for
    // object member combinations that should be impossible to create.
    // Some examples are domain objects with the same name but different service name members
    // (the name entry is defined as unique in the database). Also it tests for null
    // values for all 3 members, but the Domain service create method will reject any domain
    // creator with a null value for any member variable.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^I can compare domain objects$")
    public void checkDomainComparison()
            throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            DomainCreator tmpCreator = domainFactory.newCreator("name_1");
            HashSet<Actions> tmpAct = new HashSet<>();
            tmpAct.add(Actions.read);
            tmpAct.add(Actions.write);
            tmpCreator.setActions(tmpAct);
            Domain tmpDom1 = domainRegistryService.create(tmpCreator);
            assertNotNull(tmpDom1);

            assertTrue(tmpDom1.equals(tmpDom1));
            assertFalse(tmpDom1.equals(null));
            assertFalse(tmpDom1.equals(String.valueOf("")));

            Domain tmpDom2 = null;
            tmpDom2 = domainRegistryService.find(null, tmpDom1.getId());
            assertNotNull(tmpDom2);

            tmpCreator.setName("name_2");
            Domain tmpDom3 = domainRegistryService.create(tmpCreator);
            assertNotNull(tmpDom3);

            tmpCreator.setName("name_3");
            tmpAct.remove(Actions.write);
            tmpCreator.setActions(tmpAct);
            Domain tmpDom4 = domainRegistryService.create(tmpCreator);
            assertNotNull(tmpDom4);

            assertTrue(tmpDom1.equals(tmpDom2));
            assertFalse(tmpDom1.equals(tmpDom3));
            assertFalse(tmpDom1.equals(tmpDom4));
            return null;
        });
    }

    @Then("^The domain matches the creator$")
    public void checkDomainAgainstCreator() {

        Domain domain = (Domain) stepData.get("Domain");
        DomainCreator domainCreator = (DomainCreator) stepData.get("DomainCreator");

        assertNotNull(domain);
        assertNotNull(domain.getId());
        assertNotNull(domainCreator);
        assertEquals(domainCreator.getName(), domain.getName());
        if (domainCreator.getActions() != null) {
            assertNotNull(domain.getActions());
            assertEquals(domainCreator.getActions().size(), domain.getActions().size());
            for (Actions a : domainCreator.getActions()) {
                assertTrue(domain.getActions().contains(a));
            }
        }
    }

    @Then("^The domain matches the parameters$")
    public void checkDomainAgainstParameters(List<CucDomain> domains) {

        Domain domain = (Domain) stepData.get("Domain");

        assertEquals(1, domains.size());
        CucDomain tmpDom = domains.get(0);
        tmpDom.doParse();

        if (tmpDom.getName() != null) {
            assertEquals(tmpDom.getName(), domain.getName());
        }
        if (tmpDom.getActionSet() != null) {
            assertEquals(tmpDom.getActionSet().size(), domain.getActions().size());
            for (Actions a : tmpDom.getActionSet()) {
                assertTrue(domain.getActions().contains(a));
            }
        }
    }

    @Then("^(\\d+) more domains (?:was|were) created$")
    public void checkIncreasedCountResult(Long cnt) {

        Long count = (Long) stepData.get("Count");
        Long initialCount = (Long) stepData.get("InitialCount");

        assertEquals(cnt.longValue(), count.longValue() - initialCount.longValue());
    }

    @When("^I configure the group service$")
    public void setGroupConfigurationValue(List<CucConfig> cucConfigs)
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
            groupService.setConfigValues(scopeId, parentScopeId, valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the group entries in the database$")
    public void countGroupEntries()
            throws Exception {

        stepData.remove("Count");
        primeException();
        try {
            Long count = groupService.count(groupFactory.newQuery(SYS_SCOPE_ID));
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I create the group(?:|s)$")
    public void createAListOfGroups(List<CucGroup> groups)
            throws Exception {

        Group group = null;
        GroupCreator groupCreator = null;
        stepData.remove("GroupCreator");
        stepData.remove("Group");
        stepData.remove("GroupId");

        primeException();
        for (CucGroup tmpGrp : groups) {
            tmpGrp.doParse();
            groupCreator = groupFactory.newCreator(tmpGrp.getScopeId(), tmpGrp.getName());

            try {
                group = groupService.create(groupCreator);
                stepData.put("GroupCreator", groupCreator);
                stepData.put("Group", group);
                stepData.put("GroupId", group.getId());
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @When("^I update the group name to \"(.+)\"$")
    public void updateLastGroupName(String name)
            throws Exception {

        Group group = (Group) stepData.get("Group");
        group.setName(name);
        // Sleep for a bit to make sure the time stamps are really different!
        Thread.sleep(50);

        try {
            Group groupSecond = groupService.update(group);
            stepData.put("GroupSecond", groupSecond);
        } catch (KapuaException ex) {
            verifyException(ex);
        }

    }

    @When("^I update the group with an incorrect ID$")
    public void updateGroupWithFalseId()
            throws Exception {

        Group group = (Group) stepData.get("Group");
        group.setId(getKapuaId());

        primeException();
        try {
            groupService.update(group);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last created group$")
    public void deleteLastCreatedGroup()
            throws Exception {

        Group group = (Group) stepData.get("Group");

        primeException();
        try {
            groupService.delete(group.getScopeId(), group.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I try to delete a random group id$")
    public void deleteGroupWithRandomId()
            throws Exception {

        primeException();
        try {
            groupService.delete(SYS_SCOPE_ID, getKapuaId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created group$")
    public void findGroupByRememberedId()
            throws Exception {

        Group group = (Group) stepData.get("Group");

        primeException();
        try {
            Group groupSecond = groupService.find(group.getScopeId(), group.getId());
            stepData.put("GroupSecond", groupSecond);
        } catch (KapuaException ex) {
            verifyException(ex);
        }

    }

    @When("^I count all the groups in scope (\\d+)$")
    public void countGroupsInScope(int scope)
            throws Exception {

        KapuaId tmpId = getKapuaId(scope);
        GroupQuery tmpQuery = groupFactory.newQuery(tmpId);

        primeException();
        try {
            Long count = groupService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for the group \"(.+)\" in scope (\\d+)$")
    public void queryForGroup(String name, int scope)
            throws Exception {

        KapuaId tmpId = getKapuaId(scope);
        GroupQuery tmpQuery = groupFactory.newQuery(tmpId);
        tmpQuery.setPredicate(new AttributePredicateImpl<>(GroupAttributes.NAME, name));

        stepData.remove("GroupList");
        stepData.remove("Group");
        stepData.remove("Count");

        primeException();
        try {
            GroupListResult groupList = groupService.query(tmpQuery);
            stepData.put("GroupList", groupList);
            stepData.put("Group", groupList.getFirstItem());
            stepData.put("Count", Long.valueOf(groupList.getSize()));
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^A group was created$")
    public void checkGroupNotNull() {

        assertNotNull(stepData.get("Group"));
    }

    @Then("^No group was created$")
    public void checkGroupIsNull() {

        assertNull(stepData.get("Group"));
    }

    @Then("^The group was found$")
    public void checkThatTheGroupWasFound() {

        assertNotNull(stepData.get("GroupSecond"));
    }

    @Then("^No group was found$")
    public void checkNoGroupWasFound() {

        assertNull(stepData.get("GroupSecond"));
    }

    @Then("^The group name is \"(.+)\"$")
    public void checkGroupName(String name) {

        Group group = (Group) stepData.get("Group");
        assertEquals(group.getName(), name.trim());
    }

    @Then("^The group matches the creator$")
    public void checkGroupAgainstCreator() {

        Group group = (Group) stepData.get("Group");
        GroupCreator groupCreator = (GroupCreator) stepData.get("GroupCreator");

        assertNotNull(group);
        assertNotNull(group.getId());
        assertNotNull(groupCreator);
        assertEquals(groupCreator.getScopeId(), group.getScopeId());
        assertEquals(groupCreator.getName(), group.getName());
        assertNotNull(group.getCreatedBy());
        assertNotNull(group.getCreatedOn());
        assertNotNull(group.getModifiedBy());
        assertNotNull(group.getModifiedOn());
    }

    @Then("^The group was correctly updated$")
    public void checkUpdatedGroup() {

        Group group = (Group) stepData.get("Group");
        Group groupSecond = (Group) stepData.get("GroupSecond");

        assertNotNull(groupSecond);
        assertNotNull(groupSecond.getId());
        assertEquals(group.getScopeId(), groupSecond.getScopeId());
        assertEquals(group.getName(), groupSecond.getName());
        assertEquals(group.getCreatedBy(), groupSecond.getCreatedBy());
        assertEquals(group.getCreatedOn(), groupSecond.getCreatedOn());
        assertEquals(group.getModifiedBy(), groupSecond.getModifiedBy());
        assertNotEquals(group.getModifiedOn(), groupSecond.getModifiedOn());
    }

    @Then("^The group was correctly found$")
    public void checkFoundGroup() {

        Group group = (Group) stepData.get("Group");
        Group groupSecond = (Group) stepData.get("GroupSecond");

        assertNotNull(groupSecond);
        assertNotNull(groupSecond.getId());
        assertEquals(group.getScopeId(), groupSecond.getScopeId());
        assertEquals(group.getName(), groupSecond.getName());
        assertEquals(group.getCreatedBy(), groupSecond.getCreatedBy());
        assertEquals(group.getCreatedOn(), groupSecond.getCreatedOn());
        assertEquals(group.getModifiedBy(), groupSecond.getModifiedBy());
        assertEquals(group.getModifiedOn(), groupSecond.getModifiedOn());
    }

    @Given("^The permission(?:|s) \"(.+)\"$")
    public void createPermissionsForDomain(String permList) {

        // Split the parameter string and make sure there is at least one item
        String[] tmpList = permList.toLowerCase().split(",");
        assertNotNull(tmpList);
        assertNotEquals(0, tmpList.length);

        // Parse the items and fill the list
        Set<Permission> permissions = new HashSet<>();
        KapuaId currId = (KapuaId) stepData.get("LastAccountId");

        // Get the current domain
        Domain curDomain = (Domain) stepData.get("Domain");

        for (String perm : tmpList) {
            switch (perm.trim()) {
                case "read":
                    permissions.add(permissionFactory.newPermission(curDomain.getDomain(), Actions.read, currId));
                    break;
                case "write":
                    permissions.add(permissionFactory.newPermission(curDomain.getDomain(), Actions.write, currId));
                    break;
                case "delete":
                    permissions.add(permissionFactory.newPermission(curDomain.getDomain(), Actions.delete, currId));
                    break;
                case "connect":
                    permissions.add(permissionFactory.newPermission(curDomain.getDomain(), Actions.connect, currId));
                    break;
                case "execute":
                    permissions.add(permissionFactory.newPermission(curDomain.getDomain(), Actions.execute, currId));
                    break;
            }
        }
        // Make sure that there is at least one valid item
        assertFalse(permissions.isEmpty());

        stepData.put("Permissions", permissions);
    }

    @Given("^The role \"(.*)\"$")
    public void provideRoleForDomain(String name)
            throws Exception {

        KapuaId currId = (KapuaId) stepData.get("LastAccountId");
        Set<Permission> permissions = (Set<Permission>) stepData.get("Permissions");

        RoleCreator roleCreator = roleFactory.newCreator(currId);
        roleCreator.setName(name);
        roleCreator.setPermissions(permissions);

        try {
            primeException();
            stepData.remove("Role");
            stepData.remove("RoleIds");
            Role role = roleService.create(roleCreator);
            stepData.put("Role", role);
            Set<KapuaId> roleIds = new HashSet<>();
            roleIds.add(role.getId());
            stepData.put("RoleIds", roleIds);
        } catch (KapuaException e) {
            verifyException(e);
        }
    }

    @Given("^An invalid role ID$")
    public void provideInvalidRoleObjectID() {

        stepData.remove("RoleIds");
        Set<KapuaId> roleIds = new HashSet<>();
        roleIds.add(getKapuaId());
        stepData.put("RoleIds", roleIds);
    }

    @When("^I create the access role$")
    public void createAccessRole()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get("LastAccountId");
        AccessInfo accessInfo = (AccessInfo) stepData.get("AccessInfo");
        Role role = (Role) stepData.get("Role");
        AccessRoleCreator tmpCreator = accessRoleFactory.newCreator(currScope);
        tmpCreator.setAccessInfoId(accessInfo.getId());
        tmpCreator.setRoleId(role.getId());

        try {
            primeException();
            stepData.remove("AccessRole");
            AccessRole accessRole = accessRoleService.create(tmpCreator);
            stepData.put("AccessRole", accessRole);
        } catch (KapuaException e) {
            verifyException(e);
        }
    }

    @When("^I create a clean access info entity$")
    public void createCleanAccessInfoEntity()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get("LastAccountId");
        User tmpUser = (User) stepData.get("User");
        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(currScope);
        accessInfoCreator.setUserId(tmpUser.getId());

        stepData.remove("Permissions");
        stepData.remove("RoleIds");

        try {
            primeException();
            stepData.put("AccessInfoCreator", accessInfoCreator);
            stepData.remove("AccessInfo");
            AccessInfo accessInfo = accessInfoService.create(accessInfoCreator);
            stepData.put("AccessInfo", accessInfo);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I create the access info entity$")
    public void createAccessInfoEntity()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get("LastAccountId");
        User tmpUser = (User) stepData.get("User");
        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(currScope);
        accessInfoCreator.setUserId(tmpUser.getId());

        Set<Permission> permissions = (Set<Permission>) stepData.get("Permissions");
        Set<KapuaId> roleIds = (Set<KapuaId>) stepData.get("RoleIds");

        if (permissions != null && !permissions.isEmpty()) {
            accessInfoCreator.setPermissions(permissions);
        } else {
            accessInfoCreator.setPermissions(null);
        }

        if (roleIds != null && !roleIds.isEmpty()) {
            accessInfoCreator.setRoleIds(roleIds);
        } else {
            accessInfoCreator.setRoleIds(null);
        }

        try {
            primeException();
            stepData.put("AccessInfoCreator", accessInfoCreator);
            stepData.remove("AccessInfo");
            AccessInfo accessInfo = accessInfoService.create(accessInfoCreator);
            stepData.put("AccessInfo", accessInfo);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the permissions of the last access info entity$")
    public void findThePermissionsOfTheLastAccessInfoEntity()
            throws Exception {

        AccessInfo accessInfo = (AccessInfo) stepData.get("AccessInfo");

        try {
            primeException();
            stepData.remove("AccessPermissions");
            AccessPermissionListResult accessPermissions = accessPermissionService.findByAccessInfoId(
                    accessInfo.getScopeId(), accessInfo.getId());
            stepData.put("AccessPermissions", accessPermissions);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the roles of the last access info entity$")
    public void findTheRolesOfTheLastAccessInfoEntity()
            throws Exception {

        AccessInfo accessInfo = (AccessInfo) stepData.get("AccessInfo");

        try {
            primeException();
            stepData.remove("AccessRoles");
            AccessRoleListResult accessRoles = accessRoleService.findByAccessInfoId(accessInfo.getScopeId(), accessInfo.getId());
            stepData.put("AccessRoles", accessRoles);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created access info entity$")
    public void findLastCreatedAccessInfoEntity()
            throws Exception {

        AccessInfo accessInfo = (AccessInfo) stepData.get("AccessInfo");

        try {
            primeException();
            stepData.remove("AccessInfoFound");
            AccessInfo accessInfoFound = accessInfoService.find(accessInfo.getScopeId(), accessInfo.getId());
            stepData.put("AccessInfoFound", accessInfoFound);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for an access info entity by user ID$")
    public void findTheAccessInfoEntityByUserId()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get("LastAccountId");
        User tmpUser = (User) stepData.get("User");

        try {
            primeException();
            stepData.remove("AccessInfoFound");
            AccessInfo accessInfoFound = accessInfoService.findByUserId(currScope, tmpUser.getId());
            stepData.put("AccessInfoFound", accessInfoFound);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created access role entity$")
    public void findLastCreatedAccessRole()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get("LastAccountId");
        AccessRole accessRole = (AccessRole) stepData.get("AccessRole");

        try {
            primeException();
            stepData.remove("AccessRoleFound");
            AccessRole accessRoleFound = accessRoleService.find(currScope, accessRole.getId());
            stepData.put("AccessRoleFound", accessRoleFound);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the access roles in scope (\\d+)$")
    public void countAccesRolesInScope(Integer scope)
            throws Exception {

        AccessRoleQuery tmpQuery = accessRoleFactory.newQuery(getKapuaId(scope));

        try {
            primeException();
            stepData.remove("Count");
            Long count = accessRoleService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last created access role entry$")
    public void deleteLastCreatedAccessRoleEntry()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get("LastAccountId");
        AccessRole accessRole = (AccessRole) stepData.get("AccessRole");

        try {
            primeException();
            accessRoleService.delete(currScope, accessRole.getId());
        } catch (KapuaException e) {
            verifyException(e);
        }
    }

    @When("^I delete the existing access info entity$")
    public void deleteLastCreatedAccessInfoEntity()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get("LastAccountId");
        AccessInfo accessInfo = (AccessInfo) stepData.get("AccessInfo");

        try {
            primeException();
            accessInfoService.delete(currScope, accessInfo.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the access info entities for scope (\\d+)$")
    public void countAccessInfoEntitiesInScope(Integer scope)
            throws Exception {

        AccessInfoQuery tmpQuery = accessInfoFactory.newQuery(getKapuaId(scope));

        try {
            primeException();
            stepData.remove("Count");
            Long count = accessInfoService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for the access info entities for the last user$")
    public void queryForLastUserAccessInfoEntities()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get("LastAccountId");
        User user = (User) stepData.get("User");

        AccessInfoQuery tmpQuery = accessInfoFactory.newQuery(currScope);
        tmpQuery.setPredicate(new AttributePredicateImpl<>(AccessInfoAttributes.USER_ID, user.getId()));

        try {
            primeException();
            stepData.remove("Count");
            stepData.remove("AccessList");
            AccessInfoListResult accessList = accessInfoService.query(tmpQuery);
            stepData.put("AccessList", accessList);
            if (accessList != null) {
                stepData.put("Count", Long.valueOf(accessList.getSize()));
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I create the permission(?:|s)$")
    public void createPermissionEntries()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get("LastAccountId");
        AccessInfo accessInfo = (AccessInfo) stepData.get("AccessInfo");
        Set<Permission> permissions = (Set<Permission>) stepData.get("Permissions");

        AccessPermissionCreator accessPermissionCreator = accessPermissionFactory.newCreator(currScope);
        accessPermissionCreator.setAccessInfoId(accessInfo.getId());

        try {
            primeException();
            AccessPermission accessPermission = null;
            stepData.remove("AccessPermissionCreator");
            stepData.remove("AccessPermission");
            stepData.remove("LastAccessPermission");
            for (Permission tmpPerm : permissions) {
                accessPermissionCreator.setPermission(tmpPerm);
                accessPermission = accessPermissionService.create(accessPermissionCreator);
            }
            stepData.put("AccessPermissionCreator", accessPermissionCreator);
            if (accessPermission != null) {
                stepData.put("AccessPermission", accessPermission);
                stepData.put("LastAccessPermission", accessPermission);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created permission$")
    public void findTheLastCreatedAccessPermission()
            throws Exception {

        stepData.remove("AccessPermissionFound");
        AccessPermission permission = (AccessPermission) stepData.get("LastAccessPermission");

        try {
            AccessPermission accessPermissionFound = accessPermissionService.find(permission.getScopeId(), permission.getId());
            stepData.put("AccessPermissionFound", accessPermissionFound);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last created access permission$")
    public void deleteLastCreatedPermission()
            throws Exception {

        AccessPermission permission = (AccessPermission) stepData.get("LastAccessPermission");

        try {
            primeException();
            accessPermissionService.delete(permission.getScopeId(), permission.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the permissions in scope (\\d+)$")
    public void countPermissionsForScope(Integer scope)
            throws Exception {

        KapuaId tmpId = getKapuaId(scope);
        AccessPermissionQuery tmpQuery = accessPermissionFactory.newQuery(tmpId);

        try {
            primeException();
            stepData.remove("Count");
            Long count = accessPermissionService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I check the sanity of the access info factory$")
    public void accessInfoServiceSanityCheck()
            throws Exception {

        try {
            primeException();

            assertNotNull(accessInfoFactory.newCreator(getKapuaId()));
            assertNotNull(accessInfoFactory.newEntity(null));
            assertNotNull(accessInfoFactory.newQuery(getKapuaId()));
            assertNotNull(accessInfoFactory.newListResult());

            AccessInfoCreator tmpCreator = new AccessInfoCreatorImpl(getKapuaId());
            assertNotNull(tmpCreator);
            tmpCreator.setUserId(getKapuaId());
            AccessInfoCreator tmpCreator2 = new AccessInfoCreatorImpl(tmpCreator);
            assertNotNull(tmpCreator2);
            assertEquals(tmpCreator.getUserId(), tmpCreator2.getUserId());

            AccessInfo tmpAccInfo = new AccessInfoImpl(getKapuaId());
            assertNotNull(tmpAccInfo);
            tmpAccInfo.setUserId(getKapuaId());

            AccessInfo tmpAccInfo2 = new AccessInfoImpl(tmpAccInfo);
            assertNotNull(tmpAccInfo2);
            assertNotNull(tmpAccInfo2.getUserId());

            tmpAccInfo2.setUserId(null);
            assertNull(tmpAccInfo2.getUserId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I check the sanity of the access permission factory$")
    public void accessPermissionFactorySanityCheck() {
        assertNotNull(accessPermissionFactory.newCreator(getKapuaId()));
        assertNotNull(accessPermissionFactory.newEntity(null));
        assertNotNull(accessPermissionFactory.newEntity(getKapuaId()));
        assertNotNull(accessPermissionFactory.newQuery(getKapuaId()));
        assertNotNull(accessPermissionFactory.newListResult());

        KapuaId tmpId = getKapuaId();
        AccessPermissionCreator tmpCreator = new AccessPermissionCreatorImpl(tmpId);
        assertNotNull(tmpCreator);
        assertNotNull(tmpCreator.getScopeId());
        assertEquals(tmpId, tmpCreator.getScopeId());

        AccessPermission tmpAccPerm = new AccessPermissionImpl(getKapuaId());
        assertNotNull(tmpAccPerm);
        tmpAccPerm.setAccessInfoId(getKapuaId());
        tmpAccPerm.setPermission(new PermissionImpl("test_domain", Actions.read, getKapuaId(), getKapuaId()));

        AccessPermission tmpAccPerm2 = new AccessPermissionImpl(tmpAccPerm);
        assertNotNull(tmpAccPerm2);
        assertEquals(tmpAccPerm.getAccessInfoId(), tmpAccPerm2.getAccessInfoId());
        assertEquals(tmpAccPerm.getPermission(), tmpAccPerm2.getPermission());

        tmpAccPerm.setAccessInfoId(null);
        assertNull(tmpAccPerm.getAccessInfoId());

        // No typo. This is by design. When an object permissions are null, when asked for them, a
        // new set of empty permissions is returned instead.
        tmpAccPerm.setPermission(null);
        assertNotNull(tmpAccPerm.getPermission());
    }

    @When("^I check the sanity of the access role factory$")
    public void accessRoleFactorySanityCheck()
            throws Exception {

        try {
            primeException();

            assertNotNull(accessRoleFactory.newCreator(getKapuaId()));
            assertNotNull(accessRoleFactory.newEntity(getKapuaId()));
            assertNotNull(accessRoleFactory.newQuery(getKapuaId()));
            assertNotNull(accessRoleFactory.newListResult());

            KapuaId tmpId = getKapuaId();
            AccessRoleCreator tmpCreator = new AccessRoleCreatorImpl(tmpId);
            assertNotNull(tmpCreator);
            assertNotNull(tmpCreator.getScopeId());
            assertEquals(tmpId, tmpCreator.getScopeId());

            AccessRole tmpRole = new AccessRoleImpl(getKapuaId());
            assertNotNull(tmpRole);
            tmpRole.setAccessInfoId(getKapuaId());
            tmpRole.setRoleId(getKapuaId());
            AccessRole tmpRole2 = new AccessRoleImpl(tmpRole);
            assertNotNull(tmpRole2);
            assertEquals(tmpRole.getRoleId(), tmpRole2.getRoleId());
            assertEquals(tmpRole.getAccessInfoId(), tmpRole2.getAccessInfoId());

            tmpRole.setAccessInfoId(null);
            assertNull(tmpRole.getAccessInfoId());

            tmpRole.setRoleId(null);
            assertNull(tmpRole.getRoleId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^A role entity was created$")
    public void checkThatRoleWasCreated() {
        Role role = (Role) stepData.get("Role");
        assertNotNull(role);
    }

    @Then("^An access role entity was created$")
    public void checkThatAccessRoleWasCreated() {
        AccessRole accessRole = (AccessRole) stepData.get("AccessRole");
        assertNotNull(accessRole);
    }

    @Then("^I find an access role entity$")
    public void checkThatAnAccessRoleEntityWasFound() {
        AccessRole accessRoleFound = (AccessRole) stepData.get("AccessRoleFound");
        assertNotNull(accessRoleFound);
    }

    @Then("^An access info entity was created$")
    public void checkThatAccessInfoEntityExists() {
        AccessInfo accessInfo = (AccessInfo) stepData.get("AccessInfo");
        assertNotNull(accessInfo);
    }

    @Then("^I find an accessinfo entity$")
    public void checkThatAnAccessInfoEntityWasFound() {
        AccessInfo accessInfoFound = (AccessInfo) stepData.get("AccessInfoFound");
        assertNotNull(accessInfoFound);
    }

    @Then("^I find no access info entity$")
    public void checkThatAnAccessInfoEntityWasNotFound() {
        AccessInfo accessInfoFound = (AccessInfo) stepData.get("AccessInfoFound");
        assertNull(accessInfoFound);
    }

    @Then("^I find an access permission entity$")
    public void checkThatAnAccessPermissionWasFound() {
        AccessPermission accessPermissionFound = (AccessPermission) stepData.get("AccessPermissionFound");
        assertNotNull(accessPermissionFound);
    }

    @Then("^There are no such access permissions$")
    public void checkThatThePermissionsWereRemoved() {
        AccessPermissionListResult accessPermissions = (AccessPermissionListResult) stepData.get("AccessPermissions");
        assertEquals(0, accessPermissions.getSize());
    }

    @Then("^There are no such access roles$")
    public void checkThatTheRolesWereRemoved() {
        AccessRoleListResult accessRoles = (AccessRoleListResult) stepData.get("AccessRoles");
        assertEquals(0, accessRoles.getSize());
    }

    @Then("^The entity matches the creator$")
    public void checkEntityAgainstCreator() {

        AccessInfoCreator accessInfoCreator = (AccessInfoCreator) stepData.get("AccessInfoCreator");
        AccessInfo accessInfo = (AccessInfo) stepData.get("AccessInfo");

        assertEquals(accessInfoCreator.getUserId(), accessInfo.getUserId());
        assertEquals(accessInfoCreator.getScopeId(), accessInfo.getScopeId());
    }

    @Then("^The permissions match the creator$")
    public void checkAccessInfoEntityPermissions() {

        AccessInfoCreator accessInfoCreator = (AccessInfoCreator) stepData.get("AccessInfoCreator");
        AccessPermissionListResult accessPermissions = (AccessPermissionListResult) stepData.get("AccessPermissions");

        assertEquals(accessInfoCreator.getPermissions().size(), accessPermissions.getSize());

        for (int i = 0; i < accessPermissions.getSize(); i++) {
            assertTrue(accessInfoCreator.getPermissions().contains(accessPermissions.getItem(i).getPermission()));
        }
    }

    @Then("^The access info roles match the creator$")
    public void checkAccessInfoEntityRoles() {

        AccessInfoCreator accessInfoCreator = (AccessInfoCreator) stepData.get("AccessInfoCreator");
        AccessRoleListResult accessRoles = (AccessRoleListResult) stepData.get("AccessRoles");

        assertNotEquals(0, accessRoles.getSize());
        assertEquals(accessInfoCreator.getRoleIds().size(), accessRoles.getSize());

        for (int i = 0; i < accessRoles.getSize(); i++) {
            assertTrue(accessInfoCreator.getRoleIds().contains(accessRoles.getItem(i).getRoleId()));
        }
    }

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Access Role object equals function.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^I can compare access role objects$")
    public void checkAccessRoleComparison() {

        AccessRoleImpl accRole1 = new AccessRoleImpl(getKapuaId());
        AccessRoleImpl accRole2 = new AccessRoleImpl(getKapuaId());

        assertTrue(accRole1.equals(accRole1));
        assertFalse(accRole1.equals(null));
        assertFalse(accRole1.equals(Integer.valueOf(15)));

        assertTrue(accRole1.equals(accRole2));

        accRole2.setAccessInfoId(getKapuaId());
        assertFalse(accRole1.equals(accRole2));

        accRole1.setAccessInfoId(getKapuaId());
        accRole2.setAccessInfoId(null);
        assertFalse(accRole1.equals(accRole2));

        accRole2.setAccessInfoId(accRole1.getAccessInfoId());
        assertTrue(accRole1.equals(accRole2));

        accRole2.setRoleId(getKapuaId());
        assertFalse(accRole1.equals(accRole2));

        accRole1.setRoleId(getKapuaId());
        accRole2.setRoleId(null);
        assertFalse(accRole1.equals(accRole2));

        accRole2.setRoleId(accRole1.getRoleId());
        assertTrue(accRole1.equals(accRole2));
    }

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Access Role object equals function.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^I can compare access permission objects$")
    public void checkAccessPermissionComparison() {

        AccessPermissionImpl accPerm1 = new AccessPermissionImpl(getKapuaId());
        AccessPermissionImpl accPerm2 = new AccessPermissionImpl(getKapuaId());
        Permission tmpPerm1 = new PermissionImpl("test_domain", Actions.read, SYS_SCOPE_ID, getKapuaId());
        Permission tmpPerm2 = new PermissionImpl("test_domain", Actions.write, SYS_SCOPE_ID, getKapuaId());

        assertTrue(accPerm1.equals(accPerm1));
        assertFalse(accPerm1.equals(null));
        assertFalse(accPerm1.equals(Integer.valueOf(15)));

        assertTrue(accPerm1.equals(accPerm2));

        accPerm1.setAccessInfoId(null);
        accPerm2.setAccessInfoId(getKapuaId());
        assertFalse(accPerm1.equals(accPerm2));

        accPerm1.setAccessInfoId(getKapuaId());
        accPerm2.setAccessInfoId(null);
        assertFalse(accPerm1.equals(accPerm2));

        accPerm1.setAccessInfoId(getKapuaId());
        accPerm2.setAccessInfoId(getKapuaId());
        assertFalse(accPerm1.equals(accPerm2));

        accPerm2.setAccessInfoId(accPerm1.getAccessInfoId());
        assertTrue(accPerm1.equals(accPerm2));

        accPerm1.setPermission(null);
        accPerm2.setPermission(tmpPerm2);
        assertFalse(accPerm1.equals(accPerm2));

        accPerm1.setPermission(tmpPerm1);
        accPerm2.setPermission(null);
        assertFalse(accPerm1.equals(accPerm2));

        accPerm1.setPermission(tmpPerm1);
        accPerm2.setPermission(tmpPerm2);
        assertFalse(accPerm1.equals(accPerm2));

        accPerm2.setPermission(accPerm1.getPermission());
        assertTrue(accPerm1.equals(accPerm2));
    }

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Authorization Permission factory.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^The permission factory returns sane results$")
    public void permissionFactorySanityChecks()
            throws KapuaException {

        Permission tmpPerm = null;
        TestDomain tmpDomain = new TestDomain();

        tmpPerm = permissionFactory.newPermission(tmpDomain, Actions.read, SYS_SCOPE_ID);
        assertNotNull(tmpPerm);
        assertNotNull(tmpPerm.getDomain());
        assertEquals(tmpDomain.getName(), tmpPerm.getDomain());
        assertEquals(Actions.read, tmpPerm.getAction());

        tmpPerm = permissionFactory.newPermission(tmpDomain, Actions.write, SYS_SCOPE_ID, getKapuaId(9));
        assertNotNull(tmpPerm);
        assertNotNull(tmpPerm.getDomain());
        assertEquals(tmpDomain.getName(), tmpPerm.getDomain());
        assertEquals(Actions.write, tmpPerm.getAction());
        assertEquals(getKapuaId(9), tmpPerm.getGroupId());
        assertFalse(tmpPerm.getForwardable());

        tmpPerm = permissionFactory.newPermission(null, Actions.execute, SYS_SCOPE_ID, getKapuaId(9), true);
        assertNotNull(tmpPerm);
        assertEquals(Actions.execute, tmpPerm.getAction());
        assertTrue(tmpPerm.getForwardable());

        tmpDomain.setName(null);
        tmpPerm = permissionFactory.newPermission(tmpDomain, Actions.connect, SYS_SCOPE_ID, getKapuaId());
        assertNotNull(tmpPerm);
        assertEquals(Actions.connect, tmpPerm.getAction());
    }

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Authorization Permission object equals function.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^I can compare permission objects$")
    public void checkPermissionComparison() {

        Permission perm1 = new PermissionImpl("test_domain_1", Actions.read, getKapuaId(10), getKapuaId(100));
        Permission perm2 = new PermissionImpl("test_domain_1", Actions.read, getKapuaId(10), getKapuaId(100));

        assertTrue(perm1.equals(perm1));
        assertFalse(perm1.equals(null));
        assertFalse(perm1.equals(Integer.valueOf(10)));

        assertTrue(perm1.equals(perm2));

        perm1.setDomain(null);
        assertFalse(perm1.equals(perm2));
        perm2.setDomain(null);
        assertTrue(perm1.equals(perm2));
        perm1.setDomain("test_1");
        assertFalse(perm1.equals(perm2));
        perm2.setDomain("test_2");
        assertFalse(perm1.equals(perm2));

        perm1.setDomain("test");
        perm2.setDomain("test");

        perm1.setTargetScopeId(null);
        assertFalse(perm1.equals(perm2));
        perm2.setTargetScopeId(null);
        assertTrue(perm1.equals(perm2));
        perm1.setTargetScopeId(getKapuaId(10));
        assertFalse(perm1.equals(perm2));
        perm2.setTargetScopeId(getKapuaId(15));
        assertFalse(perm1.equals(perm2));

        perm1.setTargetScopeId(getKapuaId(10));
        perm2.setTargetScopeId(getKapuaId(10));

        perm1.setGroupId(null);
        assertFalse(perm1.equals(perm2));
        perm2.setGroupId(null);
        assertTrue(perm1.equals(perm2));
        perm1.setGroupId(getKapuaId(100));
        assertFalse(perm1.equals(perm2));
        perm2.setGroupId(getKapuaId(101));
        assertFalse(perm1.equals(perm2));
        perm2.setGroupId(getKapuaId(100));
        assertTrue(perm1.equals(perm2));

        perm1.setAction(Actions.read);
        perm2.setAction(Actions.write);
        assertFalse(perm1.equals(perm2));
    }
}
