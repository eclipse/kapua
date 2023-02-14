/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.systeminfo.test;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = { "classpath:features/SystemInfoServiceUnitTests.feature"
    },
    glue = {"org.eclipse.kapua.service.device.registry.steps",
        "org.eclipse.kapua.service.systeminfo.test",
        "org.eclipse.kapua.service.systeminfo.steps",
        "org.eclipse.kapua.qa.common"
    },
    plugin = { "pretty",
        "html:target/cucumber/SystemInfoService",
        "json:target/SystemInfoService_cucumber.json" },
    monochrome = true)
public class RunSystemInfoUnitTest {
}
