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

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class PlaceholderReplacerTest extends Assert {

    @Test
    public void placeholderReplacerTest() throws Exception {
        Constructor<PlaceholderReplacer> placeholderReplacer = PlaceholderReplacer.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(placeholderReplacer.getModifiers()));
        placeholderReplacer.setAccessible(true);
        placeholderReplacer.newInstance();
    }

    @Test
    public void replaceTest() {
        String[] replaceValues = {"", "test", "T123est", "test-123", "test_TEST", "!@T1 23est<>", "test(999)", "?>TeSt    01"};
        for (String replaceValue : replaceValues) {
            String stringValue = PlaceholderReplacer.replace(replaceValue);
            assertEquals("Expected and actual values should be the same.", replaceValue, stringValue);
        }
    }

    @Test
    public void replaceNullTest() {
        String stringValue = PlaceholderReplacer.replace(null);
        assertNull("Null expected.", stringValue);
    }
}