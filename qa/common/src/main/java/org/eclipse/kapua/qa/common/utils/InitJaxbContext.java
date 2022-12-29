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
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ScenarioScoped
public class InitJaxbContext {

    private static final Logger logger = LoggerFactory.getLogger(InitJaxbContext.class);

    @Given("^Init Jaxb Context$")
    public void initJAXBContext() throws KapuaException {
        logger.info("Initializing Test JAXB context...");
        XmlUtil.setContextProvider(new TestJAXBContextProvider());
//        final HashSet<ClassProvider> providers = new HashSet<>();
//        providers.add(new XmlSerializableClassesProvider(
//                ConfigurationPrinter
//                        .create()
//                        .withLogger(LoggerFactory.getLogger(this.getClass()))
//                        .withLogLevel(ConfigurationPrinter.LogLevel.INFO)
//                        .withTitle("Kapua Locator Configuration")
//                        .addParameter("Resource Name", "tests configuration"),
//                Arrays.asList("org.eclipse.kapua"),
//                Collections.emptyList()));
//        XmlUtil.setContextProvider(new JAXBContextProviderImpl(providers));
        logger.info("Initializing Test JAXB context... DONE");
    }
}
