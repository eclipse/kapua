/*******************************************************************************
 * Copyright (c) 2017, 2021 Red Hat Inc and others.
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
import java.nio.ByteBuffer;

@Category(JUnitTests.class)
public class BuffersTest extends Assert {

    @Test
    public void buffersTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<Buffers> constructor = Buffers.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void wrapByteArrayDataNullTest() {
        assertEquals("Expected and actual value should be the same", null, Buffers.wrap(null));
    }

    @Test
    public void wrapTest() {
        final byte[] byteArray = new byte[]{1, 3, 5, 7, 9};
        final ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        byteBuffer.position(byteBuffer.limit());
        assertEquals("Expected and actual value should be the same", byteBuffer, Buffers.wrap(byteArray));
    }

    @Test
    public void toByteArrayBufferNullTest() {
        assertEquals("Expected and actual value should be the same", null, Buffers.toByteArray(null));
    }

    @Test
    public void toByteArrayTest() {
        ByteBuffer byteBuffer = Mockito.mock(ByteBuffer.class);
        assertThat("Instance of Buffers expected.", Buffers.toByteArray(byteBuffer), IsInstanceOf.instanceOf(byte[].class));
    }
}