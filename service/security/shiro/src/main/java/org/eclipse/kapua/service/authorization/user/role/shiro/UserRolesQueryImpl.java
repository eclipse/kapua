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
package org.eclipse.kapua.service.authorization.user.role.shiro;

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.user.role.UserRoles;
import org.eclipse.kapua.service.authorization.user.role.UserRolesQuery;

/**
 * User roles query implementation.
 * 
 * @since 1.0
 * 
 */
public class UserRolesQueryImpl extends AbstractKapuaQuery<UserRoles> implements UserRolesQuery
{

    /**
     * Constructor
     */
    public UserRolesQueryImpl()
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public UserRolesQueryImpl(KapuaId scopeId)
    {
        this();
        setScopeId(scopeId);
    }
}
