/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
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

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class PayloadTest {

    Object[] objectValues = new Object[]{1, 2147483647, -2147483648, true, false, 3.4028235E38, 1.4E-45, 1.7976931348623157E308, 4.9E-324, 9223372036854775807L, -9223372036854775808L, "String", 'S'};
    String[] keys = {"", "!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", "regularNaming", "regular Naming", "49", "regularNaming49", "NAMING", "246465494135646120009090049684646496468456468496846464968496844"};

    @Test
    public void putAndTimestampBuilderValidTest() {
        for (Object objValue : objectValues) {
            for (String keyValue : keys) {
                Payload payload = new Payload.Builder().timestamp(Instant.now()).put(keyValue, objValue).build();
                Assert.assertNotNull("Object should not be null!", payload);
                Assert.assertEquals("Expected and actual values should be the same!", 1, payload.getValues().size());
                Assert.assertEquals("Expected and actual values should be the same!", objValue, payload.getValues().get(keyValue));
                Assert.assertFalse("No exception expected!", Instant.now().isBefore(payload.getTimestamp()));
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void timestampBuilderNullTest() {
        new Payload.Builder().timestamp(null).build();
    }

    @Test(expected = NullPointerException.class)
    public void valuesBuilderNullTest() {
        new Payload.Builder().values(null).build();
    }

    @Test(expected = NullPointerException.class)
    public void putBuilderNullTest() {
        new Payload.Builder().put(null, null).build();
    }

    @Test
    public void timestampAndValuesBuilderValidTest() {
        for (Object objValue : objectValues) {
            for (String keyValue : keys) {
                Payload payload = new Payload.Builder().timestamp(Instant.now()).values(Collections.singletonMap(keyValue, objValue)).build();
                Assert.assertNotNull("Object should not be null!", payload);
                Assert.assertEquals("Expected and actual values should be the same!", 1, payload.getValues().size());
                Assert.assertEquals("Expected and actual values should be the same!", objValue, payload.getValues().get(keyValue));
                Assert.assertFalse("No exception expected!", Instant.now().isBefore(payload.getTimestamp()));
            }
        }
    }

    @Test
    public void toStringTest(){
        Payload.Builder builder = new Payload.Builder();
        Payload payload = builder.build();
        String expectedString = "[Payload - timestamp: " + payload.getTimestamp() +", values: " + payload.getValues() +"]";
        Assert.assertEquals("Expected and actual values should be the same!", expectedString, payload.toString());
    }

    @Test
    public void builderTimestampTest(){
        Payload.Builder builder = new Payload.Builder();
        Payload payload = builder.build();
        Assert.assertEquals("Expected and actual values should be the same!", builder,builder.timestamp(payload.getTimestamp()));
        Assert.assertEquals("Expected and actual values should be the same!", builder.timestamp(),builder.timestamp(payload.getTimestamp()).timestamp());
    }

    @Test
    public void payloadBuilderValuesTest() {
        final Map<String, Object> expectedValues = new HashMap<>();
        Payload.Builder builder = new Payload.Builder();
        Assert.assertEquals("Expected and actual values should be the same!", expectedValues, builder.values());
    }

    @Test(expected = NullPointerException.class)
    public void payloadOfWithOneParameterNullTest() {
        Payload.of((String) null, null);
    }

    @Test(expected = NullPointerException.class)
    public void payloadOfWithTwoParametersNullTest() {
        Payload.of(null);
    }

    @Test(expected = NullPointerException.class)
    public void payloadOfWithThreeParametersNullTest() {
        Payload.of(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void payloadOfWithFourParametersNullTest() {
        Payload.of((Instant) null, null);
    }

    @Test
    public void payloadOfWithOneParameterValidTest() {
        for (Object objValue : objectValues) {
            for (String keyValue : keys) {
                Payload payload = Payload.of(keyValue, objValue);
                Assert.assertNotNull("Object should not be null!", payload);
                Assert.assertEquals("Expected and actual values should be the same!", 1, payload.getValues().size());
                Assert.assertEquals("Expected and actual values should be the same!", objValue, payload.getValues().get(keyValue));
                Assert.assertFalse("No exception expected!", Instant.now().isBefore(payload.getTimestamp()));
            }
        }
    }

    @Test
    public void payloadOfWithTwoParametersValidTest() {
        for (Object objValue : objectValues) {
            for (String keyValue : keys) {
                Payload payload = Payload.of(Collections.singletonMap(keyValue, objValue));
                Assert.assertNotNull("Object should not be null!", payload);
                Assert.assertEquals("Expected and actual values should be the same!", 1, payload.getValues().size());
                Assert.assertEquals("Expected and actual values should be the same!", objValue, payload.getValues().get(keyValue));
                Assert.assertFalse("No exception expected!", Instant.now().isBefore(payload.getTimestamp()));
            }
        }
    }

    @Test
    public void payloadOfWithThreeParametersValidTest() {
        for (Object objValue : objectValues) {
            for (String keyValue : keys) {
                Payload payload = Payload.of(Instant.now(), keyValue, objValue);
                Assert.assertNotNull("Object should not be null!", payload);
                Assert.assertEquals("Expected and actual values should be the same!", 1, payload.getValues().size());
                Assert.assertEquals("Expected and actual values should be the same!", objValue, payload.getValues().get(keyValue));
                Assert.assertFalse("No exception expected!", Instant.now().isBefore(payload.getTimestamp()));
            }
        }
    }

    @Test
    public void payloadOfWithFourParametersValidTest() {
        for (Object objValue : objectValues) {
            for (String keyValue : keys) {
                Payload payload = Payload.of(Instant.now(), Collections.singletonMap(keyValue, objValue));
                Assert.assertNotNull("Object should not be null!", payload);
                Assert.assertEquals("Expected and actual values should be the same!", 1, payload.getValues().size());
                Assert.assertEquals("Expected and actual values should be the same!", objValue, payload.getValues().get(keyValue));
                Assert.assertFalse("No exception expected!", Instant.now().isBefore(payload.getTimestamp()));
            }
        }
    }
}
