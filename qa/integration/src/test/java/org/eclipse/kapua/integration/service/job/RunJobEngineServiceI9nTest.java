/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.integration.service.job;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.qa.common.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = {
                "classpath:features/job/JobEngineServiceStartOfflineDeviceI9n.feature",
                "classpath:features/job/JobEngineServiceStartOnlineDeviceI9n.feature",
                "classpath:features/job/JobEngineServiceRestartOnlineDeviceI9n.feature",
                "classpath:features/job/JobEngineServiceRestartOfflineDeviceI9n.feature"
        },
        glue = { "org.eclipse.kapua.service.job.steps",
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.device.registry.steps",
        },
        plugin = { "pretty",
                "html:target/cucumber",
                "json:target/cucumber.json" },
        strict = true,
        monochrome = true)
public class RunJobEngineServiceI9nTest {
}
