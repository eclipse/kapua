/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.commons.storage.KapuaUpdatableEntityRepositoryCachingWrapper;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.storage.KapuaUpdatableEntityRepository;
import org.eclipse.kapua.storage.TxContext;

public class CachingServiceConfigRepository
        extends KapuaUpdatableEntityRepositoryCachingWrapper<ServiceConfig, ServiceConfigListResult>
        implements ServiceConfigRepository {
    private final KapuaUpdatableEntityRepository<ServiceConfig, ServiceConfigListResult> wrapped;

    public CachingServiceConfigRepository(KapuaUpdatableEntityRepository<ServiceConfig, ServiceConfigListResult> wrapped, EntityCache entityCache) {
        super(wrapped, entityCache);
        this.wrapped = wrapped;
    }

    @Override
    public ServiceConfig create(TxContext txContext, ServiceConfig entity) throws KapuaException {
        final ServiceConfig created = super.create(txContext, entity);
        entityCache.removeList(created.getScopeId(), created.getPid());
        return created;
    }

    @Override
    public ServiceConfig update(TxContext txContext, ServiceConfig entity) throws KapuaException {
        final ServiceConfig updated = super.update(txContext, entity);
        entityCache.removeList(updated.getScopeId(), updated.getPid());
        return updated;
    }

    @Override
    public ServiceConfigListResult findByScopeAndPid(TxContext txContext, KapuaId scopeId, String pid) throws KapuaException {
        final ServiceConfigListResult cached = (ServiceConfigListResult) entityCache.getList(scopeId, pid);
        if (cached != null) {
            return cached;
        }
        final ServiceConfigListResult found = ((ServiceConfigRepository) wrapped).findByScopeAndPid(txContext, scopeId, pid);
        entityCache.putList(scopeId, pid, found);
        return found;
    }

    @Override
    public ServiceConfigListResult query(TxContext txContext, KapuaQuery kapuaQuery) throws KapuaException {
        return super.query(txContext, kapuaQuery);
    }
}
