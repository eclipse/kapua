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
package org.eclipse.kapua.service.authorization.user.permission.shiro;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.user.UserPermission;
import org.eclipse.kapua.service.authorization.user.UserPermissionCreator;

/**
 * User permission creator service implementation.
 * 
 * @since 1.0
 * 
 */
public class UserPermissionCreatorImpl extends AbstractKapuaEntityCreator<UserPermission> implements UserPermissionCreator
{
    private static final long serialVersionUID = 972154225756734130L;

    @XmlElement(name = "userId")
    private KapuaId           userId;

    private Permission        permission;

    /**
     * Contructor
     * 
     * @param scopeId
     */
    public UserPermissionCreatorImpl(KapuaId scopeId)
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
    public void setPermission(Permission permission)
    {
        this.permission = permission;
    }

    @Override
    public Permission getPermission()
    {
        return permission;
    }

}
