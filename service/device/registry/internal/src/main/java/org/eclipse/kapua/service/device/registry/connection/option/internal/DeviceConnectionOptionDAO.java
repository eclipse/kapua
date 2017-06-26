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
package org.eclipse.kapua.service.device.registry.connection.option.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;

/**
 * Device connection options DAO
 *
 * @since 1.0
 */
public class DeviceConnectionOptionDAO extends ServiceDAO {

    /**
     * Find the device connection by device connection identifier
     *
     * @param em
     * @param deviceConnectionOptionsId
     * @return
     */
    public static DeviceConnectionOption find(EntityManager em, KapuaId deviceConnectionOptionsId) {
        return em.find(DeviceConnectionOptionImpl.class, deviceConnectionOptionsId);
    }

    /**
     * Update the provided device connection options
     *
     * @param em
     * @param deviceConnectionOptions
     * @return
     * @throws KapuaEntityNotFoundException
     *             If the {@link DeviceConnectionOption} is not found.
     */
    public static DeviceConnectionOption update(EntityManager em, DeviceConnectionOption deviceConnectionOptions)
            throws KapuaException {
        DeviceConnectionOptionImpl deviceConnectionOpptionsImpl = (DeviceConnectionOptionImpl) deviceConnectionOptions;
        return ServiceDAO.update(em, DeviceConnectionOptionImpl.class, deviceConnectionOpptionsImpl);
    }
}
