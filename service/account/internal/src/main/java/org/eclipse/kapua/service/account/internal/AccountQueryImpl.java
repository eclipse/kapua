/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountQuery;

/**
 * User roles factory service implementation.
 *
 * @since 1.0
 */
public class AccountQueryImpl extends AbstractKapuaQuery<Account> implements AccountQuery {

    /**
     * Constructor
     */
    private AccountQueryImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public AccountQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }

}
