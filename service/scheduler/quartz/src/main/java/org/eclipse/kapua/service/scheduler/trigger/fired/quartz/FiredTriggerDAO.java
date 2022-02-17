/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.trigger.fired.quartz;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTrigger;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerListResult;

/**
 * {@link FiredTrigger} DAO.
 *
 * @since 1.5.0
 */
public class FiredTriggerDAO {

    private FiredTriggerDAO() {
    }

    /**
     * Creates and return new FiredTrigger
     *
     * @param em                  The {@link EntityManager} that owns the transaction.
     * @param firedTriggerCreator The {@link FiredTriggerCreator} to persist.
     * @return The newly created {@link FiredTrigger}.
     * @since 1.5.0
     */
    public static FiredTrigger create(EntityManager em, FiredTriggerCreator firedTriggerCreator) {

        FiredTriggerImpl firedTriggerImpl = new FiredTriggerImpl(firedTriggerCreator.getScopeId());
        firedTriggerImpl.setTriggerId(firedTriggerCreator.getTriggerId());
        firedTriggerImpl.setFiredOn(firedTriggerCreator.getFiredOn());
        firedTriggerImpl.setStatus(firedTriggerCreator.getStatus());
        firedTriggerImpl.setMessage(firedTriggerCreator.getMessage());

        return ServiceDAO.create(em, firedTriggerImpl);
    }

    /**
     * Finds the firedTrigger by firedTrigger identifier
     *
     * @param em             The {@link EntityManager} that owns the transaction.
     * @param firedTriggerId The {@link FiredTrigger#getId()}.
     * @return The found {@link FiredTrigger} or {@code null}
     * @since 1.5.0
     */
    public static FiredTrigger find(EntityManager em, KapuaId firedTriggerId) {
        return ServiceDAO.find(em, FiredTriggerImpl.class, null, firedTriggerId);
    }

    /**
     * Finds the {@link FiredTrigger} by {@link FiredTrigger} identifier.
     *
     * @param scopeId        The {@link FiredTrigger#getScopeId()}.
     * @param firedTriggerId The {@link FiredTrigger#getId()}.
     * @return The found {@link FiredTrigger} or {@code null}.
     * @since 1.5.0
     */
    public static FiredTrigger find(EntityManager em, KapuaId scopeId, KapuaId firedTriggerId) {
        return ServiceDAO.find(em, FiredTriggerImpl.class, scopeId, firedTriggerId);
    }

    /**
     * Returns the firedTrigger list matching the provided query
     *
     * @param em                The {@link EntityManager} that owns the transaction.
     * @param firedTriggerQuery The {@link org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerQuery} to perform.
     * @return The {@link FiredTriggerListResult} that matches the given {@link org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerQuery}.
     * @throws KapuaException if error occurs while querying
     * @since 1.5.0
     */
    public static FiredTriggerListResult query(EntityManager em, KapuaQuery firedTriggerQuery)
            throws KapuaException {
        return ServiceDAO.query(em, FiredTrigger.class, FiredTriggerImpl.class, new FiredTriggerListResultImpl(), firedTriggerQuery);
    }

    /**
     * Returns the firedTrigger count matching the provided query
     *
     * @param em                The {@link EntityManager} that owns the transaction.
     * @param firedTriggerQuery The {@link org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerQuery} to perform.
     * @return The {@link FiredTriggerListResult} that matches the given {@link org.eclipse.kapua.service.scheduler.trigger.fired.FiredTriggerQuery}.
     * @throws KapuaException if error occurs while counting
     * @since 1.5.0
     */
    public static long count(EntityManager em, KapuaQuery firedTriggerQuery)
            throws KapuaException {
        return ServiceDAO.count(em, FiredTrigger.class, FiredTriggerImpl.class, firedTriggerQuery);
    }

    /**
     * Deletes the firedTrigger by firedTrigger identifier
     *
     * @param em             The {@link EntityManager} that owns the transaction.
     * @param scopeId        The {@link FiredTrigger#getScopeId()}.
     * @param firedTriggerId The {@link FiredTrigger#getId()}.
     * @return The deleted {@link FiredTrigger}
     * @throws KapuaEntityNotFoundException If the {@link FiredTrigger} is not found
     * @since 1.5.0
     */
    public static FiredTrigger delete(EntityManager em, KapuaId scopeId, KapuaId firedTriggerId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, FiredTriggerImpl.class, scopeId, firedTriggerId);
    }

}
