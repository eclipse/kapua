/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
        features = "classpath:features/user/UserPermissionI9n.feature",

        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.service.authorization.steps",
                "org.eclipse.kapua.service.device.registry.steps",
                "org.eclipse.kapua.service.tag.steps",
                "org.eclipse.kapua.service.job.steps",
                "org.eclipse.kapua.service.datastore.steps",
                "org.eclipse.kapua.service.scheduler.steps",
                "org.eclipse.kapua.service.endpoint.steps"
        },
        plugin = {"pretty",
                "html:target/cucumber/UserServiceI9n",
                "json:target/UserServiceI9n_cucumber.json"
        },
        strict = true,
        monochrome = true)
@CucumberProperty(key = "kapua.config.url", value = "")
@CucumberProperty(key = "datastore.elasticsearch.provider", value = "org.eclipse.kapua.service.elasticsearch.client.rest.RestElasticsearchClientProvider")
@CucumberProperty(key = "datastore.elasticsearch.nodes", value = "127.0.0.1:9200")
@CucumberProperty(key = "org.eclipse.kapua.qa.datastore.extraStartupDelay", value = "5")
@CucumberProperty(key = "org.eclipse.kapua.qa.broker.extraStartupDelay", value = "5")
@CucumberProperty(key = "es.set.netty.runtime.available.processors", value = "false")  // see https://github.com/elastic/elasticsearch/issues/25741
public class RunUserPermissionI9nTest {
}
