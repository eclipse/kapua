/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.integration.experiment;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.qpid.jms.JmsQueue;
import org.apache.qpid.jms.message.JmsBytesMessage;
import org.apache.qpid.jms.message.JmsMessage;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.qa.common.BasicSteps;
import org.eclipse.kapua.qa.integration.steps.DockerSteps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeployKapua {

    private static final Logger logger = LoggerFactory.getLogger(DeployKapua.class);

    /**
     * This main method uses in a not proper way the org.eclipse.kapua.qa.integration.steps.DockerSteps
     * Use only for experiment!
     * @param argv
     * @throws Exception 
     */
    public static void main(String argv[]) throws Exception {
        new DeployKapua().start();
    }

    private void start() throws Exception {
        System.setProperty("BINDING_IP", "0.0.0.0");
        String amqpHost = "localhost";
        int amqpPort = 5682;
        String amqpUsername = SystemSetting.getInstance().getString(SystemSettingKey.BROKER_INTERNAL_CONNECTOR_USERNAME);
        String amqpPassword = SystemSetting.getInstance().getString(SystemSettingKey.BROKER_INTERNAL_CONNECTOR_PASSWORD);

        DockerSteps dockerSteps = new DockerSteps(null, null);
        dockerSteps.startBaseDockerEnvironment();
        dockerSteps.startAuthServiceContainer(BasicSteps.AUTH_SERVICE_CONTAINER_NAME);
        dockerSteps.startMessageBrokerContainer("hash_subscription_test");

        //
        AmqpClient amqpClient1 = new AmqpClient(amqpHost, amqpPort, "telemetry");
        amqpClient1.init(amqpUsername, amqpPassword, null, true, new String[] {"#"}, createMessageListener(amqpClient1));
        AmqpClient amqpClient2 = new AmqpClient(amqpHost, amqpPort, "lifecycle");
        amqpClient2.init(amqpUsername, amqpPassword, null, true, new String[] {"$EDC/#"}, createMessageListener(amqpClient2));
        AmqpClient amqpClient3 = new AmqpClient(amqpHost, amqpPort, "abc");
        amqpClient3.init(amqpUsername, amqpPassword, null, true, new String[] {"$ABC/#"}, createMessageListener(amqpClient2));
        AmqpClient amqpClient4 = new AmqpClient(amqpHost, amqpPort, "def");
        amqpClient4.init(amqpUsername, amqpPassword, null, true, new String[] {"$DEF/#"}, createMessageListener(amqpClient2));
        Thread.sleep(1024*1024*1024);
    }

    private DeployKapua() {
    }

    private MessageListener createMessageListener(AmqpClient client) {
        MessageListener messageListener = new MessageListener() {

            @Override
            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        if (message.getJMSDestination() instanceof JmsQueue) {
                            logger.info("{}/{} | content: {}", client.getClientId(), (((JmsQueue)((TextMessage)message).getJMSDestination())).getQueueName(), ((TextMessage)message).getText());
                        }
                        else {
                            logger.warn(">>> {}", message.getJMSDestination().toString());
                        }
                    }
                    else if (message instanceof JmsMessage) {
                        JmsMessage jmsMessage = (JmsMessage) message;
                        if (message instanceof JmsBytesMessage) {
                            JmsBytesMessage jmsByteMessage = (JmsBytesMessage) message;
                            byte[] body = new byte[(int)jmsByteMessage.getBodyLength()];
                            jmsByteMessage.readBytes(body);
                            logger.info("{}/{} | content: {}", client.getClientId(), jmsMessage.getJMSDestination(), new String(body));
                        }
                        else {
                            Object body = jmsMessage.getBody(Object.class);
                            logger.info("{}/{} | content: {}", client.getClientId(), jmsMessage.getJMSDestination(), body);
                        }
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        };
        return messageListener;
    }

}