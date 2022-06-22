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
package org.eclipse.kapua.commons.jpa;

import com.google.common.base.Strings;
import org.eclipse.kapua.commons.crypto.CryptoUtil;
import org.eclipse.kapua.commons.crypto.exception.AesDecryptionException;
import org.eclipse.kapua.commons.crypto.exception.AesEncryptionException;
import org.eclipse.kapua.model.KapuaEntityAttributes;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.PersistenceException;

/**
 * {@link AttributeConverter} for {@link KapuaEntityAttributes} that need to be two-way encrypted.
 *
 * @since 2.0.0
 */
@Converter
public class SecretAttributeConverter implements AttributeConverter<String, String> {

    private static final String AES_V1 = "$aes$";

    @Override
    public String convertToDatabaseColumn(String entityAttribute) {
        if (Strings.isNullOrEmpty(entityAttribute)) {
            return entityAttribute;
        }

        try {
            return AES_V1 + CryptoUtil.encryptAes(entityAttribute);
        } catch (AesEncryptionException e) {
            throw new PersistenceException("Cannot write value to database", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String databaseValue) {
        if (Strings.isNullOrEmpty(databaseValue)) {
            return databaseValue;
        }

        // Handling encryption versions
        if (databaseValue.startsWith(AES_V1)) {
            try {
                return CryptoUtil.decryptAes(databaseValue.substring(AES_V1.length()));
            } catch (AesDecryptionException e) {
                throw new PersistenceException("Cannot read value from database", e);
            }
        } else {
            return databaseValue;
        }
    }
}
