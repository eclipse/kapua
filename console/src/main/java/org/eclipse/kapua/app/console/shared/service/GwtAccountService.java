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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccountCreator;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccountStringListItem;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("account")
public interface GwtAccountService extends RemoteService {

    /**
     * Creates a new Account based on the information provided in the supplied GwtAccountCreator. Each new account results in the creation of an Organization, a set of Users, and a provisioning step
     * to seed users into the MQTT broker instance.
     * 
     * @param gwtAccountCreator
     * @return
     * @throws GwtKapuaException
     */
    public GwtAccount create(GwtXSRFToken xsfrToken, GwtAccountCreator gwtAccountCreator)
        throws GwtKapuaException;

    /**
     * Returns a GwtAccount by its Id or null if an account with such Id does not exist.
     * 
     * @param accountId
     * @return
     */
    public GwtAccount find(String accountId)
        throws GwtKapuaException;

    /**
     * Returns a GwtAccount by its acountName or null if an account with such acountName does not exist.
     *
     * @param accountName
     * @return GwtAccount
     */
    public GwtAccount findByAccountName(String accountName)
        throws GwtKapuaException;

    /**
     * Get account info ad name values pairs
     * 
     * @param gwtAccountId
     * @return
     * @throws GwtKapuaException
     */
    public ListLoadResult<GwtGroupedNVPair> getAccountInfo(String gwtAccountId)
        throws GwtKapuaException;

    /**
     * Updates GwtAccount PROPERTIES ONLY in the database and returns the refreshed/reloaded entity instance.
     * 
     * @param xsfrToken
     * @param gwtAccount
     * @return
     * @throws GwtKapuaException
     */
    public GwtAccount updateAccountProperties(GwtXSRFToken xsfrToken, GwtAccount gwtAccount)
        throws GwtKapuaException;

    /**
     * Updates a GwtAccount in the database and returns the refreshed/reloaded entity instance.
     * 
     * @param xsfrToken
     * @param gwtAccount
     * @return
     * @throws GwtKapuaException
     */
    public GwtAccount update(GwtXSRFToken xsfrToken, GwtAccount gwtAccount)
        throws GwtKapuaException;

    /**
     * Deletes the supplied GwtAccount.
     * 
     * @param xsfrToken
     * @param gwtAccount
     * @throws GwtKapuaException
     */
    public void delete(GwtXSRFToken xsfrToken, GwtAccount gwtAccount)
        throws GwtKapuaException;

    /**
     * Lists GwtAccounts.
     * 
     * FIXME: Add query predicates, ordering and pagination.
     * 
     * @throws GwtKapuaException
     */
    public ListLoadResult<GwtAccount> findAll(String scopeId)
        throws GwtKapuaException;

    /**
     * Lists GwtAccounts child of the given accountId.
     * 
     * @param accountId
     *            The account id for which to find children
     * @param recoursive
     *            If true it list all child accounts. If false it list only the direct children
     * @return
     * @throws GwtKapuaException
     */
    ListLoadResult<GwtAccount> findChildren(String accountId, boolean recoursive)
        throws GwtKapuaException;

    /**
     * Lists all child of the given account id as a list of strings
     * 
     * @param scopeId
     *            The account id for which to find children
     * @param recoursive
     *            If true it list all child accounts. If false it list only the direct children
     * @return
     * @throws GwtKapuaException
     */
    ListLoadResult<GwtAccountStringListItem> findChildrenAsStrings(String scopeId, boolean recoursive)
        throws GwtKapuaException;

}
