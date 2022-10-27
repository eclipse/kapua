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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


@Category(JUnitTests.class)
public class TransportAsyncTest {

    final ExecutorService executorService = Mockito.mock(ExecutorService.class);
    TransportAsync transportAsync = new TransportAsync(executorService);

    @Test
    public void transportAsyncExecutorIdTest() {
        Assert.assertThat("Instance of TransportAsync expected.", transportAsync, IsInstanceOf.instanceOf(TransportAsync.class));
    }

    @Test
    public void handleConnectedTest() {
        Assert.assertThat("Instance of Future expected", transportAsync.handleConnected(), IsInstanceOf.instanceOf(Future.class));
    }

    @Test
    public void handleDisconnectedTest() {
        Assert.assertThat("Instance of Future expected", transportAsync.handleDisconnected(), IsInstanceOf.instanceOf(Future.class));
    }

    @Test
    public void listenTest() {
        final Transport.Listener listener = Mockito.mock(Transport.Listener.class);
        Assert.assertThat("Instance of ListenerHandle expected.", transportAsync.listen(listener), IsInstanceOf.instanceOf(Transport.ListenerHandle.class));
    }

    @Test
    public void listenListenerNullTest() {
        Assert.assertThat("Instance of ListenerHandle expected.", transportAsync.listen(null), IsInstanceOf.instanceOf(Transport.ListenerHandle.class));
    }
}