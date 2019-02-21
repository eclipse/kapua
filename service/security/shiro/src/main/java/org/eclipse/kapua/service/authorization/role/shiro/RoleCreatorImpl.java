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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link RoleCreator} implementation.
 *
 * @since 1.0.0
 */
public class RoleCreatorImpl extends AbstractKapuaNamedEntityCreator<Role> implements RoleCreator {

    private static final long serialVersionUID = 972154225756734130L;

    private Set<Permission> permissions;

    /**
     * Constructor
     *
     * @param scopeId The scope {@link KapuaId}
     * @since 1.0.0
     */
    public RoleCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public Set<Permission> getPermissions() {
        if (permissions == null) {
            permissions = new HashSet<>();
        }
        return permissions;
    }

}
