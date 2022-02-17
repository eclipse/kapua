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
package org.eclipse.kapua.service.tag;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * {@link Tag} service definition.
 *
 * @since 1.0.0
 */
public interface TagService extends KapuaEntityService<Tag, TagCreator>,
        KapuaUpdatableEntityService<Tag>,
        KapuaConfigurableService {

    /**
     * Creates a new {@link Tag} based on the parameters provided in the {@link TagCreator}.<br>
     * {@link Tag} must have a unique name within the scope.
     *
     * @param tagCreator The creator object from which to create the {@link Tag}.
     * @return The created {@link Tag}
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    Tag create(TagCreator tagCreator) throws KapuaException;

    /**
     * Updates the {@link Tag} according the given updated entity.<br>
     * The {@link Tag#getName()} can be updated, but must remain unique within the scope.
     *
     * @param tag The updated {@link Tag}.
     * @return A {@link Tag} with updated values.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    Tag update(Tag tag) throws KapuaException;

    /**
     * Finds the {@link Tag} by scope identifier and {@link Tag} id.
     *
     * @param scopeId The scope id in which to search.
     * @param tagId   The {@link Tag} id to search.
     * @return The {@link Tag} found or {@code null} if no entity was found.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    Tag find(KapuaId scopeId, KapuaId tagId) throws KapuaException;

    /**
     * Returns the {@link TagListResult} with elements matching the provided query.
     *
     * @param query The {@link TagQuery} used to filter results.
     * @return The {@link TagListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    TagListResult query(KapuaQuery query) throws KapuaException;

    /**
     * Returns the count of the {@link Tag} elements matching the provided query.
     *
     * @param query The {@link TagQuery} used to filter results.
     * @return The count of the {@link Tag} elements matching the provided query.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    long count(KapuaQuery query) throws KapuaException;

    /**
     * Delete the {@link Tag} by scope id and {@link Tag} id.
     *
     * @param scopeId The scope id in which to delete.
     * @param tagId   The {@link Tag} id to delete.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    void delete(KapuaId scopeId, KapuaId tagId) throws KapuaException;

}
