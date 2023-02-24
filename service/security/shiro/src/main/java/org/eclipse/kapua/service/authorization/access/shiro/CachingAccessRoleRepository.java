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
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleRepository;

public class CachingAccessRoleRepository extends KapuaEntityRepositoryCachingWrapper<AccessRole> implements AccessRoleRepository {
    public CachingAccessRoleRepository(AccessRoleRepository wrapped, EntityCache entityCache) {
        super(wrapped, entityCache);
    }

    public KapuaListResult<AccessRole> findAll(KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        KapuaListResult<AccessRole> listResult = entityCache.getList(scopeId, accessInfoId);
        if (listResult == null) {
            // Do find and populate cache
            listResult = ((AccessRoleRepository) wrapped).findAll(scopeId, accessInfoId);
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
