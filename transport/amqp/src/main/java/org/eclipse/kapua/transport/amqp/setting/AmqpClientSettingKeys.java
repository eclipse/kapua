/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.transport.amqp.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Available settings key for AMQP transport level
 *
 * @since 1.0.0
 */
public enum AmqpClientSettingKeys implements SettingKey {

    /**
     * The prefix for the id set to the AMQP Client
     * 
     * @since 1.0.0
     */
     CLIENT_ID_PREFIX("transport.amqp.client.id.prefix"),
    /**
     * The username to use for AMQP connection
     * 
     * @since 1.0.0
     */
    TRANSPORT_CREDENTIAL_USERNAME("transport.credential.username"),

    /**
     * The password to use for AMQP connection
     * 
     * @since 1.0.0
     */
    TRANSPORT_CREDENTIAL_PASSWORD("transport.credential.password"),

    /**
     * The character separator for topic levels.
     * 
     * @since 1.0.0
     */
    TRANSPORT_TOPIC_SEPARATOR("transport.topic.separator"),

    /**
     * Timeout for send sync operations.
     * 
     * @since 1.0.0
     */
    SEND_TIMEOUT_MAX("send.timeout.max"),

    /**
     * Connection port
     */
    TRANSPORT_CONNECTION_PORT("transport.amqp.connection.port"),

    /**
     * Prefetch messages
     */
    TRANSPORT_PREFETCH_MESSAGES("transport.amqp.connection.prefetch_messages"),

    /**
     * Wait between reconnection (in milliseconds)
     */
    TRANSPORT_WAIT_BETWEEN_RECONNECT("transport.amqp.connection.wait_between_reconnect"),

    /**
     * Connect timeout (in milliseconds)
     */
    TRANSPORT_CONNECT_TIMEOUT("transport.amqp.connection.connect_timeout"),

    /**
     * Connection idle timeout (in seconds)
     */
    TRANSPORT_IDLE_TIMEOUT("transport.amqp.connection.idle_timeout"),

    /**
     * Maximum reconnection attempts
     */
    TRANSPORT_MAXIMUM_RECONNECTION_ATTEMPTS("transport.amqp.connection.maximum_reconnection_attempt"),

    /**
     * Set to true if the devices are connected through ActiveMQ with VirtualTopic enabled
     */
    TRANSPORT_MQTT_VT_ENABLED("transport.mqtt.enabled_virtual_topic"),

    /**
     * Exit code
     */
    EXIT_CODE("transport.amqp.exit_code");

    /**
     * The key value in the configuration resources.
     * 
     * @since 1.0.0
     */
    private String key;

    /**
     * Set up the {@code enum} with the key value provided
     * 
     * @param key
     *            The value mapped by this {@link Enum} value
     * @since 1.0.0
     */
    private AmqpClientSettingKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the key for this {@link AmqpClientSettingKeys}
     * 
     * @since 1.0.0
     */
    public String key() {
        return key;
    }
}
