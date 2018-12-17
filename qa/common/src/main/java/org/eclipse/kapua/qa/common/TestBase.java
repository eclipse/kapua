/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.common;

import cucumber.api.Scenario;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.junit.Assert;

public class TestBase extends Assert {

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

    /**
     * Commonly used constants
     */
    protected static final KapuaEid SYS_SCOPE_ID = new KapuaEid(BigInteger.ONE);
    protected static final KapuaEid SYS_USER_ID = new KapuaEid(BigInteger.ONE);
    protected static final int DEFAULT_SCOPE_ID = 42;
    protected static final KapuaEid DEFAULT_ID = new KapuaEid(BigInteger.valueOf(DEFAULT_SCOPE_ID));

    public TestBase() {

        testType = System.getProperty("test.type");
        if (testType != null) {
            testType = testType.trim().toLowerCase();
        } else {
            testType = "";
        }
    }

    public KapuaId getKapuaId() {
        return new KapuaEid(BigInteger.valueOf(random.nextLong()));
    }

    public KapuaId getKapuaId(int i) {
        return new KapuaEid(BigInteger.valueOf(i));
    }

    public KapuaId getCurrentScopeId() {

        Account tmpAccount = (Account) stepData.get("LastAccount");
        KapuaId tmpId = (KapuaId) stepData.get("CurrentScopeId");

        if (tmpAccount != null) {
            return tmpAccount.getId();
        } else {
            return tmpId;
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
        String exceptionName = stepData.contains("ExceptionName") ? ((String)stepData.get("ExceptionName")).trim() : "";
        String exceptionMessage = stepData.contains("ExceptionMessage") ? ((String)stepData.get("ExceptionMessage")).trim() : "";

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

    public Date parseDateString(String date) {
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        Date expDate = null;
        Instant now = Instant.now();

        if (date == null) {
            return null;
        }
        // Special keywords for date
        switch (date.trim().toLowerCase()) {
            case "yesterday":
                expDate = Date.from(now.minus(Duration.ofDays(1)));
                break;
            case "today":
                expDate = Date.from(now);
                break;
            case "tomorrow":
                expDate = Date.from(now.plus(Duration.ofDays(1)));
                break;
            case "null":
                break;
        }

        // Not one of the special cases. Just parse the date.
        try {
            expDate = df.parse(date.trim().toLowerCase());
        } catch (ParseException | NullPointerException e) {
            // skip, leave date null
        }

        return expDate;
    }
}
