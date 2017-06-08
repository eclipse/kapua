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

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;

/**
 * Tag entity definition.<br>
 * Tags serve as tags for entities marked as {@link Taggable}.
 * It is possible to assign a tag to an entity.
 * {@link Tag#getName()} must be unique within the scope.
 * 
 * @since 1.0.0
 */
@XmlRootElement(name = "tag")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "name" }, //
        factoryClass = TagXmlRegistry.class, factoryMethod = "newTag")
public interface Tag extends KapuaUpdatableEntity {

    @XmlTransient
    public static final KapuaId ANY = KapuaLocator.getInstance().getFactory(KapuaIdFactory.class).newKapuaId(BigInteger.ONE.negate());

    public static final String TYPE = "tag";

    public default String getType() {
        return TYPE;
    }

    /**
     * Sets the {@link Tag} name.<br>
     * It must be unique within the scope.
     * 
     * @param name
     *            The name of the {@link Tag}
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
