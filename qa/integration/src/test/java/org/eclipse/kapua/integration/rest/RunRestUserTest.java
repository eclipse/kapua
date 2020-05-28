/*******************************************************************************
 * Copyright (c) 2018, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.integration.rest;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/rest/user/RestUser.feature",
        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.qa.integration.steps",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.user.steps"
        },
        plugin = { "pretty",
                "html:target/cucumber/RestUser",
                "json:target/RestUser_cucumber.json"
        },
        strict = true,
        monochrome = true)

//@CucumberProperty(key="certificate.jwt.private.key", value= "certificates/key.pk8")
//@CucumberProperty(key="certificate.jwt.certificate", value= "certificates/certificate.pem")
public class RunRestUserTest {
}
