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
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCode;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeFactory;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeListResult;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeQuery;

/**
 * {@link ScratchCodeFactory} implementation.
 */
@KapuaProvider
public class ScratchCodeFactoryImpl implements ScratchCodeFactory {

    @Override
    public ScratchCodeCreatorImpl newCreator(KapuaId scopeId, KapuaId credentialId, String mfaCredentialKey) {
        return new ScratchCodeCreatorImpl(scopeId, credentialId, mfaCredentialKey);
    }

    @Override
    public ScratchCodeListResult newListResult() {
        return new ScratchCodeListResultImpl();
    }

    @Override
    public ScratchCode newEntity(KapuaId scopeId) {
        return new ScratchCodeImpl(scopeId);
    }

    @Override
    public ScratchCode newScratchCode(KapuaId scopeId, KapuaId mfaCredentialOptionId, String code) {
        return new ScratchCodeImpl(scopeId, mfaCredentialOptionId, code);
    }

    @Override
    public ScratchCodeQuery newQuery(KapuaId scopeId) {
        return new ScratchCodeQueryImpl(scopeId);
    }

    @Override
    public ScratchCodeCreator newCreator(KapuaId scopeId) {
        return new ScratchCodeCreatorImpl(scopeId);
    }

    @Override
    public ScratchCode clone(ScratchCode scratchCode) {
        try {
            return new ScratchCodeImpl(scratchCode);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, ScratchCode.TYPE, scratchCode);
        }
    }
}
