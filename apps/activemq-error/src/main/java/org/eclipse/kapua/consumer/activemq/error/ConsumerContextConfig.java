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
package org.eclipse.kapua.consumer.activemq.error;

import org.eclipse.kapua.commons.core.ObjectContextConfig;
import org.eclipse.kapua.commons.core.vertx.HttpRestServer;
import org.eclipse.kapua.commons.core.vertx.HttpRestServerImpl;

import com.google.inject.Singleton;

public class ConsumerContextConfig extends ObjectContextConfig {

    public ConsumerContextConfig() {
    }

    @Override
    protected void configure() {
        super.configure();
        bind(HttpRestServer.class).to(HttpRestServerImpl.class).in(Singleton.class);;
    }

}
