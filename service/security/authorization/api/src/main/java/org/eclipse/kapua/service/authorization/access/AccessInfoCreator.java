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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * Access info creator service definition
 *
 * @since 1.0
 * 
 */
public interface AccessInfoCreator extends KapuaEntityCreator<AccessInfo> {

    /**
     * Set the user identifier
     * 
     * @param userId
     */
    public void setUserId(KapuaId userId);

    /**
     * get the user identifier
     * 
     * @return
     */
    @XmlElement(name = "userId")
    public KapuaId getUserId();

    /**
     * Set user role ids
     * 
     * @param role
     */
    public void setRoleIds(Set<KapuaId> roleIds);

    /**
     * Get user role ids
     * 
     * @return
     */
    @XmlElementWrapper(name = "roleIds")
    @XmlElement(name = "roleId")
    public Set<KapuaId> getRoleIds();

    /**
     * Set user permissions
     * 
     * @param permission
     */
    public void setPermissions(Set<Permission> permissions);

    /**
     * Get user permissions
     * 
     * @return
     */
    @XmlElementWrapper(name = "permissions")
    @XmlElement(name = "permission")
    public <P extends Permission> Set<P> getPermissions();

}
