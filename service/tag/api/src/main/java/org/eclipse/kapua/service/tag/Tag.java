/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link Tag} {@link org.eclipse.kapua.model.KapuaEntity} definition
 * <p>
 * {@link Tag}s serve as tag for entities marked as {@link Taggable}.
 * It is possible to assign a {@link Tag} to a {@link org.eclipse.kapua.model.KapuaEntity}.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "tag")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = TagXmlRegistry.class, factoryMethod = "newTag")
public interface Tag extends KapuaNamedEntity {

    @XmlTransient
    KapuaId ANY = KapuaId.ANY;

    String TYPE = "tag";

    @Override
    default String getType() {
        return TYPE;
    }
}
