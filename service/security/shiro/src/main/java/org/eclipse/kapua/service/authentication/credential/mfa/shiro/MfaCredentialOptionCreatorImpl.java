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

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionCreator;

/**
 * MfaCredentialOption creator implementation.
 */
public class MfaCredentialOptionCreatorImpl extends AbstractKapuaEntityCreator<MfaCredentialOption> implements MfaCredentialOptionCreator {

    private static final long serialVersionUID = -4619585500941519330L;

    private KapuaId userId;
    private String mfaCredentialKey;

    /**
     * Constructor
     *
     * @param scopeId          scope identifier
     * @param userId           user identifier
     * @param mfaCredentialKey the secret key
     */
    public MfaCredentialOptionCreatorImpl(KapuaId scopeId, KapuaId userId, String mfaCredentialKey) {
        super(scopeId);
        this.userId = userId;
        this.mfaCredentialKey = mfaCredentialKey;
    }

    public MfaCredentialOptionCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getUserId() {
        return userId;
    }

    @Override
    public void setUserId(KapuaId userId) {
        this.userId = userId;
    }

    @Override
    public String getMfaCredentialKey() {
        return mfaCredentialKey;
    }

    @Override
    public void setMfaCredentialKey(String mfaCredentialKey) {
        this.mfaCredentialKey = mfaCredentialKey;
    }
}
