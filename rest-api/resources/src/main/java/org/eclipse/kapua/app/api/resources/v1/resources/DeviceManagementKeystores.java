/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.app.api.resources.v1.resources.model.device.management.keystore.DeviceKeystoreCertificateInfo;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.keystore.DeviceKeystoreManagementFactory;
import org.eclipse.kapua.service.device.management.keystore.DeviceKeystoreManagementService;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSR;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSRInfo;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCertificate;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItem;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItemQuery;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItems;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreKeypair;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystores;
import org.eclipse.kapua.service.device.registry.Device;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * {@link DeviceKeystoreManagementService} {@link AbstractKapuaResource}
 *
 * @since 1.5.0
 */
@Path("{scopeId}/devices/{deviceId}/keystore")
public class DeviceManagementKeystores extends AbstractKapuaResource {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final DeviceKeystoreManagementService DEVICE_KEYSTORE_MANAGEMENT_SERVICE = LOCATOR.getService(DeviceKeystoreManagementService.class);
    private static final DeviceKeystoreManagementFactory DEVICE_KEYSTORE_MANAGEMENT_FACTORY = LOCATOR.getFactory(DeviceKeystoreManagementFactory.class);

    /**
     * Gets the {@link DeviceKeystores} present on the {@link Device}.
     *
     * @param scopeId  The {@link Device#getScopeId()}.
     * @param deviceId The {@link Device#getId()}.
     * @param timeout  The timeout of the operation in milliseconds
     * @return The {@link DeviceKeystores}.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceKeystores getKeystores(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout) throws KapuaException {
        return DEVICE_KEYSTORE_MANAGEMENT_SERVICE.getKeystores(scopeId, deviceId, timeout);
    }

    /**
     * Gets the {@link DeviceKeystoreItems} present on the {@link Device}.
     *
     * @param scopeId  The {@link Device#getScopeId()}.
     * @param deviceId The {@link Device#getId()}.
     * @param timeout  The timeout of the operation in milliseconds
     * @return The {@link DeviceKeystoreItems}.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @GET
    @Path("items")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceKeystoreItems getKeystoreItems(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("keystoreId") String keystoreId,
            @QueryParam("alias") String alias,
            @QueryParam("timeout") Long timeout) throws KapuaException {

        DeviceKeystoreItemQuery itemQuery = DEVICE_KEYSTORE_MANAGEMENT_FACTORY.newDeviceKeystoreItemQuery();
        itemQuery.setKeystoreId(keystoreId);
        itemQuery.setAlias(alias);

        return DEVICE_KEYSTORE_MANAGEMENT_SERVICE.getKeystoreItems(scopeId, deviceId, itemQuery, timeout);
    }

    /**
     * Gets the {@link DeviceKeystoreItem} present on the {@link Device}.
     *
     * @param scopeId  The {@link Device#getScopeId()}.
     * @param deviceId The {@link Device#getId()}.
     * @param timeout  The timeout of the operation in milliseconds
     * @return The {@link DeviceKeystoreItem}.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @GET
    @Path("item")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceKeystoreItem getKeystoreItem(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("keystoreId") String keystoreId,
            @QueryParam("alias") String alias,
            @QueryParam("timeout") Long timeout) throws KapuaException {

        return DEVICE_KEYSTORE_MANAGEMENT_SERVICE.getKeystoreItem(scopeId, deviceId, keystoreId, alias, timeout);
    }

    /**
     * Creates a {@link DeviceKeystoreCertificate} into the {@link Device}.
     *
     * @param scopeId                 The {@link Device#getScopeId()}.
     * @param deviceId                The {@link Device#getId()}.
     * @param keystoreCertificateInfo The {@link DeviceKeystoreCertificateInfo} to create.
     * @param timeout                 The timeout of the operation in milliseconds
     * @return HTTP {@link Response#noContent()} code.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @POST
    @Path("items/certificateInfo")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createDeviceKeystoreCertificate(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout,
            DeviceKeystoreCertificateInfo keystoreCertificateInfo) throws KapuaException {

        DEVICE_KEYSTORE_MANAGEMENT_SERVICE.createKeystoreCertificate(
                scopeId,
                deviceId,
                keystoreCertificateInfo.getKeystoreId(),
                keystoreCertificateInfo.getAlias(),
                keystoreCertificateInfo.getCertificateInfoId(),
                timeout);

        return returnNoContent();
    }

    /**
     * Creates a {@link DeviceKeystoreCertificate} into the {@link Device}.
     *
     * @param scopeId             The {@link Device#getScopeId()}.
     * @param deviceId            The {@link Device#getId()}.
     * @param keystoreCertificate The {@link DeviceKeystoreCertificate} to create.
     * @param timeout             The timeout of the operation in milliseconds
     * @return HTTP {@link Response#noContent()} code.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @POST
    @Path("items/certificateRaw")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createDeviceKeystoreCertificate(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout,
            DeviceKeystoreCertificate keystoreCertificate) throws KapuaException {

        DEVICE_KEYSTORE_MANAGEMENT_SERVICE.createKeystoreCertificate(scopeId, deviceId, keystoreCertificate, timeout);

        return returnNoContent();
    }


    /**
     * Creates a {@link DeviceKeystoreKeypair} into the {@link Device}.
     *
     * @param scopeId               The {@link Device#getScopeId()}.
     * @param deviceId              The {@link Device#getId()}.
     * @param deviceKeystoreKeypair The {@link DeviceKeystoreKeypair} to create.
     * @param timeout               The timeout of the operation in milliseconds
     * @return HTTP {@link Response#noContent()} code.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @POST
    @Path("items/keypair")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createDeviceKeystoreKeypair(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout,
            DeviceKeystoreKeypair deviceKeystoreKeypair) throws KapuaException {

        DEVICE_KEYSTORE_MANAGEMENT_SERVICE.createKeystoreKeypair(scopeId, deviceId, deviceKeystoreKeypair, timeout);

        return returnNoContent();
    }

    /**
     * Sends a {@link DeviceKeystoreCSRInfo} into the {@link Device}.
     *
     * @param scopeId               The {@link Device#getScopeId()}.
     * @param deviceId              The {@link Device#getId()}.
     * @param deviceKeystoreCSRInfo The {@link DeviceKeystoreCSRInfo} to create.
     * @param timeout               The timeout of the operation in milliseconds
     * @return The {@link DeviceKeystoreCSR}.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @POST
    @Path("items/csr")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceKeystoreCSR createDeviceKeystoreCSR(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout,
            DeviceKeystoreCSRInfo deviceKeystoreCSRInfo) throws KapuaException {

        return DEVICE_KEYSTORE_MANAGEMENT_SERVICE.createKeystoreCSR(scopeId, deviceId, deviceKeystoreCSRInfo, timeout);
    }

    /**
     * Gets the {@link DeviceKeystoreItem} present on the {@link Device}.
     *
     * @param scopeId  The {@link Device#getScopeId()}.
     * @param deviceId The {@link Device#getId()}.
     * @param timeout  The timeout of the operation in milliseconds
     * @return HTTP {@link Response#noContent()} code.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.5.0
     */
    @DELETE
    @Path("item")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteKeystoreItem(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("keystoreId") String keystoreId,
            @QueryParam("alias") String alias,
            @QueryParam("timeout") Long timeout) throws KapuaException {

        DEVICE_KEYSTORE_MANAGEMENT_SERVICE.deleteKeystoreItem(scopeId, deviceId, keystoreId, alias, timeout);

        return returnNoContent();
    }
}
