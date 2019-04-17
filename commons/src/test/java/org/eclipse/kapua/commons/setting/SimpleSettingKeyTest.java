/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.setting;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class SimpleSettingKeyTest extends Assert {

    @Test
    public void testConstructor() throws Exception {

        SimpleSettingKey settingKey = new SimpleSettingKey("string");
        Assert.assertEquals("string", settingKey.key());
    }

    @Test
    public void testKey() {
        SimpleSettingKey key1 = new SimpleSettingKey("key");
        Assert.assertSame("key", key1.key());
    }
}