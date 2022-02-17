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
package org.eclipse.kapua.service.datastore;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.storable.StorableService;
import org.eclipse.kapua.service.storable.model.id.StorableId;

/**
 * {@link MessageStoreService} definition.
 * <p>
 * {@link StorableService} for {@link DatastoreMessage}.
 * <p>
 * It also manages the storing of:
 * <ul>
 *     <li>{@link org.eclipse.kapua.service.datastore.model.ChannelInfo}</li>
 *     <li>{@link org.eclipse.kapua.service.datastore.model.ClientInfo}</li>
 *     <li>{@link org.eclipse.kapua.service.datastore.model.MetricInfo}</li>
 * </ul>
 *
 * @since 1.0.0
 */
public interface MessageStoreService extends KapuaService, KapuaConfigurableService, StorableService<DatastoreMessage, MessageListResult, MessageQuery> {

    /**
     * Stores a {@link KapuaMessage}.
     *
     * @param message The {@link KapuaMessage} to store.
     * @return The {@link DatastoreMessage#getId()}
     * @throws KapuaException If something goes wrong.
     * @since 1.0.0
     */
    StorableId store(KapuaMessage<?, ?> message) throws KapuaException;

    /**
     * Stores a {@link KapuaMessage} forcing its {@link DatastoreMessage#getId()}.
     *
     * @param message     The {@link KapuaMessage} to store.
     * @param datastoreId The {@link StorableId} in {@link String} form to assing.
     * @return @return The {@link DatastoreMessage#getId()} which matches the given one.
     * @throws KapuaException If something goes wrong.
     * @since 1.0.0
     */
    StorableId store(KapuaMessage<?, ?> message, String datastoreId) throws KapuaException;

    /**
     * Deletes a {@link DatastoreMessage}  by the scope {@link KapuaId} and its {@link StorableId}.
     *
     * @param scopeId The scope {@link KapuaId}
     * @param id      The {@link StorableId}
     * @throws KapuaException If something goes wrong.
     * @since 1.0.0
     */
    void delete(KapuaId scopeId, StorableId id) throws KapuaException;

    /**
     * Deletes {@link DatastoreMessage}s according to the {@link MessageQuery}.
     *
     * @param query The {@link MessageQuery} to filter result to be deleted.
     * @throws KapuaException If something goes wrong.
     * @since 1.0.0
     */
    void delete(MessageQuery query) throws KapuaException;
}
