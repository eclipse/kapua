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

import javax.inject.Inject;

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
     * Constructor.
     *
     * @since 2.0.0
     */
    @Inject
    public EncryptionMigrationSettings() {
        super(SETTINGS_RESOURCE);
    }
}
