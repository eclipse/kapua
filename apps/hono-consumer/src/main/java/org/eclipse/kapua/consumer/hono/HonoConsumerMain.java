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
package org.eclipse.kapua.consumer.hono;

import org.eclipse.kapua.KapuaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 * 
 *
 */
public class HonoConsumerMain extends AbstractVerticle {

    protected final static Logger logger = LoggerFactory.getLogger(HonoConsumerMain.class);

    public static void main(String argv[]) throws KapuaException {
        HonoConsumer messageConsumer = new HonoConsumer();
        VertxOptions options = new VertxOptions();
        options.setBlockedThreadCheckInterval(60 * 1000);
        // TODO more options?
        Vertx vertx = Vertx.vertx(options);
        vertx.deployVerticle(messageConsumer);
    }

}
