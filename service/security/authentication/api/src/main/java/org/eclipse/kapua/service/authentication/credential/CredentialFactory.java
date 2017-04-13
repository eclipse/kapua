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
package org.eclipse.kapua.service.authentication.credential;

import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Credential factory service definition.
 *
 * @since 1.0
 *
 */
public interface CredentialFactory extends KapuaEntityFactory<Credential, CredentialCreator, CredentialQuery, CredentialListResult> {

    /**
     * Create a new {@link Credential}
     *
     * @param scopeId
     * @param userId
     * @param credentialType
     * @param credentialKey
     * @return
     */
    public Credential newCredential(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey);

    /**
     * Create a new {@link CredentialCreator} for the specific credential type
     *
     * @param scopeId
     * @param userId
     * @param credentialType
     * @param credentialKey
     * @return
     */
    public CredentialCreator newCreator(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey);

}
