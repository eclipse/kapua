/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.client.gateway.mqtt.fuse.internal;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.fusesource.mqtt.client.Callback;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.concurrent.CompletableFuture;


@Category(JUnitTests.class)
public class CallbacksTest {

    @Test
    public void callbacksTest() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<Callbacks> constructor = Callbacks.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void asCallbackTest() {
        final CompletableFuture<Object> future = Mockito.mock(CompletableFuture.class);
        Throwable value = Mockito.mock(Throwable.class);
        Callback<Object> callbacks = Callbacks.asCallback(future);
        callbacks.onSuccess(future);
        callbacks.onFailure(value);
        Mockito.verify(future).complete(future);
        Mockito.verify(future).completeExceptionally(value);
    }

    @Test(expected = NullPointerException.class)
    public void asCallbackFutureNullTest() {
        Callback<Object> callbacks = Callbacks.asCallback(null);
        callbacks.onSuccess(null);
    }

    @Test
    public void asCallbackThrowableNullTest() {
        final CompletableFuture<Object> future = Mockito.mock(CompletableFuture.class);
        Callback<Object> callbacks = Callbacks.asCallback(future);
        callbacks.onSuccess(future);
        callbacks.onFailure(null);
        Mockito.verify(future).complete(future);
        Mockito.verify(future).completeExceptionally(null);
    }
}