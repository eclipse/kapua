/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ValueTokenizerTest extends Assert {

    @Test
    public void valueTokenizerNullStringTest() throws Exception {
        try {
            assertNotNull(new ValueTokenizer(null));
        } catch (Exception ex) {
            fail("Exception not expected for parameter");
        }
    }

    @Test
    public void valueTokenizerNotNullTest() throws Exception {
        try {
            assertNotNull(new ValueTokenizer("valueTokenizerConstructor"));
        } catch (Exception ex){
            fail("No exception expected");
        }
    }

    //tests for method ...getValuesAsString();
    @Test
    public void valueTokenizerDelimeterTest() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer("a123,aaa,rrr");
        try {
            assertEquals("a123,aaa,rrr", valueTokenizer.getValuesAsString());
        } catch (Exception ex){
            fail("Failed to delimit input string");
        }
    }

    @Test
    public void valueTokenizerEscapeTest() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer("a123\\aaa\\rrr");
        try {
            assertEquals("a123aaarrr", valueTokenizer.getValuesAsString());
        } catch (Exception ex){
            fail("Failed to load escape string");
        }
    }

    @Test
    public void valueTokenizerEscapeAtEndTest() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer("a123aaarrr\\");
        try {
            assertEquals("a123aaarrr", valueTokenizer.getValuesAsString());
        } catch (Exception ex){
            fail("Failed to load escape string");
        }
    }

    @Test
    public void valueTokenizerEscapeAtBeginningTest() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer("\\a123aaarrr");
        try {
            valueTokenizer.getValuesAsString();
            assertEquals("a123aaarrr", valueTokenizer.getValuesAsString());
        } catch (Exception ex){
            fail("Failed to load escape string");
        }
    }

    @Test
    public void valueTokenizerSpaceAtBeginningTest() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer(" a123aaarrr");
        try {
            assertEquals("a123aaarrr", valueTokenizer.getValuesAsString());
        } catch (Exception ex){
            fail("Cannot load string whit white space at the beginning");
        }
    }

    @Test
    public void valueTokenizerSpaceAtTheEndTest() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer("a123aaarrr ");
        try {
            assertEquals("a123aaarrr", valueTokenizer.getValuesAsString());
        } catch (Exception ex){
            fail("Cannot load string whit white space at the end");
        }
    }

    @Test
    public void valueTokenizerValuesAsStringNullTest() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer(null);
        try {
            assertNull(valueTokenizer.getValuesAsString());
        } catch (Exception ex) {
            fail("The value is not NULL");
        }
    }

    @Test
    public void valueTokenizerValueAStringSpaceMiddleTest() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer("a 123");
        try {
            assertEquals("a 123", valueTokenizer.getValuesAsString());
        } catch (Exception ex) {
            fail("Cannot convert value to string");
        }
    }

    //tests for method ...getValuesAsArray();
    @Test
    public void valueTokenizerValuesAsArrayWithDelimeter() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer("a,123,aaa,rrr");
        String [] expectedValues = new String[]{"a", "123", "aaa", "rrr"};
        try {
            assertArrayEquals(expectedValues, valueTokenizer.getValuesAsArray());
        } catch (Exception ex) {
            fail("Arrays are not equal");
        }
    }

    @Test
    public void valueTokenizerValuesAsArrayWithEscape() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer("a123\\aaarr\\r");
        String [] expectedValues = new String[]{"a123aaarrr"};
        try {
            assertArrayEquals(expectedValues, valueTokenizer.getValuesAsArray());
        } catch (Exception ex) {
            fail("Arrays are not equal");
        }
    }

    @Test
    public void valueTokenizerValuesAsArrayWithNullString() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer(null);
        try {
            assertNull(valueTokenizer.getValuesAsArray());
        } catch (Exception ex) {
            fail("Arrays are not equal");
        }
    }

    @Test
    public void valueTokenizerValueAsArrayDelimiterAndEscape() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer("a12\\3,a\\,aa,r\\rr,");
        String [] expectedValues = new String[] {"a123", "a,aa", "rrr", ""};
        try {
            assertArrayEquals(expectedValues,valueTokenizer.getValuesAsArray());
        } catch (Exception ex) {
            fail("Arrays are not equal");
        }
    }

    @Test
    public void valueTokenizerValueAsArray() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer(" a123, aaa, rrr");
        String [] expectedValues = new String[] {"a123", "aaa", "rrr"};
        try {
            assertArrayEquals(expectedValues,valueTokenizer.getValuesAsArray());
        } catch (Exception ex) {
            fail("Arrays are not equal");
        }
    }

     //test for method ...getValues();
    @Test
    public void valueTokenizerGetValuesWithDelimeter() throws Exception {
        ValueTokenizer valueTokenizer = new ValueTokenizer("a123,aaarrr");
        String [] expectedValues = new String[] {"a123", "aaarrr"};
        try {
            assertArrayEquals(expectedValues, valueTokenizer.getValues().toArray());
        } catch (Exception ex) {
            fail("Arrays are not equal");
        }
    }
}
