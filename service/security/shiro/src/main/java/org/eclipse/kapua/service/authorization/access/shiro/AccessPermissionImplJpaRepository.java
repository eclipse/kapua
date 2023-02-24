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
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaEntityRepositoryJpaImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionAttributes;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionQuery;
import org.eclipse.kapua.service.authorization.access.AccessPermissionRepository;

import java.util.function.Supplier;

public class AccessPermissionImplJpaRepository
        extends KapuaEntityRepositoryJpaImpl<AccessPermission, AccessPermissionImpl, AccessPermissionListResult>
        implements AccessPermissionRepository {
    public AccessPermissionImplJpaRepository(Supplier<AccessPermissionListResult> listSupplier, EntityManagerSession entityManagerSession) {
        super(AccessPermissionImpl.class, listSupplier, entityManagerSession);
    }

    @Override
    // This method is overridden for the sole purpose of throwing a different exception if the role did not exist in the first place
    public AccessPermission delete(KapuaId scopeId, KapuaId accessPermissionId) throws KapuaException {
        return entityManagerSession.doTransactedAction(em -> {
            //
            // Checking existence
            AccessPermission entityToDelete = doFind(em, scopeId, accessPermissionId);

            //
            // Deleting if found
            if (entityToDelete != null) {
                em.remove(entityToDelete);
                em.flush();
            } else {
                throw new KapuaEntityNotFoundException(AccessPermission.TYPE, accessPermissionId);
            }

            //
            // Returning deleted entity
            return entityToDelete;
        });
    }

    @Override
    public AccessPermissionListResult findByAccessInfoId(KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        AccessPermissionQuery query = new AccessPermissionQueryImpl(scopeId);
        query.setPredicate(query.attributePredicate(AccessPermissionAttributes.ACCESS_INFO_ID, accessInfoId));
        return this.query(query);
    }
}
