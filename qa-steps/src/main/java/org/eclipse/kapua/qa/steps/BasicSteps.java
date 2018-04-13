/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.steps;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.service.StepData;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.Duration;

@ScenarioScoped
public class BasicSteps extends Assert {

    private static final Logger logger = LoggerFactory.getLogger(BasicSteps.class);

    private static final double WAIT_MULTIPLIER = Double.parseDouble(System.getProperty("org.eclipse.kapua.qa.waitMultiplier", "1.0"));

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

    @Given("^I expect the exception \"(.+)\" with the text \"(.+)\"$")
    public void setExpectedExceptionDetails(String name, String text) {
        stepData.put("ExceptionExpected", true);
        stepData.put("ExceptionName", name);
        stepData.put("ExceptionMessage", text);
    }

    @When("I wait (\\d+) seconds?.*")
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
        String exName = stepData.contains("ExceptionName") ? (String)stepData.get("ExceptionName") : "Unknown";
        boolean exCaught = stepData.contains("ExceptionCaught") ? (boolean) stepData.get("ExceptionCaught") : false;
        assertTrue(String.format("Exception %s was expected but was not raised.", exName), exCaught);
    }

    @Then("^No exception was thrown$")
    public void noExceptionCaught() {
        boolean exCaught = stepData.contains("ExceptionCaught") ? (boolean) stepData.get("ExceptionCaught") : false;
        assertFalse("An unexpected exception was raised!", exCaught);
    }

    @Then("^I get (\\d+)$")
    public void checkCountResult(int num) {
        assertEquals(num, (int) stepData.get("Count"));
    }

    @Then("^I get the text \"(.+)\"$")
    public void checkStringResult(String text) {
        assertEquals(text, (String) stepData.get("Text"));
    }
}
