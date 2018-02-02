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

import com.google.common.collect.Lists;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.SystemUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
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

import java.net.URI;
import java.net.URISyntaxException;

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

    private static final AccountService ACCOUNT_SERVICE = LOCATOR.getService(AccountService.class);
    private static final AccountFactory ACCOUNT_FACTORY = LOCATOR.getFactory(AccountFactory.class);

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
        return entityManagerSession.onResult(em -> {
            EndpointInfoListResult endpointInfoListResult = EndpointInfoDAO.query(em, query);

            if (endpointInfoListResult.isEmpty() && query.getScopeId() != null) {

                KapuaId scopeId = query.getScopeId();
                do {
                    Account account = ACCOUNT_SERVICE.find(scopeId);

                    query.setScopeId(account.getScopeId());
                    endpointInfoListResult = EndpointInfoDAO.query(em, query);

                    scopeId = query.getScopeId();
                }
                while (scopeId != null && endpointInfoListResult.isEmpty());
            }

            if (endpointInfoListResult.isEmpty()) {
                URI endpointInfoURI = null;
                try {
                    endpointInfoURI = SystemUtils.getNodeURI();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                EndpointInfo endpointInfo = new EndpointInfoImpl();
                endpointInfo.setSchema(endpointInfoURI.getScheme());
                endpointInfo.setDns(endpointInfoURI.getHost());
                endpointInfo.setPort(endpointInfoURI.getPort());
                endpointInfo.setSecure(false);

                endpointInfoListResult.addItems(Lists.newArrayList(endpointInfo));
            }

            return endpointInfoListResult;
        });
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
        return entityManagerSession.onResult(em -> {
            long endpointInfoCount = EndpointInfoDAO.count(em, query);

            if (endpointInfoCount == 0 && query.getScopeId() != null) {

                KapuaId scopeId = query.getScopeId();
                do {
                    Account account = ACCOUNT_SERVICE.find(scopeId);

                    query.setScopeId(account.getScopeId());
                    endpointInfoCount = EndpointInfoDAO.count(em, query);

                    scopeId = query.getScopeId();
                }
                while (scopeId != null && endpointInfoCount == 0);
            }

            try {
                if (endpointInfoCount == 0 && SystemUtils.getNodeURI() != null) {
                    endpointInfoCount = 1;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            return endpointInfoCount;
        });
    }
}
