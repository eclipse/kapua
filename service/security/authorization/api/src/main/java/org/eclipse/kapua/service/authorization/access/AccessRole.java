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
package org.eclipse.kapua.service.authorization.access;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Access role entity.<br>
 * Describes a {@link Role} associated to the access info.<br>
 * Wrapping of the {@link Role} id into this class is intended to add auditing
 * informations like {@link AccessRole#getCreatedBy()} and{@link AccessRole#getCreatedOn()}.<br>
 * <br>
 * This is a not editable entity so it can be only removed or created and therefore any change to
 * {@link AccessRole#getAccessInfoId()} and {@link AccessRole#getRoleId()} property is forbidden.<br>
 * <br>
 * To edit {@link Role} entity please refer to {@link RoleService#update(Role)}
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "accessRole")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "accessInfoId", "roleId" }, //
        factoryClass = AccessRoleXmlRegistry.class, //
        factoryMethod = "newAccessRole")
public interface AccessRole extends KapuaEntity {

    String TYPE = "accessRole";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Sets the {@link KapuaId} id of which this {@link AccessRole} belongs.
     *
     * @param accessInfoId The {@link KapuaId} id.
     * @since 1.0.0
     */
    void setAccessInfoId(KapuaId accessInfoId);

    /**
     * Gets the {@link AccessInfo} id of which this {@link AccessRole} belongs.
     *
     * @return The {@link AccessInfo} id.
     * @since 1.0.0
     */
    @XmlElement(name = "accessInfoId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getAccessInfoId();

    /**
     * Sets the {@link Role} id that this {@link AccessRole} has.<br>
     * It up to the implementation class to make a clone of the given {@link Role} or use the given {@link Role}.
     *
     * @param roleId The {@link Role} id to set for this {@link AccessRole}.
     * @since 1.0.0
     */
    void setRoleId(KapuaId roleId) throws KapuaException;

    /**
     * Gets the {@link Role} id that this {@link AccessRole} has.
     *
     * @return The {@link Role} id that this {@link AccessRole} has.
     */
    @XmlElement(name = "roleId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getRoleId();
}
