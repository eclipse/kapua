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
package org.eclipse.kapua.service.authorization.user.permission;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * User permission entity.<br>
 * Describes a permission associates to the user.
 * 
 * @since 1.0
 *
 */
public interface UserPermission extends KapuaEntity
{
    public static final String TYPE = "userPermission";

    default public String getType()
    {
        return TYPE;
    }

    /**
     * Set the user identifier
     * 
     * @param userId
     */
    public void setUserId(KapuaId userId);

    /**
     * Get the user identifier
     * 
     * @return
     */
    public KapuaId getUserId();

    /**
     * Set the permission
     * 
     * @param permission
     */
    public void setPermission(Permission permission);

    /**
     * Get the permission
     * 
     * @return
     */
    public Permission getPermission();

}
