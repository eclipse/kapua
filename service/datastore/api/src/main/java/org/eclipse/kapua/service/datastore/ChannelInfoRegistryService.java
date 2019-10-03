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
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;

/**
 * Channel information registry service definition.<br>
 * The channel information binds information about the channels and the devices that published on these channels.
 *
 * @since 1.0.0
 */
public interface ChannelInfoRegistryService extends KapuaService {

    /**
     * Find channel information by identifier
     *
     * @param scopeId
     * @param id
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    ChannelInfo find(KapuaId scopeId, StorableId id) throws KapuaException;

    /**
     * Query for channels informations objects matching the given query
     *
     * @param query
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    ChannelInfoListResult query(ChannelInfoQuery query) throws KapuaException;

    /**
     * Get the channels informations count matching the given query
     *
     * @param query
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    long count(ChannelInfoQuery query) throws KapuaException;
}
