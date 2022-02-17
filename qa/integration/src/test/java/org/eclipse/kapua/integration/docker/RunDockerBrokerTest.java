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
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.integration.docker;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.qa.common.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = "classpath:features/docker/broker.feature",
        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.qa.integration.steps"
        },
        plugin = {"pretty",
                "html:target/cucumber/DockerBroker",
                "json:target/DockerBroker_cucumber.json"
        },
        monochrome = true)
public class RunDockerBrokerTest {
}
