/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.tag;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaEntityCreator;

/**
 * {@link Tag} creator definition.<br>
 * It is used to create a new {@link Tag}.
 * 
 * @since 1.0.0
 */
@XmlRootElement(name = "tagCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "name" }, //
        factoryClass = TagXmlRegistry.class, //
        factoryMethod = "newTagCreator")
public interface TagCreator extends KapuaEntityCreator<Tag> {

    /**
     * Sets the {@link Tag} name.
     * 
     * @param name
     *            The {@link Tag} name.
     * 
     * @since 1.0.0
     */
    public void setName(String name);

    /**
     * Gets the {@link Tag} name.
     * 
     * @return The {@link Tag} name.
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    public String getName();
}
