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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.integration.service.account;

import cucumber.api.CucumberOptions;
import org.eclipse.kapua.qa.common.cucumber.CucumberProperty;
import org.eclipse.kapua.qa.common.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = {
                "classpath:features/account/AccountService.feature",
                "classpath:features/account/AccountExpirationI9n.feature",
                "classpath:features/account/FindSelfAccount.feature",
                "classpath:features/account/AccountGroupService.feature",
                "classpath:features/account/AccountDeviceRegistryService.feature",
                "classpath:features/account/AccountJobService.feature",
                "classpath:features/account/AccountRoleService.feature",
                "classpath:features/account/AccountTagService.feature",
                "classpath:features/account/AccountUserService.feature",
                "classpath:features/account/AccountCredentialService.feature"
        },
        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.service.authorization.steps",
                "org.eclipse.kapua.service.device.registry.steps",
                "org.eclipse.kapua.service.job.steps",
                "org.eclipse.kapua.service.tag.steps"
        },
        plugin = {"pretty",
                "html:target/cucumber/AccountServiceI9n",
                "json:target/AccountServiceI9n_cucumber.json"
        },
        strict = true,
        monochrome = true)

@CucumberProperty(key = "DOCKER_HOST", value = "")
@CucumberProperty(key = "DOCKER_CERT_PATH", value = "")
@CucumberProperty(key = "commons.db.schema.update", value = "")
@CucumberProperty(key = "commons.db.connection.host", value = "")
@CucumberProperty(key = "commons.db.connection.port", value = "")
@CucumberProperty(key = "commons.eventbus.url", value = "")
@CucumberProperty(key = "broker.ip", value = "")
@CucumberProperty(key = "certificate.jwt.private.key", value = "")
@CucumberProperty(key = "certificate.jwt.certificate", value = "")
@CucumberProperty(key = "datastore.elasticsearch.nodes", value = "")
@CucumberProperty(key = "datastore.elasticsearch.provider", value = "")
public class RunAccountServiceI9nTest {
}
