/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.trigger.quartz;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;

/**
 * {@link Trigger} DAO.
 *
 * @since 1.0.0
 */
public class TriggerDAO {

    private TriggerDAO() {
    }

    /**
     * Creates and return new {@link Trigger}
     *
     * @param em             The {@link EntityManager} that owns the transaction.
     * @param triggerCreator The {@link TriggerCreator} to persist.
     * @return The newly created {@link Trigger}.
     * @since 1.0.0
     */
    public static Trigger create(EntityManager em, TriggerCreator triggerCreator) {
        TriggerImpl triggerImpl = new TriggerImpl(triggerCreator.getScopeId());
        triggerImpl.setName(triggerCreator.getName());
        triggerImpl.setStartsOn(triggerCreator.getStartsOn());
        triggerImpl.setEndsOn(triggerCreator.getEndsOn());
        triggerImpl.setTriggerDefinitionId(triggerCreator.getTriggerDefinitionId());
        triggerImpl.setTriggerProperties(triggerCreator.getTriggerProperties());

        return ServiceDAO.create(em, triggerImpl);
    }

    /**
     * Updates the provided {@link Trigger}
     *
     * @param em      The {@link EntityManager} that owns the transaction.
     * @param trigger The {@link Trigger} to update.
     * @return The updated {@link Trigger}.
     * @throws KapuaException if error occurs while updating.
     * @since 1.0.0
     */
    public static Trigger update(EntityManager em, Trigger trigger) throws KapuaException {
        //
        // Update trigger
        TriggerImpl triggerImpl = (TriggerImpl) trigger;

        return ServiceDAO.update(em, TriggerImpl.class, triggerImpl);
    }

    /**
     * Finds the trigger by trigger identifier
     *
     * @param em        The {@link EntityManager} that owns the transaction
     * @param scopeId   The {@link Trigger#getScopeId()}.
     * @param triggerId The {@link Trigger#getId()}.
     * @return The found {@link Trigger} or {@code null}.
     * @since 1.0.0
     */
    public static Trigger find(EntityManager em, KapuaId scopeId, KapuaId triggerId) {
        return ServiceDAO.find(em, TriggerImpl.class, scopeId, triggerId);
    }

    /**
     * Returns the trigger list matching the provided query
     *
     * @param em           The {@link EntityManager} that owns the transaction.
     * @param triggerQuery The {@link org.eclipse.kapua.service.scheduler.trigger.TriggerQuery} to perform
     * @return The {@link TriggerListResult} matching the given query.
     * @throws KapuaException if error occurs while quering.
     * @since 1.0.0
     */
    public static TriggerListResult query(EntityManager em, KapuaQuery triggerQuery)
            throws KapuaException {
        return ServiceDAO.query(em, Trigger.class, TriggerImpl.class, new TriggerListResultImpl(), triggerQuery);
    }

    /**
     * Returns the trigger count matching the provided query
     *
     * @param em           The {@link EntityManager} that owns the transaction.
     * @param triggerQuery The {@link org.eclipse.kapua.service.scheduler.trigger.TriggerQuery} to perform
     * @return The {@link TriggerListResult} matching the given query.
     * @throws KapuaException if error occurs while counting.
     * @since 1.0.0
     */
    public static long count(EntityManager em, KapuaQuery triggerQuery)
            throws KapuaException {
        return ServiceDAO.count(em, Trigger.class, TriggerImpl.class, triggerQuery);
    }

    /**
     * Deletes the trigger by trigger identifier
     *
     * @param em        The {@link EntityManager} that owns the transaction.
     * @param scopeId   The {@link Trigger#getScopeId()}.
     * @param triggerId The {@link Trigger#getId()}
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If the {@link Trigger} is not found
     * @since 1.0.0
     */
    public static Trigger delete(EntityManager em, KapuaId scopeId, KapuaId triggerId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, TriggerImpl.class, scopeId, triggerId);
    }

}
