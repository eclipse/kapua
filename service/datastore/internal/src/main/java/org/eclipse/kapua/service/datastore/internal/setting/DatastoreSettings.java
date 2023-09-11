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

import javax.inject.Inject;

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
     * Initialize the {@link AbstractKapuaSetting} with the {@link DatastoreSettings#DATASTORE_CONFIG_RESOURCE} value.
     *
     * @since 1.0.0
     */
    @Inject
    public DatastoreSettings() {
        super(DATASTORE_CONFIG_RESOURCE);
    }

}
