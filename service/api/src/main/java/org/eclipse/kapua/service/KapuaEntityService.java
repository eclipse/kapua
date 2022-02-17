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
import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;

/**
 * Common interface for all KapuaService that are managing identifiable entities.
 *
 * @param <E> - Class of the KapuaEntity being managed by this Service
 * @param <C> - Creator Class of the KapuaEntity being managed by this Service
 * @since 1.0
 */
public interface KapuaEntityService<E extends KapuaEntity, C extends KapuaEntityCreator<E>> extends KapuaService {

    /**
     * Creates the entity using information provided via entity creator
     *
     * @param creator
     * @return
     * @throws KapuaException
     */
    E create(C creator) throws KapuaException;

    /**
     * Find the entity identified by entity and scope identifiers
     *
     * @param scopeId
     * @param entityId
     * @return
     * @throws KapuaException
     */
    E find(KapuaId scopeId, KapuaId entityId) throws KapuaException;

    /**
     * Returns the entity list matching the provided query
     *
     * @param query
     * @return
     * @throws KapuaException
     */
    KapuaListResult<E> query(KapuaQuery query) throws KapuaException;

    /**
     * Returns the entity count matching the provided query
     *
     * @param query
     * @return
     * @throws KapuaException
     */
    long count(KapuaQuery query) throws KapuaException;

    /**
     * Delete the entity identified by the entity and scope identifiers
     *
     * @param scopeId
     * @param entityId
     * @throws KapuaException
     */
    void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException;
}
