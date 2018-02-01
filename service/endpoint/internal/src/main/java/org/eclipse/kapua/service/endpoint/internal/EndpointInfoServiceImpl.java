/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.endpoint.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfo;
import org.eclipse.kapua.service.endpoint.EndpointInfoCreator;
import org.eclipse.kapua.service.endpoint.EndpointInfoFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfoListResult;
import org.eclipse.kapua.service.endpoint.EndpointInfoQuery;
import org.eclipse.kapua.service.endpoint.EndpointInfoService;

/**
 * {@link EndpointInfoService} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class EndpointInfoServiceImpl
        extends AbstractKapuaConfigurableResourceLimitedService<EndpointInfo, EndpointInfoCreator, EndpointInfoService, EndpointInfoListResult, EndpointInfoQuery, EndpointInfoFactory>
        implements EndpointInfoService {

    private static final Domain ENDPOINT_DOMAIN = new EndpointInfoDomain();

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    public EndpointInfoServiceImpl() {
        super(EndpointInfoService.class.getName(), ENDPOINT_DOMAIN, EndpointEntityManagerFactory.getInstance(), EndpointInfoService.class, EndpointInfoFactory.class);
    }

    @Override
    public EndpointInfo create(EndpointInfoCreator endpointInfoCreator)
            throws KapuaException {
        ArgumentValidator.notNull(endpointInfoCreator, "endpointInfoCreator");
        ArgumentValidator.notNull(endpointInfoCreator.getScopeId(), "endpointInfoCreator.scopeId");
        ArgumentValidator.notEmptyOrNull(endpointInfoCreator.getSchema(), "endpointCreator.schema");
        ArgumentValidator.notEmptyOrNull(endpointInfoCreator.getDns(), "endpointCreator.dns");
        ArgumentValidator.notNegative(endpointInfoCreator.getPort(), "endpointCreator.port");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(ENDPOINT_DOMAIN, Actions.write, null));

        //
        // Do create
        return entityManagerSession.onTransactedInsert(em -> EndpointInfoDAO.create(em, endpointInfoCreator));
    }

    @Override
    public EndpointInfo update(EndpointInfo endpointInfo) throws KapuaException {
        ArgumentValidator.notNull(endpointInfo, "endpointInfo");
        ArgumentValidator.notNull(endpointInfo.getScopeId(), "endpointInfo.scopeId");
        ArgumentValidator.notEmptyOrNull(endpointInfo.getSchema(), "endpointInfo.schema");
        ArgumentValidator.notEmptyOrNull(endpointInfo.getDns(), "endpointInfo.dns");
        ArgumentValidator.notNegative(endpointInfo.getPort(), "endpointInfo.port");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(ENDPOINT_DOMAIN, Actions.write, null));

        //
        // Do update
        return entityManagerSession.onTransactedInsert(em -> EndpointInfoDAO.update(em, endpointInfo));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId endpointInfoId) throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(endpointInfoId, "endpointInfoId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(ENDPOINT_DOMAIN, Actions.delete, null));

        //
        // Do delete
        entityManagerSession.onTransactedAction(em -> EndpointInfoDAO.delete(em, endpointInfoId));
    }

    @Override
    public EndpointInfo find(KapuaId scopeId, KapuaId endpointInfoId)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(endpointInfoId, "endpointInfoId");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(ENDPOINT_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.onResult(em -> EndpointInfoDAO.find(em, endpointInfoId));
    }

    @Override
    public EndpointInfoListResult query(KapuaQuery<EndpointInfo> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(ENDPOINT_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do Query
        return entityManagerSession.onResult(em -> EndpointInfoDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<EndpointInfo> query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(ENDPOINT_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.onResult(em -> EndpointInfoDAO.count(em, query));
    }
}
