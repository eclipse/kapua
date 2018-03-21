/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authorization.server;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtDomain;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtDomainRegistryService;
import org.eclipse.kapua.app.console.module.authorization.shared.util.KapuaGwtAuthorizationModelConverter;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;
import org.eclipse.kapua.service.authorization.domain.DomainRegistryService;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainPredicates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GwtDomainRegistryServiceImpl extends KapuaRemoteServiceServlet implements GwtDomainRegistryService {

    private static final long serialVersionUID = -699492835893299489L;

    @Override
    public List<GwtDomain> findAll() throws GwtKapuaException {
        List<GwtDomain> gwtDomainList = new ArrayList<GwtDomain>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DomainRegistryService domainRegistryService = locator.getService(DomainRegistryService.class);
            DomainFactory domainFactory = locator.getFactory(DomainFactory.class);
            DomainQuery query = domainFactory.newQuery(null);
            DomainListResult list = domainRegistryService.query(query);

            for (Domain domain : list.getItems()) {
                gwtDomainList.add(KapuaGwtAuthorizationModelConverter.convertDomain(domain));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        Collections.sort(gwtDomainList);
        return gwtDomainList;
    }

    @Override
    public List<GwtAction> findActionsByDomainName(String domainName) throws GwtKapuaException {
        List<GwtAction> gwtActionList = new ArrayList<GwtAction>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DomainRegistryService domainRegistryService = locator.getService(DomainRegistryService.class);
            DomainFactory domainFactory = locator.getFactory(DomainFactory.class);
            DomainQuery query = domainFactory.newQuery(null);
            query.setPredicate(new AttributePredicateImpl<String>(DomainPredicates.NAME, domainName));
            DomainListResult queryResult = domainRegistryService.query(query);
            if (!queryResult.isEmpty()) {
                for (Actions action : queryResult.getFirstItem().getActions()) {
                    gwtActionList.add(KapuaGwtAuthorizationModelConverter.convertAction(action));
                }
                Collections.sort(gwtActionList);
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtActionList;
    }

    @SuppressWarnings("unused")
    private String getServiceName(KapuaId scopeId, String domainName) throws GwtKapuaException {
        String serviceName = null;
        KapuaLocator locator = KapuaLocator.getInstance();
        DomainRegistryService domainRegistryService = locator.getService(DomainRegistryService.class);
        DomainFactory domainFactory = locator.getFactory(DomainFactory.class);
        DomainQuery domainQuery = domainFactory.newQuery(null);
        domainQuery.setScopeId(scopeId);
        domainQuery.setPredicate(new AttributePredicateImpl<String>(DomainPredicates.NAME, domainName));
        try {
            DomainListResult result = domainRegistryService.query(domainQuery);
            if (!result.isEmpty()) {
                serviceName = result.getFirstItem().getServiceName();
            }
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return serviceName;
    }

}
