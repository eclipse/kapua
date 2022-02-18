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
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.storable.exception.MappingException;

import java.util.Map;

/**
 * Message mediator definition
 */
public interface MessageStoreMediator {

    /**
     * Get the message metadata
     *
     * @param scopeId
     * @param indexedOn
     * @return
     * @throws ClientException
     */
    Metadata getMetadata(KapuaId scopeId, long indexedOn) throws ClientException, MappingException;

    /**
     * On after message mappings event handler
     *
     * @param scopeId
     * @param indexedOn
     * @param metrics
     * @throws ClientException
     */
    void onUpdatedMappings(KapuaId scopeId, long indexedOn, Map<String, Metric> metrics) throws ClientException, MappingException;

    /**
     * On after message store event handler
     *
     * @param messageInfo
     * @param message
     * @throws ClientException
     */
    void onAfterMessageStore(MessageInfo messageInfo, DatastoreMessage message)
            throws KapuaIllegalArgumentException, ConfigurationException, MappingException, ClientException;
}
