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
package org.eclipse.kapua.service.user.internal;

import org.eclipse.kapua.commons.service.internal.cache.NamedEntityCache;
import org.eclipse.kapua.commons.storage.KapuaNamedEntityRepositoryCachingWrapper;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserRepository;
import org.eclipse.kapua.storage.KapuaNamedEntityRepository;

public class CachingUserRepository extends KapuaNamedEntityRepositoryCachingWrapper<User, UserListResult> implements UserRepository {
    public CachingUserRepository(KapuaNamedEntityRepository<User, UserListResult> wrapped, NamedEntityCache entityCache) {
        super(wrapped, entityCache);
    }
}
