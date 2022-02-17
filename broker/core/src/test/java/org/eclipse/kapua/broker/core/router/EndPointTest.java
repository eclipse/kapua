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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.regex.Pattern;

@Category(JUnitTests.class)
public class EndPointTest extends Assert {

    @Test
    public void replacePlaceholderTest() {
        String[] regexValues = {null, "\\", "regex", "re gex", "", "reg\fex", "rege\rx", "re\bgex", " re\1gex", " r\2Egex", "\3rege123x, re-gex\4, r\5egex, r\6egex, reg\7ex, r\0ege_x", "1234re\\gex", "re\tge", "reg\nex1234567890", "\'"};
        for (String value : regexValues) {
            assertEquals("Expected and actual values should be the same.", value, EndPoint.replacePlaceholder(value));
        }
    }

    @Test
    public void parseRegexNullTest() {
        String[] regexValue = {null, "\\", "re\\gex"};
        for (String value : regexValue) {
            assertNull("Null expected.", EndPoint.parseRegex(value));
        }
    }

    @Test
    public void parseRegexTest() {
        String[] regexValues = {"", "regex", "re\tge", "reg\nex1234567890", "!@#$%^&*()_+=-/.,?><|:;'|", "\'", "re gex"};
        for (String value : regexValues) {
            assertThat("Instance of Pattern expected.", EndPoint.parseRegex(value), IsInstanceOf.instanceOf(Pattern.class));
        }
    }
}