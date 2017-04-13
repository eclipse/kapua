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
package org.eclipse.kapua.transport.mqtt.pooling.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Class that offers access to all setting of {@link org.eclipse.kapua.transport.mqtt.pooling}
 *
 * @since 1.0.0
 */
public class MqttClientPoolSetting extends AbstractKapuaSetting<MqttClientPoolSettingKeys> {

    /**
     * Resource file from which source properties.
     * 
     * @since 1.0.0
     */
    private static final String MQTT_CLIENT_POOL_CONFIG_RESOURCE = "mqtt-client-pool-setting.properties";

    /**
     * Singleton instance of this {@link class}.
     * 
     * @since 1.0.0
     */
    private static final MqttClientPoolSetting instance = new MqttClientPoolSetting();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link MqttClientPoolSetting#MQTT_CLIENT_POOL_CONFIG_RESOURCE} value.
     * 
     * @since 1.0.0
     */
    private MqttClientPoolSetting() {
        super(MQTT_CLIENT_POOL_CONFIG_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link MqttClientPoolSetting}.
     * 
     * @return A singleton instance of MqttClientPoolSetting.
     * @since 1.0.0
     */
    public static MqttClientPoolSetting getInstance() {
        return instance;
    }
}
