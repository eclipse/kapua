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
package org.eclipse.kapua.broker.core.plugin.metric;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class SecurityMetricsTest extends Assert {

    @Test
    public void securityMetricsTest() throws Exception {
        Constructor<SecurityMetrics> securityMetrics = SecurityMetrics.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(securityMetrics.getModifiers()));
        securityMetrics.setAccessible(true);
        securityMetrics.newInstance();
    }
}