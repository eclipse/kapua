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

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoAttributes;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
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
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.UserImpl;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

/**
 * Implementation of Gherkin steps used in AccessInfoService.feature scenarios.
 */
@ScenarioScoped
public class AccessInfoServiceTestSteps extends AbstractAuthorizationServiceTest {

    private static final TestDomain TEST_DOMAIN = new TestDomain();

    // Test data scratchpads
    private CommonTestData commonData;
    private CommonTestSteps commonSteps;
    private AccessInfoServiceTestData accessData;

    // Various Access service related service references
    private AccessInfoService accessInfoService;
    private AccessInfoFactory accessInfoFactory;
    private AccessPermissionService accessPermissionService;
    private AccessPermissionFactory accessPermissionFactory;
    private AccessRoleService accessRoleService;
    private AccessRoleFactory accessRoleFactory;

    // References to dependent services
    private UserService userService;
    private UserFactory userFactory;
    private PermissionFactory permissionFactory;
    private RoleService roleService;
    private RoleFactory roleFactory;

    // Currently executing scenario.
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
        this.scenario = scenario;

        // Instantiate all the services and factories that are required by the tests
        accessInfoService = new AccessInfoServiceImpl();
        accessInfoFactory = new AccessInfoFactoryImpl();
        accessPermissionService = new AccessPermissionServiceImpl();
        accessPermissionFactory = new AccessPermissionFactoryImpl();
        accessRoleService = new AccessRoleServiceImpl();
        accessRoleFactory = new AccessRoleFactoryImpl();

        userService = LOCATOR.getService(UserService.class);
        userFactory = LOCATOR.getFactory(UserFactory.class);
        permissionFactory = LOCATOR.getFactory(PermissionFactory.class);

        roleService = LOCATOR.getService(RoleService.class);
        roleFactory = LOCATOR.getFactory(RoleFactory.class);

        // Clean up the database. A clean slate is needed for truly independent
        // test case executions!
        dropDatabase();
        setupDatabase();

