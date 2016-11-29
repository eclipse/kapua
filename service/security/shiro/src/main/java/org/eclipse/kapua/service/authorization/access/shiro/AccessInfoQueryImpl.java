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
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;

/**
 * Access info query implementation.
 * 
 * @since 1.0
 * 
 */
public class AccessInfoQueryImpl extends AbstractKapuaQuery<AccessInfo> implements AccessInfoQuery {

    /**
     * Constructor
     */
    public AccessInfoQueryImpl() {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public AccessInfoQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }
}
