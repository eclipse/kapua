/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.shiro.utils.AuthenticationUtils;
import org.eclipse.kapua.service.authentication.shiro.utils.CryptAlgorithm;

/**
 * {@link Credential} {@link ServiceDAO}
 *
 * @since 1.0
 */
public class CredentialDAO extends ServiceDAO {

    /**
     * Creates and return new credential
     *
     * @param em
     * @param credentialCreator
     * @return
     * @throws KapuaException
     */
    public static Credential create(EntityManager em, CredentialCreator credentialCreator)
            throws KapuaException {

        //
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

        //
        // Create Credential
        CredentialImpl credentialImpl = new CredentialImpl(credentialCreator.getScopeId(),
                credentialCreator.getUserId(),
                credentialCreator.getCredentialType(),
                cryptedCredential,
                credentialCreator.getCredentialStatus(),
                credentialCreator.getExpirationDate());

        //
        // Do create
        return ServiceDAO.create(em, credentialImpl);
    }

    /**
     * Update the provided credential
     *
     * @param em
     * @param credential
     * @return
     * @throws KapuaException
     */
    public static Credential update(EntityManager em, Credential credential)
            throws KapuaException {
        //
        // Update credential
        CredentialImpl credentialImpl = (CredentialImpl) credential;

        return ServiceDAO.update(em, CredentialImpl.class, credentialImpl);
    }

    /**
     * Find the credential by credential identifier
     *
     * @param em
     * @param scopeId
     * @param credentialId
     * @return
     */
    public static Credential find(EntityManager em, KapuaId scopeId, KapuaId credentialId) {
        return ServiceDAO.find(em, CredentialImpl.class, scopeId, credentialId);
    }

    /**
     * Return the credential list matching the provided query
     *
     * @param em
     * @param credentialQuery
     * @return
     * @throws KapuaException
     */
    public static CredentialListResult query(EntityManager em, KapuaQuery<Credential> credentialQuery)
            throws KapuaException {
        return ServiceDAO.query(em, Credential.class, CredentialImpl.class, new CredentialListResultImpl(), credentialQuery);
    }

    /**
     * Return the credential count matching the provided query
     *
     * @param em
     * @param credentialQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<Credential> credentialQuery)
            throws KapuaException {
        return ServiceDAO.count(em, Credential.class, CredentialImpl.class, credentialQuery);
    }

    /**
     * Delete the credential by credential identifier
     *
     * @param em
     * @param scopeId
     * @param credentialId
     * @throws KapuaEntityNotFoundException If {@link Credential} is now found.
     */
    public static void delete(EntityManager em, KapuaId scopeId, KapuaId credentialId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, CredentialImpl.class, scopeId, credentialId);
    }

    //
    // Private methods

    //
    private static String cryptPassword(String credentialPlainKey) throws KapuaException {
        return AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, credentialPlainKey);
    }

    private static String cryptApiKey(String credentialPlainKey) throws KapuaException {
        KapuaAuthenticationSetting setting = KapuaAuthenticationSetting.getInstance();
        int preLength = setting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_LENGTH);
        String preSeparator = setting.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_CREDENTIAL_APIKEY_PRE_SEPARATOR);

        String hashedValue = credentialPlainKey.substring(0, preLength); // Add the pre in clear text
        hashedValue += preSeparator; // Add separator
        hashedValue += AuthenticationUtils.cryptCredential(CryptAlgorithm.BCRYPT, credentialPlainKey.substring(preLength, credentialPlainKey.length())); // Bcrypt the rest

        return hashedValue;
    }
}
