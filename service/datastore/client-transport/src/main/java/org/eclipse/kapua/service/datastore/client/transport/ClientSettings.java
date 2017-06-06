/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client.transport;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Datastore settings implementation
 * 
 * @since 1.0
 */
public class ClientSettings extends AbstractKapuaSetting<ClientSettingsKey> {

    private static final String DATASTORE_CONFIG_RESOURCE = "kapua-datastore-transport-client-setting.properties";

    private static final ClientSettings INSTANCE = new ClientSettings();

    private ClientSettings() {
        super(DATASTORE_CONFIG_RESOURCE);
    }

    /**
     * Get the datastore settings instance
     * 
     * @return
     */
    public static ClientSettings getInstance() {
        return INSTANCE;
    }
}
