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
package org.eclipse.kapua.commons.setting.system;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;


@Category(JUnitTests.class)
public class SystemSettingTest {

    @Test
    public void systemSettingTest() throws Exception {
        Constructor<SystemSetting> systemSetting = SystemSetting.class.getDeclaredConstructor();
        systemSetting.setAccessible(true);
        systemSetting.newInstance();
    }

    @Test
    public void getInstanceTest() {
        Assert.assertThat("Instance of SystemSetting expected.", SystemSetting.getInstance(), IsInstanceOf.instanceOf(SystemSetting.class));
    }

    @Test
    public void getMessageClassifierTest() {
        Assert.assertEquals("Expected and actual values should be the same.", "$EDC", SystemSetting.getInstance().getMessageClassifier());
    }
}
