/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
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
 * 
 * @since 1.0
 * 
 */
public interface KapuaEntityService<E extends KapuaEntity, C extends KapuaEntityCreator<E>> extends KapuaService
{
    /**
     * Creates the entity using information provided via entitty creator
     * 
     * @param creator
     * @return
     * @throws KapuaException
     */
    public E create(C creator)
        throws KapuaException;

    /**
     * Find the entity identified by entity and scope identifiers
     * 
     * @param scopeId
     * @param entityId
     * @return
     * @throws KapuaException
     */
    public E find(KapuaId scopeId, KapuaId entityId)
        throws KapuaException;

    /**
     * Returns the entity list matching the provided query
     * 
     * @param query
     * @return
     * @throws KapuaException
     */
    public KapuaListResult<E> query(KapuaQuery<E> query)
        throws KapuaException;

    /**
     * Returns the entity count matching the provided query
     * 
     * @param query
     * @return
     * @throws KapuaException
     */
    public long count(KapuaQuery<E> query)
        throws KapuaException;

    /**
     * Delete the entity identified by the entity and scope identifiers
     * 
     * @param scopeId
     * @param entityId
     * @throws KapuaException
     */
    public void delete(KapuaId scopeId, KapuaId entityId)
        throws KapuaException;
}
