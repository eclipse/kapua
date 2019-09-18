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
import org.eclipse.kapua.service.authorization.permission.Permission;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * {@link AccessPermission} creator definition.<br>
 * It is used to create a new {@link AccessPermission}.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "accessPermissionCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "accessInfoId", "permission" },//
        factoryClass = AccessPermissionXmlRegistry.class, factoryMethod = "newCreator")
public interface AccessPermissionCreator extends KapuaEntityCreator<AccessPermission> {

    /**
     * Sets the {@link AccessInfo} id for this {@link AccessPermission}.
     *
     * @param accessInfoId The {@link AccessInfo} id for this {@link AccessPermission}.
     * @since 1.0.0
     */
    void setAccessInfoId(KapuaId accessInfoId);

    /**
     * Gets the {@link AccessInfo} id of this {@link AccessPermission}.
     *
     * @return The {@link AccessInfo} id of this {@link AccessPermission}.
     * @since 1.0.0
     */
    @XmlElement(name = "accessInfoId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getAccessInfoId();

    /**
     * Sets the {@link Permission} to assign to the {@link AccessPermission} created entity.
     * It up to the implementation class to make a clone of the object or use the given object.
     *
     * @param permission The {@link Permission}.
     * @since 1.0.0
     */
    void setPermission(Permission permission);

    /**
     * Gets the set of {@link Permission} added to this {@link AccessPermission}.
     *
     * @return The set of {@link Permission}.
     * @since 1.0.0
     */
    @XmlElement(name = "permission")
    <P extends Permission> P getPermission();
}
