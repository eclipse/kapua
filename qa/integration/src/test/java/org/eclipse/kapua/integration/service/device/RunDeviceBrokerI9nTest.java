/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.integration.service.device;

import org.eclipse.kapua.qa.common.cucumber.CucumberProperty;
import org.eclipse.kapua.qa.common.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = {"classpath:features/broker/DeviceBrokerI9n.feature"},
        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.service.tag.steps",
                "org.eclipse.kapua.service.device.registry.steps"
        },
        plugin = {"pretty",
                "html:target/cucumber/DeviceBrokerI9n",
                "json:target/DeviceBrokerI9n_cucumber.json"
        },
        strict = true,
        monochrome = true )
@CucumberProperty(key="broker.ip", value="localhost")
@CucumberProperty(key="kapua.config.url", value="")
public class RunDeviceBrokerI9nTest {}

