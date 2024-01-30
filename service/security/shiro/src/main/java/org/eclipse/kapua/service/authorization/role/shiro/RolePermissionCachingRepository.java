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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.commons.storage.KapuaEntityRepositoryCachingWrapper;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionRepository;
import org.eclipse.kapua.storage.TxContext;

public class RolePermissionCachingRepository
        extends KapuaEntityRepositoryCachingWrapper<RolePermission, RolePermissionListResult>
        implements RolePermissionRepository {

    private final RolePermissionRepository wrapped;

    public RolePermissionCachingRepository(RolePermissionRepository wrapped, EntityCache entityCache) {
        super(wrapped, entityCache);
        this.wrapped = wrapped;
    }

    @Override
    public RolePermission create(TxContext txContext, RolePermission entity) throws KapuaException {
        final RolePermission created = super.create(txContext, entity);
        entityCache.removeList(entity.getScopeId(), entity.getRoleId());
        return created;
    }

    @Override
    public RolePermission delete(TxContext txContext, KapuaId scopeId, KapuaId entityId) throws KapuaException {
        final RolePermission deleted = super.delete(txContext, scopeId, entityId);
        entityCache.removeList(scopeId, deleted.getRoleId());
        return deleted;
    }

    @Override
    public RolePermission delete(TxContext txContext, RolePermission entityToDelete) {
        final RolePermission deleted = super.delete(txContext, entityToDelete);
        entityCache.removeList(deleted.getScopeId(), deleted.getRoleId());
        return deleted;
    }

    @Override
    public RolePermissionListResult findByRoleId(TxContext tx, KapuaId scopeId, KapuaId roleId) throws KapuaException {

        RolePermissionListResult listResult = (RolePermissionListResult) entityCache.getList(scopeId, roleId);
        if (listResult != null) {
            return listResult;
        }
        final RolePermissionListResult fromWrapped = wrapped.findByRoleId(tx, scopeId, roleId);

        entityCache.putList(scopeId, roleId, listResult);
        return fromWrapped;
    }

    @Override
    public RolePermissionListResult deleteAllByDomainAndAction(TxContext tx, String domainName, Actions actionToDelete) throws KapuaException {
        final RolePermissionListResult removed = wrapped.deleteAllByDomainAndAction(tx, domainName, actionToDelete);
        if (!removed.isEmpty()) {
            removed.getItems().forEach(item -> entityCache.removeList(item.getScopeId(), item.getRoleId()));
        }
        return removed;
    }
}
