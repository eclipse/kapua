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

import java.util.Map;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.internal.schema.Metadata;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;

/**
 * Message mediator definition
 *
 * @since 1.0.0
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
    public Metadata getMetadata(KapuaId scopeId, long indexedOn)
            throws ClientException;

    /**
     * On after message mappings event handler
     *
     * @param scopeId
     * @param indexedOn
     * @param metrics
     * @throws ClientException
     */
    public void onUpdatedMappings(KapuaId scopeId, long indexedOn, Map<String, Metric> metrics)
            throws ClientException;

    /**
     * On after message store event handler
     *
     * @param messageInfo
     * @param message
     * @throws ClientException
     */
    public void onAfterMessageStore(MessageInfo messageInfo, DatastoreMessage message)
            throws KapuaIllegalArgumentException,
            ConfigurationException, ClientException;
}
