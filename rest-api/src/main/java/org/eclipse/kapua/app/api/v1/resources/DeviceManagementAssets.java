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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.eclipse.kapua.app.api.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetManagementService;
import org.eclipse.kapua.service.device.management.asset.DeviceAssets;
import org.eclipse.kapua.service.device.registry.Device;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api("Devices")
@Path("{scopeId}/devices/{deviceId}/assets")
public class DeviceManagementAssets extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceAssetManagementService assetService = locator.getService(DeviceAssetManagementService.class);

    /**
     * Returns the list of all the Assets configured on the device.
     *
     * @param scopeId  The {@link ScopeId} of the {@link Device}.
     * @param deviceId The id of the device
     * @param timeout  The timeout of the operation in milliseconds
     * @return The list of Assets
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(value = "Gets a list of assets", notes = "Returns the list of all the Assets installed on the device.", response = DeviceAssets.class)
    public DeviceAssets get(
            @ApiParam(value = "The ScopeId of the device.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The timeout of the operation in milliseconds") @QueryParam("timeout") Long timeout) {
        DeviceAssets deviceAssets = null;
        try {
            deviceAssets = assetService.get(scopeId, deviceId, timeout);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(deviceAssets);
    }

//    /**
//     * Starts the asset
//     *
//     * @param scopeId  The {@link ScopeId} of the {@link Device}.
//     * @param deviceId The {@link Device} ID.
//     * @param assetId the ID of the asset to start
//     * @param timeout  The timeout of the operation in milliseconds
//     * @return HTTP 200 if operation has completed successfully.
//     */
//    @POST
//    @Path("{assetId}/_start")
//    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//    public Response start(
//            @ApiParam(value = "The ScopeId of the device.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
//            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId,
//            @ApiParam(value = "the ID of the asset to start", required = true) @PathParam("assetId") String assetId,
//            @ApiParam(value = "The timeout of the operation in milliseconds") @QueryParam("timeout") Long timeout) {
//        try {
//            assetService.start(scopeId, deviceId, assetId, timeout);
//        } catch (Throwable t) {
//            handleException(t);
//        }
//        return Response.ok().build();
//    }
//
//    /**
//     * Stops the asset
//     *
//     * @param deviceId The {@link Device} ID.
//     * @param assetId the ID of the asset to stop
//     * @return HTTP 200 if operation has completed successfully.
//     */
//    @POST
//    @Path("{assetId}/_stop")
//    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//    public Response stop(
//            @ApiParam(value = "The ScopeId of the device.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
//            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId,
//            @ApiParam(value = "the ID of the asset to stop", required = true) @PathParam("assetId") String assetId,
//            @ApiParam(value = "The timeout of the operation in milliseconds") @QueryParam("timeout") Long timeout) {
//        try {
//            assetService.stop(scopeId, deviceId, assetId, timeout);
//        } catch (Throwable t) {
//            handleException(t);
//        }
//        return Response.ok().build();
//    }
}
