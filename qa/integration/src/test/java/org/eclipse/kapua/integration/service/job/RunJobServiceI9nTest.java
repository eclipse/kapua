/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.integration.service.job;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.qa.common.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = { "classpath:features/job/JobServiceI9n.feature",
                     "classpath:features/job/JobStepServiceI9n.feature",
                     "classpath:features/job/JobTargetServiceI9n.feature",
                     "classpath:features/job/JobExecutionServiceI9n.feature"
                   },
        glue = { "org.eclipse.kapua.service.job.steps",
                 "org.eclipse.kapua.service.user.steps",
                 "org.eclipse.kapua.qa.common"
               },
        plugin = { "pretty",
                   "html:target/cucumber",
                   "json:target/cucumber.json" },
        strict = true,
        monochrome = true)
public class RunJobServiceI9nTest {
}
