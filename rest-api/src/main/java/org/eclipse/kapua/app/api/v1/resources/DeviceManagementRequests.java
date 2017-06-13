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
package org.eclipse.kapua.app.api.v1.resources;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.eclipse.kapua.app.api.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.kapua.service.device.management.request.DeviceRequestManagementService;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.registry.Device;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Api("Devices")
@Path("{scopeId}/devices/{deviceId}/sendRequest")
public class DeviceManagementRequests extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceRequestManagementService requestService = locator.getService(DeviceRequestManagementService.class);

    /**
     * Executes a remote command on a device and return the command output.
     * <p>
     * <p>
     * Example to list all files in the current working directory:
     * <p>
     * 
     * <pre>
     * Client client = client();
     * WebResource apisWeb = client.resource(APIS_TEST_URL);
     * WebResource.Builder deviceCommandWebXml = apisWeb.path(&quot;devices&quot;)
     *         .path(s_clientId)
     *         .path(&quot;command&quot;)
     *         .accept(MediaType.APPLICATION_XML)
     *         .type(MediaType.APPLICATION_XML);
     *
     * DeviceCommandInput commandInput = new DeviceCommandInput();
     * commandInput.setCommand(&quot;ls&quot;);
     * commandInput.setArguments(new String[] { &quot;-l&quot;, &quot;-a&quot; });
     *
     * DeviceCommandOutput commandOutput = deviceCommandWebXml.post(DeviceCommandOutput.class, commandInput);
     * </pre>
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The {@link Device} ID.
     * @param timeout
     *            The timeout of the command execution
     * @param requestMessage
     *            The input command
     * @return The command output.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Executes a command", notes = "Executes a remote command on a device and return the command output.", response = DeviceCommandOutput.class)
    public KapuaResponseMessage sendCommand(
            @ApiParam(value = "The ScopeId of the device", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The timeout of the command execution") @QueryParam("timeout") Long timeout,
            @ApiParam(value = "The input command", required = true) GenericRequestMessage requestMessage) throws Exception {
        requestMessage.setScopeId(scopeId);
        requestMessage.setDeviceId(deviceId);
        return requestService.exec(requestMessage, timeout);
    }
}
