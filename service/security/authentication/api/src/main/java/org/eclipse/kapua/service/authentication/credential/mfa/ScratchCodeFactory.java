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

import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link ScratchCodeFactory} definition.
 *
 * @see KapuaEntityFactory
 */
public interface ScratchCodeFactory extends KapuaEntityFactory<ScratchCode, ScratchCodeCreator, ScratchCodeQuery, ScratchCodeListResult> {

    /**
     * Instantiates a new {@link ScratchCode}.
     *
     * @param scopeId               The scope {@link KapuaId} to set into the {@link ScratchCode}.
     * @param mfaOptionId The {@link MfaOption} {@link KapuaId} to set into the
     *                              {@link ScratchCode}.
     * @param code                  The code to set into the {@link ScratchCode}.
     * @return The newly instantiated {@link ScratchCode}
     */
    ScratchCode newScratchCode(KapuaId scopeId, KapuaId mfaOptionId, String code);

    /**
     * Instantiates a new {@link ScratchCodeCreator}.
     *
     * @param scopeId               The scope {@link KapuaId} to set into the {@link ScratchCodeCreator}.
     * @param mfaOptionId The {@link MfaOption} {@link KapuaId} to set into the
     *                              {@link ScratchCode}.
     * @param code                  The code to set into the {@link ScratchCode}.
     * @return The newly instantiated {@link ScratchCodeCreator}
     */
    ScratchCodeCreator newCreator(KapuaId scopeId, KapuaId mfaOptionId, String code);

}
