/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.qa.common.utils;

import java.io.IOException;
import java.time.Duration;

import cucumber.api.java.en.Given;
import org.eclipse.kapua.service.datastore.client.embedded.EsEmbeddedEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.runtime.java.guice.ScenarioScoped;

/**
 * Singleton for managing datastore creation and deletion inside Gherkin scenarios.
 */
@ScenarioScoped
public class EmbeddedDatastore {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedDatastore.class);

    private static final int EXTRA_STARTUP_DELAY = Integer.getInteger("org.eclipse.kapua.qa.datastore.extraStartupDelay", 0);

    private static final boolean NO_EMBEDDED_SERVERS = Boolean.getBoolean("org.eclipse.kapua.qa.noEmbeddedServers");

    private static EsEmbeddedEngine esEmbeddedEngine;


    @Given("^Start Datastore$")
    public void setup() {

        if (NO_EMBEDDED_SERVERS) {
            return;
        }
        logger.info("starting embedded datastore");
        esEmbeddedEngine = new EsEmbeddedEngine();
        if (EXTRA_STARTUP_DELAY > 0) {
            try {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("starting embedded datastore DONE");
    }

    @Given("^Stop Datastore$")
    public void closeNode() throws IOException {

        if (NO_EMBEDDED_SERVERS) {
            return;
        }
        logger.info("closing embedded datastore");
        if (EXTRA_STARTUP_DELAY > 0) {
            try {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (esEmbeddedEngine != null) {
            esEmbeddedEngine.close();
        }
        logger.info("closing embedded datastore DONE");
    }
}
