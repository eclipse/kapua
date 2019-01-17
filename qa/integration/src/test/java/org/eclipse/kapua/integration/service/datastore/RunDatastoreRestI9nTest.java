/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.integration.service.datastore;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.test.cucumber.CucumberProperty;
import org.eclipse.kapua.test.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = "classpath:features/datastore/Datastore.feature",
        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.rest.steps",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.service.device.steps",
                "org.eclipse.kapua.service.datastore.steps",
                "org.eclipse.kapua.service.device.registry.steps" },
        plugin = {"pretty",
                "html:target/cucumber/DatastoreRestI9n",
                "json:target/DatastoreRestI9n_cucumber.json" },
        strict = true,
        monochrome = true)
@CucumberProperty(key="datastore.client.class", value="org.eclipse.kapua.service.datastore.client.rest.RestDatastoreClient")
@CucumberProperty(key="broker.ip", value="localhost")
@CucumberProperty(key="kapua.config.url", value="")
@CucumberProperty(key="org.eclipse.kapua.qa.datastore.extraStartupDelay", value="25")
@CucumberProperty(key="org.eclipse.kapua.qa.broker.extraStartupDelay", value="23")
@CucumberProperty(key="commons.settings.hotswap", value="true")
@CucumberProperty(key="commons.db.schema", value="kapuadb")
public class RunDatastoreRestI9nTest {
}
