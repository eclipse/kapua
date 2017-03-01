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
package org.eclipse.kapua.service.account;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaNamedEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * AccountService exposes APIs to manage Account objects.<br>
 * It includes APIs to create, update, find, list and delete Accounts.<br>
 * Instances of the AccountService can be acquired through the ServiceLocator object.
 * 
 * @since 1.0
 * 
 */
public interface AccountService extends KapuaEntityService<Account, AccountCreator>,
                                KapuaUpdatableEntityService<Account>,
                                KapuaNamedEntityService<Account>,
                                KapuaConfigurableService
{
    /**
     * Finds the account by account identifiers
     * 
     * @param id
     * @return
     * @throws KapuaException
     */
    public Account find(KapuaId id)
        throws KapuaException;
    
    /**
     * Returns the {@link AccountListResult} with elements matching the provided query.
     * 
     * @param query
     *            The {@link AccountQuery} used to filter results.
     * @return The {@link AccountListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    public AccountListResult query(KapuaQuery<Account> query)
            throws KapuaException;

    /**
     * Returns a List of direct child account of the provided account identifier
     * 
     * @param accountId the Id of the parent Account
     * @return List of direct child account of an account
     * @throws KapuaException
     */
    public AccountListResult findChildsRecursively(KapuaId accountId)
        throws KapuaException;
}
