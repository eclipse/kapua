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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.query.KapuaListResult;

/**
 * This contract builds upon {@link KapuaEntityRepository}, adding update functionalities for Kapua Entities that are capable of being updated ({@link KapuaUpdatableEntity})
 *
 * @param <E> The specific subclass of {@link KapuaEntity} handled by this repository
 * @param <L> The specific subclass of {@link KapuaListResult}&lt;E&gt; meant to hold list results for the kapua entity handled by this repo
 * @since 2.0.0
 */
public interface KapuaUpdatableEntityRepository<E extends KapuaUpdatableEntity, L extends KapuaListResult<E>>
        extends KapuaEntityRepository<E, L> {
    /**
     * Updates the {@link KapuaUpdatableEntity}.
     *
     * @param entity The {@link KapuaUpdatableEntity} to be updated.
     * @return The updated {@link KapuaUpdatableEntity}.
     * @throws KapuaEntityNotFoundException If the {@link KapuaEntity} does not exist.
     * @since 2.0.0
     */
    E update(TxContext txContext, E entity) throws KapuaException;

    E update(TxContext txContext, E currentEntity, E updatedEntity);
}
