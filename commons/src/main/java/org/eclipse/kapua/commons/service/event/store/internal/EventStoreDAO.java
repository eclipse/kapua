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
package org.eclipse.kapua.commons.service.event.store.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecord;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordListResult;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;

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

    /**
     * Returns the kapuaEvent list matching the provided query
     *
     * @param em
     * @param kapuaEventQuery
     * @return
     * @throws KapuaException
     */
    public static EventStoreRecordListResult query(EntityManager em, KapuaQuery<EventStoreRecord> kapuaEventQuery)
            throws KapuaException {
        return ServiceDAO.query(em, EventStoreRecord.class, EventStoreRecordImpl.class, new EventStoreRecordListResultImpl(), kapuaEventQuery);
    }

    /**
     * Returns the kapuaEvent count matching the provided query
     *
     * @param em
     * @param kapuaEventQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<EventStoreRecord> kapuaEventQuery)
            throws KapuaException {
        return ServiceDAO.count(em, EventStoreRecord.class, EventStoreRecordImpl.class, kapuaEventQuery);
    }

    /**
     * Deletes the kapua event by event identifier
     *
     * @param em
     * @param scopeId
     * @param eventId
     * @throws KapuaEntityNotFoundException
     */
    public static void delete(EntityManager em, KapuaId scopeId, KapuaId eventId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, EventStoreRecordImpl.class, scopeId, eventId);
    }
}