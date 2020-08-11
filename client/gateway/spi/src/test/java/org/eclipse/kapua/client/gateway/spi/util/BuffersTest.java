/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others.
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

import java.nio.ByteBuffer;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class BuffersTest {

    @Test
    public void test1() {
        final ByteBuffer result = Buffers.wrap(new byte[0]);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.hasRemaining());
    }

    @Test
    public void test2() {
        final ByteBuffer result = Buffers.wrap(null);
        Assert.assertNull(result);
    }

    @Test
    public void test3() {
        final ByteBuffer result = Buffers.wrap(new byte[] { 12 });
        Assert.assertNotNull(result);
        result.flip();
        Assert.assertEquals(1, result.remaining());
        Assert.assertEquals(12, result.get());
    }

    @Test
    public void test4() {
        final ByteBuffer input = ByteBuffer.allocate(1);
        input.put((byte) 12);

        input.flip();

        final byte[] result = Buffers.toByteArray(input);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.length);
        Assert.assertEquals(12, result[0]);
    }

    @Test
    public void test5() {
        final byte[] result = Buffers.toByteArray(null);
        Assert.assertNull(result);
    }

}
