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

import javax.inject.Inject;

import cucumber.api.java.After;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.KapuaContainer;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoPredicates;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermissionQuery;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
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
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.UserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

/**
 * Implementation of Gherkin steps used in AccessInfoService.feature scenarios.
 *
 */
@ScenarioScoped
public class AccessInfoServiceTestSteps extends AbstractAuthorizationServiceTest {

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(AccessInfoServiceTestSteps.class);

    private static final Domain testDomain = new TestDomain();

    // Test data scratchpads
    private CommonTestData commonData = null;
    private AccessInfoServiceTestData accessData = null;

    // Various Access service related service references
    private AccessInfoService accessInfoService = null;
    private AccessInfoFactory accessInfoFactory = null;
    private AccessPermissionService accessPermissionService = null;
    private AccessPermissionFactory accessPermissionFactory = null;
    private AccessRoleService accessRoleService = null;
    private AccessRoleFactory accessRoleFactory = null;

    // References to dependent services
    private UserService userService = null;
    private UserFactory userFactory = null;
    private PermissionFactory permissionFactory = null;
    private RoleService roleService = null;
    private RoleFactory roleFactory = null;

    // Currently executing scenario.
    @SuppressWarnings("unused")
    private Scenario scenario;

    @Inject
    public AccessInfoServiceTestSteps(AccessInfoServiceTestData accessData, CommonTestData commonData) {
        this.accessData = accessData;
        this.commonData = commonData;
    }

    // Setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario)
            throws Exception {
        container = new KapuaContainer() {};
        container.startup();
        locator = KapuaLocator.getInstance();

        this.scenario = scenario;

        // Instantiate all the services and factories that are required by the tests
        accessInfoService = new AccessInfoServiceImpl();
        accessInfoFactory = new AccessInfoFactoryImpl();
        accessPermissionService = new AccessPermissionServiceImpl();
        accessPermissionFactory = new AccessPermissionFactoryImpl();
        accessRoleService = new AccessRoleServiceImpl();
        accessRoleFactory = new AccessRoleFactoryImpl();

        userService = locator.getService(UserService.class);
        userFactory = locator.getFactory(UserFactory.class);
        permissionFactory = locator.getFactory(PermissionFactory.class);

        roleService = locator.getService(RoleService.class);
        roleFactory = locator.getFactory(RoleFactory.class);

        // Clean up the database. A clean slate is needed for truly independent
        // test case executions!
        dropDatabase();
        setupDatabase();

        // Clean up the test data scratchpads
        accessData.clearData();
        commonData.clearData();
    }

