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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.client.message;

public class Util {

    private Util() {
    }

    // =========================================
    // wildcards conversion
    // =========================================

    /**
     * ActiveMQ translate wildcards from jms to mqtt
     * function ActiveMQ MQTT
     * separator . /
     * element * +
     * sub tree &gt; #
     *
     * @param jmsTopic
     * @return
     */
    public static String convertJmsWildCardToMqtt(String jmsTopic) {
        String processedTopic = null;
        if (jmsTopic != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < jmsTopic.length(); i++) {
                sb.append(convertWildcardJmsToMqtt(jmsTopic.charAt(i)));
            }
            processedTopic = sb.toString();
        }
        return processedTopic;
    }

    private static char convertWildcardJmsToMqtt(char c) {
        switch (c) {
            case '.':
                return '/';
            case '/':
                return '.';
            default:
                return c;
        }
    }

    /**
     * ActiveMQ translate wildcards from jms to mqtt
     * function ActiveMQ MQTT
     * separator . /
     * element * +
     * sub tree &gt; #
     *
     * @param mqttTopic
     * @return
     */
    public static String convertMqttWildCardToJms(String mqttTopic) {
        String processedTopic = null;
        if (mqttTopic != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mqttTopic.length(); i++) {
                sb.append(convertWildcardMqttToJms(mqttTopic.charAt(i)));
            }
            processedTopic = sb.toString();
        }
        return processedTopic;
    }

    private static char convertWildcardMqttToJms(char c) {
        switch (c) {
            case '.':
                return '/';
            case '/':
                return '.';
            default:
                return c;
        }
    }

}
