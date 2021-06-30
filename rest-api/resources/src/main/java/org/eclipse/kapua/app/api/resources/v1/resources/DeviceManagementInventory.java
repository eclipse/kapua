/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.core.model.EntityId;
import org.eclipse.kapua.app.api.core.model.ScopeId;
import org.eclipse.kapua.app.api.core.resources.AbstractKapuaResource;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementService;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundle;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundleAction;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.inventory.DeviceInventoryBundles;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.packages.DeviceInventoryPackages;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.system.DeviceInventorySystemPackages;
import org.eclipse.kapua.service.device.registry.Device;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("{scopeId}/devices/{deviceId}/inventory")
public class DeviceManagementInventory extends AbstractKapuaResource {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceInventoryManagementService DEVICE_INVENTORY_MANAGEMENT_SERVICE = LOCATOR.getService(DeviceInventoryManagementService.class);

    /**
     * Gets the {@link DeviceInventory} present on the {@link Device}.
     *
     * @param scopeId  The {@link Device#getScopeId()}.
     * @param deviceId The {@link Device#getId()}.
     * @param timeout  The timeout of the operation in milliseconds
     * @return The {@link DeviceInventory}.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceInventory getInventory(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout) throws KapuaException {
        return DEVICE_INVENTORY_MANAGEMENT_SERVICE.getInventory(scopeId, deviceId, timeout);
    }

    /**
     * Gets the {@link DeviceInventoryBundles} present on the {@link Device}.
     *
     * @param scopeId  The {@link Device#getScopeId()}.
     * @param deviceId The {@link Device#getId()}.
     * @param timeout  The timeout of the operation in milliseconds
     * @return The {@link DeviceInventoryBundles}.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @GET
    @Path("bundles")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceInventoryBundles getInventoryBundles(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout) throws KapuaException {
        return DEVICE_INVENTORY_MANAGEMENT_SERVICE.getBundles(scopeId, deviceId, timeout);
    }

    /**
     * Starts a  {@link DeviceInventoryBundle} present on the {@link Device}.
     *
     * @param scopeId               The {@link Device#getScopeId()}.
     * @param deviceId              The {@link Device#getId()}.
     * @param deviceInventoryBundle The {@link DeviceInventoryBundle} to start.
     * @param timeout               The timeout of the operation in milliseconds
     * @return The {@link DeviceInventoryBundles}.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @POST
    @Path("bundles/_start")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response startInventoryBundles(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout,
            DeviceInventoryBundle deviceInventoryBundle) throws KapuaException {

        DEVICE_INVENTORY_MANAGEMENT_SERVICE.execBundle(scopeId, deviceId, deviceInventoryBundle, DeviceInventoryBundleAction.START, timeout);

        return returnNoContent();
    }

    /**
     * Starts a  {@link DeviceInventoryBundle} present on the {@link Device}.
     *
     * @param scopeId               The {@link Device#getScopeId()}.
     * @param deviceId              The {@link Device#getId()}.
     * @param deviceInventoryBundle The {@link DeviceInventoryBundle} to start.
     * @param timeout               The timeout of the operation in milliseconds
     * @return The {@link DeviceInventoryBundles}.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @POST
    @Path("bundles/_stop")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response stopInventoryBundles(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout,
            DeviceInventoryBundle deviceInventoryBundle) throws KapuaException {

        DEVICE_INVENTORY_MANAGEMENT_SERVICE.execBundle(scopeId, deviceId, deviceInventoryBundle, DeviceInventoryBundleAction.STOP, timeout);

        return returnNoContent();
    }

    /**
     * Gets the {@link DeviceInventoryPackages} present on the {@link Device}.
     *
     * @param scopeId  The {@link Device#getScopeId()}.
     * @param deviceId The {@link Device#getId()}.
     * @param timeout  The timeout of the operation in milliseconds
     * @return The {@link DeviceInventoryPackages}.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @GET
    @Path("packages")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceInventoryPackages getInventoryPackages(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout) throws KapuaException {
        return DEVICE_INVENTORY_MANAGEMENT_SERVICE.getDeploymentPackages(scopeId, deviceId, timeout);
    }

    /**
     * Gets the {@link DeviceInventorySystemPackages} present on the {@link Device}.
     *
     * @param scopeId  The {@link Device#getScopeId()}.
     * @param deviceId The {@link Device#getId()}.
     * @param timeout  The timeout of the operation in milliseconds
     * @return The {@link DeviceInventorySystemPackages}.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @GET
    @Path("system")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceInventorySystemPackages getInventorySystemPackages(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout) throws KapuaException {
        return DEVICE_INVENTORY_MANAGEMENT_SERVICE.getSystemPackages(scopeId, deviceId, timeout);
    }
}
