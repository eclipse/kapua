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
package org.eclipse.kapua.processor.lifecycle.broker;

import java.util.concurrent.TimeUnit;

import org.eclipse.kapua.commons.core.vertx.EnvironmentSetup;
import org.eclipse.kapua.commons.core.vertx.VertxApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ActiveMQ AMQP consumer with Kura payload converter and Kapua lifecycle manager
 *
 */
public class ProcessorApplication extends VertxApplication<MainVerticle> {

    protected final static Logger logger = LoggerFactory.getLogger(ProcessorApplication.class);

    private static final String NAME = "processor-lifecycle";

    public static void main(String args[]) throws Exception {
        ProcessorApplication application = null;
        try {
            application = new ProcessorApplication();
            application.run(args);
        }
        catch (Exception e) {
            logger.error("Failed to start application: {}", e.getMessage(), e);
            logger.info("Shutting down application...");
            application.shutdown();
            logger.info("Shutting down application...DONE");
            TimeUnit.SECONDS.sleep(2);
            System.exit(1);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class<MainVerticle> getMainVerticle() {
        return MainVerticle.class;
    }

    @Override
    public void initialize(EnvironmentSetup setup) throws Exception {
        super.initialize(setup);
        setup.configure(new ProcessorContextConfig());
    }
}
