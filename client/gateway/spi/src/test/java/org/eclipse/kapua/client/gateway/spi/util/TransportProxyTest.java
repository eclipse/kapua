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
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.spi.util;

import org.eclipse.kapua.client.gateway.Transport;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.concurrent.Executor;


@Category(JUnitTests.class)
public class TransportProxyTest {

    @Test(expected = IllegalStateException.class)
    public void closeTest() {
        final Transport transport = Mockito.mock(Transport.class);
        final Executor executor = Mockito.mock(Executor.class);
        TransportProxy transportProxy = TransportProxy.proxy(transport, executor);
        transportProxy.close();
        Assert.assertNull(transportProxy.listen(Mockito.mock(Transport.Listener.class)));
    }

    @Test
    public void listenTest() {
        final Transport transport = Mockito.mock(Transport.class);
        final Executor executor = Mockito.mock(Executor.class);
        TransportProxy transportProxy = TransportProxy.proxy(transport, executor);
        final Transport.Listener listener = Mockito.mock(Transport.Listener.class);
        Assert.assertThat("Instance of ListenerHandle expected.", transportProxy.listen(listener), IsInstanceOf.instanceOf(Transport.ListenerHandle.class));
    }

    @Test(expected = NullPointerException.class)
    public void listenListenerNullTest() {
        final Transport transport = Mockito.mock(Transport.class);
        final Executor executor = Mockito.mock(Executor.class);
        TransportProxy transportProxy = TransportProxy.proxy(transport, executor);
        Assert.assertThat("Instance of ListenerHandle expected.", transportProxy.listen(null), IsInstanceOf.instanceOf(Transport.ListenerHandle.class));
    }

    @Test
    public void proxyTest() {
        final Transport transport = Mockito.mock(Transport.class);
        final Executor executor = Mockito.mock(Executor.class);
        Assert.assertThat("Instance of TransportProxy expected.", TransportProxy.proxy(transport, executor), IsInstanceOf.instanceOf(TransportProxy.class));
    }

    @Test(expected = NullPointerException.class)
    public void proxyTransportNullTest() {
        final Executor executor = Mockito.mock(Executor.class);
        Assert.assertThat("Instance of TransportProxy expected.", TransportProxy.proxy(null, executor), IsInstanceOf.instanceOf(TransportProxy.class));
    }

    @Test(expected = NullPointerException.class)
    public void proxyExecutorNullTest() {
        final Transport transport = Mockito.mock(Transport.class);
        Assert.assertThat("Instance of TransportProxy expected.", TransportProxy.proxy(transport, null), IsInstanceOf.instanceOf(TransportProxy.class));
    }

    @Test(expected = NullPointerException.class)
    public void proxyTransportNullAndExecutorNullTest() {
        Assert.assertThat("Instance of TransportProxy expected.", TransportProxy.proxy(null, null), IsInstanceOf.instanceOf(TransportProxy.class));
    }
}