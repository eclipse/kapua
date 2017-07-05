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
package org.eclipse.kapua.service.datastore.client.embedded;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Datastore ES embedded node settings implementation
 * 
 * @since 1.0
 */
public class EmbeddedNodeSettings extends AbstractKapuaSetting<EmbeddedNodeSettingsKey> {

    private static final String DATASTORE_CONFIG_RESOURCE = "kapua-datastore-embedded-client-setting.properties";

    private static final EmbeddedNodeSettings INSTANCE = new EmbeddedNodeSettings();

    private EmbeddedNodeSettings() {
        super(DATASTORE_CONFIG_RESOURCE);
    }

    /**
     * Get the datastore ES embedded node settings instance
     * 
     * @return
     */
    public static EmbeddedNodeSettings getInstance() {
        return INSTANCE;
    }
}
