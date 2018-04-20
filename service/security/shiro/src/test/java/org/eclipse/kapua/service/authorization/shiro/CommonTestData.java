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

import javax.inject.Singleton;

import cucumber.api.Scenario;
import org.eclipse.kapua.model.id.KapuaId;

@Singleton
public class CommonTestData {

    public Scenario scenario;

    // Scratchpad data related to exception checking
    public boolean exceptionExpected;
    public String exceptionName;
    public String exceptionMessage;
    public boolean exceptionCaught;

    public long count;
    public int intValue;
    public String stringValue;
    public KapuaId scopeId;

    public void clearData() {
        exceptionExpected = false;
        exceptionName = "";
        exceptionMessage = "";
        exceptionCaught = false;
        count = 0;
        intValue = 0;
        stringValue = "";
        scopeId = null;
    }

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
