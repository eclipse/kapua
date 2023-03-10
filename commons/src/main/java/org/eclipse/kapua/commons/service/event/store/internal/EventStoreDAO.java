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
package org.eclipse.kapua.commons.service.event.store.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecord;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * @deprecated since 2.0.0 - use {@link org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordRepository} instead
 */
@Deprecated
public class EventStoreDAO {

    private EventStoreDAO() {
    }

    /**
     * Creates and return new KapuaEvent
     *
     * @param em
     * @param kapuaEvent
     * @return
     * @throws KapuaException
     */
    public static EventStoreRecord create(EntityManager em, EventStoreRecord kapuaEvent)
            throws KapuaException {
        return ServiceDAO.create(em, kapuaEvent);
    }

    /**
     * Updates the provided KapuaEvent
     *
     * @param em
     * @param kapuaEvent
     * @return
     * @throws KapuaException
     */
    public static EventStoreRecord update(EntityManager em, EventStoreRecord kapuaEvent)
            throws KapuaException {

        EventStoreRecordImpl kapuaEventImpl = (EventStoreRecordImpl) kapuaEvent;

        return ServiceDAO.update(em, EventStoreRecordImpl.class, kapuaEventImpl);
    }

    /**
     * Finds the event by event identifier
     *
     * @param em
     * @param scopeId
     * @param eventId
     * @return
     */
    public static EventStoreRecord find(EntityManager em, KapuaId scopeId, KapuaId eventId) {
        return ServiceDAO.find(em, EventStoreRecordImpl.class, scopeId, eventId);
    }


}
