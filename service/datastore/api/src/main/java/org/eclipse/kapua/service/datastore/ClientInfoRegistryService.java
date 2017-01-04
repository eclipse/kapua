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
 *******************************************************************************/
package org.eclipse.kapua.service.datastore;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;

/**
 * Client information registry service definition.<br>
 * The client information keeps information about the clients that published messages on the platform.
 * 
 * @since 1.0
 *
 */
public interface ClientInfoRegistryService extends KapuaService,
                                           KapuaConfigurableService
{

    /**
     * Delete client information by identifier
     * 
     * @param scopeId
     * @param id
     * @throws KapuaException
     */
    public void delete(KapuaId scopeId, StorableId id)
        throws KapuaException;

    /**
     * Find client information by identifier
     * 
     * @param scopeId
     * @param id
     * @return
     * @throws KapuaException
     */
    public ClientInfo find(KapuaId scopeId, StorableId id)
        throws KapuaException;

    /**
     * Query for clients informations objects matching the given query
     * 
     * @param scopeId
     * @param query
     * @return
     * @throws KapuaException
     */
    public ClientInfoListResult query(KapuaId scopeId, ClientInfoQuery query)
        throws KapuaException;

    /**
     * Get the clients informations count matching the given query
     * 
     * @param scopeId
     * @param query
     * @return
     * @throws KapuaException
     */
    public long count(KapuaId scopeId, ClientInfoQuery query)
        throws KapuaException;

    /**
     * Delete clients informations matching the given query
     * 
     * @param scopeId
     * @param query
     * @throws KapuaException
     */
    public void delete(KapuaId scopeId, ClientInfoQuery query)
        throws KapuaException;
}
