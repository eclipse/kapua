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
package org.eclipse.kapua.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link KapuaNamedEntity} definition.<br>
 * All the {@link KapuaNamedEntity}es will be an extension of this entity.<br>
 * 
 * A 'named' entity is a entity that has a 'name' field which must be unique within the platform.
 * 
 * @since 1.0.0
 * 
 */
@XmlType(propOrder = { "name" })
public interface KapuaNamedEntity extends KapuaUpdatableEntity {

    /**
     * Gets the {@link KapuaNamedEntity} name.
     * 
     * @return The {@link KapuaNamedEntity} name.
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    public String getName();

    /**
     * Sets the {@link KapuaNamedEntity} name.
     * 
     * @param name
     *            The {@link KapuaNamedEntity} name.
     * @since 1.0.0
     */
    public void setName(String name);
}
