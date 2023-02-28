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
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleAttributes;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.access.AccessRoleTransactedRepository;
import org.eclipse.kapua.storage.TxManager;

import java.util.function.Supplier;

public class AccessRoleImplJpaTransactedRepository
        extends KapuaEntityJpaTransactedRepository<AccessRole, AccessRoleImpl, AccessRoleListResult>
        implements AccessRoleTransactedRepository {
    public AccessRoleImplJpaTransactedRepository(TxManager txManager, Supplier<AccessRoleListResult> listSupplier) {
        super(txManager, AccessRoleImpl.class, listSupplier);
    }

    @Override
    public AccessRoleListResult findAll(KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        // Do find and populate cache
        AccessRoleQuery query = new AccessRoleQueryImpl(scopeId);
        query.setPredicate(query.attributePredicate(AccessRoleAttributes.ACCESS_INFO_ID, accessInfoId));

        return this.query(query);
    }

    @Override
    // This method is overridden for the sole purpose of throwing a different exception if the role did not exist in the first place
    public AccessRole delete(KapuaId scopeId, KapuaId accessRoleId) throws KapuaException {
        return txManager.executeWithResult(tx -> {
            //
            // Checking existence
            AccessRole entityToDelete = bareRepository.find(tx, scopeId, accessRoleId);

            //
            // Deleting if found
            if (entityToDelete == null) {
                throw new KapuaEntityNotFoundException(AccessRole.TYPE, accessRoleId);
            }
            return bareRepository.doDelete(tx, entityToDelete);
        });
    }
}
