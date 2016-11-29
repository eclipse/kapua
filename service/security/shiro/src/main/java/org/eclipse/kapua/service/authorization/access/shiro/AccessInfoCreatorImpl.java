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
package org.eclipse.kapua.service.authorization.access.shiro;

import java.util.Set;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * Access info creator service implementation.
 * 
 * @since 1.0
 * 
 */
public class AccessInfoCreatorImpl extends AbstractKapuaEntityCreator<AccessInfo> implements AccessInfoCreator {

    private static final long serialVersionUID = 972154225756734130L;

    private KapuaId userId;
    private Set<KapuaId> roleIds;
    private Set<Permission> permissions;

    /**
     * Constructor
     * 
     * @param accessInfo
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public AccessInfoCreatorImpl(AccessInfoCreator accessInfo) {
        super((AbstractKapuaEntityCreator) accessInfo);

        setUserId(accessInfo.getUserId());
        setRoleIds(accessInfo.getRoleIds());
        setPermissions(accessInfo.getPermissions());
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public AccessInfoCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public void setUserId(KapuaId userId) {
        this.userId = userId;
    }

    @Override
    public KapuaId getUserId() {
        return userId;
    }

    @Override
    public void setRoleIds(Set<KapuaId> roleIds) {
        this.roleIds = roleIds;
    }

    @Override
    public Set<KapuaId> getRoleIds() {
        return roleIds;
    }

    @Override
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;

    }

    @Override
    public Set<Permission> getPermissions() {
        return permissions;
    }

}
