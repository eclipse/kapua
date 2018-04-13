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
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.shared;

import javax.inject.Inject;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.junit.Assert;

@ScenarioScoped
public class SharedTestSteps {

    // Currently executing scenario.
    private static Scenario scenario;

    // Scratchpad data related to exception checking
    public static boolean exceptionExpected;
    public static String exceptionName;
    public static String exceptionMessage;
    public static boolean exceptionCaught;

    // Setup and tear-down steps

    @Inject
    public SharedTestSteps() {}

    @Before
    public void beforeScenario(Scenario scenario)
            throws Exception {
        this.scenario = scenario;

        exceptionExpected = false;
        exceptionName = "";
        exceptionMessage = "";
        exceptionCaught = false;
    }

    @Given("^I expect the exception \"(.+)\" with the text \"(.+)\"$")
    public void setExpectedExceptionDetails(String name, String text) {
        exceptionExpected = true;
        exceptionName = name;
        exceptionMessage = text;
    }

    @Then("^An exception was raised$")
    public void anExceptionWasRaised() {
        Assert.assertTrue(exceptionCaught);
    }

    @Then("^There was no exception$")
    public void noExceptionWasRaised() {
        Assert.assertFalse(exceptionCaught);
    }

    // Helper functions

    public void primeException() {
        exceptionCaught = false;
    }

    // Check the exception that was caught. In case the exception was expected the type and message is shown in the cucumber logs.
    // Otherwise the exception is rethrown failing the test and dumping the stack trace to help resolving problems.
    public void verifyException(Exception ex)
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
}
