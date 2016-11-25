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
package org.eclipse.kapua.service.authorization.role.shiro;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RolePermission;

/**
 * Role creator service implementation.
 * 
 * @since 1.0
 * 
 */
public class RoleCreatorImpl extends AbstractKapuaEntityCreator<Role> implements RoleCreator
{
    private static final long   serialVersionUID = 972154225756734130L;

    @XmlElement(name = "name")
    private String              name;

    private Set<RolePermission> permissions;

    public RoleCreatorImpl(KapuaId scopeId)
    {
        super(scopeId);
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setRolePermissions(Set<RolePermission> permissions)
    {
        this.permissions = permissions;
    }

    @Override
    public Set<RolePermission> getRolePermissions()
    {
        return permissions;
    }

}
