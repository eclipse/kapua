/*******************************************************************************
 * Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationAttributes;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;
import org.eclipse.kapua.service.device.registry.Device;

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

//@Path("{scopeId}/devices/{deviceId}/operations")
public class DeviceManagementOperations extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceManagementOperationRegistryService deviceManagementOperationRegistryService = locator.getService(DeviceManagementOperationRegistryService.class);
    private final DeviceManagementOperationFactory deviceManagementOperationFactory = locator.getFactory(DeviceManagementOperationFactory.class);

    /**
     * Gets the {@link DeviceManagementOperation} list in the scope.
     *
     * @param scopeId  The {@link ScopeId} in which to search results.
     * @param deviceId The id of the {@link Device} in which to search results
     * @param resource The resource of the {@link DeviceManagementOperation} in which to search results
     * @param offset   The result set offset.
     * @param limit    The result set limit.
     * @return The {@link DeviceManagementOperationListResult} of all the deviceManagementOperations associated to the current selected scope.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceManagementOperationListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("resource") String resource,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        DeviceManagementOperationQuery query = deviceManagementOperationFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate();

        andPredicate.and(query.attributePredicate(DeviceManagementOperationAttributes.DEVICE_ID, deviceId));

        if (!Strings.isNullOrEmpty(resource)) {
            andPredicate.and(query.attributePredicate(DeviceManagementOperationAttributes.RESOURCE, resource));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, deviceId, query);
    }

    /**
     * Queries the results with the given {@link DeviceManagementOperationQuery} parameter.
     *
     * @param scopeId  The {@link ScopeId} in which to search results.
     * @param deviceId The id of the {@link Device} in which to search results
     * @param query    The {@link DeviceManagementOperationQuery} to use to filter results.
     * @return The {@link DeviceManagementOperationListResult} of all the result matching the given {@link DeviceManagementOperationQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeviceManagementOperationListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            DeviceManagementOperationQuery query) throws Exception {
        query.setScopeId(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        andPredicate.and(query.attributePredicate(DeviceManagementOperationAttributes.DEVICE_ID, deviceId));
        query.setPredicate(andPredicate);

        return deviceManagementOperationRegistryService.query(query);
    }

    /**
     * Counts the results with the given {@link DeviceManagementOperationQuery} parameter.
     *
     * @param scopeId  The {@link ScopeId} in which to search results.
     * @param deviceId The id of the {@link Device} in which to search results
     * @param query    The {@link DeviceManagementOperationQuery} to use to filter results.
     * @return The count of all the result matching the given {@link DeviceManagementOperationQuery} parameter.
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
            DeviceManagementOperationQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(query.attributePredicate(DeviceManagementOperationAttributes.DEVICE_ID, deviceId));

        return new CountResult(deviceManagementOperationRegistryService.count(query));
    }

    /**
     * Returns the DeviceManagementOperation specified by the "deviceManagementOperationId" path parameter.
     *
     * @param scopeId                     The {@link ScopeId} of the requested {@link DeviceManagementOperation}.
     * @param deviceId                    The {@link Device} id of the request {@link DeviceManagementOperation}.
     * @param deviceManagementOperationId The id of the requested DeviceManagementOperation.
     * @return The requested DeviceManagementOperation object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{deviceManagementOperationId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceManagementOperation find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @PathParam("deviceManagementOperationId") EntityId deviceManagementOperationId) throws Exception {
        DeviceManagementOperationQuery query = deviceManagementOperationFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate(
                query.attributePredicate(DeviceManagementOperationAttributes.DEVICE_ID, deviceId),
                query.attributePredicate(DeviceManagementOperationAttributes.ENTITY_ID, deviceManagementOperationId)
        );

        query.setPredicate(andPredicate);
        query.setOffset(0);
        query.setLimit(1);

        DeviceManagementOperationListResult results = deviceManagementOperationRegistryService.query(query);

        if (!results.isEmpty()) {
            return results.getFirstItem();
        } else {
            throw new KapuaEntityNotFoundException(DeviceManagementOperation.TYPE, deviceManagementOperationId);
        }
    }

    /**
     * Deletes the DeviceManagementOperation specified by the "deviceManagementOperationId" path parameter.
     *
     * @param deviceId                    The id of the Device in which to delete the ManagementOperation
     * @param deviceManagementOperationId The id of the DeviceManagementOperation to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @DELETE
    @Path("{deviceManagementOperationId}")
    public Response deleteDeviceManagementOperation(@PathParam("scopeId") ScopeId scopeId,
                                                    @PathParam("deviceId") EntityId deviceId,
                                                    @PathParam("deviceManagementOperationId") EntityId deviceManagementOperationId) throws Exception {
        deviceManagementOperationRegistryService.delete(scopeId, deviceManagementOperationId);

        return returnOk();
    }
}
