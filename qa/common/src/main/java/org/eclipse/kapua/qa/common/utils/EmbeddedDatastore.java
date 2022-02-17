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
 *     Eurotech
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.qa.common.utils;

import cucumber.api.java.en.Given;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.service.elasticsearch.server.embedded.EsEmbeddedEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

/**
 * Singleton for managing datastore creation and deletion inside Gherkin scenarios.
 */
@ScenarioScoped
public class EmbeddedDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedDatastore.class);

    private static final int EXTRA_STARTUP_DELAY = Integer.getInteger("org.eclipse.kapua.qa.datastore.extraStartupDelay", 0);

    private static final boolean NO_EMBEDDED_SERVERS = Boolean.getBoolean("org.eclipse.kapua.qa.noEmbeddedServers");

    private static EsEmbeddedEngine esEmbeddedEngine;


    @Given("^Start Datastore$")
    public void setup() {

        if (NO_EMBEDDED_SERVERS) {
            return;
        }

        LOG.info("Starting embedded datastore...");
        esEmbeddedEngine = new EsEmbeddedEngine();

        if (EXTRA_STARTUP_DELAY > 0) {
            try {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        LOG.info("Starting embedded datastore... DONE!");
    }

    @Given("^Stop Datastore$")
    public void closeNode() throws IOException {

        if (NO_EMBEDDED_SERVERS) {
            return;
        }

        LOG.info("Stopping embedded datastore...");
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

        LOG.info("Stopping embedded datastore... DONE!");
    }
}
