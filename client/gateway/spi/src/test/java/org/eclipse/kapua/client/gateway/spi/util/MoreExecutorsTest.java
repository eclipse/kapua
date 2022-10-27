/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others.
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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Category(JUnitTests.class)
public class MoreExecutorsTest {

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    ScheduledExecutorService executor = MoreExecutors.preventShutdown(executorService);

    @Test
    public void moreExecutorsTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<MoreExecutors> constructor = MoreExecutors.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void preventShutdownTest() {
        Assert.assertNotNull("Actual value shouldn't be null", MoreExecutors.preventShutdown(executorService));
        Assert.assertThat("Instance of ScheduledExecutorService expected.", MoreExecutors.preventShutdown(executorService), IsInstanceOf.instanceOf(ScheduledExecutorService.class));
    }

    @Test(expected = NullPointerException.class)
    public void preventShutdownNullExecutorTest() {
        Assert.assertNotNull("Actual value shouldn't be null", MoreExecutors.preventShutdown(null));
    }

    @Test
    public void scheduleRunnableIdTest() {
        Runnable command = Mockito.mock(Runnable.class);
        long delay = 1L;
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.schedule(command, delay, unit), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test(expected = NullPointerException.class)
    public void scheduleRunnableIdNullTest() {
        long delay = 1L;
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.schedule((Runnable)null, delay, unit), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test(expected = NullPointerException.class)
    public void scheduleRunnableIdUnitNullTest() {
        Runnable command = Mockito.mock(Runnable.class);
        long delay = 1L;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.schedule(command, delay, null), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test
    public void scheduleRunnableIdDelayNullTest() {
        Runnable command = Mockito.mock(Runnable.class);
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.schedule(command, 0, unit), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test
    public void executeTest() {
        Runnable command = Mockito.mock(Runnable.class);
        executor.execute(command);
        Assert.assertFalse(executor.isShutdown());
    }

    @Test(expected = NullPointerException.class)
    public void executeNullTest() {
        executor.execute(null);
        Assert.assertFalse(executor.isShutdown());
    }

    @Test
    public void scheduleCallableIdTest() {
        Callable<Integer> callable = Mockito.mock(Callable.class);
        long delay = 1L;
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.schedule(callable, delay, unit), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test(expected = NullPointerException.class)
    public void scheduleCallableIdNullTest() {
        long delay = 1L;
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.schedule((Callable)null, delay, unit), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test(expected = NullPointerException.class)
    public void scheduleCallableIdUnitNullTest() {
        Callable<Integer> callable = Mockito.mock(Callable.class);
        long delay = 1L;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.schedule(callable, delay, null), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test
    public void scheduleCallableIdDelayNullTest() {
        Callable<Integer> callable = Mockito.mock(Callable.class);
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.schedule(callable, 0, unit), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test
    public void scheduleAtFixedRateTest() {
        Runnable command = Mockito.mock(Runnable.class);
        long delay = 1L;
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.scheduleAtFixedRate(command, delay, 1, unit), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test(expected = NullPointerException.class)
    public void scheduleAtFixedRateRunnableIdNullTest() {
        long delay = 1L;
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.scheduleAtFixedRate(null, delay, 1, unit), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test(expected = NullPointerException.class)
    public void scheduleAtFixedRateUnitIdNullTest() {
        Runnable command = Mockito.mock(Runnable.class);
        long delay = 1L;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.scheduleAtFixedRate(command, delay,1, null), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test
    public void scheduleAtFixedRateDelayIdNullTest() {
        Runnable command = Mockito.mock(Runnable.class);
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.scheduleAtFixedRate(command, 0,1, unit), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shutdownTest() {
        executor.shutdown();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shutdownNowTest() {
        executor.shutdownNow();
    }

    @Test
    public void scheduleWithFixedDelayTest() {
        Runnable runnable = Mockito.mock(Runnable.class);
        long initialDelay = 1L;
        long delay = 1L;
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.scheduleWithFixedDelay(runnable, initialDelay, delay, unit), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test(expected = NullPointerException.class)
    public void scheduleWithFixedDelayRunnableIdNullTest() {
        long initialDelay = 1L;
        long delay = 1L;
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.scheduleWithFixedDelay(null, initialDelay, delay, unit), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test(expected = NullPointerException.class)
    public void scheduleWithFixedDelayUnitIdNullTest() {
        Runnable runnable = Mockito.mock(Runnable.class);
        long initialDelay = 1L;
        long delay = 1L;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.scheduleWithFixedDelay(runnable, initialDelay, delay, null), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test
    public void scheduleWithFixedDelayInitialDelayIdNullTest() {
        Runnable runnable = Mockito.mock(Runnable.class);
        TimeUnit unit = TimeUnit.MINUTES;
        long delay = 1L;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.scheduleWithFixedDelay(runnable, 0, delay, unit), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void scheduleWithFixedDelayDelayIdNullTest() {
        Runnable runnable = Mockito.mock(Runnable.class);
        TimeUnit unit = TimeUnit.MINUTES;
        long initialDelay = 1L;
        Assert.assertThat("Instance of ScheduledFuture expected.", executor.scheduleWithFixedDelay(runnable, initialDelay, 0, unit), IsInstanceOf.instanceOf(ScheduledFuture.class));
    }

    @Test
    public void isShutdownTest() {
        Assert.assertFalse("Expected false", executor.isShutdown());
    }

    @Test
    public void isTerminatedTest() {
        Assert.assertFalse("Expected false", executor.isTerminated());
    }

    @Test
    public void awaitTerminationTest() throws InterruptedException {
        long timeout = 5L;
        TimeUnit unit = TimeUnit.SECONDS;
        Assert.assertFalse("Expected false", executor.awaitTermination(timeout, unit));
    }

    @Test(expected = NullPointerException.class)
    public void awaitTerminationUnitIdNullTest() throws InterruptedException {
        long timeout = 1L;
        Assert.assertFalse("Expected false", executor.awaitTermination(timeout, null));
    }

    @Test(expected = NullPointerException.class)
    public void submitCallableIdNullTest() {
        Assert.assertThat("Instance of Future expected.", executor.submit((Callable)null), IsInstanceOf.instanceOf(Future.class));
    }

    @Test
    public void submitCallableIdTest() {
        Callable<Integer> callable = Mockito.mock(Callable.class);
        Assert.assertThat("Instance of Future expected.", executor.submit(callable), IsInstanceOf.instanceOf(Future.class));
    }

    @Test
    public void submitRunnableAndTIdTest() {
        Runnable runnable = Mockito.mock(Runnable.class);
        String result = "result";
        Assert.assertThat("Instance of Future expected.", executor.submit(runnable, result), IsInstanceOf.instanceOf(Future.class));
    }

    @Test(expected = NullPointerException.class)
    public void submitRunnableNullAndTIdTest() {
        String result = "result";
        Assert.assertThat("Instance of Future expected.", executor.submit((Runnable)null, result), IsInstanceOf.instanceOf(Future.class));
    }

    @Test
    public void submitRunnableAndTNullIdTest() {
        Runnable runnable = Mockito.mock(Runnable.class);
        Assert.assertThat("Instance of Future expected.", executor.submit(runnable, null), IsInstanceOf.instanceOf(Future.class));
    }

    @Test
    public void submitRunnableIdTest() {
        Runnable runnable = Mockito.mock(Runnable.class);
        Assert.assertThat("Instance of Future expected.", executor.submit(runnable), IsInstanceOf.instanceOf(Future.class));
    }

    @Test(expected = NullPointerException.class)
    public void submitRunnableIdNullTest() {
        Assert.assertThat("Instance of Future expected.", executor.submit((Runnable)null), IsInstanceOf.instanceOf(Future.class));
    }

    @Test
    public void invokeAllTasksIdTest() throws InterruptedException {
        Collection<Callable<Object>> tasks = new ArrayList<>();
        tasks.add(Mockito.mock(Callable.class));
        tasks.add(Mockito.mock(Callable.class));
        Assert.assertThat("Instance of List expected.", executor.invokeAll(tasks), IsInstanceOf.instanceOf(List.class));
    }

    @Test(expected = NullPointerException.class)
    public void invokeAllTasksIdNullTest() throws InterruptedException {
        Assert.assertThat("Instance of List expected.", executor.invokeAll(null), IsInstanceOf.instanceOf(List.class));
    }

    @Test
    public void invokeAllTasksTimeoutAndUnitIdTest() throws InterruptedException {
        Collection<Callable<Object>> tasks = new ArrayList<>();
        tasks.add(Mockito.mock(Callable.class));
        tasks.add(Mockito.mock(Callable.class));
        long timeout = 1L;
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertThat("Instance of List expected.", executor.invokeAll(tasks, timeout, unit), IsInstanceOf.instanceOf(List.class));
    }

    @Test(expected = NullPointerException.class)
    public void invokeAllTasksNullTimeoutAndUnitIdTest() throws InterruptedException {
        long timeout = 1L;
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertThat("Instance of List expected.", executor.invokeAll(null, timeout, unit), IsInstanceOf.instanceOf(List.class));
    }

    @Test(expected = NullPointerException.class)
    public void invokeAllTasksTimeoutAndUnitNullIdTest() throws InterruptedException {
        Collection<Callable<Object>> tasks = new ArrayList<>();
        tasks.add(Mockito.mock(Callable.class));
        tasks.add(Mockito.mock(Callable.class));
        long timeout = 1L;
        Assert.assertThat("Instance of List expected.", executor.invokeAll(tasks, timeout, null), IsInstanceOf.instanceOf(List.class));
    }

    @Test
    public void invokeAnyTasksIdTest() throws ExecutionException, InterruptedException {
        Collection<Callable<Object>> tasks = new ArrayList<>();
        tasks.add(Mockito.mock(Callable.class));
        tasks.add(Mockito.mock(Callable.class));
        Assert.assertEquals("Actual and expected values are not the same", executor.invokeAny(tasks), executorService.invokeAny(tasks));
    }

    @Test
    public void invokeAnyTasksTimeoutAndUnitIdTest() throws InterruptedException, ExecutionException, TimeoutException {
        Collection<Callable<Object>> tasks = new ArrayList<>();
        tasks.add(Mockito.mock(Callable.class));
        tasks.add(Mockito.mock(Callable.class));
        long timeout = 1L;
        TimeUnit unit = TimeUnit.MINUTES;
        Assert.assertEquals("Actual and expected values are not the same", executor.invokeAny(tasks, timeout, unit), executorService.invokeAny(tasks, timeout, unit));
    }

    @Test(expected = NullPointerException.class)
    public void invokeAnyTasksTimeoutAndUnitNullIdTest() throws InterruptedException, ExecutionException, TimeoutException {
        Collection<Callable<Object>> tasks = new ArrayList<>();
        tasks.add(Mockito.mock(Callable.class));
        tasks.add(Mockito.mock(Callable.class));
        long timeout = 1L;
        Assert.assertEquals("Actual and expected values are not the same", executor.invokeAny(tasks, timeout, null), executorService.invokeAny(tasks, timeout, null));
    }
}