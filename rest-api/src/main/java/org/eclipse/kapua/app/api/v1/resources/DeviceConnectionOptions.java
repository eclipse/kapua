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

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOption;
import org.eclipse.kapua.service.device.registry.connection.option.DeviceConnectionOptionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Device Connections")
@Path("{scopeId}/deviceconnections/{connectionId}/options")
public class DeviceConnectionOptions extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceConnectionOptionService deviceConnectionOptionsService = locator.getService(DeviceConnectionOptionService.class);

    /**
     * Returns the {@link DeviceConnectionOption} specified by the given parameters.
     *
     * @param scopeId
     *            The {@link ScopeId} of the requested {@link DeviceConnectionOption}.
     * @param connectionId
     *            The {@link DeviceConnectionOption} id of the request
     *            {@link DeviceConnectionOption}.
     * @return The requested {@link DeviceConnectionOption} object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the DeviceConnection list in the scope", notes = "Returns the list of all the deviceConnections associated to the current selected scope.", response = DeviceConnection.class, responseContainer = "DeviceConnectionListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceConnectionOption find(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The connection id of the requested options.") @PathParam("connectionId") EntityId connectionId) throws Exception {
        DeviceConnectionOption deviceConnectionOptions = deviceConnectionOptionsService.find(scopeId, connectionId);

        if (deviceConnectionOptions != null) {
            return deviceConnectionOptions;
        } else {
            throw new KapuaEntityNotFoundException(DeviceConnectionOption.TYPE, connectionId);
        }

    }

    /**
     * Returns the DeviceConnection specified by the "deviceConnectionId" path parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the requested {@link DeviceConnection}.
     * @param deviceConnectionId
     *            The id of the requested DeviceConnection.
     * @return The requested DeviceConnection object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(value = "Get an DeviceConnectionOption", notes = "Returns the DeviceConnectionOption specified by the given parameters", response = DeviceConnectionOption.class)
    @PUT
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceConnectionOption update(
            @ApiParam(value = "The ScopeId of the requested DeviceConnectionOptions.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested DeviceConnectionOptions", required = true) @PathParam("connectionId") EntityId deviceConnectionId,
            @ApiParam(value = "The modified Device connection options whose attributed need to be updated", required = true) DeviceConnectionOption deviceConnectionOptions)
            throws Exception {

        deviceConnectionOptions.setScopeId(scopeId);
        deviceConnectionOptions.setId(deviceConnectionId);

        return deviceConnectionOptionsService.update(deviceConnectionOptions);
    }

}
