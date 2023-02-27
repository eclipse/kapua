/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityRepositoryJpaImpl;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;

public class ServiceConfigImplJpaRepository
        extends KapuaUpdatableEntityRepositoryJpaImpl<ServiceConfig, ServiceConfigImpl, ServiceConfigListResult> implements ServiceConfigRepository {
    @Override
    public ServiceConfigListResult findByScopeAndPid(KapuaId scopeId, String pid) throws KapuaException {
        ServiceConfigQueryImpl query = new ServiceConfigQueryImpl(scopeId);

        query.setPredicate(
                query.andPredicate(
                        query.attributePredicate(ServiceConfigAttributes.SERVICE_ID, pid),
                        query.attributePredicate(KapuaEntityAttributes.SCOPE_ID, scopeId)
                )
        );

        return this.query(query);
    }

    public ServiceConfigImplJpaRepository(
            EntityManagerSession entityManagerSession) {
        super(ServiceConfigImpl.class, () -> new ServiceConfigListResultImpl(), entityManagerSession);
    }

    @Override
    public ServiceConfig create(ServiceConfig entity) throws KapuaException {
        return super.create(entity);
    }
}
