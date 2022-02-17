/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.router;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.eclipse.kapua.broker.core.message.MessageConstants;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

@Category(JUnitTests.class)
public class SimpleEndPointTest extends Assert {

    SimpleEndPoint simpleEndPoint;
    Exchange exchange;
    Object[] values;
    String[] previousList;
    Map<String, Object> properties;
    Message message;
    StringBuffer stringBuffer;
    String[] prefix;
    String[] endPoints;
    String[] regexList;

    @Before
    public void initialize() {
        simpleEndPoint = new SimpleEndPoint();
        exchange = Mockito.mock(Exchange.class);
        values = new Object[]{null, true, false, 'c', "String", 10, 10L, 10.11f, 10.11d, 1, 0};
        previousList = new String[]{"", "Previous!#", "Previous#<> 123456-7890", "PrEviouS-1pr23", "pR-12!viouS", "Previous!123#", "() _ + ?><|/.Previous!@#$ % ^&*"};
        properties = new HashMap<>();
        message = Mockito.mock(Message.class);
        stringBuffer = new StringBuffer();
        prefix = new String[]{"Prefix", "pre-fix", "123 prefix", "pre123fi_X21", "!PRE789fix", "PR#$"};
        endPoints = new String[]{"", "endpoint", "endpoint 123", "123456_789-0", "!@#$%^&*()_+,.<>|", "end-point123", "!@eNd_poiNT", "end123Point()"};
        regexList = new String[]{"", "Regex", "regex123", "regex-123", "rE_geX789", "!@#$%^&*()_|<>/", "re\rgex", "re\\g-123", "r\tgex", "reg\fex", "re gex", "", "re\bgex", " re\1gex", " r\2Egex", "\3regex, regex\4, r\5egex, r\6egex, reg\7ex, r\0egex", "re\\gex", "re\tge", "reg\nex1234567890", "!@#$%^&*()_+=-/.,?><|:;'|", "\'"};
    }

    @Test
    public void matchesEmptyPropertiesTest() {
        for (Object value : values) {
            for (String previous : previousList) {
                assertFalse("False expected.", simpleEndPoint.matches(exchange, value, previous, properties));
            }
        }
    }

    @Test
    public void matchesTest() {
        properties.put("key1", "value");
        properties.put("key2", 10);
        for (Object value : values) {
            for (String previous : previousList) {
                assertFalse("False expected.", simpleEndPoint.matches(exchange, value, previous, properties));
            }
        }
    }

    @Test
    public void matchesNullExchangeTest() {
        for (Object value : values) {
            for (String previous : previousList) {
                assertFalse("False expected.", simpleEndPoint.matches(null, value, previous, properties));
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void matchesNullPreviousTest() {
        for (Object value : values) {
            Mockito.when(exchange.getIn()).thenReturn(message);
            Mockito.when(message.getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class)).thenReturn("Topic");
            simpleEndPoint.matches(exchange, value, null, properties);
        }
    }

    @Test
    public void matchesNullPreviousFalseTest() {
        simpleEndPoint.setRegex("Different topic");
        for (Object value : values) {
            Mockito.when(exchange.getIn()).thenReturn(message);
            Mockito.when(message.getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class)).thenReturn("Topic");
            assertFalse("False expected.", simpleEndPoint.matches(exchange, value, null, properties));
        }
    }

    @Test
    public void matchesNullPreviousTrueTest() {
        simpleEndPoint.setRegex("Topic");
        for (Object value : values) {
            Mockito.when(exchange.getIn()).thenReturn(message);
            Mockito.when(message.getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class)).thenReturn("Topic");
            assertTrue("True expected.", simpleEndPoint.matches(exchange, value, null, properties));
        }
    }

    @Test
    public void matchesNullPropertiesTest() {
        for (Object value : values) {
            for (String previous : previousList) {
                assertFalse("False expected.", simpleEndPoint.matches(exchange, value, previous, null));
            }
        }
    }

    @Test
    public void setAndGetEndPointWithParametersTest() {
        for (Object value : values) {
            for (String previous : previousList) {
                simpleEndPoint.setEndPoint("End Point");
                assertEquals("Expected and actual values should be the same.", "End Point", simpleEndPoint.getEndPoint(exchange, value, previous, properties));
                simpleEndPoint.setEndPoint(null);
                assertNull("Null expected.", simpleEndPoint.getEndPoint(exchange, value, previous, properties));
            }
        }
    }

