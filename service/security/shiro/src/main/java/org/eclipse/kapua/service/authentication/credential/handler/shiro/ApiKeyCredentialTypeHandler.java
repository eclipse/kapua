/*******************************************************************************
 * Copyright (c) 2024, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.handler.shiro;


import org.apache.shiro.codec.Base64;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.RandomUtils;
import org.eclipse.kapua.service.authentication.credential.handler.CredentialTypeHandler;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * API Key {@link CredentialTypeHandler} implementation
 *
 * @since 2.1.0
 */
public class ApiKeyCredentialTypeHandler implements CredentialTypeHandler {

    public static final String TYPE = "API_KEY";

    private final KapuaAuthenticationSetting authenticationSetting;

    private final AuthenticationUtils authenticationUtils;

    /**
     * FIXME: this should be replaced by {@link RandomUtils} when it will be promoted to injectable collaborator
     */
    private final SecureRandom random;

    @Inject
    public ApiKeyCredentialTypeHandler(KapuaAuthenticationSetting authenticationSetting, AuthenticationUtils authenticationUtils) {
        this.authenticationSetting = authenticationSetting;
        this.authenticationUtils = authenticationUtils;

        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw KapuaRuntimeException.internalError(e, "Cannot instantiate SecureRandom (SHA1PRNG)");
        }
    }

    @Override
    public String getName() {
        return TYPE;
    }

    @Override
    public void validateCreator(CredentialCreator credentialCreator) throws KapuaIllegalArgumentException {
        ArgumentValidator.isNull(credentialCreator.getCredentialPlainKey(), "credentialCreator.credentialKey");
    }

    @Override
    public boolean isServiceGenerated() {
        return true;
    }

    @Override
    public String generate() {
        int preLength = authenticationSetting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_LENGTH);
        int keyLength = authenticationSetting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_KEY_LENGTH);

        byte[] bPre = new byte[preLength];
        random.nextBytes(bPre);
        String pre = Base64.encodeToString(bPre).substring(0, preLength);

        byte[] bKey = new byte[keyLength];
        random.nextBytes(bKey);
        String key = Base64.encodeToString(bKey);

        return pre + key;
    }

    @Override
    public String cryptCredentialKey(String credentialPlainKey) throws KapuaException {
        int preLength = authenticationSetting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_LENGTH);
        String preSeparator = authenticationSetting.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_SEPARATOR);

        String hashedValue = credentialPlainKey.substring(0, preLength); // Add the pre in clear text
        hashedValue += preSeparator; // Add separator
        hashedValue += authenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, credentialPlainKey.substring(preLength)); // Bcrypt the rest

        return hashedValue;
    }
}
