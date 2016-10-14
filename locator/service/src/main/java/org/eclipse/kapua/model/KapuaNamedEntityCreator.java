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
 * Kapua named entity creator service definition.
 *
 * @param <E> entity type
 * 
 * @since 1.0
 * 
 */
@XmlType(propOrder = {"name" })
public interface KapuaNamedEntityCreator<E extends KapuaEntity> extends KapuaUpdatableEntityCreator<E>
{
    /**
     * Get the entity name
     * 
     * @return
     */
	@XmlElement(name="name")
    public String getName();

    /**
     * Set the entity name
     * 
     * @param name
     */
    public void setName(String name);
}
