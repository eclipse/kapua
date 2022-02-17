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
package org.eclipse.kapua.integration.service.authorization;

import org.eclipse.kapua.qa.common.cucumber.CucumberProperty;
import org.eclipse.kapua.qa.common.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = { "classpath:features/authorization/AccessInfoService.feature",
                     "classpath:features/authorization/DomainService.feature",
                     "classpath:features/authorization/GroupService.feature",
                     "classpath:features/authorization/MiscAuthorization.feature",
                     "classpath:features/authorization/RoleService.feature"
                   },
        glue = { "org.eclipse.kapua.qa.common",
                 "org.eclipse.kapua.service.authorization.steps",
                 "org.eclipse.kapua.service.account.steps",
                 "org.eclipse.kapua.service.user.steps",
                 "org.eclipse.kapua.service.device.registry.steps"
               },
        plugin = {"pretty",
                  "html:target/cucumber/AuthorizationServiceI9n",
                  "json:target/AuthorizationServiceI9n_cucumber.json"
                 },
        strict = true,
        monochrome = true)
@CucumberProperty(key="test.type", value="integration")
@CucumberProperty(key="commons.db.schema", value="kapuadb")
@CucumberProperty(key="commons.db.schema.update", value="true")
public class RunAuthorizationServiceI9nTest {}
