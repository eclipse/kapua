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
package org.eclipse.kapua.service.device.management.bundle;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;

/**
 * Device bundle service definition.
 *
 * @since 1.0
 *
 */
public interface DeviceBundleManagementService extends KapuaService {

    /**
     * Get the device bundles list for the given device identifier
     *
     * @param scopeId
     * @param deviceId
     * @param timeout
     *            timeout waiting for the device response
     * @return
     * @throws KapuaException
     */
    public DeviceBundles get(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException;

    /**
     * Start the device bundle identified by the given device identifier and device bundle identifier
     *
     * @param scopeId
     * @param deviceId
     * @param bundleId
     * @param timeout
     *            timeout waiting for the device response
     * @throws KapuaException
     */
    public void start(KapuaId scopeId, KapuaId deviceId, String bundleId, Long timeout)
            throws KapuaException;

    /**
     * Stop the device bundle identified by the given device identifier and device bundle identifier
     *
     * @param scopeId
     * @param deviceId
     * @param bundleId
     * @param timeout
     *            timeout waiting for the device response
     * @throws KapuaException
     */
    public void stop(KapuaId scopeId, KapuaId deviceId, String bundleId, Long timeout)
            throws KapuaException;
}
