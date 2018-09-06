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

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class ExternalConsumers {

    protected final static Logger logger = LoggerFactory.getLogger(ExternalConsumers.class);

    private static org.eclipse.kapua.consumer.activemq.datastore.ConsumerApplication datastore;
    private static org.eclipse.kapua.consumer.activemq.lifecycle.ConsumerAppplication lifecycle;
    private static org.eclipse.kapua.consumer.activemq.error.ConsumerApplication error;

    private DBHelper database;

    @Inject
    public ExternalConsumers(DBHelper database) {
        this.database = database;
    }

    @Before(value = "@StartExternalConsumers")
    public void start() throws Exception {
        database.setup();
        datastore = new org.eclipse.kapua.consumer.activemq.datastore.ConsumerApplication();
        lifecycle = new org.eclipse.kapua.consumer.activemq.lifecycle.ConsumerAppplication();
        error = new org.eclipse.kapua.consumer.activemq.error.ConsumerApplication();
        logger.info("Starting datastore consumer {}", datastore);
        datastore.run(null);
        logger.info("Starting lifecycle consumer {}", lifecycle);
        lifecycle.run(null);
        logger.info("Starting error consumer {}", error);
        error.run(null);
    }

    @After(value = "@StopExternalConsumers")
    public void stop() throws Exception {
        logger.info("Stopping datastore consumer (timeout 60sec) {}", datastore);
        datastore.shutdown(60000);
        logger.info("Stopping lifecycle consumer (timeout 60sec) {}", lifecycle);
        lifecycle.shutdown(60000);
        logger.info("Stopping error consumer (timeout 60sec) {}", error);
        error.shutdown(60000);
    }

}
