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
package org.eclipse.kapua.service.authentication.credential.mfa.shiro;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionFactory;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionQuery;

/**
 * {@link MfaOptionFactory} implementation.
 */
@KapuaProvider
public class MfaOptionFactoryImpl implements MfaOptionFactory {

    @Override
    public MfaOptionCreatorImpl newCreator(KapuaId scopeId, KapuaId userId, String mfaSecretKey) {
        return new MfaOptionCreatorImpl(scopeId, userId, mfaSecretKey);
    }

    @Override
    public MfaOptionListResult newListResult() {
        return new MfaOptionListResultImpl();
    }

    @Override
    public MfaOption newEntity(KapuaId scopeId) {
        return new MfaOptionImpl(scopeId);
    }

    @Override
    public MfaOption newMfaOption(KapuaId scopeId, KapuaId userId, String mfaSecretKey) {
        return new MfaOptionImpl(scopeId, userId, mfaSecretKey);
    }

    @Override
    public MfaOptionQuery newQuery(KapuaId scopeId) {
        return new MfaOptionQueryImpl(scopeId);
    }

    @Override
    public MfaOptionCreator newCreator(KapuaId scopeId) {
        return new MfaOptionCreatorImpl(scopeId);
    }

    @Override
    public MfaOption clone(MfaOption mfaOption) {
        try {
            return new MfaOptionImpl(mfaOption);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, MfaOption.TYPE, mfaOption);
        }
    }
}
