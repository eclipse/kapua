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
package org.eclipse.kapua.service.authorization.domain.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;
import org.eclipse.kapua.service.authorization.domain.DomainService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link DomainService} implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class DomainServiceImpl extends AbstractKapuaService implements DomainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainServiceImpl.class);

    private static final Domain DOMAIN_DOMAIN = new DomainDomain();

    public DomainServiceImpl() {
        super(AuthorizationEntityManagerFactory.getInstance());
    }

    @Override
    public Domain create(DomainCreator domainCreator)
            throws KapuaException {
        ArgumentValidator.notNull(domainCreator, "domainCreator");
        ArgumentValidator.notEmptyOrNull(domainCreator.getName(), "domainCreator.name");
        ArgumentValidator.notEmptyOrNull(domainCreator.getServiceName(), "domainCreator.serviceName");
        ArgumentValidator.notNull(domainCreator.getActions(), "domainCreator.actions");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DOMAIN_DOMAIN, Actions.write, null));

        return entityManagerSession.onTransactedInsert(em -> DomainDAO.create(em, domainCreator));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId domainId) throws KapuaException {
        ArgumentValidator.notNull(domainId, "domainId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DOMAIN_DOMAIN, Actions.delete, null));

        entityManagerSession.onTransactedAction(em -> {
            if (DomainDAO.find(em, domainId) == null) {
                throw new KapuaEntityNotFoundException(Domain.TYPE, domainId);
            }

            DomainDAO.delete(em, domainId);
        });
    }

    @Override
    public Domain find(KapuaId scopeId, KapuaId domainId)
            throws KapuaException {
        ArgumentValidator.notNull(domainId, "domainId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DOMAIN_DOMAIN, Actions.read, KapuaId.ANY));

        return entityManagerSession.onResult(em -> DomainDAO.find(em, domainId));
    }

    @Override
    public Domain findByServiceName(String serviceName)
            throws KapuaException {
        ArgumentValidator.notEmptyOrNull(serviceName, "serviceName");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DOMAIN_DOMAIN, Actions.read, KapuaId.ANY));

        return entityManagerSession.onResult(em -> {
            DomainFactory domainFactory = locator.getFactory(DomainFactory.class);
            DomainQuery query = domainFactory.newQuery(null);
            query.setPredicate(new AttributePredicate<>(DomainPredicates.SERVICE_NAME, serviceName));

            DomainListResult results = DomainDAO.query(em, query);

            Domain domain = null;
            if (!results.isEmpty()) {
                domain = results.getItem(0);
            }
            return domain;
        });
    }

    @Override
    public DomainListResult query(KapuaQuery<Domain> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DOMAIN_DOMAIN, Actions.read, KapuaId.ANY));

        return entityManagerSession.onResult(em -> DomainDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<Domain> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DOMAIN_DOMAIN, Actions.read, KapuaId.ANY));

        return entityManagerSession.onResult(em -> DomainDAO.count(em, query));
    }

    //@ListenServiceEvent(fromAddress="account")
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        if (kapuaEvent == null) {
            //service bus error. Throw some exception?
        }
        LOGGER.info("DomainService: received kapua event from {}, operation {}", kapuaEvent.getService(), kapuaEvent.getOperation());
        if ("account".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteDomainByAccountId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        }
    }

    private void deleteDomainByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        DomainFactory domainFactory = locator.getFactory(DomainFactory.class);

        DomainQuery query = domainFactory.newQuery(accountId);

        DomainListResult domainsToDelete = query(query);

        for (Domain d : domainsToDelete.getItems()) {
            delete(d.getScopeId(), d.getId());
        }
    }
}
