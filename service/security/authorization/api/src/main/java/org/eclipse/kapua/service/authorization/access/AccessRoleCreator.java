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
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.access;

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.authorization.role.Role;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * {@link AccessRole} creator definition.<br>
 * It is used to create a new {@link AccessRole}.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "accessRoleCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "accessInfoId", "roleId" },//
        factoryClass = AccessRoleXmlRegistry.class, factoryMethod = "newCreator")
public interface AccessRoleCreator extends KapuaEntityCreator<AccessRole> {

    /**
     * Sets the {@link AccessInfo} id for this {@link AccessRole}.
     *
     * @param accessInfoId The {@link AccessInfo} id for this {@link AccessRole}.
     * @since 1.0.0
     */
    void setAccessInfoId(KapuaId accessInfoId);

    /**
     * Gets the {@link AccessInfo} id of this {@link AccessRole}.
     *
     * @return The {@link AccessInfo} id of this {@link AccessRole}.
     * @since 1.0.0
     */
    @XmlElement(name = "accessInfoId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getAccessInfoId();

    /**
     * Sets the {@link Role} id to assign to the {@link AccessRole} created entity.
     * It up to the implementation class to make a clone of the object or use the given object.
     *
     * @param roleId The {@link Role} id
     * @since 1.0.0
     */
    void setRoleId(KapuaId roleId);

    /**
     * Gets the {@link Role} id added to this {@link AccessRole}.
     *
     * @return The {@link Role} id added to this {@link AccessRole}.
     * @since 1.0.0
     */
    @XmlElement(name = "roleId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getRoleId();
}
