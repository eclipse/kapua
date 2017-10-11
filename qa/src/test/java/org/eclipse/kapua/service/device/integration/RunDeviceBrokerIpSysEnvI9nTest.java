/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.integration;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.test.cucumber.CucumberProperty;
import org.eclipse.kapua.test.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = {"classpath:features/broker/DeviceBrokerIpSysEnvI9n.feature"},
        glue = {"org.eclipse.kapua.qa.steps",
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.service.device.steps"
               },
        plugin = {"pretty",
                  "html:target/cucumber/DeviceBrokerIpSysEnvI9n",
                  "json:target/DeviceBrokerIpSysEnvI9n_cucumber.json"
                 },
        monochrome = true )
@CucumberProperty(key="broker.ip", value="192.168.33.10")
@CucumberProperty(key="kapua.config.url", value="")
public class RunDeviceBrokerIpSysEnvI9nTest {}
