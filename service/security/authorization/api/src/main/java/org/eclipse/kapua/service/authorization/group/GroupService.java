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
package org.eclipse.kapua.service.authorization.group;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * {@link Group} service definition.
 *
 * @since 1.0.0
 */
public interface GroupService extends KapuaEntityService<Group, GroupCreator>,
        KapuaUpdatableEntityService<Group>,
        KapuaConfigurableService {

    /**
     * Creates a new {@link Group} based on the parameters provided in the {@link GroupCreator}.<br>
     * {@link Group} must have a unique name within the scope.
     *
     * @param groupCreator The creator object from which to create the {@link Group}.
     * @return The created {@link Group}
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    Group create(GroupCreator groupCreator) throws KapuaException;

    /**
     * Updates the {@link Group} according the given updated entity.<br>
     * The {@link Group#getName()} can be updated, but must remain unique within the scope.
     *
     * @param group The updated {@link Group}.
     * @return A {@link Group} with updated values.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    Group update(Group group) throws KapuaException;

    /**
     * Finds the {@link Group} by scope identifier and {@link Group} id.
     *
     * @param scopeId The scope id in which to search.
     * @param groupId The {@link Group} id to search.
     * @return The {@link Group} found or {@code null} if no entity was found.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    Group find(KapuaId scopeId, KapuaId groupId) throws KapuaException;

    /**
     * Returns the {@link GroupListResult} with elements matching the provided query.
     *
     * @param query The {@link GroupQuery} used to filter results.
     * @return The {@link GroupListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    GroupListResult query(KapuaQuery query) throws KapuaException;

    /**
     * Returns the count of the {@link Group} elements matching the provided query.
     *
     * @param query The {@link GroupQuery} used to filter results.
     * @return The count of the {@link Group} elements matching the provided query.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    long count(KapuaQuery query) throws KapuaException;

    /**
     * Delete the {@link Group} by scope id and {@link Group} id.
     *
     * @param scopeId The scope id in which to delete.
     * @param groupId The {@link Group} id to delete.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    void delete(KapuaId scopeId, KapuaId groupId) throws KapuaException;

}
