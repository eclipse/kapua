/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.integration.service.authentication;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = { "classpath:features/authentication/UserCredentialServiceUnitTests.feature"
                   },
        glue = {"org.eclipse.kapua.service.security.test",
                "org.eclipse.kapua.service.authorization.steps",
                "org.eclipse.kapua.service.authentication.steps",
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.qa.common"
        },
        plugin = {"pretty",
                  "html:target/cucumber/AuthorizationServiceI9n",
                  "json:target/AuthorizationServiceI9n_cucumber.json"
                 },
        monochrome = true)
public class RunAuthenticationServiceI9nTest {}
