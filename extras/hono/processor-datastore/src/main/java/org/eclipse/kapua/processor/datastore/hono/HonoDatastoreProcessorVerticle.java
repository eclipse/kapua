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
package org.eclipse.kapua.processor.datastore.hono;

import javax.inject.Inject;

import org.eclipse.kapua.commons.core.vertx.HealthCheckAdapter;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.processor.commons.AbstractMessageProcessorVerticle;
import org.eclipse.kapua.processor.commons.MessageProcessorConfig;
import org.eclipse.kapua.processor.commons.MessageProcessorVerticle;

import io.vertx.core.Future;

// TODO move to common project
public class HonoDatastoreProcessorVerticle extends AbstractMessageProcessorVerticle implements MessageProcessorVerticle {

    @Inject
    private HonoDatastoreProcessorConfigFactory configFactory;

    private HonoDatastoreProcessor messageProcessor;

    @Override
    protected void internalStart(Future<Void> startFuture) throws Exception {
        MessageProcessorConfig<byte[], TransportMessage> config = configFactory.create();
        messageProcessor = HonoDatastoreProcessor.create(vertx, config); 
        for(HealthCheckAdapter adapter:config.getHealthCheckAdapters()) {
            messageProcessor.register(adapter);
        }
        messageProcessor.start(startFuture);
    }

    @Override
    protected void internalStop(Future<Void> stopFuture) throws Exception {
        if (messageProcessor != null) {
            messageProcessor.stop(stopFuture);
            messageProcessor = null;
        } else {
            stopFuture.complete();
        }
    }
}
