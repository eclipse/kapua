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
import org.eclipse.kapua.commons.event.service.api.ServiceEvent;
import org.eclipse.kapua.commons.event.service.api.ServiceEventListResult;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;

public class ServiceEventStoreDAO {

    private ServiceEventStoreDAO() {
    }

    /**
     * Creates and return new KapuaEvent
     * 
     * @param em
     * @param kapuaEvent
     * @return
     * @throws KapuaException
     */
    public static ServiceEvent create(EntityManager em, ServiceEvent kapuaEvent)
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
    public static ServiceEvent update(EntityManager em, ServiceEvent kapuaEvent)
            throws KapuaException {

        ServiceEventImpl kapuaEventImpl = (ServiceEventImpl) kapuaEvent;

        return ServiceDAO.update(em, ServiceEventImpl.class, kapuaEventImpl);
    }

    /**
     * Deletes the kapua event by event identifier
     * 
     * @param em
     * @param eventId
     * @throws KapuaEntityNotFoundException
     */
    public static void delete(EntityManager em, KapuaId eventId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, ServiceEventImpl.class, eventId);
    }

    /**
     * Finds the event by event identifier
     */
    public static ServiceEvent find(EntityManager em, KapuaId eventId) {
        return em.find(ServiceEventImpl.class, eventId);
    }

    /**
     * Returns the kapuaEvent list matching the provided query
     * 
     * @param em
     * @param kapuaEventQuery
     * @return
     * @throws KapuaException
     */
    public static ServiceEventListResult query(EntityManager em, KapuaQuery<ServiceEvent> kapuaEventQuery)
            throws KapuaException {
        return ServiceDAO.query(em, ServiceEvent.class, ServiceEventImpl.class, new ServiceEventListResultImpl(), kapuaEventQuery);
    }

    /**
     * Returns the kapuaEvent count matching the provided query
     * 
     * @param em
     * @param kapuaEventQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<ServiceEvent> kapuaEventQuery)
            throws KapuaException {
        return ServiceDAO.count(em, ServiceEvent.class, ServiceEventImpl.class, kapuaEventQuery);
    }
}