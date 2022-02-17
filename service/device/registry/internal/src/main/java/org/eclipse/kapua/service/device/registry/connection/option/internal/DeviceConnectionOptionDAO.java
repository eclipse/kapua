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
package org.eclipse.kapua.service.device.registry.connection.option.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionListResult;

/**
 * Device connection options DAO
 *
 * @since 1.0
 */
public class DeviceConnectionOptionDAO extends ServiceDAO {

    /**
     * Update the provided device connection options
     *
     * @param em
     * @param deviceConnectionOptions
     * @return
     * @throws KapuaEntityNotFoundException If the {@link DeviceConnectionOption} is not found.
     */
    public static DeviceConnectionOption update(EntityManager em, DeviceConnectionOption deviceConnectionOptions)
            throws KapuaException {
        DeviceConnectionOptionImpl deviceConnectionOpptionsImpl = (DeviceConnectionOptionImpl) deviceConnectionOptions;
        return ServiceDAO.update(em, DeviceConnectionOptionImpl.class, deviceConnectionOpptionsImpl);
    }

    /**
     * Find the device connection by device connection identifier
     *
     * @param em
     * @param scopeId
     * @param deviceConnectionOptionsId
     * @return
     */
    public static DeviceConnectionOption find(EntityManager em, KapuaId scopeId, KapuaId deviceConnectionOptionsId) {
        return ServiceDAO.find(em, DeviceConnectionOptionImpl.class, scopeId, deviceConnectionOptionsId);
    }

    /**
     * Return the device connection option list matching the provided query
     *
     * @param em
     * @param query
     * @return
     * @throws KapuaException
     */
    public static DeviceConnectionOptionListResult query(EntityManager em, KapuaQuery query)
            throws KapuaException {
        return ServiceDAO.query(em, DeviceConnectionOption.class, DeviceConnectionOptionImpl.class, new DeviceConnectionOptionListResultImpl(), query);
    }

    /**
     * Return the device connection options count matching the provided query
     *
     * @param em
     * @param query
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery query)
            throws KapuaException {
        return ServiceDAO.count(em, DeviceConnectionOption.class, DeviceConnectionOptionImpl.class, query);
    }
}
