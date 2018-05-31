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
package org.eclipse.kapua.connector.amqp.settings;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * ConnectorSettingsKey keys.
 * 
 * @since 1.0
 */
public enum ConnectorSettingsKey implements SettingKey {

    /**
     * Broker name (or ip)
     */
    BROKER_HOST("broker.host"),
    /**
     * Broker url
     */
    BROKER_PORT("broker.port"),
    /**
     * Username
     */
    BROKER_USERNAME("broker.username"),
    /**
     * Broker password
     */
    BROKER_PASSWORD("broker.password"),
    /**
     * Broker client id
     */
    BROKER_CLIENT_ID("broker.client_id");

    private String key;

    private ConnectorSettingsKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
