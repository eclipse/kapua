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

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Account factory service definition.
 * 
 * @since 1.0
 * 
 */
public interface AccountFactory extends KapuaObjectFactory
{

    /**
     * Creates a new {@link AccountCreator} for the specified name
     * 
     * @param scopeId
     * @param name
     * @return
     */
    public AccountCreator newAccountCreator(KapuaId scopeId, String name);
    
    /**
     * Creates a new account entity
     * 
     * @return
     */
    public Account newAccount();
    
    /**
     * Creates a new organization entity
     * 
     * @return
     */
    public Organization newOrganization();

    /**
     * Creates a new organization query for the specified scope identifier
     * 
     * @param scopeId
     * @return
     */
    public AccountQuery newQuery(KapuaId scopeId);
    
    /**
     * Creates a new account result list
     * 
     * @return
     */
    public AccountListResult newAccountListResult();
}
