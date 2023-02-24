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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;

public interface KapuaUpdatableEntityRepository<E extends KapuaUpdatableEntity> extends KapuaEntityRepository<E> {
    /**
     * Updates the {@link KapuaUpdatableEntity}.
     *
     * @param entity The {@link KapuaUpdatableEntity} to be updated.
     * @return The updated {@link KapuaUpdatableEntity}.
     * @throws KapuaEntityNotFoundException If the {@link KapuaEntity} does not exist.
     * @since 2.0.0
     */
    E update(E entity) throws KapuaException;
}
