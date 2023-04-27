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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;

import java.util.Optional;

/**
 * This contract builds upon {@link KapuaUpdatableEntityRepository} (and in turn {@link KapuaEntityRepository},
 * adding functionalities specific to Kapua Entities that are implement the contract {@link KapuaNamedEntity} (as in: have a name and description fields)
 *
 * @param <E> The specific subclass of {@link KapuaEntity} handled by this repository
 * @param <L> The specific subclass of {@link KapuaListResult}&lt;E&gt; meant to hold list results for the kapua entity handled by this repo
 * @since 2.0.0
 */
public interface KapuaNamedEntityRepository<E extends KapuaNamedEntity, L extends KapuaListResult<E>>
        extends KapuaUpdatableEntityRepository<E, L> {
    /**
     * Finds a {@link KapuaNamedEntity} by {@link KapuaNamedEntity#getName()}.
     *
     * @param value The value of the {@link KapuaNamedEntity#getName()} to search.
     * @return The {@link KapuaNamedEntity} found, or {@code null} if not found.
     * @since 2.0.0
     */
    Optional<E> findByName(TxContext txContext, String value);

    /**
     * Finds a {@link KapuaNamedEntity} by {@link KapuaNamedEntity#getName()}.
     *
     * @param scopeId The {@link KapuaNamedEntity#getScopeId()} in which to look for results.
     * @param value   The value of the field from which to search.
     * @return The {@link KapuaNamedEntity} found, or {@code null} if not found.
     * @since 2.0.0
     */
    Optional<E> findByName(TxContext txContext, KapuaId scopeId, String value);

    long countEntitiesWithName(TxContext tx, String name) throws KapuaException;

    long countEntitiesWithNameInScope(TxContext tx, KapuaId scopeId, String name) throws KapuaException;

    long countOtherEntitiesWithNameInScope(TxContext tx, KapuaId scopeId, KapuaId excludedId, String name) throws KapuaException;

}
