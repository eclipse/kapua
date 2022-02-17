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
package org.eclipse.kapua.broker.core.listener;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class CamelConstantsTest extends Assert {

    @Test
    public void camelConstantsTest() throws Exception {
        Constructor<CamelConstants> camelConstants = CamelConstants.class.getDeclaredConstructor();
        assertTrue("True expected.", Modifier.isPrivate(camelConstants.getModifiers()));
        camelConstants.setAccessible(true);
        camelConstants.newInstance();
    }

    @Test
    public void checkConstantsTest() {
        assertEquals("Expected and actual values should be the same.", "CamelFailureEndpoint", CamelConstants.JMS_EXCHANGE_FAILURE_ENDPOINT);
        assertEquals("Expected and actual values should be the same.", "CamelExceptionCaught", CamelConstants.JMS_EXCHANGE_FAILURE_EXCEPTION);
        assertEquals("Expected and actual values should be the same.", "JMSRedelivered", CamelConstants.JMS_EXCHANGE_REDELIVERED);
        assertEquals("Expected and actual values should be the same.", "JMSTimestamp", CamelConstants.JMS_HEADER_TIMESTAMP);
        assertEquals("Expected and actual values should be the same.", "JMSDestination", CamelConstants.JMS_HEADER_DESTINATION);
        assertEquals("Expected and actual values should be the same.", "JMSCorrelationID", CamelConstants.JMS_CORRELATION_ID);
    }
}