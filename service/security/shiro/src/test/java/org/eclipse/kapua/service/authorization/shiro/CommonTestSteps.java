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
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.shiro;

import java.math.BigInteger;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.test.KapuaTest;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;

// Implementation of Gherkin steps used in various integration test scenarios.
@ScenarioScoped
public class CommonTestSteps extends KapuaTest {

    // Scenario scoped common test data
    CommonTestData commonData;

    @Inject
    public CommonTestSteps(CommonTestData commonData) {
        this.commonData = commonData;
    }

    // Database setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario)
            throws KapuaException {
        commonData.clearData();
        commonData.scenario = scenario;
    }

    // Cucumber test steps

    @Given("^I expect the exception \"(.+)\" with the text \"(.+)\"$")
    public void setExpectedExceptionDetails(String name, String text) {
        commonData.exceptionExpected = true;
        commonData.exceptionName = name;
        commonData.exceptionMessage = text;
    }

    @Given("^A scope with ID (\\d+)$")
    public void setScopeId(Integer scope) {
        commonData.scopeId = new KapuaEid(BigInteger.valueOf(scope));
        assertNotNull(commonData.scopeId);
    }

    @Given("^A null scope$")
    public void setNullScopeId() {
        commonData.scopeId = null;
    }

    @Then("^An exception was thrown$")
    public void exceptionCaught() {
        assertTrue(commonData.exceptionCaught);
    }

    @Then("^No exception was thrown$")
    public void noExceptionCaught() {
        assertFalse(commonData.exceptionCaught);
    }

    @Then("^I get (\\d+) as result$")
    public void checkCountResult(Integer num) {
        assertNotNull(num);
        assertEquals(num.longValue(), commonData.count);
    }

    @Then("^I get the string \"(.+)\" as result$")
    public void checkStringResult(String text) {
        assertEquals(text, commonData.stringValue);
    }
}
