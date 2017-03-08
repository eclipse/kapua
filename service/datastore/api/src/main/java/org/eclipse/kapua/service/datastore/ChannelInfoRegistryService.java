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
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;

/**
 * Channel information registry service definition.<br>
 * The channel information binds information about the channels and the devices that published on these channels.
 * 
 * @since 1.0
 *
 */
public interface ChannelInfoRegistryService extends KapuaService,
                                            KapuaConfigurableService
{

    /**
     * Delete channel information by identifier
     * 
     * @param scopeId
     * @param id
     * @throws KapuaException
     */
    public void delete(KapuaId scopeId, StorableId id)
        throws KapuaException;

    /**
     * Find channel information by identifier
     * 
     * @param scopeId
     * @param id
     * @return
     * @throws KapuaException
     */
    public ChannelInfo find(KapuaId scopeId, StorableId id)
        throws KapuaException;

    /**
     * Query for channels informations objects matching the given query
     * 
     * @param scopeId
     * @param query
     * @return
     * @throws KapuaException
     */
    public ChannelInfoListResult query(KapuaId scopeId, ChannelInfoQuery query)
        throws KapuaException;

    /**
     * Get the channels informations count matching the given query
     * 
     * @param scopeId
     * @param query
     * @return
     * @throws KapuaException
     */
    public long count(KapuaId scopeId, ChannelInfoQuery query)
        throws KapuaException;

    /**
     * Delete channels informations matching the given query
     * 
     * @param scopeId
     * @param query
     * @throws KapuaException
     */
    public void delete(KapuaId scopeId, ChannelInfoQuery query)
        throws KapuaException;
}
