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
package org.eclipse.kapua.service.tag.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagQuery;

public interface TagRepository {
    /**
     * Creates and returns new {@link Tag}
     *
     * @param creator The {@link TagCreator} object from which create the new {@link Tag}.
     * @return The newly created {@link Tag}.
     * @throws KapuaException On create error.
     * @since 1.0.0
     */
    Tag create(TagCreator creator) throws KapuaException;

    Tag create(Tag tag) throws KapuaException;

    /**
     * Updates and returns the updated {@link Tag}
     *
     * @param tag The {@link Tag} to update
     * @return The updated {@link Tag}.
     * @throws KapuaEntityNotFoundException If {@link Tag} is not found.
     */
    Tag update(Tag tag) throws KapuaException;

    /**
     * Finds the {@link Tag} by {@link Tag} identifier
     *
     * @param scopeId
     * @param tagId   The {@link Tag} id to search.
     * @return The found {@link Tag} or {@code null} if not found.
     * @since 1.0.0
     */
    Tag find(KapuaId scopeId, KapuaId tagId) throws KapuaException;

    /**
     * Returns the {@link Tag} list matching the provided query.
     *
     * @param tagQuery The {@link TagQuery} used to filter results.
     * @return The list of {@link Tag}s that matches the given query.
     * @throws KapuaException On query error.
     * @since 1.0.0
     */
    TagListResult query(KapuaQuery tagQuery) throws KapuaException;

    /**
     * Return the {@link Tag} count matching the provided query
     *
     * @param tagQuery The {@link TagQuery} used to filter results
     * @return The count of {@link Tag}s that matches the given query.
     * @throws KapuaException
     * @since 1.0.0
     */
    long count(KapuaQuery tagQuery) throws KapuaException;

    /**
     * Deletes the {@link Tag} by {@link Tag} identifier
     *
     * @param scopeId
     * @param tagId   The {@link Tag} id to delete.
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If {@link Tag} is not found.
     * @since 1.0.0
     */
    Tag delete(KapuaId scopeId, KapuaId tagId) throws KapuaException;
}
