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
package org.eclipse.kapua.consumer.activemq.lifecycle;

import org.eclipse.kapua.commons.core.vertx.EnvironmentSetup;
import org.eclipse.kapua.commons.core.vertx.VertxApplication;

/**
 * ActiveMQ AMQP consumer with Kura payload converter and Kapua lifecycle manager
 *
 */
public class ConsumerAppplication extends VertxApplication<MainVerticle> {

    private static final String NAME = "ActiveMQ-lifecycle";

    public static void main(String args[]) throws Exception {
        new ConsumerAppplication().run(args);
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
        setup.configure(new ConsumerContextConfig());
    }
}
