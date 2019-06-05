/*******************************************************************************
 * Copyright (c) 2019, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.artemis;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttCallback implements org.eclipse.paho.client.mqttv3.MqttCallback {

    private static final Logger logger = LoggerFactory.getLogger(ArtemisEmbeddedTest.class);

    private boolean enableLog;
    private String clientId;

    public MqttCallback(String clientId){ 
        this.clientId = clientId;
    }

    public void connectionLost(Throwable cause) {
        logger.error("Connection Lost");
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        if (enableLog) {
            logger.info(clientId + " - Message arrived on \"" + topic + "\" - \"");// + new String(message.getPayload()) + "\"");
        }
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        if (enableLog) {
            logger.info("Delivery Complete: {} - time: {}", token.getMessageId(), (new SimpleDateFormat("dd/MM/YYYY HH:mm:ss")).format(new Date()));
        }
    }

}
