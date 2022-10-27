/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.setting;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Category(JUnitTests.class)
public class SimpleSettingKeyTest {

    @Test
    public void simpleSettingKeyTest() {
        SimpleSettingKey settingKey = new SimpleSettingKey("string");
        Assert.assertEquals("Expected and actual values should be the same!", "string", settingKey.key());
    }

    @Test
    public void simpleSettingKeyNullTest() {
        SimpleSettingKey settingKey = new SimpleSettingKey(null);
        Assert.assertNull("Null expected!", settingKey.key());
    }

    @Test
    public void keyRegularTest() {
        String[] permittedValues = {"", "!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", "regularNaming", "regular Naming", "49", "regularNaming49", "NAMING", "246465494135646120009090049684646496468456468496846464968496844"};
        for(String value : permittedValues) {
            SimpleSettingKey key = new SimpleSettingKey(value);
            Assert.assertEquals("Expected and actual values should be the same!", value, key.key());
        }
    }
}
