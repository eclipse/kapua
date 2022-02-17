/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.tag.test;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.qa.common.cucumber.CucumberProperty;
import org.junit.runner.RunWith;

@RunWith(CucumberWithPropertiesForTag.class)
@CucumberOptions(
        features = { "classpath:features/TagService.feature"
                   },
        glue = { "org.eclipse.kapua.service.tag.steps",
                 "org.eclipse.kapua.qa.common",
                 "org.eclipse.kapua.service.device.registry.steps"
               },
        plugin = { "pretty",
                   "html:target/cucumber",
                   "json:target/cucumber.json" },
        strict = true,
        monochrome = true)
@CucumberProperty(key="locator.class.impl", value="org.eclipse.kapua.qa.common.MockedLocator")
@CucumberProperty(key="test.type", value="unit")
@CucumberProperty(key="commons.db.schema", value="kapuadb")
@CucumberProperty(key="commons.db.schema.update", value="true")
public class RunTagUnitTest {
}
