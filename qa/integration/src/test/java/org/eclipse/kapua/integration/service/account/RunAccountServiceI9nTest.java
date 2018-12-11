/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.test.cucumber.CucumberWithProperties;
import org.junit.runner.RunWith;

@RunWith(CucumberWithProperties.class)
@CucumberOptions(
        features = "classpath:features/account/AccountExpirationI9n.feature",
        glue = {"org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.service.user.steps"
               },
        plugin = {"pretty", 
                  "html:target/cucumber/AccountServiceI9n",
                  "json:target/AccountServiceI9n_cucumber.json"
                 },
        monochrome=true)
public class RunAccountServiceI9nTest {}
