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

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionQuery;

/**
 * {@link AccessPermission} factory implementation.
 * 
 * @since 1.0.0
 * 
 */
@KapuaProvider
public class AccessPermissionFactoryImpl implements AccessPermissionFactory {

    @Override
    public AccessPermission newAccessPermission(KapuaId scopeId) {
        return new AccessPermissionImpl(scopeId);
    }

    @Override
    public AccessPermissionCreator newCreator(KapuaId scopeId) {
        return new AccessPermissionCreatorImpl(scopeId);
    }

    @Override
    public AccessPermissionQuery newQuery(KapuaId scopeId) {
        return new AccessPermissionQueryImpl(scopeId);
    }

    @Override
    public AccessPermissionListResult newAccessPermissionListResult() {
        return new AccessPermissionListResultImpl();
    }

    @Override
    public AccessPermission newAccessPermission() {
        return new AccessPermissionImpl();
    }

}
