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
     * @param mfaCredentialOptionId The {@link MfaCredentialOption} {@link KapuaId} to set into the
     *                              {@link ScratchCode}.
     * @param code                  The code to set into the {@link ScratchCode}.
     * @return The newly instantiated {@link ScratchCode}
     */
    ScratchCode newScratchCode(KapuaId scopeId, KapuaId mfaCredentialOptionId, String code);

    /**
     * Instantiates a new {@link ScratchCodeCreator}.
     *
     * @param scopeId               The scope {@link KapuaId} to set into the {@link ScratchCodeCreator}.
     * @param mfaCredentialOptionId The {@link MfaCredentialOption} {@link KapuaId} to set into the
     *                              {@link ScratchCode}.
     * @param code                  The code to set into the {@link ScratchCode}.
     * @return The newly instantiated {@link ScratchCodeCreator}
     */
    ScratchCodeCreator newCreator(KapuaId scopeId, KapuaId mfaCredentialOptionId, String code);

}
