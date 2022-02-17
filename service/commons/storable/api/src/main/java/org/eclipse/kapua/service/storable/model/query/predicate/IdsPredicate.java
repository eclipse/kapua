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
package org.eclipse.kapua.service.storable.model.query.predicate;

import org.eclipse.kapua.service.storable.model.id.StorableId;

import java.util.Collection;
import java.util.List;

/**
 * {@link StorableId} {@link StorablePredicate}.
 * <p>
 * It matches that {@link StorableId}s are matched.
 *
 * @since 1.0.0
 */
public interface IdsPredicate extends StorablePredicate {

    /**
     * Gets the identifier type
     *
     * @return The identifier type
     * @since 1.0.0
     */
    String getType();

    /**
     * Sets the identifier type
     *
     * @param type The identifier type
     * @return Itself, to chain method invocations.
     * @since 1.3.0
     */
    IdsPredicate setType(String type);

    /**
     * Gets the {@link StorableId}s {@link List}.
     *
     * @return The {@link StorableId}s {@link List}.
     * @since 1.0.0
     */
    List<StorableId> getIds();

    /**
     * Adds a {@link StorableId} to the {@link List}.
     *
     * @param storableId The {@link StorableId} to add.
     * @since 1.3.0
     */
    IdsPredicate addId(StorableId storableId);

    /**
     * Adds a {@link Collection} of {@link StorableId}s to the {@link List}.
     *
     * @param storableIds The {@link Collection} of {@link StorableId} to add.
     * @since 1.3.0
     */
    IdsPredicate addIds(List<StorableId> storableIds);

    /**
     * Sets the {@link StorableId}s
     *
     * @param storableIds The {@link StorableId}s
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    IdsPredicate setIds(List<StorableId> storableIds);
}
