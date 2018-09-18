/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.endpoint.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.endpoint.EndpointInfo;
import org.eclipse.kapua.service.endpoint.EndpointInfoCreator;
import org.eclipse.kapua.service.endpoint.EndpointInfoListResult;
import org.eclipse.kapua.service.endpoint.EndpointInfoQuery;

/**
 * {@link EndpointInfo} DAO
 *
 * @since 1.0.0
 */
public class EndpointInfoDAO extends ServiceDAO {

    /**
     * Creates and returns new {@link EndpointInfo}
     *
     * @param em      The {@link EntityManager} that holds the transaction.
     * @param creator The {@link EndpointInfoCreator} object from which create the new {@link EndpointInfo}.
     * @return The newly created {@link EndpointInfo}.
     * @throws KapuaException On create error.
     * @since 1.0.0
     */
    public static EndpointInfo create(EntityManager em, EndpointInfoCreator creator) throws KapuaException {

        EndpointInfo endpointInfo = new EndpointInfoImpl(creator.getScopeId());
        endpointInfo.setSchema(creator.getSchema());
        endpointInfo.setDns(creator.getDns());
        endpointInfo.setPort(creator.getPort());
        endpointInfo.setSecure(creator.getSecure());
        endpointInfo.setUsages(creator.getUsages());

        return ServiceDAO.create(em, endpointInfo);
    }

    /**
     * Updates and returns the updated {@link EndpointInfo}
     *
     * @param em           The {@link EntityManager} that holds the transaction.
     * @param endpointInfo The {@link EndpointInfo} to update
     * @return The updated {@link EndpointInfo}.
     * @throws KapuaEntityNotFoundException If {@link EndpointInfo} is not found.
     */
    public static EndpointInfo update(EntityManager em, EndpointInfo endpointInfo) throws KapuaEntityNotFoundException {
        EndpointInfoImpl endpointInfoImpl = (EndpointInfoImpl) endpointInfo;
        return ServiceDAO.update(em, EndpointInfoImpl.class, endpointInfoImpl);
    }

    /**
     * Finds the {@link EndpointInfo} by {@link EndpointInfo} identifier
     *
     * @param em             The {@link EntityManager} that holds the transaction.
     * @param scopeId
     * @param endpointInfoId The {@link EndpointInfo} id to search.
     * @return The found {@link EndpointInfo} or {@code null} if not found.
     * @since 1.0.0
     */
    public static EndpointInfo find(EntityManager em, KapuaId scopeId, KapuaId endpointInfoId) {
        return ServiceDAO.find(em, EndpointInfoImpl.class, scopeId, endpointInfoId);
    }

    /**
     * Returns the {@link EndpointInfo} list matching the provided query.
     *
     * @param em                The {@link EntityManager} that holds the transaction.
     * @param endpointInfoQuery The {@link EndpointInfoQuery} used to filter results.
     * @return The list of {@link EndpointInfo}s that matches the given query.
     * @throws KapuaException On query error.
     * @since 1.0.0
     */
    public static EndpointInfoListResult query(EntityManager em, KapuaQuery<EndpointInfo> endpointInfoQuery)
            throws KapuaException {
        return ServiceDAO.query(em, EndpointInfo.class, EndpointInfoImpl.class, new EndpointInfoListResultImpl(), endpointInfoQuery);
    }

    /**
     * Return the {@link EndpointInfo} count matching the provided query
     *
     * @param em                The {@link EntityManager} that holds the transaction.
     * @param endpointInfoQuery The {@link EndpointInfoQuery} used to filter results
     * @return The count of {@link EndpointInfo}s that matches the given query.
     * @throws KapuaException
     * @since 1.0.0
     */
    public static long count(EntityManager em, KapuaQuery<EndpointInfo> endpointInfoQuery)
            throws KapuaException {
        return ServiceDAO.count(em, EndpointInfo.class, EndpointInfoImpl.class, endpointInfoQuery);
    }

    /**
     * Deletes the {@link EndpointInfo} by {@link EndpointInfo} identifier
     *
     * @param em             The {@link EntityManager} that holds the transaction.
     * @param scopeId
     * @param endpointInfoId The {@link EndpointInfo} id to delete.
     * @throws KapuaEntityNotFoundException If {@link EndpointInfo} is not found.
     * @since 1.0.0
     */
    public static void delete(EntityManager em, KapuaId scopeId, KapuaId endpointInfoId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, EndpointInfoImpl.class, scopeId, endpointInfoId);
    }
}
