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

import org.eclipse.kapua.commons.repository.KapuaUpdatableEntityRepositoryCachingWrapper;
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.repository.KapuaUpdatableEntityRepository;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoRepository;

public class CachingAccessInfoRepository extends KapuaUpdatableEntityRepositoryCachingWrapper<AccessInfo> implements AccessInfoRepository {
    public CachingAccessInfoRepository(KapuaUpdatableEntityRepository<AccessInfo> wrapped, EntityCache entityCache) {
        super(wrapped, entityCache);
    }
}
