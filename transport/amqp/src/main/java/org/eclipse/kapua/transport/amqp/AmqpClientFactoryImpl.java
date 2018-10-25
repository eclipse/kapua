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
package org.eclipse.kapua.transport.amqp;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.transport.TransportClientFactory;
import org.eclipse.kapua.transport.amqp.message.AmqpMessage;
import org.eclipse.kapua.transport.amqp.message.AmqpPayload;
import org.eclipse.kapua.transport.amqp.message.AmqpTopic;

import java.util.Map;

/**
 * Implementation of {@link TransportClientFactory} API for AQMP transport facade
 * 
 * @since 1.0.0
 *
 */
@KapuaProvider
public class AmqpClientFactoryImpl implements TransportClientFactory<AmqpTopic, AmqpPayload, AmqpMessage, AmqpMessage, AmqpFacade, AmqpClientConnectionOptions> {

    @Override
    public AmqpFacade getFacade(Map<String, Object> configParameters)
            throws KapuaException {
        return new AmqpFacade(formatNodeUri(configParameters.get("serverAddress").toString()), configParameters);
    }

    @Override
    public AmqpClientConnectionOptions newConnectOptions() {
        return new AmqpClientConnectionOptions();
    }

    private String formatNodeUri(String nodeAddress) {
        return SystemSetting.getInstance().getString(SystemSettingKey.BROKER_SCHEME) + "://" + nodeAddress + ":" + SystemSetting.getInstance().getString(SystemSettingKey.BROKER_PORT);
    }
}
