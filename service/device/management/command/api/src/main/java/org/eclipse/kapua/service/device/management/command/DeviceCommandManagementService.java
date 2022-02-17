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
package org.eclipse.kapua.service.device.management.command;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.DeviceManagementService;
import org.eclipse.kapua.service.device.registry.Device;

/**
 * {@link DeviceCommand} {@link KapuaService} definition.
 *
 * @since 1.0.0
 */
public interface DeviceCommandManagementService extends DeviceManagementService {

    /**
     * Executes the given {@link DeviceCommandInput} on the target {@link Device}.
     *
     * @param scopeId      The {@link Device#getScopeId()}.
     * @param deviceId     The {@link Device#getId()}.
     * @param commandInput The {@link DeviceCommandInput} to be executed.
     * @param timeout      The time to wait the {@link Device} response.
     * @return The {@link DeviceCommandOutput} containing the execution result.
     * @throws KapuaException
     * @since 1.0.0
     */
    DeviceCommandOutput exec(KapuaId scopeId, KapuaId deviceId, DeviceCommandInput commandInput, Long timeout) throws KapuaException;
}
