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

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

import org.eclipse.kapua.client.gateway.Transport;

public final class TransportProxy implements Transport, AutoCloseable {

    private final Transport transport;
    private final Executor executor;
    private boolean closed;

    private final Set<Listener> listeners = new CopyOnWriteArraySet<>();

    private ListenerHandle handle;

    private boolean lastKnownState;

    private TransportProxy(final Transport transport, final Executor executor) {
        this.transport = transport;
        this.executor = executor;
    }

    @Override
    public void close() {
        detach();
    }

    @Override
    public synchronized ListenerHandle listen(final Listener listener) {
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
     * @param listener
     *            the listener to remove
     */
    private synchronized void removeListener(final Listener listener) {

        // simply remove listener

        listeners.remove(listener);
    }

    private synchronized void handleChange(final boolean state) {
        lastKnownState = state;
        fireEvent(state, new CopyOnWriteArraySet<>(listeners));
    }

    private void fireEvent(final boolean state, final Set<Listener> listeners) {
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
     *
     * @param transport
     * @param executor
     * @return
     */
    public static TransportProxy proxy(final Transport transport, final Executor executor) {
        Objects.requireNonNull(transport);
        Objects.requireNonNull(executor);

        return new TransportProxy(transport, executor);
    }
}
