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
import org.eclipse.kapua.service.authentication.token.AccessToken;

/**
 * {@link MfaCredentialOptionFactory} definition.
 *
 * @see KapuaEntityFactory
 */
public interface MfaCredentialOptionFactory extends KapuaEntityFactory<MfaCredentialOption, MfaCredentialOptionCreator, MfaCredentialOptionQuery,
        MfaCredentialOptionListResult> {

    /**
     * Instantiates a new {@link MfaCredentialOption}.
     *
     * @param scopeId          The scope {@link KapuaId} to set into the {@link MfaCredentialOption}.
     * @param userId           The {@link org.eclipse.kapua.service.user.User} {@link KapuaId} to set into the{@link AccessToken}.
     * @param mfaCredentialKey The key to set into the {@link MfaCredentialOption}.
     * @return The newly instantiated {@link MfaCredentialOption}
     */
    MfaCredentialOption newMfaCredentialOption(KapuaId scopeId, KapuaId userId, String mfaCredentialKey);

    /**
     * Instantiates a new {@link MfaCredentialOptionCreator}.
     *
     * @param scopeId          The scope {@link KapuaId} to set into the {@link MfaCredentialOptionCreator}.
     * @param userId           The {@link org.eclipse.kapua.service.user.User} {@link KapuaId} to set into the{@link AccessToken}.
     * @param mfaCredentialKey The key to set into the {@link MfaCredentialOptionCreator}.
     * @return The newly instantiated {@link MfaCredentialOptionCreator}
     */
    MfaCredentialOptionCreator newCreator(KapuaId scopeId, KapuaId userId, String mfaCredentialKey);

}
