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
package org.eclipse.kapua.transport.message.jms;

import org.eclipse.kapua.transport.jms.setting.JmsClientSetting;
import org.eclipse.kapua.transport.jms.setting.JmsClientSettingKeys;
import org.eclipse.kapua.transport.message.TransportChannel;

/**
 * Implementation of {@link TransportChannel} API for JMS transport facade
 *
 * @since 1.0.0
 */
public class JmsTopic implements TransportChannel {

    /**
     * The topic separator value for JMS topics returned from {@link JmsClientSetting}.{@link JmsClientSettingKeys#TRANSPORT_TOPIC_SEPARATOR}
     *
     * @since 1.0.0
     */
    private static final String TOPIC_SEPARATOR = JmsClientSetting.getInstance().getString(JmsClientSettingKeys.TRANSPORT_TOPIC_SEPARATOR);

    /**
     * The full topic.
     *
     * @since 1.0.0
     */
    private String topic;

    /**
     * Construct a {@link JmsTopic} with the given parameter
     *
     * @param topic The topic to set for this {@link JmsTopic}
     * @since 1.0.0
     */
    public JmsTopic(String topic) {
        setTopic(topic);
    }

    /**
     * Construct a {@link JmsTopic} with the given parameters.
     * <p>
     * Topic is built by concatenating all {@link String}[] token following the array order,
     * separating each token with the topic separator configured in {@link JmsClientSetting}.{@link JmsClientSettingKeys#TRANSPORT_TOPIC_SEPARATOR}
     * </p>
     *
     * @param topicParts The {@link String}[] from which build the full topic.
     * @since 1.0.0
     */
    public JmsTopic(String[] topicParts) {
        //
        // Concatenate topic parts
        if (topicParts != null) {
            StringBuilder sb = new StringBuilder();
            for (String s : topicParts) {
                sb.append(s)
                        .append(TOPIC_SEPARATOR);
            }
            sb.deleteCharAt(sb.length() - TOPIC_SEPARATOR.length());
            setTopic(sb.toString());
        }
    }

    /**
     * Gets the full topic set for this {@link JmsTopic}
     *
     * @return the full topic of this {@link JmsTopic}
     * @since 1.0.0
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets the full topic for this {@link JmsTopic}
     *
     * @param topic The full topic to set for this {@link JmsTopic}
     * @since 1.0.0
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Gets the topic split-ed by the topic separator configured in {@link JmsClientSetting}.{@link JmsClientSettingKeys#TRANSPORT_TOPIC_SEPARATOR}
     *
     * @return The topic tokens or {@code null} if full topic has been set to {@code null}
     * @since 1.0.0
     */
    public String[] getSplittedTopic() {
        if (topic == null) {
            return null;
        }
        return topic.split("\\" + TOPIC_SEPARATOR);
    }
}