    @After
    public void afterScenario() throws KapuaException {
        container.shutdown();
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

    @Given("^A user such as$")
    public void createUser(List<UserImpl> users)
            throws KapuaException {

        // The scope ID must be defined for this test step!
        assertNotNull(commonData.scopeId);

        // Sanity checks; There must be exactly one user defined by the test step
        assertNotNull(users);
        assertEquals(1, users.size());

        // Additional sanity checks; At least the user name must be specified.
        User tmpUsr = users.get(0);
        assertNotNull(tmpUsr);
        assertNotNull(tmpUsr.getName());

        // Prepare test user data
        accessData.userCreator = userFactory.newCreator(commonData.scopeId, tmpUsr.getName());
        assertNotNull(accessData.userCreator);

        if (tmpUsr.getDisplayName() != null && !tmpUsr.getDisplayName().isEmpty()) {
            accessData.userCreator.setDisplayName(tmpUsr.getDisplayName());
        } else {
            accessData.userCreator.setDisplayName(tmpUsr.getName() + " display");
        }

        if (tmpUsr.getEmail() != null && !tmpUsr.getEmail().isEmpty()) {
            accessData.userCreator.setEmail(tmpUsr.getEmail());
        } else {
            accessData.userCreator.setEmail(tmpUsr.getName() + "@test.company.org");
        }

        if (tmpUsr.getPhoneNumber() != null && !tmpUsr.getPhoneNumber().isEmpty()) {
            accessData.userCreator.setPhoneNumber(tmpUsr.getPhoneNumber());
        } else {
            accessData.userCreator.setPhoneNumber(BigInteger.valueOf(random.nextLong()).toString());
        }

        // Create the actual user in the database
        KapuaSecurityUtils.doPrivileged(() -> {
            accessData.user = userService.create(accessData.userCreator);
            return null;
        });
        assertNotNull(accessData.user);
        assertNotNull(accessData.user.getId());
    }

    @Given("^The permission(?:|s) \"(.+)\"$")
    public void createPermissionsForDomain(String permList) {
        // Sanity checks
        // The scope ID must be defined
        assertNotNull(commonData.scopeId);

        // Split the parameter string and make sure there is at least one
        // item
        String[] tmpList = permList.toLowerCase().split(",");
        assertNotNull(tmpList);
        assertNotEquals(0, tmpList.length);

        // Parse the items and fill the list
        accessData.permissions = new HashSet<>();
        for (String perm : tmpList) {
            switch (perm.trim()) {
            case "read":
                accessData.permissions.add(permissionFactory.newPermission(testDomain, Actions.read, commonData.scopeId));
                break;
            case "write":
                accessData.permissions.add(permissionFactory.newPermission(testDomain, Actions.write, commonData.scopeId));
                break;
            case "delete":
                accessData.permissions.add(permissionFactory.newPermission(testDomain, Actions.delete, commonData.scopeId));
                break;
            case "connect":
                accessData.permissions.add(permissionFactory.newPermission(testDomain, Actions.connect, commonData.scopeId));
                break;
            case "execute":
                accessData.permissions.add(permissionFactory.newPermission(testDomain, Actions.execute, commonData.scopeId));
                break;
            }
        }
        // Make sure that there is at least one valid item
        assertFalse(accessData.permissions.isEmpty());
    }

    @Given("^The role \"(.*)\"$")
    public void provideRoleForDomain(String name)
            throws KapuaException {

        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.permissions);
        assertFalse(accessData.permissions.isEmpty());

        assertNotNull(name);
        assertFalse(name.isEmpty());

        accessData.roleCreator = roleFactory.newCreator(commonData.scopeId);
        assertNotNull(accessData.roleCreator);

        accessData.roleCreator.setName(name);
        accessData.roleCreator.setPermissions(accessData.permissions);

        try {
            commonData.exceptionCaught = false;
            KapuaSecurityUtils.doPrivileged(() -> {
                accessData.role = roleService.create(accessData.roleCreator);
                return null;
            });
        } catch (KapuaException e) {
            commonData.exceptionCaught = true;
            return;
        }
        assertNotNull(accessData.role);
        assertNotNull(accessData.role.getId());

        accessData.roleIds = new HashSet<>();
        accessData.roleIds.add(accessData.role.getId());
        assertEquals(1, accessData.roleIds.size());
    }

    @Given("^An invalid role ID$")
    public void provideInvalidRoleObjectID() {
        accessData.roleIds = new HashSet<>();
        accessData.roleIds.add(generateId());
        assertEquals(1, accessData.roleIds.size());
    }

    @Given("^An invalid user$")
    public void provideInvalidUserObject() {
        accessData.user = new UserImpl(generateId(), generateId().toString());
        accessData.user.setId(generateId());
        assertNotNull(accessData.user);
    }

    @When("^I create the access role$")
    public void createAccessRole()
            throws KapuaException {

        assertNotNull(commonData.scopeId);

        AccessRoleCreator tmpCreator = accessRoleFactory.newCreator(commonData.scopeId);
        assertNotNull(tmpCreator);

        tmpCreator.setAccessInfoId(generateId());
        tmpCreator.setRoleId(generateId());

        try {
            commonData.exceptionCaught = false;
            KapuaSecurityUtils.doPrivileged(() -> {
                accessData.accessRole = accessRoleService.create(tmpCreator);
                return null;
            });
        } catch (KapuaException e) {
            commonData.exceptionCaught = true;
        }
    }

    @When("^I create the access info entity$")
    public void createAccessInfoEntity() {

        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.user);

        accessData.accessInfoCreator = accessInfoFactory.newCreator(commonData.scopeId);
        assertNotNull(accessData.accessInfoCreator);

        accessData.accessInfoCreator.setUserId(accessData.user.getId());

        if (accessData.permissions != null && !accessData.permissions.isEmpty()) {
            accessData.accessInfoCreator.setPermissions(accessData.permissions);
        } else {
            accessData.accessInfoCreator.setPermissions(null);
        }

        if (accessData.roleIds != null && !accessData.roleIds.isEmpty()) {
            accessData.accessInfoCreator.setRoleIds(accessData.roleIds);
        } else {
            accessData.accessInfoCreator.setRoleIds(null);
        }

