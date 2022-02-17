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
package org.eclipse.kapua.service;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaEntity;

/**
 * Common interface for all KapuaService that are managing identifiable entities.
 *
 * @param <E> - Class of the KapuaEntity being managed by this Service
 * @since 1.0
 */
public interface KapuaUpdatableEntityService<E extends KapuaEntity> extends KapuaService {

    /**
     * Update the provided entity
     *
     * @param entity
     * @return
     * @throws KapuaException
     */
    E update(E entity) throws KapuaException;
}
