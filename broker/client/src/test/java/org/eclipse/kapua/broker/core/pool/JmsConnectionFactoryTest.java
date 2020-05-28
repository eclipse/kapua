/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.broker.client.pool.JmsConnectionFactory;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class JmsConnectionFactoryTest {

    @Test
    public void jmsConnectionFactoryTest() throws Exception {
        Constructor<JmsConnectionFactory> jmsConnectionFactory = JmsConnectionFactory.class.getDeclaredConstructor();
        Assert.assertTrue("True expected.", Modifier.isPrivate(jmsConnectionFactory.getModifiers()));
        jmsConnectionFactory.setAccessible(true);
        jmsConnectionFactory.newInstance();
    }

    @Test
    public void staticBlockTest() {
        ActiveMQConnectionFactory connectionFactory = JmsConnectionFactory.VM_CONN_FACTORY;

        Assert.assertFalse("False expected.", connectionFactory.isAlwaysSessionAsync());
        Assert.assertFalse("False expected.", connectionFactory.isAlwaysSyncSend());
        Assert.assertFalse("False expected.", connectionFactory.isCopyMessageOnSend());
        Assert.assertFalse("False expected.", connectionFactory.isDispatchAsync());
        Assert.assertFalse("False expected.", connectionFactory.isOptimizeAcknowledge());
        Assert.assertTrue("True expected.", connectionFactory.isOptimizedMessageDispatch());
        Assert.assertTrue("True expected.", connectionFactory.isUseAsyncSend());
    }
}