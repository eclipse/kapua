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

import java.util.Date;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.transport.TransportClientFactory;
import org.eclipse.kapua.transport.TransportFacade;
import org.eclipse.kapua.transport.message.TransportMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class MqttFacadeTest extends Assert {

    /**
     * Ignoring this test for a while. We should fix the build in the first place and then use embedded ActiveMQ
     * broker for tests.
     */
    @Ignore
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testMqttClientSend()
            throws Exception {
        //
        // Get facade
        KapuaLocator locator = KapuaLocator.getInstance();
        TransportClientFactory transportFacadeFactory = locator.getFactory(TransportClientFactory.class);
        /* FIXME the following line will fail once the test will not be ignored anymore.
           Now the facade needs the direct broker uri as a string to point to the broker
           in a clustered environment
        */
        TransportFacade transportFacade = transportFacadeFactory.getFacade(null);

        assertNotNull("client.clientId", transportFacade.getClientId());

        //
        // Send
        String sendTopic = SystemSetting.getInstance().getMessageClassifier() + "/kapua-sys/" + transportFacade.getClientId() + "/" + MqttClientTest.class.getSimpleName() + "/testTransportFacadeSend";

        MqttTopic mqttTopic = new MqttTopic(sendTopic);
        MqttPayload mqttPayload = new MqttPayload("testTransportFacadeSendPayload".getBytes());

        MqttMessage mqttMessage = new MqttMessage(mqttTopic,
                new Date(),
                mqttPayload);

        TransportMessage responseMessage = null;
        try {
            responseMessage = transportFacade.sendSync(mqttMessage, null);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        //
        // Verify
        assertNull("responseMessage", responseMessage);

        //
        // Clean
        try {
            transportFacade.clean();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
