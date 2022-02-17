/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestDomain;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.qa.common.cucumber.CucUser;
import org.eclipse.kapua.qa.common.cucumber.CucRolePermission;
import org.eclipse.kapua.qa.common.cucumber.CucDomain;
import org.eclipse.kapua.qa.common.cucumber.CucGroup;
import org.eclipse.kapua.qa.common.cucumber.CucRole;
import org.eclipse.kapua.qa.common.cucumber.CucConfig;
import org.eclipse.kapua.service.account.Account;
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
import org.eclipse.kapua.service.authorization.access.AccessRoleAttributes;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;
import org.eclipse.kapua.service.authorization.domain.DomainRegistryService;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupCreator;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.GroupQuery;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionAttributes;
import org.eclipse.kapua.service.authorization.role.RolePermissionQuery;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Implementation of Gherkin steps used to test miscellaneous Shiro
// authorization functionality.

@ScenarioScoped
public class AuthorizationServiceSteps extends TestBase {

    private static final String ACCESS_INFO = "AccessInfo";
    private static final String ACCESS_INFO_CREATOR = "AccessInfoCreator";
    private static final String ACCESS_INFO_FOUND = "AccessInfoFound";
    private static final String ACCESS_PERMISSIONS = "AccessPermissions";
    private static final String ACCESS_PERMISSION_FOUND = "AccessPermissionFound";
    private static final String ACCESS_ROLE = "AccessRole";
    private static final String ACCESS_ROLES = "AccessRoles";
    private static final String ACCESS_ROLE_FOUND = "AccessRoleFound";
    private static final String CHILD_ACCOUNT_ACCESS_INFO = "ChildAccountAccessInfo";
    private static final String CHILD_ACCOUNT_ROLE_PERMISSION = "ChildAccountRolePermission";
    private static final String COUNT = "Count";
    private static final String DOMAIN = "Domain";
    private static final String DOMAIN_CREATOR = "DomainCreator";
    private static final String DOMAIN_ID = "DomainId";
    private static final String GROUP = "Group";
    private static final String GROUP_CREATOR = "GroupCreator";
    private static final String GROUP_SECOND = "GroupSecond";
    private static final String GROUP_NAME = "GroupName";
    private static final String LAST_ACCESS_PERMISSION = "LastAccessPermission";
    private static final String LAST_ACCOUNT = "LastAccount";
    private static final String LAST_ACCOUNT_ID = "LastAccountId";
    private static final String PERMISSIONS = "Permissions";
    private static final String PERMISSION_LIST = "PermissionList";
    private static final String ROLE_CREATOR = "RoleCreator";
    private static final String ROLE_FOUND = "RoleFound";
    private static final String ROLE_IDS = "RoleIds";
    private static final String ROLE_LIST = "RoleList";
    private static final String ROLE_LIST_RESULT = "RoleListResult";
    private static final String ROLE_NAME = "roleName";
    private static final String ROLE_PERMISSION = "RolePermission";
    private static final String ROLE_PERMISSIONS = "RolePermissions";
    private static final String ROLE_PERMISSION_FOUND = "RolePermissionFound";

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
    private GroupService groupService;
    private GroupFactory groupFactory;
    private RoleService roleService;
    private RoleFactory roleFactory;
    private RolePermissionService rolePermissionService;
    private RolePermissionFactory rolePermissionFactory;
    private UserService userService;
    private UserFactory userFactory;

