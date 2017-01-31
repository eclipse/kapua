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
 * {@link KapuaNamedEntityCreator} definition.<br>
 * All the {@link KapuaNamedEntityCreator}s will be an extension of this entity.
 * 
 * @param <E>
 *            The {@link KapuaNamedEntity} of which this {@link KapuaNamedEntityCreator} is the creator model.
 * 
 * @since 1.0.0
 * 
 */
@XmlType(propOrder = { "name" })
public interface KapuaNamedEntityCreator<E extends KapuaEntity> extends KapuaUpdatableEntityCreator<E> {

    /**
     * Gets the {@link KapuaNamedEntityCreator} name.
     * 
     * @return The name assigned to this {@link KapuaNamedEntityCreator}.
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    public String getName();

    /**
     * Sets the name that will be assigned to the created {@link KapuaNamedEntity}.
     * 
     * @param name
     *            The name that will be assigned to the created {@link KapuaNamedEntity}.
     * @since 1.0.0
     */
    public void setName(String name);
}
