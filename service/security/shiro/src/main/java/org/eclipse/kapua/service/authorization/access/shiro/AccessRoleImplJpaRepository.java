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
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleAttributes;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.access.AccessRoleRepository;

import java.util.function.Supplier;

public class AccessRoleImplJpaRepository
        extends KapuaEntityRepositoryJpaImpl<AccessRole, AccessRoleImpl, AccessRoleListResult>
        implements AccessRoleRepository {
    public AccessRoleImplJpaRepository(Supplier<AccessRoleListResult> listSupplier, EntityManagerSession entityManagerSession) {
        super(AccessRoleImpl.class, listSupplier, entityManagerSession);
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
        return entityManagerSession.doTransactedAction(em -> {
            //
            // Checking existence
            AccessRole entityToDelete = doFind(em, scopeId, accessRoleId);

            //
            // Deleting if found
            if (entityToDelete != null) {
                em.remove(entityToDelete);
                em.flush();
            } else {
                throw new KapuaEntityNotFoundException(AccessRole.TYPE, accessRoleId);
            }

            //
            // Returning deleted entity
            return entityToDelete;
        });
    }
}
