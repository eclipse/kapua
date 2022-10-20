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
package org.eclipse.kapua.app.api.core.settings;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;


@Category(JUnitTests.class)
public class KapuaApiCoreSettingTest {

    @Test
    public void kapuaApiCoreSettingTest() throws Exception {
        Constructor<KapuaApiCoreSetting> kapuaApiCoreSetting = KapuaApiCoreSetting.class.getDeclaredConstructor();
        kapuaApiCoreSetting.setAccessible(true);
        kapuaApiCoreSetting.newInstance();
        Assert.assertTrue("True expected.", Modifier.isPrivate(kapuaApiCoreSetting.getModifiers()));
    }

    @Test
    public void getInstanceTest() {
        Assert.assertTrue("True expected.", KapuaApiCoreSetting.getInstance() instanceof KapuaApiCoreSetting);
    }
}