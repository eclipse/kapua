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
package org.eclipse.kapua.service.datastore.integration;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.test.cucumber.CucumberProperty;
import org.eclipse.kapua.test.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = "classpath:features/datastore/Datastore.feature",
        glue = {"org.eclipse.kapua.qa.steps",
                "org.eclipse.kapua.service.datastore.steps",
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.service.device.steps"},
        plugin = {"pretty",
                  "html:target/cucumber/DatastoreTransportI9n",
                  "json:target/DatastoreTransportI9n_cucumber.json"},
        monochrome = true)
@CucumberProperty(key="datastore.client.class", value="org.eclipse.kapua.service.datastore.client.transport.TransportDatastoreClient")
@CucumberProperty(key="org.eclipse.kapua.qa.datastore.extraStartupDelay", value="5")
@CucumberProperty(key="broker.ip", value="192.168.33.10")
@CucumberProperty(key="kapua.config.url", value="")
@CucumberProperty(key="org.eclipse.kapua.qa.broker.extraStartupDelay", value="3")
public class RunDatastoreI9nTest {
}
