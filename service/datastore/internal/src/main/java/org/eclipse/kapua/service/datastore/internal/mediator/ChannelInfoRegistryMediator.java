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
package org.eclipse.kapua.service.datastore.internal.mediator;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.QueryMappingException;
import org.eclipse.kapua.service.datastore.internal.schema.Metadata;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;

/**
 * Channel information registry mediator definition
 *
 * @since 1.0.0
 */
public interface ChannelInfoRegistryMediator {

    /**
     * Get the channel info metadata
     *
     * @param scopeId
     * @param indexedOn
     * @return
     * @throws ClientException
     */
    public Metadata getMetadata(KapuaId scopeId, long indexedOn)
            throws ClientException;

    /**
     * On before channel info delete event handler
     *
     * @param channelInfo
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public void onBeforeChannelInfoDelete(ChannelInfo channelInfo)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            QueryMappingException,
            ClientException;

    /**
     * On after channel info delete event handler
     *
     * @param channelInfo
     */
    public void onAfterChannelInfoDelete(ChannelInfo channelInfo);
}
