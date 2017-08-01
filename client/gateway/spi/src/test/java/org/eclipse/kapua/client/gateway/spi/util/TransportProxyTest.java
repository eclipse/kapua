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

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.kapua.client.gateway.Transport.ListenerHandle;
import org.eclipse.kapua.client.gateway.spi.util.TransportAsync;
import org.eclipse.kapua.client.gateway.spi.util.TransportProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TransportProxyTest {

    private ExecutorService executor;

    @Before
    public void createExecutor() {
        this.executor = Executors.newSingleThreadExecutor();
    }

    @After
    public void disposeExecutor() {
        this.executor.shutdown();
    }

    @Test
    public void test1() throws Exception {

        final AtomicBoolean b1 = new AtomicBoolean();
        final AtomicBoolean b2 = new AtomicBoolean();

        final TransportAsync transport = new TransportAsync(executor);
        transport.handleConnected().get();

        try (final TransportProxy transportProxy = TransportProxy.proxy(transport, executor)) {

            assertEquals(false, b1.get());
            assertEquals(false, b2.get());

            final ListenerHandle h1 = transportProxy.listen(b1::set);
            Thread.sleep(100);

            assertEquals(true, b1.get());
            assertEquals(false, b2.get());

            final ListenerHandle h2 = transportProxy.listen(b2::set);
            Thread.sleep(100);

            assertEquals(true, b1.get());
            assertEquals(true, b2.get());

            transport.handleDisconnected().get();

            assertEquals(false, b1.get());
            assertEquals(false, b2.get());

            h2.close();

            transport.handleConnected().get();

            assertEquals(true, b1.get());
            assertEquals(false, b2.get());

            h1.close();

            transport.handleDisconnected().get();

            assertEquals(true, b1.get());
            assertEquals(false, b2.get());

        }
    }

    @Test(expected = NullPointerException.class)
    public void testFail1() {
        TransportProxy.proxy(null, executor);
    }

    @Test(expected = NullPointerException.class)
    public void testFail2() {
        TransportProxy.proxy(new TransportAsync(executor), null);
    }

    @Test(expected = IllegalStateException.class)
    public void testFail3() {
        final TransportProxy proxy = TransportProxy.proxy(new TransportAsync(executor), executor);
        proxy.close();
        proxy.listen((state) -> {
        });
    }

}