    @Test
    public void setAndGetEndPointWithNullParametersTest() {
        for (String endpoint : endPoints) {
            simpleEndPoint.setEndPoint(endpoint);
            assertEquals("Expected and actual values should be the same.", endpoint, simpleEndPoint.getEndPoint(null, null, null, null));
        }
        simpleEndPoint.setEndPoint(null);
        assertNull("Null expected.", simpleEndPoint.getEndPoint(null, null, null, null));
    }

    @Test
    public void setAndGetEndPointWithNullExchangeParameterTest() {
        for (Object value : values) {
            for (String previous : previousList) {
                for (String endpoint : endPoints) {
                    simpleEndPoint.setEndPoint(endpoint);
                    assertEquals("Expected and actual values should be the same.", endpoint, simpleEndPoint.getEndPoint(null, value, previous, properties));
                }
                simpleEndPoint.setEndPoint(null);
                assertNull("Null expected.", simpleEndPoint.getEndPoint(null, value, previous, properties));
            }
        }
    }

    @Test
    public void setAndGetEndPointWithNullPreviousParameterTest() {
        for (Object value : values) {
            for (String endpoint : endPoints) {
                simpleEndPoint.setEndPoint(endpoint);
                assertEquals("Expected and actual values should be the same.", endpoint, simpleEndPoint.getEndPoint(exchange, value, null, properties));
            }
            simpleEndPoint.setEndPoint(null);
            assertNull("Null expected.", simpleEndPoint.getEndPoint(exchange, value, null, properties));
        }
    }

    @Test
    public void setAndGetEndPointWithNullPropertiesParameterTest() {
        for (Object value : values) {
            for (String previous : previousList) {
                for (String endpoint : endPoints) {
                    simpleEndPoint.setEndPoint(endpoint);
                    assertEquals("Expected and actual values should be the same.", endpoint, simpleEndPoint.getEndPoint(exchange, value, previous, null));
                }
                simpleEndPoint.setEndPoint(null);
                assertNull("Null expected.", simpleEndPoint.getEndPoint(exchange, value, previous, null));
            }
        }
    }

    @Test
    public void setAndGetRegexTest() {
        assertNull("Null expected.", simpleEndPoint.getRegex());

        for (String regex : regexList) {
            simpleEndPoint.setRegex(regex);
            assertEquals("Expected and actual values should be the same.", regex, simpleEndPoint.getRegex());
        }
        simpleEndPoint.setRegex(null);
        assertNull("Null expected.", simpleEndPoint.getRegex());
    }

    @Test
    public void setAndGetEndPointTest() {
        for (String endPoint : endPoints) {
            simpleEndPoint.setEndPoint(endPoint);
            assertEquals("Expected and actual values should be the same.", endPoint, simpleEndPoint.getEndPoint());
        }
        simpleEndPoint.setEndPoint(null);
        assertNull("Null expected.", simpleEndPoint.getEndPoint());
    }

    @Test
    public void toLogTest() {
        try {
            for (String prefixValue : prefix) {
                simpleEndPoint.toLog(stringBuffer, prefixValue);
                assertEquals("Expected and actual values should be the same.", "Regex: null\n" + prefixValue + "\tEnd point: null", stringBuffer.toString());
                stringBuffer.delete(0, 99);
            }
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void toLogWithRegexAndEndPointTest() {
        simpleEndPoint.setEndPoint("ENDPOINT");
        simpleEndPoint.setRegex("REGEX");
        try {
            for (String prefixValue : prefix) {
                simpleEndPoint.toLog(stringBuffer, prefixValue);
                assertEquals("Expected and actual values should be the same.", "Regex: REGEX\n" + prefixValue + "\tEnd point: ENDPOINT", stringBuffer.toString());
                stringBuffer.delete(0, 99);
            }
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test(expected = NullPointerException.class)
    public void toLogNullBufferTest() {
        for (String prefixValue : prefix) {
            simpleEndPoint.toLog(null, prefixValue);
        }
    }

    @Test
    public void toLogNullPrefixTest() {
        try {
            simpleEndPoint.toLog(stringBuffer, null);
            assertEquals("Expected and actual values should be the same.", "Regex: null\n" + null + "\tEnd point: null", stringBuffer.toString());
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }
}