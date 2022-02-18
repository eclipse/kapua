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
package org.eclipse.kapua.service.storable;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.storable.model.Storable;
import org.eclipse.kapua.service.storable.model.StorableListResult;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.eclipse.kapua.service.storable.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.storable.model.query.StorableQuery;

/**
 * {@link StorableService} definition.
 * <p>
 * It is the base {@code interface} for all services that are {@link StorableService}s
 *
 * @since 5.7.0
 */
public interface StorableService<S extends Storable, L extends StorableListResult<S>, Q extends StorableQuery> {

    /**
     * Gets a {@link Storable} by the scope {@link KapuaId} and its {@link StorableId} fetching all fields.
     * <p>
     * This means that invoking {@link #find(KapuaId, StorableId, StorableFetchStyle)} with {@link StorableFetchStyle#SOURCE_FULL} produces the same result.
     *
     * @param scopeId The scope {@link KapuaId}
     * @param id      The {@link StorableId}
     * @return The found {@link Storable}, or {@code null}
     * @throws KapuaException If something goes wrong.
     * @since 1.0.0
     */
    S find(KapuaId scopeId, StorableId id) throws KapuaException;

    /**
     * Gets a {@link Storable} by the scope {@link KapuaId} and its {@link StorableId} fetching fields according to the given {@link StorableFetchStyle}.
     *
     * @param scopeId    The scope {@link KapuaId}
     * @param id         The {@link StorableId}
     * @param fetchStyle The {@link StorableFetchStyle}.
     * @return The found {@link Storable}, or {@code null}
     * @throws KapuaException If something goes wrong.
     * @since 1.0.0
     */
    S find(KapuaId scopeId, StorableId id, StorableFetchStyle fetchStyle) throws KapuaException;

    /**
     * Queries the {@link Storable} according to the {@link StorableQuery}.
     *
     * @param query The {@link StorableQuery} to filter {@link Storable}s
     * @return The {@link StorableListResult} containing the filtered {@link Storable}s.
     * @throws KapuaException If something goes wrong.
     * @since 1.0.0
     */
    L query(Q query) throws KapuaException;

    /**
     * Counts the {@link Storable} according to the {@link StorableQuery}.
     *
     * @param query The {@link StorableQuery} to filter {@link Storable}s
     * @return The count of the filtered {@link Storable}s.
     * @throws KapuaException If something goes wrong.
     * @since 1.0.0
     */
    long count(Q query) throws KapuaException;
}
