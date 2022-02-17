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
package org.eclipse.kapua.broker.core.pool;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class JmsConnectionFactoryTest extends Assert {

    @Test
    public void jmsConnectionFactoryTest() throws Exception {
        Constructor<JmsConnectionFactory> jmsConnectionFactory = JmsConnectionFactory.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(jmsConnectionFactory.getModifiers()));
        jmsConnectionFactory.setAccessible(true);
        jmsConnectionFactory.newInstance();
    }

    @Test
    public void staticBlockTest() {
        ActiveMQConnectionFactory connectionFactory = JmsConnectionFactory.VM_CONN_FACTORY;

        assertFalse("False expected.", connectionFactory.isAlwaysSessionAsync());
        assertFalse("False expected.", connectionFactory.isAlwaysSyncSend());
        assertFalse("False expected.", connectionFactory.isCopyMessageOnSend());
        assertFalse("False expected.", connectionFactory.isDispatchAsync());
        assertFalse("False expected.", connectionFactory.isOptimizeAcknowledge());
        assertTrue("True expected.", connectionFactory.isOptimizedMessageDispatch());
        assertTrue("True expected.", connectionFactory.isUseAsyncSend());
    }
}