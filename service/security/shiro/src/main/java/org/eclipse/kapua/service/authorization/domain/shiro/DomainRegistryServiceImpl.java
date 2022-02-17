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
package org.eclipse.kapua.service.authorization.domain.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationDomains;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;
import org.eclipse.kapua.service.authorization.domain.DomainRegistryService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link DomainRegistryService} implementation.
 *
 * @since 1.0
 */
@KapuaProvider
public class DomainRegistryServiceImpl extends AbstractKapuaService implements DomainRegistryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DomainRegistryServiceImpl.class);

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
    private final PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

    public DomainRegistryServiceImpl() {
        super(AuthorizationEntityManagerFactory.getInstance());
    }

    @Override
    public Domain create(DomainCreator domainCreator)
            throws KapuaException {
        ArgumentValidator.notNull(domainCreator, "domainCreator");
        ArgumentValidator.notEmptyOrNull(domainCreator.getName(), "domainCreator.name");
        ArgumentValidator.notNull(domainCreator.getActions(), "domainCreator.actions");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.DOMAIN_DOMAIN, Actions.write, null));

        return entityManagerSession.doTransactedAction(em -> DomainDAO.create(em, domainCreator));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId domainId) throws KapuaException {
        ArgumentValidator.notNull(domainId, "domainId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.DOMAIN_DOMAIN, Actions.delete, null));

        entityManagerSession.doTransactedAction(em -> {
            if (DomainDAO.find(em, scopeId, domainId) == null) {
                throw new KapuaEntityNotFoundException(Domain.TYPE, domainId);
            }

            return DomainDAO.delete(em, scopeId, domainId);
        });
    }

    @Override
    public Domain find(KapuaId scopeId, KapuaId domainId)
            throws KapuaException {
        ArgumentValidator.notNull(domainId, "domainId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.DOMAIN_DOMAIN, Actions.read, KapuaId.ANY));

        return entityManagerSession.doAction(em -> DomainDAO.find(em, scopeId, domainId));
    }

    @Override
    public Domain findByName(String name)
            throws KapuaException {

        ArgumentValidator.notNull(name, "name");

        //
        // Do find
        return entityManagerSession.doAction(em -> {
            Domain domain = DomainDAO.findByName(em, name);

            if (domain != null) {
                authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.DOMAIN_DOMAIN, Actions.read, KapuaId.ANY));
            }

            return domain;
        });
    }

    @Override
    public DomainListResult query(KapuaQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.DOMAIN_DOMAIN, Actions.read, KapuaId.ANY));

        return entityManagerSession.doAction(em -> DomainDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(AuthorizationDomains.DOMAIN_DOMAIN, Actions.read, KapuaId.ANY));

        return entityManagerSession.doAction(em -> DomainDAO.count(em, query));
    }

    //@ListenServiceEvent(fromAddress="account")
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        if (kapuaEvent == null) {
            //service bus error. Throw some exception?
        }
        LOGGER.info("DomainRegistryService: received kapua event from {}, operation {}", kapuaEvent.getService(), kapuaEvent.getOperation());
        if ("account".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteDomainByAccountId(kapuaEvent.getEntityId());
        }
    }

    private void deleteDomainByAccountId(KapuaId accountId) throws KapuaException {
        DomainFactory domainFactory = locator.getFactory(DomainFactory.class);

        DomainQuery query = domainFactory.newQuery(accountId);

        DomainListResult domainsToDelete = query(query);

        for (Domain d : domainsToDelete.getItems()) {
            delete(d.getScopeId(), d.getId());
        }
    }
}
