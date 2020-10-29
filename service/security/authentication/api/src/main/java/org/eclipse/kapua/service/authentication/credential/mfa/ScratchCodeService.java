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
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;

/**
 * ScratchCode service definition.
 */
public interface ScratchCodeService extends KapuaEntityService<ScratchCode, ScratchCodeCreator>, KapuaUpdatableEntityService<ScratchCode> {

    /**
     * Generates all the scratch codes.
     * The number of generated scratch codes is decided through the MfaAuthenticationService.
     * The scratch code provided within the scratchCodeCreator parameter is ignored.
     *
     * @param scratchCodeCreator
     * @return
     * @throws KapuaException
     */
    ScratchCodeListResult createAllScratchCodes(ScratchCodeCreator scratchCodeCreator) throws KapuaException;

    /**
     * Return the ScratchCode list result looking by MfaCredentialOption identifier (and also scope identifier)
     *
     * @param scopeId
     * @param mfaCredentialOptionId
     * @return
     * @throws KapuaException
     */
    ScratchCodeListResult findByMfaCredentialOptionId(KapuaId scopeId, KapuaId mfaCredentialOptionId) throws KapuaException;


    /**
     * Queries for all MfaCredentialOption
     *
     * @param query
     */
    @Override
    ScratchCodeListResult query(KapuaQuery query) throws KapuaException;
}
