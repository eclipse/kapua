/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.mfa;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;

/**
 * {@link ScratchCode} service definition.
 */
public interface ScratchCodeService extends KapuaService {
    /**
     * Return the ScratchCode list result looking by {@link MfaOption} identifier (and also scope identifier)
     *
     * @param scopeId
     * @param mfaOptionId
     * @return
     * @throws KapuaException
     */
    ScratchCodeListResult findByMfaOptionId(KapuaId scopeId, KapuaId mfaOptionId) throws KapuaException;

    void delete(KapuaId scopeId, KapuaId scratchCodeId) throws KapuaException;

}
