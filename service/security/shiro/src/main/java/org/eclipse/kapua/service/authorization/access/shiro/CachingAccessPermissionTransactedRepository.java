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
import org.eclipse.kapua.commons.storage.KapuaEntityTransactedRepositoryCachingWrapper;
import org.eclipse.kapua.commons.service.internal.cache.EntityCache;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionAttributes;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionQuery;
import org.eclipse.kapua.service.authorization.access.AccessPermissionTransactedRepository;

public class CachingAccessPermissionTransactedRepository extends KapuaEntityTransactedRepositoryCachingWrapper<AccessPermission, AccessPermissionListResult> implements AccessPermissionTransactedRepository {
    public CachingAccessPermissionTransactedRepository(AccessPermissionTransactedRepository wrapped, EntityCache entityCache) {
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
    public AccessPermissionListResult findByAccessInfoId(KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        AccessPermissionListResult listResult = (AccessPermissionListResult) entityCache.getList(scopeId, accessInfoId);
        if (listResult == null) {
            //
            // Build query
            AccessPermissionQuery query = new AccessPermissionQueryImpl(scopeId);
            query.setPredicate(query.attributePredicate(AccessPermissionAttributes.ACCESS_INFO_ID, accessInfoId));

            listResult = ((AccessPermissionTransactedRepository) wrapped).findByAccessInfoId(scopeId, accessInfoId);
            entityCache.putList(scopeId, accessInfoId, listResult);
        }
        return listResult;
    }
}
