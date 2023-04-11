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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.JpaAwareTxContext;
import org.eclipse.kapua.commons.jpa.KapuaEntityJpaRepository;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionRepository;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;

public class RolePermissionImplJpaRepository
        extends KapuaEntityJpaRepository<RolePermission, RolePermissionImpl, RolePermissionListResult>
        implements RolePermissionRepository {

    public RolePermissionImplJpaRepository(KapuaJpaRepositoryConfiguration configuration) {
        super(RolePermissionImpl.class, () -> new RolePermissionListResultImpl(), configuration);
    }

    @Override
    public RolePermissionListResult findByRoleId(TxContext tx, KapuaId scopeId, KapuaId roleId) throws KapuaException {
        final RolePermissionListResult res = listSupplier.get();
        res.addItems(doFindAllByField(tx, scopeId, RolePermissionImpl_.ROLE_ID, roleId));
        return res;
    }

    @Override
    // TODO: check if it is correct to remove this statement (already thrown by the delete method, but
    //  without TYPE)
    public RolePermission delete(TxContext txContext, KapuaId scopeId, KapuaId rolePermissionId) throws KapuaException {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(txContext);
        return doFind(em, scopeId, rolePermissionId)
                .map(toDelete -> doDelete(em, toDelete))
                .orElseThrow(() -> new KapuaEntityNotFoundException(RolePermission.TYPE, rolePermissionId));
    }
}
