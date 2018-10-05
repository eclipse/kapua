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
package org.eclipse.kapua.broker.client.amqp;

import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;

import io.vertx.core.Vertx;
import io.vertx.proton.ProtonQoS;

public class AmqpReceiver extends AbstractAmqpClient {

    //TODO make them configurable if needed
    private final static Integer PREFETCH = new Integer(10);
    private final static boolean AUTO_ACCEPT = false;
    private final static ProtonQoS QOS = ProtonQoS.AT_MOST_ONCE;

    private String destination;

    public AmqpReceiver(Vertx vertx, ClientOptions clientOptions) {
        super(vertx, clientOptions);
        destination = clientOptions.getString(AmqpClientOptions.DESTINATION);
    }

    @Override
    protected void doAfterConnect() {
        createReceiver(destination, PREFETCH, AUTO_ACCEPT, QOS);
    }

}
