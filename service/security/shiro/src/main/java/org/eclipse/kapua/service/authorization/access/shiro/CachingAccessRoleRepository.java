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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.commons.storage.KapuaEntityRepositoryCachingWrapper;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleRepository;
import org.eclipse.kapua.storage.TxContext;

public class CachingAccessRoleRepository extends KapuaEntityRepositoryCachingWrapper<AccessRole, AccessRoleListResult> implements AccessRoleRepository {
    private final AccessRoleRepository wrapped;

    public CachingAccessRoleRepository(AccessRoleRepository wrapped, EntityCache entityCache) {
        super(wrapped, entityCache);
        this.wrapped = wrapped;
    }

    public AccessRoleListResult findAll(TxContext txContext, KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        AccessRoleListResult listResult = (AccessRoleListResult) entityCache.getList(scopeId, accessInfoId);
        if (listResult == null) {
            // Do find and populate cache
            listResult = wrapped.findAll(txContext, scopeId, accessInfoId);
            entityCache.putList(scopeId, accessInfoId, listResult);
        }
        return listResult;
    }

    @Override
    public AccessRoleListResult findByAccessInfoId(TxContext txContext, KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        return wrapped.findByAccessInfoId(txContext, scopeId, accessInfoId);
    }

    @Override
    public AccessRole delete(TxContext tx, KapuaId scopeId, KapuaId entityId) throws KapuaException {
        final AccessRole deleted = super.delete(tx, scopeId, entityId);
        if (deleted != null) {
            entityCache.removeList(scopeId, deleted.getAccessInfoId());
        }
        return deleted;
    }
}
