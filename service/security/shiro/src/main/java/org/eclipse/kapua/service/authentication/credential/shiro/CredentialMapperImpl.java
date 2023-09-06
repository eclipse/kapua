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
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;

public class CredentialMapperImpl implements CredentialMapper {
    public CredentialMapperImpl(CredentialFactory credentialFactory,
                                KapuaAuthenticationSetting setting) {
        this.credentialFactory = credentialFactory;
        this.setting = setting;
    }

    private final CredentialFactory credentialFactory;
    private final KapuaAuthenticationSetting setting;

    @Override
    public Credential map(CredentialCreator credentialCreator) throws KapuaException {
        // Crypto credential
        String cryptedCredential;
        switch (credentialCreator.getCredentialType()) {
            case API_KEY:
                cryptedCredential = cryptApiKey(credentialCreator.getCredentialPlainKey());
                break;
            case PASSWORD:
            default:
                cryptedCredential = cryptPassword(credentialCreator.getCredentialPlainKey());
                break;
        }
        // Create Credential
        final Credential credential = credentialFactory.newEntity(credentialCreator.getScopeId());
        credential.setUserId(credentialCreator.getUserId());
        credential.setCredentialType(credentialCreator.getCredentialType());
        credential.setCredentialKey(cryptedCredential);
        credential.setStatus(credentialCreator.getCredentialStatus());
        credential.setExpirationDate(credentialCreator.getExpirationDate());
        return credential;
    }

    // Private methods
    private String cryptPassword(String credentialPlainKey) throws KapuaException {
        return AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, credentialPlainKey);
    }

    private String cryptApiKey(String credentialPlainKey) throws KapuaException {
        int preLength = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_LENGTH);
        String preSeparator = setting.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_SEPARATOR);

        String hashedValue = credentialPlainKey.substring(0, preLength); // Add the pre in clear text
        hashedValue += preSeparator; // Add separator
        hashedValue += AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, credentialPlainKey.substring(preLength, credentialPlainKey.length())); // Bcrypt the rest

        return hashedValue;
    }

}
