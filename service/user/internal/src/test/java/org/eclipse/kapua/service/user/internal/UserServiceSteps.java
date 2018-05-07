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
 *******************************************************************************/
package org.eclipse.kapua.service.user.internal;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.jpa.AbstractEntityManagerFactory;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.UserStatus;
import org.eclipse.kapua.test.MockedLocator;
import org.eclipse.kapua.test.steps.AbstractKapuaSteps;

import org.mockito.Matchers;
import org.mockito.Mockito;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

/**
 * Implementation of Gherkin steps used in UserService.feature scenarios.
 * <p>
 * MockedLocator is used for Location Service.
 * Mockito is used to mock other services that UserService is dependent on.
 */
@ScenarioScoped
public class UserServiceSteps extends AbstractKapuaSteps {

    static {
        setupDI();
    }

    /**
     * User service is implemented in beforeScenario()
     */
    private UserService userService;

    /**
     * User factory is implemented in beforeScenario()
     */
    private UserFactory userFactory;

    private static final String DEFAULT_COMMONS_PATH = "../../../commons/";
    private static final String DROP_FILTER = "usr_*_drop.sql";

    private static final int DEFAULT_SCOPE_ID = 42;

    /**
     * User creator object used for creating new users.
     */
    private UserCreator userCreator;

    /**
     * Simple user object used for creation of check of returned user object.
     */
    private User user;

    /**
     * Result of user qurey in last executed step.
     */
    private UserListResult queryResult;

    /**
     * Count of users in last executed step.
     */
    private long userCnt;

    /**
     * Currently executing scenario.
     */
    private Scenario scenario;

    /**
     * XML of metadata.
     */
    private KapuaTocd metadata;

    /**
     * Metadata boolean value.
     */
    private Boolean boolVal;

    /**
     * Metadata integer value.
     */
    private Integer intVal;

    /**
     * Set of users that are created in step.
     */
    private Set<ComparableUser> iHaveUsers;

    /**
     * Set of users that are found in step.
     */
    private Set<ComparableUser> iFoundUsers;

    /**
     * Scratchpad data related to exception checking
     */
    private boolean exceptionExpected;
    private String exceptionName;
    private String exceptionMessage;
    private boolean exceptionCaught;

    /**
     * Setup DI with Google Guice DI.
     * Create mocked and non mocked service under test and bind them with Guice.
     * It is based on custom MockedLocator locator that is meant for sevice unit tests.
     */
    private static void setupDI() {

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

                MockedLocator mockedLocator = (MockedLocator)locator;

                mockedLocator.setMockedService(AuthorizationService.class, mockedAuthorization);
                // Inject mocked Permission Factory
                PermissionFactory mockedPermissionFactory = Mockito.mock(PermissionFactory.class);
                mockedLocator.setMockedFactory(PermissionFactory.class, mockedPermissionFactory);
                // Set KapuaMetatypeFactory for Metatype configuration
                KapuaMetatypeFactory metaFactory = new KapuaMetatypeFactoryImpl();
                mockedLocator.setMockedFactory(KapuaMetatypeFactory.class, metaFactory);
                // Inject actual implementation of UserService
                UserEntityManagerFactory userEntityManagerFactory = (UserEntityManagerFactory) UserEntityManagerFactory.getInstance();
                bind(UserEntityManagerFactory.class).toInstance(userEntityManagerFactory);
                UserService userService = new UserServiceImpl();
                mockedLocator.setMockedService(UserService.class, userService);
                UserFactory userFactory = new UserFactoryImpl();
                mockedLocator.setMockedFactory(UserFactory.class, userFactory);
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }

    @Before
    public void beforeScenario(Scenario scenario) throws Exception {

        locator = KapuaLocator.getInstance();
        userService = locator.getService(UserService.class);
        userFactory = locator.getFactory(UserFactory.class);

        this.scenario = scenario;

        exceptionExpected = false;
        exceptionName = "";
        exceptionMessage = "";
        exceptionCaught = false;

        // Create User Service tables
        enableH2Connection();
        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "kapua", "kapua").update();

        // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
        // All operations on database are performed using system user.
        User user = userService.findByName("kapua-sys");
        KapuaSession kapuaSession = new KapuaSession(null, user.getScopeId(), user.getId());
        KapuaSecurityUtils.setSession(kapuaSession);

        // Setup JAXB context
        XmlUtil.setContextProvider(new UsersJAXBContextProvider());
    }

    @After
    public void afterScenario() throws Exception {

        // Drop User Service tables
        scriptSession((AbstractEntityManagerFactory) UserEntityManagerFactory.getInstance(), DROP_FILTER);

        // Drop system configuration tables
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);

        KapuaSecurityUtils.clearSession();
    }

    @Given("^I expect the exception \"(.+)\" with the text \"(.+)\"$")
    public void setExpectedExceptionDetails(String name, String text) {
        exceptionExpected = true;
        exceptionName = name;
        exceptionMessage = text;
    }

    @Given("^User with name \"(.*)\" in scope with id (\\d+)$")
    public void crateUserWithName(String userName, int scopeId) {
        long now = (new Date()).getTime();
        String userEmail = MessageFormat.format("testuser_{0,number,#}@organization.com", now);
        String displayName = MessageFormat.format("User Display Name {0}", now);
        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(scopeId));

        userCreator = userFactory.newCreator(scpId, userName);

        userCreator.setDisplayName(displayName);
        userCreator.setEmail(userEmail);
        userCreator.setPhoneNumber("+1 555 123 4567");
        userCreator.setUserStatus(UserStatus.ENABLED);

        scenario.write("User " + userName + " created.");
    }

    @When("^I create user$")
    public void createUser() throws Exception {
        user = userService.create(userCreator);
    }

    @When("^I change name to \"(.*)\"$")
    public void changeUserName(String userName) throws Exception {
        user.setName(userName);
        user = userService.update(user);
    }

    @When("^I change user to$")
    public void changeUserTo(List<UserImpl> userList) throws Exception {
        for (User userItem : userList) {
            user.setName(userItem.getName());
            user.setDisplayName(userItem.getDisplayName());
            user.setEmail(userItem.getEmail());
            user.setPhoneNumber(userItem.getPhoneNumber());
            user.setStatus(userItem.getStatus());
            user = userService.update(user);
        }
    }

    @When("^I delete user$")
    public void deleteUser() throws Exception {
        try {
            primeException();
            userService.delete(user);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @When("^I search for user with name \"(.*)\"$")
    public void searchUserWithName(String userName) throws Exception {
        user = userService.findByName(userName);
    }

    @When("^I search for users")
    public void searchForUsers() throws Exception {
        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(DEFAULT_SCOPE_ID));

        KapuaQuery<User> query = userFactory.newQuery(scpId);
        queryResult = userService.query(query);
        iFoundUsers = new HashSet<>();
        List<User> users = queryResult.getItems();
        for (User userItem : users) {
            iFoundUsers.add(new ComparableUser(userItem));
        }
    }

    @Then("^I find user with name \"(.*)\"$")
    public void findUserWithName(String userName) throws Exception {
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
        user = userService.findByName(userName);

        assertNull(user);
    }

    @Then("^I find user$")
    public void findUserFull(List<UserImpl> userList) throws Exception {
        for (User userItem : userList) {
            assertNotNull(user.getId());
            assertNotNull(user.getId().getId());
            assertTrue(user.getOptlock() >= 0);
            assertNotNull(user.getScopeId());
            assertEquals(userItem.getName(), user.getName());
            assertNotNull(user.getCreatedOn());
            assertNotNull(user.getCreatedBy());
            assertNotNull(user.getModifiedOn());
            assertNotNull(user.getModifiedBy());
            assertEquals(userItem.getDisplayName(), user.getDisplayName());
            assertEquals(userItem.getEmail(), user.getEmail());
            assertEquals(userItem.getPhoneNumber(), user.getPhoneNumber());
            assertEquals(userItem.getStatus(), user.getStatus());
            assertNotNull(user.getEntityProperties());
            assertNotNull(user.getEntityAttributes());
        }
    }

    @Then("^I find users$")
    public void findUsersFull(List<UserImpl> userList) throws Exception {
        for (User userItem : userList) {
            if (iFoundUsers.contains(new ComparableUser(userItem))) {
                for (ComparableUser foundUserItem : iFoundUsers) {
                    if (foundUserItem.equals(new ComparableUser(userItem))) {
                        User origUser = foundUserItem.getUser();

                        assertNotNull(origUser.getId());
                        assertNotNull(origUser.getId().getId());
                        assertTrue(origUser.getOptlock() >= 0);
                        assertNotNull(origUser.getScopeId());
                        assertEquals(userItem.getName(), origUser.getName());
                        assertNotNull(origUser.getCreatedOn());
                        assertNotNull(origUser.getCreatedBy());
                        assertNotNull(origUser.getModifiedOn());
                        assertNotNull(origUser.getModifiedBy());
                        assertEquals(userItem.getDisplayName(), origUser.getDisplayName());
                        assertEquals(userItem.getEmail(), origUser.getEmail());
                        assertEquals(userItem.getPhoneNumber(), origUser.getPhoneNumber());
                        assertEquals(userItem.getStatus(), origUser.getStatus());
                        assertNotNull(origUser.getEntityProperties());
                        assertNotNull(origUser.getEntityAttributes());
                    }
                }
            } else {
                fail("User not found name: " + userItem.getName());
            }
        }
    }

    @Then("^I find no user$")
    public void noUserFound() {

        assertNull(user);
    }

    @When("^I search for user with id (\\d+) in scope with id (\\d+)$")
    public void searchUserWithIdAndScopeId(int userId, int scopeId) throws Exception {
        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(scopeId));
        KapuaEid usrId = new KapuaEid(BigInteger.valueOf(userId));
        user = userService.find(scpId, usrId);
    }

    @When("^I search for created user by id$")
    public void searchUserById() throws Exception {
        user = userService.find(user.getId(), user.getScopeId());
    }

    @When("^I search for created user by name$")
    public void searchUserByName() throws Exception {
        user = userService.findByName(user.getName());
    }

    @When("^I query for users in scope with id (\\d+)$")
    public void queryForUsers(int scopeId) throws Exception {
        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(scopeId));

        KapuaQuery<User> query = userFactory.newQuery(scpId);
        queryResult = userService.query(query);
    }

    @When("^I count for users in scope with id (\\d+)$")
    public void countForUsers(int scopeId) throws Exception {
        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(scopeId));

        KapuaQuery<User> query = userFactory.newQuery(scpId);
        userCnt = userService.count(query);
    }

    @When("^I count users in scope (\\d+)$")
    public void countUsersInScope(int scopeId) throws Exception {
        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(scopeId));
        KapuaQuery<User> query = userFactory.newQuery(scpId);
        userCnt = userService.count(query);
    }

    @Then("^I count (\\d+) (?:user|users)$")
    public void countUserCount(int cnt) {
        assertEquals(cnt, userCnt);
    }

    @Then("^I count (\\d+) (?:user|users) as query result list$")
    public void countUserQuery(int cnt) {
        assertEquals(cnt, queryResult.getSize());
    }

    @Then("^I create same user$")
    public void createSameUser() throws Exception {
        try {
            primeException();
            user = userService.create(userCreator);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Then("^I get Kapua exception$")
    public void getKapuaException() throws Exception {
        if (!exceptionCaught) {
            fail("Should fail with KapuaException.");
        }
    }

    @Given("^User that doesn't exist$")
    public void createNonexistentUser() {
        user = createUserInstance(3234123, 1354133);
    }

    @When("^I update nonexistent user$")
    public void updateNonexistenUser() throws Exception {
        try {
            primeException();
            userService.update(user);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @When("^I delete nonexistent user$")
    public void deleteNonexistenUser() throws Exception {
        try {
            primeException();
            userService.delete(user);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("^I have following (?:user|users)$")
    public void haveUsers(List<UserImpl> userList) throws Exception {
        iHaveUsers = new HashSet<>();
        try {
            primeException();
            for (User userItem : userList) {
                String name = userItem.getName();
                String displayName = userItem.getDisplayName();
                String email = userItem.getEmail();
                String phone = userItem.getPhoneNumber();
                @SuppressWarnings("unused")
                UserStatus status = userItem.getStatus();

                KapuaEid scpId = new KapuaEid(BigInteger.valueOf(DEFAULT_SCOPE_ID));
                UserCreator userCreator = userCreatorCreator(name, displayName, email, phone, scpId);
                user = userService.create(userCreator);
                iHaveUsers.add(new ComparableUser(user));
            }
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @When("^I retrieve metadata$")
    public void getMetadata() throws KapuaException {
        metadata = userService.getConfigMetadata();
    }

    @Then("^I have metadata$")
    public void haveMetadata() {
        if (metadata == null) {
            fail("Metadata should be retreived.");
        }
    }

    @When("^I retrieve \"(.*)\" metadata with id \"(.*)\" in scope (\\d+)$")
    public void getMetadataWithIdInScope(String type, String metadataId, int scopeId) throws KapuaException {
        KapuaEid scpId = new KapuaEid(BigInteger.valueOf(scopeId));
        Map<String, Object> values = userService.getConfigValues(scpId);

        switch (type) {
        case "boolean":
            boolVal = (Boolean) values.get(metadataId);
            break;
        case "integer":
            intVal = (Integer) values.get(metadataId);
            break;
        default:
            break;
        }
    }

    @Then("^I receive \"(.*)\" metadata with value \"(.*)\"$")
    public void reciveMetadata(String type, String value) {
        switch (type) {
        case "boolean":
            Boolean expectedBool = Boolean.valueOf(value);
            if (!boolVal.equals(expectedBool)) {
                fail(MessageFormat.format("Boolean values doesn't match, expected {0}, was {1}", expectedBool, boolVal));
            }
            break;
        case "integer":
            Integer expectedInt = Integer.valueOf(value);
            if (!intVal.equals(expectedInt)) {
                fail(MessageFormat.format("Integer values doesn't match, expected {0}, was {1}", expectedInt, intVal));
            }
            break;
        default:
            break;
        }
    }

    @When("^I configure$")
    public void setConfigurationValue(List<TestConfig> testConfigs) throws Exception {
        Map<String, Object> valueMap = new HashMap<>();

        for (TestConfig config : testConfigs) {
            config.addConfigToMap(valueMap);
        }
        try {
            primeException();
            userService.setConfigValues(new KapuaEid(BigInteger.valueOf(DEFAULT_SCOPE_ID)),
                    new KapuaEid(BigInteger.ONE), valueMap);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    // *******************
    // * Private Helpers *
    // *******************

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
     * Create userCreator instance with full data about user.
     *
     * @return UserCreator instance for creating user
     */
    private UserCreator userCreatorCreator(String name, String displayName, String email, String phone, KapuaEid scopeId) {
        UserCreator userCreator = userFactory.newCreator(scopeId, name);

        userCreator.setName(name);
        userCreator.setDisplayName(displayName);
        userCreator.setEmail(email);
        userCreator.setPhoneNumber(phone);

        return userCreator;
    }

    private void primeException() {
        exceptionCaught = false;
    }

    /**
     * Check the exception that was caught. In case the exception was expected the type and message is shown in the cucumber logs.
     * Otherwise the exception is rethrown failing the test and dumping the stack trace to help resolving problems.
     */
    private void verifyException(Exception ex)
            throws Exception {

        if (!exceptionExpected ||
                (!exceptionName.isEmpty() && !ex.getClass().toGenericString().contains(exceptionName)) ||
                (!exceptionMessage.isEmpty() && !exceptionMessage.trim().contentEquals("*") && !ex.getMessage().contains(exceptionMessage))) {
            scenario.write("An unexpected exception was raised!");
            throw(ex);
        }

        scenario.write("Exception raised as expected: " + ex.getClass().getCanonicalName() + ", " + ex.getMessage());
        exceptionCaught = true;
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
