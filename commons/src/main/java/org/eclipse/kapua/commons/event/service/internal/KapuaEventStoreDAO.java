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
package org.eclipse.kapua.commons.event.service.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.event.KapuaEvent;
import org.eclipse.kapua.service.event.KapuaEventListResult;

public class KapuaEventStoreDAO {

    private KapuaEventStoreDAO() {
    }

    /**
     * Creates and return new KapuaEvent
     * 
     * @param em
     * @param kapuaEvent
     * @return
     * @throws KapuaException
     */
    public static KapuaEvent create(EntityManager em, KapuaEvent kapuaEvent)
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
    public static KapuaEvent update(EntityManager em, KapuaEvent kapuaEvent)
            throws KapuaException {

        KapuaEventImpl kapuaEventImpl = (KapuaEventImpl) kapuaEvent;

        return ServiceDAO.update(em, KapuaEventImpl.class, kapuaEventImpl);
    }

    /**
     * Deletes the kapua event by event identifier
     * 
     * @param em
     * @param eventId
     * @throws KapuaEntityNotFoundException
     *             If the {@link Account} is not found
     */
    public static void delete(EntityManager em, KapuaId eventId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, KapuaEventImpl.class, eventId);
    }

    /**
     * Finds the event by event identifier
     */
    public static KapuaEvent find(EntityManager em, KapuaId eventId) {
        return em.find(KapuaEventImpl.class, eventId);
    }

    /**
     * Returns the kapuaEvent list matching the provided query
     * 
     * @param em
     * @param kapuaEventQuery
     * @return
     * @throws KapuaException
     */
    public static KapuaEventListResult query(EntityManager em, KapuaQuery<KapuaEvent> kapuaEventQuery)
            throws KapuaException {
        return ServiceDAO.query(em, KapuaEvent.class, KapuaEventImpl.class, new KapuaEventListResultImpl(), kapuaEventQuery);
    }

    /**
     * Returns the kapuaEvent count matching the provided query
     * 
     * @param em
     * @param kapuaEventQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<KapuaEvent> kapuaEventQuery)
            throws KapuaException {
        return ServiceDAO.count(em, KapuaEvent.class, KapuaEventImpl.class, kapuaEventQuery);
    }
}