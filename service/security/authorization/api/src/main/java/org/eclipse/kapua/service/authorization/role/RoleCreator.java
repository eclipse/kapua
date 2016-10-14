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
package org.eclipse.kapua.service.authorization.role;

import java.util.Set;

import org.eclipse.kapua.model.KapuaEntityCreator;

/**
 * Role creator service definition.
 * 
 * @since 1.0
 * 
 */
public interface RoleCreator extends KapuaEntityCreator<Role>
{
    /**
     * Set the permission name
     * 
     * @param name
     */
    public void setName(String name);

    /**
     * Return the permission name
     * 
     * @return
     */
    public String getName();

    /**
     * Set the {@link RolePermission} set
     * 
     * @param permissions
     */
    public void setRoles(Set<RolePermission> permissions);

    /**
     * Return the {@link RolePermission} set
     * 
     * @return
     */
    public Set<RolePermission> getRoles();
}
