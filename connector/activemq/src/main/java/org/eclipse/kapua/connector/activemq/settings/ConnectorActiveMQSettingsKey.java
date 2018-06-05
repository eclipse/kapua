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
package org.eclipse.kapua.connector.activemq.settings;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * ConnectorActiveMQSettingsKey keys.
 * 
 * @since 1.0
 */
public enum ConnectorActiveMQSettingsKey implements SettingKey {

    /**
     * Broker name (or ip)
     */
    BROKER_HOST("connector.activemq.broker.host"),
    /**
     * Broker url
     */
    BROKER_PORT("connector.activemq.broker.port"),
    /**
     * Username
     */
    BROKER_USERNAME("connector.activemq.broker.username"),
    /**
     * Broker password
     */
    BROKER_PASSWORD("connector.activemq.broker.password"),
    /**
     * Broker client id
     */
    BROKER_CLIENT_ID("connector.activemq.broker.client_id"),
    /**
     * Maximum reconnection attempt (without any success between them) before exiting JVM (negative numbers means no exit)
     */
    MAX_RECONNECTION_ATTEMPTS("connector.activemq.maximum_reconnection_attempt"),
    /**
     * Exiting code when maximum reconnection attempt is reached
     */
    EXIT_CODE("connector.activemq.exit_code");

    private String key;

    private ConnectorActiveMQSettingsKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
