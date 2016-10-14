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
package org.eclipse.kapua.service.authorization.user.permission.shiro;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
import org.eclipse.kapua.service.authorization.user.permission.UserPermission;

@Entity(name = "UserPermission")
@Table(name = "athz_user_permission")
/**
 * User permission implementation.
 * 
 * @since 1.0
 *
 */
public class UserPermissionImpl extends AbstractKapuaEntity implements UserPermission
{
    private static final long serialVersionUID = -3760818776351242930L;

    @XmlElement(name = "userId")
    @Embedded
    @AttributeOverrides({
                          @AttributeOverride(name = "eid", column = @Column(name = "user_id", nullable = false, updatable = false))
    })
    private KapuaEid          userId;

    @Embedded
    private PermissionImpl    permission;

    /**
     * Constructor
     */
    public UserPermissionImpl()
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public UserPermissionImpl(KapuaId scopeId)
    {
        super(scopeId);
    }

    @Override
    public void setUserId(KapuaId userId)
    {
        this.userId = (KapuaEid) userId;
    }

    @Override
    public KapuaId getUserId()
    {
        return userId;
    }

    @Override
    public void setPermission(Permission permission)
    {
        this.permission = (PermissionImpl) permission;
    }

    @Override
    public Permission getPermission()
    {
        return permission;
    }
}
