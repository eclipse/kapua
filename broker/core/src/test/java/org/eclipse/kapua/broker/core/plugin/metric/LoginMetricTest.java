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

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class LoginMetricTest extends Assert {

    LoginMetric loginMetric;

    @Before
    public void initialize() {
        loginMetric = LoginMetric.getInstance();
    }

    @Test
    public void loginMetricTest() throws Exception {
        Constructor<LoginMetric> loginMetric = LoginMetric.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(loginMetric.getModifiers()));
        loginMetric.setAccessible(true);
        loginMetric.newInstance();
    }

    @Test
    public void gettersTest() {
        assertTrue("Instance of Counter expected.", loginMetric.getSuccess() instanceof Counter);
        assertTrue("Instance of Counter expected.", loginMetric.getFailure() instanceof Counter);
        assertTrue("Instance of Counter expected.", loginMetric.getInvalidUserPassword() instanceof Counter);
        assertTrue("Instance of Counter expected.", loginMetric.getInvalidClientId() instanceof Counter);
        assertTrue("Instance of Counter expected.", loginMetric.getKapuasysTokenAttempt() instanceof Counter);
        assertTrue("Instance of Counter expected.", loginMetric.getInternalConnectorAttempt() instanceof Counter);
        assertTrue("Instance of Counter expected.", loginMetric.getNormalUserAttempt() instanceof Counter);
        assertTrue("Instance of Counter expected.", loginMetric.getStealingLinkConnect() instanceof Counter);
        assertTrue("Instance of Counter expected.", loginMetric.getStealingLinkDisconnect() instanceof Counter);
        assertTrue("Instance of Counter expected.", loginMetric.getRemoteStealingLinkDisconnect() instanceof Counter);
        assertTrue("Instance of Counter expected.", loginMetric.getAdminStealingLinkDisconnect() instanceof Counter);
        assertTrue("Instance of Counter expected.", loginMetric.getInternalConnectorConnected() instanceof Counter);
        assertTrue("Instance of Counter expected.", loginMetric.getInternalConnectorDisconnected() instanceof Counter);
        assertTrue("Instance of Timer expected.", loginMetric.getAddConnectionTime() instanceof Timer);
        assertTrue("Instance of Timer expected.", loginMetric.getNormalUserTime() instanceof Timer);
        assertTrue("Instance of Timer expected.", loginMetric.getShiroLoginTime() instanceof Timer);
        assertTrue("Instance of Timer expected.", loginMetric.getCheckAccessTime() instanceof Timer);
        assertTrue("Instance of Timer expected.", loginMetric.getFindDeviceConnectionTime() instanceof Timer);
        assertTrue("Instance of Timer expected.", loginMetric.getUpdateDeviceConnectionTime() instanceof Timer);
        assertTrue("Instance of Timer expected.", loginMetric.getShiroLogoutTime() instanceof Timer);
        assertTrue("Instance of Timer expected.", loginMetric.getSendLoginUpdateMsgTime() instanceof Timer);
        assertTrue("Instance of Timer expected.", loginMetric.getRemoveConnectionTime() instanceof Timer);
    }
}