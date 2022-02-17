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

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionCreator;

/**
 * {@link MfaOption} creator implementation.
 */
public class MfaOptionCreatorImpl extends AbstractKapuaEntityCreator<MfaOption> implements MfaOptionCreator {

    private static final long serialVersionUID = -4619585500941519330L;

    private KapuaId userId;
    private String mfaSecretKey;

    /**
     * Constructor
     *
     * @param scopeId          scope identifier
     * @param userId           user identifier
     * @param mfaSecretKey the secret key
     */
    public MfaOptionCreatorImpl(KapuaId scopeId, KapuaId userId, String mfaSecretKey) {
        super(scopeId);
        this.userId = userId;
        this.mfaSecretKey = mfaSecretKey;
    }

    public MfaOptionCreatorImpl(KapuaId scopeId) {
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
    public String getMfaSecretKey() {
        return mfaSecretKey;
    }

    @Override
    public void setMfaSecretKey(String mfaSecretKey) {
        this.mfaSecretKey = mfaSecretKey;
    }
}
