/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.transport.amqp;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.transport.TransportClientFactory;
import org.eclipse.kapua.transport.amqp.message.AmqpMessage;
import org.eclipse.kapua.transport.amqp.message.AmqpPayload;
import org.eclipse.kapua.transport.amqp.message.AmqpTopic;
import org.eclipse.kapua.transport.exception.TransportClientGetException;

import java.util.Map;

/**
 * Implementation of {@link TransportClientFactory} API for AQMP transport facade
 * 
 * @since 1.6.0
 *
 */
@KapuaProvider
public class AmqpClientFactoryImpl implements TransportClientFactory<AmqpTopic, AmqpPayload, AmqpMessage, AmqpMessage, AmqpFacade, AmqpClientConnectionOptions> {

    @Override
    public AmqpFacade getFacade(Map<String, Object> configParameters)
            throws TransportClientGetException {
        try {
            return new AmqpFacade(formatNodeUri(configParameters.get("serverAddress").toString()));
        } catch (KapuaException e) {
                throw new TransportClientGetException(e, configParameters.get("serverAddress").toString());
        }
    }

    @Override
    public AmqpClientConnectionOptions newConnectOptions() {
        return new AmqpClientConnectionOptions();
    }

    //can we use SystemUtils.getNodeURI?
    private String formatNodeUri(String nodeAddress) {
        return SystemSetting.getInstance().getString(SystemSettingKey.BROKER_SCHEME) + "://" + nodeAddress + ":" + SystemSetting.getInstance().getString(SystemSettingKey.BROKER_INTERNAL_CONNECTOR_PORT);
    }
}
