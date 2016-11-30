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

import java.security.Permissions;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * {@link Role} creator definition.<br>
 * It is used to create a new {@link Role} with {@link Permission}s associated
 * 
 * @since 1.0.0
 */
public interface RoleCreator extends KapuaEntityCreator<Role> {

    /**
     * Sets the {@link Role} name.
     * 
     * @param name
     *            The {@link Role} name.
     * 
     * @since 1.0.0
     */
    public void setName(String name);

    /**
     * Gets the {@link Role} name.
     * 
     * @return The {@link Role} name.
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    public String getName();

    /**
     * Sets the set of {@link Permissions} to assign to the {@link Role} created entity.
     * It up to the implementation class to make a clone of the set or use the given set.
     * 
     * @param permissions
     *            The set of {@link Permissions}.
     * @since 1.0.0
     */
    public void setPermissions(Set<Permission> permissions);

    /**
     * Gets the set of {@link Permission} added to this {@link Role}.
     * The implementation must return the reference of the set and not make a clone.
     * 
     * @return The set of {@link Permission}.
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "permissions")
    @XmlElement(name = "permission")
    public <P extends Permission> Set<P> getPermissions();
}
