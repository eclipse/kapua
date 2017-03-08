/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.app.api.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandManagementService;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshotManagementService;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventPredicates;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Devices")
@Path("{scopeId}/devices/{deviceId}/commands")
public class DeviceManagementCommands extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
    private final DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);
    private final DeviceCommandManagementService commandService = locator.getService(DeviceCommandManagementService.class);
    
    /**
     * Executes a remote command on a device and return the command output.
     *
     * <p>
     * Example to list all files in the current working directory:
     *
     * <pre>
     * Client client = client();
     * WebResource apisWeb = client.resource(APIS_TEST_URL);
     * WebResource.Builder deviceCommandWebXml = apisWeb.path(&quot;devices&quot;)
     * .path(s_clientId)
     * .path(&quot;command&quot;)
     * .accept(MediaType.APPLICATION_XML)
     * .type(MediaType.APPLICATION_XML);
     *
     * DeviceCommandInput commandInput = new DeviceCommandInput();
     * commandInput.setCommand(&quot;ls&quot;);
     * commandInput.setArguments(new String[] { &quot;-l&quot;, &quot;-a&quot; });
     *
     * DeviceCommandOutput commandOutput = deviceCommandWebXml.post(DeviceCommandOutput.class, commandInput);
     * </pre>
     *
     * @param deviceId
     * The {@link Device} ID.
     * @param timeout
     * The timeout of the command execution
     * @param commandInput
     * The input command
     *
     * @return The command output.
     */
     @POST
     @Path("_execute")
     @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
     @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
     @ApiOperation(value = "Executes a command", notes = "Executes a remote command on a device and return the command output.", response = DeviceCommandOutput.class)
     public DeviceCommandOutput sendCommand(
             @PathParam("scopeId") ScopeId scopeId,
             @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId, //
             @ApiParam(value = "The timeout of the command execution", required = false) @QueryParam("timeout") @DefaultValue("") Long timeout,
             
     @ApiParam(value = "The input command", required = true) DeviceCommandInput commandInput) 
             {
         DeviceCommandOutput commandOutput = null;
         
         try {
             commandOutput =  commandService.exec(scopeId, deviceId, commandInput, timeout);
         } catch (Throwable t) {
             handleException(t);
         }
         return returnNotNullEntity(commandOutput);
     }
}
