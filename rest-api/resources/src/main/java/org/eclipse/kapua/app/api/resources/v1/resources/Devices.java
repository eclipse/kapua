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

import java.util.List;

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

import io.swagger.annotations.Authorization;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DevicePredicates;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;

import com.google.common.base.Strings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "Devices", authorizations = { @Authorization(value = "kapuaAccessToken") })
@Path("{scopeId}/devices")
public class Devices extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceRegistryService deviceService = locator.getService(DeviceRegistryService.class);
    private final DeviceFactory deviceFactory = locator.getFactory(DeviceFactory.class);

    /**
     * Gets the {@link Device} list in the scope.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param clientId
     *            The id of the {@link Device} in which to search results
     * @param connectionStatus
     *            The {@link DeviceConnectionStatus} in which to search results
     * @param fetchAttributes
     *            Additional attributes to be returned. Allowed values: connection, lastEvent
     * @param offset
     *            The result set offset.
     * @param limit
     *            The result set limit.
     * @return The {@link DeviceListResult} of all the devices associated to the current selected scope.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     * 
     */
    @ApiOperation(nickname = "deviceSimpleQuery", value = "Gets the Device list in the scope", notes = "Returns the list of all the devices associated to the current selected scope.", response = DeviceListResult.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceListResult simpleQuery(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The tag id to filter results.") @QueryParam("tagId") EntityId tagId,
            @ApiParam(value = "The client id to filter results.") @QueryParam("clientId") String clientId,
            @ApiParam(value = "The connection status to filter results.") @QueryParam("status") DeviceConnectionStatus connectionStatus,
            @ApiParam(value = "Additional attributes to be returned. Allowed values: connection, lastEvent", allowableValues = "connection, lastEvent", allowMultiple = true) @QueryParam("fetchAttributes") List<String> fetchAttributes,
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,
            @ApiParam(value = "The result set limit.", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        DeviceQuery query = deviceFactory.newQuery(scopeId);

        AndPredicate andPredicate = new AndPredicate();
        if (tagId != null) {
            andPredicate.and(new AttributePredicate<KapuaId>(DevicePredicates.TAG_IDS, tagId));
        }
        if (!Strings.isNullOrEmpty(clientId)) {
            andPredicate.and(new AttributePredicate<>(DevicePredicates.CLIENT_ID, clientId));
        }
        if (connectionStatus != null) {
            andPredicate.and(new AttributePredicate<>(DevicePredicates.CONNECTION_STATUS, connectionStatus));
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
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link DeviceQuery} to use to filter results.
     * @return The {@link DeviceListResult} of all the result matching the given {@link DeviceQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "deviceQuery", value = "Queries the Devices", notes = "Queries the Devices with the given Devices parameter returning all matching Devices", response = DeviceListResult.class)
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public DeviceListResult query(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The DeviceQuery to use to filter results.", required = true) DeviceQuery query) throws Exception {
        query.setScopeId(scopeId);

        return deviceService.query(query);
    }

    /**
     * Counts the results with the given {@link DeviceQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link DeviceQuery} to use to filter results.
     * @return The count of all the result matching the given {@link DeviceQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "deviceCount", value = "Counts the Devices", notes = "Counts the Devices with the given DeviceQuery parameter returning the number of matching Devices", response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(
            @ApiParam(value = "The ScopeId in which to count results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The DeviceQuery to use to filter count results", required = true) DeviceQuery query) throws Exception {
        query.setScopeId(scopeId);

        return new CountResult(deviceService.count(query));
    }

    /**
     * Creates a new Device based on the information provided in DeviceCreator
     * parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to create the {@link Device}
     * @param deviceCreator
     *            Provides the information for the new Device to be created.
     * @return The newly created Device object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "deviceCreate", value = "Create an Device", notes = "Creates a new Device based on the information provided in DeviceCreator parameter.", response = Device.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Device create(
            @ApiParam(value = "The ScopeId in which to create the Device.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Provides the information for the new Device to be created", required = true) DeviceCreator deviceCreator) throws Exception {
        deviceCreator.setScopeId(scopeId);

        return deviceService.create(deviceCreator);
    }

    /**
     * Returns the Device specified by the "deviceId" path parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the requested {@link Device}.
     * @param deviceId
     *            The id of the requested Device.
     * @return The requested Device object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "deviceFind", value = "Get a Device", notes = "Returns the Device specified by the \"deviceId\" path parameter.", response = Device.class)
    @GET
    @Path("{deviceId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Device find(
            @ApiParam(value = "The ScopeId of the requested Device", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested Device", required = true) @PathParam("deviceId") EntityId deviceId) throws Exception {
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
     * @param scopeId
     *            The ScopeId of the requested Device.
     * @param deviceId
     *            The id of the requested {@link Device}
     * @param device
     *            The modified Device whose attributed need to be updated.
     * @return The updated device.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "deviceUpdate", value = "Update a Device", notes = "Updates a new Device based on the information provided in the Device parameter.", response = Device.class)
    @PUT
    @Path("{deviceId}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Device update(
            @ApiParam(value = "The ScopeId of the requested Device.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested Device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The modified Device whose attributed need to be updated", required = true) Device device) throws Exception {
        device.setScopeId(scopeId);
        device.setId(deviceId);

        return deviceService.update(device);
    }

    /**
     * Deletes the Device specified by the "deviceId" path parameter.
     *
     * @param scopeId
     *            The ScopeId of the requested {@link Device}.
     * @param deviceId
     *            The id of the Device to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "deviceDelete", value = "Delete a Device", notes = "Deletes the Device specified by the \"deviceId\" path parameter.")
    @DELETE
    @Path("{deviceId}")
    public Response deleteDevice(
            @ApiParam(value = "The ScopeId of the Device to delete.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Device to be deleted", required = true) @PathParam("deviceId") EntityId deviceId) throws Exception {
        deviceService.delete(scopeId, deviceId);

        return returnOk();
    }
}
