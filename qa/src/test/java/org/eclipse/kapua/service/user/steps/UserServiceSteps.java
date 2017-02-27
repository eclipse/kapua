/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *
 *******************************************************************************/
package org.eclipse.kapua.service.user.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.internal.AccountFactoryImpl;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialFactoryImpl;
import org.eclipse.kapua.service.authentication.shiro.UsernamePasswordCredentialsImpl;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoFactoryImpl;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.UserDomain;
import org.eclipse.kapua.service.user.internal.UserFactoryImpl;
import org.eclipse.kapua.test.KapuaTest;

import java.math.BigInteger;
import java.util.*;

import static org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers.resolveJdbcUrl;

/**
 * Implementation of Gherkin steps used in UserServiceI9n.feature scenarios.
 */
public class UserServiceSteps extends KapuaTest {

    /**
     * Path to root of full DB schema scripts.
     */
    public static final String FULL_SCHEMA_PATH = "../dev-tools/src/main/database/";

    /**
     * Filter for droping full DB schema.
     */
    public static final String DROP_FILTER = "all_drop.sql";

    /**
     * User service by locator.
     */
    private UserService userService;

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

    /**
     * Check if exception was fired in step.
     */
    private boolean isException;

    /**
     * One of two users used in tests - A
     */
    ComparableUser userA = null;

    /**
     * One of two users used in tests - B
     */
    ComparableUser userB = null;

    /**
     * Account that was created by last Account creation step.
     */
    private Account lastAccount;

    /**
     * User that was created by last User creation step.
     */
    private ComparableUser lastUser;

    @Before
    public void beforeScenario(Scenario scenario) throws KapuaException {

        this.isException = false;

        // Create User Service tables
        enableH2Connection();
        new KapuaLiquibaseClient(resolveJdbcUrl(), "kapua", "kapua").update();

        // Services by default Locator
        KapuaLocator locator = KapuaLocator.getInstance();
        userService = locator.getService(UserService.class);
        authenticationService = locator.getService(AuthenticationService.class);
        accountService = locator.getService(AccountService.class);
        credentialService = locator.getService(CredentialService.class);
        accessInfoService = locator.getService(AccessInfoService.class);
    }

    @After
    public void afterScenario() throws KapuaException {

        // Drop User Service tables
        KapuaConfigurableServiceSchemaUtils.scriptSession(FULL_SCHEMA_PATH, DROP_FILTER);

        KapuaSecurityUtils.clearSession();
    }

    @Given("^Account$")
    public void givenAccount(List<TestAccount> accountList) throws Exception {
        TestAccount testAccount = accountList.get(0);
        // If accountId is not set in account list, use last created Account for scope id
        if (testAccount.getScopeId() == null) {
            testAccount.setScopeId(lastAccount.getId().getId());
        }

        lastAccount = createAccount(testAccount);
    }

    @Given("^Credentials$")
    public void givenCredentials(List<TestCredentials> credentialsList) throws Exception {
        TestCredentials testCredentials = credentialsList.get(0);

        createCredentials(testCredentials);
    }

    @Given("^Permissions$")
    public void givenPermissions(List<TestPermission> permissionList) throws Exception {
        createPermissions(permissionList, lastUser, lastAccount);
    }

    @Given("^User A$")
    public void givenUserA(List<TestUser> userList) throws Exception {
        // User is created within account that was last created in steps
        HashSet<ComparableUser> createdList = createUsersInList(userList, lastAccount);
        Iterator<ComparableUser> userIterator = createdList.iterator();
        while (userIterator.hasNext()) {
            userA = userIterator.next();
        }
        lastUser = userA;
    }

    @Given("^User B$")
    public void givenUserB(List<TestUser> userList) throws Exception {
        // User is created within account that was last created in steps
        HashSet<ComparableUser> createdList = createUsersInList(userList, lastAccount);
        Iterator<ComparableUser> userIterator = createdList.iterator();
        while (userIterator.hasNext()) {
            userB = userIterator.next();
        }
        lastUser = userB;
    }

    @When("^I login as user with name \"(.*)\" and password \"(.*)\"$")
    public void loginUser(String userName, String password) throws KapuaException {

        char[] passwd = password.toCharArray();
        LoginCredentials credentials = new UsernamePasswordCredentialsImpl(userName, passwd);
        authenticationService.login(credentials);
    }

    @Then("^I try to delete user \"(.*)\"$")
    public void thenDeleteUser(String userName) throws KapuaException {
        try {
            User userToDelete = userService.findByName(userName);
            if (userToDelete != null) {
                userService.delete(userToDelete);
            }
        } catch (KapuaException e) {
            isException = true;
        }
    }

