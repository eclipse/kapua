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
package org.eclipse.kapua.transport.mqtt.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Available settings key for MQTT transport level
 *
 * @since 1.0.0
 */
public enum MqttClientSettingKeys implements SettingKey {

    /**
     * The MQTT protocol version to use.
     * 
     * @since 1.0.0
     */
    TRANSPORT_PROTOCOL_VERSION("transport.protocol.version"),

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
     * MQTT Client SystemToken Length
     */
    SYSTEM_TOKEN_LENGTH("system.token.length"),

    /**
     * SystemToken TTL (in seconds)
     */
    SYSTEM_TOKEN_TTL("system.token.ttl"),
    ;

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
    private MqttClientSettingKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the key for this {@link MqttClientSettingKeys}
     * 
     * @since 1.0.0
     */
    public String key() {
        return key;
    }
}
