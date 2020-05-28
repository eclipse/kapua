/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.en.Given;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class InitJaxbContext {

    private static final Logger logger = LoggerFactory.getLogger(InitJaxbContext.class);

    @Given("^Init Jaxb Context$")
    public void initJAXBContext() throws KapuaException {
        logger.info("Initializing Test JAXB context...");
        XmlUtil.setContextProvider(new TestJAXBContextProvider());
        logger.info("Initializing Test JAXB context... DONE");
    }
}
