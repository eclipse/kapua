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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.integration;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/DeviceServiceI9n.feature", 
        glue = { "org.eclipse.kapua.service.device.steps",
                 "org.eclipse.kapua.service.user.steps",
                 "org.eclipse.kapua.service.common.steps" }, 
        plugin = { "pretty",
                   "html:target/cucumber/DeviceServiceI9n",
                   "json:target/DeviceServiceI9n_cucumber.json" }, 
        monochrome = true)

public class RunDeviceServiceI9nTest {
}
