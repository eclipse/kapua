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
package org.eclipse.kapua.service.authorization.group.shiro;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.group.GroupCreator;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
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
        return new GroupCreatorImpl(scopeId, name);
    }

    @Override
    public GroupQuery newQuery(KapuaId scopeId) {
        return new GroupQueryImpl(scopeId);
    }
}
