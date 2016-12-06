/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.domain.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
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

/**
 * {@link DomainService} implementation.
 * 
 * @since 1.0
 *
 */
@KapuaProvider
public class DomainServiceImpl extends AbstractKapuaService implements DomainService {

    private static final Domain domainDomain = new DomainDomain();

    public DomainServiceImpl() {
        super(AuthorizationEntityManagerFactory.getInstance());
    }

    @Override
    public Domain create(DomainCreator domainCreator)
            throws KapuaException {
        ArgumentValidator.notNull(domainCreator, "domainCreator");
        ArgumentValidator.notEmptyOrNull(domainCreator.getName(), "domainCreator.name");
        ArgumentValidator.notEmptyOrNull(domainCreator.getServiceName(), "domainCreator.serviceName");
        ArgumentValidator.notNull(domainCreator.getActions(), "roleCreator.actions");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(domainDomain, Actions.write, null));

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
        authorizationService.checkPermission(permissionFactory.newPermission(domainDomain, Actions.delete, null));

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
        authorizationService.checkPermission(permissionFactory.newPermission(domainDomain, Actions.read, null));

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
        authorizationService.checkPermission(permissionFactory.newPermission(domainDomain, Actions.read, null));

        return entityManagerSession.onResult(em -> {
            DomainFactory domainFactory = locator.getFactory(DomainFactory.class);
            DomainQuery query = domainFactory.newQuery();
            query.setPredicate(new AttributePredicate<String>(DomainPredicates.SERVICE_NAME, serviceName));

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
        authorizationService.checkPermission(permissionFactory.newPermission(domainDomain, Actions.read, null));

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
        authorizationService.checkPermission(permissionFactory.newPermission(domainDomain, Actions.read, null));

        return entityManagerSession.onResult(em -> DomainDAO.count(em, query));
    }
}
