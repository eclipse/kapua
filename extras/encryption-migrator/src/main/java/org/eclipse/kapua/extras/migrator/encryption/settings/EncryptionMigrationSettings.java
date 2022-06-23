/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.extras.migrator.encryption.settings;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

/**
 * {@link EncryptionMigrationSettings} for {@code kapua-encryption-migrator} module.
 *
 * @see AbstractKapuaSetting
 * @since 2.0.0
 */
public class EncryptionMigrationSettings extends AbstractKapuaSetting<EncryptionMigrationSettingKeys> {

    /**
     * Setting filename.
     *
     * @since 2.0.0
     */
    private static final String SETTINGS_RESOURCE = "encryption-migrator-settings.properties";

    /**
     * Singleton instance.
     *
     * @since 2.0.0
     */
    private static final EncryptionMigrationSettings INSTANCE = new EncryptionMigrationSettings();

    /**
     * Constructor.
     *
     * @since 2.0.0
     */
    private EncryptionMigrationSettings() {
        super(SETTINGS_RESOURCE);
    }

    /**
     * Gets a singleton instance of {@link EncryptionMigrationSettings}.
     *
     * @return A singleton instance of {@link EncryptionMigrationSettings}.
     * @since 2.0.0
     */
    public static EncryptionMigrationSettings getInstance() {
        return INSTANCE;
    }
}
