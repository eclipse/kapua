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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Access info entity definition.<br>
 * It contains all authorization accesses for a {@link User}.<br>
 * It refers to the {@link User} entity by the {@link AccessInfo#getUserId()} property.<br>
 * It contains the set of {@link AccessPermission} assigned to the {@link User} entity.<br>
 * It contains the set of {@link AccessRole} assigned to the {@link User} entity.<br>
 * <br>
 * {@link AccessInfo} is unique by the {@link AccessInfo#getUserId()} property.
 * 
 * @since 1.0.0
 */
public interface AccessInfo extends KapuaUpdatableEntity {

    public static final String TYPE = "accessInfo";

    public default String getType() {
        return TYPE;
    }

    /**
     * Sets the user id.
     * 
     * @param userId
     *            The user id.
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
     * Sets the set of {@link AccessRole} of this {@link AccessInfo}.<br>
     * It up to the implementation class to make a clone of the set or use the given set.
     * 
     * @param roles
     *            The set of {@link AccessRoles}.
     * @throws KapuaException
     *             If the given set is incompatible with the implementation-specific field.
     * @since 1.0.0
     */
    public void setAccessRoles(Set<AccessRole> roles) throws KapuaException;

    /**
     * Gets the set of {@link AccessRole} of this {@link AccessInfo}.<br>
     * The implementation must return the reference of the set and not make a clone.
     * 
     * @return The set of {@link AccessRole}.
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "accessRoles")
    @XmlElement(name = "accessRole")
    public <R extends AccessRole> Set<R> getAccessRoles();

    /**
     * Sets the set of {@link AccessPermission} of this {@link AccessInfo}.<br>
     * It up to the implementation class to make a clone of the set or use the given set.
     * 
     * @param permissions
     *            The set of {@link AccessPermission}.
     * @throws KapuaException
     *             If the given set is incompatible with the implementation-specific field.
     * @since 1.0.0
     */
    public void setAccessPermissions(Set<AccessPermission> permissions);

    /**
     * Gets the set of {@link AccessPermission} of this {@link AccessInfo}.<br>
     * The implementation must return the reference of the set and not make a clone.
     * 
     * @return The set of {@link AccessPermission}.
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "accessPermissions")
    @XmlElement(name = "accessPermission")
    public <P extends AccessPermission> Set<P> getAccessPermissions();
}