    @Inject
    public AuthorizationServiceSteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
        this.database = dbHelper;
    }

    // Database setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario) {

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
        userFactory = locator.getFactory(UserFactory.class);
        userService = locator.getService(UserService.class);

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
        Account tmpAccount = (Account) stepData.get(LAST_ACCOUNT);

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

        Domain domain = (Domain) stepData.get(DOMAIN);
        RoleCreator roleCreator = null;
        Set<Permission> permissions;
        Role role = null;

        stepData.remove(PERMISSIONS);
        stepData.remove(ROLE_CREATOR);
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
                stepData.put(PERMISSIONS, permissions);
                stepData.put(ROLE_CREATOR, roleCreator);
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
        Domain domain = (Domain) stepData.get(DOMAIN);
        RolePermission rolePermission = null;
        ArrayList<RolePermission> rolePermissions = new ArrayList<>();

        stepData.remove(ROLE_PERMISSION);

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
                stepData.remove(ROLE_PERMISSIONS);
                rolePermission = rolePermissionService.create(rolePermissionCreator);
                rolePermissions.add(rolePermission);
                stepData.put(ROLE_PERMISSIONS, rolePermissions);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }

        stepData.put(ROLE_PERMISSION, rolePermission);
    }

    @When("^I update the last created role name to \"(.+)\"$")
    public void updateRoleNameTo(String name)
            throws Exception {

        Role role = roleFactory.clone((Role) stepData.get("Role"));
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

        stepData.remove(PERMISSION_LIST);

        primeException();
        try {
            RolePermissionListResult permissionList = rolePermissionService.findByRoleId(
                    role.getScopeId(), role.getId());
            stepData.put(PERMISSION_LIST, permissionList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the role with name \"(.*)\"$")
    public void findRoleWithName(String roleName)
            throws Exception {

        Role role = (Role) stepData.get("Role");
        assertEquals(roleName, role.getName());

        stepData.remove(ROLE_FOUND);

        primeException();
        try {
            Role roleFound = roleService.find(role.getScopeId(), role.getId());
            stepData.put(ROLE_FOUND, roleFound);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created role permission$")
    public void findLastCreatedRolePermission()
            throws Exception {

        RolePermission rolePermission = (RolePermission) stepData.get(ROLE_PERMISSION);
        stepData.remove(ROLE_PERMISSION_FOUND);

        primeException();
        try {
            RolePermission rolePermissionFound = rolePermissionService.find(
                    rolePermission.getScopeId(), rolePermission.getId());
            stepData.put(ROLE_PERMISSION_FOUND, rolePermissionFound);
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
            stepData.put(ROLE_FOUND, roleFound);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the role with name \"([^\"]*)\"$")
    public void deleteRoleWithName(String roleName)
            throws Exception {

        Role role = (Role) stepData.get("Role");

        primeException();
        try {
            assertEquals(roleName, role.getName());
            KapuaId roleId = role.getId();
            roleService.delete(role.getScopeId(), roleId);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last created role permission$")
    public void deleteLastCreatedRolePermission()
            throws Exception {

        RolePermission rolePermission = (RolePermission) stepData.get(ROLE_PERMISSION);

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

        stepData.remove(COUNT);

        primeException();
        try {
            Long count = roleService.count(tmpQuery);
            stepData.put(COUNT, count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the role permissions in scope (\\d+)$")
    public void countRolePermissionsInScope(Integer scope)
            throws Exception {

        KapuaId tmpId = getKapuaId(scope);
        RolePermissionQuery tmpQuery = rolePermissionFactory.newQuery(tmpId);

        stepData.remove(COUNT);

        primeException();
        try {
            Long count = rolePermissionService.count(tmpQuery);
            stepData.put(COUNT, count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for the role \"(.+)\" in scope (\\d+)$")
    public void queryForRoleInScope(String name, Integer scope)
            throws Exception {

        KapuaId tmpId = getKapuaId(scope);
        RoleQuery tmpQuery = roleFactory.newQuery(tmpId);
        tmpQuery.setPredicate(tmpQuery.attributePredicate(KapuaNamedEntityAttributes.NAME, name, AttributePredicate.Operator.EQUAL));

        stepData.remove(ROLE_LIST);
        stepData.remove(ROLE_FOUND);
        stepData.remove(COUNT);

        primeException();
        try {
            RoleListResult roleList = roleService.query(tmpQuery);
            stepData.put(ROLE_LIST, roleList);
            stepData.put(ROLE_FOUND, roleList.getFirstItem());
            stepData.put(COUNT, Long.valueOf(roleList.getSize()));
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^The role was found$")
    public void chackThatSomethingWasFound() {
        assertNotNull(stepData.get(ROLE_FOUND));
    }

    @Then("^I find no roles$")
    public void chackThatNothingWasFound() {
        assertNull(stepData.get(ROLE_FOUND));
    }

    @Then("^I find no permissions$")
    public void checkThatNoPermissionWasFound() {
        assertNull(stepData.get(ROLE_PERMISSION_FOUND));
    }

    @Then("^The role matches the creator$")
    public void checkLastRoleAgainstCreator() {

        Role role = (Role) stepData.get("Role");
        RoleCreator roleCreator = (RoleCreator) stepData.get(ROLE_CREATOR);

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

        Set<Permission> permissions = (Set<Permission>) stepData.get(PERMISSIONS);
        RolePermissionListResult permissionList = (RolePermissionListResult) stepData.get(PERMISSION_LIST);

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

        RolePermissionListResult permissionList = (RolePermissionListResult) stepData.get(PERMISSION_LIST);

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
        Role roleFound = (Role) stepData.get(ROLE_FOUND);

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

        RolePermission rolePermission = (RolePermission) stepData.get(ROLE_PERMISSION);
        RolePermission rolePermissionFound = (RolePermission) stepData.get(ROLE_PERMISSION_FOUND);

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
        Role roleFound = (Role) stepData.get(ROLE_FOUND);

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

        RolePermission perm1 = rolePermissionFactory.newEntity(SYS_SCOPE_ID);
        assertNotNull(perm1);
//        RolePermission perm2 = new RolePermissionImpl(perm1);
//        assertNotNull(perm2);
//        assertTrue(perm1.equals(perm2));
//        RolePermission perm3 = new RolePermissionImpl(SYS_SCOPE_ID, tmpPermission);
//        assertNotNull(perm3);
//        assertEquals(perm3.getPermission(), tmpPermission);
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

        stepData.remove(DOMAIN_CREATOR);
        stepData.remove(DOMAIN);
        stepData.remove(DOMAIN_ID);

        primeException();
        for (CucDomain tmpDom : domains) {
            tmpDom.doParse();

            domainCreator = domainFactory.newCreator(tmpDom.getName());
            if (tmpDom.getActionSet() != null) {
                domainCreator.setActions(tmpDom.getActionSet());
            }
            stepData.put(DOMAIN_CREATOR, domainCreator);

            try {
                domain = domainRegistryService.create(domainCreator);
                stepData.put(DOMAIN, domain);
                if (domain != null) {
                    stepData.put(DOMAIN_ID, domain.getId());
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
        query.setPredicate(query.attributePredicate(KapuaNamedEntityAttributes.NAME, name, AttributePredicate.Operator.EQUAL));

        try {
            primeException();
            DomainListResult domains = domainRegistryService.query(query);
            stepData.put(DOMAIN, domains.getFirstItem());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created domain$")
    public void findDomainByRememberedId()
            throws Exception {

        KapuaId domainId = (KapuaId) stepData.get(DOMAIN_ID);
        stepData.remove(DOMAIN);

        try {
            primeException();
            Domain domain = domainRegistryService.find(null, domainId);
            stepData.put(DOMAIN, domain);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last created domain$")
    public void deleteLastCreatedDomain()
            throws Exception {

        KapuaId domainId = (KapuaId) stepData.get(DOMAIN_ID);

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

        stepData.remove(COUNT);

        try {
            primeException();
            DomainQuery query = domainFactory.newQuery(null);
            Long count = domainRegistryService.count(query);
            stepData.put(COUNT, count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for domains with the name \"(.+)\"$")
    public void queryForNamedDomain(String name)
            throws Exception {

        DomainQuery query = domainFactory.newQuery(null);
        query.setPredicate(query.attributePredicate(KapuaNamedEntityAttributes.NAME, name, AttributePredicate.Operator.EQUAL));

        stepData.remove("DomainList");
        stepData.remove(COUNT);

        try {
            primeException();
            DomainListResult domainList = domainRegistryService.query(query);
            stepData.put("DomainList", domainList);
            stepData.put(COUNT, Long.valueOf(domainList.getSize()));
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^This is the initial count$")
    public void setInitialCount() {

        Long startCount = (Long) stepData.get(COUNT);
        stepData.put("InitialCount", startCount);
    }

    @Then("^A domain was created$")
    public void checkDomainNotNull() {

        Domain domain = (Domain) stepData.get(DOMAIN);
        assertNotNull(domain);
    }

    @Then("^There is no domain$")
    public void checkDomainIsNull() {

        Domain domain = (Domain) stepData.get(DOMAIN);
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

        Domain domain = (Domain) stepData.get(DOMAIN);
        DomainCreator domainCreator = (DomainCreator) stepData.get(DOMAIN_CREATOR);

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

        Domain domain = (Domain) stepData.get(DOMAIN);

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

        Long count = (Long) stepData.get(COUNT);
        Long initialCount = (Long) stepData.get("InitialCount");

        assertEquals(cnt.longValue(), count.longValue() - initialCount.longValue());
    }

    @When("^I configure the group service$")
    public void setGroupConfigurationValue(List<CucConfig> cucConfigs)
            throws Exception {

        Map<String, Object> valueMap = new HashMap<>();
        KapuaId scopeId;
        KapuaId parentScopeId;
        Account tmpAccount = (Account) stepData.get(LAST_ACCOUNT);

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

        stepData.remove(COUNT);
        primeException();
        try {
            Long count = groupService.count(groupFactory.newQuery(SYS_SCOPE_ID));
            stepData.put(COUNT, count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I create the group(?:|s)$")
    public void createAListOfGroups(List<CucGroup> groups)
            throws Exception {

        Group group = null;
        GroupCreator groupCreator = null;
        stepData.remove(GROUP_CREATOR);
        stepData.remove(GROUP);
        stepData.remove("GroupId");

        primeException();
        for (CucGroup tmpGrp : groups) {
            tmpGrp.doParse();
            groupCreator = groupFactory.newCreator(tmpGrp.getScopeId(), tmpGrp.getName());

            try {
                group = groupService.create(groupCreator);
                stepData.put(GROUP_CREATOR, groupCreator);
                stepData.put(GROUP, group);
                stepData.put("GroupId", group.getId());
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @When("^I update the group name to \"(.+)\"$")
    public void updateLastGroupName(String name)
            throws Exception {

        Group group = (Group) stepData.get(GROUP);
        group.setName(name);
        // Sleep for a bit to make sure the time stamps are really different!
        Thread.sleep(50);

        try {
            Group groupSecond = groupService.update(group);
            stepData.put(GROUP_SECOND, groupSecond);
        } catch (KapuaException ex) {
            verifyException(ex);
        }

    }

    @When("^I update the group with an incorrect ID$")
    public void updateGroupWithFalseId()
            throws Exception {

        Group group = (Group) stepData.get(GROUP);
        group.setId(getKapuaId());

        primeException();
        try {
            groupService.update(group);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^I delete the group with name \"([^\"]*)\"$")
    public void deleteGroupWithName(String groupName)
            throws Exception {

        primeException();
        try {
            GroupQuery tmpQuery = groupFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(KapuaNamedEntityAttributes.NAME, groupName, AttributePredicate.Operator.EQUAL));
            Group group = groupService.query(tmpQuery).getFirstItem();
            groupService.delete(group.getScopeId(), group.getId());
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Then("^I try to delete the group with name \"([^\"]*)\"$")
    public void tryToDeleteGroupWithName(String groupName)
            throws Exception {
        Group group = (Group) stepData.get(GROUP);

        primeException();
        try {
            assertEquals(groupName, group.getName());
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

    @When("^I search for the group with name \"([^\"]*)\"$")
    public void findGroupByRememberedId(String groupName)
            throws Exception {

        Group group = (Group) stepData.get(GROUP);
        assertEquals(groupName, group.getName());

        primeException();
        try {
            Group groupSecond = groupService.find(group.getScopeId(), group.getId());
            stepData.put(GROUP_SECOND, groupSecond);
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
            stepData.put(COUNT, count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for the group \"(.+)\" in scope (\\d+)$")
    public void queryForGroup(String name, int scope)
            throws Exception {

        KapuaId tmpId = getKapuaId(scope);
        GroupQuery tmpQuery = groupFactory.newQuery(tmpId);
        tmpQuery.setPredicate(tmpQuery.attributePredicate(KapuaNamedEntityAttributes.NAME, name, AttributePredicate.Operator.EQUAL));

        stepData.remove("GroupList");
        stepData.remove(GROUP);
        stepData.remove(COUNT);

        primeException();
        try {
            GroupListResult groupList = groupService.query(tmpQuery);
            stepData.put("GroupList", groupList);
            stepData.put(GROUP, groupList.getFirstItem());
            stepData.put(COUNT, Long.valueOf(groupList.getSize()));
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^A group was created$")
    public void checkGroupNotNull() {

        assertNotNull(stepData.get(GROUP));
    }

    @Then("^No group was created$")
    public void checkGroupIsNull() {

        assertNull(stepData.get(GROUP));
    }

    @Then("^The group was found$")
    public void checkThatTheGroupWasFound() {

        assertNotNull(stepData.get(GROUP_SECOND));
    }

    @Then("^No group was found$")
    public void checkNoGroupWasFound() {

        assertNull(stepData.get(GROUP_SECOND));
    }

    @Then("^The group name is \"(.+)\"$")
    public void checkGroupName(String name) {

        Group group = (Group) stepData.get(GROUP);
        assertEquals(group.getName(), name.trim());
    }

    @Then("^The group matches the creator$")
    public void checkGroupAgainstCreator() {

        Group group = (Group) stepData.get(GROUP);
        GroupCreator groupCreator = (GroupCreator) stepData.get(GROUP_CREATOR);

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

        Group group = (Group) stepData.get(GROUP);
        Group groupSecond = (Group) stepData.get(GROUP_SECOND);

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

        Group group = (Group) stepData.get(GROUP);
        Group groupSecond = (Group) stepData.get(GROUP_SECOND);

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
        KapuaId currId = (KapuaId) stepData.get(LAST_ACCOUNT_ID);

        // Get the current domain
        Domain curDomain = (Domain) stepData.get(DOMAIN);

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

        stepData.put(PERMISSIONS, permissions);
    }

    @Given("^The role \"(.*)\"$")
    public void provideRoleForDomain(String name)
            throws Exception {

        KapuaId currId = (KapuaId) stepData.get(LAST_ACCOUNT_ID);
        Set<Permission> permissions = (Set<Permission>) stepData.get(PERMISSIONS);

        RoleCreator roleCreator = roleFactory.newCreator(currId);
        roleCreator.setName(name);
        roleCreator.setPermissions(permissions);

        try {
            primeException();
            stepData.remove("Role");
            stepData.remove(ROLE_IDS);
            Role role = roleService.create(roleCreator);
            stepData.put("Role", role);
            Set<KapuaId> roleIds = new HashSet<>();
            roleIds.add(role.getId());
            stepData.put(ROLE_IDS, roleIds);
        } catch (KapuaException e) {
            verifyException(e);
        }
    }

    @Given("^An invalid role ID$")
    public void provideInvalidRoleObjectID() {

        stepData.remove(ROLE_IDS);
        Set<KapuaId> roleIds = new HashSet<>();
        roleIds.add(getKapuaId());
        stepData.put(ROLE_IDS, roleIds);
    }

    @When("^I create the access role$")
    public void createAccessRole()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get(LAST_ACCOUNT_ID);
        AccessInfo accessInfo = (AccessInfo) stepData.get(ACCESS_INFO);
        Role role = (Role) stepData.get("Role");
        AccessRoleCreator tmpCreator = accessRoleFactory.newCreator(currScope);
        tmpCreator.setAccessInfoId(accessInfo.getId());
        tmpCreator.setRoleId(role.getId());

        try {
            primeException();
            stepData.remove(ACCESS_ROLE);
            AccessRole accessRole = accessRoleService.create(tmpCreator);
            stepData.put(ACCESS_ROLE, accessRole);
        } catch (KapuaException e) {
            verifyException(e);
        }
    }

    @When("^I create a clean access info entity$")
    public void createCleanAccessInfoEntity()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get(LAST_ACCOUNT_ID);
        User tmpUser = (User) stepData.get("User");
        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(currScope);
        accessInfoCreator.setUserId(tmpUser.getId());

        stepData.remove(PERMISSIONS);
        stepData.remove(ROLE_IDS);

        try {
            primeException();
            stepData.put(ACCESS_INFO_CREATOR, accessInfoCreator);
            stepData.remove(ACCESS_INFO);
            AccessInfo accessInfo = accessInfoService.create(accessInfoCreator);
            stepData.put(ACCESS_INFO, accessInfo);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I create the access info entity$")
    public void createAccessInfoEntity()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get(LAST_ACCOUNT_ID);
        User tmpUser = (User) stepData.get("User");
        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(currScope);
        accessInfoCreator.setUserId(tmpUser.getId());

        Set<Permission> permissions = (Set<Permission>) stepData.get(PERMISSIONS);
        Set<KapuaId> roleIds = (Set<KapuaId>) stepData.get(ROLE_IDS);

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
            stepData.put(ACCESS_INFO_CREATOR, accessInfoCreator);
            stepData.remove(ACCESS_INFO);
            AccessInfo accessInfo = accessInfoService.create(accessInfoCreator);
            stepData.put(ACCESS_INFO, accessInfo);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the permissions of the last access info entity$")
    public void findThePermissionsOfTheLastAccessInfoEntity()
            throws Exception {

        AccessInfo accessInfo = (AccessInfo) stepData.get(ACCESS_INFO);

        try {
            primeException();
            stepData.remove(ACCESS_PERMISSIONS);
            AccessPermissionListResult accessPermissions = accessPermissionService.findByAccessInfoId(
                    accessInfo.getScopeId(), accessInfo.getId());
            stepData.put(ACCESS_PERMISSIONS, accessPermissions);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the roles of the last access info entity$")
    public void findTheRolesOfTheLastAccessInfoEntity()
            throws Exception {

        AccessInfo accessInfo = (AccessInfo) stepData.get(ACCESS_INFO);

        try {
            primeException();
            stepData.remove(ACCESS_ROLES);
            AccessRoleListResult accessRoles = accessRoleService.findByAccessInfoId(accessInfo.getScopeId(), accessInfo.getId());
            stepData.put(ACCESS_ROLES, accessRoles);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created access info entity$")
    public void findLastCreatedAccessInfoEntity()
            throws Exception {

        AccessInfo accessInfo = (AccessInfo) stepData.get(ACCESS_INFO);

        try {
            primeException();
            stepData.remove(ACCESS_INFO_FOUND);
            AccessInfo accessInfoFound = accessInfoService.find(accessInfo.getScopeId(), accessInfo.getId());
            stepData.put(ACCESS_INFO_FOUND, accessInfoFound);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for an access info entity by user ID$")
    public void findTheAccessInfoEntityByUserId()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get(LAST_ACCOUNT_ID);
        User tmpUser = (User) stepData.get("User");

        try {
            primeException();
            stepData.remove(ACCESS_INFO_FOUND);
            AccessInfo accessInfoFound = accessInfoService.findByUserId(currScope, tmpUser.getId());
            stepData.put(ACCESS_INFO_FOUND, accessInfoFound);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created access role entity$")
    public void findLastCreatedAccessRole()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get(LAST_ACCOUNT_ID);
        AccessRole accessRole = (AccessRole) stepData.get(ACCESS_ROLE);

        try {
            primeException();
            stepData.remove(ACCESS_ROLE_FOUND);
            AccessRole accessRoleFound = accessRoleService.find(currScope, accessRole.getId());
            stepData.put(ACCESS_ROLE_FOUND, accessRoleFound);
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
            stepData.remove(COUNT);
            Long count = accessRoleService.count(tmpQuery);
            stepData.put(COUNT, count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last created access role entry$")
    public void deleteLastCreatedAccessRoleEntry()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get(LAST_ACCOUNT_ID);
        AccessRole accessRole = (AccessRole) stepData.get(ACCESS_ROLE);

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

        KapuaId currScope = (KapuaId) stepData.get(LAST_ACCOUNT_ID);
        AccessInfo accessInfo = (AccessInfo) stepData.get(ACCESS_INFO);

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
            stepData.remove(COUNT);
            Long count = accessInfoService.count(tmpQuery);
            stepData.put(COUNT, count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for the access info entities for the last user$")
    public void queryForLastUserAccessInfoEntities()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get(LAST_ACCOUNT_ID);
        User user = (User) stepData.get("User");

        AccessInfoQuery tmpQuery = accessInfoFactory.newQuery(currScope);
        tmpQuery.setPredicate(tmpQuery.attributePredicate(AccessInfoAttributes.USER_ID, user.getId(), AttributePredicate.Operator.EQUAL));

        try {
            primeException();
            stepData.remove(COUNT);
            stepData.remove("AccessList");
            AccessInfoListResult accessList = accessInfoService.query(tmpQuery);
            stepData.put("AccessList", accessList);
            if (accessList != null) {
                stepData.put(COUNT, Long.valueOf(accessList.getSize()));
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I create the permission(?:|s)$")
    public void createPermissionEntries()
            throws Exception {

        KapuaId currScope = (KapuaId) stepData.get(LAST_ACCOUNT_ID);
        AccessInfo accessInfo = (AccessInfo) stepData.get(ACCESS_INFO);
        Set<Permission> permissions = (Set<Permission>) stepData.get(PERMISSIONS);

        AccessPermissionCreator accessPermissionCreator = accessPermissionFactory.newCreator(currScope);
        accessPermissionCreator.setAccessInfoId(accessInfo.getId());

        try {
            primeException();
            AccessPermission accessPermission = null;
            stepData.remove("AccessPermissionCreator");
            stepData.remove("AccessPermission");
            stepData.remove(LAST_ACCESS_PERMISSION);
            for (Permission tmpPerm : permissions) {
                accessPermissionCreator.setPermission(tmpPerm);
                accessPermission = accessPermissionService.create(accessPermissionCreator);
            }
            stepData.put("AccessPermissionCreator", accessPermissionCreator);
            if (accessPermission != null) {
                stepData.put("AccessPermission", accessPermission);
                stepData.put(LAST_ACCESS_PERMISSION, accessPermission);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created permission$")
    public void findTheLastCreatedAccessPermission()
            throws Exception {

        stepData.remove(ACCESS_PERMISSION_FOUND);
        AccessPermission permission = (AccessPermission) stepData.get(LAST_ACCESS_PERMISSION);

        try {
            AccessPermission accessPermissionFound = accessPermissionService.find(permission.getScopeId(), permission.getId());
            stepData.put(ACCESS_PERMISSION_FOUND, accessPermissionFound);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last created access permission$")
    public void deleteLastCreatedPermission()
            throws Exception {

        AccessPermission permission = (AccessPermission) stepData.get(LAST_ACCESS_PERMISSION);

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
            stepData.remove(COUNT);
            Long count = accessPermissionService.count(tmpQuery);
            stepData.put(COUNT, count);
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

            AccessInfoCreator tmpCreator = accessInfoFactory.newCreator(getKapuaId());
            assertNotNull(tmpCreator);
//            tmpCreator.setUserId(getKapuaId());
//
//            AccessInfoCreator tmpCreator2 = new AccessInfoCreatorImpl(tmpCreator);
//            assertNotNull(tmpCreator2);
//            assertEquals(tmpCreator.getUserId(), tmpCreator2.getUserId());

            AccessInfo tmpAccInfo = accessInfoFactory.newEntity(getKapuaId());
            assertNotNull(tmpAccInfo);
//            tmpAccInfo.setUserId(getKapuaId());
//
//            AccessInfo tmpAccInfo2 = new AccessInfoImpl(tmpAccInfo);
//            assertNotNull(tmpAccInfo2);
//            assertNotNull(tmpAccInfo2.getUserId());
//
//            tmpAccInfo2.setUserId(null);
//            assertNull(tmpAccInfo2.getUserId());

            tmpAccInfo.setUserId(null);
            assertNull(tmpAccInfo.getUserId());
        } catch (Exception ex) {
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
        AccessPermissionCreator tmpCreator = accessPermissionFactory.newCreator(tmpId);
        assertNotNull(tmpCreator);
        assertNotNull(tmpCreator.getScopeId());
        assertEquals(tmpId, tmpCreator.getScopeId());

        AccessPermission tmpAccPerm = accessPermissionFactory.newEntity(getKapuaId());
        assertNotNull(tmpAccPerm);
        tmpAccPerm.setAccessInfoId(getKapuaId());
        Permission tmpPerm = permissionFactory.newPermission(new TestDomain(), Actions.read, getKapuaId(), getKapuaId());
        tmpAccPerm.setPermission(tmpPerm);
        assertEquals(tmpPerm, tmpAccPerm.getPermission());

//        AccessPermission tmpAccPerm2 = new AccessPermissionImpl(tmpAccPerm);
//        assertNotNull(tmpAccPerm2);
//        assertEquals(tmpAccPerm.getAccessInfoId(), tmpAccPerm2.getAccessInfoId());
//        assertEquals(tmpAccPerm.getPermission(), tmpAccPerm2.getPermission());

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
            AccessRoleCreator tmpCreator = accessRoleFactory.newCreator(tmpId);
            assertNotNull(tmpCreator);
            assertNotNull(tmpCreator.getScopeId());
            assertEquals(tmpId, tmpCreator.getScopeId());

            AccessRole tmpRole = accessRoleFactory.newEntity(getKapuaId());
            assertNotNull(tmpRole);
            tmpRole.setAccessInfoId(getKapuaId());
            tmpRole.setRoleId(getKapuaId());

//            AccessRole tmpRole2 = new AccessRoleImpl(tmpRole);
//            assertNotNull(tmpRole2);
//            assertEquals(tmpRole.getRoleId(), tmpRole2.getRoleId());
//            assertEquals(tmpRole.getAccessInfoId(), tmpRole2.getAccessInfoId());

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
        AccessRole accessRole = (AccessRole) stepData.get(ACCESS_ROLE);
        assertNotNull(accessRole);
    }

    @Then("^I find an access role entity$")
    public void checkThatAnAccessRoleEntityWasFound() {
        AccessRole accessRoleFound = (AccessRole) stepData.get(ACCESS_ROLE_FOUND);
        assertNotNull(accessRoleFound);
    }

    @Then("^An access info entity was created$")
    public void checkThatAccessInfoEntityExists() {
        AccessInfo accessInfo = (AccessInfo) stepData.get(ACCESS_INFO);
        assertNotNull(accessInfo);
    }

    @Then("^I find an accessinfo entity$")
    public void checkThatAnAccessInfoEntityWasFound() {
        AccessInfo accessInfoFound = (AccessInfo) stepData.get(ACCESS_INFO_FOUND);
        assertNotNull(accessInfoFound);
    }

    @Then("^I find no access info entity$")
    public void checkThatAnAccessInfoEntityWasNotFound() {
        AccessInfo accessInfoFound = (AccessInfo) stepData.get(ACCESS_INFO_FOUND);
        assertNull(accessInfoFound);
    }

    @Then("^I find an access permission entity$")
    public void checkThatAnAccessPermissionWasFound() {
        AccessPermission accessPermissionFound = (AccessPermission) stepData.get(ACCESS_PERMISSION_FOUND);
        assertNotNull(accessPermissionFound);
    }

    @Then("^There are no such access permissions$")
    public void checkThatThePermissionsWereRemoved() {
        AccessPermissionListResult accessPermissions = (AccessPermissionListResult) stepData.get(ACCESS_PERMISSIONS);
        assertEquals(0, accessPermissions.getSize());
    }

    @Then("^There are no such access roles$")
    public void checkThatTheRolesWereRemoved() {
        AccessRoleListResult accessRoles = (AccessRoleListResult) stepData.get(ACCESS_ROLES);
        assertEquals(0, accessRoles.getSize());
    }

    @Then("^The entity matches the creator$")
    public void checkEntityAgainstCreator() {

        AccessInfoCreator accessInfoCreator = (AccessInfoCreator) stepData.get(ACCESS_INFO_CREATOR);
        AccessInfo accessInfo = (AccessInfo) stepData.get(ACCESS_INFO);

        assertEquals(accessInfoCreator.getUserId(), accessInfo.getUserId());
        assertEquals(accessInfoCreator.getScopeId(), accessInfo.getScopeId());
    }

    @Then("^The permissions match the creator$")
    public void checkAccessInfoEntityPermissions() {

        AccessInfoCreator accessInfoCreator = (AccessInfoCreator) stepData.get(ACCESS_INFO_CREATOR);
        AccessPermissionListResult accessPermissions = (AccessPermissionListResult) stepData.get(ACCESS_PERMISSIONS);

        assertEquals(accessInfoCreator.getPermissions().size(), accessPermissions.getSize());

        for (int i = 0; i < accessPermissions.getSize(); i++) {
            assertTrue(accessInfoCreator.getPermissions().contains(accessPermissions.getItem(i).getPermission()));
        }
    }

    @Then("^The access info roles match the creator$")
    public void checkAccessInfoEntityRoles() {

        AccessInfoCreator accessInfoCreator = (AccessInfoCreator) stepData.get(ACCESS_INFO_CREATOR);
        AccessRoleListResult accessRoles = (AccessRoleListResult) stepData.get(ACCESS_ROLES);

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
    public void checkAccessRoleComparison() throws Exception {

        AccessRole accRole1 = accessRoleFactory.newEntity(getKapuaId());
        AccessRole accRole2 = accessRoleFactory.newEntity(getKapuaId());

        primeException();
        try {
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
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Access Role object equals function.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^I can compare access permission objects$")
    public void checkAccessPermissionComparison() {

        AccessPermission accPerm1 = accessPermissionFactory.newEntity(getKapuaId());
        AccessPermission accPerm2 = accessPermissionFactory.newEntity(getKapuaId());
        Permission tmpPerm1 = permissionFactory.newPermission(new TestDomain(), Actions.read, SYS_SCOPE_ID, getKapuaId());
        Permission tmpPerm2 = permissionFactory.newPermission(new TestDomain(), Actions.write, SYS_SCOPE_ID, getKapuaId());

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

        Permission perm1 = permissionFactory.newPermission(new TestDomain("test_domain_1"), Actions.read, getKapuaId(10), getKapuaId(100));
        Permission perm2 = permissionFactory.newPermission(new TestDomain("test_domain_1"), Actions.read, getKapuaId(10), getKapuaId(100));

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

    @Then("^Access role with name \"([^\"]*)\" is found$")
    public void accessRoleWithNameIsFinded(String roleName) throws Exception {

        try {
            primeException();
            RoleQuery roleQuery = roleFactory.newQuery(getCurrentScopeId());
            roleQuery.setPredicate(roleQuery.attributePredicate(KapuaNamedEntityAttributes.NAME, roleName));
            RoleListResult roleList = roleService.query(roleQuery);

            AccessRoleQuery accessRoleQuery = accessRoleFactory.newQuery(getCurrentScopeId());
            accessRoleQuery.setPredicate(accessRoleQuery.attributePredicate(AccessRoleAttributes.ROLE_ID, roleList.getFirstItem().getId()));
            AccessRoleListResult searchAccessRole = accessRoleService.query(accessRoleQuery);
            assertTrue(searchAccessRole.getSize() > 0);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I create the roles$")
    public void iCreateTheRoles(List<CucRole> roleNames) throws Exception {
        RoleCreator roleCreator = roleFactory.newCreator(getCurrentScopeId());
        ArrayList<Role> roleArrayList = new ArrayList<Role>();
        stepData.remove(ROLE_LIST);
        Role role = null;
        for (CucRole roleName : roleNames) {
            roleCreator.setName(roleName.getName());
            try {
                primeException();
                stepData.remove("Role");
                role = roleService.create(roleCreator);
                roleArrayList.add(role);
                stepData.put("Role", role);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
        stepData.put(ROLE_LIST, roleArrayList);
    }

    @And("^I add permissions to the role$")
    public void iAddPermissionsToTheRole() throws Exception {
        Role role = (Role) stepData.get("Role");
        Set<Permission> permissions = (Set<Permission>) stepData.get(PERMISSIONS);
        Set<RolePermission> rolePermissionList = new HashSet<>();
        RolePermissionCreator rolePermissionCreator = rolePermissionFactory.newCreator(getCurrentScopeId());
        for (Permission permission : permissions) {
            rolePermissionCreator.setPermission(permission);
            rolePermissionCreator.setRoleId(role.getId());

            try {
                primeException();
                stepData.remove(ROLE_PERMISSION);
                RolePermission rolePermission = rolePermissionService.create(rolePermissionCreator);
                rolePermissionList.add(rolePermission);
                stepData.put(ROLE_PERMISSION, rolePermission);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
        stepData.put("RolePermissionList", rolePermissionList);
    }

    @And("^I add access roles to user \"([^\"]*)\"$")
    public void iAddAccessRolesToUser(String userName) throws Exception {
        AccessInfo accessInfo = (AccessInfo) stepData.get(ACCESS_INFO);
        List<Role> roleList = (List<Role>) stepData.get(ROLE_LIST);
        User user = (User) stepData.get("User");
        assertEquals(userName, user.getName());
        AccessRoleCreator accessRoleCreator = accessRoleFactory.newCreator(getCurrentScopeId());

        for (Role role : roleList) {
            accessRoleCreator.setAccessInfoId(accessInfo.getId());
            accessRoleCreator.setRoleId(role.getId());
            stepData.put("AccessRoleCreator", accessRoleCreator);

            try {
                primeException();
                stepData.remove(ACCESS_ROLE);
                AccessRole accessRole = accessRoleService.create(accessRoleCreator);
                stepData.put(ACCESS_ROLE, accessRole);
                stepData.put("AccessRoleId", accessRole.getId());
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @And("^I delete role permissions$")
    public void iDeleteAccessRolePermissions() throws Exception {
        RolePermission rolePermission = (RolePermission) stepData.get(ROLE_PERMISSION);

        try {
            rolePermissionService.delete(rolePermission.getScopeId(), rolePermission.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I find the group with name \"([^\"]*)\"$")
    public void iFindLastCreatedGroup(String groupName) throws Exception {
        Group group = (Group) stepData.get(GROUP);

        try {
            primeException();
            assertEquals(groupName, group.getName());
            assertNotNull(groupService.find(getCurrentScopeId(), group.getId()));
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I find a role with name \"([^\"]*)\"$")
    public void iFindRoleWithName(String roleName) throws Exception {
        try {
            primeException();
            RoleQuery roleQuery = roleFactory.newQuery(getCurrentScopeId());
            roleQuery.setPredicate(roleQuery.attributePredicate(KapuaNamedEntityAttributes.NAME, roleName, AttributePredicate.Operator.EQUAL));

            stepData.remove(ROLE_LIST_RESULT);
            stepData.remove("Role");
            RoleListResult roleListResult = roleService.query(roleQuery);
            stepData.put(ROLE_LIST_RESULT, roleListResult);
            stepData.put("Role", roleListResult.getFirstItem());

            assertTrue(roleListResult.getSize() > 0);
        } catch (KapuaException ke) {
            verifyException(ke);
        }

    }

    @And("^I create a group with name \"([^\"]*)\"$")
    public void iCreateAGroupWithName(String groupName) throws Exception {
        GroupCreator groupCreator = groupFactory.newCreator(getCurrentScopeId());
        groupCreator.setName(groupName);

        try {
            primeException();
            stepData.remove(GROUP);
            Group group = groupService.create(groupCreator);
            stepData.put(GROUP, group);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I try to create groups with invalid characters in name$")
    public void iTryToCreateInvalidGroups() throws Exception {
        GroupCreator groupCreator = groupFactory.newCreator(getCurrentScopeId());
        String invalidCharacters = "!\"#$%&'()=»Ç>:;<-.,⁄@‹›€*ı–°·‚_±Œ„‰?“‘”’ÉØ∏{}|ÆæÒÔÓÌÏÎÍÅ«";
        for (int i = 0; i < invalidCharacters.length(); i++) {
            String groupName = GROUP + invalidCharacters.charAt(i);
            groupCreator.setName(groupName);

            try {
                primeException();
                stepData.remove(GROUP);
                Group group = groupService.create(groupCreator);
                stepData.put(GROUP, group);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @And("^I delete the last created role permissions$")
    public void iDeleteTheLastCreatedRolePermissions() throws Exception {
        ArrayList<RolePermission> rolePermissions = (ArrayList<RolePermission>) stepData.get(ROLE_PERMISSIONS);

        try {
            primeException();
            for (RolePermission rolePermission : rolePermissions) {
                rolePermissionService.delete(rolePermission.getScopeId(), rolePermission.getId());
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I search for the permissions of role \"([^\"]*)\"$")
    public void iSearchForThePermissionsOfTheRole(String roleName) throws Exception {
        Role role = (Role) stepData.get("Role");
        ArrayList<RolePermission> rolePermissionList = new ArrayList<>();
        assertEquals(roleName, role.getName());

        RolePermissionQuery rolePermissionQuery = rolePermissionFactory.newQuery(getCurrentScopeId());
        rolePermissionQuery.setPredicate(rolePermissionQuery.attributePredicate(RolePermissionAttributes.ROLE_ID, role.getId(), AttributePredicate.Operator.EQUAL));

        RolePermissionListResult rolePermissions = rolePermissionService.query(rolePermissionQuery);
        stepData.remove(ROLE_PERMISSIONS);
        for (int i = 0; i < rolePermissions.getSize(); i++) {
            stepData.remove(ROLE_PERMISSION);
            RolePermission rolePermission = rolePermissions.getItem(i);
            stepData.put(ROLE_PERMISSION, rolePermission);
            rolePermissionList.add(rolePermission);
        }
        stepData.put(ROLE_PERMISSIONS, rolePermissionList);

        long rolePermissionListSize = rolePermissionList.size();
        stepData.put(COUNT, rolePermissionListSize);
    }

    @And("^I delete the default admin role permission$")
    public void iDeleteTheDefaultRolePermission() throws Exception {
        ArrayList<RolePermission> rolePermissions = (ArrayList<RolePermission>) stepData.get(ROLE_PERMISSIONS);

        primeException();
        try {
            for (RolePermission rolePermission : rolePermissions) {
                if (rolePermission.getId().equals(KapuaId.ONE)) {
                    rolePermissionService.delete(getCurrentScopeId(), rolePermission.getId());
                }
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I delete all admin role permissions except default permission$")
    public void iDeleteTheLastCreatedAdminRolePermissions() throws Exception {
        ArrayList<RolePermission> rolePermissions = (ArrayList<RolePermission>) stepData.get(ROLE_PERMISSIONS);

        try {
            primeException();
            for (RolePermission rolePermission : rolePermissions) {
                if (!rolePermission.getId().equals(KapuaId.ONE)) {
                    rolePermissionService.delete(rolePermission.getScopeId(), rolePermission.getId());
                }
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I count the access roles from user \"([^\"]*)\"$")
    public void iCountTheAccessRolesFromUser(String userName) throws Exception {

        User lastUser = (User) stepData.get("User");
        AccessInfo accessInfo = (AccessInfo) stepData.get(ACCESS_INFO);
        AccessRoleQuery tmpQuery = accessRoleFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(tmpQuery.attributePredicate(AccessRoleAttributes.ACCESS_INFO_ID, accessInfo.getId(), AttributePredicate.Operator.EQUAL));

        assertEquals(userName, lastUser.getName());

        try {
            primeException();
            stepData.remove(COUNT);
            Long count = accessRoleService.count(tmpQuery);
            stepData.put(COUNT, count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for granted user$")
    public void iSearchGrantedUserToRole() throws Exception {

        ArrayList<User> grantedUserList = new ArrayList<>();
        long grantedUsersCount = 0;
        try {
            primeException();
            AccessRoleQuery accessRoleQuery = accessRoleFactory.newQuery(getCurrentScopeId());
            AccessRoleListResult accessRoleList = accessRoleService.query(accessRoleQuery);

            for (AccessRole a : accessRoleList.getItems()) {
                AccessInfo accessInfo = accessInfoService.find(getCurrentScopeId(), a.getAccessInfoId());
                User grantedUser = userService.find(getCurrentScopeId(), accessInfo.getUserId());
                stepData.put("GrantedUser", grantedUser);

                if (!grantedUser.getId().equals(KapuaId.ONE)) {
                    grantedUserList.add(grantedUser);
                    grantedUsersCount = grantedUserList.size();
                }
            }
            stepData.put("GrantedUserList", grantedUserList);
            stepData.put(COUNT, grantedUsersCount);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^I find granted user(?:|s) with name$")
    public void iFindGrantedUsersWithName(List<CucUser> grantedUsers) throws Exception {
        ArrayList<String> grantedUserNames = new ArrayList<>();
        ArrayList<User> grantedUsersList = (ArrayList<User>) stepData.get("GrantedUserList");

        for (User grantedUser : grantedUsersList) {
            grantedUserNames.add(grantedUser.getName());
        }

        for (CucUser user : grantedUsers) {
            assertTrue(grantedUserNames.contains(user.getName()));
        }
    }

    @And("^I try to find role with name \"([^\"]*)\" in account \"([^\"]*)\"$")
    public void iFindSpecificRoleInChildAccount(String roleName, String accountName) throws Exception {
        Account account = (Account) stepData.get(LAST_ACCOUNT);
        assertEquals(accountName, account.getName());

        RoleQuery roleQuery = roleFactory.newQuery(account.getId());

        roleQuery.setPredicate(roleQuery.attributePredicate(KapuaNamedEntityAttributes.NAME, roleName, AttributePredicate.Operator.EQUAL));

        RoleListResult childRolesList = roleService.query(roleQuery);
        stepData.put("ChildRolesList", childRolesList);
    }

    @And("^I create role \"([^\"]*)\" in account \"([^\"]*)\"$")
    public void iCreateRoleInSubaccount(String roleName, String accountName) throws Exception {
        Account account = (Account) stepData.get(LAST_ACCOUNT);
        assertEquals(accountName, account.getName());
        RoleCreator roleCreator = null;

        roleCreator = roleFactory.newCreator(account.getId());
        roleCreator.setName(roleName);
        try {
            Role role = roleService.create(roleCreator);
            stepData.put(ROLE_CREATOR, roleCreator);
            stepData.put("Role", role);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I create the following role permission(?:|s) in account \"([^\"]*)\"$")
    public void iCreateTheFollowingRolePermissionInSubaccount(String accountName, List<CucRolePermission> perms) throws Exception {

        Role role = (Role) stepData.get("Role");
        Account account = (Account) stepData.get(LAST_ACCOUNT);
        assertEquals(accountName, account.getName());
        Domain domain = (Domain) stepData.get(DOMAIN);
        RolePermission rolePermission = null;
        ArrayList<RolePermission> rolePermissions = new ArrayList<>();

        stepData.remove(CHILD_ACCOUNT_ROLE_PERMISSION);

        primeException();
        for (CucRolePermission tmpCPerm : perms) {
            tmpCPerm.doParse();
            assertNotNull(tmpCPerm.getScopeId());
            assertNotNull(tmpCPerm.getAction());

            domain.setScopeId(tmpCPerm.getScopeId());

            RolePermissionCreator rolePermissionCreator = rolePermissionFactory.newCreator(account.getId());
            rolePermissionCreator.setRoleId(role.getId());
            rolePermissionCreator.setPermission(permissionFactory.newPermission(domain.getDomain(), tmpCPerm.getAction(), tmpCPerm.getTargetScopeId()));

            try {
                stepData.remove("ChildAccountRolePermissions");
                rolePermission = rolePermissionService.create(rolePermissionCreator);
                rolePermissions.add(rolePermission);
                stepData.put("ChildAccountRolePermissions", rolePermissions);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @And("^I search for the permissions of found role \"([^\"]*)\" in account \"([^\"]*)\"$")
    public void iSearchForThePermissionsOfFoundedRoleInSubaccount(String roleName, String accountName) throws Exception {
        Role role = (Role) stepData.get("Role");
        assertEquals(roleName, role.getName());
        Account account = (Account) stepData.get(LAST_ACCOUNT);
        assertEquals(accountName, account.getName());
        ArrayList<RolePermission> rolePermissionList = new ArrayList<>();

        RolePermissionQuery rolePermissionQuery = rolePermissionFactory.newQuery(account.getId());
        rolePermissionQuery.setPredicate(rolePermissionQuery.attributePredicate(RolePermissionAttributes.ROLE_ID, role.getId(), AttributePredicate.Operator.EQUAL));

        RolePermissionListResult rolePermissions = rolePermissionService.query(rolePermissionQuery);
        stepData.remove(CHILD_ACCOUNT_ROLE_PERMISSION);
        for (int i = 0; i < rolePermissions.getSize(); i++) {
            stepData.remove(CHILD_ACCOUNT_ROLE_PERMISSION);
            RolePermission rolePermission = rolePermissions.getItem(i);
            stepData.put(CHILD_ACCOUNT_ROLE_PERMISSION, rolePermission);
            rolePermissionList.add(rolePermission);
        }
        stepData.put(CHILD_ACCOUNT_ROLE_PERMISSION, rolePermissionList);

        long rolePermissionListSize = rolePermissionList.size();
        stepData.put(COUNT, rolePermissionListSize);
    }

    @Then("^Role with name \"([^\"]*)\" in account \"([^\"]*)\" is found$")
    public void roleInChildAccountIsFound(String roleName, String accountName) {
        RoleListResult childRolesList = (RoleListResult) stepData.get("ChildRolesList");
        Role role = (Role) stepData.get("Role");
        Account account = (Account) stepData.get(LAST_ACCOUNT);
        assertEquals(roleName, role.getName());
        assertEquals(accountName, account.getName());

        assertTrue(childRolesList.getSize() > 0);
    }

    @And("^I add access role \"([^\"]*)\" to user \"([^\"]*)\" in account \"([^\"]*)\"$")
    public void iAddAccessRoleToUserInChildAccount(String roleName, String childUserName, String accountName) throws Exception {
        AccessInfo accessInfo = (AccessInfo) stepData.get(CHILD_ACCOUNT_ACCESS_INFO);
        Account account = (Account) stepData.get(LAST_ACCOUNT);
        User childUser = (User) stepData.get("ChildAccountUser");
        Role role = (Role) stepData.get("Role");
        AccessRoleCreator accessRoleCreator = accessRoleFactory.newCreator(account.getId());
        accessRoleCreator.setAccessInfoId(accessInfo.getId());
        accessRoleCreator.setRoleId(role.getId());
        stepData.put("ChildAccountAccessRoleCreator", accessRoleCreator);

        assertEquals(roleName, role.getName());
        assertEquals(accountName, account.getName());
        assertEquals(childUserName, childUser.getName());

        try {
            primeException();
            stepData.remove("ChildAccountAccessRole");
            AccessRole accessRole = accessRoleService.create(accessRoleCreator);
            stepData.put("ChildAccountAccessRole", accessRole);
            stepData.put("ChildAccountAccessRoleId", accessRole.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I create the access info entity in account \"([^\"]*)\"$")
    public void iCreateTheAccessInfoEntityInChildAccount(String accountName) throws Exception {
        Account account = (Account) stepData.get(LAST_ACCOUNT);
        User tmpUser = (User) stepData.get("ChildAccountUser");
        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(account.getId());
        accessInfoCreator.setUserId(tmpUser.getId());

        assertEquals(accountName, account.getName());

        Set<Permission> permissions = (Set<Permission>) stepData.get(PERMISSIONS);
        Set<KapuaId> roleIds = (Set<KapuaId>) stepData.get(ROLE_IDS);

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
            stepData.put("ChildAccountAccessInfoCreator", accessInfoCreator);
            stepData.remove(CHILD_ACCOUNT_ACCESS_INFO);
            AccessInfo accessInfo = accessInfoService.create(accessInfoCreator);
            stepData.put(CHILD_ACCOUNT_ACCESS_INFO, accessInfo);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I search for access roles in user \"([^\"]*)\"$")
    public void iSearchForAccessRolesFromTheLastUser(String userName) throws Exception {
        AccessInfo accessInfo = (AccessInfo) stepData.get(ACCESS_INFO);
        User user = (User) stepData.get("User");

        assertEquals(userName, user.getName());

        AccessRoleQuery accessRoleQuery = accessRoleFactory.newQuery(getCurrentScopeId());
        accessRoleQuery.setPredicate(accessRoleQuery.attributePredicate(AccessRoleAttributes.ACCESS_INFO_ID, accessInfo.getId(), AttributePredicate.Operator.EQUAL));

        try {
            primeException();
            AccessRoleListResult accessRoleListResult = accessRoleService.query(accessRoleQuery);
            stepData.put(COUNT, (long) accessRoleListResult.getSize());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I count the access roles from user in account \"([^\"]*)\"$")
    public void iCountTheAccessRolesFromUserInChildAccount(String accountName) throws Exception {
        Account account = (Account) stepData.get(LAST_ACCOUNT);
        AccessInfo accessInfo = (AccessInfo) stepData.get(CHILD_ACCOUNT_ACCESS_INFO);
        assertEquals(accountName, account.getName());

        AccessRoleQuery tmpQuery = accessRoleFactory.newQuery(account.getId());
        tmpQuery.setPredicate(tmpQuery.attributePredicate(AccessRoleAttributes.ACCESS_INFO_ID, accessInfo.getId(), AttributePredicate.Operator.EQUAL));

        try {
            primeException();
            stepData.remove(COUNT);
            Long count = accessRoleService.count(tmpQuery);
            stepData.put(COUNT, count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I update the role name to \"([^\"]*)\"$")
    public void iUpdateTheRoleName(String newRoleName) throws Exception {
        Role role = (Role) stepData.get("Role");
        role.setName(newRoleName);

        try {
            primeException();
            stepData.remove("Role");
            Role newRole = roleService.update(role);
            stepData.put("Role", newRole);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I create the access info entities$")
    public void iCreateTheAccessInfoEntities() throws Exception {
        KapuaId currScope = (KapuaId) stepData.get(LAST_ACCOUNT_ID);
        ArrayList<User> userArray = (ArrayList<User>) stepData.get("UserList");
        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(currScope);
        ArrayList<AccessInfo> accessInfoList = new ArrayList<>();

        Set<Permission> permissions = (Set<Permission>) stepData.get(PERMISSIONS);
        Set<KapuaId> roleIds = (Set<KapuaId>) stepData.get(ROLE_IDS);

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

        for (User user : userArray) {
            accessInfoCreator.setUserId(user.getId());
            try {
                primeException();
                stepData.put(ACCESS_INFO_CREATOR, accessInfoCreator);
                stepData.remove(ACCESS_INFO);
                AccessInfo accessInfo = accessInfoService.create(accessInfoCreator);
                stepData.put(ACCESS_INFO, accessInfo);
                accessInfoList.add(accessInfo);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
        stepData.put("AccessInfoList", accessInfoList);
    }

    @Given("^I prepare a role creator with name \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void iPrepareARoleCreatorWithNameAndDescription(String name, String description) {
        RoleCreator roleCreator = roleFactory.newCreator(SYS_SCOPE_ID);
        roleCreator.setName(name);
        roleCreator.setDescription(description);

        stepData.put(ROLE_CREATOR, roleCreator);
    }

    @When("^I create a new role entity from the existing creator$")
    public void iCreateANewRoleEntityFromTheExistingCreator() throws Exception {
        RoleCreator roleCreator = (RoleCreator) stepData.get(ROLE_CREATOR);
        primeException();
        try {
            stepData.remove("Role");
            stepData.remove("CurrentRoleId");
            Role role = roleService.create(roleCreator);
            stepData.put("Role", role);
            stepData.put("CurrentRoleId", role.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I create (\\d+) roles$")
    public void iCreateRoles(int num) throws Exception {
        primeException();
        try {
            for (int i = 0; i < num; i++) {
                RoleCreator tmpCreator = roleFactory.newCreator(getCurrentScopeId());
                tmpCreator.setName(String.format("TestRoleNum%d", i));
                roleService.create(tmpCreator);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the roles in the database$")
    public void iCountTheRolesInTheDatabase() throws Exception {
        RoleQuery tmpQuery = roleFactory.newQuery(getCurrentScopeId());

        primeException();
        try {
            stepData.remove(COUNT);
            Long count = roleService.count(tmpQuery);
            stepData.put(COUNT, count - 1);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I update the role description to \"([^\"]*)\"$")
    public void iUpdateTheRoleDescriptionTo(String newRoleDesc) throws Exception {
        Role role = (Role) stepData.get("Role");
        role.setDescription(newRoleDesc);

        try {
            primeException();
            stepData.remove("Role");
            Role newRole = roleService.update(role);
            stepData.put("Role", newRole);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^I delete the role with name \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void iDeleteTheRoleWithNameAndDescription(String roleName, String roleDescription) throws Throwable {
        Role role = (Role) stepData.get("Role");

        primeException();
        try {
            assertEquals(roleName, role.getName());
            assertEquals(roleDescription, role.getDescription());
            KapuaId roleId = role.getId();
            roleService.delete(role.getScopeId(), roleId);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^I search for a role with description \"([^\"]*)\"$")
    public void iFindARoleWithDescription(String roleDesc) throws Throwable {
        try {
            primeException();
            RoleQuery roleQuery = roleFactory.newQuery(getCurrentScopeId());
            roleQuery.setPredicate(roleQuery.attributePredicate(KapuaNamedEntityAttributes.DESCRIPTION, roleDesc, AttributePredicate.Operator.EQUAL));

            stepData.remove(ROLE_LIST_RESULT);
            stepData.remove(ROLE_FOUND);
            RoleListResult roleListResult = roleService.query(roleQuery);
            stepData.put(ROLE_LIST_RESULT, roleListResult);
            stepData.put(ROLE_FOUND, roleListResult.getFirstItem());
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("^I try to create roles with invalid characters \"([^\"]*)\" in name$")
    public void iTryToCreateRolesWithInvalidCharactersInName(String invalidCharacters) throws Exception {
        RoleCreator roleCreator = roleFactory.newCreator(SYS_SCOPE_ID);
        for (int i = 0; i < invalidCharacters.length(); i++) {
            String roleName = ROLE_NAME + invalidCharacters.charAt(i);
            roleCreator.setName(roleName);

            try {
                primeException();
                stepData.remove("Role");
                Role role = roleService.create(roleCreator);
                stepData.put("Role", role);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @Given("^I try to create roles with invalid characters \"([^\"]*)\" in description$")
    public void iTryToCreateRolesWithInvalidCharactersInDescription(String invalidCharacters) throws Exception {
        RoleCreator roleCreator = roleFactory.newCreator(SYS_SCOPE_ID);
        for (int i = 0; i < invalidCharacters.length(); i++) {
            String roleDescription = "roleDescription" + invalidCharacters.charAt(i);
            roleCreator.setDescription(roleDescription);
            roleCreator.setName(ROLE_NAME + i);

            try {
                primeException();
                stepData.remove("Role");
                Role role = roleService.create(roleCreator);
                stepData.put("Role", role);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @Then("^I update the role name with special characters \"([^\"]*)\"$")
    public void iUpdateTheRoleNameWithSpecialCharacters(String invalidSymbols) throws Throwable {
        RoleCreator roleCreator = roleFactory.newCreator(SYS_SCOPE_ID);
        for (int i = 0; i < invalidSymbols.length(); i++) {
            String roleName = ROLE_NAME + invalidSymbols.charAt(i);
            roleCreator.setName(ROLE_NAME + i);

            try {
                primeException();
                stepData.remove("Role");
                Role role = roleService.create(roleCreator);
                role.setName(ROLE_NAME + invalidSymbols.charAt(i));
                roleService.update(role);
                stepData.put("Role", role);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @And("^I change the group name from \"([^\"]*)\" to \"([^\"]*)\"$")
    public void iChangeTheGroupNameTo(String groupName, String newGroupName) throws Throwable {

        try {
            GroupQuery query = groupFactory.newQuery(SYS_SCOPE_ID);
            query.setPredicate(query.attributePredicate(KapuaNamedEntityAttributes.NAME, groupName, AttributePredicate.Operator.EQUAL));
            GroupListResult queryResult = groupService.query(query);
            Group group = queryResult.getFirstItem();
            group.setName(newGroupName);
            groupService.update(group);
            stepData.put(GROUP, group);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @And("^I change the description of group with name \"([^\"]*)\" to \"([^\"]*)\"$")
    public void iChangeTheGroupDescriptionTo(String groupName, String groupDescription) throws Throwable {

        try {
            GroupQuery query = groupFactory.newQuery(SYS_SCOPE_ID);
            query.setPredicate(query.attributePredicate(KapuaNamedEntityAttributes.NAME, groupName, AttributePredicate.Operator.EQUAL));
            GroupListResult queryResult = groupService.query(query);
            Group group = queryResult.getFirstItem();
            group.setDescription(groupDescription);
            stepData.put(GROUP, group);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @And("^I search for a group named \"([^\"]*)\"$")
    public void iSearchForAGroupNamed(String groupName) throws Exception {
        try {
            stepData.remove(GROUP_SECOND);
            GroupQuery query = groupFactory.newQuery(SYS_SCOPE_ID);
            query.setPredicate(query.attributePredicate(KapuaNamedEntityAttributes.NAME, groupName, AttributePredicate.Operator.EQUAL));
            GroupListResult queryResult = groupService.query(query);
            Group foundGroup = queryResult.getFirstItem();
            stepData.put(GROUP_SECOND, foundGroup);
            stepData.put("queryResult", queryResult);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @And("^I change devices group$")
    public void iChangeDevicesGroup() throws Exception {
        Group group = (Group) stepData.get(GROUP);
        Device device = (Device) stepData.get("Device");

        try {
            device.setGroupId(group.getId());
            stepData.put("Device", device);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @Given("^I create the group with name \"([^\"]*)\" and description \"([^\"]*)\"$")
    public void iCreateTheGroupWithName(String groupName, String groupDescription) throws Exception {
        GroupCreator groupCreator = groupFactory.newCreator(getCurrentScopeId());
        groupCreator.setName(groupName);
        groupCreator.setDescription(groupDescription);

        try {
            primeException();
            stepData.remove(GROUP);
            Group group = groupService.create(groupCreator);
            stepData.put(GROUP, group);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the group with description \"([^\"]*)\"$")
    public void iSearchForTheGroupWithDescription(String description) throws Exception {

        Group group = (Group) stepData.get(GROUP);
        assertEquals(description, group.getDescription());

        primeException();
        try {
            Group groupSecond = groupService.find(group.getScopeId(), group.getId());
            stepData.put(GROUP_SECOND, groupSecond);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^I find the group with description \"([^\"]*)\"$")
    public void iFindTheGroupWithDescription(String description) throws Exception {
        Group group = (Group) stepData.get(GROUP);

        try {
            primeException();
            assertEquals(description, group.getDescription());
            assertNotNull(groupService.find(getCurrentScopeId(), group.getId()));
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I update the group name from \"([^\"]*)\" to \"([^\"]*)\"$")
    public void iUpdateTheGroupNameFromTo(String name1, String name2) throws Exception {
        Group group = (Group) stepData.get(GROUP);
        assertEquals(name1, group.getName());
        group.setName(name2);

        try {
            Group secondGroup = groupService.update(group);
            stepData.put(GROUP_SECOND, secondGroup);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I update the group description from \"([^\"]*)\" to \"([^\"]*)\"$")
    public void iUpdateTheGroupDescriptionFromTo(String description1, String description2) throws Exception {
        Group group = (Group) stepData.get(GROUP);
        assertEquals(description1, group.getDescription());
        group.setDescription(description2);

        try {
            Group groupSecond = groupService.update(group);
            stepData.put(GROUP_SECOND, groupSecond);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I try to create the group with invalid characters \"([^\"]*)\" in name and description \"([^\"]*)\"$")
    public void iTryToCreateTheGroupWithInvalidCharactersInNameAndDescription(String invalidSymbols, String description) throws Exception {
        GroupCreator groupCreator = groupFactory.newCreator(getCurrentScopeId());
        for (int i = 0; i < invalidSymbols.length(); i++) {
            String groupName = GROUP_NAME + invalidSymbols.charAt(i);
            groupCreator.setName(groupName);
            groupCreator.setDescription(description);

            try {
                primeException();
                stepData.remove(GROUP);
                Group group = groupService.create(groupCreator);
                stepData.put(GROUP, group);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @When("^I update the group name from \"([^\"]*)\" to name with special characters \"([^\"]*)\"$")
    public void iUpdateTheGroupNameFromToNameWithSpecialCharacters(String name, String invalidSymbols) throws Exception {
        Group group = (Group) stepData.get(GROUP);
        assertEquals(name, group.getName());
        for (int i = 0; i < invalidSymbols.length(); i++) {
            String groupName = name + invalidSymbols.charAt(i);
            group.setName(groupName);

            try {
                primeException();
                stepData.remove(GROUP);
                Group secondGroup = groupService.update(group);
                stepData.put(GROUP_SECOND, secondGroup);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @Given("^I try to create the group with special characters \"([^\"]*)\" in description$")
    public void iTryToCreateTheGroupWithSpecialCharactersInDescription(String invalidSymbols) throws Exception {
        GroupCreator groupCreator = groupFactory.newCreator(SYS_SCOPE_ID);
        for (int i = 0; i < invalidSymbols.length(); i++) {
            String groupDescription = "description" + invalidSymbols.charAt(i);
            groupCreator.setDescription(groupDescription);
            groupCreator.setName(GROUP_NAME + i);

            try {
                primeException();
                stepData.remove(GROUP);
                Group group = groupService.create(groupCreator);
                stepData.put(GROUP, group);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @When("^I update the group description to description with special characters \"([^\"]*)\"$")
    public void iUpdateTheGroupDescriptionFromToDescriptionWithSpecialCharacters(String invalidSymbols) throws Exception {
        GroupCreator groupCreator = groupFactory.newCreator(SYS_SCOPE_ID);
        for (int i = 0; i < invalidSymbols.length(); i++) {
            String groupDescription = "description" + invalidSymbols.charAt(i);
            groupCreator.setDescription(groupDescription);
            groupCreator.setName(GROUP_NAME + i);

            try {
                primeException();
                stepData.remove(GROUP);
                Group group = groupService.create(groupCreator);
                group.setDescription("description" + invalidSymbols.charAt(i));
                groupService.update(group);
                stepData.put(GROUP, group);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

}
