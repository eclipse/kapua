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
package org.eclipse.kapua.service.device.management.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;

/**
 * Device configuration service definition.
 * 
 * @since 1.0
 *
 */
public interface DeviceConfigurationManagementService extends KapuaService
{

    /**
     * Get the device configuration for the given device identifier and configuration identifier
     * 
     * @param scopeId
     * @param deviceId
     * @param configurationId
     * @param configurationComponentPid
     * @param timeout timeout waiting for the device response
     * @return
     * @throws KapuaException
     */
    public DeviceConfiguration get(KapuaId scopeId,
                                   KapuaId deviceId,
                                   String configurationId,
                                   String configurationComponentPid,
                                   Long timeout)
        throws KapuaException;

    /**
     * Put the provided configuration to the device identified by the provided device identifier
     * 
     * @param scopeId
     * @param deviceId
     * @param xmlDeviceConfig xml marshalled device configuration
     * @param timeout timeout waiting for the device response
     * @throws KapuaException
     */
    public void put(KapuaId scopeId, KapuaId deviceId, String xmlDeviceConfig, Long timeout)
        throws KapuaException;

    /**
     * Put the provided configuration to the device identified by the provided device identifier
     * 
     * @param scopeId
     * @param deviceId
     * @param deviceConfig
     * @param timeout timeout waiting for the device response
     * @throws KapuaException
     */
    public void put(KapuaId scopeId, KapuaId deviceId, DeviceConfiguration deviceConfig, Long timeout)
        throws KapuaException;

    /**
     * Put the provided configuration to the device identified by the provided device identifier
     * 
     * @param scopeId
     * @param deviceId
     * @param deviceComponentConfig
     * @param timeout timeout waiting for the device response
     * @throws KapuaException
     */
    public void put(KapuaId scopeId, KapuaId deviceId, DeviceComponentConfiguration deviceComponentConfig, Long timeout)
        throws KapuaException;
}
