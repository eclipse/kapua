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
package org.eclipse.kapua.service.authentication.credential;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * Credential service definition.
 *
 * @since 1.0
 */
public interface CredentialService extends KapuaEntityService<Credential, CredentialCreator>,
        KapuaUpdatableEntityService<Credential>,
        KapuaConfigurableService {

    /**
     * Return the credential list result looking by user identifier (and also scope identifier)
     *
     * @param scopeId
     * @param userId
     * @return
     * @throws KapuaException
     * @since 1.0
     */
    CredentialListResult findByUserId(KapuaId scopeId, KapuaId userId)
            throws KapuaException;

    /**
     * Returns the {@link Credential} of type {@link CredentialType#API_KEY} matching the given parameters
     *
     * @param tokenApiKey The API key to match
     * @return The matched {@link Credential}
     * @throws KapuaException
     * @since 1.0
     */
    Credential findByApiKey(String tokenApiKey) throws KapuaException;

    /**
     * Queries for all users
     *
     * @param query
     */
    @Override
    CredentialListResult query(KapuaQuery query)
            throws KapuaException;

    /**
     * Unlocks a {@link Credential}
     *
     * @param scopeId
     * @param credentialId
     * @throws KapuaException
     */
    void unlock(KapuaId scopeId, KapuaId credentialId) throws KapuaException;

    /**
     * Returns the minimum password length according to account setting and system default
     * @param scopeId           The id of the Account to check the setting
     * @return                  The minimum required password length
     * @throws KapuaException   When something goes wrong
     */
    int getMinimumPasswordLength(KapuaId scopeId) throws KapuaException;
}
