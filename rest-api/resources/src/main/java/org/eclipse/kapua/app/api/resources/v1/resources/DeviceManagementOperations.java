/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperation;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationAttributes;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationProperty;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.DeviceManagementOperationRegistryService;
import org.eclipse.kapua.service.device.management.registry.operation.OperationStatus;
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
import java.util.Arrays;
import java.util.Date;

@Api(value = "Devices Operations", authorizations = {@Authorization(value = "kapuaAccessToken")})
@Path("{scopeId}/devices/{deviceId}/operations")
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
    @ApiOperation(nickname = "deviceManagementOperationSimpleQuery", value = "Gets the DeviceManagementOperation list in the scope", notes = "Returns the list of all the deviceManagementOperations associated to the current selected scope.", response = DeviceManagementOperationListResult.class)
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceManagementOperationListResult simpleQuery(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The client id to filter results.") @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The resource of the DeviceManagementOperation in which to search results") @QueryParam("resource") String resource,
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,
            @ApiParam(value = "The result set limit.", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        DeviceManagementOperationQuery query = deviceManagementOperationFactory.newQuery(scopeId);

        AndPredicateImpl andPredicate = new AndPredicateImpl();
        andPredicate.and(new AttributePredicateImpl<>(DeviceManagementOperationAttributes.DEVICE_ID, deviceId));
        if (!Strings.isNullOrEmpty(resource)) {
            andPredicate.and(new AttributePredicateImpl<>(DeviceManagementOperationAttributes.RESOURCE, resource));
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
    @ApiOperation(nickname = "deviceManagementOperationQuery", value = "Queries the DeviceManagementOperations", notes = "Queries the DeviceManagementOperations with the given DeviceManagementOperations parameter returning all matching DeviceManagementOperations", response = DeviceManagementOperationListResult.class)
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public DeviceManagementOperationListResult query(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Device in which to search results") @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The DeviceManagementOperationQuery to use to filter results.", required = true) DeviceManagementOperationQuery query) throws Exception {
        query.setScopeId(scopeId);

        AndPredicateImpl andPredicate = new AndPredicateImpl();
        andPredicate.and(new AttributePredicateImpl<>(DeviceManagementOperationAttributes.DEVICE_ID, deviceId));
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
    @ApiOperation(nickname = "deviceManagementOperationCount", value = "Counts the DeviceManagementOperations", notes = "Counts the DeviceManagementOperations with the given DeviceManagementOperationQuery parameter returning the number of matching DeviceManagementOperations", response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @ApiParam(value = "The ScopeId in which to count results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Device in which to count results") @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The DeviceManagementOperationQuery to use to filter count results", required = true) DeviceManagementOperationQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(new AttributePredicateImpl<>(DeviceManagementOperationAttributes.DEVICE_ID, deviceId));

        try {
            DeviceManagementOperationProperty deviceManagementOperationProperty = deviceManagementOperationFactory.newStepProperty("asd", "string", "qwe");

            DeviceManagementOperationCreator deviceManagementOperationCreator = deviceManagementOperationFactory.newCreator(scopeId);
            deviceManagementOperationCreator.setStartedOn(new Date());
            deviceManagementOperationCreator.setDeviceId(deviceId);
            deviceManagementOperationCreator.setStatus(OperationStatus.COMPLETED);
            deviceManagementOperationCreator.setAppId("ASD");
            deviceManagementOperationCreator.setAction(KapuaMethod.CREATE);
            deviceManagementOperationCreator.setResource("qwe");
            deviceManagementOperationCreator.setInputProperties(Arrays.asList(deviceManagementOperationProperty));

            deviceManagementOperationRegistryService.create(deviceManagementOperationCreator);

        } catch (Exception e) {
            e.printStackTrace();
        }

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
    @ApiOperation(nickname = "deviceManagementOperationFind", value = "Get an DeviceManagementOperation", notes = "Returns the DeviceManagementOperation specified by the \"deviceManagementOperationId\" path parameter.", response = DeviceManagementOperation.class)
    @GET
    @Path("{deviceManagementOperationId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public DeviceManagementOperation find(
            @ApiParam(value = "The ScopeId of the requested DeviceManagementOperation.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested Device", required = true) @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The id of the requested DeviceManagementOperation", required = true) @PathParam("deviceManagementOperationId") EntityId deviceManagementOperationId) throws Exception {
        DeviceManagementOperationQuery query = deviceManagementOperationFactory.newQuery(scopeId);

        AndPredicateImpl andPredicate = new AndPredicateImpl();
        andPredicate.and(new AttributePredicateImpl<>(DeviceManagementOperationAttributes.DEVICE_ID, deviceId));
        andPredicate.and(new AttributePredicateImpl<>(DeviceManagementOperationAttributes.ENTITY_ID, deviceManagementOperationId));

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
    @ApiOperation(nickname = "deviceManagementOperationDelete", value = "Delete a DeviceManagementOperation", notes = "Deletes the DeviceManagementOperation specified by the \"deviceManagementOperationId\" path parameter.")
    @DELETE
    @Path("{deviceManagementOperationId}")
    public Response deleteDeviceManagementOperation(@PathParam("scopeId") ScopeId scopeId,
                                                    @ApiParam(value = "The id of the Device in which to delete the ManagementOperation.", required = true) @PathParam("deviceId") EntityId deviceId,
                                                    @ApiParam(value = "The id of the DeviceManagementOperation to be deleted", required = true) @PathParam("deviceManagementOperationId") EntityId deviceManagementOperationId) throws Exception {
        deviceManagementOperationRegistryService.delete(scopeId, deviceManagementOperationId);

        return returnOk();
    }
}
