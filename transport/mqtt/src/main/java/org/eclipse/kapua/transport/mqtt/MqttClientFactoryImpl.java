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
package org.eclipse.kapua.transport.mqtt;

import com.google.common.base.Strings;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.transport.TransportClientFactory;
import org.eclipse.kapua.transport.exception.TransportClientGetException;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;

import java.util.Map;

/**
 * Implementation of {@link TransportClientFactory} API for MQTT transport facade
 *
 * @since 1.0.0
 */
@KapuaProvider
public class MqttClientFactoryImpl implements TransportClientFactory<MqttTopic, MqttPayload, MqttMessage, MqttMessage, MqttFacade, MqttClientConnectionOptions> {

    @Override
    public MqttFacade getFacade(Map<String, Object> configParameters) throws TransportClientGetException {
        String host = (String) configParameters.get("serverAddress");

        if (Strings.isNullOrEmpty(host)) {
            throw new TransportClientGetException(host);
        }

        return new MqttFacade(formatNodeUri(host));
    }

    @Override
    public MqttClientConnectionOptions newConnectOptions() {
        return new MqttClientConnectionOptions();
    }

    private String formatNodeUri(String nodeAddress) {
        return SystemSetting.getInstance().getString(SystemSettingKey.BROKER_SCHEME) + "://" + nodeAddress + ":" + SystemSetting.getInstance().getString(SystemSettingKey.BROKER_INTERNAL_CONNECTOR_PORT);
    }
}
