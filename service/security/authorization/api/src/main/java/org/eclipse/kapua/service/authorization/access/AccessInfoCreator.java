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

import java.security.Permissions;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * {@link AccessInfo} creator definition.<br>
 * It is used to assign a set of {@link Role}s and {@link Permission}s to the referenced {@link User}.<br>
 * 
 * @since 1.0.0
 */
public interface AccessInfoCreator extends KapuaEntityCreator<AccessInfo> {

    /**
     * Sets the user identifier.
     * 
     * @param userId
     *            The user id to set.
     * @since 1.0.0
     */
    public void setUserId(KapuaId userId);

    /**
     * Gets the user id.
     * 
     * @return The user id.
     * @since 1.0.0
     */
    @XmlElement(name = "userId")
    public KapuaId getUserId();

    /**
     * Sets the set of {@link Role} ids to assign to the {@link AccessInfo} created entity.
     * It up to the implementation class to make a clone of the set or use the given set.
     * 
     * @param roleIds
     *            The set of {@link Role} ids.
     * @since 1.0.0
     */
    public void setRoleIds(Set<KapuaId> roleIds);

    /**
     * Gets the set of {@link Role} ids added to this {@link AccessInfoCreator}.
     * The implementation must return the reference of the set and not make a clone.
     * 
     * @return The set of {@link Role} ids.
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "roleIds")
    @XmlElement(name = "roleId")
    public Set<KapuaId> getRoleIds();

    /**
     * Sets the set of {@link Permissions} to assign to the {@link AccessInfo} created entity.
     * It up to the implementation class to make a clone of the set or use the given set.
     * 
     * @param permissions
     *            The set of {@link Permissions}.
     * @since 1.0.0
     */
    public void setPermissions(Set<Permission> permissions);

    /**
     * Gets the set of {@link Permission} added to this {@link AccessInfoCreator}.
     * The implementation must return the reference of the set and not make a clone.
     * 
     * @return The set of {@link Permission}.
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "permissions")
    @XmlElement(name = "permission")
    public <P extends Permission> Set<P> getPermissions();

}
