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
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessInfo;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessInfoCreator;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessRoleQuery;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;
import org.eclipse.kapua.app.console.shared.service.GwtAccessInfoService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.*;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

public class GwtAccessInfoServiceImpl extends KapuaRemoteServiceServlet implements GwtAccessInfoService {

    private static final long serialVersionUID = 3606053200278262228L;

    @Override
    public GwtAccessInfo create(GwtXSRFToken xsrfToken, GwtAccessInfoCreator gwtAccessInfoCreator) throws GwtKapuaException {

        //
        // Checking XSRF token
        checkXSRFToken(xsrfToken);

        //
        // Do create
        GwtAccessInfo gwtAccessInfo = null;
        try {
            // Convert from GWT Entity
            AccessInfoCreator accessInfoCreator = GwtKapuaModelConverter.convert(gwtAccessInfoCreator);

            // Create
            KapuaLocator locator = KapuaLocator.getInstance();
            AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
            AccessInfo accessInfo = accessInfoService.create(accessInfoCreator);

            // Convert
            gwtAccessInfo = KapuaGwtModelConverter.convert(accessInfo);

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        //
        // Return result
        return gwtAccessInfo;
    }

    @Override
    public void delete(GwtXSRFToken gwtXsrfToken, String scopeShortId, String accessInfoShortId) throws GwtKapuaException {

        //
        // Checking XSRF token
        checkXSRFToken(gwtXsrfToken);

        //
        // Do delete
        try {
            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaModelConverter.convert(scopeShortId);
            KapuaId accessInfoId = GwtKapuaModelConverter.convert(accessInfoShortId);

            // Delete
            KapuaLocator locator = KapuaLocator.getInstance();
            AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
            accessInfoService.delete(scopeId, accessInfoId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public GwtAccessInfo findByUserIdOrCreate(String scopeShortId, String userShortId) throws GwtKapuaException {
        GwtAccessInfo gwtAccessInfo = null;

        try {
            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaModelConverter.convert(scopeShortId);
            KapuaId userId = GwtKapuaModelConverter.convert(userShortId);

            // Find
            KapuaLocator locator = KapuaLocator.getInstance();
            AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
            AccessInfo accessInfo = accessInfoService.findByUserId(scopeId, userId);
            if (accessInfo == null) {
                AccessInfoFactory accessInfoFactory = locator.getFactory(AccessInfoFactory.class);
                AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(scopeId);
                accessInfoCreator.setUserId(userId);
                accessInfo = accessInfoService.create(accessInfoCreator);
            }
            gwtAccessInfo = KapuaGwtModelConverter.convert(accessInfo);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtAccessInfo;
    }

    @Override
    public PagingLoadResult<GwtUser> query(PagingLoadConfig pagingLoadConfig,
            GwtAccessRoleQuery query) throws GwtKapuaException {
        int totalLength = 0;
        List<GwtUser> list = new ArrayList<GwtUser>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            UserService userService = locator.getService(UserService.class);
            AccessRoleService accessRoleService = locator.getService(AccessRoleService.class);
            AccessRoleQuery accessRoleQuery = GwtKapuaModelConverter
                    .convertAccessRoleQuery(pagingLoadConfig, query);
            AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
            AccessRoleListResult accessRoleList = accessRoleService.query(accessRoleQuery);
            if (!accessRoleList.isEmpty()) {
                if (accessRoleList.getSize() >= pagingLoadConfig.getLimit()) {
                    totalLength = new Long(accessRoleService.count(accessRoleQuery)).intValue();

                } else {
                    totalLength = accessRoleList.getSize();
                }

                for (AccessRole a : accessRoleList.getItems()) {
                    AccessInfo accessInfo = accessInfoService.find(KapuaEid.parseCompactId(query.getScopeId()), a.getAccessInfoId());
                    User user = userService.find(KapuaEid.parseCompactId(query.getScopeId()), accessInfo.getUserId());
                    GwtUser gwtUser = KapuaGwtModelConverter.convert(user);
                    gwtUser.set("type", "USER");
                    list.add(gwtUser);
                }
            }
        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }
        return new BasePagingLoadResult<>(list, pagingLoadConfig.getOffset(), totalLength);
    }
}