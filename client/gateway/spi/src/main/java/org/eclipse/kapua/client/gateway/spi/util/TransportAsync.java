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
package org.eclipse.kapua.client.gateway.spi.util;

import static java.util.concurrent.CompletableFuture.completedFuture;

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.eclipse.kapua.client.gateway.Transport;

public class TransportAsync implements Transport {

    private final ExecutorService executor;
    private CopyOnWriteArraySet<Listener> listeners = new CopyOnWriteArraySet<>();
    private boolean state;

    public TransportAsync(final ExecutorService executor) {
        this.executor = executor;
    }

    private Future<?> fireEvent(final boolean state) {
        if (listeners.isEmpty()) {
            return completedFuture(null);
        }

        final CopyOnWriteArraySet<Listener> listeners = new CopyOnWriteArraySet<>(this.listeners);
        return executor.submit(() -> listeners.stream().forEach(l -> l.stateChange(state)));
    }

    public synchronized Future<?> handleConnected() {
        if (!state) {
            state = true;
            return fireEvent(true);
        }

        return completedFuture(null);
    }

    public synchronized Future<?> handleDisconnected() {
        if (state) {
            state = false;
            return fireEvent(false);
        }

        return completedFuture(null);
    }

    @Override
    public synchronized ListenerHandle listen(final Listener listener) {
        this.listeners.add(listener);
        fireEvent(state);
        return () -> removeListener(listener);
    }

    private synchronized void removeListener(final Listener listener) {
        this.listeners.remove(listener);
    }
}
