/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.snapshot;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;

/**
 * Device snapshot service definition.
 *
 * @since 1.0
 */
public interface DeviceSnapshotManagementService extends KapuaService {

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
