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
package org.eclipse.kapua.consumer.activemq.datastore;

import org.eclipse.kapua.commons.core.BeanContextConfig;
import org.eclipse.kapua.commons.core.Configuration;
import org.eclipse.kapua.commons.core.vertx.Environment;
import org.eclipse.kapua.commons.core.vertx.EnvironmentSetup;
import org.eclipse.kapua.commons.core.vertx.VertxApplication;

/**
 * ActiveMQ AMQP consumer with Kura payload converter and Kapua data store ingestion
 *
 */
public class Consumer extends VertxApplication<MainVerticle> {

    private static final String NAME = "ActiveMQ-datastore";
    public static void main(String args[]) throws Exception {
        new Consumer().run(args);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void initialize(EnvironmentSetup setup) {
        setup.configure(new BeanContextConfig() {});
    }

    @Override
    public void run(Environment environment, Configuration config) {
        super.deployMainVerticle(environment, MainVerticle.class);
    }

}
