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

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaCredentialOptionQuery;

/**
 * MfaCredentialOption query implementation.
 */
public class MfaCredentialOptionQueryImpl extends AbstractKapuaQuery implements MfaCredentialOptionQuery {

    /**
     * Constructor
     */
    public MfaCredentialOptionQueryImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public MfaCredentialOptionQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }
}
