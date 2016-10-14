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

import java.util.Set;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.user.role.UserRoles;
import org.eclipse.kapua.service.authorization.user.role.UserRolesCreator;

/**
 * User roles creator service implementation.
 * 
 * @since 1.0
 * 
 */
public class UserRolesCreatorImpl extends AbstractKapuaEntityCreator<UserRoles> implements UserRolesCreator
{
    private static final long serialVersionUID = 972154225756734130L;

    private KapuaId           userId;
    private Set<Role>         roles;

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public UserRolesCreatorImpl(KapuaId scopeId)
    {
        super(scopeId);
    }

    @Override
    public void setUserId(KapuaId userId)
    {
        this.userId = userId;
    }

    @Override
    public KapuaId getUserId()
    {
        return userId;
    }

    @Override
    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    @Override
    public Set<Role> getRoles()
    {
        return roles;
    }

}
