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
package org.eclipse.kapua.service.device.management.snapshot;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.DeviceManagementService;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.registry.Device;

/**
 * {@link DeviceSnapshot} {@link KapuaService} definition.
 *
 * @since 1.0.0
 */
public interface DeviceSnapshotManagementService extends DeviceManagementService {

    /**
     * Gets the {@link DeviceSnapshots} for the given {@link Device}
     *
     * @param scopeId  The scope {@link KapuaId}
     * @param deviceid The {@link Device#getId()}
     * @param timeout  The timeout waiting for the {@link Device} response
     * @return The {@link DeviceSnapshots} retrieved
     * @throws KapuaException if something goes wrong
     */
    DeviceSnapshots get(KapuaId scopeId, KapuaId deviceid, Long timeout) throws KapuaException;

    /**
     * Rollbacks the {@link DeviceConfiguration} to the {@link DeviceSnapshot#getId()}
     *
     * @param scopeId    The scope {@link KapuaId}
     * @param deviceid   The {@link Device#getId()}
     * @param snapshotId The {@link DeviceSnapshot#getId()}
     * @param timeout    The timeout waiting for the {@link Device} response
     * @throws KapuaException if something goes wrong
     */
    void rollback(KapuaId scopeId, KapuaId deviceid, String snapshotId, Long timeout) throws KapuaException;
}
