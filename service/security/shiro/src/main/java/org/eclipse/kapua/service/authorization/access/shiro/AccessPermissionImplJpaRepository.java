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
import org.eclipse.kapua.commons.jpa.KapuaEntityJpaRepository;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionAttributes;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionQuery;
import org.eclipse.kapua.service.authorization.access.AccessPermissionRepository;
import org.eclipse.kapua.storage.TxContext;

public class AccessPermissionImplJpaRepository
        extends KapuaEntityJpaRepository<AccessPermission, AccessPermissionImpl, AccessPermissionListResult>
        implements AccessPermissionRepository {
    public AccessPermissionImplJpaRepository(KapuaJpaRepositoryConfiguration configuration) {
        super(AccessPermissionImpl.class, AccessPermission.TYPE, () -> new AccessPermissionListResultImpl(), configuration);
    }

    @Override
    public AccessPermissionListResult findByAccessInfoId(TxContext tx, KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        AccessPermissionQuery query = new AccessPermissionQueryImpl(scopeId);
        query.setPredicate(query.attributePredicate(AccessPermissionAttributes.ACCESS_INFO_ID, accessInfoId));
        return this.query(tx, query);
    }
}
