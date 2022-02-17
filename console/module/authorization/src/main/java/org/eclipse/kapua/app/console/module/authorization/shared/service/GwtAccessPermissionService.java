/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.authorization.shared.service;

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessPermissionCreator;

@RemoteServiceRelativePath("accesspermission")
public interface GwtAccessPermissionService extends RemoteService {

    public GwtAccessPermission create(GwtXSRFToken gwtXsrfToken, GwtAccessPermissionCreator gwtAccessPermissionCreator)
            throws GwtKapuaException;

    public PagingLoadResult<GwtAccessPermission> findByUserId(PagingLoadConfig loadConfig, String scopeShortId, String userShortId)
            throws GwtKapuaException;

    void delete(GwtXSRFToken gwtXsrfToken, String scopeShortId, String accessPermissionShortId)
            throws GwtKapuaException;
}
