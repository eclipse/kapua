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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.server;

import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtDomain;
import org.eclipse.kapua.app.console.shared.service.GwtDomainService;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authorization.domain.*;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainPredicates;
import org.eclipse.kapua.service.authorization.permission.Action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GwtDomainServiceImpl extends KapuaRemoteServiceServlet implements GwtDomainService {

    /**
     * 
     */
    private static final long serialVersionUID = -699492835893299489L;

    @Override
    public List<GwtDomain> findAll() throws GwtKapuaException {
        List<GwtDomain> gwtDomainList = new ArrayList<GwtDomain>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DomainService domainService = locator.getService(DomainService.class);
            DomainFactory domainFactory = locator.getFactory(DomainFactory.class);
            DomainQuery query = domainFactory.newQuery();
            DomainListResult list = domainService.query(query);

            for (Domain domain : list.getItems()) {
                gwtDomainList.add(KapuaGwtModelConverter.convertDomain(domain.getName()));
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
            DomainService domainService = locator.getService(DomainService.class);
            DomainFactory domainFactory = locator.getFactory(DomainFactory.class);
            DomainQuery query = domainFactory.newQuery();
            query.setPredicate(new AttributePredicate<String>(DomainPredicates.NAME, domainName));
            DomainListResult queryResult = domainService.query(query);
            if (!queryResult.isEmpty()) {
                for (Action action : queryResult.getFirstItem().getActions()) {
                    gwtActionList.add(KapuaGwtModelConverter.convertAction(action));
                }
                Collections.sort(gwtActionList);
            }
            
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtActionList;
    }
    
}