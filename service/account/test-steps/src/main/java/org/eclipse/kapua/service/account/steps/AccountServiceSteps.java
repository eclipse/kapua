/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.account.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.commons.model.id.IdGenerator;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.qa.common.cucumber.CucAccount;
import org.eclipse.kapua.qa.common.cucumber.CucConfig;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.account.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Implementation of Gherkin steps used in AccountService.feature scenarios.
 * <p>
 * MockedLocator is used for Location Service. Mockito is used to mock other
 * services that the Account services dependent on. Dependent services are:
 * - Authorization Service
 */
@ScenarioScoped
public class AccountServiceSteps extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceSteps.class);

    // Account creator object used for creating new accounts.
    private AccountService accountService;
    private AccountFactory accountFactory;

    // Default constructor
    @Inject
    public AccountServiceSteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
        this.database = dbHelper;
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

    // Setup and tear-down steps

    @Before
    public void beforeScenario(Scenario scenario) {

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();
        accountFactory = locator.getFactory(AccountFactory.class);
        accountService = locator.getService(AccountService.class);

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
            LOGGER.info("Logging out in cleanup");
            if (isIntegrationTest()) {
                database.deleteAll();
                SecurityUtils.getSubject().logout();
            } else {
                database.dropAll();
                database.close();
            }
            KapuaSecurityUtils.clearSession();
        } catch (Exception e) {
            LOGGER.error("Failed to log out in @After", e);
        }
    }

    // The Cucumber test steps

    @Given("^An account creator with the name \"(.*)\"$")
    public void prepareTestAccountCreatorWithName(String name) {

        AccountCreator accountCreator = prepareRegularAccountCreator(SYS_SCOPE_ID, name);
        stepData.put("AccountCreator", accountCreator);
    }

    @Given("^Account$")
    public void givenAccount(List<CucAccount> accountList) throws Exception {

        CucAccount cucAccount = accountList.get(0);
        // If accountId is not set in account list, use last created Account for scope id
        if (cucAccount.getScopeId() == null) {
            cucAccount.setScopeId(((Account) stepData.get("LastAccount")).getId().getId());
        }

        Account tmpAccount = createAccount(cucAccount);
        stepData.put("LastAccount", tmpAccount);
        if (tmpAccount != null) {
            stepData.put("LastAccountId", tmpAccount.getId());
        }
    }

    @When("^I create a generic account with name \"(.*)\"$")
    public void createGenericAccount(String name)
            throws Exception {

        AccountCreator accountCreator = prepareRegularAccountCreator(SYS_SCOPE_ID, name);
        stepData.put("AccountCreator", accountCreator);
        stepData.remove("LastAccount");
        stepData.remove("LastAccountId");
        try {
            primeException();
            Account account = accountService.create(accountCreator);
            stepData.put("LastAccount", account);
            stepData.put("LastAccountId", account.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^An existing account that expires on \"(.*)\" with the name \"(.*)\"$")
    public void createTestAccountWithName(String expirationDateStr, String name)
            throws Exception {

        Date expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse(expirationDateStr);
        AccountCreator accountCreator = prepareRegularAccountCreator(SYS_SCOPE_ID, name);
        accountCreator.setExpirationDate(expirationDate);
        stepData.put("AccountCreator", accountCreator);
        stepData.remove("LastAccount");
        stepData.remove("LastAccountId");
        try {
            primeException();
            Account account = accountService.create(accountCreator);
            stepData.put("LastAccount", account);
            stepData.put("LastAccountId", account.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I create (\\d+) childs for account with Id (\\d+)$")
    public void createANumberOfAccounts(int num, int parentId)
            throws Exception {

        for (int i = 0; i < num; i++) {
            AccountCreator accountCreator = prepareRegularAccountCreator(new KapuaEid(BigInteger.valueOf(parentId)), "tmp_acc_" + String.format("%d", i));
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
            AccountCreator accountCreator = prepareRegularAccountCreator(tmpAcc.getId(), "tmp_acc_" + String.format("%d", i));
            try {
                primeException();
                accountService.create(accountCreator);
            } catch (KapuaException ex) {
                verifyException(ex);
                break;
            }
        }
    }

    @Given("^I create (\\d+) childs for account with expiration date \"(.*)\" and name \"(.*)\"$")
    public void createANumberOfChildrenForAccountWithName(int num, String expirationDateStr, String name)
            throws Exception {

        Account tmpAcc = accountService.findByName(name);
        for (int i = 0; i < num; i++) {
            Date expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse(expirationDateStr);
            AccountCreator accountCreator = prepareRegularAccountCreator(tmpAcc.getId(), "tmp_acc_" + String.format("%d", i));
            accountCreator.setExpirationDate(expirationDate);
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
            AccountCreator accountCreator = prepareRegularAccountCreator(SYS_SCOPE_ID, "tmp_acc_" + String.format("%d", i));
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

    @When("^I create an account with a null name$")
    public void createAccountWithNullName()
            throws Exception {

        AccountCreator accountCreator = prepareRegularAccountCreator(SYS_SCOPE_ID, null);
        try {
            primeException();
            accountService.create(accountCreator);
        } catch (KapuaIllegalNullArgumentException ex) {
            verifyException(ex);
        }
    }

    @When("^I modify the account \"(.*)\"$")
    public void changeAccountDetails(String name)
            throws Exception {

        try {
            primeException();

            Account account = accountService.findByName(name);
            Organization tmpOrg = account.getOrganization();

            // Change an organization detail
            tmpOrg.setName(tmpOrg.getName() + "_xx");
            tmpOrg.setCity(tmpOrg.getCity() + "_xx");
            account.setOrganization(tmpOrg);
            stepData.put("LastAccount", account);

            accountService.update(account);
        } catch (KapuaIllegalNullArgumentException ex) {
            verifyException(ex);
        }
    }

    @When("^I modify the current account$")
    public void updateAccount()
            throws Exception {

        Account account = (Account) stepData.get("LastAccount");

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

    @When("^I change the expiration date of the account \"(.*)\" to \"(.*)\"$")
    public void changeAccountExpirationDate(String accName, String expirationDateStr)
            throws Exception {

        Account tmpAcc = accountService.findByName(accName);
        Date expirationDate;
        if (expirationDateStr.equals("never")) {
            expirationDate = null;
        } else {
            expirationDate = new SimpleDateFormat("yyyy-DD-mm").parse(expirationDateStr);
        }
        tmpAcc.setExpirationDate(expirationDate);
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
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

//    @When("^I try to change the account \"(.*)\" scope Id to (\\d+)$")
//    public void changeAccountScopeId(String name, int newScopeId)
//            throws Exception {
//
//        AccountImpl tmpAcc = (AccountImpl) accountService.findByName(name);
//        tmpAcc.setScopeId(new KapuaEid(BigInteger.valueOf(newScopeId)));
//
//        try {
//            primeException();
//            accountService.update(tmpAcc);
//        } catch (KapuaException ex) {
//            verifyException(ex);
//        }
//    }

    @When("^I select account \"(.*)\"$")
    public void selectAccount(String accountName) throws KapuaException {
        Account tmpAccount;
        tmpAccount = accountService.findByName(accountName);
        if (tmpAccount != null) {
            stepData.put("LastAccount", tmpAccount);
        } else {
            stepData.remove("LastAccount");
        }
    }

    @When("I change the current account expiration date to \"(.+)\"")
    public void changeCurrentAccountExpirationDate(String newExpiration) throws Exception {

        Account currAcc = (Account) stepData.get("LastAccount");
        Date newDate = parseDateString(newExpiration);

        try {
            primeException();
            currAcc.setExpirationDate(newDate);
            Account tmpAcc = accountService.update(currAcc);
            stepData.put("LastAccount", tmpAcc);
        } catch (KapuaException e) {
            verifyException(e);
        }
    }

    @When("^I delete account \"(.*)\"$")
    public void deleteAccountWithName(String name)
            throws Exception {

        try {
            primeException();
            Account tmpAcc = accountService.findByName(name);
            accountService.delete(tmpAcc.getScopeId(), tmpAcc.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
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
            accountService.delete(SYS_SCOPE_ID, tmpAcc.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete a random account$")
    public void deleteRandomAccount()
            throws Exception {

        try {
            primeException();
            accountService.delete(SYS_SCOPE_ID, new KapuaEid(IdGenerator.generate()));
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the account with name \"(.*)\"$")
    public void findAccountByName(String name)
            throws Exception {

        try {
            primeException();
            stepData.remove("LastAccount");
            Account account = accountService.findByName(name);
            stepData.put("LastAccount", account);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the account with the remembered account Id$")
    public void findAccountByStoredId()
            throws Exception {

        try {
            primeException();
            stepData.remove("LastAccount");
            KapuaId accountId = (KapuaId) stepData.get("LastAccountId");
            Account account = accountService.find(accountId);
            stepData.put("LastAccount", account);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the account with the remembered parent and account Ids$")
    public void findAccountByStoredScopeAndAccountIds()
            throws Exception {

        try {
            primeException();
            stepData.remove("LastAccount");
            KapuaId accountId = (KapuaId) stepData.get("LastAccountId");
            Account account = accountService.find(SYS_SCOPE_ID, accountId);
            stepData.put("LastAccount", account);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for a random account Id$")
    public void findRandomAccountId()
            throws Exception {

        try {
            primeException();
            stepData.remove("LastAccount");
            Account account = accountService.find(SYS_SCOPE_ID, new KapuaEid(IdGenerator.generate()));
            stepData.put("LastAccount", account);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I set the following parameters$")
    public void setAccountParameters(List<StringTuple> paramList)
            throws Exception {

        Account account = (Account) stepData.get("LastAccount");
        Properties accProps = account.getEntityProperties();

        for (StringTuple param : paramList) {
            accProps.setProperty(param.getName(), param.getValue());
        }
        account.setEntityProperties(accProps);

        try {
            primeException();
            account = accountService.update(account);
            stepData.put("LastAccount", account);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
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

        Account account = (Account) stepData.get("LastAccount");

        try {
            primeException();
            accountService.setConfigValues(account.getId(), account.getScopeId(), valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I add the unknown config item \"(.*)\" with value (\\d+)$")
    public void addUnknownIntegerConfigurationValue(String name, int value)
            throws Exception {

        Account account = (Account) stepData.get("LastAccount");
        try {
            primeException();
            Map<String, Object> valuesRead = accountService.getConfigValues(account.getId());
            valuesRead.put(name, value);
            accountService.setConfigValues(account.getId(), account.getScopeId(), valuesRead);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for all accounts that have the system account as parent$")
    public void queryForNumberOfTopLevelAccounts()
            throws Exception {

        AccountQuery query = accountFactory.newQuery(SYS_SCOPE_ID);
        stepData.remove("IntValue");
        try {
            primeException();
            AccountListResult accList = accountService.query(query);
            stepData.put("IntValue", accList.getSize());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I set the expiration date to (.*)$")
    public void setExpirationDate(String expirationDateStr)
            throws Exception {

        Account account = (Account) stepData.get("LastAccount");
        stepData.remove("LastAccount");
        try {
            primeException();
            Date expirationDate = new SimpleDateFormat("yyyy-MM-dd").parse(expirationDateStr);
            account.setExpirationDate(expirationDate);
            account = accountService.update(account);
            stepData.put("LastAccount", account);
        } catch (KapuaException|ParseException ex) {
            verifyException(ex);
        }
    }

    @Then("^The account matches the creator settings$")
    public void checkCreatedAccountDefaults() {

        Account account = (Account) stepData.get("LastAccount");
        AccountCreator accountCreator = (AccountCreator) stepData.get("AccountCreator");

        assertNotNull(account);
        assertNotNull(account.getId());
        assertNotNull(account.getId().getId());
        assertTrue(account.getOptlock() >= 0);
        assertEquals(SYS_SCOPE_ID, account.getScopeId());
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

        Account account = (Account) stepData.get("LastAccount");
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

    @Then("^The account does not exist$")
    public void tryToFindInexistentAccount() {

        Account account = (Account) stepData.get("LastAccount");

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

        Account account = (Account) stepData.get("LastAccount");
        Properties accProps = account.getEntityProperties();

        for (StringTuple param : paramList) {
            assertEquals(param.getValue(), accProps.getProperty(param.getName()));
        }
    }

    @Then("^The account has metadata$")
    public void checkMetadataExistence()
            throws KapuaException {

        KapuaId accountId = (KapuaId) stepData.get("LastAccountId");
        KapuaTocd metaData = accountService.getConfigMetadata(accountId);

        assertNotNull(metaData);
    }

    @Then("^The default configuration for the account is set$")
    public void checkDefaultAccountConfiguration()
            throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        Map<String, Object> valuesRead = accountService.getConfigValues(account.getId());

        assertTrue(valuesRead.containsKey("maxNumberChildEntities"));
        assertEquals(0, valuesRead.get("maxNumberChildEntities"));
    }

    @Then("^The config item \"(.*)\" is set to \"(.*)\"$")
    public void checkConfigValue(String name, String value)
            throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        Map<String, Object> valuesRead = accountService.getConfigValues(account.getId());

        assertTrue(valuesRead.containsKey(name));
        assertEquals(value, valuesRead.get(name).toString());
    }

    @Then("^The config item \"(.*)\" is missing$")
    public void checkMissingConfigItem(String name)
            throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        Map<String, Object> valuesRead = accountService.getConfigValues(account.getId());

        assertFalse(valuesRead.containsKey(name));
    }

    @Then("^The returned value is (\\d+)$")
    public void checkIntegerReturnValue(int val) {

        int intVal = (int) stepData.get("IntValue");

        assertEquals(val, intVal);
    }

    @When("^I configure account service$")
    public void setAccountServiceConfig(List<CucConfig> cucConfigs)
            throws Exception {

        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId;
        KapuaId scopeId;

        for (CucConfig config : cucConfigs) {
            config.addConfigToMap(valueMap);
        }

        primeException();
        try {
            Account tmpAccount = (Account) stepData.get("LastAccount");
            if (tmpAccount != null) {
                accId = tmpAccount.getId();
                scopeId = SYS_SCOPE_ID;
            } else {
                accId = SYS_SCOPE_ID;
                scopeId = SYS_SCOPE_ID;
            }
            accountService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    // *******************
    // * Private Helpers *
    // *******************

    /**
     * Create a user creator object. The creator is pre-filled with default data.
     *
     * @param parentId Id of the parent account
     * @param name     The name of the account
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

    /**
     * Create account in privileged mode as kapua-sys user.
     * Account is created in scope specified by scopeId in cucAccount parameter.
     * This is not accountId, but account under which it is created. AccountId itself
     * is created automatically.
     *
     * @param cucAccount basic data about account
     * @return Kapua Account object
     */
    private Account createAccount(CucAccount cucAccount) throws Exception {
        List<Account> accountList = new ArrayList<>();
        KapuaSecurityUtils.doPrivileged(() -> {
            primeException();
            try {
                Account account = accountService.create(accountCreatorCreator(cucAccount.getName(),
                        cucAccount.getScopeId(), cucAccount.getExpirationDate()));
                accountList.add(account);
            } catch (KapuaException ke) {
                verifyException(ke);
            }

            return null;
        });

        return accountList.size() == 1 ? accountList.get(0) : null;
    }

    /**
     * Create account creator.
     *
     * @param name    account name
     * @param scopeId acount scope id
     * @return
     */
    private AccountCreator accountCreatorCreator(String name, BigInteger scopeId, Date expiration) {
        AccountCreator accountCreator;

        accountCreator = accountFactory.newCreator(new KapuaEid(scopeId), name);
        if (expiration != null) {
            accountCreator.setExpirationDate(expiration);
        }
        accountCreator.setOrganizationName("ACME Inc.");
        accountCreator.setOrganizationEmail("some@one.com");

        return accountCreator;
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
