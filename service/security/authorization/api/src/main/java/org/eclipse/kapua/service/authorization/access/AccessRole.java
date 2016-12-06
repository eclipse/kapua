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

import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleService;

/**
 * Access role entity.<br>
 * Describes a {@link Role} associated to the access info.<br>
 * Wrapping of the {@link Role} into this class is intended to add auditing
 * informations like {@link AccessRole#getCreatedBy()} and{@link AccessRole#getCreatedOn()}.<br>
 * <br>
 * This is a not editable entity so it can be only removed or created and therefore any change to
 * {@link AccessRole#getAccessId()} and {@link AccessRole#getRole()} property is forbidden.<br>
 * <br>
 * To edit {@link Role} entity please refer to {@link RoleService#update(Role)}
 * 
 * @since 1.0.0
 */
public interface AccessRole extends KapuaEntity {

    public static final String TYPE = "accessRole";

    public default String getType() {
        return TYPE;
    }

    /**
     * Sets the {@link AccessInfo} id of which this {@link AccessRole} belongs.
     * 
     * @param accessId
     *            The {@link AccessInfo} id.
     * @since 1.0.0
     */
    public void setAccessId(KapuaId accessId);

    /**
     * Gets the {@link AccessInfo} id of which this {@link AccessRole} belongs.
     * 
     * @return The {@link AccessInfo} id.
     * @since 1.0.0
     */
    @XmlElement(name = "accessId")
    public KapuaId getAccessId();

    /**
     * Sets the {@link Role} that this {@link AccessRole} has.<br>
     * It up to the implementation class to make a clone of the given {@link Role} or use the given {@link Role}.
     * 
     * @param role
     *            The {@link Role} to set for this {@link AccessRole}.
     * @since 1.0.0
     */
    public void setRole(Role role) throws KapuaException;

    /**
     * Gets the {@link Role} that this {@link AccessRole} has.
     * 
     * @return The {@link Role} that this {@link AccessRole} has.
     */
    @XmlElement(name = "role")
    public <R extends Role> R getRole();
}
