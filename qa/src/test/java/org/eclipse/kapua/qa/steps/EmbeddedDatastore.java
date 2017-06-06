/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.qa.steps;

import java.io.IOException;
import org.eclipse.kapua.service.datastore.client.embedded.EsEmbeddedEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.java.guice.ScenarioScoped;

/**
 * Singleton for managing datastore creation and deletion inside Gherkin scenarios.
 */
@ScenarioScoped
public class EmbeddedDatastore {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedDatastore.class);

    private EsEmbeddedEngine esEmbeddedEngine;

    @Before(order = HookPriorities.DATASTORE)
    public void setup() {
        logger.info("starting embedded datstore");
        esEmbeddedEngine = new EsEmbeddedEngine();
        logger.info("starting embedded datstore DONE");
    }

    @After(order = HookPriorities.DATASTORE)
    public void closeNode() throws IOException {
        logger.info("closing embedded datstore");
        esEmbeddedEngine.close();
        logger.info("closing embedded datstore DONE");
    }
}
