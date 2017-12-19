/*******************************************************************************
 * Copyright (c) 2016, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.user.test;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.test.cucumber.CucumberProperty;
import org.eclipse.kapua.test.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = "classpath:features/UserService.feature",
        glue = { "org.eclipse.kapua.qa.steps",
                 "org.eclipse.kapua.service.user.steps"
               },
        plugin = {"pretty",
                  "html:target/cucumber",
                  "json:target/cucumber.json"
                 },
        monochrome = true )
@CucumberProperty(key="locator.class.impl", value="org.eclipse.kapua.test.MockedLocator")
@CucumberProperty(key="test.type", value="unit")
public class RunTest {
}
