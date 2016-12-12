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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.service.authorization.access.AccessInfo;

/**
 * Role entity definition.<br>
 * Role is a collection of {@link RolePermission}s that can be assigned to one or more {@link User}s (using {@link AccessInfo}).<br>
 * {@link Role#getName()} must be unique within the scope.
 * 
 * @since 1.0.0
 */
@XmlRootElement(name = "role")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "name" }, //
        factoryClass = RoleXmlRegistry.class, factoryMethod = "newRole")
public interface Role extends KapuaUpdatableEntity {

    public static final String TYPE = "role";

    default public String getType() {
        return TYPE;
    }

    /**
     * Sets the {@link Role} name.<br>
     * It must be unique within the scope.
     * 
     * @param name
     *            The name of the {@link Role}
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
}
