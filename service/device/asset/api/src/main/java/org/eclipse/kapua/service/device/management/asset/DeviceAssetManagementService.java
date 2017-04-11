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
package org.eclipse.kapua.service.device.management.asset;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;

/**
 * Device asset service definition.
 *
 * @since 1.0
 *
 */
public interface DeviceAssetManagementService extends KapuaService {

    /**
     * Get the device assets list for the given device identifier
     *
     * @param scopeId
     * @param deviceId
     * @param timeout
     *            timeout waiting for the device response
     * @return
     * @throws KapuaException
     */
    public DeviceAssets get(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException;
}
