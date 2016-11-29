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
package org.eclipse.kapua.service.authorization.access;

import java.util.Set;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;

/**
 * Access info service definition.
 * 
 * @since 1.0
 *
 */
public interface AccessInfoService extends KapuaEntityService<AccessInfo, AccessInfoCreator> {

    /**
     * Create a new access info
     * 
     * @param accessInfoCreator
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    public AccessInfo create(AccessInfoCreator accessInfoCreator)
            throws KapuaException;

    /**
     * Find the access info by scope identifier and access info identifier
     * 
     * @param scopeId
     * @param accessInfoId
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    public AccessInfo find(KapuaId scopeId, KapuaId accessInfoId)
            throws KapuaException;

    /**
     * Return the access info list matching the provided query
     * 
     * @param query
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    public AccessInfoListResult query(KapuaQuery<AccessInfo> query)
            throws KapuaException;

    /**
     * Return the count of the access info matching the provided query
     * 
     * @param query
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    public long count(KapuaQuery<AccessInfo> query)
            throws KapuaException;

    /**
     * Delete the access info by scope identifier and entity identifier
     * 
     * @param scopeId
     * @param accessInfoId
     * @throws KapuaException
     * @since 1.0.0
     */
    public void delete(KapuaId scopeId, KapuaId accessInfoId)
            throws KapuaException;

    /**
     * Merge the new permissions list with the already persisted permissions.<br>
     * In other word the newPermission list will replace all the roles for the user in the database.
     * 
     * @deprecated
     * @param newPermissions
     * @return
     * @throws KapuaException
     */
    @Deprecated
    public AccessInfoListResult merge(Set<AccessInfoCreator> newPermissions)
            throws KapuaException;
}
