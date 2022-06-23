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

import org.eclipse.kapua.commons.setting.SettingKey;
import org.eclipse.kapua.extras.migrator.encryption.authentication.MfaOptionMigrator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;

/**
 * {@link SettingKey}s for {@link EncryptionMigrationSettings}.
 *
 * @since 2.0.0
 */
public enum EncryptionMigrationSettingKeys implements SettingKey {

    /**
     * Whether running in dry run mode ot not.
     *
     * @since 2.0.0
     */
    DRY_RUN("migrator.encryption.dryRun"),

    /**
     * The old encryption key.
     *
     * @since 2.0.0
     */
    OLD_ENCRYPTION_KEY("migrator.encryption.key.old"),

    /**
     * The old encryption key used in the {@link MfaOption#getMfaSecretKey()} encryption.
     * <p>
     * See {@link MfaOptionMigrator#getMfaSecretKey()}
     *
     * @since 2.0.0
     */
    MFA_OLD_ENCRYPTION_KEY("migrator.encryption.key.mfa"),

    /**
     * The old encryption key.
     *
     * @since 2.0.0
     */
    NEW_ENCRYPTION_KEY("migrator.encryption.key.new");

    /**
     * The key value of the {@link SettingKey}.
     *
     * @since 2.0.0
     */
    private final String key;

    /**
     * Constructor.
     *
     * @param key The key value of the {@link SettingKey}.
     * @since 2.0.0
     */
    EncryptionMigrationSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
