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
package org.eclipse.kapua.client.gateway.kura;

import java.nio.ByteBuffer;
import java.util.Collections;

import org.eclipse.kapua.client.gateway.Payload;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PayloadCodecTest {

    private KuraBinaryPayloadCodec codec;

    @Before
    public void setup() {
        final KuraBinaryPayloadCodec.Builder builder = new KuraBinaryPayloadCodec.Builder();
        codec = builder.build();
    }

    @Test(expected = NullPointerException.class)
    public void testEncodeNull1() throws Exception {
        codec.encode(null, null);
    }

    @Test
    public void testEncodeEmpty1() throws Exception {
        final ByteBuffer result = codec.encode(Payload.of(Collections.emptyMap()), null);
        Assert.assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testDecodeNull1() throws Exception {
        codec.decode(null);
    }

    @Test
    public void testDecode1() throws Exception {
        final ByteBuffer buffer = codec.encode(Payload.of(Collections.emptyMap()), null);
        buffer.flip();

        final Payload payload = codec.decode(buffer);

        Assert.assertNotNull(payload);
        Assert.assertTrue(payload.getValues().isEmpty());
    }

    @Test
    public void testDecode2() throws Exception {
        processCodecTwo(null);
    }

    @Test
    public void testDecode3() throws Exception {
        processCodecTwo(ByteBuffer.allocate(100));
    }

    private void processCodecTwo(ByteBuffer buffer) throws Exception {
        buffer = codec.encode(Payload.of("foo", 1), buffer);
        buffer = codec.encode(Payload.of("bar", 2), buffer);

        buffer.flip();

        final ByteBuffer buffer1 = buffer.slice();
        buffer1.limit(20);
        final Payload payload1 = codec.decode(buffer1);

        final ByteBuffer buffer2 = buffer.slice();
        buffer2.position(20);
        final Payload payload2 = codec.decode(buffer2);

        Assert.assertNotNull(payload1);
        Assert.assertNotNull(payload2);

        Assert.assertEquals(Collections.singletonMap("foo", 1), payload1.getValues());
        Assert.assertEquals(Collections.singletonMap("bar", 2), payload2.getValues());
    }
}
