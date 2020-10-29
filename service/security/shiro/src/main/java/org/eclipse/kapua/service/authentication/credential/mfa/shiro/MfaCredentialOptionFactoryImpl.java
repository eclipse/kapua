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
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionFactory;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionQuery;

/**
 * {@link MfaCredentialOptionFactory} implementation.
 */
@KapuaProvider
public class MfaCredentialOptionFactoryImpl implements MfaCredentialOptionFactory {

    @Override
    public MfaCredentialOptionCreatorImpl newCreator(KapuaId scopeId, KapuaId userId, String mfaCredentialKey) {
        return new MfaCredentialOptionCreatorImpl(scopeId, userId, mfaCredentialKey);
    }

    @Override
    public MfaCredentialOptionListResult newListResult() {
        return new MfaCredentialOptionListResultImpl();
    }

    @Override
    public MfaCredentialOption newEntity(KapuaId scopeId) {
        return new MfaCredentialOptionImpl(scopeId);
    }

    @Override
    public MfaCredentialOption newMfaCredentialOption(KapuaId scopeId, KapuaId userId, String mfaCredentialKey) {
        return new MfaCredentialOptionImpl(scopeId, userId, mfaCredentialKey);
    }

    @Override
    public MfaCredentialOptionQuery newQuery(KapuaId scopeId) {
        return new MfaCredentialOptionQueryImpl(scopeId);
    }

    @Override
    public MfaCredentialOptionCreator newCreator(KapuaId scopeId) {
        return new MfaCredentialOptionCreatorImpl(scopeId);
    }

    @Override
    public MfaCredentialOption clone(MfaCredentialOption mfaCredentialOption) {
        try {
            return new MfaCredentialOptionImpl(mfaCredentialOption);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, MfaCredentialOption.TYPE, mfaCredentialOption);
        }
    }
}
