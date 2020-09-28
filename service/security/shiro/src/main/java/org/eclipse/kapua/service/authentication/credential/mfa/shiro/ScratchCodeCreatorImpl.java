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
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCode;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeCreator;

/**
 * ScratchCode creator implementation.
 */
public class ScratchCodeCreatorImpl extends AbstractKapuaEntityCreator<ScratchCode> implements ScratchCodeCreator {

    private static final long serialVersionUID = 3925204275937566004L;

    private String code;
    private KapuaId mfaCredentialOptionId;

    /**
     * Constructor
     *
     * @param scopeId                scope identifier
     * @param mfaCredentialOptionId credential identifier
     * @param code
     */
    public ScratchCodeCreatorImpl(KapuaId scopeId, KapuaId mfaCredentialOptionId, String code) {
        super(scopeId);
        this.mfaCredentialOptionId = mfaCredentialOptionId;
        this.code = code;
    }

    public ScratchCodeCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getMfaCredentialOptionId() {
        return this.mfaCredentialOptionId;
    }

    @Override
    public void setMfaCredentialOptionId(KapuaId mfaCredentialOptionId) {
        this.mfaCredentialOptionId = mfaCredentialOptionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
