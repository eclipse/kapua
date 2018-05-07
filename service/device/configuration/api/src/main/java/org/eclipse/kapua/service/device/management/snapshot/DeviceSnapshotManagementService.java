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
package org.eclipse.kapua.service.device.management.snapshot;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.DeviceManagementService;

/**
 * Device snapshot service definition.
 *
 * @since 1.0
 */
public interface DeviceSnapshotManagementService extends KapuaService, DeviceManagementService {

    /**
     * Get the device snapshots list for the the provided device identifier
     *
     * @param scopeId
     * @param deviceid
     * @param timeout  timeout waiting for the device response
     * @return
     * @throws KapuaException
     */
    DeviceSnapshots get(KapuaId scopeId, KapuaId deviceid, Long timeout) throws KapuaException;

    /**
     * Rollback the device configuration to the device snapshot identified by the provided snapshot identifier
     *
     * @param scopeId
     * @param deviceid
     * @param snapshotId
     * @param timeout    timeout waiting for the device response
     * @throws KapuaException
     */
    void rollback(KapuaId scopeId, KapuaId deviceid, String snapshotId, Long timeout) throws KapuaException;
}
