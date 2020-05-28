/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

import org.eclipse.kapua.qa.common.cucumber.CucumberProperty;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:features/broker/DeviceBrokerIpConfigFileI9n.feature"},
        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.qa.integration.steps",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.service.tag.steps",
                "org.eclipse.kapua.service.device.registry.steps"
               },
        plugin = {"pretty",
                  "html:target/cucumber/DeviceBrokerIpConfigFileI9n",
                  "json:target/DeviceBrokerIpConfigFileI9n_cucumber.json"
                 },
        strict = true,
        monochrome = true )
@CucumberProperty(key="kapua.config.url", value="broker.setting/kapua-broker-setting-1.properties")
public class RunDeviceBrokerIpConfigFileI9nTest {}
