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
package org.eclipse.kapua.extras.migrator.encryption.utils;

import com.google.common.base.Strings;
import org.eclipse.kapua.commons.crypto.CryptoUtil;
import org.eclipse.kapua.commons.crypto.exception.AesDecryptionException;
import org.eclipse.kapua.commons.crypto.exception.AesEncryptionException;
import org.eclipse.kapua.extras.migrator.encryption.settings.EncryptionMigrationSettingKeys;
import org.eclipse.kapua.extras.migrator.encryption.settings.EncryptionMigrationSettings;

import javax.persistence.PersistenceException;

/**
 * @since 2.0.0
 */
public class SecretAttributeMigratorModelUtils {

    private static final String AES_V1 = "$aes$";

    private static final String OLD_ENCRYPTION_KEY = EncryptionMigrationSettings.getInstance().getString(EncryptionMigrationSettingKeys.OLD_ENCRYPTION_KEY);
    private static final String NEW_ENCRYPTION_KEY = EncryptionMigrationSettings.getInstance().getString(EncryptionMigrationSettingKeys.NEW_ENCRYPTION_KEY);

    private SecretAttributeMigratorModelUtils() {
    }

    public static String read(String databaseValue) {
        if (Strings.isNullOrEmpty(databaseValue)) {
            return databaseValue;
        }

        // Handling encryption versions
        if (databaseValue.startsWith(AES_V1)) {
            try {
                return CryptoUtil.decryptAes(databaseValue.substring(AES_V1.length()), OLD_ENCRYPTION_KEY);
            } catch (AesDecryptionException e) {
                throw new PersistenceException("Cannot read value from entity", e);
            }
        } else {
            return databaseValue;
        }
    }

    public static String write(String entityAttribute) {
        if (Strings.isNullOrEmpty(entityAttribute)) {
            return entityAttribute;
        }

        try {
            return AES_V1 + CryptoUtil.encryptAes(entityAttribute, NEW_ENCRYPTION_KEY);
        } catch (AesEncryptionException e) {
            throw new PersistenceException("Cannot write value to entity", e);
        }
    }
}
