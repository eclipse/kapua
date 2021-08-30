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

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.qa.common.QaTestJAXBContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ScenarioScoped
public class InitJaxbContext {

    private static final Logger LOG = LoggerFactory.getLogger(InitJaxbContext.class);

    @Given("^Init Jaxb Context$")
    public void initJAXBContext() throws KapuaException {
        LOG.info("Initializing Test JAXB context...");
        XmlUtil.setContextProvider(new QaTestJAXBContextProvider());
        LOG.info("Initializing Test JAXB context... DONE");
    }
}
