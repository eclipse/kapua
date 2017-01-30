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
package org.eclipse.kapua.service.authorization.group;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.role.RolePermission;

/**
 * Group entity definition.<br>
 * Groups serve as tags for entities marked as {@link Groupable}.
 * It is possible to assign a group to an entity.
 * It is possible to assign {@link AccessPermission} and {@link RolePermission} based on the {@link Group#getId()}.
 * {@link Group#getName()} must be unique within the scope.
 * 
 * @since 1.0.0
 */
public interface Group extends KapuaUpdatableEntity {

    @XmlTransient
    public static final KapuaId ANY = KapuaLocator.getInstance().getFactory(KapuaIdFactory.class).newKapuaId(BigInteger.ONE.negate());

    public static final String TYPE = "group";

    public default String getType() {
        return TYPE;
    }

    /**
     * Sets the {@link Group} name.<br>
     * It must be unique within the scope.
     * 
     * @param name
     *            The name of the {@link Group}
     * @since 1.0.0
     */
    public void setName(String name);

    /**
     * Gets the {@link Group} name.
     * 
     * @return The {@link Group} name.
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    public String getName();

}
