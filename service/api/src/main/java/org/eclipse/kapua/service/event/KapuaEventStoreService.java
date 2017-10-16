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
package org.eclipse.kapua.service.event;

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
 * 
 */
public interface KapuaEventStoreService extends KapuaEntityService<KapuaEvent, KapuaEventCreator>,
        KapuaUpdatableEntityService<KapuaEvent> {

    /**
     * Finds the kapuaEvent by kapuaEvent identifiers
     * 
     * @param id
     * @return
     * @throws KapuaException
     */
    public KapuaEvent find(KapuaId id)
            throws KapuaException;

    /**
     * Returns the {@link KapuaEventListResult} with elements matching the provided query.
     * 
     * @param query
     *            The {@link KapuaEventStoreQuery} used to filter results.
     * @return The {@link KapuaEventListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    public KapuaEventListResult query(KapuaQuery<KapuaEvent> query)
            throws KapuaException;

}