    @Then("^I get KapuaException$")
    public void thenGetKapuaException() throws KapuaException {
        if (!isException) {
            fail("Should fail with KapuaException.");
        }
    }

    @Then("^I don't get KapuaException$")
    public void thenDontGetKapuaException() throws KapuaException {
        if (isException) {
            fail("Should not fail with KapuaException.");
        }
    }

    @Then("^I logout$")
    public void logout() throws KapuaException {
        authenticationService.logout();
    }

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
    private HashSet<ComparableUser> createUsersInList(List<TestUser> userList, Account account) throws Exception {
        HashSet<ComparableUser> users = new HashSet<>();
        KapuaSecurityUtils.doPriviledge(() -> {
            try {
                for (TestUser userItem : userList) {
                    String name = userItem.getName();
                    String displayName = userItem.getDisplayName();
                    String email = userItem.getEmail();
                    String phone = userItem.getPhoneNumber();
                    KapuaEid scopeId = (KapuaEid) account.getScopeId();

                    UserCreator userCreator = userCreatorCreator(name, displayName, email, phone, scopeId);
                    User user = userService.create(userCreator);
                    users.add(new ComparableUser(user));
                }
            } catch (KapuaException ke) {
                isException = true;
            }

            return null;
        });

        return users;
    }

    /**
     * Create account in privileged mode as kapua-sys user.
     * Account is created in scope specified by scopeId in testAccount parameter.
     * This is not accountId, but account under which it is created. AccountId itself
     * is created automatically.
     *
     * @param testAccount basic data about account
     * @return Kapua Account object
     */
    private Account createAccount(TestAccount testAccount) throws Exception {
        List<Account> accountList = new ArrayList<>();
        KapuaSecurityUtils.doPriviledge(() -> {
            try {
                Account account = accountService.create(accountCreatorCreator(testAccount.getName(),
                        testAccount.getScopeId()));
                accountList.add(account);
            } catch (KapuaException ke) {
                isException = true;
            }

            return null;
        });

        return accountList.size() == 1 ? accountList.get(0) : null;
    }

    /**
     * Create credentials for specific user, set users password.
     * It finds user by name and sets its password.
     *
     * @param testCredentials username and open password
     * @return created credential
     */
    private Credential createCredentials(TestCredentials testCredentials) throws Exception {
        List<Credential> credentialList = new ArrayList<>();

        KapuaSecurityUtils.doPriviledge(() -> {
            try {
                User user = userService.findByName(testCredentials.getName());

                Credential credential = credentialService.create(credentialCreatorCreator(user.getScopeId(),
                        user.getId(), testCredentials.getPassword()));
                credentialList.add(credential);
            } catch (KapuaException ke) {
                isException = true;
            }

            return null;
        });

        return credentialList.size() == 1 ? credentialList.get(0) : null;
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
    private void createPermissions(List<TestPermission> permissionList, ComparableUser user, Account account)
            throws Exception {

        KapuaSecurityUtils.doPriviledge(() -> {
            try {
                accessInfoService.create(accessInfoCreatorCreator(permissionList, user, account));
            } catch (KapuaException ke) {
                isException = true;
            }

            return null;
        });

        return;
    }

    /**
     * Create account creator.
     *
     * @param name    account name
     * @param scopeId acount scope id
     * @return
     */
    private AccountCreator accountCreatorCreator(String name, BigInteger scopeId) {
        AccountCreator accountCreator;

        accountCreator = new AccountFactoryImpl().newAccountCreator(new KapuaEid(scopeId), name);
        accountCreator.setAccountPassword("TooManySecrets#123");
        accountCreator.setOrganizationName("ACME Inc.");
        accountCreator.setOrganizationEmail("some@one.com");

        return accountCreator;
    }

    /**
     * Create credential creator for user with password.
     *
     * @param scopeId  scopeId in which user is
     * @param userId   userId for which credetntials are set
     * @param password open password as credetntials
     * @return credential creator used for creating credentials
     */
    private CredentialCreator credentialCreatorCreator(KapuaId scopeId, KapuaId userId, String password) {
        CredentialCreator credentialCreator;

        credentialCreator = new CredentialFactoryImpl().newCreator(scopeId, userId, CredentialType.PASSWORD, password);

        return credentialCreator;
    }

    /**
     * Create userCreator instance with full data about user.
     *
     * @return UserCreator instance for creating user
     */
    private UserCreator userCreatorCreator(String name, String displayName, String email, String phone, KapuaEid scopeId) {
        UserCreator userCreator = new UserFactoryImpl().newCreator(scopeId, name);

        userCreator.setName(name);
        userCreator.setDisplayName(displayName);
        userCreator.setEmail(email);
        userCreator.setPhoneNumber(phone);

        return userCreator;
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

}
