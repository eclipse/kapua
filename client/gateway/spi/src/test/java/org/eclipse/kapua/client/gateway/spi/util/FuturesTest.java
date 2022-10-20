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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;


@Category(JUnitTests.class)
public class FuturesTest {

    @Test
    public void futuresTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<Futures> constructor = Futures.class.getDeclaredConstructor();
        Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test(expected = NullPointerException.class)
    public void completedExceptionallyThrowableNullTest() {
        Assert.assertThat("Instance of CompletableFuture expected", Futures.completedExceptionally(null), IsInstanceOf.instanceOf(CompletableFuture.class));
    }

    @Test
    public void completedExceptionallyTest() {
        Throwable error = new Throwable();
        Assert.assertThat("Instance of CompletableFuture expected", Futures.completedExceptionally(error), IsInstanceOf.instanceOf(CompletableFuture.class));
    }

    @Test(expected = InvocationTargetException.class)
    public void reportToTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Futures.class.getDeclaredMethod("reportTo", CompletionStage.class, CompletableFuture.class);
        method.setAccessible(true);
        CompletionStage<String> completionStageNull = (CompletionStage<String>) method.invoke(null, null, null);
        Assert.assertNull(completionStageNull);
    }
}