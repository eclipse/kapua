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
import org.eclipse.kapua.commons.repository.KapuaEntityRepositoryCachingWrapper;
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionAttributes;
import org.eclipse.kapua.service.authorization.access.AccessPermissionQuery;
import org.eclipse.kapua.service.authorization.access.AccessPermissionRepository;

public class CachingAccessPermissionRepository extends KapuaEntityRepositoryCachingWrapper<AccessPermission> implements AccessPermissionRepository {
    public CachingAccessPermissionRepository(AccessPermissionRepository wrapped, EntityCache entityCache) {
        super(wrapped, entityCache);
    }


    @Override
    public AccessPermission create(AccessPermission entity) throws KapuaException {
        final AccessPermission created = super.create(entity);
        entityCache.removeList(created.getScopeId(), created.getAccessInfoId());
        return created;
    }

    @Override
    public AccessPermission delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {

        final AccessPermission deleted = super.delete(scopeId, entityId);
        if (deleted != null) {
            entityCache.removeList(scopeId, deleted.getAccessInfoId());
        }
        return deleted;
    }

    @Override
    public KapuaListResult<AccessPermission> findByAccessInfoId(KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        KapuaListResult<AccessPermission> listResult = entityCache.getList(scopeId, accessInfoId);
        if (listResult == null) {
            //
            // Build query
            AccessPermissionQuery query = new AccessPermissionQueryImpl(scopeId);
            query.setPredicate(query.attributePredicate(AccessPermissionAttributes.ACCESS_INFO_ID, accessInfoId));

            listResult = ((AccessPermissionRepository) wrapped).findByAccessInfoId(scopeId, accessInfoId);
            entityCache.putList(scopeId, accessInfoId, listResult);
        }
        return listResult;
    }
}
