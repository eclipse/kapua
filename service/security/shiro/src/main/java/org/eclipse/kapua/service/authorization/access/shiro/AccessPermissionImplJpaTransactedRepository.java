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
import org.eclipse.kapua.commons.jpa.KapuaEntityJpaTransactedRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionAttributes;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionQuery;
import org.eclipse.kapua.service.authorization.access.AccessPermissionTransactedRepository;
import org.eclipse.kapua.storage.TxManager;

import java.util.function.Supplier;

public class AccessPermissionImplJpaTransactedRepository
        extends KapuaEntityJpaTransactedRepository<AccessPermission, AccessPermissionImpl, AccessPermissionListResult>
        implements AccessPermissionTransactedRepository {
    public AccessPermissionImplJpaTransactedRepository(TxManager txManager, Supplier<AccessPermissionListResult> listSupplier) {
        super(txManager, AccessPermissionImpl.class, listSupplier);
    }

    @Override
    // This method is overridden for the sole purpose of throwing a different exception if the role did not exist in the first place
    public AccessPermission delete(KapuaId scopeId, KapuaId accessPermissionId) throws KapuaException {
        return txManager.executeWithResult(tx -> {
            //
            // Checking existence
            AccessPermission entityToDelete = bareRepository.find(tx, scopeId, accessPermissionId);

            //
            // Deleting if found
            if (entityToDelete == null) {
                throw new KapuaEntityNotFoundException(AccessPermission.TYPE, accessPermissionId);
            }
            return bareRepository.doDelete(tx, entityToDelete);
        });
    }

    @Override
    public AccessPermissionListResult findByAccessInfoId(KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        AccessPermissionQuery query = new AccessPermissionQueryImpl(scopeId);
        query.setPredicate(query.attributePredicate(AccessPermissionAttributes.ACCESS_INFO_ID, accessInfoId));
        return this.query(query);
    }
}
