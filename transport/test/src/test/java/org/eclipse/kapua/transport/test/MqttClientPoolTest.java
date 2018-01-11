/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.transport.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.kapua.transport.mqtt.MqttClient;
import org.eclipse.kapua.transport.mqtt.pooling.MqttClientPool;
import org.eclipse.kapua.transport.mqtt.pooling.setting.MqttClientPoolSetting;
import org.eclipse.kapua.transport.mqtt.pooling.setting.MqttClientPoolSettingKeys;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class MqttClientPoolTest extends Assert {

    /**
     * Ignoring this test for a while. We should fix the build in the first place and then use embedded ActiveMQ
     * broker for tests.
     */
    @Ignore
    @Test
    public void testPoolBorrow()
            throws Exception {
        //
        // Get pool
        /* FIXME the following line will fail once the test will not be ignored anymore.
           Now the facade needs the direct broker uri as a string to point to the broker
           in a clustered environment
        */
        MqttClientPool transportPool = MqttClientPool.getInstance(null);

        //
        // Borrow client
        MqttClient mqttClient = null;
        try {
            mqttClient = transportPool.borrowObject();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        //
        // Verify client
        assertNotNull("mqttClient", mqttClient);
        assertTrue("mqttClient.isConnected", mqttClient.isConnected());

        //
        // Return client
        try {
            transportPool.returnObject(mqttClient);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Ignoring this test for a while. We should fix the build in the first place and then use embedded ActiveMQ
     * broker for tests.
     */
    @Ignore
    @Test
    public void testPoolBorrowMax()
            throws Exception {
        //
        // Get pool
        /* FIXME the following line will fail once the test will not be ignored anymore.
           Now the facade needs the direct broker uri as a string to point to the broker
           in a clustered environment
        */
        MqttClientPool transportPool = MqttClientPool.getInstance(null);

        //
        // Test max borrow clients
        MqttClientPoolSetting setting = MqttClientPoolSetting.getInstance();
        long maxClients = setting.getLong(MqttClientPoolSettingKeys.CLIENT_POOL_SIZE_TOTAL_MAX);
        List<MqttClient> mqttClients = new ArrayList<>();
        for (int i = 0; i < maxClients; i++) {
            mqttClients.add(transportPool.borrowObject());
        }

        //
        // Verify borrowed clients
        for (MqttClient t : mqttClients) {
            assertNotNull("mqttClient", t);
            assertTrue("mqttClient.isConnected", t.isConnected());
        }
        assertEquals("numActiveClients", maxClients, transportPool.getNumActive());

        //
        // Ask one more transport client
        try {
            mqttClients.add(transportPool.borrowObject());
            fail("Should have thrown " + NoSuchElementException.class.getName() + " exception");
        } catch (NoSuchElementException nsee) {
            // All ok
        } catch (Exception e) {
            assertEquals("Should have thrown " + NoSuchElementException.class.getName() + " exception", NoSuchElementException.class, e.getClass());
        }

        //
        // Return clients
        Iterator<MqttClient> transportClientIterator = mqttClients.iterator();
        while (transportClientIterator.hasNext()) {
            transportPool.returnObject(transportClientIterator.next());
        }
        assertEquals("numActiveClients", 0, transportPool.getNumActive());
    }

}
