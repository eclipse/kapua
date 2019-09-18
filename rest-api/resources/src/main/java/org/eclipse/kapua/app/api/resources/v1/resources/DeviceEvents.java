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
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventAttributes;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;

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

@Path("{scopeId}/devices/{deviceId}/events")
public class DeviceEvents extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceEventService deviceEventService = locator.getService(DeviceEventService.class);
    private final DeviceEventFactory deviceEventFactory = locator.getFactory(DeviceEventFactory.class);

    private final DeviceRegistryService deviceRegistryService = locator.getService(DeviceRegistryService.class);

    /**
     * Gets the {@link DeviceEvent} list in the scope.
     *
     * @param scopeId  The {@link ScopeId} in which to search results.
     * @param deviceId The id of the {@link Device} in which to search results
     * @param resource The resource of the {@link DeviceEvent} in which to search results
     * @param offset   The result set offset.
     * @param limit    The result set limit.
     * @return The {@link DeviceEventListResult} of all the deviceEvents associated to the current selected scope.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceEventListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("resource") String resource,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        DeviceEventQuery query = deviceEventFactory.newQuery(scopeId);

        if (deviceRegistryService.find(scopeId, deviceId) == null) {
             throw new KapuaEntityNotFoundException(Device.TYPE, deviceId);
        }

        AndPredicate andPredicate = query.andPredicate(query.attributePredicate(DeviceEventAttributes.DEVICE_ID, deviceId));

        if (!Strings.isNullOrEmpty(resource)) {
            andPredicate.and(query.attributePredicate(DeviceEventAttributes.RESOURCE, resource));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, deviceId, query);
    }

    /**
     * Queries the results with the given {@link DeviceEventQuery} parameter.
     *
     * @param scopeId  The {@link ScopeId} in which to search results.
     * @param deviceId The id of the {@link Device} in which to search results
     * @param query    The {@link DeviceEventQuery} to use to filter results.
     * @return The {@link DeviceEventListResult} of all the result matching the given {@link DeviceEventQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeviceEventListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            DeviceEventQuery query) throws Exception {
        query.setScopeId(scopeId);

        if (deviceRegistryService.find(scopeId, deviceId) == null) {
            throw new KapuaEntityNotFoundException(Device.TYPE, deviceId);
        }

        AndPredicate andPredicate = query.andPredicate(
                query.attributePredicate(DeviceEventAttributes.DEVICE_ID, deviceId),
                query.getPredicate()
        );

        query.setPredicate(andPredicate);

        return deviceEventService.query(query);
    }

    /**
     * Counts the results with the given {@link DeviceEventQuery} parameter.
     *
     * @param scopeId  The {@link ScopeId} in which to search results.
     * @param deviceId The id of the {@link Device} in which to search results
     * @param query    The {@link DeviceEventQuery} to use to filter results.
     * @return The count of all the result matching the given {@link DeviceEventQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            DeviceEventQuery query) throws Exception {

        if (deviceRegistryService.find(scopeId, deviceId) == null) {
            throw new KapuaEntityNotFoundException(Device.TYPE, deviceId);
        }

        query.setScopeId(scopeId);
        query.setPredicate(query.attributePredicate(DeviceEventAttributes.DEVICE_ID, deviceId));

        return new CountResult(deviceEventService.count(query));
    }

    /**
     * Returns the DeviceEvent specified by the "deviceEventId" path parameter.
     *
     * @param scopeId       The {@link ScopeId} of the requested {@link DeviceEvent}.
     * @param deviceId      The {@link Device} id of the request {@link DeviceEvent}.
     * @param deviceEventId The id of the requested DeviceEvent.
     * @return The requested DeviceEvent object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{deviceEventId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceEvent find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @PathParam("deviceEventId") EntityId deviceEventId) throws Exception {

        if (deviceRegistryService.find(scopeId, deviceId) == null) {
            throw new KapuaEntityNotFoundException(Device.TYPE, deviceId);
        }

        DeviceEventQuery query = deviceEventFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate(
                query.attributePredicate(DeviceEventAttributes.DEVICE_ID, deviceId),
                query.attributePredicate(KapuaEntityAttributes.ENTITY_ID, deviceEventId)
        );

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
     * @param deviceId      The id of the Device in which to delete the event
     * @param deviceEventId The id of the DeviceEvent to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @DELETE
    @Path("{deviceEventId}")
    public Response deleteDeviceEvent(@PathParam("scopeId") ScopeId scopeId,
                                      @PathParam("deviceId") EntityId deviceId,
                                      @PathParam("deviceEventId") EntityId deviceEventId) throws Exception {

        if (deviceRegistryService.find(scopeId, deviceId) == null) {
            throw new KapuaEntityNotFoundException(Device.TYPE, deviceId);
        }

        deviceEventService.delete(scopeId, deviceEventId);

        return returnOk();
    }
}
