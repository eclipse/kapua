/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.storage.KapuaUpdatableEntityRepositoryCachingWrapper;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoRepository;
import org.eclipse.kapua.storage.TxContext;

import java.util.Optional;

public class AccessInfoCachingRepository
        extends KapuaUpdatableEntityRepositoryCachingWrapper<AccessInfo, AccessInfoListResult>
        implements AccessInfoRepository {
    private final AccessInfoRepository wrapped;
    private final AccessInfoCache entityCache;

    public AccessInfoCachingRepository(AccessInfoRepository wrapped, AccessInfoCache entityCache) {
        super(wrapped, entityCache);
        this.wrapped = wrapped;
        this.entityCache = entityCache;
    }

    @Override
    public Optional<AccessInfo> findByUserId(TxContext txContext, KapuaId scopeId, KapuaId userId) throws KapuaException {
        final AccessInfo fromCache = (AccessInfo) entityCache.getByUserId(scopeId, userId);
        if (fromCache != null) {
            return Optional.of(fromCache);
        }
        final Optional<AccessInfo> found = wrapped.findByUserId(txContext, scopeId, userId);
        found.ifPresent(entityCache::put);
        return found;
    }
}
