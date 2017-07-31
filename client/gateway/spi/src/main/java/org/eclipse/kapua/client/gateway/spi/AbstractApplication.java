/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.spi;

import java.util.concurrent.CompletionStage;

import org.eclipse.kapua.client.gateway.Application;
import org.eclipse.kapua.client.gateway.ErrorHandler;
import org.eclipse.kapua.client.gateway.MessageHandler;
import org.eclipse.kapua.client.gateway.Payload;
import org.eclipse.kapua.client.gateway.Topic;
import org.eclipse.kapua.client.gateway.Transport;

public abstract class AbstractApplication implements Application {

    private final AbstractClient.Context context;

    public AbstractApplication(final AbstractClient.Context context) {
        this.context = context;
    }

    @Override
    public synchronized Transport transport() {
        return context.transport();
    }

    @Override
    public AbstractData data(Topic topic) {
        return new AbstractData(this, topic);
    }

    @Override
    public void close() throws Exception {
        context.close();
    }

    protected CompletionStage<?> publish(Topic topic, Payload payload) {
        return context.publish(topic, payload);
    }

    public CompletionStage<?> subscribe(final Topic topic, final MessageHandler handler, final ErrorHandler<? extends Throwable> errorHandler) throws Exception {
        return context.subscribe(topic, handler, errorHandler);
    }
}