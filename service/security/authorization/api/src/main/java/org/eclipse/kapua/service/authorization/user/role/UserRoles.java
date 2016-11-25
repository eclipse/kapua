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

import java.util.Set;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;

/**
 * User role entity.<br>
 * Describes a role associates to the user.
 * 
 * @since 1.0
 * 
 */
public interface UserRoles extends KapuaEntity {

    public static final String TYPE = "userRole";

    default public String getType() {
        return TYPE;
    }

    /**
     * Set the user identifiers
     * 
     * @param userId
     */
    public void setUserId(KapuaId userId);

    /**
     * Get the user identifiers
     * 
     * @return
     */
    public KapuaId getUserId();

    /**
     * Set the roles list
     * 
     * @param roles
     * @throws KapuaException
     */
    public void setRoles(Set<Role> roles) throws KapuaException;

    /**
     * Get the roles list
     * 
     * @return
     */
    public Set<Role> getRoles();
}
