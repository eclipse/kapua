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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.JpaAwareTxContext;
import org.eclipse.kapua.commons.jpa.KapuaEntityJpaRepository;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleAttributes;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleRepository;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;

public class AccessRoleImplJpaRepository
        extends KapuaEntityJpaRepository<AccessRole, AccessRoleImpl, AccessRoleListResult>
        implements AccessRoleRepository {
    public AccessRoleImplJpaRepository(KapuaJpaRepositoryConfiguration configuration) {
        super(AccessRoleImpl.class, () -> new AccessRoleListResultImpl(), configuration);
    }

    @Override
    public AccessRoleListResult findByAccessInfoId(TxContext txContext, KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        final AccessRoleListResult res = listSupplier.get();
        res.addItems(doFindAllByField(txContext, scopeId, AccessRoleAttributes.ACCESS_INFO_ID, accessInfoId));
        return res;
    }

    @Override
    // This method is overridden for the sole purpose of throwing a different exception if the role did not exist in the first place
    public AccessRole delete(TxContext tx, KapuaId scopeId, KapuaId accessRoleId) throws KapuaException {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        // Checking existence
        return this.doFind(em, scopeId, accessRoleId)
                // Deleting if found
                .map(entityToDelete -> doDelete(em, entityToDelete))
                .orElseThrow(() -> new KapuaEntityNotFoundException(AccessRole.TYPE, accessRoleId));
    }
}
