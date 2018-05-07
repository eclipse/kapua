/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.math.BigInteger;

/**
 * Tag entity definition.<br>
 * Tags serve as tag for entities marked as {@link Taggable}.
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
    KapuaId ANY = KapuaLocator.getInstance().getFactory(KapuaIdFactory.class).newKapuaId(BigInteger.ONE.negate());

    String TYPE = "tag";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Sets the {@link Tag} name.<br>
     * It must be unique within the scope.
     *
     * @param name The name of the {@link Tag}
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Gets the {@link Tag} name.
     *
     * @return The {@link Tag} name.
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    String getName();

}
