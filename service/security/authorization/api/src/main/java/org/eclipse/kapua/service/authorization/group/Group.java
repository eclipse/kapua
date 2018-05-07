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
package org.eclipse.kapua.service.authorization.group;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.role.RolePermission;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.math.BigInteger;

/**
 * Group entity definition.<br>
 * Groups serve as tag for entities marked as {@link Groupable}.
 * It is possible to assign a group to an entity.
 * It is possible to assign {@link AccessPermission} and {@link RolePermission} based on the {@link Group#getId()}.
 * {@link Group#getName()} must be unique within the scope.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "group")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = GroupXmlRegistry.class, factoryMethod = "newGroup")
public interface Group extends KapuaUpdatableEntity {

    @XmlTransient
    KapuaId ANY = KapuaLocator.getInstance().getFactory(KapuaIdFactory.class).newKapuaId(BigInteger.ONE.negate());

    String TYPE = "group";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Sets the {@link Group} name.<br>
     * It must be unique within the scope.
     *
     * @param name The name of the {@link Group}
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Gets the {@link Group} name.
     *
     * @return The {@link Group} name.
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    String getName();

}
