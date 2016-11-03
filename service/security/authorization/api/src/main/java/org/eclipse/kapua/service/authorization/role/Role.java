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

import org.eclipse.kapua.model.KapuaUpdatableEntity;

/**
 * Role entity definition.
 * 
 * @since 1.0
 *
 */
public interface Role extends KapuaUpdatableEntity {

    public static final String TYPE = "role";

    default public String getType() {
        return TYPE;
    }

    /**
     * Set the role name
     * 
     * @param name
     */
    public void setName(String name);

    /**
     * Get the role name
     * 
     * @return
     */
    public String getName();

    /**
     * Set the permissions set
     * 
     * @param permissions
     */
    public void setPermissions(Set<RolePermission> permissions);

    /**
     * Get the permissions set
     * 
     * @return
     */
    public Set<RolePermission> getPermissions();
}
