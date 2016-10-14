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
package org.eclipse.kapua.service.authorization.user.role.shiro;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.user.role.UserRolesCreator;
import org.eclipse.kapua.service.authorization.user.role.UserRolesFactory;
import org.eclipse.kapua.service.authorization.user.role.UserRolesQuery;

/**
 * User roles factory service implementation.
 * 
 * @since 1.0
 * 
 */
public class UserRolesFactoryImpl implements UserRolesFactory
{

    @Override
    public UserRolesCreator newCreator(KapuaId scopeId)
    {
        return new UserRolesCreatorImpl(scopeId);
    }

    @Override
    public UserRolesQuery newQuery(KapuaId scopeId)
    {
        return new UserRolesQueryImpl(scopeId);
    }

}
