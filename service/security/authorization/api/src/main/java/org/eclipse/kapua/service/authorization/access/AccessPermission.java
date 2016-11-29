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

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * Access permission entity.<br>
 * Describes a permission associated to the access info.
 * 
 * @since 1.0
 *
 */
public interface AccessPermission extends KapuaEntity {

    public static final String TYPE = "accessPermission";

    public default String getType() {
        return TYPE;
    }

    /**
     * Set the access identifier
     * 
     * @param accessId
     */
    public void setAccessId(KapuaId accessId);

    /**
     * Get the access identifier
     * 
     * @return
     */
    public KapuaId getAccessId();

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
    public <P extends Permission> P getPermission();

}
