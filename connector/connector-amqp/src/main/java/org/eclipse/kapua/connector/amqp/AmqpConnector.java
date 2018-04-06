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
package org.eclipse.kapua.connector.amqp;

import org.eclipse.kapua.connector.Processor;
import org.eclipse.kapua.connector.converter.kura.KuraPayloadProtoConverter;
import org.eclipse.kapua.connector.processor.logger.LoggerProcessor;
import org.eclipse.kapua.message.transport.TransportMessage;

import io.vertx.core.Vertx;

public class AmqpConnector {

    private AmqpConnector() {
    }

    public static void main(String argv[]) {

        // TODO options
        Vertx vertx = Vertx.vertx(); 

        KuraPayloadProtoConverter converter = new KuraPayloadProtoConverter();
        Processor<TransportMessage> processor = new LoggerProcessor();
        AmqpConnectorVerticle amqpConnVrtcl = new AmqpConnectorVerticle(converter, processor);
        vertx.deployVerticle(amqpConnVrtcl);
    }
}
