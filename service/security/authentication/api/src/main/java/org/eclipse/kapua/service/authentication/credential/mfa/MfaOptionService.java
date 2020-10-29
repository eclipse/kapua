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
 * {@link MfaOption} service definition.
 */
public interface MfaOptionService extends KapuaEntityService<MfaOption, MfaOptionCreator>,
        KapuaUpdatableEntityService<MfaOption> {

    /**
     * Return the {@link MfaOption} result looking by user identifier (and also scope identifier)
     *
     * @param scopeId
     * @param userId
     * @return
     * @throws KapuaException
     */
    MfaOption findByUserId(KapuaId scopeId, KapuaId userId) throws KapuaException;

    /**
     * Enables the trust machine for the given {@link MfaOption}.
     *
     * @param mfaOption the {@link MfaOption}
     */
    void enableTrust(MfaOption mfaOption) throws KapuaException;

    /**
     * Enables the trust machine for the {@link KapuaId} of the {@link MfaOption}.
     *
     * @param scopeId the scopeId
     * @param mfaOptionId the {@link KapuaId} of the {@link MfaOption}
     */
    void enableTrust(KapuaId scopeId, KapuaId mfaOptionId) throws KapuaException;

    /**
     * Disable the trust machine for the given {@link MfaOption}.
     *
     * @param scopeId the scopeid
     * @param mfaOptionId the {@link KapuaId} of the {@link MfaOption}
     */
    void disableTrust(KapuaId scopeId, KapuaId mfaOptionId) throws KapuaException;
}
