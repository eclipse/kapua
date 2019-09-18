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
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleManagementService;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;
import org.eclipse.kapua.service.device.registry.Device;

@Path("{scopeId}/devices/{deviceId}/bundles")
public class DeviceManagementBundles extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceBundleManagementService bundleService = locator.getService(DeviceBundleManagementService.class);

    /**
     * Returns the list of all the Bundles installed on the device.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @return The list of Bundles
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceBundles get(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout) throws Exception {
        return bundleService.get(scopeId, deviceId, timeout);
    }

    /**
     * Starts the bundle
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The {@link Device} ID.
     * @param bundleId
     *            the ID of the bundle to start
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("{bundleId}/_start")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response start(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @PathParam("bundleId") String bundleId,
            @QueryParam("timeout") Long timeout) throws Exception {
        bundleService.start(scopeId, deviceId, bundleId, timeout);

        return returnOk();
    }

    /**
     * Stops the bundle
     *
     * @param deviceId
     *            The {@link Device} ID.
     * @param bundleId
     *            the ID of the bundle to stop
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("{bundleId}/_stop")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response stop(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @PathParam("bundleId") String bundleId,
            @QueryParam("timeout") Long timeout) throws Exception {
        bundleService.stop(scopeId, deviceId, bundleId, timeout);

        return returnOk();
    }
}
