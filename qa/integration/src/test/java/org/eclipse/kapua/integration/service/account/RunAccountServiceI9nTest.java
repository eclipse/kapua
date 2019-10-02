/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.integration.service.account;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.qa.common.cucumber.CucumberProperty;
import org.eclipse.kapua.qa.common.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = {"classpath:features/account/AccountExpirationI9n.feature",
                    "classpath:features/account/FindSelfAccount.feature"},
        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.user.steps"
               },
        plugin = {"pretty", 
                  "html:target/cucumber/AccountServiceI9n",
                  "json:target/AccountServiceI9n_cucumber.json"
                 },
        strict = true,
        monochrome = true)
@CucumberProperty(key="DOCKER_HOST", value= "")
@CucumberProperty(key="DOCKER_CERT_PATH", value= "")
@CucumberProperty(key="commons.db.schema.update", value= "")
@CucumberProperty(key="commons.db.connection.host", value= "")
@CucumberProperty(key="commons.db.connection.port", value= "")
@CucumberProperty(key="datastore.elasticsearch.nodes", value= "")
@CucumberProperty(key="datastore.elasticsearch.port", value= "")
@CucumberProperty(key="datastore.client.class", value= "")
@CucumberProperty(key="commons.eventbus.url", value= "")
@CucumberProperty(key="certificate.jwt.private.key", value= "")
@CucumberProperty(key="certificate.jwt.certificate", value= "")
@CucumberProperty(key="broker.ip", value= "")
public class RunAccountServiceI9nTest {}
