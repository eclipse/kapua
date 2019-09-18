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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources;

import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.request.DeviceRequestManagementService;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;
import org.eclipse.kapua.service.device.registry.Device;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("{scopeId}/devices/{deviceId}/requests")
public class DeviceManagementRequests extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceRequestManagementService requestService = locator.getService(DeviceRequestManagementService.class);

    /**
     * Sends a request message to a device.
     * This call is generally used to perform remote management of resources
     * attached to the device such sensors and registries.
     *
     * @param scopeId        The {@link ScopeId} of the {@link Device}.
     * @param deviceId       The {@link Device} ID.
     * @param timeout        The timeout of the request execution
     * @param requestMessage The input request
     * @return The response output.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_XML})
    public GenericResponseMessage sendRequest(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout,
            GenericRequestMessage requestMessage) throws Exception {
        requestMessage.setScopeId(scopeId);
        requestMessage.setDeviceId(deviceId);

        return requestService.exec(requestMessage, timeout);
    }
}
