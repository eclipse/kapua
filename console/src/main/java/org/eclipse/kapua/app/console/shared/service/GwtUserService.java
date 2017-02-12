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
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRole;
import org.eclipse.kapua.app.console.shared.model.authorization.GwtRoleQuery;
import org.eclipse.kapua.app.console.shared.model.user.GwtUser;
import org.eclipse.kapua.app.console.shared.model.user.GwtUserCreator;
import org.eclipse.kapua.app.console.shared.model.user.GwtUserQuery;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("user")
public interface GwtUserService extends RemoteService {

    /**
     * Creates a new user under the account specified in the UserCreator.
     * 
     * @param gwtUserCreator
     * @return
     * @throws GwtKapuaException
     */
    public GwtUser create(GwtXSRFToken xsfrToken, GwtUserCreator gwtUserCreator)
        throws GwtKapuaException;

    /**
     * Updates an User in the database and returns the refreshed/reloaded entity instance.
     * 
     * @param gwtUser
     * @return
     * @throws GwtKapuaException
     */
    public GwtUser update(GwtXSRFToken xsfrToken, GwtUser gwtUser)
        throws GwtKapuaException;

    /**
     * Delete the supplied User.
     * 
     * @param gwtUserId
     * @throws GwtKapuaException
     */
    public void delete(GwtXSRFToken xsfrToken, String accountId, String gwtUserId)
        throws GwtKapuaException;

    /**
     * Returns an User by its Id or null if an account with such Id does not exist.
     * 
     * @param userId
     * @return
     * @throws GwtKapuaException
     * 
     */
    public GwtUser find(String accountId, String userId)
        throws GwtKapuaException;

    /**
     * Returns the list of all User which belong to an account.
     * 
     * @param scopeIdStirng
     * @return
     * @throws GwtKapuaException
     * 
     */
    public ListLoadResult<GwtUser> findAll(String scopeIdString)
        throws GwtKapuaException;

    /**
     * Returns the list of all User matching the query.
     * 
     * @param gwtUserQuery
     * @return
     * @throws GwtKapuaException
     * 
     */
    public PagingLoadResult<GwtUser> query(PagingLoadConfig loadConfig, GwtUserQuery gwtUserQuery)
            throws GwtKapuaException;
}
