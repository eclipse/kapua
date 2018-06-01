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

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * ConnectorActiveMQSettings implementation
 * 
 * @since 1.0
 */
public class ConnectorActiveMQSettings extends AbstractKapuaSetting<ConnectorActiveMQSettingsKey> {

    private static final String CONNECTOR_CONFIG_RESOURCE = "kapua-connector-activemq-setting.properties";

    private static final ConnectorActiveMQSettings INSTANCE = new ConnectorActiveMQSettings();

    private ConnectorActiveMQSettings() {
        super(CONNECTOR_CONFIG_RESOURCE);
    }

    /**
     * Get the datastore settings instance
     * 
     * @return
     */
    public static ConnectorActiveMQSettings getInstance() {
        return INSTANCE;
    }
}
