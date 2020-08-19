/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin.metric;

import com.codahale.metrics.Counter;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class LoginMetricTest extends Assert {

    LoginMetric loginMetric;

    @Before
    public void start() {
        loginMetric = LoginMetric.getInstance();
    }

    @Test
    public void gettersTest() {
        assertNotNull(loginMetric.getSuccess());
        assertNotNull(loginMetric.getFailure());
        assertNotNull(loginMetric.getInvalidUserPassword());
        assertNotNull(loginMetric.getInvalidClientId());
        assertNotNull(loginMetric.getKapuasysTokenAttempt());
        assertNotNull(loginMetric.getNormalUserAttempt());
        assertNotNull(loginMetric.getStealingLinkConnect());
        assertNotNull(loginMetric.getStealingLinkDisconnect());
        assertNotNull(loginMetric.getRemoteStealingLinkDisconnect());
        assertNotNull(loginMetric.getAdminStealingLinkDisconnect());
        assertNotNull(loginMetric.getAddConnectionTime());
        assertNotNull(loginMetric.getNormalUserTime());
        assertNotNull(loginMetric.getShiroLoginTime());
        assertNotNull(loginMetric.getCheckAccessTime());
        assertNotNull(loginMetric.getFindDeviceConnectionTime());
        assertNotNull(loginMetric.getUpdateDeviceConnectionTime());
        assertNotNull(loginMetric.getShiroLogoutTime());
        assertNotNull(loginMetric.getSendLoginUpdateMsgTime());
        assertNotNull(loginMetric.getRemoveConnectionTime());
    }

    @Test
    public void settersTest() {
        Counter counter = new Counter();
        loginMetric.setAdminStealingLinkDisconnect(counter);
        assertEquals(counter, loginMetric.getAdminStealingLinkDisconnect());
    }
}
