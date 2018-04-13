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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

public class TopicTest {

    @Test
    public void test1() {
        final Topic topic = Topic.of("foo");
        Assert.assertEquals(Arrays.asList("foo"), topic.getSegments());
    }

    @Test
    public void test2() {
        final Topic topic = Topic.of("foo", "bar");
        Assert.assertEquals(Arrays.asList("foo", "bar"), topic.getSegments());
    }

    @Test
    public void test3() {
        final Topic topic = Topic.of(new LinkedList<>(Arrays.asList("foo", "bar")));
        Assert.assertEquals(Arrays.asList("foo", "bar"), topic.getSegments());
    }

    @Test
    public void testEmpty1() {
        final Topic topic = Topic.of((List<String>) null);
        Assert.assertNull(topic);
    }

    @Test
    public void testEmpty2() {
        final Topic topic = Topic.of(Collections.emptyList());
        Assert.assertNull(topic);
    }

    @Test
    public void testEmpty3() {
        final Topic topic = Topic.of((String) null);
        Assert.assertNull(topic);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmpty4() {
        Topic.of("foo", "bar", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpecial1() {
        Topic.of("foo", "#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpecial2() {
        Topic.of("foo", "+");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpecial3() {
        Topic.of("foo", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpecial4() {
        Topic.of("foo", "foo/bar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpecial4a() {
        Topic.of("foo", "/bar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpecial4b() {
        Topic.of("foo", "foo/");
    }

    @Test
    public void testSplit1() {
        final Topic topic = Topic.split("foo/bar");
        Assert.assertEquals(Arrays.asList("foo", "bar"), topic.getSegments());
    }

    @Test
    public void testSplit2() {
        final Topic topic = Topic.split("foo//bar");
        Assert.assertEquals(Arrays.asList("foo", "bar"), topic.getSegments());
    }

    @Test
    public void testSplit3() {
        final Topic topic = Topic.split("/foo//bar");
        Assert.assertEquals(Arrays.asList("foo", "bar"), topic.getSegments());
    }

    @Test
    public void testSplit4() {
        final Topic topic = Topic.split("/foo//bar//");
        Assert.assertEquals(Arrays.asList("foo", "bar"), topic.getSegments());
    }

    @Test
    public void testSplit5() {
        final Topic topic = Topic.split("//");
        Assert.assertNull(topic);
    }

    @Test
    public void testSplitNull() {
        final Topic topic = Topic.split(null);
        Assert.assertNull(topic);
    }

    @Test
    public void testSplitEmpty() {
        final Topic topic = Topic.split("");
        Assert.assertNull(topic);
    }

    @Test
    public void testEquals1() {
        final Topic t1 = Topic.split("foo/bar/baz");
        final Topic t2 = Topic.split("foo/bar/baz");
        Assert.assertEquals(t1, t2);
    }

    @Test
    public void testEquals2() {
        final Topic t1 = Topic.split("foo/bar/baz");
        final Topic t2 = Topic.of("foo", "bar", "baz");
        Assert.assertEquals(t1, t2);
    }

    @Test
    public void testNotEquals1() {
        final Topic t1 = Topic.split("foo/bar");
        final Topic t2 = Topic.split("foo/baz");
        Assert.assertNotEquals(t1, t2);
    }

    @Test
    public void testStream() {
        final String result = Topic.split("foo/bar").stream().collect(Collectors.joining());
        Assert.assertEquals("foobar", result);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("foo/bar", Topic.of("foo", "bar").toString());
    }

    @Test
    public void testHashCode() {
        final Map<Topic, Object> map = new HashMap<>();
        map.put(Topic.of("foo", "bar"), 1);
        map.put(Topic.of("foo", "baz"), 2);

        Assert.assertEquals(1, map.get(Topic.split("foo/bar")));
        Assert.assertEquals(2, map.get(Topic.split("foo/baz")));
    }

}
