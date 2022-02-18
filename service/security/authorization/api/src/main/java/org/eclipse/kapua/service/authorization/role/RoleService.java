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
package org.eclipse.kapua.service.authorization.role;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

/**
 * {@link Role} service definition.
 *
 * @since 1.0.0
 */
public interface RoleService extends KapuaEntityService<Role, RoleCreator>,
        KapuaUpdatableEntityService<Role>,
        KapuaConfigurableService {

    /**
     * Creates a new {@link Role} based on the parameters provided in the {@link RoleCreator}.<br>
     * {@link Role} must have a unique name within the scope.
     *
     * @param roleCreator The creator object from which to create the {@link Role}.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    Role create(RoleCreator roleCreator) throws KapuaException;

    /**
     * Updates the {@link Role} according the given updated entity.<br>
     * The {@link Role#getName()} can be updated, but must remain unique within the scope.<br>
     *
     * @param role The updated {@link Role}.
     * @return A {@link Role} with updated values.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    Role update(Role role) throws KapuaException;

    /**
     * Finds the {@link Role} by scope identifier and {@link Role} id.
     *
     * @param scopeId The scope id in which to search.
     * @param roleId  The {@link Role} id to search.
     * @return The {@link Role} found or {@code null} if no entity was found.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    Role find(KapuaId scopeId, KapuaId roleId) throws KapuaException;

    /**
     * Returns the {@link RoleListResult} with elements matching the provided query.
     *
     * @param query The {@link RoleQuery} used to filter results.
     * @return The {@link RoleListResult} with elements matching the query parameter.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    RoleListResult query(KapuaQuery query) throws KapuaException;

    /**
     * Returns the count of the {@link Role} elements matching the provided query.
     *
     * @param query The {@link RoleQuery} used to filter results.
     * @return The count of the {@link Role} elements matching the provided query.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    long count(KapuaQuery query) throws KapuaException;

    /**
     * Delete the {@link Role} by scope id and {@link Role} id.
     *
     * @param scopeId The scope id in which to delete.
     * @param roleId  The {@link Role} id to delete.
     * @throws KapuaException
     * @since 1.0.0
     */
    @Override
    void delete(KapuaId scopeId, KapuaId roleId) throws KapuaException;

}
