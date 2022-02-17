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

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionQuery;

/**
 * {@link MfaOption} query implementation.
 *
 * @since 1.3.0
 */
public class MfaOptionQueryImpl extends AbstractKapuaQuery implements MfaOptionQuery {

    /**
     * Constructor.
     *
     * @since 1.3.0
     */
    public MfaOptionQueryImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The {@link #getScopeId()}.
     * @since 1.3.0
     */
    public MfaOptionQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }
}
