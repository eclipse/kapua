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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.integration.service.user;

import org.eclipse.kapua.test.cucumber.CucumberProperty;
import org.eclipse.kapua.test.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = "classpath:features/user/UserServiceI9n.feature",
        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.user.steps"
               },
        plugin = {"pretty", 
                  "html:target/cucumber/UserServiceI9n",
                  "json:target/UserServiceI9n_cucumber.json"
                 },
        strict = true,
        monochrome = true)
@CucumberProperty(key="broker.ip", value="192.168.33.10")
@CucumberProperty(key="kapua.config.url", value="")
@CucumberProperty(key="datastore.client.class", value="org.eclipse.kapua.service.datastore.client.rest.RestDatastoreClient")
@CucumberProperty(key="org.eclipse.kapua.qa.datastore.extraStartupDelay", value="5")
@CucumberProperty(key="org.eclipse.kapua.qa.broker.extraStartupDelay", value="5")
public class RunUserServiceI9nTest {}
