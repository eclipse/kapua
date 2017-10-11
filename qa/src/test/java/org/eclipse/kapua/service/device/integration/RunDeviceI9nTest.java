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
 *******************************************************************************/
package org.eclipse.kapua.service.device.integration;

import org.eclipse.kapua.test.cucumber.CucumberProperty;
import org.eclipse.kapua.test.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = {"classpath:features/device"
                   },
        glue = {"org.eclipse.kapua.qa.steps",
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.service.device.steps",
                "org.eclipse.kapua.service.tag.steps"
               },
        plugin = {"pretty", 
                  "html:target/cucumber/DeviceI9n",
                  "json:target/DeviceI9n_cucumber.json"
                 },
        monochrome = true )
@CucumberProperty(key="broker.ip", value="192.168.33.10")
@CucumberProperty(key="kapua.config.url", value="")
public class RunDeviceI9nTest {}
