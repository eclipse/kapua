/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class TopicTest extends Assert {

    String[] stringValues = {"!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", "regularNaming", "regular Naming", "49", "regularNaming49", "NAMING", "246465494135646120009090049684646496468456468496846464968496844"};

    @Test
    public void topicOfOneParameterValidTest() {
        for (String value : stringValues) {
            Topic topic = Topic.of(new LinkedList<>(Arrays.asList(value, value)));
            assertEquals("Expected and actual values should be the same!", Arrays.asList(value, value), topic.getSegments());
        }
    }

    @Test
    public void topicOfOneParameterNullTest() {
        Topic topic = Topic.of(null);
        assertNull("Null expected!", topic);
    }

    @Test
    public void topicOfTwoParameterEmptyTest() {
        Topic topic = Topic.of(Collections.emptyList());
        assertNull("Null expected!", topic);
    }

    @Test
    public void topicOfTwoParameterValidTest() {
        for (String value : stringValues) {
            Topic topic = Topic.of(value);
            assertEquals("Expected and actual values should be the same!", Collections.singletonList(value), topic.getSegments());
        }
        for (String value : stringValues) {
            Topic topic = Topic.of(value, value);
            assertEquals("Expected and actual values should be the same!", Arrays.asList(value, value), topic.getSegments());
        }
    }

    @Test
    public void topicOfTwoParameterNullTest() {
        Topic topic = Topic.of(null);
        assertNull("Null expected!", topic);
        for (String value : stringValues) {
            Topic topic2 = Topic.of(null, value);
            assertNull("Null expected!", topic2);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void topicOfTwoParameterSecondNullTest() {
        Topic.of("foo", "bar", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureNotSpecialHashSymbolTest() {
        Topic.of("foo", "#");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureNotSpecialPlusSymbolTest() {
        Topic.of("foo", "+");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureNotSpecialEmptyStringTest() {
        Topic.of("foo", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureNotSpecialSlashSymbolTest() {
        Topic.of("foo", "foo/bar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureNotSpecialForwardSlashSymbolTest() {
        Topic.of("foo", "/bar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureNotSpecialTrailingSlashSymbolTest() {
        Topic.of("foo", "foo/");
    }

    @Test
    public void splitValidTest() {
        for (String value : stringValues) {
            Topic topic = Topic.split(value + "/" + value);
            assertEquals("Expected and actual values should be the same!", Arrays.asList(value, value), topic.getSegments());
        }
        for (String value : stringValues) {
            Topic topic = Topic.split(value + "//" + value);
            assertEquals("Expected and actual values should be the same!", Arrays.asList(value, value), topic.getSegments());
        }
        for (String value : stringValues) {
            Topic topic = Topic.split("/" + value + "//" + value);
            assertEquals("Expected and actual values should be the same!", Arrays.asList(value, value), topic.getSegments());
        }
        for (String value : stringValues) {
            Topic topic = Topic.split("/" + value + "//" + value + "//");
            assertEquals("Expected and actual values should be the same!", Arrays.asList(value, value), topic.getSegments());
        }
    }

    @Test
    public void splitValidTestNullOutcomeTest() {
        Topic topic = Topic.split("//");
        assertNull("Null expected!", topic);
    }

    @Test
    public void splitNullTest() {
        Topic topic = Topic.split(null);
        assertNull("Null expected!", topic);
    }

    @Test
    public void splitEmptyTest() {
        Topic topic = Topic.split("");
        assertNull("Null expected!", topic);
    }

    @Test
    public void splitTest() {
        Topic topic1 = Topic.split("foo/bar/baz");
        Topic topic2 = Topic.split("foo/bar/baz");
        assertEquals("Expected and actual values should be the same!", topic1, topic2);
    }

    @Test
    public void splitAndOfTest() {
        Topic topic1 = Topic.split("foo/bar/baz");
        Topic topic2 = Topic.of("foo", "bar", "baz");
        assertEquals("Expected and actual values should be the same!", topic1, topic2);
    }

    @Test
    public void splitInvalidTest() {
        Topic topic1 = Topic.split("foo/bar");
        Topic topic2 = Topic.split("foo/baz");
        assertNotEquals("Expected and actual values should be the same!", topic1, topic2);
    }

    @Test
    public void streamValidTest() {
        String result = Topic.split("foo/bar").stream().collect(Collectors.joining());
        assertEquals("Expected and actual values should be the same!", "foobar", result);
    }

    @Test
    public void toStringTest() {
        assertEquals("Expected and actual values should be the same!", "foo/bar", Topic.of("foo", "bar").toString());
    }

    @Test
    public void hashCodeTest() {
        Map<Topic, Object> map = new HashMap<>();
        map.put(Topic.of("foo", "bar"), 1);
        map.put(Topic.of("foo", "baz"), 2);

        assertEquals("Expected and actual values should be the same!", 1, map.get(Topic.split("foo/bar")));
        assertEquals("Expected and actual values should be the same!", 2, map.get(Topic.split("foo/baz")));
    }

    @Test
    public void equalsTest() {
        Topic topic1 = Topic.split("String");
        Topic topic2 = Topic.split("String");
        Topic topic3 = Topic.split("StringString");
        Object object = new Object();

        assertTrue("True expected!",topic1.equals(topic1));
        assertFalse("False expected!", topic1.equals(null));
        assertFalse("False expected!", topic1.equals(object));
        assertTrue("True expected!", topic1.equals(topic2));
        assertFalse("False expected!", topic1.equals(topic3));
    }
}