/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.user.steps;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
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
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.qa.steps.BaseQATests;
import org.eclipse.kapua.qa.steps.DBHelper;
import org.eclipse.kapua.service.StepData;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.internal.AccountFactoryImpl;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialFactoryImpl;
import org.eclipse.kapua.service.authentication.shiro.UsernamePasswordCredentialsImpl;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoFactoryImpl;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.UserStatus;
import org.eclipse.kapua.service.user.internal.UserDomain;
import org.eclipse.kapua.service.user.internal.UserEntityManagerFactory;
import org.eclipse.kapua.service.user.internal.UserFactoryImpl;
import org.eclipse.kapua.service.user.internal.UserImpl;
import org.eclipse.kapua.service.user.internal.UserServiceImpl;
import org.eclipse.kapua.service.user.internal.UsersJAXBContextProvider;
import org.eclipse.kapua.test.MockedLocator;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Implementation of Gherkin steps used in User Service feature scenarios.
 */
@ScenarioScoped
public class UserServiceSteps extends BaseQATests {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceSteps.class);

    /**
     * User service by locator.
     */
    private UserService userService;
    private UserFactory userFactory;

    /**
     * Account service by locator.
     */
    private AccountService accountService;

    /**
     * Authentication service by locator.
     */
    private AuthenticationService authenticationService;

    /**
     * Credential service by locator.
     */
    private CredentialService credentialService;

    /**
     * Permission service by locator.
     */
    private AccessInfoService accessInfoService;

    @Inject
    public UserServiceSteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
        this.database = dbHelper;
    }

    @Before
    public void beforeScenario(Scenario scenario) {

        this.database.setup();

        // Services by default Locator
        locator = KapuaLocator.getInstance();
        userService = locator.getService(UserService.class);
        userFactory = locator.getFactory(UserFactory.class);
        authenticationService = locator.getService(AuthenticationService.class);
        accountService = locator.getService(AccountService.class);
        credentialService = locator.getService(CredentialService.class);
        accessInfoService = locator.getService(AccessInfoService.class);

        XmlUtil.setContextProvider(new UsersJAXBContextProvider());

        this.scenario = scenario;
        this.stepData.clear();

        if (isUnitTest()) {
            try {
                setupMockLocatorForUser();
            } catch (Exception ex) {
                logger.error("Failed to set up mock locator in @Before", ex);
            }
        }
    }

    @After
    public void afterScenario() {

        try {
            logger.info("Logging out in cleanup");
            if (isIntegrationTest()) {
                SecurityUtils.getSubject().logout();
            }
            KapuaSecurityUtils.clearSession();
        } catch (Exception e) {
            logger.error("Failed to log out in @After", e);
        }
    }

    @Given("^Account$")
    public void givenAccount(List<TestAccount> accountList) throws Exception {
        TestAccount testAccount = accountList.get(0);
        // If accountId is not set in account list, use last created Account for scope id
        if (testAccount.getScopeId() == null) {
            testAccount.setScopeId(((Account) stepData.get("LastAccount")).getId().getId());
        }

        stepData.put("LastAccount", createAccount(testAccount));
    }

    @Given("^Credentials$")
    public void givenCredentials(List<TestCredentials> credentialsList) throws Exception {
        TestCredentials testCredentials = credentialsList.get(0);
        createCredentials(testCredentials);
    }

    @Given("^Permissions$")
    public void givenPermissions(List<TestPermission> permissionList) throws Exception {
        ComparableUser tmpCompUsr = new ComparableUser((User) stepData.get("LastUser"));
        createPermissions(permissionList, tmpCompUsr, (Account) stepData.get("LastAccount"));
    }

    @Given("^User A$")
    public void givenUserA(List<TestUser> userList) throws Exception {
        // User is created within account that was last created in steps
        ComparableUser tmpUser = null;
        Account tmpAccount = (Account) stepData.get("LastAccount");
        KapuaId tmpId = (KapuaId) stepData.get("CurrentScopeId");
        KapuaId accId;

        if (tmpAccount != null) {
            accId = tmpAccount.getId();
        } else if (tmpId != null) {
            accId = tmpId;
        } else {
            accId = new KapuaEid(BigInteger.ONE);
        }

        HashSet<ComparableUser> createdList = createUsersInList(userList, accId);
        Iterator<ComparableUser> userIterator = createdList.iterator();
        while (userIterator.hasNext()) {
            tmpUser = userIterator.next();
        }

        stepData.put("UserA", tmpUser);
        stepData.put("LastUser", tmpUser.getUser());
    }

    @Given("^User B$")
    public void givenUserB(List<TestUser> userList) throws Exception {
        // User is created within account that was last created in steps
        ComparableUser tmpUser = null;
        Account tmpAccount = (Account) stepData.get("LastAccount");
        KapuaId tmpId = (KapuaId) stepData.get("CurrentScopeId");
        KapuaId accId;

        if (tmpAccount != null) {
            accId = tmpAccount.getId();
        } else if (tmpId != null) {
            accId = tmpId;
        } else {
            accId = new KapuaEid(BigInteger.ONE);
        }

        HashSet<ComparableUser> createdList = createUsersInList(userList, accId);
        Iterator<ComparableUser> userIterator = createdList.iterator();
        while (userIterator.hasNext()) {
            tmpUser = userIterator.next();
        }

        stepData.put("UserB", tmpUser);
        stepData.put("LastUser", tmpUser.getUser());
    }

    @Given("^(?:The|A) generic users?$")
    public void givenGenericUser(List<TestUser> userList) throws Exception {
        // User is created within account that was last created in steps
        ComparableUser tmpUser = null;
        Account tmpAccount = (Account) stepData.get("LastAccount");
        KapuaId tmpId = (KapuaId) stepData.get("CurrentScopeId");
        KapuaId accId;

        if (tmpAccount != null) {
            accId = tmpAccount.getId();
        } else if (tmpId != null) {
            accId = tmpId;
        } else {
            accId = new KapuaEid(BigInteger.ONE);
        }

        HashSet<ComparableUser> createdList = createUsersInList(userList, accId);
        Iterator<ComparableUser> userIterator = createdList.iterator();
        while (userIterator.hasNext()) {
            tmpUser = userIterator.next();
        }
        if (tmpUser != null) {
            stepData.put("LastUser", tmpUser.getUser());
        }
    }

    @When("^I create user \"(.*)\" in scope with id (\\d+)$")
    public void crateUserWithName(String userName, int scopeId) throws Exception {
        long now = (new Date()).getTime();
        String userEmail = MessageFormat.format("testuser_{0,number,#}@organization.com", now);
        String displayName = MessageFormat.format("User Display Name {0}", now);
        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(scopeId));
        UserCreator tmpCreator;

        tmpCreator = userFactory.newCreator(scpId, userName);
        tmpCreator.setDisplayName(displayName);
        tmpCreator.setEmail(userEmail);
        tmpCreator.setPhoneNumber("+1 555 123 4567");
        tmpCreator.setUserStatus(UserStatus.ENABLED);

        primeException();
        try {
            User tmpUsr = userService.create(tmpCreator);
            stepData.put("LastUser", tmpUsr);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Given("^A fake user with random IDs$")
    public void createFakeUser() {

        User tmpUsr = createUserInstance(random.nextInt(), random.nextInt());
        stepData.put("LastUser", tmpUsr);
    }

    @When("^I update the user$")
    public void updateLastUser() throws Exception {

        User tmpUsr = (User) stepData.get("LastUser");
        try {
            primeException();
            userService.update(tmpUsr);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @When("^I change user to$")
    public void changeUserTo(List<TestUser> userList) throws Exception {

        User tmpUsr = (User) stepData.get("LastUser");
        primeException();

        try {
            for (TestUser userItem : userList) {
                tmpUsr.setName(userItem.getName());
                tmpUsr.setDisplayName(userItem.getDisplayName());
                tmpUsr.setEmail(userItem.getEmail());
                tmpUsr.setPhoneNumber(userItem.getPhoneNumber());
                tmpUsr.setStatus(userItem.getStatus());
                tmpUsr = userService.update(tmpUsr);
            }
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created user by id$")
    public void searchUserById() throws Exception {

        User lastUsr = (User) stepData.get("LastUser");

        primeException();
        try {
            stepData.remove("LastUser");
            User tmpUsr = userService.find(lastUsr.getScopeId(), lastUsr.getId());
            stepData.put("LastUser", tmpUsr);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @When("^I search for a user with name \"(.*)\"$")
    public void searchUserWithName(String userName) throws Exception {

        stepData.remove("LastUser");
        primeException();
        try {
            User tmpUsr = userService.findByName(userName);
            stepData.put("LastUser", tmpUsr);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @When("^I query for users in scope with id (\\d+)$")
    public void queryForUsers(int scopeId) throws Exception {

        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(scopeId));
        KapuaQuery<User> query = userFactory.newQuery(scpId);
        UserListResult tmpList;

        try {
            stepData.remove("UserList");
            tmpList = userService.query(query);
            stepData.put("UserList", tmpList);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @When("^I count users in scope (\\d+)$")
    public void countUsersInScope(int scopeId) throws Exception {
        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(scopeId));
        KapuaQuery<User> query = userFactory.newQuery(scpId);

        primeException();
        try {
            stepData.remove("Count");
            long tmpCnt = userService.count(query);
            stepData.put("Count", tmpCnt);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @When("^I search for a user with id (\\d+) in scope with id (\\d+)$")
    public void searchUserWithIdAndScopeId(int userId, int scopeId) throws Exception {
        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(scopeId));
        KapuaEid usrId = new KapuaEid(BigInteger.valueOf(userId));

        primeException();
        try {
            stepData.remove("LastUser");
            User tmpUser = userService.find(scpId, usrId);
            stepData.put("LastUser", tmpUser);
        } catch(Exception ex) {
            verifyException(ex);
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

    @When("^I delete the user \"(.+)\"$")
    public void deleteNamedUser(String name) throws Exception {

        primeException();
        try {
            User tmpUsr = userService.findByName(name);
            userService.delete(tmpUsr);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @When("^Delete the last user$")
    public void deleteLastUser() throws Exception {

        User tmpUsr = (User) stepData.get("LastUser");

        primeException();
        try {
            userService.delete(tmpUsr);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Then("^The user matches the data$")
    public void checkTheUserData(List<TestUser> reqList) {

        User tmpUsr = (User) stepData.get("LastUser");

        assertNotNull(tmpUsr);
        assertNotNull(reqList);
        assertEquals(1, reqList.size());

        assertTrue(checkUserAgainstRequirements(tmpUsr, reqList.get(0)));
    }

    @Then("^The users in the list match the data$")
    public void checkUsersFull(List<TestUser> reqList) {

        UserListResult usrList = (UserListResult) stepData.get("UserList");
        boolean found = false;

        assertNotNull(usrList);
        assertFalse(usrList.isEmpty());
        assertNotNull(reqList);
        assertEquals(reqList.size(), usrList.getSize());

        for(User tmpUsr: usrList.getItems()) {
            found = false;
            for (TestUser tmpReq : reqList) {
                if (checkUserAgainstRequirements(tmpUsr, tmpReq)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                break;
            }
        }
        assertTrue(found);
    }

    @Then("^There is no such user$")
    public void checkThatNoUserWasFound() {

        User tmpUSr = (User) stepData.get("LastUser");
        assertNull(tmpUSr);
    }

    @Then("^There (?:is|are) (\\d+) users? in the query result list$")
    public void countUserQuery(int cnt) {

        UserListResult tmpList = (UserListResult) stepData.get("UserList");
        assertEquals(cnt, tmpList.getSize());
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

    @When("^I select account \"(.*)\"$")
    public void selectAccount(String accountName) throws KapuaException{

        Account tmpAccount;
        tmpAccount = accountService.findByName(accountName);
        if (tmpAccount != null) {
            stepData.put("LastAccount", tmpAccount);
        } else {
            stepData.remove("LastAccount");
        }
    }

    @When("^I try to delete account \"(.*)\"$")
    public void deleteAccount(String accountName) throws KapuaException{

        Account accountToDelete;
        accountToDelete = accountService.findByName(accountName);
        if (accountToDelete != null) {
            accountService.delete(accountToDelete.getScopeId(), accountToDelete.getId());
        }
    }

    @When("^I set the current scope id to (\\d+)$")
    public void setCurrentScopeId(int id) {

        stepData.put("CurrentScopeId", new KapuaEid(BigInteger.valueOf(id)));
    }

    @Then("^I find user \"(.*)\"$")
    public void thenIFindUser(String userName) throws Exception {

        primeException();
        try {
            User user = userService.findByName(userName);
            assertNotNull("User doesn't exist.", user);
        } catch (KapuaException e) {
            verifyException(e);
        }
    }

    @Then("^I don't find user \"(.*)\"$")
    public void thenIdontFindUser(String userName) throws Exception {

        primeException();
        try {
            User user = userService.findByName(userName);
            assertNull("User still exists.", user);
        } catch (KapuaException e) {
            verifyException(e);
        }
    }

    @When("^I configure account service$")
    public void setAccountServiceConfig(List<TestConfig> testConfigs)
            throws Exception {
        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId;
        KapuaId scopeId;

        for (TestConfig config : testConfigs) {
            config.addConfigToMap(valueMap);
        }

        primeException();
        try {
            Account tmpAccount = (Account) stepData.get("LastAccount");
            if (tmpAccount != null) {
                accId = tmpAccount.getId();
                scopeId = new KapuaEid(BigInteger.ONE);
            } else {
                accId = new KapuaEid(BigInteger.ONE);
                scopeId = new KapuaEid(BigInteger.ONE);
            }
            accountService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I configure user service$")
    public void setUserServiceConfig(List<TestConfig> testConfigs)
            throws Exception {
        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId;
        KapuaId scopeId;
        Account tmpAccount = (Account) stepData.get("LastAccount");
        KapuaId tmpId = (KapuaId) stepData.get("CurrentScopeId");

        if (tmpAccount != null) {
            accId = tmpAccount.getId();
            scopeId = tmpAccount.getScopeId();
        } else if (tmpId != null) {
            accId = tmpId;
            scopeId = new KapuaEid(BigInteger.ONE);
        } else {
            accId = new KapuaEid(BigInteger.ONE);
            scopeId = new KapuaEid(BigInteger.ONE);
        }

        for (TestConfig config : testConfigs) {
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
    public void setCredentialServiceConfig(List<TestConfig> testConfigs)
            throws Exception {
        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId;
        KapuaId scopeId;
        Account tmpAccount = (Account) stepData.get("LastAccount");

        if (tmpAccount != null) {
            accId = tmpAccount.getId();
            scopeId = tmpAccount.getScopeId();
        } else {
            accId = new KapuaEid(BigInteger.ONE);
            scopeId = new KapuaEid(BigInteger.ONE);
        }

        for (TestConfig config : testConfigs) {
            config.addConfigToMap(valueMap);
        }

        primeException();
        try {
            credentialService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I retrieve metadata$")
    public void getMetadata() throws KapuaException {

        KapuaTocd metadata = userService.getConfigMetadata();
        stepData.put("UserMetadata", metadata);
    }

    @Then("^I have metadata$")
    public void haveMetadata() {

        KapuaTocd metadata = (KapuaTocd) stepData.get("UserMetadata");
        assertNotNull("Metadata should be retreived.", metadata);
    }

    @Then("^I logout$")
    public void logout() throws KapuaException {
        authenticationService.logout();
    }

    @And("^Using kapua-sys account$")
    public void usingSysAccount() {
        stepData.put("LastAccount", null);
    }

    /**
     * Extract list of users form step parameter table and create those users in
     * kapua.
     * Operation is performed in privileged mode, without access and authorization checks.
     *
     * @param userList
     *            list of users in step
     * @param accountId
     *            account in which users are created
     * @return Set of created users as ComparableUser Set
     * @throws Exception
     */
    private HashSet<ComparableUser> createUsersInList(List<TestUser> userList, KapuaId accountId /*Account account*/) throws Exception {
        HashSet<ComparableUser> users = new HashSet<>();
        KapuaSecurityUtils.doPrivileged(() -> {
            primeException();
            try {
                for (TestUser userItem : userList) {
                    String name = userItem.getName();
                    String displayName = userItem.getDisplayName();
                    String email = userItem.getEmail();
                    String phone = userItem.getPhoneNumber();
                    KapuaEid scopeId = (KapuaEid) accountId; //(KapuaEid) account.getId();
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

        User user = new UserImpl(scpId, username);

        user.setId(usrId);
        user.setName(username);
        user.setDisplayName(displayName);
        user.setEmail(userEmail);

        return user;
    }

    /**
     * Create account in privileged mode as kapua-sys user.
     * Account is created in scope specified by scopeId in testAccount parameter.
     * This is not accountId, but account under which it is created. AccountId itself
     * is created automatically.
     *
     * @param testAccount
     *            basic data about account
     * @return Kapua Account object
     */
    private Account createAccount(TestAccount testAccount) throws Exception {
        List<Account> accountList = new ArrayList<>();
        KapuaSecurityUtils.doPrivileged(() -> {
            primeException();
            try {
                Account account = accountService.create(accountCreatorCreator(testAccount.getName(),
                        testAccount.getScopeId()));
                accountList.add(account);
            } catch (KapuaException ke) {
                verifyException(ke);
            }

            return null;
        });

        return accountList.size() == 1 ? accountList.get(0) : null;
    }

    /**
     * Create credentials for specific user, set users password.
     * It finds user by name and sets its password.
     *
     * @param testCredentials
     *            username and open password
     * @return created credential
     */
    private Credential createCredentials(TestCredentials testCredentials) throws Exception {
        List<Credential> credentialList = new ArrayList<>();

        KapuaSecurityUtils.doPrivileged(() -> {
            primeException();
            try {
                User user = userService.findByName(testCredentials.getName());

                Credential credential = credentialService.create(
                        credentialCreatorCreator(user.getScopeId(),
                        user.getId(), testCredentials.getPassword(),
                        testCredentials.getStatus(), testCredentials.getExpirationDate()));
                credentialList.add(credential);
            } catch (KapuaException ke) {
                verifyException(ke);
            }

            return null;
        });

        return credentialList.size() == 1 ? credentialList.get(0) : null;
    }

    /**
     * Creates permissions for user with specified account. Permissions are created in priveledged mode.
     *
     * @param permissionList
     *            list of permissions for user, if targetScopeId is not set user scope that is
     *            specifed as account
     * @param user
     *            user for whom permissions are set
     * @param account
     *            account in which user is defined
     * @throws Exception
     */
    private void createPermissions(List<TestPermission> permissionList, ComparableUser user, Account account)
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
     * Create account creator.
     *
     * @param name
     *            account name
     * @param scopeId
     *            acount scope id
     * @return
     */
    private AccountCreator accountCreatorCreator(String name, BigInteger scopeId) {
        AccountCreator accountCreator;

        accountCreator = new AccountFactoryImpl().newCreator(new KapuaEid(scopeId), name);
        accountCreator.setOrganizationName("ACME Inc.");
        accountCreator.setOrganizationEmail("some@one.com");

        return accountCreator;
    }

    /**
     * Create credential creator for user with password.
     *
     * @param scopeId
     *            scopeId in which user is
     * @param userId
     *            userId for which credetntials are set
     * @param password
     *            open password as credetntials
     * @param status status of credentials enabled or disabled
     * @param expirationDate credential expiration date
     * @return credential creator used for creating credentials
     */
    private CredentialCreator credentialCreatorCreator(KapuaId scopeId, KapuaId userId, String password, CredentialStatus status, Date expirationDate) {
        CredentialCreator credentialCreator;

        credentialCreator = new CredentialFactoryImpl().newCreator(scopeId, userId, CredentialType.PASSWORD, password, status, expirationDate);

        return credentialCreator;
    }

    /**
     * Create userCreator instance with full data about user.
     *
     * @return UserCreator instance for creating user
     */
    private UserCreator userCreatorCreator(String name, String displayName, String email, String phone, KapuaEid scopeId, Date expirationDate) {
        UserCreator userCreator = new UserFactoryImpl().newCreator(scopeId, name);

        userCreator.setName(name);
        userCreator.setDisplayName(displayName);
        userCreator.setEmail(email);
        userCreator.setPhoneNumber(phone);
        userCreator.setExpirationDate(expirationDate);

        return userCreator;
    }

    /**
     * Create accessInfoCreator instance with data about user permissions.
     * If target scope is not defined in permission list use account scope.
     *
     * @param permissionList
     *            list of all permissions
     * @param user
     *            user for which permissions are set
     * @param account
     *            that user belongs to
     * @return AccessInfoCreator instance for creating user permissions
     */
    private AccessInfoCreator accessInfoCreatorCreator(List<TestPermission> permissionList,
            ComparableUser user, Account account) {

        PermissionFactory permissionFactory = new PermissionFactoryImpl();
        AccessInfoCreator accessInfoCreator = new AccessInfoFactoryImpl().newCreator(account.getId());
        accessInfoCreator.setUserId(user.getUser().getId());
        accessInfoCreator.setScopeId(user.getUser().getScopeId());
        Set<Permission> permissions = new HashSet<>();
        for (TestPermission testPermission : permissionList) {
            Actions action = testPermission.getAction();
            KapuaEid targetScopeId = testPermission.getTargetScopeId();
            if (targetScopeId == null) {
                targetScopeId = (KapuaEid) account.getId();
            }
            Domain domain = new UserDomain();
            Permission permission = permissionFactory.newPermission(domain,
                    action, targetScopeId);
            permissions.add(permission);
        }
        accessInfoCreator.setPermissions(permissions);

        return accessInfoCreator;
    }

    /**
     * Check whether the supplied user matches the data in the requirements.
     *
     * @param user
     *          The user to be checked
     * @param reqs
     *          The requirements that must be met by the user
     * @return
     */
    private boolean checkUserAgainstRequirements(User user, TestUser reqs) {

        boolean res = true;

        if (reqs.getName() != null) {
            res &= user.getName().equals(reqs.getName());
        }
        if (reqs.getDisplayName() != null) {
            res &= user.getDisplayName().equals(reqs.getDisplayName());
        }
        if (reqs.getEmail() != null) {
            res &= user.getEmail().equals(reqs.getEmail());
        }
        if (reqs.getPhoneNumber() != null) {
            res &= user.getPhoneNumber().equals(reqs.getPhoneNumber());
        }
        if (reqs.getExpirationDate() != null) {
            res &= user.getExpirationDate().equals(reqs.getExpirationDate());
        }
        if (reqs.getStatus() != null) {
            res &= user.getStatus().equals(reqs.getStatus());
        }
        if (reqs.getUserType() != null) {
            res &= user.getUserType().equals(reqs.getUserType());
        }

        return res;
    }

    /**
     * Set up the preconditions for unit tests. This includes filling the mock locator with the correct
     * mocked services and the actual service implementation under test.
     * Also, all the unit tests will be run with the kapua-sys user.
     *
     * @throws Exception
     */
    private void setupMockLocatorForUser() throws Exception {
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
                PermissionFactory mockedPermissionFactory = Mockito.mock(PermissionFactory.class);
                bind(PermissionFactory.class).toInstance(mockedPermissionFactory);
                // Set KapuaMetatypeFactory for Metatype configuration
                KapuaMetatypeFactory metaFactory = new KapuaMetatypeFactoryImpl();
                bind(KapuaMetatypeFactory.class).toInstance(metaFactory);

                // Inject actual implementation of UserService
                UserEntityManagerFactory userEntityManagerFactory = (UserEntityManagerFactory) UserEntityManagerFactory.getInstance();
                bind(UserEntityManagerFactory.class).toInstance(userEntityManagerFactory);
                UserService userService = new UserServiceImpl();
                bind(UserService.class).toInstance(userService);
                UserFactory userFactory = new UserFactoryImpl();
                bind(UserFactory.class).toInstance(userFactory);
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);

        userService = KapuaLocator.getInstance().getService(UserService.class);
        userFactory = KapuaLocator.getInstance().getFactory(UserFactory.class);

        // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
        // All operations on database are performed using system user.
        User user = userService.findByName("kapua-sys");
        KapuaSession kapuaSession = new KapuaSession(null, user.getScopeId(), user.getId());
        KapuaSecurityUtils.setSession(kapuaSession);
    }
}
