/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.integration.service.jobEngine.stepDefinitions;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.qa.common.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = {
                "classpath:features/jobEngine/JobEngineServiceKeystoreStepDefinitionsI9n.feature"
        },
        glue = {
                "org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.service.job.steps",
                "org.eclipse.kapua.service.user.steps",

                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.device.registry.steps",
        },
        plugin = {"pretty",
                "html:target/cucumber/JobEngineServiceKeystoreStepDefinitionsI9n",
                "json:target/JobEngineServiceKeystoreStepDefinitionsI9n_cucumber.json"},
        strict = true,
        monochrome = true)
public class RunJobEngineServiceKeystoreStepDefinitionsI9nTest {
}
