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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RoleQuery;

/**
 * Role factory service implementation.
 * 
 * @since 1.0
 * 
 */
public class RoleFactoryImpl implements RoleFactory {

    @Override
    public Role newRole(KapuaId scopeId) {
        return new RoleImpl(scopeId);
    }

    @Override
    public RoleCreator newCreator(KapuaId scopeId) {
        return new RoleCreatorImpl(scopeId);
    }

    @Override
    public RoleQuery newQuery(KapuaId scopeId) {
        return new RoleQueryImpl(scopeId);
    }

}
