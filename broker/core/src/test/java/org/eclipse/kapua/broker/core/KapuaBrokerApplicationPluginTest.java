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
package org.eclipse.kapua.broker.core;

import org.apache.activemq.broker.Broker;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(JUnitTests.class)
public class KapuaBrokerApplicationPluginTest extends Assert {

    KapuaBrokerApplicationPlugin kapuaBrokerApplicationPlugin;
    Broker broker;

    @Before
    public void initialize() {
        kapuaBrokerApplicationPlugin = new KapuaBrokerApplicationPlugin();
        broker = Mockito.mock(Broker.class);
    }

    @Test
    public void installPluginTest() throws Exception {
        assertTrue("True expected.", kapuaBrokerApplicationPlugin.installPlugin(broker) instanceof Broker);
    }

    @Test
    public void installPluginNullTest() throws Exception {
        assertTrue("True expected.", kapuaBrokerApplicationPlugin.installPlugin(null) instanceof Broker);
    }
}