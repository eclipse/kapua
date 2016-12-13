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

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.permission.Permission;

/**
 * {@link AccessPermission} creator implementation.
 * 
 * @since 1.0.0
 * 
 */
public class AccessPermissionCreatorImpl extends AbstractKapuaEntityCreator<AccessPermission> implements AccessPermissionCreator {

    private static final long serialVersionUID = 972154225756734130L;

    private KapuaId accessInfo;
    private Permission permission;

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public AccessPermissionCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    public KapuaId getAccessInfoId() {
        return accessInfo;
    }

    public void setAccessInfoId(KapuaId accessInfo) {
        this.accessInfo = accessInfo;
    }

    @SuppressWarnings("unchecked")
    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
