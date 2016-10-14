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
package org.eclipse.kapua.service.authorization.user.role;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * User roles permission entity.<br>
 * Describes the permission associated to a role.
 * 
 * @since 1.0
 * 
 */
public interface UserRolesPermission extends KapuaEntity
{
    public static final String TYPE = "rolePermission";

    default public String getType()
    {
        return TYPE;
    }

    /**
     * Set role identifier
     * 
     * @param roleId
     */
    public void setRoleId(KapuaId roleId);

    /**
     * Get role identifier
     * 
     * @return
     */
    public KapuaId getRoleId();

    /**
     * Set permission
     * 
     * @param permission
     */
    public void setPermission(Permission permission);

    /**
     * Get permission
     * 
     * @return
     */
    public Permission getPermission();
}
