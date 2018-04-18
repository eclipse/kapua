/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.endpoint.server;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpoint;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointCreator;
import org.eclipse.kapua.app.console.module.endpoint.shared.model.GwtEndpointQuery;
import org.eclipse.kapua.app.console.module.endpoint.shared.service.GwtEndpointService;
import org.eclipse.kapua.app.console.module.endpoint.shared.util.GwtKapuaEndpointModelConverter;
import org.eclipse.kapua.app.console.module.endpoint.shared.util.KapuaGwtEndpointModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.endpoint.EndpointInfo;
import org.eclipse.kapua.service.endpoint.EndpointInfoCreator;
import org.eclipse.kapua.service.endpoint.EndpointInfoFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfoListResult;
import org.eclipse.kapua.service.endpoint.EndpointInfoQuery;
import org.eclipse.kapua.service.endpoint.EndpointInfoService;
import org.eclipse.kapua.service.endpoint.EndpointUsage;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class GwtEndpointServiceImpl extends KapuaRemoteServiceServlet implements GwtEndpointService {

    private static final long serialVersionUID = 929002466564699535L;

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final EndpointInfoService ENDPOINT_INFO_SERVICE = LOCATOR.getService(EndpointInfoService.class);
    private static final EndpointInfoFactory ENDPOINT_INFO_FACTORY = LOCATOR.getFactory(EndpointInfoFactory.class);

    private static final UserService USER_SERVICE = LOCATOR.getService(UserService.class);
    private static final UserFactory USER_FACTORY = LOCATOR.getFactory(UserFactory.class);

    @Override
    public GwtEndpoint create(GwtEndpointCreator gwtEndpointCreator) throws GwtKapuaException {
        GwtEndpoint gwtEndpoint = null;
        try {
            KapuaId scopeId = KapuaEid.parseCompactId(gwtEndpointCreator.getScopeId());

            EndpointInfoCreator endpointCreator = ENDPOINT_INFO_FACTORY.newCreator(scopeId);
            endpointCreator.setSchema(gwtEndpointCreator.getSchema());
            endpointCreator.setDns(gwtEndpointCreator.getDns());
            endpointCreator.setPort(gwtEndpointCreator.getPort().intValue());
            endpointCreator.setSecure(gwtEndpointCreator.getSecure());

            EndpointInfo endpointInfo = ENDPOINT_INFO_SERVICE.create(endpointCreator);

            gwtEndpoint = KapuaGwtEndpointModelConverter.convertEndpoint(endpointInfo);

        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return gwtEndpoint;
    }

    @Override
    public GwtEndpoint update(GwtEndpoint gwtEndpoint) throws GwtKapuaException {
        GwtEndpoint gwtEndpointUpdated = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            KapuaId scopeId = KapuaEid.parseCompactId(gwtEndpoint.getScopeId());
            KapuaId endpointId = KapuaEid.parseCompactId(gwtEndpoint.getId());

            EndpointInfo endpointInfo = ENDPOINT_INFO_SERVICE.find(scopeId, endpointId);

            if (endpointInfo != null) {
                endpointInfo.setSchema(gwtEndpoint.getSchema());
                endpointInfo.setDns(gwtEndpoint.getDns());
                endpointInfo.setPort(gwtEndpoint.getPort().intValue());
                endpointInfo.setSecure(gwtEndpoint.getSecure());

                ENDPOINT_INFO_SERVICE.update(endpointInfo);

                gwtEndpointUpdated = KapuaGwtEndpointModelConverter.convertEndpoint(ENDPOINT_INFO_SERVICE.find(endpointInfo.getScopeId(), endpointInfo.getId()));
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return gwtEndpointUpdated;
    }

    @Override
    public GwtEndpoint find(String scopeShortId, String endpointShortId) throws GwtKapuaException {
        KapuaId scopeId = KapuaEid.parseCompactId(scopeShortId);
        KapuaId endpointId = KapuaEid.parseCompactId(endpointShortId);
        GwtEndpoint gwtEndpoint = null;
        try {
            EndpointInfo endpoint = ENDPOINT_INFO_SERVICE.find(scopeId, endpointId);

            if (endpoint != null) {
                gwtEndpoint = KapuaGwtEndpointModelConverter.convertEndpoint(endpoint);
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return gwtEndpoint;
    }

    @Override
    public PagingLoadResult<GwtEndpoint> query(PagingLoadConfig loadConfig, final GwtEndpointQuery gwtEndpointQuery) throws GwtKapuaException {
        int totalLength = 0;
        List<GwtEndpoint> gwtEndpointList = new ArrayList<GwtEndpoint>();
        try {
            EndpointInfoQuery endpointQuery = GwtKapuaEndpointModelConverter.convertEndpointQuery(loadConfig, gwtEndpointQuery);

            Map<String, String> usernameMap = new HashMap<String, String>();
            UserListResult userListResult = KapuaSecurityUtils.doPrivileged(new Callable<UserListResult>() {

                @Override
                public UserListResult call() throws Exception {
                    return USER_SERVICE.query(USER_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(gwtEndpointQuery.getScopeId())));
                }
            });

            for (User user : userListResult.getItems()) {
                usernameMap.put(user.getId().toCompactId(), user.getName());
            }
            EndpointInfoListResult endpoints = ENDPOINT_INFO_SERVICE.query(endpointQuery);
            if (!endpoints.isEmpty()) {
                totalLength = Long.valueOf(ENDPOINT_INFO_SERVICE.count(endpointQuery)).intValue();

                for (EndpointInfo ei : endpoints.getItems()) {
                    GwtEndpoint gwtEndpoint = KapuaGwtEndpointModelConverter.convertEndpoint(ei);
                    gwtEndpoint.setCreatedByName(ei.getCreatedBy() != null ? usernameMap.get(ei.getCreatedBy().toCompactId()) : null);
                    gwtEndpoint.setModifiedByName(ei.getModifiedBy() != null ? usernameMap.get(ei.getModifiedBy().toCompactId()) : null);
                    gwtEndpointList.add(gwtEndpoint);

                }
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BasePagingLoadResult<GwtEndpoint>(gwtEndpointList, loadConfig.getOffset(),
                totalLength);
    }

    @Override
    public void delete(String scopeId, String endpointId) throws GwtKapuaException {

        try {
            KapuaId scopeId2 = KapuaEid.parseCompactId(scopeId);
            KapuaId endpointId2 = GwtKapuaCommonsModelConverter.convertKapuaId(endpointId);

            ENDPOINT_INFO_SERVICE.delete(scopeId2, endpointId2);
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }

    }

    @Override
    public ListLoadResult<GwtGroupedNVPair> getEndpointDescription(String scopeShortId, String endpointShortId) throws GwtKapuaException {
        List<GwtGroupedNVPair> gwtEndpointDescription = new ArrayList<GwtGroupedNVPair>();
        try {
            final KapuaId scopeId = KapuaEid.parseCompactId(scopeShortId);
            KapuaId endpointId = KapuaEid.parseCompactId(endpointShortId);

            final EndpointInfo endpointInfo = ENDPOINT_INFO_SERVICE.find(scopeId, endpointId);

            User createdUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return USER_SERVICE.find(scopeId, endpointInfo.getCreatedBy());
                }
            });
            User modifiedUser = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return USER_SERVICE.find(scopeId, endpointInfo.getModifiedBy());
                }
            });

            if (endpointInfo != null) {
                // gwtEndpointDescription.add(new GwtGroupedNVPair("Entity", "Scope
                // Id", KapuaGwtCommonsModelConverter.convertKapuaId(endpointInfo.getScopeId())));
                gwtEndpointDescription.add(new GwtGroupedNVPair("endpointInfo", "endpointSchema", endpointInfo.getSchema()));
                gwtEndpointDescription.add(new GwtGroupedNVPair("endpointInfo", "endpointDns", endpointInfo.getDns()));
                gwtEndpointDescription.add(new GwtGroupedNVPair("endpointInfo", "endpointPort", endpointInfo.getPort()));
                gwtEndpointDescription.add(new GwtGroupedNVPair("endpointInfo", "endpointSecure", endpointInfo.getSecure()));

                List<String> usages = new ArrayList<String>();
                for (EndpointUsage eu : endpointInfo.getUsages()) {
                    usages.add(eu.getName());
                }
                gwtEndpointDescription.add(new GwtGroupedNVPair("endpointInfo", "endpointUsages", usages));

                gwtEndpointDescription.add(new GwtGroupedNVPair("entityInfo", "endpointModifiedOn", KapuaDateUtils.formatDateTime(endpointInfo.getModifiedOn())));
                gwtEndpointDescription.add(new GwtGroupedNVPair("entityInfo", "endpointModifiedBy", modifiedUser != null ? modifiedUser.getName() : null));
                gwtEndpointDescription.add(new GwtGroupedNVPair("entityInfo", "endpointCreatedOn", KapuaDateUtils.formatDateTime(endpointInfo.getCreatedOn())));
                gwtEndpointDescription.add(new GwtGroupedNVPair("entityInfo", "endpointCreatedBy", createdUser != null ? createdUser.getName() : null));

            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BaseListLoadResult<GwtGroupedNVPair>(gwtEndpointDescription);
    }

    @Override
    public List<GwtEndpoint> findAll(String scopeId) {
        List<GwtEndpoint> endpointList = new ArrayList<GwtEndpoint>();

        EndpointInfoQuery query = ENDPOINT_INFO_FACTORY.newQuery(GwtKapuaCommonsModelConverter.convertKapuaId(scopeId));
        try {
            EndpointInfoListResult result = ENDPOINT_INFO_SERVICE.query(query);

            for (EndpointInfo endpoint : result.getItems()) {
                endpointList.add(KapuaGwtEndpointModelConverter.convertEndpoint(endpoint));
            }
        } catch (KapuaException e) {
            e.printStackTrace();
        }
        return endpointList;
    }
}
