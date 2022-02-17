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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.jms.JMSException;
import javax.jms.MessageListener;

@Category(JUnitTests.class)
public class JmsConsumerWrapperTest {

    @Test(expected = KapuaException.class)
    public void jmsConsumerWrapperTest() throws KapuaException, JMSException {
        MessageListener messageListener = Mockito.mock(MessageListener.class);
        boolean[] transacted = new boolean[]{true, false};
        boolean[] start = new boolean[]{true, false};
        for (boolean transactedValue : transacted) {
            for (boolean startValue : start) {
                new JmsConsumerWrapper("", transactedValue, startValue, messageListener);
            }
        }
    }
}