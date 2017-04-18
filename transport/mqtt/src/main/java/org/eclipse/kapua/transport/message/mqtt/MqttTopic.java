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
package org.eclipse.kapua.transport.message.mqtt;

import org.eclipse.kapua.transport.message.TransportChannel;
import org.eclipse.kapua.transport.mqtt.setting.MqttClientSetting;
import org.eclipse.kapua.transport.mqtt.setting.MqttClientSettingKeys;

/**
 * Implementation of {@link TransportChannel} API for MQTT transport facade
 * 
 * @since 1.0.0
 */
public class MqttTopic implements TransportChannel {

    /**
     * The topic separator value for MQTT topics returned from {@link MqttClientSetting}.{@link MqttClientSettingKeys#TRANSPORT_TOPIC_SEPARATOR}
     * 
     * @since 1.0.0
     */
    private static String topicSeparator = MqttClientSetting.getInstance().getString(MqttClientSettingKeys.TRANSPORT_TOPIC_SEPARATOR);

    /**
     * The full topic.
     * 
     * @since 1.0.0
     */
    private String topic;

    /**
     * Construct a {@link MqttTopic} with the given parameter
     * 
     * @param topic
     *            The topic to set for this {@link MqttTopic}
     * @since 1.0.0
     */
    public MqttTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Construct a {@link MqttTopic} with the given parameters.
     * <p>
     * Topic is built by concatenating all {@link String}[] token following the array order,
     * separating each token with the topic separator configured in {@link MqttClientSetting}.{@link MqttClientSettingKeys#TRANSPORT_TOPIC_SEPARATOR}
     * </p>
     * 
     * @param topicParts
     *            The {@link String}[] from which build the full topic.
     * @since 1.0.0
     */
    public MqttTopic(String[] topicParts) {
        //
        // Concatenate topic parts
        if (topicParts != null) {
            StringBuilder sb = new StringBuilder();
            for (String s : topicParts) {
                sb.append(s)
                        .append(topicSeparator);
            }
            sb.deleteCharAt(sb.length() - topicSeparator.length());
            setTopic(sb.toString());
        }
    }

    /**
     * Gets the full topic set for this {@link MqttTopic}
     * 
     * @return the full topic of this {@link MqttTopic}
     * @since 1.0.0
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets the full topic for this {@link MqttTopic}
     * 
     * @param topic
     *            The full topic to set for this {@link MqttTopic}
     * @since 1.0.0
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Gets the topic split-ed by the topic separator configured in {@link MqttClientSetting}.{@link MqttClientSettingKeys#TRANSPORT_TOPIC_SEPARATOR}
     * 
     * @return The topic tokens or {@code null} if full topic has been set to {@code null}
     * @since 1.0.0
     */
    public String[] getSplittedTopic() {
        if (topic == null) {
            return null;
        }
        return topic.split("\\" + topicSeparator);
    }
}
