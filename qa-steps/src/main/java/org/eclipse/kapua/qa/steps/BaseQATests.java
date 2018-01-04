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
package org.eclipse.kapua.qa.steps;

import cucumber.api.Scenario;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.StepData;

import java.util.Random;

public class BaseQATests {

    /**
     * Common locator instance
     */
    public KapuaLocator locator;

    /**
     * Inter step data scratchpad.
     */
    public StepData stepData;

    /**
     * Common database helper
     */
    public DBHelper database;

    /**
     * Current scenario scope
     */
    public Scenario scenario;

    /**
     * Current test type
     * Either unit or integration
     */
    private String testType;

    /**
     * Random number generator
     */
    public Random random = new Random();

    public BaseQATests() {

        testType = System.getProperty("test.type");
        if (testType != null) {
            testType = testType.trim().toLowerCase();
        } else {
            testType = "";
        }
    }

    public boolean isUnitTest() {
        return testType.equals("unit");
    }

    public boolean isIntegrationTest() {
        return testType.isEmpty() || testType.equals("integration");
    }

    public void primeException() {
        stepData.put("ExceptionCaught", false);
        stepData.remove("Exception");
    }

    /**
     * Check the exception that was caught. In case the exception was expected the type and message is shown in the cucumber logs.
     * Otherwise the exception is rethrown failing the test and dumping the stack trace to help resolving problems.
     */
    public void verifyException(Exception ex)
            throws Exception {

        boolean exceptionExpected = stepData.contains("ExceptionExpected") ? (boolean)stepData.get("ExceptionExpected") : false;
        String exceptionName = stepData.contains("ExceptionName") ? (String)stepData.get("ExceptionName") : "";
        String exceptionMessage = stepData.contains("ExceptionMessage") ? (String)stepData.get("ExceptionMessage") : "";

        if (!exceptionExpected ||
                (!exceptionName.isEmpty() && !ex.getClass().toGenericString().contains(exceptionName)) ||
                (!exceptionMessage.isEmpty() && !exceptionMessage.trim().contentEquals("*") && !ex.getMessage().contains(exceptionMessage))) {
            scenario.write("An unexpected exception was raised!");
            throw(ex);
        }

        scenario.write("Exception raised as expected: " + ex.getClass().getCanonicalName() + ", " + ex.getMessage());
        stepData.put("ExceptionCaught", true);
        stepData.put("Exception", ex);
    }
}
