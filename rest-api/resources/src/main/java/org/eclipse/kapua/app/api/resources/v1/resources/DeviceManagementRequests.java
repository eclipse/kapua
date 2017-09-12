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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;

import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
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

@Api(value = "Devices", authorizations = { @Authorization(value = "kapuaAccessToken") })
@Path("{scopeId}/devices/{deviceId}/requests")
public class DeviceManagementRequests extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceRequestManagementService requestService = locator.getService(DeviceRequestManagementService.class);

    /**
     * Sends a request message to a device.
     * This call is generally used to perform remote management of resources
     * attached to the device such sensors and registries.
     * <p>
     * <p>
     * Example to send a request to the Kura Command application:
     * <p>
     * 
     * <pre>
     * {
     *   "type": "genericRequestMessage",
     *   "position": {
     *     "type": "kapuaPosition"
     *   },
     *   "channel": {
     *     "type": "genericRequestChannel",
     *     "method": "EXECUTE",
     *     "appName": "CMD",
     *     "version": "V1",
     *     "resources": ["command"]
     *   },
     *   "payload": {
     *     "type": "genericRequestPayload",
     *     "metrics": {
     *       "metric": [
     *         {
     *           "valueType": "string",
     *           "value": "ls",
     *           "name": "command.command"
     *         },
     *         {
     *           "valueType": "string",
     *           "value": "-lisa",
     *           "name": "command.argument0"
     *         }
     *       ]
     *     }
     *   }
     * }
     * </pre>
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The {@link Device} ID.
     * @param timeout
     *            The timeout of the request execution
     * @param requestMessage
     *            The input request
     * @return The response output.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     */
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @ApiOperation(nickname = "deviceRequestSend", value = "Sends a request", notes = "Sends a request message to a device", response = DeviceCommandOutput.class)
    public GenericResponseMessage sendRequest(
            @ApiParam(value = "The ScopeId of the device", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The timeout of the request execution") @QueryParam("timeout") Long timeout,
            @ApiParam(value = "The input request", required = true) GenericRequestMessage requestMessage) throws Exception {
        requestMessage.setScopeId(scopeId);
        requestMessage.setDeviceId(deviceId);
        return requestService.exec(requestMessage, timeout);
    }
}
