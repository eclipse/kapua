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
package org.eclipse.kapua.client.gateway.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.kapua.client.gateway.Transport;
import org.eclipse.kapua.client.gateway.utils.TransportAsync;
import org.junit.Assert;
import org.junit.Test;

public class TransportAsyncTest {

    @Test
    public void test1() throws InterruptedException {
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        try {
            final TransportAsync transport = new TransportAsync(executor);

            final Instant start = Instant.now();
            executor.schedule(() -> {
                transport.handleConnected();
            }, 100, TimeUnit.MILLISECONDS);

            Transport.waitForConnection(transport);

            Duration duration = Duration.between(start, Instant.now());
            System.out.println(duration);

            Assert.assertTrue(duration.compareTo(Duration.ofMillis(100)) > 0);

        } finally {
            executor.shutdown();
        }
    }

    @Test
    public void test2() throws InterruptedException {
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        try {
            final TransportAsync transport = new TransportAsync(executor);
            transport.events(events -> {
            });
        } finally {
            executor.shutdown();
        }
    }
}
