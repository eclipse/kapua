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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.service;

import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtAccessRoleCreator;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("accessrole")
public interface GwtAccessRoleService extends RemoteService {

    public GwtAccessRole create(GwtXSRFToken gwtXsrfToken, GwtAccessRoleCreator gwtAccessRoleCreator)
            throws GwtKapuaException;

    public void delete(GwtXSRFToken gwtXsrfToken, String scopeShortId, String accessRoleShortId)
            throws GwtKapuaException;
    
    public PagingLoadResult<GwtAccessRole> findByUserId(PagingLoadConfig loadConfig, String scopeShortId, String userShortId)
            throws GwtKapuaException;
}