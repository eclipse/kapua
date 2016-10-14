/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.app.console.shared.model.GwtSession;
import org.eclipse.kapua.app.console.shared.model.GwtUser;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("auth")
public interface GwtAuthorizationService extends RemoteService
{

    public GwtSession login(GwtUser gwtUser)
        throws GwtKapuaException;

    /**
     * Return the currently authenticated user or null if no session has been established.
     */
    public GwtSession getCurrentSession()
        throws GwtKapuaException;

    /**
     * Checks whether the current Subject is granted the supplied permission;
     * returns true if access is granted, false otherwise.
     * <b>The API does not perform any access control check and it is meant for internal use.</b>
     *
     * @param gwtPermission
     * @return
     * @throws GwtKapuaException
     */
    public Boolean hasAccess(String gwtPermission)
        throws GwtKapuaException;

    public void logout()
        throws GwtKapuaException;

}
