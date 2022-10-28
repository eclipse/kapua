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
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Field;
import java.util.Stack;


@Category(JUnitTests.class)
public class ServiceEventScopeTest {

    ServiceEvent serviceEvent;

    @Before
    public void createInstanceOfClasses() {
        serviceEvent = new ServiceEvent();
    }

    @Test
    public void serviceEventBeginNullTest() throws Exception {
        ServiceEventScope.begin();
        Field privateEventContextThdLocal = ServiceEventScope.class.getDeclaredField("eventContextThdLocal");
        privateEventContextThdLocal.setAccessible(true);
        ThreadLocal<Stack<ServiceEvent>> fieldValue = (ThreadLocal<Stack<ServiceEvent>>) privateEventContextThdLocal.get(null);
        Assert.assertNotNull(fieldValue.get().peek().getContextId());
    }

    @Test(expected = KapuaRuntimeException.class)
    public void serviceEventEndNullTest() throws Exception {
        Field privateEventContextThdLocal = ServiceEventScope.class.getDeclaredField("eventContextThdLocal");
        privateEventContextThdLocal.setAccessible(true);
        ThreadLocal<Stack<ServiceEvent>> fieldValue = (ThreadLocal<Stack<ServiceEvent>>) privateEventContextThdLocal.get(null);
        fieldValue.set(null);
        ServiceEventScope.end();
    }

    @Test
    public void serviceEventBeginEndTest() throws Exception {
        ServiceEventScope.begin();
        Field privateEventContextThdLocal = ServiceEventScope.class.getDeclaredField("eventContextThdLocal");
        privateEventContextThdLocal.setAccessible(true);
        ThreadLocal<Stack<ServiceEvent>> fieldValue = (ThreadLocal<Stack<ServiceEvent>>) privateEventContextThdLocal.get(null);
        ServiceEventScope.end();
        Assert.assertNull("not_null", fieldValue.get());
    }

    @Test
    public void serviceEventSetAndGetRegularTest() {
        ServiceEventScope.set(serviceEvent);
        Assert.assertEquals("not_equals", serviceEvent, ServiceEventScope.get());
    }
}
