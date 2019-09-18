/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("{scopeId}/devices")
public class Devices extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceRegistryService deviceService = locator.getService(DeviceRegistryService.class);
    private final DeviceFactory deviceFactory = locator.getFactory(DeviceFactory.class);

    /**
     * Gets the {@link Device} list in the scope.
     *
     * @param scopeId          The {@link ScopeId} in which to search results.
     * @param clientId         The id of the {@link Device} in which to search results
     * @param connectionStatus The {@link DeviceConnectionStatus} in which to search results
     * @param fetchAttributes  Additional attributes to be returned. Allowed values: connection, lastEvent
     * @param offset           The result set offset.
     * @param limit            The result set limit.
     * @return The {@link DeviceListResult} of all the devices associated to the current selected scope.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @QueryParam("tagId") EntityId tagId,
            @QueryParam("clientId") String clientId,
            @QueryParam("status") DeviceConnectionStatus connectionStatus,
            @QueryParam("fetchAttributes") List<String> fetchAttributes,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        DeviceQuery query = deviceFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        if (tagId != null) {
            andPredicate.and(query.attributePredicate(DeviceAttributes.TAG_IDS, tagId));
        }
        if (!Strings.isNullOrEmpty(clientId)) {
            andPredicate.and(query.attributePredicate(DeviceAttributes.CLIENT_ID, clientId));
        }
        if (connectionStatus != null) {
            andPredicate.and(query.attributePredicate(DeviceAttributes.CONNECTION_STATUS, connectionStatus));
        }
        query.setPredicate(andPredicate);
        query.setFetchAttributes(fetchAttributes);
        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link DeviceQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link DeviceQuery} to use to filter results.
     * @return The {@link DeviceListResult} of all the result matching the given {@link DeviceQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeviceListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            DeviceQuery query) throws Exception {
        query.setScopeId(scopeId);

        return deviceService.query(query);
    }

    /**
     * Counts the results with the given {@link DeviceQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link DeviceQuery} to use to filter results.
     * @return The count of all the result matching the given {@link DeviceQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @PathParam("scopeId") ScopeId scopeId,
            DeviceQuery query) throws Exception {
        query.setScopeId(scopeId);

        return new CountResult(deviceService.count(query));
    }

    /**
     * Creates a new Device based on the information provided in DeviceCreator
     * parameter.
     *
     * @param scopeId       The {@link ScopeId} in which to create the {@link Device}
     * @param deviceCreator Provides the information for the new Device to be created.
     * @return The newly created Device object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Device create(
            @PathParam("scopeId") ScopeId scopeId,
            DeviceCreator deviceCreator) throws Exception {
        deviceCreator.setScopeId(scopeId);

        return deviceService.create(deviceCreator);
    }

    /**
     * Returns the Device specified by the "deviceId" path parameter.
     *
     * @param scopeId  The {@link ScopeId} of the requested {@link Device}.
     * @param deviceId The id of the requested Device.
     * @return The requested Device object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{deviceId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Device find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId) throws Exception {
        Device device = deviceService.find(scopeId, deviceId);

        if (device != null) {
            return device;
        } else {
            throw new KapuaEntityNotFoundException(Device.TYPE, deviceId);
        }
    }

    /**
     * Updates the Device based on the information provided in the Device parameter.
     *
     * @param scopeId  The ScopeId of the requested Device.
     * @param deviceId The id of the requested {@link Device}
     * @param device   The modified Device whose attributed need to be updated.
     * @return The updated device.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @PUT
    @Path("{deviceId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Device update(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            Device device) throws Exception {
        device.setScopeId(scopeId);
        device.setId(deviceId);

        return deviceService.update(device);
    }

    /**
     * Deletes the Device specified by the "deviceId" path parameter.
     *
     * @param scopeId  The ScopeId of the requested {@link Device}.
     * @param deviceId The id of the Device to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @DELETE
    @Path("{deviceId}")
    public Response deleteDevice(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId) throws Exception {
        deviceService.delete(scopeId, deviceId);

        return returnOk();
    }
}
