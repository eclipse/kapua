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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import org.eclipse.kapua.client.gateway.Application;
import org.eclipse.kapua.client.gateway.ErrorHandler;
import org.eclipse.kapua.client.gateway.MessageHandler;
import org.eclipse.kapua.client.gateway.Payload;
import org.eclipse.kapua.client.gateway.Topic;
import org.eclipse.kapua.client.gateway.Transport;
import org.eclipse.kapua.client.gateway.utils.TransportAsync;

public abstract class AbstractApplication implements Application {

    private final AbstractClient client;
    protected final Set<Topic> subscriptions = new HashSet<>();
    protected final String applicationId;
    protected final TransportAsync transport;
    private boolean closed;

    public AbstractApplication(final AbstractClient client, final String applicationId, final Executor executor) {
        this.client = client;
        this.applicationId = applicationId;
        transport = new TransportAsync(executor);
    }

    protected synchronized void handleConnected() {
        if (closed) {
            return;
        }
        transport.handleConnected();
    }

    protected synchronized void handleDisconnected() {
        if (closed) {
            return;
        }
        transport.handleDisconnected();
    }

    protected void checkClosed() {
        if (closed) {
            throw new IllegalStateException("Application is closed");
        }
    }

    @Override
    public synchronized Transport transport() {
        checkClosed();
        return transport;
    }

    @Override
    public abstract AbstractData data(Topic topic);

    @Override
    public void close() throws Exception {
        synchronized (this) {
            if (closed) {
                return;
            }
            closed = true;
        }

        client.internalCloseApplication(applicationId, subscriptions, this);
    }

    protected abstract CompletionStage<?> publish(Topic topic, Payload payload);

    public CompletionStage<?> subscribe(Topic topic, MessageHandler handler, ErrorHandler<? extends Throwable> errorHandler) throws Exception {
        synchronized (this) {
            checkClosed();
            recordSubscription(topic);
        }
        return internalSubscribe(topic, handler, errorHandler);
    }

    private void recordSubscription(final Topic topic) {
        subscriptions.add(topic);
    }

    protected abstract CompletionStage<?> internalSubscribe(Topic topic, MessageHandler handler, ErrorHandler<? extends Throwable> errorHandler) throws Exception;
}