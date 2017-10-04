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
package org.eclipse.kapua.transport.mqtt;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.transport.TransportClientFactory;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;

import java.util.Map;

/**
 * Implementation of {@link TransportClientFactory} API for MQTT transport facade
 * 
 * @since 1.0.0
 *
 */
@KapuaProvider
public class MqttClientFactoryImpl implements TransportClientFactory<MqttTopic, MqttPayload, MqttMessage, MqttMessage, MqttFacade, MqttClientConnectionOptions> {

    @Override
    public MqttFacade getFacade(Map<String, Object> configParameters)
            throws KapuaException {
        return new MqttFacade(formatBrokerUri(configParameters.get("serverIp").toString()));
    }

    @Override
    public MqttClientConnectionOptions newConnectOptions() {
        return new MqttClientConnectionOptions();
    }

    private String formatBrokerUri(String serverIp) {
        return SystemSetting.getInstance().getString(SystemSettingKey.BROKER_SCHEME) + "://" + serverIp + ":" + SystemSetting.getInstance().getString(SystemSettingKey.BROKER_PORT);
    }
}
