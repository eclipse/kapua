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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.account.internal;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.math.BigInteger;
import java.security.acl.Permission;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.guice.KapuaLocatorImpl;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.Organization;
import org.eclipse.kapua.service.account.internal.setting.KapuaAccountSetting;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.test.MockedLocator;
import org.eclipse.kapua.test.steps.AbstractKapuaSteps;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

/**
 * Implementation of Gherkin steps used in AccountService.feature scenarios.
 *
 * MockedLocator is used for Location Service. Mockito is used to mock other
 * services that the Account services dependent on. Dependent services are: -
 * Authorization Service -
 *
 *
 */
@ScenarioScoped
public class AccountServiceTestSteps extends AbstractKapuaSteps {

    public static final String DEFAULT_PATH = "src/main/sql/H2";
    public static final String DEFAULT_COMMONS_PATH = "../../../commons";
    public static final String DROP_ACCOUNT_TABLES = "act_*_drop.sql";

    KapuaId rootScopeId = new KapuaEid(BigInteger.ONE);

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(AccountServiceTestSteps.class);

    // Account creator object used for creating new accounts.
    AccountCreator accountCreator = null;
    AccountService accountService = null;
    AccountFactory accountFactory = null;

    // Simple account objects used for creation and checks of account operations.
    Account account;
    Organization organization;
    KapuaId accountId;

    // Check if exception was fired in step.
    boolean exceptionCaught = false;

    // Currently executing scenario.
    Scenario scenario;

    // Metadata boolean value.
    Boolean boolVal = null;

    // Metadata integer value.
    Integer intVal = null;

    // Default constructor
    public AccountServiceTestSteps() {
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

    // Setup and tear-down steps

    @Before
    public void beforeScenario(Scenario scenario) throws Exception {
        container.startup();
        locator = KapuaLocatorImpl.getInstance();

        this.scenario = scenario;
        exceptionCaught = false;

        // Create User Service tables
        enableH2Connection();

        // Create the account service tables
        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "kapua", "kapua").update();
        XmlUtil.setContextProvider(new AccountsJAXBContextProvider());

        MockedLocator mockLocator = (MockedLocator) locator;

        // Inject mocked Authorization Service method checkPermission
        AuthorizationService mockedAuthorization = mock(AuthorizationService.class);
        // TODO: Check why does this line needs an explicit cast!
        Mockito.doNothing().when(mockedAuthorization).checkPermission(
                (org.eclipse.kapua.service.authorization.permission.Permission) any(Permission.class));
        mockLocator.setMockedService(org.eclipse.kapua.service.authorization.AuthorizationService.class,
                mockedAuthorization);

        // Inject mocked Permission Factory
        PermissionFactory mockedPermissionFactory = mock(PermissionFactory.class);
        mockLocator.setMockedFactory(org.eclipse.kapua.service.authorization.permission.PermissionFactory.class,
                mockedPermissionFactory);

        // Inject actual account related services
        accountService = new AccountServiceImpl();
        mockLocator.setMockedService(org.eclipse.kapua.service.account.AccountService.class, accountService);
        accountFactory = new AccountFactoryImpl();
        mockLocator.setMockedFactory(org.eclipse.kapua.service.account.AccountFactory.class, accountFactory);

        // Set KapuaMetatypeFactory for Metatype configuration
        mockLocator.setMockedFactory(org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory.class, new KapuaMetatypeFactoryImpl());

        // All operations on database are performed using system user.
        KapuaSession kapuaSession = new KapuaSession(null, new KapuaEid(BigInteger.ONE), new KapuaEid(BigInteger.ONE));
        KapuaSecurityUtils.setSession(kapuaSession);
    }

    @After
    public void afterScenario() throws Exception {
        // Drop the Account Service tables
        scriptSession(AccountEntityManagerFactory.getInstance(), DROP_ACCOUNT_TABLES);
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
        KapuaSecurityUtils.clearSession();

        container.shutdown();
    }

    // The Cucumber test steps

    @Given("^An account creator with the name \"(.*)\"$")
    public void prepareTestAccountCreatorWithName(String name) {
        accountCreator = prepareRegularAccountCreator(rootScopeId, name);
    }

    @Given("^An existing account with the name \"(.*)\"$")
    public void createTestAccountWithName(String name)
            throws KapuaException {
        accountCreator = prepareRegularAccountCreator(rootScopeId, name);
        account = accountService.create(accountCreator);
        accountId = account.getId();
    }

