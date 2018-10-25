/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.steps;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.java.en.Given;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class ExternalConsumers {

    protected final static Logger logger = LoggerFactory.getLogger(ExternalConsumers.class);

    private static org.eclipse.kapua.processor.datastore.broker.ProcessorApplication datastore;
    private static org.eclipse.kapua.processor.lifecycle.broker.ProcessorApplication lifecycle;
    private static org.eclipse.kapua.processor.error.broker.ProcessorApplication error;

    private DBHelper database;

    @Inject
    public ExternalConsumers(DBHelper database) {
        this.database = database;
    }

    @Given("^Start External Consumers$")
    public void start() throws Exception {
        database.setup();
        datastore = new org.eclipse.kapua.processor.datastore.broker.ProcessorApplication();
        lifecycle = new org.eclipse.kapua.processor.lifecycle.broker.ProcessorApplication();
        error = new org.eclipse.kapua.processor.error.broker.ProcessorApplication();
        logger.info("Starting datastore consumer {}", datastore);
        datastore.run(null);
        logger.info("Starting lifecycle consumer {}", lifecycle);
        lifecycle.run(null);
        logger.info("Starting error consumer {}", error);
        error.run(null);
    }

    @Given("^Stop External Consumers$")
    public void stop() throws Exception {
        logger.info("Stopping datastore consumer (timeout 60sec) {}", datastore);
        datastore.shutdown(60000);
        logger.info("Stopping lifecycle consumer (timeout 60sec) {}", lifecycle);
        lifecycle.shutdown(60000);
        logger.info("Stopping error consumer (timeout 60sec) {}", error);
        error.shutdown(60000);
    }

}
