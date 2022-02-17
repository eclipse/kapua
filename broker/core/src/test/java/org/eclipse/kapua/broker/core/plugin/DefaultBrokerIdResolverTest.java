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
package org.eclipse.kapua.broker.core.plugin;

import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.command.BrokerId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(JUnitTests.class)
public class DefaultBrokerIdResolverTest extends Assert {

    DefaultBrokerIdResolver defaultBrokerIdResolver;

    @Before
    public void initialize() {
        defaultBrokerIdResolver = new DefaultBrokerIdResolver();
    }

    @Test
    public void getBrokerIdTest() {
        String[] ids = {"", "broker id", "id1234567890", "id_)(*&^%$#@!?.,,.|", "broker-id123", "321broker_id!@", " bRoKeR&ID=99", ".broker1!*", "BROKER <id1> "};
        BrokerFilter brokerFilter = Mockito.mock(BrokerFilter.class);

        for (String id : ids) {
            BrokerId brokerId = new BrokerId(id);
            Mockito.when(brokerFilter.getBrokerId()).thenReturn(brokerId);
            assertEquals("Expected and actual values should be the same", id, defaultBrokerIdResolver.getBrokerId(brokerFilter));
        }
    }

    @Test(expected = NullPointerException.class)
    public void getBrokerIdNullTest() {
        defaultBrokerIdResolver.getBrokerId(null);
    }
}