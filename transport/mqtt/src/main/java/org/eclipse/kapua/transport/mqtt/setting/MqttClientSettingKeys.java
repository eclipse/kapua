/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.transport.mqtt.setting;

import org.eclipse.kapua.commons.setting.SettingKey;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;

/**
 * Available settings key for MQTT transport level
 *
 * @since 1.0.0
 */
public enum MqttClientSettingKeys implements SettingKey {

    /**
     * The username to use for MQTT connection
     *
     * @since 1.0.0
     */
    TRANSPORT_CREDENTIAL_USERNAME("transport.credential.username"),

    /**
     * The password to use for MQTT connection
     *
     * @since 1.0.0
     */
    TRANSPORT_CREDENTIAL_PASSWORD("transport.credential.password"),

    /**
     * The max length of the {@link MqttPayload#getBody()} used when invoking {@link MqttPayload#toString()}
     *
     * @since 1.2.0
     */
    PAYLOAD_TOSTRING_LENGTH("transport.mqtt.payload.body.toString.length"),

    /**
     * The character separator for topic levels.
     *
     * @since 1.0.0
     */
    TRANSPORT_TOPIC_SEPARATOR("transport.mqtt.topic.separator"),

    /**
     * The MQTT protocol version to use.
     *
     * @since 1.0.0
     */
    TRANSPORT_PROTOCOL_VERSION("transport.mqtt.protocol.version"),

    /**
     * Timeout for send sync operations.
     *
     * @since 1.0.0
     * @deprecated Since 1.2.0. It is not used.
     */
    @Deprecated
    SEND_TIMEOUT_MAX("transport.send.timeout.max"),
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
     * @param key The value mapped by this {@link Enum} value
     * @since 1.0.0
     */
    MqttClientSettingKeys(String key) {
        this.key = key;
    }

    /**
     * Gets the key for this {@link MqttClientSettingKeys}
     *
     * @since 1.0.0
     */
    @Override
    public String key() {
        return key;
    }
}
