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
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleTransactedRepository;

public class CachingAccessRoleTransactedRepository extends KapuaEntityTransactedRepositoryCachingWrapper<AccessRole, AccessRoleListResult> implements AccessRoleTransactedRepository {
    public CachingAccessRoleTransactedRepository(AccessRoleTransactedRepository wrapped, EntityCache entityCache) {
        super(wrapped, entityCache);
    }

    public AccessRoleListResult findAll(KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        AccessRoleListResult listResult = (AccessRoleListResult) entityCache.getList(scopeId, accessInfoId);
        if (listResult == null) {
            // Do find and populate cache
            listResult = ((AccessRoleTransactedRepository) wrapped).findAll(scopeId, accessInfoId);
            entityCache.putList(scopeId, accessInfoId, listResult);
        }
        return listResult;
    }

    @Override
    public AccessRole delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        final AccessRole deleted = super.delete(scopeId, entityId);
        if (deleted != null) {
            entityCache.removeList(scopeId, deleted.getAccessInfoId());
        }
        return deleted;
    }
}
