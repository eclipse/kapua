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
package org.eclipse.kapua.service.device.management.request;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.response.KapuaResponseChannel;
import org.eclipse.kapua.service.device.management.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.response.KapuaResponsePayload;

public interface DeviceRequestManagementService extends KapuaService {
    /**
     * Execute the given device request with the provided options
     *
     * @param scopeId
     * @param deviceId
     * @param requestInput
     * @param timeout
     *            command timeout
     * @return
     * @throws KapuaException
     */
     KapuaResponseMessage<KapuaResponseChannel, KapuaResponsePayload> exec(GenericRequestMessage requestInput, Long timeout)
            throws KapuaException;
}
