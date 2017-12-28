/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 *
 */
public interface CredentialService extends KapuaEntityService<Credential, CredentialCreator>, KapuaUpdatableEntityService<Credential>, KapuaConfigurableService {

    /**
     * Return the credential list result looking by user identifier (and also scope identifier)
     * 
     * @param scopeId
     * @param userId
     * @return
     * @throws KapuaException
     * 
     * @since 1.0
     */
    public CredentialListResult findByUserId(KapuaId scopeId, KapuaId userId)
            throws KapuaException;

    /**
     * Returns the {@link Credential} of type {@link CredentialType#API_KEY} matching the given parameters
     * 
     * @param tokenApiKey
     *            The API key to match
     * @return The matched {@link Credential}
     * @throws KapuaException
     * @since 1.0
     */
    public Credential findByApiKey(String tokenApiKey) throws KapuaException;

    /**
     * Queries for all users
     * 
     * @param query
     */
    public CredentialListResult query(KapuaQuery<Credential> query)
            throws KapuaException;

    /**
     * Unlocks a {@link Credential}
     * @param scopeId
     * @param credentialId
     * @throws KapuaException
     */
    public void unlock(KapuaId scopeId, KapuaId credentialId) throws KapuaException;
}
