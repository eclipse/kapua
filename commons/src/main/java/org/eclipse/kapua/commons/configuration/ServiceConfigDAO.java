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
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Service configuration DAO
 *
 * @since 1.0
 * @deprecated since 2.0.0 - use {@link ServiceConfigRepository} instead
 */
@Deprecated
public class ServiceConfigDAO extends ServiceDAO {

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


}
