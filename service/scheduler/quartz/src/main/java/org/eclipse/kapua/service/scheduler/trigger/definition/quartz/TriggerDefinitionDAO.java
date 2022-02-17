/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.scheduler.trigger.definition.quartz;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionCreator;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionListResult;

/**
 * {@link TriggerDefinition} DAO.
 *
 * @since 1.1.0
 */
public class TriggerDefinitionDAO {

    private TriggerDefinitionDAO() {
    }

    /**
     * Creates and return new TriggerDefinition
     *
     * @param em                       The {@link EntityManager} that owns the transaction.
     * @param triggerDefinitionCreator The {@link TriggerDefinitionCreator} to persist.
     * @return The newly created {@link TriggerDefinition}
     * @since 1.1.0
     */
    public static TriggerDefinition create(EntityManager em, TriggerDefinitionCreator triggerDefinitionCreator) {

        TriggerDefinitionImpl triggerDefinitionImpl = new TriggerDefinitionImpl(triggerDefinitionCreator.getScopeId());
        triggerDefinitionImpl.setName(triggerDefinitionCreator.getName());
        triggerDefinitionImpl.setDescription(triggerDefinitionCreator.getDescription());
        triggerDefinitionImpl.setProcessorName(triggerDefinitionCreator.getProcessorName());
        triggerDefinitionImpl.setTriggerProperties(triggerDefinitionCreator.getTriggerProperties());

        return ServiceDAO.create(em, triggerDefinitionImpl);
    }

    /**
     * Updates the provided triggerDefinition
     *
     * @param em                The {@link EntityManager} that owns the transaction.
     * @param triggerDefinition The {@link TriggerDefinition} to update.
     * @return The updated {@link TriggerDefinition}.
     * @throws KapuaException if error occurs while updating
     * @since 1.1.0
     */
    public static TriggerDefinition update(EntityManager em, TriggerDefinition triggerDefinition) throws KapuaException {
        //
        // Update triggerDefinition
        TriggerDefinitionImpl triggerDefinitionImpl = (TriggerDefinitionImpl) triggerDefinition;

        return ServiceDAO.update(em, TriggerDefinitionImpl.class, triggerDefinitionImpl);
    }

    /**
     * Finds the triggerDefinition by triggerDefinition identifier
     *
     * @param em                  The {@link EntityManager} that owns the transaction.
     * @param triggerDefinitionId The {@link TriggerDefinition#getId()}
     * @return The found {@link TriggerDefinition} or {@code null}
     * @since 1.1.0
     */
    public static TriggerDefinition find(EntityManager em, KapuaId triggerDefinitionId) {
        return ServiceDAO.find(em, TriggerDefinitionImpl.class, null, triggerDefinitionId);
    }

    /**
     * Finds the triggerDefinition by triggerDefinition identifier
     *
     * @param em                  The {@link EntityManager} that owns the transaction.
     * @param triggerDefinitionId The {@link TriggerDefinition#getScopeId()}
     * @param triggerDefinitionId The {@link TriggerDefinition#getId()}
     * @return The found {@link TriggerDefinition} or {@code null}
     * @since 1.1.0
     */
    public static TriggerDefinition find(EntityManager em, KapuaId scopeId, KapuaId triggerDefinitionId) {
        return ServiceDAO.find(em, TriggerDefinitionImpl.class, scopeId, triggerDefinitionId);
    }

    /**
     * @param em   The {@link EntityManager} that owns the transaction.
     * @param name The {@link TriggerDefinition#getName()}
     * @return The found {@link TriggerDefinition} or {@code null}
     * @since 1.1.0
     */
    public static TriggerDefinition findByName(EntityManager em, String name) {
        return ServiceDAO.findByField(em, TriggerDefinitionImpl.class, KapuaNamedEntityAttributes.NAME, name);
    }

    /**
     * Returns the triggerDefinition list matching the provided query
     *
     * @param em                     The {@link EntityManager} that owns the transaction.
     * @param triggerDefinitionQuery The {@link org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionQuery} to perform
     * @return The {@link TriggerDefinitionListResult} that matches the given {@link org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionQuery}
     * @throws KapuaException if any error occurs while querying.
     * @since 1.1.0
     */
    public static TriggerDefinitionListResult query(EntityManager em, KapuaQuery triggerDefinitionQuery)
            throws KapuaException {
        return ServiceDAO.query(em, TriggerDefinition.class, TriggerDefinitionImpl.class, new TriggerDefinitionListResultImpl(), triggerDefinitionQuery);
    }

    /**
     * Returns the triggerDefinition count matching the provided query
     *
     * @param em                     The {@link EntityManager} that owns the transaction.
     * @param triggerDefinitionQuery The {@link org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionQuery} to perform
     * @return The {@link TriggerDefinitionListResult} that matches the given {@link org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionQuery}
     * @throws KapuaException if any error occurs while counting.
     * @since 1.1.0
     */
    public static long count(EntityManager em, KapuaQuery triggerDefinitionQuery)
            throws KapuaException {
        return ServiceDAO.count(em, TriggerDefinition.class, TriggerDefinitionImpl.class, triggerDefinitionQuery);
    }

    /**
     * Deletes the triggerDefinition by triggerDefinition identifier
     *
     * @param em                  The {@link EntityManager} that owns the transaction.
     * @param scopeId             The {@link TriggerDefinition#getScopeId()}.
     * @param triggerDefinitionId The {@link TriggerDefinition#getId()}.
     * @return The deleted {@link TriggerDefinition}.
     * @throws KapuaEntityNotFoundException If the {@link TriggerDefinition} is not found
     * @since 1.1.0
     */
    public static TriggerDefinition delete(EntityManager em, KapuaId scopeId, KapuaId triggerDefinitionId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, TriggerDefinitionImpl.class, scopeId, triggerDefinitionId);
    }

}
