/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
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
