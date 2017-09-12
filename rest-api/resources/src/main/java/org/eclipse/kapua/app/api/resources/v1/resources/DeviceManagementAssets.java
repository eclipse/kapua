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
package org.eclipse.kapua.app.api.resources.v1.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Authorization;

import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetFactory;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetManagementService;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.registry.Device;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "Devices", authorizations = { @Authorization(value = "kapuaAccessToken") })
@Path("{scopeId}/devices/{deviceId}/assets")
public class DeviceManagementAssets extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceAssetManagementService deviceManagementAssetService = locator.getService(DeviceAssetManagementService.class);
    private final DeviceAssetFactory deviceAssetFilter = locator.getFactory(DeviceAssetFactory.class);

    /**
     * Returns the list of all the Assets configured on the device.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @return The list of Assets
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(nickname = "deviceAssetGet", value = "Gets a list of assets", notes = "Returns the list of all the Assets installed on the device.", response = DeviceAssets.class)
    public DeviceAssets get(
            @ApiParam(value = "The ScopeId of the device.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The timeout of the operation in milliseconds") @QueryParam("timeout") Long timeout) throws Exception {
        return get(scopeId, deviceId, timeout, deviceAssetFilter.newAssetListResult());
    }

    /**
     * Returns the list of all the Assets configured on the device filtered by the {@link DeviceAssets} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @return The list of Assets
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(nickname = "deviceAssetFilteredGet", value = "Gets a list of assets", notes = "Returns the list of all the Assets installed on the device.", response = DeviceAssets.class)
    public DeviceAssets get(
            @ApiParam(value = "The ScopeId of the device.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The timeout of the operation in milliseconds") @QueryParam("timeout") Long timeout,
            @ApiParam(value = "The filter of the request") DeviceAssets deviceAssetFilter) throws Exception {
        return deviceManagementAssetService.get(scopeId, deviceId, deviceAssetFilter, timeout);
    }

    /**
     * Reads {@link DeviceAssetChannel}s values available on the device filtered by the {@link DeviceAssets} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @return The list of Assets
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_read")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(nickname = "deviceAssetRead", value = "Reads asset channel values", notes = "Returns the value read from the asset channel", response = DeviceAssets.class)
    public DeviceAssets read(
            @ApiParam(value = "The ScopeId of the device.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The timeout of the operation in milliseconds") @QueryParam("timeout") Long timeout,
            @ApiParam(value = "The filter of the read request") DeviceAssets deviceAssetFilter) throws Exception {
        return deviceManagementAssetService.read(scopeId, deviceId, deviceAssetFilter, timeout);
    }

    /**
     * Writes {@link DeviceAssetChannel}s configured on the device filtered by the {@link DeviceAssets} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @return The list of Assets
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_write")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(nickname = "deviceAssetWrite", value = "Gets a list of assets", notes = "Returns the list of all the Assets installed on the device.", response = DeviceAssets.class)
    public DeviceAssets write(
            @ApiParam(value = "The ScopeId of the device.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The timeout of the operation in milliseconds") @QueryParam("timeout") Long timeout,
            @ApiParam(value = "The values to write to the asset channels") DeviceAssets deviceAssetFilter) throws Exception {
        return deviceManagementAssetService.write(scopeId, deviceId, deviceAssetFilter, timeout);
    }

}
