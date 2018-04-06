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

import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.processor.commons.MessageProcessor;
import org.eclipse.kapua.processor.commons.MessageProcessorConfig;

import io.vertx.core.Vertx;

public class AmqpLifecycleProcessor extends MessageProcessor<byte[], TransportMessage> {

    public static AmqpLifecycleProcessor create(Vertx aVertx, MessageProcessorConfig<byte[], TransportMessage> aConfig) {
        return new AmqpLifecycleProcessor(aVertx, aConfig);
    }

    protected AmqpLifecycleProcessor(Vertx aVertx, MessageProcessorConfig<byte[], TransportMessage> aConfig) {
        super(aVertx, aConfig);
    }
}
