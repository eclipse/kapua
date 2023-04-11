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
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RoleRepository;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;

public class RoleImplJpaRepository
        extends KapuaNamedEntityJpaRepository<Role, RoleImpl, RoleListResult>
        implements RoleRepository {
    public RoleImplJpaRepository(KapuaJpaRepositoryConfiguration jpaRepoConfig) {
        super(RoleImpl.class, () -> new RoleListResultImpl(), jpaRepoConfig);
    }

    @Override
    public Role delete(TxContext txContext, KapuaId scopeId, KapuaId roleId) throws KapuaException {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(txContext);
        // Check existence
        return doFind(em, scopeId, roleId)
                // Do delete
                .map(roleToDelete -> doDelete(em, roleToDelete))
                .orElseThrow(() -> new KapuaEntityNotFoundException(Role.TYPE, roleId));
    }
}