        try {
            KapuaSecurityUtils.doPrivileged(() -> {
                commonData.exceptionCaught = false;
                accessData.accessInfo = accessInfoService.create(accessData.accessInfoCreator);
                return null;
            });
        } catch (KapuaException ex) {
            commonData.exceptionCaught = true;
        }
    }

    @When("^I search for the permissions of the last access info entity$")
    public void findThePermissionsOfTheLastAccessInfoEntity()
            throws KapuaException {
        assertNotNull(accessData.accessInfo);
        assertNotNull(accessData.accessInfo.getScopeId());
        assertNotNull(accessData.accessInfo.getId());

        KapuaSecurityUtils.doPrivileged(() -> {
            accessData.accessPermissions = accessPermissionService.findByAccessInfoId(
                    accessData.accessInfo.getScopeId(), accessData.accessInfo.getId());
            return null;
        });
    }

    @When("^I search for the roles of the last access info entity$")
    public void findTheRolesOfTheLastAccessInfoEntity()
            throws KapuaException {
        assertNotNull(accessData.accessInfo);
        assertNotNull(accessData.accessInfo.getScopeId());
        assertNotNull(accessData.accessInfo.getId());

        KapuaSecurityUtils.doPrivileged(() -> {
            accessData.accessRoles = accessRoleService.findByAccessInfoId(
                    accessData.accessInfo.getScopeId(), accessData.accessInfo.getId());
            return null;
        });
    }

    @When("^I search for the last created access info entity$")
    public void findLastCreatedAccessInfoEntity()
            throws KapuaException {
        assertNotNull(accessData.accessInfo);
        assertNotNull(accessData.accessInfo.getScopeId());
        assertNotNull(accessData.accessInfo.getId());

        KapuaSecurityUtils.doPrivileged(() -> {
            accessData.accessInfoFound = null;
            accessData.accessInfoFound = accessInfoService.find(
                    accessData.accessInfo.getScopeId(), accessData.accessInfo.getId());
            return null;
        });
    }

    @When("^I search for an access info entity by user ID$")
    public void findTheAccessInfoEntityByUserId()
            throws KapuaException {
        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.user);
        assertNotNull(accessData.user.getId());

        KapuaSecurityUtils.doPrivileged(() -> {
            accessData.accessInfoFound = accessInfoService.findByUserId(commonData.scopeId, accessData.user.getId());
            return null;
        });
    }

    @When("^I search for the last created access role entity$")
    public void findLastCreatedAccessRole()
            throws KapuaException {
        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.accessRole);
        assertNotNull(accessData.accessRole.getId());

        KapuaSecurityUtils.doPrivileged(() -> {
            accessData.accessRoleFound = accessRoleService.find(commonData.scopeId, accessData.accessRole.getId());
            return null;
        });
    }

    @When("^I count the access roles in scope (\\d+)$")
    public void countAccesRolesInScope(Integer scope)
            throws KapuaException {
        assertNotNull(scope);
        AccessRoleQuery tmpQuery = accessRoleFactory.newQuery(new KapuaEid(BigInteger.valueOf(scope)));

        KapuaSecurityUtils.doPrivileged(() -> {
            commonData.count = accessRoleService.count(tmpQuery);
            return null;
        });
    }

    @When("^I delete the last created access role entry$")
    public void deleteLastCreatedAccessRoleEntry()
            throws KapuaException {
        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.accessRole);
        assertNotNull(accessData.accessRole.getId());

        try {
            commonData.exceptionCaught = false;
            KapuaSecurityUtils.doPrivileged(() -> {
                accessRoleService.delete(commonData.scopeId, accessData.accessRole.getId());
                return null;
            });
        } catch (KapuaException e) {
            commonData.exceptionCaught = true;
        }
    }

    @When("^I delete the existing access info entity$")
    public void deleteLastCreatedAccessInfoEntity()
            throws KapuaException {
        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.accessInfo);
        assertNotNull(accessData.accessInfo.getId());

        try {
            commonData.exceptionCaught = false;
            KapuaSecurityUtils.doPrivileged(() -> {
                accessInfoService.delete(commonData.scopeId, accessData.accessInfo.getId());
                return null;
            });
        } catch (KapuaException ex) {
            commonData.exceptionCaught = true;
        }
    }

    @When("^I count the access info entities for scope (\\d+)$")
    public void countAccessInfoEntitiesInScope(Integer scope)
            throws KapuaException {
        assertNotNull(scope);
        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(scope));
        AccessInfoQuery tmpQuery = accessInfoFactory.newQuery(tmpId);

        KapuaSecurityUtils.doPrivileged(() -> {
            commonData.count = accessInfoService.count(tmpQuery);
            return null;
        });
    }

    @When("^I query for the access info entities for the last user$")
    public void queryForLastUserAccessInfoEntities()
            throws KapuaException {
        assertNotNull(accessData.user);
        assertNotNull(accessData.user.getId());
        assertNotNull(commonData.scopeId);

        AccessInfoQuery tmpQuery = accessInfoFactory.newQuery(commonData.scopeId);
        tmpQuery.setPredicate(new AttributePredicate<>(AccessInfoPredicates.USER_ID, accessData.user.getId()));
        accessData.accessList = accessInfoFactory.newListResult();

        KapuaSecurityUtils.doPrivileged(() -> {
            accessData.accessList = accessInfoService.query(tmpQuery);
            return null;
        });

        if (accessData.accessList != null) {
            commonData.count = accessData.accessList.getSize();
        }
    }

    @When("^I create the permission(?:|s)$")
    public void createPermissionEntries()
            throws KapuaException {
        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.permissions);
        assertFalse(accessData.permissions.isEmpty());

        accessData.accessPermissionCreator = accessPermissionFactory.newCreator(commonData.scopeId);
        accessData.accessPermissionCreator.setAccessInfoId(generateId());

        try {
            commonData.exceptionCaught = false;
            for (Permission tmpPerm : accessData.permissions) {
                accessData.accessPermissionCreator.setPermission(tmpPerm);
                KapuaSecurityUtils.doPrivileged(() -> {
                    accessData.accessPermission = accessPermissionService.create(accessData.accessPermissionCreator);
                    return null;
                });
            }
        } catch (KapuaException ex) {
            commonData.exceptionCaught = true;
        }
    }

    @When("^I search for the last created permission$")
    public void findTheLastCreatedAccessPermission()
            throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            accessData.accessPermissionFound = accessPermissionService.find(commonData.scopeId, accessData.accessPermission.getId());
            return null;
        });
    }

    @When("^I delete the last created access permission$")
    public void deleteLastCreatedPermission()
            throws KapuaException {

        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.accessPermission);

        try {
            commonData.exceptionCaught = false;
            KapuaSecurityUtils.doPrivileged(() -> {
                accessPermissionService.delete(commonData.scopeId, accessData.accessPermission.getId());
                return null;
            });
        } catch (KapuaException ex) {
            commonData.exceptionCaught = true;
        }
    }

    @When("^I count the permissions in scope (\\d+)$")
    public void countPermissionsForScope(Integer scope)
            throws KapuaException {
        assertNotNull(scope);
        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(scope));
        assertNotNull(tmpId);

        AccessPermissionQuery tmpQuery = accessPermissionFactory.newQuery(tmpId);
        KapuaSecurityUtils.doPrivileged(() -> {
            commonData.count = accessPermissionService.count(tmpQuery);
            return null;
        });
    }

    @When("^I check the sanity of the access info factory$")
    public void accessInfoServiceSanityCheck() {
        try {
            commonData.exceptionCaught = false;

            assertNotNull(accessInfoFactory.newCreator(generateId()));
            assertNotNull(accessInfoFactory.newEntity(null));
            assertNotNull(accessInfoFactory.newQuery(generateId()));
            assertNotNull(accessInfoFactory.newListResult());

            AccessInfoCreator tmpCreator = new AccessInfoCreatorImpl(generateId());
            assertNotNull(tmpCreator);
            tmpCreator.setUserId(generateId());
            AccessInfoCreator tmpCreator_2 = new AccessInfoCreatorImpl(tmpCreator);
            assertNotNull(tmpCreator_2);
            assertEquals(tmpCreator.getUserId(), tmpCreator_2.getUserId());

            AccessInfo tmpAccInfo = new AccessInfoImpl(generateId());
            assertNotNull(tmpAccInfo);
            tmpAccInfo.setUserId(generateId());

            AccessInfo tmpAccInfo_2 = new AccessInfoImpl(tmpAccInfo);
            assertNotNull(tmpAccInfo_2);
            assertNotNull(tmpAccInfo_2.getUserId());

            tmpAccInfo_2.setUserId(null);
            assertNull(tmpAccInfo_2.getUserId());
        } catch (KapuaException ex) {
            commonData.exceptionCaught = true;
        }
    }

    @When("^I check the sanity of the access permission factory$")
    public void accessPermissionFactorySanityCheck() {
        assertNotNull(accessPermissionFactory.newCreator(generateId()));
        assertNotNull(accessPermissionFactory.newEntity(null));
        assertNotNull(accessPermissionFactory.newEntity(generateId()));
        assertNotNull(accessPermissionFactory.newQuery(generateId()));
        assertNotNull(accessPermissionFactory.newListResult());

        KapuaId tmpId = generateId();
        AccessPermissionCreator tmpCreator = new AccessPermissionCreatorImpl(tmpId);
        assertNotNull(tmpCreator);
        assertNotNull(tmpCreator.getScopeId());
        assertEquals(tmpId, tmpCreator.getScopeId());

        AccessPermission tmpAccPerm = new AccessPermissionImpl(generateId());
        assertNotNull(tmpAccPerm);
        tmpAccPerm.setAccessInfoId(generateId());
        tmpAccPerm.setPermission(new PermissionImpl("test_domain", Actions.read, generateId(), generateId()));

        AccessPermission tmpAccPerm_2 = new AccessPermissionImpl(tmpAccPerm);
        assertNotNull(tmpAccPerm_2);
        assertEquals(tmpAccPerm.getAccessInfoId(), tmpAccPerm_2.getAccessInfoId());
        assertEquals(tmpAccPerm.getPermission(), tmpAccPerm_2.getPermission());

        tmpAccPerm.setAccessInfoId(null);
        assertNull(tmpAccPerm.getAccessInfoId());

        // No typo. This is by design. When an object permissions are null, when asked for them, a 
        // new set of empty permissions is returned instead. 
        tmpAccPerm.setPermission(null);
        assertNotNull(tmpAccPerm.getPermission());
    }

    @When("^I check the sanity of the access role factory$")
    public void accessRoleFactorySanityCheck() {
        try {
            commonData.exceptionCaught = false;
            assertNotNull(accessRoleFactory.newCreator(generateId()));
            assertNotNull(accessRoleFactory.newEntity(generateId()));
            assertNotNull(accessRoleFactory.newQuery(generateId()));
            assertNotNull(accessRoleFactory.newListResult());

            KapuaId tmpId = generateId();
            AccessRoleCreator tmpCreator = new AccessRoleCreatorImpl(tmpId);
            assertNotNull(tmpCreator);
            assertNotNull(tmpCreator.getScopeId());
            assertEquals(tmpId, tmpCreator.getScopeId());

            AccessRole tmpRole = new AccessRoleImpl(generateId());
            assertNotNull(tmpRole);
            tmpRole.setAccessInfoId(generateId());
            tmpRole.setRoleId(generateId());
            AccessRole tmpRole_2 = new AccessRoleImpl(tmpRole);
            assertNotNull(tmpRole_2);
            assertEquals(tmpRole.getRoleId(), tmpRole_2.getRoleId());
            assertEquals(tmpRole.getAccessInfoId(), tmpRole_2.getAccessInfoId());

            tmpRole.setAccessInfoId(null);
            assertNull(tmpRole.getAccessInfoId());

            tmpRole.setRoleId(null);
            assertNull(tmpRole.getRoleId());
        } catch (KapuaException ex) {
            commonData.exceptionCaught = true;
        }
    }

    @Then("^A role entity was created$")
    public void checkThatRoleWasCreated() {
        assertNotNull(accessData.role);
    }

    @Then("^An access role entity was created$")
    public void checkThatAccessRoleWasCreated() {
        assertNotNull(accessData.accessRole);
    }

    @Then("^I find an access role entity$")
    public void checkThatAnAccessRoleEntityWasFound() {
        assertNotNull(accessData.accessRoleFound);
    }

    @Then("^An access info entity was created$")
    public void checkThatAccessInfoEntityExists() {
        assertNotNull(accessData.accessInfo);
    }

    @Then("^I find an accessinfo entity$")
    public void checkThatAnAccessInfoEntityWasFound() {
        assertNotNull(accessData.accessInfoFound);
    }

    @Then("^I find no access info entity$")
    public void checkThatAnAccessInfoEntityWasNotFound() {
        assertNull(accessData.accessInfoFound);
    }

    @Then("^I find an access permission entity$")
    public void checkThatAnAccessPermissionWasFound() {
        assertNotNull(accessData.accessPermissionFound);
    }

    @Then("^The entity matches the creator$")
    public void checkEntityAgainstCreator() {
        assertNotNull(accessData.accessInfoCreator);
        assertNotNull(accessData.accessInfo);
        assertEquals(accessData.accessInfoCreator.getUserId(), accessData.accessInfo.getUserId());
        assertEquals(accessData.accessInfoCreator.getScopeId(), accessData.accessInfo.getScopeId());
    }

    @Then("^The permissions match the creator$")
    public void checkAccessInfoEntityPermissions() {
        assertNotNull(accessData.accessPermissions);
        assertEquals(accessData.accessInfoCreator.getPermissions().size(), accessData.accessPermissions.getSize());

        for (int i = 0; i < accessData.accessPermissions.getSize(); i++) {
            assertTrue(accessData.accessInfoCreator.getPermissions().contains(accessData.accessPermissions.getItem(i).getPermission()));
        }
    }

    @Then("^The access info roles match the creator$")
    public void checkAccessInfoEntityRoles() {
        assertNotNull(accessData.accessRoles);
        assertNotEquals(0, accessData.accessRoles.getSize());
        assertEquals(accessData.accessInfoCreator.getRoleIds().size(), accessData.accessRoles.getSize());

        for (int i = 0; i < accessData.accessRoles.getSize(); i++) {
            assertTrue(accessData.accessInfoCreator.getRoleIds().contains(accessData.accessRoles.getItem(i).getRoleId()));
        }
    }

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Access Role object equals function.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^I can compare access role objects$")
    public void checkAccessRoleComparison()
            throws KapuaException {

        AccessRoleImpl accRole_1 = new AccessRoleImpl(generateId());
        AccessRoleImpl accRole_2 = new AccessRoleImpl(generateId());

        assertTrue(accRole_1.equals(accRole_1));
        assertFalse(accRole_1.equals(null));
        assertFalse(accRole_1.equals(Integer.valueOf(15)));

        assertTrue(accRole_1.equals(accRole_2));

        accRole_2.setAccessInfoId(generateId());
        assertFalse(accRole_1.equals(accRole_2));

        accRole_1.setAccessInfoId(generateId());
        accRole_2.setAccessInfoId(null);
        assertFalse(accRole_1.equals(accRole_2));

        accRole_2.setAccessInfoId(accRole_1.getAccessInfoId());
        assertTrue(accRole_1.equals(accRole_2));

        accRole_2.setRoleId(generateId());
        assertFalse(accRole_1.equals(accRole_2));

        accRole_1.setRoleId(generateId());
        accRole_2.setRoleId(null);
        assertFalse(accRole_1.equals(accRole_2));

        accRole_2.setRoleId(accRole_1.getRoleId());
        assertTrue(accRole_1.equals(accRole_2));
    }

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Access Role object equals function.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^I can compare access permission objects$")
    public void checkAccessPermissionComparison()
            throws KapuaException {

        AccessPermissionImpl accPerm_1 = new AccessPermissionImpl(generateId());
        AccessPermissionImpl accPerm_2 = new AccessPermissionImpl(generateId());
        Permission tmpPerm_1 = new PermissionImpl("test_domain", Actions.read, rootScopeId, generateId());
        Permission tmpPerm_2 = new PermissionImpl("test_domain", Actions.write, rootScopeId, generateId());

        assertTrue(accPerm_1.equals(accPerm_1));
        assertFalse(accPerm_1.equals(null));
        assertFalse(accPerm_1.equals(Integer.valueOf(15)));

        assertTrue(accPerm_1.equals(accPerm_2));

        accPerm_1.setAccessInfoId(null);
        accPerm_2.setAccessInfoId(generateId());
        assertFalse(accPerm_1.equals(accPerm_2));

        accPerm_1.setAccessInfoId(generateId());
        accPerm_2.setAccessInfoId(null);
        assertFalse(accPerm_1.equals(accPerm_2));

        accPerm_1.setAccessInfoId(generateId());
        accPerm_2.setAccessInfoId(generateId());
        assertFalse(accPerm_1.equals(accPerm_2));

        accPerm_2.setAccessInfoId(accPerm_1.getAccessInfoId());
        assertTrue(accPerm_1.equals(accPerm_2));

        accPerm_1.setPermission(null);
        accPerm_2.setPermission(tmpPerm_2);
        assertFalse(accPerm_1.equals(accPerm_2));

        accPerm_1.setPermission(tmpPerm_1);
        accPerm_2.setPermission(null);
        assertFalse(accPerm_1.equals(accPerm_2));

        accPerm_1.setPermission(tmpPerm_1);
        accPerm_2.setPermission(tmpPerm_2);
        assertFalse(accPerm_1.equals(accPerm_2));

        accPerm_2.setPermission(accPerm_1.getPermission());
        assertTrue(accPerm_1.equals(accPerm_2));
    }
}
