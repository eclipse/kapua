/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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

/**
 * Device bundle service definition.
 *
 * @since 1.0
 */
public interface DeviceCommandManagementService extends KapuaService {

    /**
     * Execute the given device command with the provided options
     *
     * @param scopeId
     * @param deviceId
     * @param commandInput
     * @param timeout      command timeout
     * @return
     * @throws KapuaException
     */
    DeviceCommandOutput exec(KapuaId scopeId, KapuaId deviceId, DeviceCommandInput commandInput, Long timeout) throws KapuaException;
}
