/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.spi.util;

import org.eclipse.kapua.client.gateway.Transport;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

public final class TransportProxy implements Transport, AutoCloseable {

    private final Transport transport;
    private final Executor executor;
    private boolean closed;

    private final Set<Listener> listeners = new CopyOnWriteArraySet<>();

    private ListenerHandle handle;

    private boolean lastKnownState;

    private TransportProxy(Transport transport, Executor executor) {
        this.transport = transport;
        this.executor = executor;
    }

    @Override
    public void close() {
        detach();
    }

    @Override
    public synchronized ListenerHandle listen(Listener listener) {
        Objects.requireNonNull(listener);

        checkClosed();

        // add before calling into attach()

        listeners.add(listener);

        // already attached?

        if (isAttached()) {

            // simply fire last known state

            fireEvent(lastKnownState, Collections.singleton(listener));

        } else {

            // attach, will implicitly call listener

            attach();
        }

        return () -> removeListener(listener);
    }

    private void checkClosed() {
        if (closed) {
            throw new IllegalStateException("TransportProxy instance is already closed");
        }
    }

    private boolean isAttached() {
        return handle != null;
    }

    /**
     * Remove a listener from the internal set
     * <p>
     * If the proxy instance is already closed, this method will <b>not</b> throw any exception
     * </p>
     *
     * @param listener the listener to remove
     */
    private synchronized void removeListener(Listener listener) {

        // simply remove listener

        listeners.remove(listener);
    }

    private synchronized void handleChange(boolean state) {
        lastKnownState = state;
        fireEvent(state, new CopyOnWriteArraySet<>(listeners));
    }

    private void fireEvent(boolean state, Set<Listener> listeners) {
        executor.execute(() -> {
            listeners.stream().forEach(l -> l.stateChange(state));
        });
    }

    private void attach() {
        handle = transport.listen(this::handleChange);
    }

    private synchronized void detach() {

        // mark closed

        closed = true;

        // check for a listener handle

        if (handle != null) {

            // and close it

            handle.close();
            handle = null;
        }
    }

    /**
     * @param transport
     * @param executor
     * @return test
     */
    public static TransportProxy proxy(Transport transport, Executor executor) {
        Objects.requireNonNull(transport);
        Objects.requireNonNull(executor);

        return new TransportProxy(transport, executor);
    }
}
