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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Category(JUnitTests.class)
public class ParentEndPointTest extends Assert {

    ParentEndPoint parentEndPoint;
    Exchange exchange;
    Object[] values;
    String[] previousList;
    Map<String, Object> properties;
    Message message;
    List<EndPoint> endPoints;
    StringBuffer stringBuffer;
    String[] prefixValue;

    @Before
    public void initialize() {
        parentEndPoint = new ParentEndPoint();
        exchange = Mockito.mock(Exchange.class);
        values = new Object[]{null, true, false, 'c', "String", 10, 10L, 10.11f, 10.11d, 1, 0};
        previousList = new String[]{"", "Previous", "Pre12345vious67890", "PrEviouS-1pr23", "pR-12!viouS", "Previous!123#", "() _ + ?><|/.Pre99vious!@#$ % ^&*"};
        properties = new HashMap<>();
        message = Mockito.mock(Message.class);
        endPoints = new ArrayList<>();
        stringBuffer = new StringBuffer();
        prefixValue = new String[]{"", "Prefix", "pre-fix", "123 prefix", "pre123fi_X21", "!PRE789fix", "PR#$"};
    }

    @Test
    public void matchesTest() {
        for (Object value : values) {
            for (String previous : previousList) {
                assertFalse("False expected.", parentEndPoint.matches(exchange, value, previous, properties));
            }
        }
    }

    @Test
    public void matchesNullExchangeTest() {
        for (Object value : values) {
            for (String previous : previousList) {
                assertFalse("False expected.", parentEndPoint.matches(null, value, previous, properties));
            }
        }
    }

    @Test
    public void matchesNullPropertiesTest() {
        for (Object value : values) {
            for (String previous : previousList) {
                assertFalse("False expected.", parentEndPoint.matches(exchange, value, previous, null));
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void matchesNullPreviousTest() {
        for (Object value : values) {
            Mockito.when(exchange.getIn()).thenReturn(message);
            Mockito.when(message.getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class)).thenReturn("Topic");
            assertFalse("False expected.", parentEndPoint.matches(exchange, value, null, properties));
        }
    }

    @Test
    public void matchesNullPreviousFalseTest() {
        parentEndPoint.setRegex("Different topic");
        for (Object value : values) {
            Mockito.when(exchange.getIn()).thenReturn(message);
            Mockito.when(message.getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class)).thenReturn("Topic");
            assertFalse("False expected.", parentEndPoint.matches(exchange, value, null, properties));
        }
    }

    @Test
    public void matchesNullPreviousTrueTest() {
        parentEndPoint.setRegex("Topic");
        for (Object value : values) {
            Mockito.when(exchange.getIn()).thenReturn(message);
            Mockito.when(message.getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class)).thenReturn("Topic");
            assertTrue("True expected.", parentEndPoint.matches(exchange, value, null, properties));
        }
    }

    @Test
    public void setAndGetEndPointTest() {
        EndPoint endPoint = new EndChainEndPoint();
        endPoints.add(endPoint);
        parentEndPoint.setEndPoints(endPoints);
        for (Object value : values) {
            for (String previous : previousList) {
                //COMMENT: method getEndPoint(Exchange exchange, Object value, String previous, Map<String, Object> properties) {
                //      in EndChainEndPoint class always returns null
                assertNull("Null expected.", parentEndPoint.getEndPoint(exchange, value, previous, properties));
            }
        }
    }

    @Test
    public void setAndGetEndPointFalseMatchesTest() {
        EndPoint endPoint = Mockito.mock(EndPoint.class);
        endPoints.add(endPoint);
        parentEndPoint.setEndPoints(endPoints);
        for (Object value : values) {
            for (String previous : previousList) {
                Mockito.when((endPoint.matches(exchange, value, previous, properties))).thenReturn(false);
                assertNull("Null expected.", parentEndPoint.getEndPoint(exchange, value, previous, properties));
            }
        }
    }

    @Test
    public void setAndGetEndPointsTest() {
        List<EndPoint> endPoints = new ArrayList<>();
        EndPoint endPoint = new EndChainEndPoint();

        parentEndPoint.setEndPoints(endPoints);

        assertEquals("Expected and actual values should be the same.", endPoints, parentEndPoint.getEndPoints());
        assertTrue("True expected.", parentEndPoint.getEndPoints().isEmpty());

        endPoints.add(endPoint);
        parentEndPoint.setEndPoints(endPoints);
        assertEquals("Expected and actual values should be the same.", endPoints, parentEndPoint.getEndPoints());
        assertFalse("False expected.", parentEndPoint.getEndPoints().isEmpty());

        parentEndPoint.setEndPoints(null);
        assertNull("Null expected.", parentEndPoint.getEndPoints());
    }

    @Test
    public void setAndGetRegexTest() {
        String[] regexValues = {null, "\\", "re!12#gex", "re gex", "", "re\bg-12ex", " !@#re\1gex", " r\2Ege|,.x", "\3rege123x, re-gex\4, r\5egex, r\6egex, reg\7ex, r\0ege_x", "1234re\\gex", "re\tge", "reg\nex123<>4567_", "\'"};

        assertNull("Null expected.", parentEndPoint.getRegex());

        for (String regex : regexValues) {
            parentEndPoint.setRegex(regex);
            assertEquals("Expected and actual values should be the same.", regex, parentEndPoint.getRegex());
        }
        parentEndPoint.setRegex(null);
        assertNull("Null expected.", parentEndPoint.getRegex());
    }

    @Test(expected = NullPointerException.class)
    public void toLogEndPointsNotSetTest() {
        for (String prefix : prefixValue) {
            parentEndPoint.toLog(stringBuffer, prefix);
        }
    }

    @Test
    public void toLogTest() {
        parentEndPoint.setRegex("Regex");
        List<EndPoint> endPoints = new ArrayList<>();
        EndPoint endPoint = new EndChainEndPoint();
        endPoints.add(endPoint);
        parentEndPoint.setEndPoints(endPoints);
        try {
            for (String prefix : prefixValue) {
                parentEndPoint.toLog(stringBuffer, prefix);
                assertEquals("Expected and actual values should be the same.", prefix + "Regex: Regex\n" + prefix + "\tEnd chain\n", stringBuffer.toString());
                stringBuffer.delete(0, 99);
            }
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test(expected = NullPointerException.class)
    public void toLogNullBufferTest() {
        parentEndPoint.setRegex("Regex");
        List<EndPoint> endPoints = new ArrayList<>();
        EndPoint endPoint = new EndChainEndPoint();
        endPoints.add(endPoint);
        parentEndPoint.setEndPoints(endPoints);
        for (String prefix : prefixValue) {
            parentEndPoint.toLog(null, prefix);
        }
    }

    @Test
    public void toLogNullPrefixTest() {
        parentEndPoint.setRegex("Regex");
        List<EndPoint> endPoints = new ArrayList<>();
        EndPoint endPoint = new EndChainEndPoint();
        endPoints.add(endPoint);
        parentEndPoint.setEndPoints(endPoints);
        try {
            parentEndPoint.toLog(stringBuffer, null);
            assertEquals("Expected and actual values should be the same.", null + "Regex: Regex\n" + null + "\tEnd chain\n", stringBuffer.toString());
            stringBuffer.delete(0, 99);
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }
}