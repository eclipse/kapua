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

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Class that offers access to all setting of {@link org.eclipse.kapua.transport.mqtt}
 *
 * @since 1.0.0
 */
public class MqttClientSetting extends AbstractKapuaSetting<MqttClientSettingKeys> {

    /**
     * Resource file from which source properties.
     *
     * @since 1.0.0
     */
    private static final String MQTT_CLIENT_CONFIG_RESOURCE = "mqtt-client-setting.properties";

    /**
     * Singleton instance of this {@link Class}.
     *
     * @since 1.0.0
     */
    private static final MqttClientSetting INSTANCE = new MqttClientSetting();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link MqttClientSetting#MQTT_CLIENT_CONFIG_RESOURCE} value.
     *
     * @since 1.0.0
     */
    private MqttClientSetting() {
        super(MQTT_CLIENT_CONFIG_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link MqttClientSetting}.
     *
     * @return A singleton instance of {@link MqttClientSetting}.
     * @since 1.0.0
     */
    public static MqttClientSetting getInstance() {
        return INSTANCE;
    }
}
