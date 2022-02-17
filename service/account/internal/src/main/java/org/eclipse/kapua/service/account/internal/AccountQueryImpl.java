/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.commons.model.query.AbstractKapuaNamedQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.AccountQuery;

/**
 * {@link AccountQuery} implementation.
 *
 * @since 1.0.0
 */
public class AccountQueryImpl extends AbstractKapuaNamedQuery implements AccountQuery {

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    private AccountQueryImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The {@link AccountQuery#getScopeId()}.
     * @since 1.0.0
     */
    public AccountQueryImpl(KapuaId scopeId) {
        this();

        setScopeId(scopeId);
    }
}
