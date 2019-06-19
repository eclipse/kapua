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
package org.eclipse.kapua.commons.util;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Category(JUnitTests.class)
public class PayloadsTest {

    private static final String PAYLOAD_DISPLAY_STR = "Boolean=true~~Double=42.42~~Float=42.42~~Integer=42~~Long=43~~String=Big brown fox~~byte=Ym9keQA~~unknown=";

    private static final Map<String, Object> TEST_DATA;

    static {
        final Map<String, Object> metrics = new HashMap<>();

        metrics.put("Float", 42.42F);
        metrics.put("Double", 42.42D);
        metrics.put("Integer", 42);
        metrics.put("Long", 43L);
        metrics.put("Boolean", Boolean.TRUE);
        metrics.put("String", "Big brown fox");
        metrics.put("byte", new byte[]{'b', 'o', 'd', 'y', 0});
        metrics.put("unknown", new BigDecimal("42.42"));
        metrics.put("null", null);

        TEST_DATA = Collections.unmodifiableMap(metrics);
    }

    @Test
    public void testConstructor() throws Exception {
        Constructor<Payloads> payload = Payloads.class.getDeclaredConstructor();
        payload.setAccessible(true);
        payload.newInstance();
    }

    @Test
    public void testNull() {
        Assert.assertEquals("", Payloads.toDisplayString(null));
    }

    @Test
    public void testEmpty() {
        Assert.assertEquals("", Payloads.toDisplayString(Collections.emptyMap()));
    }

    @Test
    public void testData() {
        Assert.assertEquals(PAYLOAD_DISPLAY_STR, Payloads.toDisplayString(TEST_DATA));
    }
}
