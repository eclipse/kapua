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
 *******************************************************************************/
package org.eclipse.kapua.service.user.internal;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;

/**
 * User factory service implementation.
 * 
 * @since 1.0
 *
 */
@KapuaProvider
public class UserFactoryImpl implements UserFactory {

    @Override
    public UserCreator newCreator(KapuaId scopeId, String name) {
        UserCreator creator = newCreator(scopeId);
        creator.setName(name);
        return creator;
    }

    @Override
    public UserQuery newQuery(KapuaId scopeId) {
        return new UserQueryImpl(scopeId);
    }

    @Override
    public UserListResult newListResult() {
        return new UserListResultImpl();
    }

    @Override
    public User newEntity(KapuaId scopeId) {
        return new UserImpl(scopeId);
    }

    @Override
    public UserCreator newCreator(KapuaId scopeId) {
        return new UserCreatorImpl(scopeId);
    }

}
