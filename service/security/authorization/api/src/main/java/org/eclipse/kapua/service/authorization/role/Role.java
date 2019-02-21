/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.role;

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.service.authorization.access.AccessInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link Role} {@link org.eclipse.kapua.model.KapuaEntity} definition.
 * <p>
 * {@link Role} is a collection of {@link RolePermission}s that can be assigned to one or more {@link org.eclipse.kapua.service.user.User}s (using {@link AccessInfo}).
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "role")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = RoleXmlRegistry.class, factoryMethod = "newRole")
public interface Role extends KapuaNamedEntity {

    String TYPE = "role";

    @Override
    default String getType() {
        return TYPE;
    }
}
