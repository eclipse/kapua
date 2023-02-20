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
package org.eclipse.kapua.repository;

import org.eclipse.kapua.KapuaEntityExistsException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;

import javax.validation.constraints.Null;

/**
 * {@link KapuaEntityServiceRepository} utility methods.
 *
 * @since 1.0.0
 */
public interface KapuaEntityServiceRepository<E extends KapuaEntity, L extends KapuaListResult<E>> {

    /**
     * Persists the {@link KapuaEntity}.
     * <p>
     * This method checks for the constraint violation and, in this case, it throws a specific exception ({@link KapuaEntityExistsException}).
     *
     * @param entity The {@link KapuaEntity} to be persisted.
     * @return The persisted {@link KapuaEntity}.
     * @since 2.0.0
     */
    E create(E entity);

    /**
     * Finds a {@link KapuaEntity}.
     *
     * @param scopeId  The {@link KapuaEntity#getScopeId()} the entity to be found.
     * @param entityId The {@link KapuaEntity#getId()} of the entity to be found.
     * @since 2.0.0
     */
    E find(@Null KapuaId scopeId, KapuaId entityId);

    /**
     * Queries the {@link KapuaEntity}es.
     *
     * @param kapuaQuery The {@link KapuaQuery} to perform.
     * @return The reference of the {@code resultContainer} parameter. Results are added to the given {@code resultContainer} parameter.
     * @throws KapuaException If filter predicates in the {@link KapuaQuery} are incorrect.
     * @since 2.0.0
     */
    L query(KapuaQuery kapuaQuery);

    /**
     * Counts the {@link KapuaEntity}es.
     *
     * @param kapuaQuery The {@link KapuaQuery} to perform.
     * @return The number of {@link KapuaEntity}es that matched the filter predicates.
     * @throws KapuaException If filter predicates in the {@link KapuaQuery} are incorrect.
     * @since 2.0.0
     */
    long count(KapuaQuery kapuaQuery);

    /**
     * Deletes a {@link KapuaEntity}.
     *
     * @param scopeId  The {@link KapuaEntity#getScopeId()} of the entity to be deleted.
     * @param entityId The {@link KapuaEntity#getId()} of the entity to be deleted.
     * @return The deleted {@link KapuaEntity}.
     * @throws KapuaEntityNotFoundException If the {@link KapuaEntity} does not exists.
     * @since 1.0.0
     */
    E delete(KapuaId scopeId, KapuaId entityId);
}
