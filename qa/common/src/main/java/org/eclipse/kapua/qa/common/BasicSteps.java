/*******************************************************************************
 * Copyright (c) 2017, 2021 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.common;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreElasticsearchClientSettings;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreElasticsearchClientSettingsKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import javax.inject.Inject;
import java.time.Duration;
import java.util.Date;

@Singleton
public class BasicSteps extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(BasicSteps.class);

    private static final double WAIT_MULTIPLIER = Double.parseDouble(System.getProperty("org.eclipse.kapua.qa.waitMultiplier", "1.0"));

    private static final String KAPUA_CUSTOM_PROPERTIES = "@KapuaProperties(\"";
    private static final int KAPUA_CUSTOM_PROPERTIES_SIZE = KAPUA_CUSTOM_PROPERTIES.length();

    private static final String LAST_ACCOUNT_ID = "LastAccountId";
    private static final String LAST_USER_ID = "LastUserId";
    private static final String EXCEPTION_NAME = "ExceptionName";
    private static final String EXCEPTION_CAUGHT = "ExceptionCaught";
    private static final String ASSERT_ERROR_NAME = "AssertErrorName";
    private static final String ASSERT_ERROR_CAUGHT = "AssertErrorCaught";

    private DBHelper database;

    @Inject
    public BasicSteps(StepData stepData, DBHelper database) {
        super(stepData);
        this.database = database;
    }

    @Before(value="@setup and @env_docker", order=0)
    public void initParametersDocker(Scenario scenario) {
        logger.info("=====> Init parameters for docker environment...");
        setProperties(scenario, "kapuadb", "true", "localhost", "3306", "DEFAULT", "org.h2.Driver",
            "jdbc:h2:tcp", "certificates/jwt/test.key", "certificates/jwt/test.cert", "localhost", "MODE=MySQL");
        logger.info("=====> Init parameters for docker environment... DONE");
    }

    @Before(value="@setup and (@env_embedded_minimal or @env_none)", order=0)
    public void initParametersEmbedded(Scenario scenario) {
        logger.info("=====> Init parameters for embedded environment...");
        setProperties(scenario, "kapuadb", "true", "", "", "H2", "org.h2.Driver", "jdbc:h2:mem:",
            "certificates/jwt/test.key", "certificates/jwt/test.cert", "localhost", null);
        logger.info("=====> Init parameters for embedded environment... DONE");
    }

    private void setProperties(Scenario scenario, String schema, String updateSchema,
            String dbHost, String dbPort, String dbConnResolver, String dbDriver, String jdbcConnection,
            String jwtKey, String jwtCertificate, String brokerIp, String additionalOptions) {
        SystemSetting.resetInstance();
        System.setProperty(SystemSettingKey.DB_SCHEMA.key(), schema);
        System.setProperty(SystemSettingKey.DB_SCHEMA_UPDATE.key(), updateSchema);
        System.setProperty(SystemSettingKey.DB_CONNECTION_HOST.key(), dbHost);
        System.setProperty(SystemSettingKey.DB_CONNECTION_PORT.key(), dbPort);
        System.setProperty(SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER.key(), dbConnResolver);
        System.setProperty(SystemSettingKey.DB_JDBC_DRIVER.key(), dbDriver);
        System.setProperty(SystemSettingKey.DB_CONNECTION_SCHEME.key(), jdbcConnection);
        if (additionalOptions!=null) {
            System.setProperty(SystemSettingKey.DB_CONNECTION_ADDITIONAL_OPTIONS.key(), additionalOptions);
        }
        System.setProperty("certificate.jwt.private.key", jwtKey);
        System.setProperty("certificate.jwt.certificate", jwtCertificate);
        System.setProperty("broker.ip", brokerIp);
        setSpecificProperties(scenario);
        logParameters();
        KapuaLocator.clearInstance();
    }

    private void logParameters() {
        logger.info("===============================================");
        logger.info("{}: {}", SystemSettingKey.DB_SCHEMA.key(), SystemSetting.getInstance().getString(SystemSettingKey.DB_SCHEMA));
        logger.info("{}: {}", SystemSettingKey.DB_SCHEMA_UPDATE.key(), SystemSetting.getInstance().getString(SystemSettingKey.DB_SCHEMA_UPDATE));
        logger.info("{}: {}", SystemSettingKey.DB_CONNECTION_HOST.key(), SystemSetting.getInstance().getString(SystemSettingKey.DB_CONNECTION_HOST));
        logger.info("{}: {}", SystemSettingKey.DB_CONNECTION_PORT.key(), SystemSetting.getInstance().getString(SystemSettingKey.DB_CONNECTION_PORT));
        logger.info("{}: {}", SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER.key(), SystemSetting.getInstance().getString(SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER));
        logger.info("{}: {}", SystemSettingKey.DB_JDBC_DRIVER.key(), SystemSetting.getInstance().getString(SystemSettingKey.DB_JDBC_DRIVER));
        logger.info("{}: {}", SystemSettingKey.DB_CONNECTION_SCHEME.key(), SystemSetting.getInstance().getString(SystemSettingKey.DB_CONNECTION_SCHEME));
        logger.info("Rest: datastore.elasticsearch.node: {}", DatastoreElasticsearchClientSettings.getInstance().getString(DatastoreElasticsearchClientSettingsKey.NODES));
        logger.info("Rest: datastore.elasticsearch.port: {}", DatastoreElasticsearchClientSettings.getInstance().getString(DatastoreElasticsearchClientSettingsKey.PORT));
        logger.info("===============================================");
    }

    private void setSpecificProperties(Scenario scenario) {
        String[] scenarioTags = scenario.getSourceTagNames().toArray(new String[]{});
        for (String str : scenarioTags) {
            if (str.startsWith(KAPUA_CUSTOM_PROPERTIES)) {
                String properties = str.substring(KAPUA_CUSTOM_PROPERTIES_SIZE, str.lastIndexOf("\""));
                String[] splittedProperties = properties.split("\\,");
                for (String property : splittedProperties) {
                    String[] splittedProperty = property.split("=");
                    System.setProperty(splittedProperty[0], splittedProperty[1]);
                }
            }
        }
    }

    @Before
    public void checkWaitMultipier() {
        if (WAIT_MULTIPLIER != 1.0d) {
            logger.info("Wait multiplier active: {}", WAIT_MULTIPLIER);
        }
    }

    @Before(value="@env_docker and not (@setup or @teardown)", order=0)
    public void beforeScenarioDockerFull(Scenario scenario) {
        beforeCommon(scenario);
    }

    @Before(value="@env_embedded_minimal and not (@setup or @teardown)", order=0)
    public void beforeScenarioEmbeddedMinimal(Scenario scenario) {
        beforeCommon(scenario);
        beforeNoDocker();
    }

    @Before(value="@env_none and not (@setup or @teardown)", order=0)
    public void beforeScenarioNone(Scenario scenario) {
        beforeCommon(scenario);
        beforeNoDocker();
    }

    @After(value="@env_docker and not (@setup or @teardown)", order=0)
    public void afterScenarioDockerFull(Scenario scenario) {
        afterScenarioDocker(scenario);
    }

    @After(value="@env_embedded_minimal and not (@setup or @teardown)", order=0)
    public void afterScenarioEmbeddedMinimal(Scenario scenario) {
        afterScenarioNoDocker(scenario);
    }

    @After(value="@env_none and not (@setup or @teardown)", order=0)
    public void afterScenarioNone(Scenario scenario) {
        afterScenarioNoDocker(scenario);
    }

    protected void beforeCommon(Scenario scenario) {
        this.scenario = scenario;
        stepData.clear();
    }

    protected void beforeNoDocker() {
        database.setup();
        // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
        // All operations on database are performed using system user.
        // Only for unit tests. Integration tests assume that a real logon is performed.
        KapuaSession kapuaSession = new KapuaSession(null, SYS_SCOPE_ID, SYS_USER_ID);
        KapuaSecurityUtils.setSession(kapuaSession);
    }

    protected void afterScenarioDocker(Scenario scenario) {
        logger.info("Database cleanup...");
        database.deleteAll();
        logger.info("Database cleanup... DONE");
        SecurityUtils.getSubject().logout();
        KapuaSecurityUtils.clearSession();
    }

    protected void afterScenarioNoDocker(Scenario scenario) {
        logger.info("Database drop...");
        try {
            database.dropAll();
            logger.info("Database drop... DONE");
            database.close();
        } catch (Exception e) {
            logger.error("Failed execute @After", e);
        }
        KapuaSecurityUtils.clearSession();
    }

    @Given("A placeholder step")
    public void doNothing() {

        // An empty placeholder step. Just a breakpoint anchor point. Used to pause
        // test execution by placing a breakpoint into.
        Integer a = 10;
    }

    @Given("^Scope with ID (\\d+)$")
    public void setSpecificScopeId(Integer id) {

        stepData.put(LAST_ACCOUNT_ID, getKapuaId(id));
    }

    @Given("^The KAPUA-SYS scope$")
    public void setRootScope() {

        stepData.put(LAST_ACCOUNT_ID, SYS_SCOPE_ID);
    }

    @Given("^A null scope$")
    public void setNullScope() {

        stepData.put(LAST_ACCOUNT_ID, null);
    }

    @Given("^The User ID (\\d+)$")
    public void setSpecificUserId(Integer id) {

        stepData.put(LAST_USER_ID, getKapuaId(id));
    }

    @Given("^The KAPUA-SYS user$")
    public void setRootUserId() {

        stepData.put(LAST_USER_ID, SYS_USER_ID);
    }

    @Given("^A null user")
    public void setNullUser() {

        stepData.put(LAST_USER_ID, null);
    }

    @Given("^Server with host \"(.+)\" on port \"(.+)\"$")
    public void setHostPort(String host, String port) {
        stepData.put("host", host);
        stepData.put("port", port);
    }

    @Given("^I expect the exception \"(.+)\" with the text \"(.+)\"$")
    public void setExpectedExceptionDetails(String name, String text) {
        stepData.put("ExceptionExpected", true);
        stepData.put(EXCEPTION_NAME, name);
        stepData.put("ExceptionMessage", text);
    }

    @When("^I wait (\\d+) seconds?$")
    public void waitSeconds(int seconds) throws InterruptedException {
        double effectiveSeconds = ((double) seconds) * WAIT_MULTIPLIER;
        Thread.sleep(Duration.ofSeconds((long) Math.ceil(effectiveSeconds)).toMillis());
    }

    @When("(\\d+) seconds?.* passed")
    public void secondsPassed(int seconds) throws InterruptedException {
        waitSeconds(seconds);
    }

    @Then("^An exception was thrown$")
    public void exceptionCaught() {
        String exName = stepData.contains(EXCEPTION_NAME) ? (String)stepData.get(EXCEPTION_NAME) : "Unknown";
        boolean exCaught = stepData.contains(EXCEPTION_CAUGHT) ? (boolean) stepData.get(EXCEPTION_CAUGHT) : false;
        assertTrue(String.format("Exception %s was expected but was not raised.", exName), exCaught);
    }

    @Then("^No exception was thrown$")
    public void noExceptionCaught() {
        boolean exCaught = stepData.contains(EXCEPTION_CAUGHT) ? (boolean) stepData.get(EXCEPTION_CAUGHT) : false;
        assertFalse("An unexpected exception was raised!", exCaught);
    }

    @Then("^I count (\\d+)$")
    public void checkCountResult(Long num) {
        assertEquals(num, stepData.get("Count"));
    }

    @Then("^I count (\\d+) or more$")
    public void checkAsyncCountResult(Long num) {
        assertTrue((Long) stepData.get("Count") >= num);
    }

    @Then("^I get the integer (\\d+)$")
    public void checkIntResult(int num) {
        assertEquals(num, (int) stepData.get("IntValue"));
    }

    @Then("^I get the boolean \"(.+)\"$")
    public void checkBoolResult(String val) {
        assertEquals(Boolean.valueOf(val), stepData.get("BoolValue"));
    }

    @Given("^The text \"(.+)\"$")
    public void setCustomText(String text) {
        stepData.put("Text", text);
    }

    @Then("^I get the text \"(.+)\"$")
    public void checkStringResult(String text) {
        assertEquals(text, stepData.get("Text"));
    }

    @Given("^The date \"(.+)\"$")
    public void setCustomDate(String dateString) throws Exception {

        primeException();
        try {
            Date date = KapuaDateUtils.parseDate(dateString);
            stepData.put("Date", date);
        } catch(Exception ex) {
            verifyException(ex);
        }
    }

    @Given("^System property \"(.*)\" with value \"(.*)\"$")
    public void setSystemProperty(String key, String value) {
        if ("null".equalsIgnoreCase(value)) {
            System.clearProperty(key);
        } else {
            System.setProperty(key, value);
        }
    }

    @And("^I expect the exception \"([^\"]*)\"$")
    public void iExpectTheException(String name) {
        stepData.put("ExceptionExpected", true);
        stepData.put(EXCEPTION_NAME, name);
    }

    @Then("^An assertion error was thrown$")
    public void anAssertionErrorWasThrown() {
        String assertErrorName = stepData.contains(ASSERT_ERROR_NAME) ? (String) stepData.get(ASSERT_ERROR_NAME) : "Unknown";
        boolean assertErrorCaught = stepData.contains(ASSERT_ERROR_CAUGHT) ? (boolean) stepData.get(ASSERT_ERROR_CAUGHT) : false;
        assertTrue(String.format("Assert error was expected but was not raised.", assertErrorName), assertErrorCaught);
    }

    @And("^I expect the assertion error \"([^\"]*)\" with the text \"([^\"]*)\"$")
    public void iExpectTheAssertErrorWithTheText(String name, String text) {
        stepData.put("AssertErrorExpected", true);
        stepData.put(ASSERT_ERROR_NAME, name);
        stepData.put("AssertErrorMessage", text);
    }

    @And("^No assertion error was thrown$")
    public void noAssertionErrorWasThrown() {
        boolean assertErrorCaught = stepData.contains(ASSERT_ERROR_CAUGHT) ? (boolean) stepData.get(ASSERT_ERROR_CAUGHT) : false;
        assertFalse("An unexpected assert error was raised!", assertErrorCaught);
    }

    @And("^I wait for (\\d+) millisecond(?:|s) for processes to settle down$")
    public void waitingMilliseconds(int milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }
}
