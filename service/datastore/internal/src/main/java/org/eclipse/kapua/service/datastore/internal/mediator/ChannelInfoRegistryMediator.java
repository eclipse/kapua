/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.mediator;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.schema.Metadata;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.exception.QueryMappingException;
import org.eclipse.kapua.service.storable.exception.MappingException;

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
    Metadata getMetadata(KapuaId scopeId, long indexedOn)
            throws ClientException, MappingException;

    /**
     * On before channel info delete event handler
     *
     * @param channelInfo
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    void onBeforeChannelInfoDelete(ChannelInfo channelInfo)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            QueryMappingException,
            ClientException;

    /**
     * On after channel info delete event handler
     *
     * @param channelInfo
     */
    void onAfterChannelInfoDelete(ChannelInfo channelInfo);
}
