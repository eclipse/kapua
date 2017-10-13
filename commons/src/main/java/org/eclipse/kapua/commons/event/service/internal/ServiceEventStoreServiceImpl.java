/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.event.service.internal;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.event.service.api.ServiceEvent;
import org.eclipse.kapua.commons.event.service.api.ServiceEventCreator;
import org.eclipse.kapua.commons.event.service.api.ServiceEventListResult;
import org.eclipse.kapua.commons.event.service.api.ServiceEventStoreService;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.event.RaiseServiceEvent;

/**
 * {@link ServiceEventStoreService} implementation.
 *
 * @since 1.0.0
 */
public class ServiceEventStoreServiceImpl extends AbstractKapuaService implements ServiceEventStoreService {

    private static final Domain KAPUA_EVENT_DOMAIN = new ServiceEventDomain();
    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    /**
     * Constructor.
     * 
     * @since 1.0.0
     */
    @Inject
    public ServiceEventStoreServiceImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    @Override
    @RaiseServiceEvent
    public ServiceEvent create(ServiceEventCreator kapuaEventCreator)
            throws KapuaException {

        throw new UnsupportedOperationException();
    }

    @Override
    @RaiseServiceEvent
    public ServiceEvent update(ServiceEvent kapuaEvent)
            throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(kapuaEvent.getId(), "kapuaEvent.id");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(KAPUA_EVENT_DOMAIN, Actions.write, kapuaEvent.getScopeId()));

        //
        // Do update
        return entityManagerSession.onTransactedResult(em -> {
            ServiceEvent oldKapuaEvent = ServiceEventStoreDAO.find(em, kapuaEvent.getId());
            if (oldKapuaEvent == null) {
                throw new KapuaEntityNotFoundException(ServiceEvent.TYPE, kapuaEvent.getId());
            }

            // Update
            return ServiceEventStoreDAO.update(em, kapuaEvent);
        });
    }

    @Override
    @RaiseServiceEvent
    public void delete(KapuaId scopeId, KapuaId kapuaEventId)
            throws KapuaException {

        //
        // Validation of the fields
        ArgumentValidator.notNull(kapuaEventId, "scopeId");
        ArgumentValidator.notNull(scopeId, "kapuaEventId");

        //
        // Check Access
        Actions action = Actions.write;
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(KAPUA_EVENT_DOMAIN, action, scopeId));

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> {
            ServiceEventStoreDAO.delete(em, kapuaEventId);
        });
    }

    @Override
    public ServiceEvent find(KapuaId scopeId, KapuaId kapuaEventId)
            throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(kapuaEventId, "kapuaEventId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(KAPUA_EVENT_DOMAIN, Actions.read, scopeId));

        //
        // Make sure kapuaEvent exists
        return findById(kapuaEventId);
    }

    @Override
    public ServiceEvent find(KapuaId kapuaEventId)
            throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(kapuaEventId, "kapuaEventId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(KAPUA_EVENT_DOMAIN, Actions.read, kapuaEventId));

        //
        // Make sure kapuaEvent exists
        return findById(kapuaEventId);
    }

    @Override
    public ServiceEventListResult query(KapuaQuery<ServiceEvent> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(KAPUA_EVENT_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> ServiceEventStoreDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<ServiceEvent> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(KAPUA_EVENT_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.onResult(em -> ServiceEventStoreDAO.count(em, query));
    }

    /**
     * Find an {@link ServiceEvent} without authorization checks.
     *
     * @param kapuaEventId
     * @return
     * @throws KapuaException
     * 
     * @since 1.0.0
     */
    private ServiceEvent findById(KapuaId kapuaEventId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(kapuaEventId, "kapuaEventId");

        return entityManagerSession.onResult(em -> ServiceEventStoreDAO.find(em, kapuaEventId));
    }

}
