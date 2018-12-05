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
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;

/**
 * Service configuration DAO
 * 
 * @since 1.0
 *
 */
public class ServiceConfigDAO extends ServiceDAO {

    /**
     * Create and return new service configuration
     * 
     * @param em
     * @param serviceConfigCreator
     * @return
     * @throws KapuaException
     */
    public static ServiceConfigImpl create(EntityManager em, ServiceConfigCreatorImpl serviceConfigCreator)
            throws KapuaException {
        //
        // Create service configuration
        ServiceConfigImpl serviceConfigImpl = new ServiceConfigImpl(serviceConfigCreator.getScopeId());

        serviceConfigImpl.setPid(serviceConfigCreator.getPid());
        serviceConfigImpl.setConfigurations(serviceConfigCreator.getConfigurations());

        return ServiceDAO.create(em, serviceConfigImpl);
    }

    /**
     * Update the provided service configuration
     * 
     * @param em
     * @param serviceConfig
     * @return
     * @throws KapuaException
     */
    public static ServiceConfig update(EntityManager em, ServiceConfig serviceConfig)
            throws KapuaException {
        //
        // Update service configuration
        ServiceConfigImpl serviceConfigImpl = (ServiceConfigImpl) serviceConfig;

        return ServiceDAO.update(em, ServiceConfigImpl.class, serviceConfigImpl);
    }

    /**
     * Find the service configuration by user identifier
     *
     * @param em
     * @param scopeId
     * @param userId
     * @return
     */
    public static ServiceConfig find(EntityManager em, KapuaId scopeId, KapuaId userId) {
        return ServiceDAO.find(em, ServiceConfigImpl.class, scopeId, userId);
    }

    /**
     * Find the service configuration by service name
     *
     * @param em
     * @param name
     * @return
     */
    public static ServiceConfig findByName(EntityManager em, String name) {
        return ServiceDAO.findByField(em, ServiceConfigImpl.class, "name", name);
    }

    /**
     * Return the service configuration list matching the provided query
     *
     * @param em
     * @param serviceConfigQuery
     * @return
     * @throws KapuaException
     */
    public static ServiceConfigListResult query(EntityManager em, KapuaQuery<ServiceConfig> serviceConfigQuery)
            throws KapuaException {
        return ServiceDAO.query(em, ServiceConfig.class, ServiceConfigImpl.class, new ServiceConfigListResultImpl(), serviceConfigQuery);
    }

    /**
     * Return the service configuration count matching the provided query
     *
     * @param em
     * @param serviceConfigQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery<ServiceConfig> serviceConfigQuery)
            throws KapuaException {
        return ServiceDAO.count(em, ServiceConfig.class, ServiceConfigImpl.class, serviceConfigQuery);
    }

    /**
     * Delete the service configuration by user identifier
     *
     * @param em
     * @param scopeId
     * @param userId
     * @throws KapuaEntityNotFoundException
     *             If {@link ServiceConfig} is not found.
     */
    public static void delete(EntityManager em, KapuaId scopeId, KapuaId userId) throws KapuaEntityNotFoundException {
        ServiceDAO.delete(em, ServiceConfigImpl.class, scopeId, userId);
    }

}
