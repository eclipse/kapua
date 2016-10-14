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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;

public class ServiceConfigDAO extends ServiceDAO
{
    public static ServiceConfigImpl create(EntityManager em, ServiceConfigCreatorImpl serviceConfigCreator)
            throws KapuaException
        {
            //
            // Create service configuration
            ServiceConfigImpl serviceConfigImpl = new ServiceConfigImpl(serviceConfigCreator.getScopeId());

            serviceConfigImpl.setPid(serviceConfigCreator.getPid());
            serviceConfigImpl.setConfigurations(serviceConfigCreator.getConfigurations());

            return ServiceDAO.create(em, serviceConfigImpl);
        }

        public static ServiceConfig update(EntityManager em, ServiceConfig serviceConfig)
            throws KapuaException
        {
            //
            // Update service configuration
            ServiceConfigImpl serviceConfigImpl = (ServiceConfigImpl) serviceConfig;

            return ServiceDAO.update(em, ServiceConfigImpl.class, serviceConfigImpl);
        }

        public static void delete(EntityManager em, KapuaId userId)
        {
            ServiceDAO.delete(em, ServiceConfigImpl.class, userId);
        }

        public static ServiceConfig find(EntityManager em, KapuaId userId)
        {
            return em.find(ServiceConfigImpl.class, userId);
        }

        public static ServiceConfig findByName(EntityManager em, String name)
        {
            return ServiceDAO.findByName(em, ServiceConfigImpl.class, name);
        }

        public static ServiceConfigListResult query(EntityManager em, KapuaQuery<ServiceConfig> serviceConfigQuery)
            throws KapuaException
        {
            return ServiceDAO.query(em, ServiceConfig.class, ServiceConfigImpl.class, new ServiceConfigListResultImpl(), serviceConfigQuery);
        }

        public static long count(EntityManager em, KapuaQuery<ServiceConfig> serviceConfigQuery)
            throws KapuaException
        {
            return ServiceDAO.count(em, ServiceConfig.class, ServiceConfigImpl.class, serviceConfigQuery);
        }

}
