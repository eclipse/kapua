/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.security.test;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:features/RoleServiceUnitTests.feature",
                    "classpath:features/GroupServiceUnitTests.feature",
                    "classpath:features/CredentialServiceUnitTests.feature"
        },
        glue = {"org.eclipse.kapua.service.security.test",
                "org.eclipse.kapua.service.authorization.steps",
                "org.eclipse.kapua.service.authentication.steps",
                "org.eclipse.kapua.qa.common"
        },
        plugin = {"pretty",
                "html:target/cucumber",
                "json:target/cucumber.json"},
        strict = true,
        monochrome = true)
public class RunSecurityUnitTest {
}
