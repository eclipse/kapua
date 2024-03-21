/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.qa.common.utils;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

/**
 * This is pretty much a noop class, useful only as a basic step to initialize test scenarios configuring the locator (e.g.: annotating the scenario with
 * [at]KapuaProperties("locator.class.impl=org.eclipse.kapua.qa.common.MockedLocator") in order to use the mocked locator instead of the full one
 */
@ScenarioScoped
public class InitMockLocator {

    private static final Logger logger = LoggerFactory.getLogger(InitMockLocator.class);

    @Given("^Init Mock Locator$")
    public void initJAXBContext() throws KapuaException {
        logger.info("Initializing Mock locator...");
        KapuaLocator.getInstance();
        logger.info("Initializing Mock Locator... DONE");
    }
}
