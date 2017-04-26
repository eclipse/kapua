/*******************************************************************************
 * Copyright (c) 2016, 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.user.integration;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/user",
        glue = {
                "org.eclipse.kapua.qa.steps",
                "org.eclipse.kapua.service.user.steps" },
        plugin = {"pretty", "html:target/cucumber/UserServiceI9n",
                  "json:target/UserServiceI9n_cucumber.json"},
        monochrome=true)
public class RunUserServiceI9nTest { }
