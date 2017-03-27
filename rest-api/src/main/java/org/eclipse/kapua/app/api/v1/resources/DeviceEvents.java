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

import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.eclipse.kapua.app.api.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventPredicates;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api("Devices")
@Path("{scopeId}/devices/{deviceId}/events")
public class DeviceEvents extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
    private final DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);

    /**
     * Gets the {@link DeviceEvent} list in the scope.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param deviceId
     *            The id of the {@link Device} in which to search results
     * @param resource
     *            The resource of the {@link DeviceEvent} in which to search results
     * @param offset
     *            The result set offset.
     * @param limit
     *            The result set limit.
     * @return The {@link DeviceEventListResult} of all the deviceEvents associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the DeviceEvent list in the scope",
            notes = "Returns the list of all the deviceEvents associated to the current selected scope.",
            response = DeviceEvent.class,
            responseContainer = "DeviceEventListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceEventListResult simpleQuery(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The client id to filter results.") EntityId deviceId,
            @ApiParam(value = "The resource of the DeviceEvent in which to search results") @QueryParam("resource") String resource,
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,
            @ApiParam(value = "The result set limit.", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit)
    {
        DeviceEventListResult deviceEventListResult = deviceEventFactory.newListResult();
        try {
            DeviceEventQuery query = deviceEventFactory.newQuery(scopeId);

            AndPredicate andPredicate = new AndPredicate();
            andPredicate.and(new AttributePredicate<>(DeviceEventPredicates.DEVICE_ID, deviceId));
            if (!Strings.isNullOrEmpty(resource)) {
                andPredicate.and(new AttributePredicate<>(DeviceEventPredicates.RESOURCE, resource));
            }
            query.setPredicate(andPredicate);

            query.setOffset(offset);
            query.setLimit(limit);

            deviceEventListResult = query(scopeId, deviceId, query);
        } catch (Throwable t) {
            handleException(t);
        }
        return deviceEventListResult;
    }

    /**
     * Queries the results with the given {@link DeviceEventQuery} parameter.
     * 
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param deviceId
     *            The id of the {@link Device} in which to search results
     * @param query
     *            The {@link DeviceEventQuery} to use to filter results.
     * @return The {@link DeviceEventListResult} of all the result matching the given {@link DeviceEventQuery} parameter.
     * @since 1.0.0
     */
    @ApiOperation(value = "Queries the DeviceEvents",
            notes = "Queries the DeviceEvents with the given DeviceEvents parameter returning all matching DeviceEvents",
            response = DeviceEvent.class,
            responseContainer = "DeviceEventListResult")
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public DeviceEventListResult query(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Device in which to search results") @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The DeviceEventQuery to use to filter results.", required = true) DeviceEventQuery query) {
        DeviceEventListResult deviceEventListResult = null;
        try {
            query.setScopeId(scopeId);

            AndPredicate andPredicate = new AndPredicate();
            andPredicate.and(new AttributePredicate<>(DeviceEventPredicates.DEVICE_ID, deviceId));
            andPredicate.and(query.getPredicate());
            query.setPredicate(andPredicate);

            deviceEventListResult = deviceEventService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(deviceEventListResult);
    }

    /**
     * Counts the results with the given {@link DeviceEventQuery} parameter.
     * 
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param deviceId
     *            The id of the {@link Device} in which to search results
     * @param query
     *            The {@link DeviceEventQuery} to use to filter results.
     * @return The count of all the result matching the given {@link DeviceEventQuery} parameter.
     * @since 1.0.0
     */
    @ApiOperation(value = "Counts the DeviceEvents",
            notes = "Counts the DeviceEvents with the given DeviceEventQuery parameter returning the number of matching DeviceEvents",
            response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(
            @ApiParam(value = "The ScopeId in which to count results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Device in which to count results") @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The DeviceEventQuery to use to filter count results", required = true) DeviceEventQuery query) {
        CountResult countResult = null;
        try {
            query.setScopeId(scopeId);
            query.setPredicate(new AttributePredicate<>(DeviceEventPredicates.DEVICE_ID, deviceId));
            countResult = new CountResult(deviceEventService.count(query));
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(countResult);
    }

    /**
     * Returns the DeviceEvent specified by the "deviceEventId" path parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the requested {@link DeviceEvent}.
     * @param deviceId
     *            The {@link Device} id of the request {@link DeviceEvent}.
     * @param deviceEventId
     *            The id of the requested DeviceEvent.
     * @return The requested DeviceEvent object.
     */
    @ApiOperation(value = "Get an DeviceEvent", notes = "Returns the DeviceEvent specified by the \"deviceEventId\" path parameter.", response = DeviceEvent.class)
    @GET
    @Path("{deviceEventId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceEvent find(
            @ApiParam(value = "The ScopeId of the requested DeviceEvent.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested Device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The id of the requested DeviceEvent", required = true) @PathParam("deviceEventId") EntityId deviceEventId) {
        DeviceEvent deviceEvent = null;
        try {
            DeviceEventQuery query = deviceEventFactory.newQuery(scopeId);

            AndPredicate andPredicate = new AndPredicate();
            andPredicate.and(new AttributePredicate<>(DeviceEventPredicates.DEVICE_ID, deviceId));
            andPredicate.and(new AttributePredicate<>(DeviceEventPredicates.ENTITY_ID, deviceEventId));

            query.setOffset(0);
            query.setLimit(1);

            DeviceEventListResult results = deviceEventService.query(query);

            if (!results.isEmpty()) {
                deviceEvent = results.getFirstItem();
            }
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(deviceEvent);
    }

    /**
     * Deletes the DeviceEvent specified by the "deviceEventId" path parameter.
     * @param deviceId
     *            The id of the Device in which to delete the event
     * @param deviceEventId
     *            The id of the DeviceEvent to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     */
    @ApiOperation(value = "Delete a DeviceEvent", notes = "Deletes the DeviceEvent specified by the \"deviceEventId\" path parameter.")
    @DELETE
    @Path("{deviceEventId}")
    public Response deleteDeviceEvent(@PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Device in which to delete the event.", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The id of the DeviceEvent to be deleted", required = true) @PathParam("deviceEventId") EntityId deviceEventId) {
        try {
            DeviceEvent deviceEvent = find(scopeId, deviceId, deviceEventId);
            if (deviceEvent == null) {
                throw new EntityNotFoundException();
            }

            deviceEventService.delete(scopeId, deviceEventId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }
}
