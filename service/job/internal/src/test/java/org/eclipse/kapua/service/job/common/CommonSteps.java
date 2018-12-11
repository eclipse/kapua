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
package org.eclipse.kapua.service.job.common;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.service.job.internal.JobServiceTestSteps;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigInteger;


@ScenarioScoped
public class CommonSteps {
    private static final Logger logger = LoggerFactory.getLogger(JobServiceTestSteps.class);

    CommonData commonData;

    // Default constructor
    @Inject
    public CommonSteps(CommonData commonData) {
        this.commonData = commonData;
    }

    @Given("^I expect the exception \"(.+)\" with the text \"(.+)\"$")
    public void setExpectedExceptionDetails(String name, String text) {
        commonData.exceptionExpected = true;
        commonData.exceptionName = name;
        commonData.exceptionMessage = text;
    }

    @Given("^Scope with ID (\\d+)$")
    public void setCurrentScope(int id) {
        commonData.currentScopeId = new KapuaEid(BigInteger.valueOf(id));
    }

    @Given("^A null scope$")
    public void setNullScope() {
        commonData.currentScopeId = null;
    }

    @Given("^The root scope$")
    public void setRootScope() {
        commonData.currentScopeId = new KapuaEid(BigInteger.ONE);
    }

    @Then("^There (?:are|is) exactly (\\d+) items?$")
    public void checkNumberOfCountedItems(long cnt) {
        Assert.assertEquals(String.format("Expected %d but counted %d", cnt, commonData.itemCount), cnt, commonData.itemCount);
    }

    @Then("^No exception was thrown$")
    public void checkThatNoExceptionWasThrown() {
        Assert.assertFalse("An unexpected exception was caught", commonData.exceptionCaught);
    }

    @Then("^An exception was thrown$")
    public void checkThatAnExceptionWasThrown() {
        Assert.assertTrue("An exception was expected but was not caught", commonData.exceptionCaught);
    }
}
