/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.transport.mqtt.exception;

import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.mqtt.MqttClient;

/**
 * {@link Exception} to {@code throw} when the {@link MqttClient} cannot publish the {@link org.eclipse.kapua.transport.message.mqtt.MqttMessage}.
 *
 * @since 1.2.0
 */
public class MqttClientPublishException extends MqttClientException {

    /**
     * The {@link org.eclipse.kapua.transport.message.mqtt.MqttTopic} where the {@link MqttMessage} that was meant to be published.
     *
     * @since 1.2.0
     */
    final String topic;

    /**
     * The {@link MqttMessage}  was meant to be published.
     *
     * @since 1.2.0
     */
    final MqttMessage mqttMessage;

    /**
     * Constructor.
     *
     * @param cause       The root {@link Throwable} that caused the error.
     * @param clientId    The clientId of the {@link org.eclipse.kapua.transport.mqtt.MqttClient} that produced this {@link MqttClientPublishException}.
     * @param topic       The {@link org.eclipse.kapua.transport.message.mqtt.MqttTopic} where the {@link MqttMessage} that was meant to be published.
     * @param mqttMessage The {@link MqttMessage}  was meant to be published.
     * @since 1.2.0
     */
    public MqttClientPublishException(Throwable cause, String clientId, String topic, MqttMessage mqttMessage) {
        super(MqttClientErrorCodes.PUBLISH_EXCEPTION, cause, clientId, topic, mqttMessage.getPayload().hasBody() ? mqttMessage.getPayload().getBody().length : null);

        this.topic = topic;
        this.mqttMessage = mqttMessage;
    }

    /**
     * Gets the {@link org.eclipse.kapua.transport.message.mqtt.MqttTopic} where the {@link MqttMessage} that was meant to be published.
     *
     * @return The {@link org.eclipse.kapua.transport.message.mqtt.MqttTopic} where the {@link MqttMessage} that was meant to be published.
     * @since 1.2.0
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Gets the {@link MqttMessage}  was meant to be published.
     *
     * @return The {@link MqttMessage}  was meant to be published.
     * @since 1.2.0
     */
    public MqttMessage getMqttMessage() {
        return mqttMessage;
    }
}
