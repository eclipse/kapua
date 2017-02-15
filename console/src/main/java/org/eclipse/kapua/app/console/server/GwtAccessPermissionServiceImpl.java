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
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessPermission;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessPermissionCreator;
import org.eclipse.kapua.app.console.shared.service.GwtAccessPermissionService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.*;

import java.util.ArrayList;
import java.util.List;

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
            AccessPermissionCreator accessInfoCreator = GwtKapuaModelConverter.convert(gwtAccessPermissionCreator);

            // Create
            KapuaLocator locator = KapuaLocator.getInstance();
            AccessPermissionService accessPermissionService = locator.getService(AccessPermissionService.class);
            AccessPermission accessPermission = accessPermissionService.create(accessInfoCreator);

            // Convert
            gwtAccessPermission = KapuaGwtModelConverter.convert(accessPermission);

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
            KapuaId scopeId = GwtKapuaModelConverter.convert(scopeShortId);
            KapuaId accessPermissionId = GwtKapuaModelConverter.convert(accessPermissionShortId);

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

                KapuaId scopeId = GwtKapuaModelConverter.convert(scopeShortId);
                KapuaId userId = GwtKapuaModelConverter.convert(userShortId);

                AccessInfo accessInfo = accessInfoService.findByUserId(scopeId, userId);
                
                if (accessInfo != null) {
                    AccessPermissionListResult accessPermissionList = accessPermissionService.findByAccessInfoId(scopeId, accessInfo.getId());

                    for (AccessPermission accessPermission : accessPermissionList.getItems()) {
                        GwtAccessPermission gwtAccessPermission = KapuaGwtModelConverter.convert(accessPermission);
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