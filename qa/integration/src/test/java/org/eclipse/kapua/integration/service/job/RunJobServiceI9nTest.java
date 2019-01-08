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
import org.eclipse.kapua.test.cucumber.CucumberWithProperties;
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
