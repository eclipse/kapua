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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;

/**
 * {@link AccessRole} factory implementation.
 * 
 * @since 1.0.0
 * 
 */
public class AccessRoleFactoryImpl implements AccessRoleFactory {

    @Override
    public AccessRole newAccessRole(KapuaId scopeId) {
        return new AccessRoleImpl(scopeId);
    }

    @Override
    public AccessRoleCreator newCreator(KapuaId scopeId) {
        return new AccessRoleCreatorImpl(scopeId);
    }

    @Override
    public AccessRoleQuery newQuery(KapuaId scopeId) {
        return new AccessRoleQueryImpl(scopeId);
    }

    @Override
    public AccessRoleListResult newAccessRoleListResult() {
        return new AccessRoleListResultImpl();
    }
}
