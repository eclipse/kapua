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
package org.eclipse.kapua.extras.migrator.encryption.authentication;

import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.crypto.setting.CryptoSettingKeys;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.extras.migrator.encryption.settings.EncryptionMigrationSettingKeys;
import org.eclipse.kapua.extras.migrator.encryption.settings.EncryptionMigrationSettings;
import org.eclipse.kapua.extras.migrator.encryption.utils.SecretAttributeMigratorModelUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCode;
import org.eclipse.kapua.service.job.step.JobStep;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * {@link JobStep} implementation.
 *
 * @since 2.0.0
 */
@Entity(name = "MfaOption")
@Table(name = "atht_mfa_option")
public class MfaOptionMigrator extends AbstractKapuaUpdatableEntity implements MfaOption {

    private static final String OLD_MFA_ENCRYPTION_KEY = EncryptionMigrationSettings.getInstance().getString(EncryptionMigrationSettingKeys.MFA_OLD_ENCRYPTION_KEY);

    @Basic
    @Column(name = "mfa_secret_key", nullable = false)
    private String mfaSecretKey;

    /**
     * Constructor.
     *
     * @since 2.0.0
     */
    public MfaOptionMigrator() {
    }

    /**
     * Clone constructor.
     *
     * @param mfaOption The {@link MfaOption} to clone.
     * @since 2.0.0
     */
    public MfaOptionMigrator(MfaOption mfaOption) {
        super(mfaOption);

        setMfaSecretKey(mfaOption.getMfaSecretKey());
    }

    /**
     * Before reading the value for the migration when changing the {@link CryptoSettingKeys#CRYPTO_SECRET_KEY} we need
     * decrypt the value with the original encryption key and way to encrypt information.
     *
     * @return The decrypted MFA secret key.
     * @since 2.0.0
     */
    @Override
    public String getMfaSecretKey() {
        if (mfaSecretKey != null && !mfaSecretKey.startsWith("$aes$")) {
            try {
                byte[] oldMfaEncryptionKey = OLD_MFA_ENCRYPTION_KEY.getBytes();

                Key key = new SecretKeySpec(oldMfaEncryptionKey, "AES");

                Cipher decryptCipher = Cipher.getInstance("AES");
                decryptCipher.init(Cipher.DECRYPT_MODE, key);

                byte[] decryptedValueBase64 = java.util.Base64.getUrlDecoder().decode(mfaSecretKey);
                byte[] decryptedValueBytes = decryptCipher.doFinal(decryptedValueBase64);

                mfaSecretKey = new String(decryptedValueBytes);
            } catch (Exception e) {
                throw KapuaRuntimeException.internalError(e, "Cannot decrypt MfaOption.secretKey value with original MFA encryption key");
            }
        }

        return SecretAttributeMigratorModelUtils.read(mfaSecretKey);
    }

    @Override
    public void setMfaSecretKey(String mfaSecretKey) {
        this.mfaSecretKey = SecretAttributeMigratorModelUtils.write(mfaSecretKey);
    }

    //
    // Attributes below do not require migration
    //

    @Override
    public KapuaId getUserId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUserId(KapuaId userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTrustKey() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTrustKey(String trustKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getTrustExpirationDate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTrustExpirationDate(Date trustExpirationDate) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getQRCodeImage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setQRCodeImage(String qrCodeImage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ScratchCode> getScratchCodes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setScratchCodes(List<ScratchCode> scratchCodes) {
        throw new UnsupportedOperationException();
    }
}
