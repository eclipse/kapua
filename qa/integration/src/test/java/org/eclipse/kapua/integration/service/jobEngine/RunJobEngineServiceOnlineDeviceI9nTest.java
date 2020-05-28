/*******************************************************************************
 * Copyright (c) 2019, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.integration.service.jobEngine;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {
                "classpath:features/jobEngine/JobEngineServiceStartOnlineDeviceI9n.feature",
                "classpath:features/jobEngine/JobEngineServiceRestartOnlineDeviceI9n.feature",
                "classpath:features/jobEngine/JobEngineServiceRestartOnlineDeviceSecondPartI9n.feature",
                "classpath:features/jobEngine/JobEngineServiceStopOnlineDeviceI9n.feature",
        },
        glue = {"org.eclipse.kapua.service.job.steps",
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.qa.integration.steps",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.device.registry.steps"
        },
        plugin = {"pretty",
                "html:target/cucumber/JobEngineOnlineDeviceI9n",
                "json:target/JobEngineOnlineDeviceI9n_cucumber.json"},
        strict = true,
        monochrome = true)
public class RunJobEngineServiceOnlineDeviceI9nTest {
}
