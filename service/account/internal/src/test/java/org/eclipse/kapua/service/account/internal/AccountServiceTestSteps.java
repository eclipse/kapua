/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
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

/**
 * Implementation of Gherkin steps used in AccountService.feature scenarios.
 *
 * MockedLocator is used for Location Service. Mockito is used to mock other
 * services that the Account services dependent on. Dependent services are:
 * - Authorization Service
 *
 */
@ScenarioScoped
public class AccountServiceTestSteps extends AbstractKapuaSteps {

    static {
        setupDI();
    }

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceTestSteps.class);

    private static final String DEFAULT_COMMONS_PATH = "../../../commons";
    private static final String DROP_ACCOUNT_TABLES = "act_*_drop.sql";

    private KapuaId rootScopeId = new KapuaEid(BigInteger.ONE);

    // Account creator object used for creating new accounts.
    private AccountCreator accountCreator;
    private AccountService accountService;
    private AccountFactory accountFactory;

    // Simple account objects used for creation and checks of account operations.
    private Account account;
    private KapuaId accountId;

    // Scratchpad data related to exception checking
    private boolean exceptionExpected;
    private String exceptionName;
    private String exceptionMessage;
    private boolean exceptionCaught;

    // Metadata integer value.
    private Integer intVal;

    private Scenario scenario;

    // Default constructor
    public AccountServiceTestSteps() {
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

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
                AuthorizationService mockedAuthorization = mock(AuthorizationService.class);
                try {
                    Mockito.doNothing().when(mockedAuthorization).checkPermission(any(org.eclipse.kapua.service.authorization.permission.Permission.class));
                } catch (KapuaException e) {
                    // skip
                }
                bind(AuthorizationService.class).toInstance(mockedAuthorization);
                // Inject mocked Permission Factory
                PermissionFactory mockedPermissionFactory = mock(PermissionFactory.class);
                bind(PermissionFactory.class).toInstance(mockedPermissionFactory);
                // Set KapuaMetatypeFactory for Metatype configuration
                KapuaMetatypeFactory metaFactory = new KapuaMetatypeFactoryImpl();
                bind(KapuaMetatypeFactory.class).toInstance(metaFactory);

                // Inject actual account related services
                AccountService accountService = new AccountServiceImpl();
                bind(AccountService.class).toInstance(accountService);
                AccountFactory accountFactory = new AccountFactoryImpl();
                bind(AccountFactory.class).toInstance(accountFactory);
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }

    // Setup and tear-down steps

    @Before
    public void beforeScenario(Scenario scenario)
            throws Exception {

        this.scenario = scenario;

        locator = KapuaLocator.getInstance();
        this.accountService = locator.getService(AccountService.class);
        this.accountFactory = locator.getFactory(AccountFactory.class);

        exceptionExpected = false;
        exceptionName = "";
        exceptionMessage = "";
        exceptionCaught = false;

        // Create User Service tables
        enableH2Connection();

        // Create the account service tables
        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "kapua", "kapua").update();

        // All operations on database are performed using system user.
        KapuaSession kapuaSession = new KapuaSession(null, new KapuaEid(BigInteger.ONE), new KapuaEid(BigInteger.ONE));
        KapuaSecurityUtils.setSession(kapuaSession);

        XmlUtil.setContextProvider(new AccountsJAXBContextProvider());
    }

    @After
    public void afterScenario() throws Exception {
        // Drop the Account Service tables
        scriptSession(AccountEntityManagerFactory.getInstance(), DROP_ACCOUNT_TABLES);
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
        KapuaSecurityUtils.clearSession();
    }

    // The Cucumber test steps

    @Given("^I expect the exception \"(.+)\" with the text \"(.+)\"$")
    public void setExpectedExceptionDetails(String name, String text) {

        exceptionExpected = true;
        exceptionName = name;
        exceptionMessage = text;
    }

    @Given("^An account creator with the name \"(.*)\"$")
    public void prepareTestAccountCreatorWithName(String name) {
        accountCreator = prepareRegularAccountCreator(rootScopeId, name);
    }

    @Given("^An existing account with the name \"(.*)\"$")
    public void createTestAccountWithName(String name)
            throws Exception {

        accountCreator = prepareRegularAccountCreator(rootScopeId, name);
        try {
            primeException();
            account = accountService.create(accountCreator);
            accountId = account.getId();
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I create (\\d+) childs for account with Id (\\d+)$")
    public void createANumberOfAccounts(int num, int parentId)
            throws Exception {

        for (int i = 0; i < num; i++) {
            accountCreator = prepareRegularAccountCreator(new KapuaEid(BigInteger.valueOf(parentId)), "tmp_acc_" + String.format("%d", i));
            try {
                primeException();
                accountService.create(accountCreator);
            } catch (KapuaException ex) {
                verifyException(ex);
                break;
            }
        }
    }

    @Given("^I create (\\d+) childs for account with name \"(.*)\"$")
    public void createANumberOfChildrenForAccountWithName(int num, String name)
            throws Exception {

        Account tmpAcc = accountService.findByName(name);
        for (int i = 0; i < num; i++) {
            accountCreator = prepareRegularAccountCreator(tmpAcc.getId(), "tmp_acc_" + String.format("%d", i));
            try {
                primeException();
                accountService.create(accountCreator);
            } catch (KapuaException ex) {
                verifyException(ex);
                break;
            }
        }
    }

    @Given("^I create (\\d+) accounts with organization name \"(.*)\"$")
    public void createANumberOfChildrenForAccountWithOrganizationName(int num, String name)
            throws Exception {

        for (int i = 0; i < num; i++) {
            accountCreator = prepareRegularAccountCreator(rootScopeId, "tmp_acc_" + String.format("%d", i));
            accountCreator.setOrganizationName(name);
            try {
                primeException();
                accountService.create(accountCreator);
            } catch (KapuaException ex) {
                verifyException(ex);
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
            primeException();
            account = accountService.create(accountCreator);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I create an account with a null name$")
    public void createAccountWithNullName()
            throws Exception {

        accountCreator = prepareRegularAccountCreator(rootScopeId, null);
        try {
            primeException();
            account = accountService.create(accountCreator);
        } catch (KapuaIllegalNullArgumentException ex) {
            verifyException(ex);
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
    public void updateAccount()
            throws Exception {

        try {
            primeException();
            accountService.update(account);
        } catch (KapuaEntityNotFoundException ex) {
            verifyException(ex);
        }
    }

    @When("^I change the account \"(.*)\" name to \"(.*)\"$")
    public void changeAccountName(String accName, String name)
            throws Exception {

        Account tmpAcc = accountService.findByName(accName);
        tmpAcc.setName(name);

        try {
            primeException();
            accountService.update(tmpAcc);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I change the parent path for account \"(.*)\"$")
    public void changeParentPathForAccount(String name)
            throws Exception {

        Account tmpAcc = accountService.findByName(name);
        String modParentPath = tmpAcc.getParentAccountPath() + "/mod";
        tmpAcc.setParentAccountPath(modParentPath);

        try {
            primeException();
            accountService.update(tmpAcc);
        } catch (KapuaAccountException ex) {
            verifyException(ex);
        }
    }

    @When("^I try to change the account \"(.*)\" scope Id to (\\d+)$")
    public void changeAccountScopeId(String name, int newScopeId)
            throws Exception {

        AccountImpl tmpAcc = (AccountImpl) accountService.findByName(name);
        tmpAcc.setScopeId(new KapuaEid(BigInteger.valueOf(newScopeId)));

        try {
            primeException();
            accountService.update(tmpAcc);
        } catch (KapuaException ex) {
            verifyException(ex);
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
            throws Exception {
        String adminUserName = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_USERNAME);
        Account tmpAcc = accountService.findByName(adminUserName);

        assertNotNull(tmpAcc);
        assertNotNull(tmpAcc.getId());

        try {
            primeException();
            accountService.delete(rootScopeId, tmpAcc.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete a random account$")
    public void deleteRandomAccount()
            throws Exception {

        try {
            primeException();
            accountService.delete(rootScopeId, new KapuaEid(IdGenerator.generate()));
        } catch (KapuaException ex) {
            verifyException(ex);
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
        account = accountService.find(rootScopeId, new KapuaEid(IdGenerator.generate()));
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
            throws Exception {
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
            primeException();
            accountService.setConfigValues(account.getId(), account.getScopeId(), valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
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
        assertTrue("An exception was expected but it was not raised!", exceptionCaught);
    }

    @Then("^The account does not exist$")
    public void tryToFindInexistentAccount() {
        assertNull(account);
    }

    @Then("^The System account exists$")
    public void findSystemAccount()
            throws KapuaException {
        String adminUserName = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_USERNAME);
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
    public void checkIntegerReturnValue(int val) {
        assertEquals(Integer.valueOf(val), intVal);
    }

    @Then("^Account service metadata is available$")
    public void checkAccountServiceMetadataExistance() {
        assertNotNull("Account settings not configured.", KapuaAccountSetting.getInstance());
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

    private void primeException() {
        exceptionCaught = false;
    }

    // Check the exception that was caught. In case the exception was expected the type and message is shown in the cucumber logs.
    // Otherwise the exception is rethrown failing the test and dumping the stack trace to help resolving problems.
    private void verifyException(Exception ex)
            throws Exception {

        if (!exceptionExpected ||
                (!exceptionName.isEmpty() && !ex.getClass().toGenericString().contains(exceptionName)) ||
                (!exceptionMessage.isEmpty() && !exceptionMessage.trim().contentEquals("*") && !ex.getMessage().contains(exceptionMessage))) {
            scenario.write("An unexpected exception was raised!");
            throw (ex);
        }

        scenario.write("Exception raised as expected: " + ex.getClass().getCanonicalName() + ", " + ex.getMessage());
        exceptionCaught = true;
    }

    // *****************
    // * Inner Classes *
    // *****************

    // Custom String tuple class for name/value pairs as given in the cucumber feature file
    static public class StringTuple {

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
