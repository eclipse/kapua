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

import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.packages.DevicePackageManagementService;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackages;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;
import org.eclipse.kapua.service.device.management.packages.model.uninstall.DevicePackageUninstallRequest;
import org.eclipse.kapua.service.device.registry.Device;

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
    public DevicePackages get(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout) throws Exception {
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
    public Response download(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout,
            DevicePackageDownloadRequest packageDownloadRequest)
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
    public Response uninstall(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout,
            DevicePackageUninstallRequest packageUninstallRequest) throws Exception {
        packageService.uninstallExec(scopeId, deviceId, packageUninstallRequest, timeout);

        return returnOk();
    }

}
