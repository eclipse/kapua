/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
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
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.AuthorizationJAXBContextProvider;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.test.MockedLocator;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of Gherkin steps used in AccessInfoService.feature scenarios.
 */
@ScenarioScoped
public class AccessInfoServiceSteps extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(AccessInfoServiceSteps.class);
    private static final TestDomain TEST_DOMAIN = new TestDomain();

    // Various Access service related service references
    private AccessInfoService accessInfoService;
    private AccessInfoFactory accessInfoFactory;
    private AccessPermissionService accessPermissionService;
    private AccessPermissionFactory accessPermissionFactory;
    private AccessRoleService accessRoleService;
    private AccessRoleFactory accessRoleFactory;

    // References to dependent services
    private PermissionFactory permissionFactory;
    private RoleService roleService;
    private RoleFactory roleFactory;

    @Inject
    public AccessInfoServiceSteps(StepData stepData, DBHelper dbHelper) {

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
                bind(AccessInfoService.class).toInstance(new AccessInfoServiceImpl());
                bind(AccessInfoFactory.class).toInstance(new AccessInfoFactoryImpl());
                bind(AccessPermissionService.class).toInstance(new AccessPermissionServiceImpl());
                bind(AccessPermissionFactory.class).toInstance(new AccessPermissionFactoryImpl());
                bind(AccessRoleService.class).toInstance(new AccessRoleServiceImpl());
                bind(AccessRoleFactory.class).toInstance(new AccessRoleFactoryImpl());
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }

    // Setup and tear-down steps
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

        permissionFactory = locator.getFactory(PermissionFactory.class);
        roleService = locator.getService(RoleService.class);
        roleFactory = locator.getFactory(RoleFactory.class);

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

    @Given("^The permission(?:|s) \"(.+)\"$")
    public void createPermissionsForDomain(String permList) {

        // Split the parameter string and make sure there is at least one item
        String[] tmpList = permList.toLowerCase().split(",");
        assertNotNull(tmpList);
        assertNotEquals(0, tmpList.length);

        // Parse the items and fill the list
        Set<Permission> permissions = new HashSet<>();
        KapuaId currId = (KapuaId) stepData.get("CurrentScope");

        for (String perm : tmpList) {
            switch (perm.trim()) {
                case "read":
                    permissions.add(permissionFactory.newPermission(TEST_DOMAIN, Actions.read, currId));
                    break;
                case "write":
                    permissions.add(permissionFactory.newPermission(TEST_DOMAIN, Actions.write, currId));
                    break;
                case "delete":
                    permissions.add(permissionFactory.newPermission(TEST_DOMAIN, Actions.delete, currId));
                    break;
                case "connect":
                    permissions.add(permissionFactory.newPermission(TEST_DOMAIN, Actions.connect, currId));
                    break;
                case "execute":
                    permissions.add(permissionFactory.newPermission(TEST_DOMAIN, Actions.execute, currId));
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

        KapuaId currId = (KapuaId) stepData.get("CurrentScope");
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

        KapuaId currScope = (KapuaId) stepData.get("CurrentScope");
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

        KapuaId currScope = (KapuaId) stepData.get("LastScope");
        User tmpUser = (User) stepData.get("User");
        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(currScope);
        accessInfoCreator.setUserId(tmpUser.getId());

        stepData.remove("Permissions");
        stepData.remove("RoleIds");

        try {
            primeException();
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

        KapuaId currScope = (KapuaId) stepData.get("LastScope");
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

        KapuaId currScope = (KapuaId) stepData.get("LastScope");
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

        KapuaId currScope = (KapuaId) stepData.get("LastScope");
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
            long count = accessRoleService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last created access role entry$")
    public void deleteLastCreatedAccessRoleEntry()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get("LastScope");
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

        KapuaId currScope = (KapuaId) stepData.get("LastScope");
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
            long count = accessInfoService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for the access info entities for the last user$")
    public void queryForLastUserAccessInfoEntities()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get("LastScope");
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
                stepData.put("Count", accessList.getSize());
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I create the permission(?:|s)$")
    public void createPermissionEntries()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get("LastScope");
        AccessInfo accessInfo = (AccessInfo) stepData.get("AccessInfo");
        Set<Permission> permissions = (Set<Permission>) stepData.get("Permissions");

        AccessPermissionCreator accessPermissionCreator = accessPermissionFactory.newCreator(currScope);
        accessPermissionCreator.setAccessInfoId(accessInfo.getId());

        try {
            primeException();
            AccessPermission accessPermission = null;
            stepData.remove("AccessPermissionCreator");
            stepData.remove("AccessPermission");
            for (Permission tmpPerm : permissions) {
                accessPermissionCreator.setPermission(tmpPerm);
                accessPermission = accessPermissionService.create(accessPermissionCreator);
            }
            stepData.put("AccessPermissionCreator", accessPermissionCreator);
            if (accessPermission != null) {
                stepData.put("AccessPermission", accessPermission);
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
            stepData.remove("IntValue");
            long count = accessPermissionService.count(tmpQuery);
            stepData.put("IntValue", count);
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
}
