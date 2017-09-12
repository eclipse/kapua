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
import javax.ws.rs.core.Response;

import io.swagger.annotations.Authorization;

import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.device.management.snapshot.DeviceSnapshots;
import org.eclipse.kapua.service.device.registry.Device;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "Devices", authorizations = { @Authorization(value = "kapuaAccessToken") })
@Path("{scopeId}/devices/{deviceId}/packages")
public class DeviceManagementPackages extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DevicePackageManagementService packageService = locator.getService(DevicePackageManagementService.class);

    /**
     * Returns the list of all the packages installed on the device.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param deviceId
     *            The id of the device
     * @param timeout
     *            The timeout of the operation
     * @return The list of packages installed.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(nickname = "devicePackageGet", value = "Gets a list of packages", notes = "Returns the list of all the packages installed on the device.", response = DeviceSnapshots.class)
    public DevicePackages get(
            @ApiParam(value = "The ScopeId of the Device", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The timeout of the operation", required = false) @QueryParam("timeout") Long timeout) throws Exception {
        return packageService.getInstalled(scopeId, deviceId, timeout);
    }

    /**
     * Download and optionally installs a package into the device.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param deviceId
     *            The {@link Device} ID.
     * @param timeout
     *            The timeout of the operation
     * @param packageDownloadRequest
     *            Mandatory object with all the informations needed to download and install a package
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_download")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @ApiOperation(nickname = "devicePackageDownload", value = "Installs a package", notes = "Installs a package into the device.")
    public Response download(
            @ApiParam(value = "The ScopeId of the Device", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The timeout of the operation") @QueryParam("timeout") Long timeout,
            @ApiParam(value = "Mandatory object with all the informations needed to download and install a package", required = true) DevicePackageDownloadRequest packageDownloadRequest)
            throws Exception {
        packageService.downloadExec(scopeId, deviceId, packageDownloadRequest, timeout);

        return returnOk();
    }

    /**
     * Uninstalls a package into the device.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param deviceId
     *            The {@link Device} ID.
     * @param timeout
     *            The timeout of the operation
     * @param packageUninstallRequest
     *            Mandatory object with all the informations needed to uninstall a package
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_uninstall")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @ApiOperation(nickname = "devicePackageUninstall", value = "Uninstalls a package", notes = "Uninstalls a package into the device.")
    public Response uninstall(
            @ApiParam(value = "The ScopeId of the Device", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The timeout of the operation") @QueryParam("timeout") Long timeout,
            @ApiParam(value = "Mandatory object with all the informations needed to uninstall a package", required = true) DevicePackageUninstallRequest packageUninstallRequest) throws Exception {
        packageService.uninstallExec(scopeId, deviceId, packageUninstallRequest, timeout);

        return returnOk();
    }

}