    @Given("^I create (\\d+) childs for account with Id (\\d+)$")
    public void createANumberOfAccounts(int num, int parentId)
            throws KapuaException {
        exceptionCaught = false;
        for (int i = 0; i < num; i++) {
            accountCreator = prepareRegularAccountCreator(new KapuaEid(BigInteger.valueOf(parentId)), "tmp_acc_" + String.format("%d", i));
            try {
                accountService.create(accountCreator);
            } catch (KapuaException ex) {
                exceptionCaught = true;
                break;
            }
        }
    }

    @Given("^I create (\\d+) childs for account with name \"(.*)\"$")
    public void createANumberOfChildrenForAccountWithName(int num, String name)
            throws KapuaException {
        Account tmpAcc = accountService.findByName(name);
        exceptionCaught = false;
        for (int i = 0; i < num; i++) {
            accountCreator = prepareRegularAccountCreator(tmpAcc.getId(), "tmp_acc_" + String.format("%d", i));
            try {
                accountService.create(accountCreator);
            } catch (KapuaException ex) {
                exceptionCaught = true;
                break;
            }
        }
    }

    @Given("^I create (\\d+) accounts with organization name \"(.*)\"$")
    public void createANumberOfChildrenForAccountWithOrganizationName(int num, String name)
            throws KapuaException {
        exceptionCaught = false;
        for (int i = 0; i < num; i++) {
            accountCreator = prepareRegularAccountCreator(rootScopeId, "tmp_acc_" + String.format("%d", i));
            accountCreator.setOrganizationName(name);
            try {
                accountService.create(accountCreator);
            } catch (KapuaException ex) {
                exceptionCaught = true;
                break;
            }
        }
    }

    @When("^I create account \"(.*)\"$")
    public void createAccount(String name)
            throws Exception {
        accountCreator = prepareRegularAccountCreator(rootScopeId, name);
        account = accountService.create(accountCreator);
        accountId = account.getId();
    }

