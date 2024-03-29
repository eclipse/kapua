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
package org.eclipse.kapua.storage;

import org.eclipse.kapua.KapuaEntityExistsException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;

import javax.validation.constraints.Null;
import java.util.Optional;

/**
 * Base contract to persists (Create, Read, Delete) {@link KapuaEntity}s
 *
 * @param <E> The specific subclass of {@link KapuaEntity} handled by this repository
 * @param <L> The specific subclass of {@link KapuaListResult}&lt;E&gt; meant to hold list results for the kapua entity handled by this repo
 * @since 2.0.0
 */
public interface KapuaEntityRepository<E extends KapuaEntity, L extends KapuaListResult<E>> {

    /**
     * Persists the {@link KapuaEntity}.
     * <p>
     * This method checks for the constraint violation and, in this case, it throws a specific exception ({@link KapuaEntityExistsException}).
     *
     * @param entity The {@link KapuaEntity} to be persisted.
     * @return The persisted {@link KapuaEntity}.
     * @since 2.0.0
     */
    E create(TxContext txContext, E entity) throws KapuaException;

    /**
     * Finds a {@link KapuaEntity}.
     *
     * @param scopeId  The {@link KapuaEntity#getScopeId()} the entity to be found.
     * @param entityId The {@link KapuaEntity#getId()} of the entity to be found.
     * @since 2.0.0
     */
    Optional<E> find(TxContext txContext, @Null KapuaId scopeId, KapuaId entityId);

    /**
     * Queries the {@link KapuaEntity}es.
     *
     * @param kapuaQuery The {@link KapuaQuery} to perform.
     * @return The reference of the {@code resultContainer} parameter. Results are added to the given {@code resultContainer} parameter.
     * @since 2.0.0
     */
    L query(TxContext txContext, KapuaQuery kapuaQuery) throws KapuaException;

    /**
     * Counts the number of {@link KapuaEntity} according to the provided query.
     *
     * @param kapuaQuery The {@link KapuaQuery} to perform.
     * @return The number of {@link KapuaEntity}es that matched the filter predicates.
     * @since 2.0.0
     */
    long count(TxContext txContext, KapuaQuery kapuaQuery) throws KapuaException;

    /**
     * Finds and deletes, if present, a {@link KapuaEntity}.
     *
     * @param scopeId  The {@link KapuaEntity#getScopeId()} of the entity to be deleted.
     * @param entityId The {@link KapuaEntity#getId()} of the entity to be deleted.
     * @return The deleted {@link KapuaEntity}.
     * @since 1.0.0
     */
    E delete(TxContext txContext, KapuaId scopeId, KapuaId entityId) throws KapuaException;

    /**
     * Deletes a {@link KapuaEntity}.
     *
     * @param entityToDelete The {@link KapuaEntity} to be deleted.
     * @return The deleted {@link KapuaEntity}.
     * @since 1.0.0
     */
    E delete(TxContext txContext, E entityToDelete);
}
