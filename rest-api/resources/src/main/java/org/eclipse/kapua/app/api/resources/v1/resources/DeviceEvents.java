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

import io.swagger.annotations.Authorization;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntityPredicates;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventPredicates;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

import com.google.common.base.Strings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "Devices", authorizations = { @Authorization(value = "kapuaAccessToken") })
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
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "deviceEventSimpleQuery", value = "Gets the DeviceEvent list in the scope", notes = "Returns the list of all the deviceEvents associated to the current selected scope.", response = DeviceEventListResult.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceEventListResult simpleQuery(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The client id to filter results.") @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The resource of the DeviceEvent in which to search results") @QueryParam("resource") String resource,
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,
            @ApiParam(value = "The result set limit.", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        DeviceEventQuery query = deviceEventFactory.newQuery(scopeId);

        AndPredicateImpl andPredicate = new AndPredicateImpl();
        andPredicate.and(new AttributePredicateImpl<>(DeviceEventPredicates.DEVICE_ID, deviceId));
        if (!Strings.isNullOrEmpty(resource)) {
            andPredicate.and(new AttributePredicateImpl<>(DeviceEventPredicates.RESOURCE, resource));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, deviceId, query);
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
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "deviceEventQuery", value = "Queries the DeviceEvents", notes = "Queries the DeviceEvents with the given DeviceEvents parameter returning all matching DeviceEvents", response = DeviceEventListResult.class)
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public DeviceEventListResult query(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Device in which to search results") @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The DeviceEventQuery to use to filter results.", required = true) DeviceEventQuery query) throws Exception {
        query.setScopeId(scopeId);

        AndPredicateImpl andPredicate = new AndPredicateImpl();
        andPredicate.and(new AttributePredicateImpl<>(DeviceEventPredicates.DEVICE_ID, deviceId));
        andPredicate.and(query.getPredicate());
        query.setPredicate(andPredicate);

        return deviceEventService.query(query);
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
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "deviceEventCount", value = "Counts the DeviceEvents", notes = "Counts the DeviceEvents with the given DeviceEventQuery parameter returning the number of matching DeviceEvents", response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(
            @ApiParam(value = "The ScopeId in which to count results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Device in which to count results") @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The DeviceEventQuery to use to filter count results", required = true) DeviceEventQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(new AttributePredicateImpl<>(DeviceEventPredicates.DEVICE_ID, deviceId));

        return new CountResult(deviceEventService.count(query));
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
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "deviceEventFind", value = "Get an DeviceEvent", notes = "Returns the DeviceEvent specified by the \"deviceEventId\" path parameter.", response = DeviceEvent.class)
    @GET
    @Path("{deviceEventId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceEvent find(
            @ApiParam(value = "The ScopeId of the requested DeviceEvent.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested Device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The id of the requested DeviceEvent", required = true) @PathParam("deviceEventId") EntityId deviceEventId) throws Exception {
        DeviceEventQuery query = deviceEventFactory.newQuery(scopeId);

        AndPredicateImpl andPredicate = new AndPredicateImpl();
        andPredicate.and(new AttributePredicateImpl<>(DeviceEventPredicates.DEVICE_ID, deviceId));
        andPredicate.and(new AttributePredicateImpl<>(KapuaEntityPredicates.ENTITY_ID, deviceEventId));

        query.setPredicate(andPredicate);
        query.setOffset(0);
        query.setLimit(1);

        DeviceEventListResult results = deviceEventService.query(query);

        if (!results.isEmpty()) {
            return results.getFirstItem();
        } else {
            throw new KapuaEntityNotFoundException(DeviceEvent.TYPE, deviceEventId);
        }
    }

    /**
     * Deletes the DeviceEvent specified by the "deviceEventId" path parameter.
     *
     * @param deviceId
     *            The id of the Device in which to delete the event
     * @param deviceEventId
     *            The id of the DeviceEvent to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "deviceEventDelete", value = "Delete a DeviceEvent", notes = "Deletes the DeviceEvent specified by the \"deviceEventId\" path parameter.")
    @DELETE
    @Path("{deviceEventId}")
    public Response deleteDeviceEvent(@PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Device in which to delete the event.", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The id of the DeviceEvent to be deleted", required = true) @PathParam("deviceEventId") EntityId deviceEventId) throws Exception {
        deviceEventService.delete(scopeId, deviceEventId);

        return returnOk();
    }
}
