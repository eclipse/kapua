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
package org.eclipse.kapua.service.authorization.access;

import java.util.Set;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Access info entity definition.
 * 
 * @since 1.0
 *
 */
public interface AccessInfo extends KapuaUpdatableEntity {

    public static final String TYPE = "accessInfo";

    public default String getType() {
        return TYPE;
    }

    /**
     * Set the user id
     * 
     * @param userId
     */
    public void setUserId(KapuaId userId);

    /**
     * Get the user id
     * 
     * @return
     */
    public KapuaId getUserId();

    /**
     * Set the role set
     * 
     * @param roles
     * @throws KapuaException
     */
    public void setRoles(Set<AccessRole> roles) throws KapuaException;

    /**
     * Get the role set
     * 
     * @return
     */
    public <R extends AccessRole> Set<R> getRoles();

    /**
     * Set the permission set
     * 
     * @param permissions
     */
    public void setPermissions(Set<AccessPermission> permissions);

    /**
     * Get the permission set
     * 
     * @return
     */
    public <P extends AccessPermission> Set<P> getPermissions();
}
