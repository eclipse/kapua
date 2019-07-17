/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.token;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.util.Set;

import org.eclipse.kapua.KapuaSerializable;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.role.RolePermission;

@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = AccessTokenXmlRegistry.class, factoryMethod = "newAccessToken")
public interface LoginInfo extends KapuaSerializable {

    @XmlElement(name = "accessToken")
    AccessToken getAccessToken();

    void setAccessToken(AccessToken accessToken);

    @XmlElement(name = "rolePermission")
    Set<RolePermission> getRolePermission();

    void setRolePermission(Set<RolePermission> rolePermissions);

    @XmlElement(name = "accessPermission")
    Set<AccessPermission> getAccessPermission();

    void setAccessPermission(Set<AccessPermission> accessPermissions);
}
