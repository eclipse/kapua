/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.group.shiro;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupCreator;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.GroupQuery;

/**
 * {@link GroupFactory} implementation.
 * 
 * @since 1.0.0
 */
@KapuaProvider
public class GroupFactoryImpl implements GroupFactory {

    @Override
    public GroupCreator newCreator(KapuaId scopeId, String name) {
        GroupCreator creator = newCreator(scopeId);
        creator.setName(name);
        return creator;
    }

    @Override
    public Group newEntity(KapuaId scopeId) {
        return new GroupImpl(scopeId);
    }

    @Override
    public GroupListResult newListResult() {
        return new GroupListResultImpl();
    }

    @Override
    public GroupQuery newQuery(KapuaId scopeId) {
        return new GroupQueryImpl(scopeId);
    }

    @Override
    public GroupCreator newCreator(KapuaId scopeId) {
        return new GroupCreatorImpl(scopeId);
    }
}