    @When("^I create a duplicate account \"(.*)\"$")
    public void createDuplicateAccount(String name)
            throws Exception {
        accountCreator = prepareRegularAccountCreator(rootScopeId, name);
        try {
            exceptionCaught = false;
            account = accountService.create(accountCreator);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I create an account with a null name$")
    public void createAccountWithNullName() {
        accountCreator = prepareRegularAccountCreator(rootScopeId, null);
        try {
            exceptionCaught = false;
            account = accountService.create(accountCreator);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I modify the account \"(.*)\"$")
    public void changeAccountDetails(String name)
            throws KapuaException {
        account = accountService.findByName(name);
        Organization tmpOrg = account.getOrganization();

        // Change an organization detail
        tmpOrg.setName(tmpOrg.getName() + "_xx");
        tmpOrg.setCity(tmpOrg.getCity() + "_xx");
        account.setOrganization(tmpOrg);

        accountService.update(account);
    }

    @When("^I modify the current account$")
    public void updateAccount() {
        try {
            exceptionCaught = false;
            accountService.update(account);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I change the account \"(.*)\" name to \"(.*)\"$")
    public void changeAccountName(String acc_name, String name)
            throws KapuaException {
        Account tmpAcc = accountService.findByName(acc_name);

        tmpAcc.setName(name);
        try {
            exceptionCaught = false;
            accountService.update(tmpAcc);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I change the parent path for account \"(.*)\"$")
    public void changeParentPathForAccount(String name)
            throws KapuaException {
        Account tmpAcc = accountService.findByName(name);
        String modParentPath = tmpAcc.getParentAccountPath() + "/mod";

        tmpAcc.setParentAccountPath(modParentPath);
        try {
            exceptionCaught = false;
            accountService.update(tmpAcc);
        } catch (KapuaAccountException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I try to change the account \"(.*)\" scope Id to (\\d+)$")
    public void changeAccountScopeId(String name, int newScopeId)
            throws KapuaException {
        AccountImpl tmpAcc = (AccountImpl) accountService.findByName(name);

        tmpAcc.setScopeId(new KapuaEid(BigInteger.valueOf(newScopeId)));
        try {
            exceptionCaught = false;
            accountService.update(tmpAcc);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I delete account \"(.*)\"$")
    public void deleteAccountWithName(String name)
            throws KapuaException {
        Account tmpAcc = accountService.findByName(name);
        accountService.delete(tmpAcc.getScopeId(), tmpAcc.getId());
    }

    @When("^I try to delete the system account$")
    public void deleteSystemAccount()
            throws KapuaException {
        String adminUserName = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_ACCOUNT);
        Account tmpAcc = accountService.findByName(adminUserName);

        assertNotNull(tmpAcc);
        assertNotNull(tmpAcc.getId());

        try {
            exceptionCaught = false;
            accountService.delete(rootScopeId, tmpAcc.getId());
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I delete a random account$")
    public void deleteRandomAccount() {
        try {
            exceptionCaught = false;
            accountService.delete(rootScopeId, new KapuaEid(BigInteger.valueOf(random.nextLong())));
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I search for the account with name \"(.*)\"$")
    public void findAccountByName(String name)
            throws KapuaException {
        account = accountService.findByName(name);
    }

    @When("^I search for the account with the remembered account Id$")
    public void findAccountByStoredId()
            throws KapuaException {
        account = accountService.find(accountId);
    }

    @When("^I search for the account with the remembered parent and account Ids$")
    public void findAccountByStoredScopeAndAccountIds()
            throws KapuaException {
        account = accountService.find(rootScopeId, accountId);
    }

    @When("^I search for a random account Id$")
    public void findRandomAccountId()
            throws KapuaException {
        account = accountService.find(rootScopeId, new KapuaEid(BigInteger.valueOf(random.nextLong())));
    }

    @When("^I set the following parameters$")
    public void setAccountParameters(List<StringTuple> paramList)
            throws KapuaException {
        Properties accProps = account.getEntityProperties();
        for (StringTuple param : paramList) {
            accProps.setProperty(param.getName(), param.getValue());
        }
        account.setEntityProperties(accProps);
        account = accountService.update(account);
    }

    @When("^I configure \"(.*)\" item \"(.*)\" to \"(.*)\"$")
    public void setConfigurationValue(String type, String name, String value)
            throws KapuaException {
        Map<String, Object> valueMap = new HashMap<>();

        switch (type) {
        case "integer":
            valueMap.put(name, Integer.valueOf(value));
            break;
        case "string":
            valueMap.put(name, value);
            break;
        default:
            break;
        }
        valueMap.put("infiniteChildEntities", true);

        try {
            exceptionCaught = false;
            accountService.setConfigValues(account.getId(), account.getScopeId(), valueMap);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I add the unknown config item \"(.*)\" with value (\\d+)$")
    public void addUnknownIntegerConfigurationValue(String name, int value)
            throws KapuaException {
        Map<String, Object> valuesRead = accountService.getConfigValues(account.getId());
        valuesRead.put(name, value);
        accountService.setConfigValues(account.getId(), account.getScopeId(), valuesRead);
    }

    @When("^I query for all accounts that have the system account as parent$")
    public void queryForNumberOfTopLevelAccounts()
            throws KapuaException {
        AccountQuery query = new AccountQueryImpl(rootScopeId);
        KapuaListResult<Account> accList = accountService.query(query);
        intVal = accList.getSize();
    }

    @Then("^The account matches the creator settings$")
    public void checkCreatedAccountDefaults() {
        assertNotNull(account);
        assertNotNull(account.getId());
        assertNotNull(account.getId().getId());
        assertTrue(account.getOptlock() >= 0);
        assertEquals(rootScopeId, account.getScopeId());
        assertNotNull(account.getCreatedOn());
        assertNotNull(account.getCreatedBy());
        assertNotNull(account.getModifiedOn());
        assertNotNull(account.getModifiedBy());
        assertNotNull(account.getOrganization());
        assertEquals(accountCreator.getOrganizationName(), account.getOrganization().getName());
        assertEquals(accountCreator.getOrganizationPersonName(), account.getOrganization().getPersonName());
        assertEquals(accountCreator.getOrganizationCountry(), account.getOrganization().getCountry());
        assertEquals(accountCreator.getOrganizationStateProvinceCounty(), account.getOrganization().getStateProvinceCounty());
        assertEquals(accountCreator.getOrganizationCity(), account.getOrganization().getCity());
        assertEquals(accountCreator.getOrganizationAddressLine1(), account.getOrganization().getAddressLine1());
        assertEquals(accountCreator.getOrganizationAddressLine2(), account.getOrganization().getAddressLine2());
        assertEquals(accountCreator.getOrganizationEmail(), account.getOrganization().getEmail());
        assertEquals(accountCreator.getOrganizationZipPostCode(), account.getOrganization().getZipPostCode());
        assertEquals(accountCreator.getOrganizationPhoneNumber(), account.getOrganization().getPhoneNumber());
    }

    @Then("^Account \"(.*)\" exists$")
    public void checkWhetherAccountExists(String name)
            throws KapuaException {
        Account tmpAcc = accountService.findByName(name);

        assertNotNull(tmpAcc);
    }

    @Then("^Account \"(.*)\" is correctly modified$")
    public void checkForAccountModifications(String name)
            throws KapuaException {
        Account tmpAcc = accountService.findByName(name);

        assertEquals(account.getOrganization().getName(), tmpAcc.getOrganization().getName());
        assertEquals(account.getOrganization().getCity(), tmpAcc.getOrganization().getCity());
    }

    @Then("^The account with Id (\\d+) has (\\d+) subaccounts$")
    public void checkNumberOfAccounts(int parentId, int num)
            throws KapuaException {
        KapuaQuery<Account> query = accountFactory.newQuery(new KapuaEid(BigInteger.valueOf(parentId)));
        long accountCnt = accountService.count(query);

        assertEquals(num, accountCnt);
    }

    @Then("^Account \"(.*)\" has (\\d+) children$")
    public void checkNumberOfChildrenForNamedAccount(String name, int num)
            throws KapuaException {
        Account tmpAcc = accountService.findByName(name);
        KapuaQuery<Account> query = accountFactory.newQuery(tmpAcc.getId());
        long accountCnt = accountService.count(query);

        assertEquals(num, accountCnt);
    }

    @Then("^An exception is caught$")
    public void checkThatAnExceptionWasCaught() {
        assertTrue(exceptionCaught);
    }

    @Then("^The account does not exist$")
    public void tryToFindInexistentAccount()
            throws KapuaException {
        assertNull(account);
    }

    @Then("^The System account exists$")
    public void findSystemAccount()
            throws KapuaException {
        String adminUserName = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_ACCOUNT);
        Account tmpAcc = accountService.findByName(adminUserName);

        assertNotNull(tmpAcc);
    }

    @Then("^The account has the following parameters$")
    public void checkAccountParameters(List<StringTuple> paramList)
            throws KapuaException {
        Properties accProps = account.getEntityProperties();
        for (StringTuple param : paramList) {
            assertEquals(param.getValue(), accProps.getProperty(param.getName()));
        }
    }

    @Then("^The account has metadata$")
    public void checkMetadataExistence()
            throws KapuaException {
        KapuaTocd metaData = accountService.getConfigMetadata();

        assertNotNull(metaData);
    }

    @Then("^The default configuration for the account is set$")
    public void checkDefaultAccountConfiguration()
            throws KapuaException {
        Map<String, Object> valuesRead = accountService.getConfigValues(account.getId());

        assertTrue(valuesRead.containsKey("maxNumberChildEntities"));
        assertEquals(0, valuesRead.get("maxNumberChildEntities"));
    }

    @Then("^The config item \"(.*)\" is set to \"(.*)\"$")
    public void checkConfigValue(String name, String value)
            throws KapuaException {
        Map<String, Object> valuesRead = accountService.getConfigValues(account.getId());

        assertTrue(valuesRead.containsKey(name));
        assertEquals(value, valuesRead.get(name).toString());
    }

    @Then("^The config item \"(.*)\" is missing$")
    public void checkMissingConfigItem(String name)
            throws KapuaException {
        Map<String, Object> valuesRead = accountService.getConfigValues(account.getId());

        assertFalse(valuesRead.containsKey(name));
    }

    @Then("^The returned value is (\\d+)$")
    public void checkIntegerReturnValue(int val)
            throws KapuaException {
        assertEquals(Integer.valueOf(val), intVal);
    }

    @Then("^Account service metadata is available$")
    public void checkAccountServiceMetadataExistance()
            throws KapuaException {
        KapuaAccountSetting tmpAccountSettings = KapuaAccountSetting.getInstance();

        assertNotNull("Account settings not configured.", tmpAccountSettings);
    }

    // *******************
    // * Private Helpers *
    // *******************

    /**
     * Create a user creator object. The creator is pre-filled with default data.
     *
     * @param parentId
     *            Id of the parent account
     * @param name
     *            The name of the account
     * @return The newly created account creator object.
     */
    private AccountCreator prepareRegularAccountCreator(KapuaId parentId, String name) {
        AccountCreator tmpAccCreator = accountFactory.newCreator(parentId, name);

        tmpAccCreator.setOrganizationName("org_" + name);
        tmpAccCreator.setOrganizationPersonName(String.format("person_%s", name));
        tmpAccCreator.setOrganizationCountry("home_country");
        tmpAccCreator.setOrganizationStateProvinceCounty("home_province");
        tmpAccCreator.setOrganizationCity("home_city");
        tmpAccCreator.setOrganizationAddressLine1("address_line_1");
        tmpAccCreator.setOrganizationAddressLine2("address_line_2");
        tmpAccCreator.setOrganizationEmail("org_" + name + "@org.com");
        tmpAccCreator.setOrganizationZipPostCode("1234");
        tmpAccCreator.setOrganizationPhoneNumber("012/123-456-789");

        return tmpAccCreator;
    }

    // *****************
    // * Inner Classes *
    // *****************

    // Custom String tuple class for name/value pairs as given in the cucumber feature file
    public class StringTuple {

        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
