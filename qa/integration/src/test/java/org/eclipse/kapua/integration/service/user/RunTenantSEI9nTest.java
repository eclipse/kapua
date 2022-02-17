/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.qa.common.cucumber.CucumberProperty;
import org.eclipse.kapua.qa.common.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = "classpath:features/user/TenantSEI9n.feature",
        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.user.steps"
        },
        plugin = {"pretty",
                "html:target/cucumber/TenantSEI9n",
                "json:target/TenantSEI9n_cucumber.json"
        },
        strict = true,
        monochrome = true)
@CucumberProperty(key = "datastore.elasticsearch.provider", value = "org.eclipse.kapua.service.elasticsearch.client.rest.RestElasticsearchClientProvider")
@CucumberProperty(key = "org.eclipse.kapua.qa.datastore.extraStartupDelay", value = "1")
@CucumberProperty(key = "org.eclipse.kapua.qa.broker.extraStartupDelay", value = "1")
public class RunTenantSEI9nTest {
}
