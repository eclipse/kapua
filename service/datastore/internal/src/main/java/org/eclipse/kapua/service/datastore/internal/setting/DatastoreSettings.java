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
package org.eclipse.kapua.service.datastore.internal.setting;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * Datastore {@link AbstractKapuaSetting}.
 *
 * @since 1.0.0
 */
public class DatastoreSettings extends AbstractKapuaSetting<DatastoreSettingsKey> {

    /**
     * Resource file from which source properties.
     *
     * @since 1.0.0
     */
    private static final String DATASTORE_CONFIG_RESOURCE = "kapua-datastore-settings.properties";

    /**
     * Singleton instance of this {@link Class}.
     *
     * @since 1.0.0
     */
    private static final DatastoreSettings INSTANCE = new DatastoreSettings();

    /**
     * Initialize the {@link AbstractKapuaSetting} with the {@link DatastoreSettings#DATASTORE_CONFIG_RESOURCE} value.
     *
     * @since 1.0.0
     */
    private DatastoreSettings() {
        super(DATASTORE_CONFIG_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link DatastoreSettings}.
     *
     * @return A singleton instance of {@link DatastoreSettings}.
     * @since 1.0.0
     */
    public static DatastoreSettings getInstance() {
        return INSTANCE;
    }
}
