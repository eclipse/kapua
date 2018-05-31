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
package org.eclipse.kapua.consumer.activemq.datastore.settings;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * ActiveMQDatastoreSettings implementation
 * 
 * @since 1.0
 */
public class ActiveMQDatastoreSettings extends AbstractKapuaSetting<ActiveMQDatastoreSettingsKey> {

    private static final String CONNECTOR_CONFIG_RESOURCE = "kapua-connector-activemq-datastore-setting.properties";

    private static final ActiveMQDatastoreSettings INSTANCE = new ActiveMQDatastoreSettings();

    private ActiveMQDatastoreSettings() {
        super(CONNECTOR_CONFIG_RESOURCE);
    }

    /**
     * Get the amqp client settings instance
     * 
     * @return
     */
    public static ActiveMQDatastoreSettings getInstance() {
        return INSTANCE;
    }
}
