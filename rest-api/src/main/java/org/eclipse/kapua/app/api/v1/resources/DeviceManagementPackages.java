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

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.app.api.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.kapua.service.device.registry.Device;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Devices")
@Path("{scopeId}/devices/{deviceId}/packages")
public class DeviceManagementPackages extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DevicePackageManagementService packageService = locator.getService(DevicePackageManagementService.class);

    /**
     * Returns the list of all the packages installed on the device.
     *
     * @param deviceId
     *            The id of the device
     * @return The list of packages installed.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(value = "Gets a list of packages", notes = "Returns the list of all the packages installed on the device.", response = DeviceSnapshots.class)
    public DevicePackages get(
            @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId, //
            @QueryParam("timeout") @DefaultValue("") Long timeout) {
        DevicePackages deviceSnapshots = null;
        try {
            deviceSnapshots = packageService.getInstalled(scopeId, deviceId, timeout);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(deviceSnapshots);
    }

    /**
     * Download and optionally installs a package into the device.
     *
     * @param deviceId
     *            The {@link Device} ID.
     * @return HTTP 200 if operation has completed successfully.
     */
    @POST
    @Path("_download")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Installs a package", notes = "Installs a package into the device.")
    public Response download(
            @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId, //
            @QueryParam("timeout") @DefaultValue("") Long timeout,
            @ApiParam(value = "Mandatory object with all the informations needed to download and install a package", required = true) DevicePackageDownloadRequest packageDownloadRequest) {
        try {
            packageService.downloadExec(scopeId, deviceId, packageDownloadRequest, timeout);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }

    /**
     * Uninstalls a package into the device.
     *
     * @param deviceId
     *            The {@link Device} ID.
     * @return HTTP 200 if operation has completed successfully.
     */
    @POST
    @Path("_uninstall")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Uninstalls a package", notes = "Uninstalls a package into the device.")
    public Response uninstall(
            @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId, //
            @QueryParam("timeout") @DefaultValue("") Long timeout,
            @ApiParam(value = "Mandatory object with all the informations needed to uninstall a package", required = true) DevicePackageUninstallRequest packageUninstallRequest) {
        try {
            packageService.uninstallExec(scopeId, deviceId, packageUninstallRequest, timeout);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }

}
