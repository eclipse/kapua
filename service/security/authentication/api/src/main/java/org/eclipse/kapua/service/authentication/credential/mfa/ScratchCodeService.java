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
 * {@link ScratchCode} {@link KapuaService} definition.
 *
 * @since 1.3.0
 */
public interface ScratchCodeService extends KapuaService {

    /**
     * Finds the {@link ScratchCodeListResult}.
     *
     * @param scopeId The {@link MfaOption#getScopeId()}
     * @param mfaOptionId The {@link MfaOption#getId()}
     * @return A {@link ScratchCodeListResult} with matching items
     * @throws KapuaException
     * @since 1.3.0
     */
    ScratchCodeListResult findByMfaOptionId(KapuaId scopeId, KapuaId mfaOptionId) throws KapuaException;

    /**
     * Deletes a {@link ScratchCode}
     * @param scopeId The {@link ScratchCode#getScopeId()}
     * @param scratchCodeId The {@link ScratchCode#getId()}
     * @throws KapuaException
     * @since 1.3.0
     */
    void delete(KapuaId scopeId, KapuaId scratchCodeId) throws KapuaException;

}
