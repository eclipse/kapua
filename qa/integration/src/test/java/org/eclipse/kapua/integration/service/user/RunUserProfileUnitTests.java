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
package org.eclipse.kapua.integration.service.user;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = { "classpath:features/user/UserProfileUnitTests.feature"
    },
    glue = {"org.eclipse.kapua.service.security.test",
        "org.eclipse.kapua.service.authorization.steps",
        "org.eclipse.kapua.service.authentication.steps",
        "org.eclipse.kapua.service.user.steps",
        "org.eclipse.kapua.service.account.steps",
        "org.eclipse.kapua.qa.common"
    },
    plugin = {"pretty",
        "html:target/cucumber/UserProfile",
        "json:target/UserProfile_cucumber.json"
    },
    monochrome = true)
public class RunUserProfileUnitTests {
}
