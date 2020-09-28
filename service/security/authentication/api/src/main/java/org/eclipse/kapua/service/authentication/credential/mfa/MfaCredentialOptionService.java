/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.mfa;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;

/**
 * MFA Credential service definition.
 */
public interface MfaCredentialOptionService extends KapuaEntityService<MfaCredentialOption, MfaCredentialOptionCreator>,
        KapuaUpdatableEntityService<MfaCredentialOption> {

    /**
     * Return the MfaCredentialOption result looking by user identifier (and also scope identifier)
     *
     * @param scopeId
     * @param userId
     * @return
     * @throws KapuaException
     */
    MfaCredentialOption findByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException;

    /**
     * Enables the trust machine for the given {@link MfaCredentialOption}.
     *
     * @param mfaCredentialOption the {@link MfaCredentialOption}
     */
    void enableTrust(MfaCredentialOption mfaCredentialOption) throws KapuaException;

    /**
     * Enables the trust machine for the {@link KapuaId} of the {@link MfaCredentialOption}.
     *
     * @param scopeId the scopeId
     * @param mfaCredentialOptionId the {@link KapuaId} of the {@link MfaCredentialOption}
     */
    void enableTrust(KapuaId scopeId, KapuaId mfaCredentialOptionId) throws KapuaException;

    /**
     * Disable the trust machine for the given {@link MfaCredentialOption}.
     *
     * @param scopeId the scopeid
     * @param mfaCredentialOptionId the {@link KapuaId} of the {@link MfaCredentialOption}
     */
    void disableTrust(KapuaId scopeId, KapuaId mfaCredentialOptionId) throws KapuaException;
}
