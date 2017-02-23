/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.internal;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features",
        glue = { "org.eclipse.kapua.service.device.registry.common",
                "org.eclipse.kapua.service.device.registry.internal",
                "org.eclipse.kapua.service.device.registry.connection.internal",
                "org.eclipse.kapua.service.device.registry.event.internal" },
        plugin = { "pretty",
                "html:target/cucumber",
                "json:target/cucumber.json" },
        monochrome = true)

public class RunTest {

}
