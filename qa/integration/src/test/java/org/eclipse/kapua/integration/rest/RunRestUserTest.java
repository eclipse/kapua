/*******************************************************************************
 * Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.integration.rest;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.qa.common.cucumber.CucumberProperty;
import org.eclipse.kapua.qa.common.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
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

@CucumberProperty(key="certificate.jwt.private.key", value= "certificates/key.pk8")
@CucumberProperty(key="certificate.jwt.certificate", value= "certificates/certificate.pem")
@CucumberProperty(key="commons.db.schema", value="kapuadb")
@CucumberProperty(key="commons.db.schema.update", value="true")
@CucumberProperty(key="commons.db.connection.host", value="localhost")
@CucumberProperty(key="commons.db.connection.port", value="3306")
@CucumberProperty(key="test.h2.server", value="false")
@CucumberProperty(key="h2.bindAddress", value="127.0.0.1")
public class RunRestUserTest {
}
