/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.server.embedded;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Elasticsearch embedded {@link AbstractKapuaSetting}.
 *
 * @since 1.0.0
 */
public class EmbeddedNodeSettings extends AbstractKapuaSetting<EmbeddedNodeSettingsKeys> {

    /**
     * The {@link AbstractKapuaSetting} filename.
     *
     * @since 1.0.0
     */
    private static final String DATASTORE_CONFIG_RESOURCE = "kapua-datastore-embedded-server-setting.properties";

    /**
     * The {@link EmbeddedNodeSettings} instance.
     *
     * @since 1.0.0
     */
    private static final EmbeddedNodeSettings INSTANCE = new EmbeddedNodeSettings();

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    private EmbeddedNodeSettings() {
        super(DATASTORE_CONFIG_RESOURCE);
    }

    /**
     * Gets the {@link EmbeddedNodeSettings}
     *
     * @return The {@link EmbeddedNodeSettings}
     * @since 1.0.0
     */
    public static EmbeddedNodeSettings getInstance() {
        return INSTANCE;
    }
}
