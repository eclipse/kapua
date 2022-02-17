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

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.qa.common.cucumber.CucumberProperty;
import org.eclipse.kapua.qa.common.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = {"classpath:features/broker/DeviceData.feature"},
        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.service.tag.steps",
                "org.eclipse.kapua.service.datastore.steps",
                "org.eclipse.kapua.service.device.registry.steps"
        },
        plugin = {"pretty",
                "html:target/cucumber/DeviceDataI9n",
                "json:target/DeviceDataI9n_cucumber.json"
        },
        strict = true,
        monochrome = true)
@CucumberProperty(key = "datastore.elasticsearch.nodes", value = "127.0.0.1")
@CucumberProperty(key = "kapua.config.url", value = "")
public class RunDeviceDataI9nTest {
}
