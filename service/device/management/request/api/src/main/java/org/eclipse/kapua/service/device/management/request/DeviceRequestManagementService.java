/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.request;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.DeviceManagementService;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;

public interface DeviceRequestManagementService extends DeviceManagementService {

    /**
     * Execute the given device request with the provided options
     *
     * @param requestInput The {@link GenericRequestMessage} for this request
     * @param timeout      The timeout in milliseconds for the request to complete
     * @return the response from the device.
     * @throws KapuaException if error occurs during processing
     * @since 1.0.0
     * @deprecated since 1.1.0. Please make use of {@link #exec(KapuaId, KapuaId, GenericRequestMessage, Long)}.
     */
    @Deprecated
    GenericResponseMessage exec(GenericRequestMessage requestInput, Long timeout) throws KapuaException;

    /**
     * Execute the given device request with the provided options
     *
     * @param scopeId      The scope {@link KapuaId} of the target device
     * @param deviceId     The device {@link KapuaId} of the target device
     * @param requestInput The {@link GenericRequestMessage} for this request
     * @param timeout      The timeout in milliseconds for the request to complete
     * @return the response from the device
     * @throws KapuaException if error occurs during processing
     * @since 1.1.0
     */
    GenericResponseMessage exec(KapuaId scopeId, KapuaId deviceId, GenericRequestMessage requestInput, Long timeout) throws KapuaException;
}
