/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.user.steps;

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
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.qa.common.cucumber.CucConfig;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.qa.common.cucumber.CucCredentials;
import org.eclipse.kapua.qa.common.cucumber.CucPermission;
import org.eclipse.kapua.qa.common.cucumber.CucUser;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.shiro.UsernamePasswordCredentialsImpl;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserDomain;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.UserStatus;
import org.eclipse.kapua.service.user.internal.UserEntityManagerFactory;
import org.eclipse.kapua.service.user.internal.UserFactoryImpl;
import org.eclipse.kapua.service.user.internal.UserServiceImpl;
import org.eclipse.kapua.test.MockedLocator;
import org.junit.Assert;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of Gherkin steps used in user test scenarios.
 * <p>
 * MockedLocator is used for Location Service.
 * Mockito is used to mock other services that UserService is dependent on.
 */
@ScenarioScoped
public class UserServiceSteps extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceSteps.class);

    /**
     * User service by locator.
     */
    private UserService userService;

    /**
     * User factory by locator.
     */
    private UserFactory userFactory;

    /**
     * Security services services by locator.
     */
    private AccessInfoService accessInfoService;
    private AuthenticationService authenticationService;
    private AccessInfoFactory accessInfoFactory;
    private PermissionFactory permissionFactory;
    private CredentialService credentialService;
    private CredentialFactory credentialFactory;


    @Inject
    public UserServiceSteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
        this.database = dbHelper;
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

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

                // Inject actual user service related services
                UserEntityManagerFactory userEntityManagerFactory = UserEntityManagerFactory.getInstance();
                bind(UserEntityManagerFactory.class).toInstance(userEntityManagerFactory);
                bind(UserService.class).toInstance(new UserServiceImpl());
                bind(UserFactory.class).toInstance(new UserFactoryImpl());
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }

    @Before
    public void beforeScenario(Scenario scenario) {

        if (isUnitTest()) {
            setupDI();
        }

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();
        userService = locator.getService(UserService.class);
        userFactory = locator.getFactory(UserFactory.class);
        authenticationService = locator.getService(AuthenticationService.class);
        credentialService = locator.getService(CredentialService.class);
        accessInfoService = locator.getService(AccessInfoService.class);
        accessInfoFactory = locator.getFactory(AccessInfoFactory.class);
        permissionFactory = locator.getFactory(PermissionFactory.class);
        credentialFactory = locator.getFactory(CredentialFactory.class);

        if (isUnitTest()) {
            // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
            // All operations on database are performed using system user.
            // Only for unit tests. Integration tests assume that a real logon is performed.
            KapuaSession kapuaSession = new KapuaSession(null, SYS_SCOPE_ID, SYS_USER_ID);
            KapuaSecurityUtils.setSession(kapuaSession);
        }

        // Setup JAXB context
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

    @Given("^User with name \"(.*)\" in scope with id (\\d+)$")
    public void crateUserWithName(String userName, int scopeId) {
        long now = (new Date()).getTime();
        String userEmail = MessageFormat.format("testuser_{0,number,#}@organization.com", now);
        String displayName = MessageFormat.format("User Display Name {0}", now);
        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(scopeId));

        UserCreator uc = userFactory.newCreator(scpId, userName);
        uc.setDisplayName(displayName);
        uc.setEmail(userEmail);
        uc.setPhoneNumber("+1 555 123 4567");
        uc.setUserStatus(UserStatus.ENABLED);

        stepData.put("UserCreator", uc);

        scenario.write("User " + userName + " created.");
    }

    @When("^I create user$")
    public void createUser() throws Exception {
        stepData.remove("User");
        User user = userService.create((UserCreator) stepData.get("UserCreator"));
        stepData.put("User", user);
    }

    @Given("^An invalid user$")
    public void provideInvalidUserObject() {
        User user = userFactory.newEntity(getKapuaId());
        user.setId(getKapuaId());
        user.setName(getKapuaId().toString());
        stepData.put("User", user);
    }

    @When("^I change name to \"(.*)\"$")
    public void changeUserName(String userName) throws Exception {
        User user = (User)stepData.get("User");
        user.setName(userName);
        user = userService.update(user);
        stepData.put("User", user);
    }

    @When("^I change user to$")
    public void changeUserTo(List<CucUser> userList) throws Exception {
        User user = (User) stepData.get("User");
        for (CucUser userItem : userList) {
            user.setName(userItem.getName());
            user.setDisplayName(userItem.getDisplayName());
            user.setEmail(userItem.getEmail());
            user.setPhoneNumber(userItem.getPhoneNumber());
            user.setStatus(userItem.getStatus());
            user = userService.update(user);
        }
        stepData.put("User", user);
    }

    @When("^I delete user$")
    public void deleteUser() throws Exception {
        try {
            primeException();
            User user = (User) stepData.get("User");
            userService.delete(user);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @When("^I search for user with name \"(.*)\"$")
    public void searchUserWithName(String userName) throws Exception {
        stepData.remove("User");
        User user = userService.findByName(userName);
        stepData.put("User", user);
    }

    @When("^I search for users")
    public void searchForUsers() throws Exception {
        KapuaId scpId = DEFAULT_ID;
        Set<ComparableUser> iFoundUsers;

        stepData.remove("UserList");

        KapuaQuery<User> query = userFactory.newQuery(scpId);
        UserListResult queryResult = userService.query(query);
        iFoundUsers = new HashSet<>();
        List<User> users = queryResult.getItems();
        for (User userItem : users) {
            iFoundUsers.add(new ComparableUser(userItem));
        }
        stepData.put("UserList", iFoundUsers);
    }

    @Then("^I find user with name \"(.*)\"$")
    public void findUserWithName(String userName) throws Exception {
        UserCreator userCreator = (UserCreator) stepData.get("UserCreator");
        User user = (User) stepData.get("User");

        assertNotNull(user.getId());
        assertNotNull(user.getId().getId());
        assertTrue(user.getOptlock() >= 0);
        assertNotNull(user.getScopeId());
        assertEquals(userName, user.getName());
        assertNotNull(user.getCreatedOn());
        assertNotNull(user.getCreatedBy());
        assertNotNull(user.getModifiedOn());
        assertNotNull(user.getModifiedBy());
        assertEquals(userCreator.getDisplayName(), user.getDisplayName());
        assertEquals(userCreator.getEmail(), user.getEmail());
        assertEquals(userCreator.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(UserStatus.ENABLED, user.getStatus());
    }

    @Then("^I don't find user with name \"(.*)\"$")
    public void dontFindUserWithName(String userName) throws Exception {
        User user = userService.findByName(userName);
        assertNull(user);
    }

    @Then("^I find user$")
    public void findUserFull(List<CucUser> userList) {
        User user = (User) stepData.get("User");

        for (CucUser userItem : userList) {
            matchUserData(user, userItem);
        }
    }

    @Then("^I find users$")
    public void findUsersFull(List<CucUser> userList) {

        Set<ComparableUser> iFoundUsers = (Set<ComparableUser>) stepData.get("UserList");
        boolean userChecks;

        for (CucUser userItem : userList) {
            userChecks = false;
            for (ComparableUser foundUserItem : iFoundUsers) {
                if (foundUserItem.getUser().getName().equals(userItem.getName())) {
                    matchUserData(foundUserItem.getUser(), userItem);
                    userChecks = true;
                    break;
                }
            }
            if (!userChecks) {
                fail(String.format("User %1s was not found!", userItem.getName()));
            }
        }
    }

    @Then("^I find no user$")
    public void noUserFound() {

        assertNull(stepData.get("User"));
    }

    @When("^I search for user with id (\\d+) in scope with id (\\d+)$")
    public void searchUserWithIdAndScopeId(int userId, int scopeId) throws Exception {
        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(scopeId));
        KapuaEid usrId = new KapuaEid(BigInteger.valueOf(userId));
        stepData.remove("User");
        User user = userService.find(scpId, usrId);
        stepData.put("User", user);
    }

    @When("^I search for created user by id$")
    public void searchUserById() throws Exception {
        User user = (User) stepData.get("User");
        user = userService.find(user.getId(), user.getScopeId());
        stepData.put("User", user);
    }

    @When("^I search for created user by name$")
    public void searchUserByName() throws Exception {
        User user = (User) stepData.get("User");
        user = userService.findByName(user.getName());
        stepData.put("User", user);
    }

    @When("^I query for users in scope with id (\\d+)$")
    public void queryForUsers(int scopeId) throws Exception {
        stepData.remove("UserList");
        UserQuery query = userFactory.newQuery(new KapuaEid(BigInteger.valueOf(scopeId)));
        UserListResult queryResult = userService.query(query);
        Set<ComparableUser> iFoundUsers = new HashSet<>();
        for (User userItem : queryResult.getItems()) {
            iFoundUsers.add(new ComparableUser(userItem));
        }
        stepData.put("UserList", iFoundUsers);
    }

    @When("^I count users in scope (\\d+)$")
    public void countUsersInScope(int scopeId) throws Exception {
        UserQuery query = userFactory.newQuery(new KapuaEid(BigInteger.valueOf(scopeId)));
        stepData.remove("Count");
        Long userCnt = userService.count(query);
        stepData.put("Count", userCnt);
    }

    @Then("^I count (\\d+) (?:user|users)$")
    public void countUserCount(Long cnt) {
        assertEquals(cnt, stepData.get("Count"));
    }

    @Then("^I count (\\d+) (?:user|users) as query result list$")
    public void countUserQuery(long cnt) {
        Set<ComparableUser> userLst = (Set<ComparableUser>)stepData.get("UserList");
        assertEquals(cnt, userLst.size());
    }

    @Then("^I create same user$")
    public void createSameUser() throws Exception {
        try {
            primeException();
            userService.create((UserCreator)stepData.get("UserCreator"));
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("^User that doesn't exist$")
    public void createNonexistentUser() {
        stepData.remove("User");
        User user = createUserInstance(3234123, 1354133);
        stepData.put("User", user);
    }

    @When("^I update nonexistent user$")
    public void updateNonexistenUser() throws Exception {
        User user = (User) stepData.get("User");
        try {
            primeException();
            userService.update(user);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @When("^I delete nonexistent user$")
    public void deleteNonexistenUser() throws Exception {
        User user = (User) stepData.get("User");
        try {
            primeException();
            userService.delete(user);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("^I have the following (?:user|users)$")
    public void haveUsers(List<CucUser> userList) throws Exception {

        Account account = (Account) stepData.get("LastAccount");
        KapuaId accountId = (KapuaId) stepData.get("LastAccountId");
        KapuaId currentAccount;
        Set<ComparableUser> iHaveUsers = new HashSet<>();
        User lastUser = null;
        stepData.remove("UserList");

        if (account != null) {
            currentAccount = account.getId();
        } else if (accountId != null) {
            currentAccount = accountId;
        } else {
            currentAccount = DEFAULT_ID;
        }

        primeException();
        try {
            for (CucUser userItem : userList) {
                String name = userItem.getName();
                String displayName = userItem.getDisplayName();
                String email = userItem.getEmail();
                String phone = userItem.getPhoneNumber();
                @SuppressWarnings("unused")
                UserStatus status = userItem.getStatus();

                UserCreator userCreator = userCreatorCreator(name, displayName, email, phone, currentAccount);
                User user = userService.create(userCreator);
                iHaveUsers.add(new ComparableUser(user));
                lastUser = user;
            }
        } catch (KapuaException ke) {
            verifyException(ke);
        }
        stepData.put("UserList", iHaveUsers);
        stepData.put("User", lastUser);
    }

    @When("^I retrieve metadata in scope (\\d+)$")
    public void getMetadata(int scopeId) throws KapuaException {
        stepData.remove("Metadata");
        KapuaTocd metadata = userService.getConfigMetadata(getKapuaId(scopeId));
        stepData.put("Metadata", metadata);
    }

    @Then("^I have metadata$")
    public void haveMetadata() {
        KapuaTocd metadata = (KapuaTocd) stepData.get("Metadata");
        assertNotNull("Metadata should be retrieved.", metadata);
    }

    @Given("^Credentials$")
    public void givenCredentials(List<CucCredentials> credentialsList) throws Exception {
        CucCredentials cucCredentials = credentialsList.get(0);
        createCredentials(cucCredentials);
    }

    @Given("^Permissions$")
    public void givenPermissions(List<CucPermission> permissionList) throws Exception {
        createPermissions(permissionList, (ComparableUser) stepData.get("LastUser"), (Account) stepData.get("LastAccount"));
    }

    @Given("^Full permissions$")
    public void givenFullPermissions() throws Exception {
        createPermissions(null, (ComparableUser) stepData.get("LastUser"), (Account) stepData.get("LastAccount"));
    }

    @Given("^User A$")
    public void givenUserA(List<CucUser> userList) throws Exception {
        // User is created within account that was last created in steps
        ComparableUser tmpUser = null;
        HashSet<ComparableUser> createdList = createUsersInList(userList, (Account) stepData.get("LastAccount"));
        Iterator<ComparableUser> userIterator = createdList.iterator();
        while (userIterator.hasNext()) {
            tmpUser = userIterator.next();
        }

        stepData.put("UserA", tmpUser);
        stepData.put("LastUser", tmpUser);
        stepData.put("User", tmpUser.getUser());
    }

    @Given("^User B$")
    public void givenUserB(List<CucUser> userList) throws Exception {
        // User is created within account that was last created in steps
        ComparableUser tmpUser = null;
        HashSet<ComparableUser> createdList = createUsersInList(userList, (Account) stepData.get("LastAccount"));
        Iterator<ComparableUser> userIterator = createdList.iterator();
        while (userIterator.hasNext()) {
            tmpUser = userIterator.next();
        }
        stepData.put("UserB", tmpUser);
        stepData.put("LastUser", tmpUser);
        stepData.put("User", tmpUser.getUser());
    }

    @Given("^A generic user$")
    public void givenGenericUser(List<CucUser> userList) throws Exception {
        // User is created within account that was last created in steps
        ComparableUser tmpUser = null;
        HashSet<ComparableUser> createdList = createUsersInList(userList, (Account) stepData.get("LastAccount"));
        Iterator<ComparableUser> userIterator = createdList.iterator();
        while (userIterator.hasNext()) {
            tmpUser = userIterator.next();
        }
        stepData.put("LastUser", tmpUser);
        stepData.put("User", tmpUser.getUser());
    }

    @When("^I login as user with name \"(.*)\" and password \"(.*)\"$")
    public void loginUser(String userName, String password) throws Exception {

        String passwd = password;
        LoginCredentials credentials = new UsernamePasswordCredentialsImpl(userName, passwd);
        authenticationService.logout();

        primeException();
        try {
            authenticationService.login(credentials);
        } catch (KapuaException e) {
            verifyException(e);
        }
    }

    @Then("^I try to delete user \"(.*)\"$")
    public void thenDeleteUser(String userName) throws Exception {

        primeException();
        try {
            User userToDelete = userService.findByName(userName);
            if (userToDelete != null) {
                userService.delete(userToDelete);
            }
        } catch (KapuaException e) {
            verifyException(e);
        }
    }

    @Then("^I find user \"(.*)\"$")
    public void thenIFindUser(String userName) throws Exception {

        primeException();
        try {
            User user = userService.findByName(userName);
            Assert.assertNotNull("User doesn't exist.", user);
        } catch (KapuaException e) {
            verifyException(e);
        }
    }

    @Then("^I don't find user \"(.*)\"$")
    public void thenIdontFindUser(String userName) throws Exception {

        primeException();
        try {
            User user = userService.findByName(userName);
            Assert.assertNull("User still exists.", user);
        } catch (KapuaException e) {
            verifyException(e);
        }
    }

    @Given("^Move User compact id from step data \"(.*)\" to \"(.*)\"$")
    public void moveUserCompactIdStepData(String keyFrom, String keyTo) {

        ComparableUser comparableUser = (ComparableUser) stepData.get(keyFrom);
        stepData.put(keyTo, comparableUser.getUser().getId().toCompactId());
    }

    @When("^I configure user service$")
    public void setUserServiceConfig(List<CucConfig> cucConfigs)
            throws Exception {
        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId;
        KapuaId scopeId;
        Account tmpAccount = (Account) stepData.get("LastAccount");

        if (tmpAccount != null) {
            accId = tmpAccount.getId();
            scopeId = tmpAccount.getScopeId();
        } else {
            accId = SYS_SCOPE_ID;
            scopeId = SYS_SCOPE_ID;
        }

        for (CucConfig config : cucConfigs) {
            config.addConfigToMap(valueMap);
        }

        primeException();
        try {
            userService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I configure the user service for the account with the id (\\d+)$")
    public void setUserServiceConfig(int accountId, List<CucConfig> cucConfigs)
            throws Exception {
        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId = new KapuaEid(BigInteger.valueOf(accountId));
        KapuaId scopeId = SYS_SCOPE_ID;

        for (CucConfig config : cucConfigs) {
            config.addConfigToMap(valueMap);
        }

        primeException();
        try {
            userService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I configure credential service$")
    public void setCredentialServiceConfig(List<CucConfig> cucConfigs)
            throws Exception {
        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId;
        KapuaId scopeId;
        Account tmpAccount = (Account) stepData.get("LastAccount");

        if (tmpAccount != null) {
            accId = tmpAccount.getId();
            scopeId = tmpAccount.getScopeId();
        } else {

            accId = SYS_SCOPE_ID;
            scopeId = SYS_SCOPE_ID;
        }

        for (CucConfig config : cucConfigs) {
            config.addConfigToMap(valueMap);
        }

        primeException();
        try {
            credentialService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^I logout$")
    public void logout() throws KapuaException {
        authenticationService.logout();
    }

    // *******************
    // * Private Helpers *
    // *******************
    /**
     * Extract list of users form step parameter table and create those users in
     * kapua.
     * Operation is performed in privileged mode, without access and authorization checks.
     *
     * @param userList list of users in step
     * @param account  account in which users are created
     * @return Set of created users as ComparableUser Set
     * @throws Exception
     */
    private HashSet<ComparableUser> createUsersInList(List<CucUser> userList, Account account) throws Exception {
        HashSet<ComparableUser> users = new HashSet<>();
        KapuaSecurityUtils.doPrivileged(() -> {
            primeException();
            try {
                for (CucUser userItem : userList) {
                    String name = userItem.getName();
                    String displayName = userItem.getDisplayName();
                    String email = userItem.getEmail();
                    String phone = userItem.getPhoneNumber();
                    KapuaEid scopeId = (KapuaEid) account.getId();
                    Date expirationDate = userItem.getExpirationDate();

                    UserCreator userCreator = userCreatorCreator(name, displayName, email, phone, scopeId, expirationDate);
                    User user = userService.create(userCreator);
                    users.add(new ComparableUser(user));
                }
            } catch (KapuaException ke) {
                verifyException(ke);
            }

            return null;
        });

        return users;
    }

    /**
     * Create User object with user data filed with quasi random data for user name,
     * email, display name. Scope id and user id is set to test wide id.
     *
     * @param userId
     *            unique user id
     * @param scopeId
     *            user scope id
     * @return User instance
     */
    private User createUserInstance(int userId, int scopeId) {
        long now = (new Date()).getTime();
        String username = MessageFormat.format("aaa_test_username_{0,number,#}", now);
        String userEmail = MessageFormat.format("testuser_{0,number,#}@organization.com", now);
        String displayName = MessageFormat.format("User Display Name {0}", now);
        KapuaEid usrId = new KapuaEid(BigInteger.valueOf(userId));
        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(scopeId));

        User user = userFactory.newEntity(scpId);
        user.setId(usrId);
        user.setName(username);
        user.setDisplayName(displayName);
        user.setEmail(userEmail);

        return user;
    }

    /**
     * Create userCreator instance with full data about user.
     *
     * @return UserCreator instance for creating user
     */
    private UserCreator userCreatorCreator(String name, String displayName, String email, String phone, KapuaId scopeId) {
        UserCreator userCreator = userFactory.newCreator(scopeId, name);

        userCreator.setName(name);
        userCreator.setDisplayName(displayName);
        userCreator.setEmail(email);
        userCreator.setPhoneNumber(phone);

        return userCreator;
    }

    private UserCreator userCreatorCreator(String name, String displayName, String email, String phone, KapuaId scopeId, Date expirationDate) {
        UserCreator userCreator = userCreatorCreator(name, displayName, email, phone, scopeId);
        userCreator.setExpirationDate(expirationDate);

        return userCreator;
    }

    /**
     * Create credentials for specific user, set users password.
     * It finds user by name and sets its password.
     *
     * @param cucCredentials username and open password
     * @return created credential
     */
    private Credential createCredentials(CucCredentials cucCredentials) throws Exception {
        List<Credential> credentialList = new ArrayList<>();

        KapuaSecurityUtils.doPrivileged(() -> {
            primeException();
            try {
                User user = userService.findByName(cucCredentials.getName());

                Credential credential = credentialService.create(credentialCreatorCreator(user.getScopeId(),
                        user.getId(), cucCredentials.getPassword(),
                        cucCredentials.getStatus(), cucCredentials.getExpirationDate()));
                credentialList.add(credential);
            } catch (KapuaException ke) {
                verifyException(ke);
            }

            return null;
        });

        return credentialList.size() == 1 ? credentialList.get(0) : null;
    }

    /**
     * Create credential creator for user with password.
     *
     * @param scopeId        scopeId in which user is
     * @param userId         userId for which credetntials are set
     * @param password       open password as credetntials
     * @param status         status of credentials enabled or disabled
     * @param expirationDate credential expiration date
     * @return credential creator used for creating credentials
     */
    private CredentialCreator credentialCreatorCreator(KapuaId scopeId, KapuaId userId, String password, CredentialStatus status, Date expirationDate) {
        CredentialCreator credentialCreator;

        credentialCreator = credentialFactory.newCreator(scopeId, userId, CredentialType.PASSWORD, password, status, expirationDate);

        return credentialCreator;
    }

    /**
     * Creates permissions for user with specified account. Permissions are created in priveledged mode.
     *
     * @param permissionList list of permissions for user, if targetScopeId is not set user scope that is
     *                       specifed as account
     * @param user           user for whom permissions are set
     * @param account        account in which user is defined
     * @throws Exception
     */
    private void createPermissions(List<CucPermission> permissionList, ComparableUser user, Account account)
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            primeException();
            try {
                accessInfoService.create(accessInfoCreatorCreator(permissionList, user, account));
            } catch (KapuaException ke) {
                verifyException(ke);
            }

            return null;
        });

        return;
    }

    /**
     * Create accessInfoCreator instance with data about user permissions.
     * If target scope is not defined in permission list use account scope.
     *
     * @param permissionList list of all permissions
     * @param user           user for which permissions are set
     * @param account        that user belongs to
     * @return AccessInfoCreator instance for creating user permissions
     */
    private AccessInfoCreator accessInfoCreatorCreator(List<CucPermission> permissionList, ComparableUser user, Account account) {

        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(account.getId());
        accessInfoCreator.setUserId(user.getUser().getId());
        accessInfoCreator.setScopeId(user.getUser().getScopeId());
        Set<Permission> permissions = new HashSet<>();
        if (permissionList != null) {
            for (CucPermission cucPermission : permissionList) {
                Actions action = cucPermission.getAction();
                KapuaEid targetScopeId = cucPermission.getTargetScopeId();
                if (targetScopeId == null) {
                    targetScopeId = (KapuaEid) account.getId();
                }
                Domain domain = new UserDomain();
                Permission permission = permissionFactory.newPermission(domain,
                        action, targetScopeId);
                permissions.add(permission);
            }
        } else {
            Permission permission = permissionFactory.newPermission(null, null, null);
            permissions.add(permission);
        }
        accessInfoCreator.setPermissions(permissions);

        return accessInfoCreator;
    }

    private boolean matchUserData(User user, CucUser cucUser) {
        assertNotNull(user.getId());
        assertNotNull(user.getScopeId());
        if ((cucUser.getName() != null) && (cucUser.getName().length() > 0)) {
            assertEquals(cucUser.getName(), user.getName());
        }
        assertNotNull(user.getCreatedOn());
        assertNotNull(user.getCreatedBy());
        assertNotNull(user.getModifiedOn());
        assertNotNull(user.getModifiedBy());
        if ((cucUser.getDisplayName() != null) && (cucUser.getDisplayName().length() > 0)) {
            assertEquals(cucUser.getDisplayName(), user.getDisplayName());
        }
        if ((cucUser.getEmail() != null) && (cucUser.getEmail().length() > 0)) {
            assertEquals(cucUser.getEmail(), user.getEmail());
        }
        if ((cucUser.getPhoneNumber() != null) && (cucUser.getPhoneNumber().length() > 0)) {
            assertEquals(cucUser.getPhoneNumber(), user.getPhoneNumber());
        }
        if (cucUser.getStatus() != null) {
            assertEquals(cucUser.getStatus(), user.getStatus());
        }
        return true;
    }

    // *****************
    // * Inner Classes *
    // *****************
    private class ComparableUser {

        private User user;

        ComparableUser(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ComparableUser) {
                ComparableUser other = (ComparableUser) obj;
                return compareAllUserAtt(user, other.getUser());
            } else {
                return false;
            }
        }

        private boolean compareAllUserAtt(User thisUser, User otherUser) {
            return thisUser.getName().equals(otherUser.getName()) &&
                    thisUser.getDisplayName().equals(otherUser.getDisplayName()) &&
                    thisUser.getEmail().equals(otherUser.getEmail()) &&
                    thisUser.getPhoneNumber().equals(otherUser.getPhoneNumber()) &&
                    thisUser.getStatus().equals(otherUser.getStatus());
        }
    }
}
