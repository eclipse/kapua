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
package org.eclipse.kapua.client.gateway;

import java.time.Instant;
import java.util.Collections;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class PayloadTest {

    @Test(expected = NullPointerException.class)
    public void testNull1() {
        Payload.of((String) null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNull2() {
        Payload.of(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNull3() {
        Payload.of(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNull4() {
        Payload.of((Instant) null, null);
    }

    @Test
    public void test1() {
        Payload p = Payload.of("foo", 1);

        Assert.assertNotNull(p);
        Assert.assertEquals(1, p.getValues().size());
        Assert.assertEquals(1, p.getValues().get("foo"));
        Assert.assertTrue(!Instant.now().isBefore(p.getTimestamp()));
    }

    @Test
    public void test2() {
        Payload p = Payload.of(Collections.singletonMap("foo", 1));

        Assert.assertNotNull(p);
        Assert.assertEquals(1, p.getValues().size());
        Assert.assertEquals(1, p.getValues().get("foo"));
        Assert.assertTrue(!Instant.now().isBefore(p.getTimestamp()));
    }

    @Test
    public void test3() {
        Payload p = Payload.of(Instant.now(), "foo", 1);

        Assert.assertNotNull(p);
        Assert.assertEquals(1, p.getValues().size());
        Assert.assertEquals(1, p.getValues().get("foo"));
        Assert.assertTrue(!Instant.now().isBefore(p.getTimestamp()));
    }

    @Test
    public void test4() {
        Payload p = Payload.of(Instant.now(), Collections.singletonMap("foo", 1));

        Assert.assertNotNull(p);
        Assert.assertEquals(1, p.getValues().size());
        Assert.assertEquals(1, p.getValues().get("foo"));
        Assert.assertTrue(!Instant.now().isBefore(p.getTimestamp()));
    }

    @Test
    public void testBuilder1() {
        final Payload p = new Payload.Builder().timestamp(Instant.now()).put("foo", 1).build();

        Assert.assertNotNull(p);
        Assert.assertEquals(1, p.getValues().size());
        Assert.assertEquals(1, p.getValues().get("foo"));
        Assert.assertTrue(!Instant.now().isBefore(p.getTimestamp()));
    }

    @Test(expected = NullPointerException.class)
    public void testBuilder2() {
        new Payload.Builder().timestamp(null).build();
    }

    @Test(expected = NullPointerException.class)
    public void testBuilder3() {
        new Payload.Builder().values(null).build();
    }

    @Test(expected = NullPointerException.class)
    public void testBuilder4() {
        new Payload.Builder().put(null, null).build();
    }

    @Test
    public void testBuilder5() {
        final Payload p = new Payload.Builder().timestamp(Instant.now()).values(Collections.singletonMap("foo", 1)).build();

        Assert.assertNotNull(p);
        Assert.assertEquals(1, p.getValues().size());
        Assert.assertEquals(1, p.getValues().get("foo"));
        Assert.assertTrue(!Instant.now().isBefore(p.getTimestamp()));
    }
}