        // Clean up the test data scratchpads
        accessData.clearData();
        commonData.clearData();
        commonData.scenario = this.scenario;
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
                    accessData.permissions.add(permissionFactory.newPermission(TEST_DOMAIN, Actions.read, commonData.scopeId));
                    break;
                case "write":
                    accessData.permissions.add(permissionFactory.newPermission(TEST_DOMAIN, Actions.write, commonData.scopeId));
                    break;
                case "delete":
                    accessData.permissions.add(permissionFactory.newPermission(TEST_DOMAIN, Actions.delete, commonData.scopeId));
                    break;
                case "connect":
                    accessData.permissions.add(permissionFactory.newPermission(TEST_DOMAIN, Actions.connect, commonData.scopeId));
                    break;
                case "execute":
                    accessData.permissions.add(permissionFactory.newPermission(TEST_DOMAIN, Actions.execute, commonData.scopeId));
                    break;
            }
        }
        // Make sure that there is at least one valid item
        assertFalse(accessData.permissions.isEmpty());
    }

    @Given("^The role \"(.*)\"$")
    public void provideRoleForDomain(String name)
            throws Exception {

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
            commonData.primeException();
            KapuaSecurityUtils.doPrivileged(() -> {
                accessData.role = roleService.create(accessData.roleCreator);
                return null;
            });
        } catch (KapuaException e) {
            commonData.verifyException(e);
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
            throws Exception {

        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.accessInfo);
        assertNotNull(accessData.accessInfo.getId());
        assertNotNull(accessData.role);
        assertNotNull(accessData.role.getId());

        AccessRoleCreator tmpCreator = accessRoleFactory.newCreator(commonData.scopeId);
        assertNotNull(tmpCreator);

        tmpCreator.setAccessInfoId(accessData.accessInfo.getId());
        tmpCreator.setRoleId(accessData.role.getId());

        try {
            commonData.primeException();
            KapuaSecurityUtils.doPrivileged(() -> {
                accessData.accessRole = accessRoleService.create(tmpCreator);
                return null;
            });
        } catch (KapuaException e) {
            commonData.verifyException(e);
        }
    }

    @When("^I create a clean access info entity$")
    public void createCleanAccessInfoEntity()
            throws Exception {

        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.user);

        accessData.accessInfoCreator = accessInfoFactory.newCreator(commonData.scopeId);
        assertNotNull(accessData.accessInfoCreator);

        accessData.accessInfoCreator.setUserId(accessData.user.getId());

        accessData.permissions = null;
        accessData.roleIds = null;

        try {
            commonData.primeException();
            KapuaSecurityUtils.doPrivileged(() -> {
                accessData.accessInfo = accessInfoService.create(accessData.accessInfoCreator);
                return null;
            });
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I create the access info entity$")
    public void createAccessInfoEntity()
            throws Exception {

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
            commonData.primeException();
            KapuaSecurityUtils.doPrivileged(() -> {
                accessData.accessInfo = accessInfoService.create(accessData.accessInfoCreator);
                return null;
            });
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
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
            throws Exception {
        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.accessRole);
        assertNotNull(accessData.accessRole.getId());

        try {
            commonData.primeException();
            KapuaSecurityUtils.doPrivileged(() -> {
                accessRoleService.delete(commonData.scopeId, accessData.accessRole.getId());
                return null;
            });
        } catch (KapuaException e) {
            commonData.verifyException(e);
        }
    }

    @When("^I delete the existing access info entity$")
    public void deleteLastCreatedAccessInfoEntity()
            throws Exception {
        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.accessInfo);
        assertNotNull(accessData.accessInfo.getId());

        try {
            commonData.primeException();
            KapuaSecurityUtils.doPrivileged(() -> {
                accessInfoService.delete(commonData.scopeId, accessData.accessInfo.getId());
                return null;
            });
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
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
        tmpQuery.setPredicate(new AttributePredicateImpl<>(AccessInfoAttributes.USER_ID, accessData.user.getId()));
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
            throws Exception {
        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.accessInfo);
        assertNotNull(accessData.accessInfo.getId());
        assertNotNull(accessData.permissions);
        assertFalse(accessData.permissions.isEmpty());

        accessData.accessPermissionCreator = accessPermissionFactory.newCreator(commonData.scopeId);
        accessData.accessPermissionCreator.setAccessInfoId(accessData.accessInfo.getId());

        try {
            commonData.primeException();
            for (Permission tmpPerm : accessData.permissions) {
                accessData.accessPermissionCreator.setPermission(tmpPerm);
                KapuaSecurityUtils.doPrivileged(() -> {
                    accessData.accessPermission = accessPermissionService.create(accessData.accessPermissionCreator);
                    return null;
                });
            }
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
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
            throws Exception {

        assertNotNull(commonData.scopeId);
        assertNotNull(accessData.accessPermission);

        try {
            commonData.primeException();
            KapuaSecurityUtils.doPrivileged(() -> {
                accessPermissionService.delete(commonData.scopeId, accessData.accessPermission.getId());
                return null;
            });
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
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
    public void accessInfoServiceSanityCheck()
            throws Exception {

        try {
            commonData.primeException();

            assertNotNull(accessInfoFactory.newCreator(generateId()));
            assertNotNull(accessInfoFactory.newEntity(null));
            assertNotNull(accessInfoFactory.newQuery(generateId()));
            assertNotNull(accessInfoFactory.newListResult());

            AccessInfoCreator tmpCreator = new AccessInfoCreatorImpl(generateId());
            assertNotNull(tmpCreator);
            tmpCreator.setUserId(generateId());
            AccessInfoCreator tmpCreator2 = new AccessInfoCreatorImpl(tmpCreator);
            assertNotNull(tmpCreator2);
            assertEquals(tmpCreator.getUserId(), tmpCreator2.getUserId());

            AccessInfo tmpAccInfo = new AccessInfoImpl(generateId());
            assertNotNull(tmpAccInfo);
            tmpAccInfo.setUserId(generateId());

            AccessInfo tmpAccInfo2 = new AccessInfoImpl(tmpAccInfo);
            assertNotNull(tmpAccInfo2);
            assertNotNull(tmpAccInfo2.getUserId());

            tmpAccInfo2.setUserId(null);
            assertNull(tmpAccInfo2.getUserId());
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
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
            commonData.primeException();

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
            AccessRole tmpRole2 = new AccessRoleImpl(tmpRole);
            assertNotNull(tmpRole2);
            assertEquals(tmpRole.getRoleId(), tmpRole2.getRoleId());
            assertEquals(tmpRole.getAccessInfoId(), tmpRole2.getAccessInfoId());

            tmpRole.setAccessInfoId(null);
            assertNull(tmpRole.getAccessInfoId());

            tmpRole.setRoleId(null);
            assertNull(tmpRole.getRoleId());
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
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

        AccessRoleImpl accRole1 = new AccessRoleImpl(generateId());
        AccessRoleImpl accRole2 = new AccessRoleImpl(generateId());

        assertTrue(accRole1.equals(accRole1));
        assertFalse(accRole1.equals(null));
        assertFalse(accRole1.equals(Integer.valueOf(15)));

        assertTrue(accRole1.equals(accRole2));

        accRole2.setAccessInfoId(generateId());
        assertFalse(accRole1.equals(accRole2));

        accRole1.setAccessInfoId(generateId());
        accRole2.setAccessInfoId(null);
        assertFalse(accRole1.equals(accRole2));

        accRole2.setAccessInfoId(accRole1.getAccessInfoId());
        assertTrue(accRole1.equals(accRole2));

        accRole2.setRoleId(generateId());
        assertFalse(accRole1.equals(accRole2));

        accRole1.setRoleId(generateId());
        accRole2.setRoleId(null);
        assertFalse(accRole1.equals(accRole2));

        accRole2.setRoleId(accRole1.getRoleId());
        assertTrue(accRole1.equals(accRole2));
    }

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Access Role object equals function.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^I can compare access permission objects$")
    public void checkAccessPermissionComparison()
            throws KapuaException {

        AccessPermissionImpl accPerm1 = new AccessPermissionImpl(generateId());
        AccessPermissionImpl accPerm2 = new AccessPermissionImpl(generateId());
        Permission tmpPerm1 = new PermissionImpl("test_domain", Actions.read, ROOT_SCOPE_ID, generateId());
        Permission tmpPerm2 = new PermissionImpl("test_domain", Actions.write, ROOT_SCOPE_ID, generateId());

        assertTrue(accPerm1.equals(accPerm1));
        assertFalse(accPerm1.equals(null));
        assertFalse(accPerm1.equals(Integer.valueOf(15)));

        assertTrue(accPerm1.equals(accPerm2));

        accPerm1.setAccessInfoId(null);
        accPerm2.setAccessInfoId(generateId());
        assertFalse(accPerm1.equals(accPerm2));

        accPerm1.setAccessInfoId(generateId());
        accPerm2.setAccessInfoId(null);
        assertFalse(accPerm1.equals(accPerm2));

        accPerm1.setAccessInfoId(generateId());
        accPerm2.setAccessInfoId(generateId());
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
