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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessPermissionCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtAccessPermissionService;
import org.eclipse.kapua.app.console.module.authorization.shared.util.GwtKapuaAuthorizationModelConverter;
import org.eclipse.kapua.app.console.module.authorization.shared.util.KapuaGwtAuthorizationModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

public class GwtAccessPermissionServiceImpl extends KapuaRemoteServiceServlet implements GwtAccessPermissionService {

    private static final long serialVersionUID = 3606053200278262228L;

    @Override
    public GwtAccessPermission create(GwtXSRFToken xsrfToken, GwtAccessPermissionCreator gwtAccessPermissionCreator) throws GwtKapuaException {

        //
        // Checking XSRF token
        checkXSRFToken(xsrfToken);

        //
        // Do create
        GwtAccessPermission gwtAccessPermission = null;
        try {
            // Convert from GWT Entity
            AccessPermissionCreator accessInfoCreator = GwtKapuaAuthorizationModelConverter.convertAccessPermissionCreator(gwtAccessPermissionCreator);

            // Create
            KapuaLocator locator = KapuaLocator.getInstance();
            AccessPermissionService accessPermissionService = locator.getService(AccessPermissionService.class);
            AccessPermission accessPermission = accessPermissionService.create(accessInfoCreator);

            // Convert
            gwtAccessPermission = KapuaGwtAuthorizationModelConverter.convertAccessPermission(accessPermission);

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        //
        // Return result
        return gwtAccessPermission;
    }

    @Override
    public void delete(GwtXSRFToken gwtXsrfToken, String scopeShortId, String accessPermissionShortId) throws GwtKapuaException {

        //
        // Checking XSRF token
        checkXSRFToken(gwtXsrfToken);

        //
        // Do delete
        try {
            // Convert from GWT Entity
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeShortId);
            KapuaId accessPermissionId = GwtKapuaCommonsModelConverter.convertKapuaId(accessPermissionShortId);

            // Delete
            KapuaLocator locator = KapuaLocator.getInstance();
            AccessPermissionService accessPermissionService = locator.getService(AccessPermissionService.class);
            accessPermissionService.delete(scopeId, accessPermissionId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
    public PagingLoadResult<GwtAccessPermission> findByUserId(PagingLoadConfig loadConfig, String scopeShortId, String userShortId) throws GwtKapuaException {
        //
        // Do get
        List<GwtAccessPermission> gwtAccessPermissions = new ArrayList<GwtAccessPermission>();
        if (userShortId != null) {

            try {
                KapuaLocator locator = KapuaLocator.getInstance();
                AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
                AccessPermissionService accessPermissionService = locator.getService(AccessPermissionService.class);

                KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeShortId);
                KapuaId userId = GwtKapuaCommonsModelConverter.convertKapuaId(userShortId);

                AccessInfo accessInfo = accessInfoService.findByUserId(scopeId, userId);

                if (accessInfo != null) {
                    AccessPermissionListResult accessPermissionList = accessPermissionService.findByAccessInfoId(scopeId, accessInfo.getId());

                    for (AccessPermission accessPermission : accessPermissionList.getItems()) {
                        GwtAccessPermission gwtAccessPermission = KapuaGwtAuthorizationModelConverter.convertAccessPermission(accessPermission);
                        gwtAccessPermissions.add(gwtAccessPermission);
                    }
                }
            } catch (Throwable t) {
                KapuaExceptionHandler.handle(t);
            }
        }
        return new BasePagingLoadResult<GwtAccessPermission>(gwtAccessPermissions, 0, gwtAccessPermissions.size());
    }
}
