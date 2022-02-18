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
package org.eclipse.kapua.client.gateway;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A control interface on the underlying client transport
 * <p>
 * <b>Note:</b> There is only one set of transport events available for the client.
 * Setting a new set of transport state listeners will clear the previously set listeners.
 * </p>
 */
public interface Transport {

    public interface TransportEvents {

        public void connected(Runnable runnable);

        public void disconnected(Runnable runnable);
    }

    @FunctionalInterface
    public interface ListenerHandle extends AutoCloseable {

        @Override
        public void close();
    }

    @FunctionalInterface
    public interface Listener {

        public void stateChange(boolean state);
    }

    /**
     * Add a state listener
     *
     * <p>
     * The listener will be called immediately after setting with the
     * last known state.
     * </p>
     *
     * @param listener
     *            the listener to transport state changes
     */
    public ListenerHandle listen(Listener listener);

    /**
     * This method allows to atomically set a state listener using simple runnable.
     *
     * <p>
     * This method is intended to be used with Java lambdas where each state change
     * (connected, disconnected) is mapped to one lambda. However, as the state change
     * will be initially reported it might happen that the state actually changes between
     * setting the connect and disconnect handler. This way there would be no way to properly
     * report the initial state.
     * </p>
     * <p>
     * Setting the event handlers using this methods works by updating
     * the provided {@link TransportEvents} fields inside the provided consumer. The
     * consumer will only be called once inside this method. The event listeners will
     * then be set atomically.
     * </p>
     *
     * <pre>
     * client.transport().events( events {@code ->} {
     *   events.connected ( () {@code ->} System.out.println ("Connected") );
     *   events.disconnected ( () {@code ->} System.out.println ("Disconnected") );
     * });
     * </pre>
     *
     * @param events
     *            code to update the {@link TransportEvents}
     * @return
     *
     */
    public default ListenerHandle events(final Consumer<TransportEvents> events) {
        class TransportEventsImpl implements TransportEvents {

            private Runnable connected;
            private Runnable disconnected;

            @Override
            public void connected(final Runnable runnable) {
                connected = runnable;
            }

            @Override
            public void disconnected(final Runnable runnable) {
                disconnected = runnable;
            }

        }

        final TransportEventsImpl impl = new TransportEventsImpl();

        events.accept(impl);

        return listen(state -> {
            if (state) {
                if (impl.connected != null) {
                    impl.connected.run();
                }
            } else {
                if (impl.disconnected != null) {
                    impl.disconnected.run();
                }
            }
        });
    }

    /**
     * Wait for the connection to be established
     * <p>
     * <b>Note:</b> This method will reset the transport listeners.
     * </p>
     *
     * @param transport
     *            to wait on
     * @throws InterruptedException
     *             if the wait got interrupted
     */
    public static void waitForConnection(final Transport transport) throws InterruptedException {
        Objects.requireNonNull(transport);

        final Semaphore sem = new Semaphore(0);

        try (ListenerHandle handle = transport.listen(state -> {
            if (state) {
                sem.release();
            }
        })) {
            sem.acquire();
        }
    }

    /**
     * Wait for the connection to be established or the timeout occurs
     * <p>
     * <b>Note:</b> This method will reset the transport listeners.
     * </p>
     *
     * @param transport
     *            to wait on
     * @param timeout
     *            the timeout
     * @throws InterruptedException
     *             if the wait got interrupted
     */
    public static boolean waitForConnection(final Transport transport, final Duration timeout) throws InterruptedException {
        Objects.requireNonNull(transport);
        Objects.requireNonNull(timeout);

        final Semaphore sem = new Semaphore(0);

        try (ListenerHandle handle = transport.listen(state -> {
            if (state) {
                sem.release();
            }
        })) {
            return sem.tryAcquire(timeout.toNanos(), TimeUnit.NANOSECONDS);
        }

    }
}
