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

import cucumber.api.Scenario;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;

import javax.inject.Singleton;
import java.math.BigInteger;
import java.util.Random;

@Singleton
public class CommonData {

    // Currently executing scenario.
    public Scenario scenario;

    // Scratchpad data related to exception checking
    public boolean exceptionExpected;
    public String exceptionName;
    public String exceptionMessage;
    public boolean exceptionCaught;

    // Misc scratchpad data
    public KapuaId currentScopeId;
    public KapuaId currentUserId;

    public long itemCount;

    public CommonData() {
        exceptionExpected = false;
        exceptionName = "";
        exceptionMessage = "";
        exceptionCaught = false;

        currentScopeId = null;
        currentUserId = null;

        itemCount = 0;
    }

    public void cleanup() {

        exceptionExpected = false;
        exceptionName = "";
        exceptionMessage = "";
        exceptionCaught = false;

        currentScopeId = null;
        currentUserId = null;

        itemCount = 0;
    }

    // ********************************************************************************************
    // Exception related helper functions
    // ********************************************************************************************

    // Set up the exception trap
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
            throw (ex);
        }

        scenario.write("Exception raised as expected: " + ex.getClass().getCanonicalName() + ", " + ex.getMessage());
        exceptionCaught = true;
    }

    // ********************************************************************************************
    // Miscellaneous helper functions
    // ********************************************************************************************

    // Create a random Kapua ID
    public KapuaId getRandomId() {

        long val = (new Random()).nextLong();

        val = (val > 0) ? val : -val; // Make sure the value is always positive!
        return new KapuaEid(BigInteger.valueOf(val));
    }
}
