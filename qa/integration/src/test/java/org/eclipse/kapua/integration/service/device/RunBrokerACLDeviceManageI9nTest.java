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
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.integration.service.device;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.qa.common.cucumber.CucumberProperty;
import org.eclipse.kapua.qa.common.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = {"classpath:features/broker/acl/BrokerACLDeviceManageI9n.feature"
        },
        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.service.tag.steps",
                "org.eclipse.kapua.service.device.registry.steps"
        },
        plugin = {"pretty",
                "html:target/cucumber/BrokerACLDeviceManageI9n",
                "json:target/BrokerACLDeviceManageI9n_cucumber.json"
        },
        strict = true,
        monochrome = true)

@CucumberProperty(key = "DOCKER_HOST", value = "")
@CucumberProperty(key = "DOCKER_CERT_PATH", value = "")
@CucumberProperty(key = "commons.db.schema.update", value = "")
@CucumberProperty(key = "commons.db.connection.host", value = "")
@CucumberProperty(key = "commons.db.connection.port", value = "")
@CucumberProperty(key = "commons.eventbus.url", value = "")
@CucumberProperty(key = "certificate.jwt.private.key", value = "")
@CucumberProperty(key = "certificate.jwt.certificate", value = "")
@CucumberProperty(key = "datastore.elasticsearch.nodes", value = "")
@CucumberProperty(key = "datastore.elasticsearch.provider", value = "")
@CucumberProperty(key = "kapua.config.url", value = "")
@CucumberProperty(key = "org.eclipse.kapua.qa.broker.extraStartupDelay", value = "3")
public class RunBrokerACLDeviceManageI9nTest {
}
