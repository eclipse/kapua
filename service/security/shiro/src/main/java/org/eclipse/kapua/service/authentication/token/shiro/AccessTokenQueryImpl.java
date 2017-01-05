/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.token.shiro;

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenQuery;

/**
 * Access token query implementation.
 * 
 * @since 1.0
 * 
 */
public class AccessTokenQueryImpl extends AbstractKapuaQuery<AccessToken> implements AccessTokenQuery {

    /**
     * Constructor
     */
    private AccessTokenQueryImpl() {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public AccessTokenQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }
}
