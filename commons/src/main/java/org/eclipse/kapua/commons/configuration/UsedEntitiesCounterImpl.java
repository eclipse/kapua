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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.jpa.EntityManagerContainer;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

public class UsedEntitiesCounterImpl<
        E extends KapuaEntity,
        C extends KapuaEntityCreator<E>,
        L extends KapuaListResult<E>,
        Q extends KapuaQuery,
        F extends KapuaEntityFactory<E, C, Q, L>
        > implements UsedEntitiesCounter {

    private final F factory;
    private final Domain domain;
    private final DaoCounter daoCounter;
    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;
    private final EntityManagerSession entityManagerSession;

    public UsedEntitiesCounterImpl(F factory,
                                   Domain domain,
                                   DaoCounter daoCounter,
                                   AuthorizationService authorizationService,
                                   PermissionFactory permissionFactory,
                                   EntityManagerSession entityManagerSession) {
        this.factory = factory;
        this.domain = domain;
        this.daoCounter = daoCounter;
        this.authorizationService = authorizationService;
        this.permissionFactory = permissionFactory;
        this.entityManagerSession = entityManagerSession;
    }

    @FunctionalInterface
    public static interface DaoCounter {
        long count(EntityManager em, KapuaQuery userPermissionQuery)
                throws KapuaException;
    }

    @Override
    public long countEntitiesInScope(KapuaId scopeId) throws KapuaException {
        final Q query = factory.newQuery(scopeId);

        //
        // Argument Validator
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(domain, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.doAction(EntityManagerContainer.<Long>create()
                .onResultHandler(em -> daoCounter.count(em, query)));
    }
}