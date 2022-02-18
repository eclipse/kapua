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
package org.eclipse.kapua.commons.service.event.store.api;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;

/**
 * KapuaEventService exposes APIs to manage KapuaEvent objects.<br>
 * It includes APIs to create, update, find, list and delete KapuaEvents.<br>
 * Instances of the KapuaEventService can be acquired through the ServiceLocator object.
 *
 * @since 1.0
 */
public interface EventStoreService extends KapuaEntityService<EventStoreRecord, EventStoreRecordCreator>,
        KapuaUpdatableEntityService<EventStoreRecord> {

    /**
     * Finds the kapuaEvent by kapuaEvent identifiers
     *
     * @param id
     * @return
     * @throws KapuaException
     */
    public EventStoreRecord find(KapuaId id)
            throws KapuaException;

    /**
     * Returns the {@link EventStoreRecordListResult} with elements matching the provided query.
     *
     * @param query The {@link EventStoreRecordQuery} used to filter results.
     * @return The {@link EventStoreRecordListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    public EventStoreRecordListResult query(KapuaQuery query)
            throws KapuaException;

}
