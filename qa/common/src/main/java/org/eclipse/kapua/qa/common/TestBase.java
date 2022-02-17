/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.common;

import cucumber.api.Scenario;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.RandomUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.junit.Assert;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

public class TestBase extends Assert {

    private static final String LAST_ACCOUNT = "LastAccount";

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
    public Random random = RandomUtils.getInstance();

    /**
     * Commonly used constants
     */
    protected static final KapuaId SYS_SCOPE_ID = KapuaId.ONE;
    protected static final KapuaId SYS_USER_ID = new KapuaEid(BigInteger.ONE);
    protected static final int DEFAULT_SCOPE_ID = 42;
    protected static final KapuaId DEFAULT_ID = new KapuaEid(BigInteger.valueOf(DEFAULT_SCOPE_ID));

    public TestBase() {

        testType = System.getProperty("test.type");
        if (testType != null) {
            testType = testType.trim().toLowerCase();
        } else {
            testType = "";
        }
    }

    public KapuaId getKapuaId() {
        return new KapuaEid(BigInteger.valueOf(random.nextLong()).abs());
    }

    public KapuaId getKapuaId(int id) {
        return new KapuaEid(BigInteger.valueOf(id));
    }

    public KapuaId getKapuaId(String id) {
        return new KapuaEid(new BigInteger(id));
    }

    public KapuaId getCurrentScopeId() {

        if (stepData.contains("LastAccountId")) {
            return (KapuaId) stepData.get("LastAccountId");
        } else if (stepData.get(LAST_ACCOUNT) != null) {
            return ((Account) stepData.get(LAST_ACCOUNT)).getId();
        } else {
            return SYS_SCOPE_ID;
        }
    }

    public KapuaId getCurrentParentId() {

        if (stepData.get(LAST_ACCOUNT) != null) {
            return ((Account) stepData.get(LAST_ACCOUNT)).getScopeId();
        } else {
            return SYS_SCOPE_ID;
        }
    }

    public KapuaId getCurrentUserId() {

        if (stepData.contains("LastUserId")) {
            return (KapuaId) stepData.get("LastUserId");
        } else if (stepData.get("LastUser") != null) {
            return ((Account) stepData.get("LastUser")).getId();
        } else {
            return SYS_USER_ID;
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

        boolean exceptionExpected = stepData.contains("ExceptionExpected") && (boolean) stepData.get("ExceptionExpected");
        String exceptionName = stepData.contains("ExceptionName") ? ((String) stepData.get("ExceptionName")).trim() : "";
        String exceptionMessage = stepData.contains("ExceptionMessage") ? ((String) stepData.get("ExceptionMessage")).trim() : "";

        if (!exceptionExpected ||
                (!exceptionName.isEmpty() && !ex.getClass().toGenericString().contains(exceptionName)) ||
                (!exceptionMessage.isEmpty() && !exceptionMessage.trim().contentEquals("*") && !ex.getMessage().contains(exceptionMessage))) {
            scenario.write("An unexpected exception was raised!");
            throw (ex);
        }

        scenario.write("Exception raised as expected: " + ex.getClass().getCanonicalName() + ", " + ex.getMessage());
        stepData.put("ExceptionCaught", true);
        stepData.put("Exception", ex);
    }

    public void verifyAssertionError(AssertionError assetError)
            throws AssertionError {

        boolean assertErrorExpected = stepData.contains("AssertErrorExpected") ? (boolean) stepData.get("AssertErrorExpected") : false;
        String assertErrorName = stepData.contains("AssertErrorName") ? ((String) stepData.get("AssertErrorName")).trim() : "";
        String assertErrorMessage = stepData.contains("AssertErrorMessage") ? ((String) stepData.get("AssertErrorMessage")).trim() : "";

        if (!assertErrorExpected ||
                (!assertErrorName.isEmpty() && !assetError.getClass().toGenericString().contains(assertErrorName)) ||
                (!assertErrorMessage.isEmpty() && !assertErrorMessage.trim().contentEquals("*") && !assetError.getMessage().contains(assertErrorMessage))) {
            scenario.write("An unexpected assert error was raised!");
            throw (assetError);
        }

        scenario.write("Assert error raised as expected: " + assetError.getClass().getCanonicalName() + ", " + assetError.getMessage());
        stepData.put("AssertErrorCaught", true);
        stepData.put("AssertError", assetError);
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

    public String getRandomString() {

        return String.valueOf(random.nextInt());
    }
}
