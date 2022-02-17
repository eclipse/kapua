/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.common;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.Duration;
import java.util.Date;

@ScenarioScoped
public class BasicSteps extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(BasicSteps.class);

    private static final double WAIT_MULTIPLIER = Double.parseDouble(System.getProperty("org.eclipse.kapua.qa.waitMultiplier", "1.0"));

    private static final String LAST_ACCOUNT_ID = "LastAccountId";
    private static final String LAST_USER_ID = "LastUserId";
    private static final String EXCEPTION_NAME = "ExceptionName";
    private static final String EXCEPTION_CAUGHT = "ExceptionCaught";
    private static final String ASSERT_ERROR_NAME = "AssertErrorName";
    private static final String ASSERT_ERROR_CAUGHT = "AssertErrorCaught";

    /**
     * Scenario scoped step data.
     */
    private StepData stepData;

    @Inject
    public BasicSteps(StepData stepData) {
        this.stepData = stepData;
    }

    @Before
    public void checkWaitMultipier() {
        if (WAIT_MULTIPLIER != 1.0d) {
            logger.info("Wait multiplier active: {}", WAIT_MULTIPLIER);
        }
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
