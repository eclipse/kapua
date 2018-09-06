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
import org.eclipse.kapua.service.device.management.registry.operation.OperationStatus;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationAttributes;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationRegistryService;
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
import java.util.Date;

@Api(value = "Devices Operations", authorizations = {@Authorization(value = "kapuaAccessToken")})
@Path("{scopeId}/devices/{deviceId}/operations/{operationId}/notifications")
public class DeviceManagementOperationNotifications extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final ManagementOperationNotificationRegistryService managementOperationNotificationRegistryService = locator.getService(ManagementOperationNotificationRegistryService.class);
    private final ManagementOperationNotificationFactory managementOperationNotificationFactory = locator.getFactory(ManagementOperationNotificationFactory.class);

    /**
     * Gets the {@link ManagementOperationNotification} list in the scope.
     *
     * @param scopeId     The {@link ScopeId} in which to search results.
     * @param operationId The id of the {@link Device} in which to search results
     * @param resource    The resource of the {@link ManagementOperationNotification} in which to search results
     * @param offset      The result set offset.
     * @param limit       The result set limit.
     * @return The {@link ManagementOperationNotificationListResult} of all the ManagementOperationNotifications associated to the current selected scope.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "ManagementOperationNotificationSimpleQuery", value = "Gets the ManagementOperationNotification list in the scope", notes = "Returns the list of all the ManagementOperationNotifications associated to the current selected scope.", response = ManagementOperationNotificationListResult.class)
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ManagementOperationNotificationListResult simpleQuery(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The device id to filter results.") @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The operation id to filter results.") @PathParam("operationId") EntityId operationId,
            @ApiParam(value = "The resource of the ManagementOperationNotification in which to search results") @QueryParam("resource") String resource,
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,
            @ApiParam(value = "The result set limit.", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        ManagementOperationNotificationQuery query = managementOperationNotificationFactory.newQuery(scopeId);

        AndPredicateImpl andPredicate = new AndPredicateImpl();

        andPredicate.and(new AttributePredicateImpl<>(ManagementOperationNotificationAttributes.OPERATION_ID, operationId));

        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, deviceId, operationId, query);
    }

    /**
     * Queries the results with the given {@link ManagementOperationNotificationQuery} parameter.
     *
     * @param scopeId     The {@link ScopeId} in which to search results.
     * @param operationId The id of the {@link Device} in which to search results
     * @param query       The {@link ManagementOperationNotificationQuery} to use to filter results.
     * @return The {@link ManagementOperationNotificationListResult} of all the result matching the given {@link ManagementOperationNotificationQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "ManagementOperationNotificationQuery", value = "Queries the ManagementOperationNotifications", notes = "Queries the ManagementOperationNotifications with the given ManagementOperationNotifications parameter returning all matching ManagementOperationNotifications", response = ManagementOperationNotificationListResult.class)
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ManagementOperationNotificationListResult query(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The device id to filter results.") @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The id of the Device in which to search results") @PathParam("operationId") EntityId operationId,
            @ApiParam(value = "The ManagementOperationNotificationQuery to use to filter results.", required = true) ManagementOperationNotificationQuery query) throws Exception {
        query.setScopeId(scopeId);

        AndPredicateImpl andPredicate = new AndPredicateImpl();
        andPredicate.and(new AttributePredicateImpl<>(ManagementOperationNotificationAttributes.OPERATION_ID, operationId));
        query.setPredicate(andPredicate);

        return managementOperationNotificationRegistryService.query(query);
    }

    /**
     * Counts the results with the given {@link ManagementOperationNotificationQuery} parameter.
     *
     * @param scopeId     The {@link ScopeId} in which to search results.
     * @param operationId The id of the {@link Device} in which to search results
     * @param query       The {@link ManagementOperationNotificationQuery} to use to filter results.
     * @return The count of all the result matching the given {@link ManagementOperationNotificationQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "ManagementOperationNotificationCount", value = "Counts the ManagementOperationNotifications", notes = "Counts the ManagementOperationNotifications with the given ManagementOperationNotificationQuery parameter returning the number of matching ManagementOperationNotifications", response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @ApiParam(value = "The ScopeId in which to count results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The device id to filter results.") @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The id of the Device in which to count results") @PathParam("operationId") EntityId operationId,
            @ApiParam(value = "The ManagementOperationNotificationQuery to use to filter count results", required = true) ManagementOperationNotificationQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(new AttributePredicateImpl<>(ManagementOperationNotificationAttributes.OPERATION_ID, operationId));

        try {
            ManagementOperationNotificationCreator managementOperationNotificationCreator = managementOperationNotificationFactory.newCreator(scopeId);
            managementOperationNotificationCreator.setOperationId(operationId);
            managementOperationNotificationCreator.setProgress(100);
            managementOperationNotificationCreator.setSentOn(new Date());
            managementOperationNotificationCreator.setStatus(OperationStatus.RUNNING);

            managementOperationNotificationRegistryService.create(managementOperationNotificationCreator);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new CountResult(managementOperationNotificationRegistryService.count(query));
    }

    /**
     * Returns the ManagementOperationNotification specified by the "ManagementOperationNotificationId" path parameter.
     *
     * @param scopeId                           The {@link ScopeId} of the requested {@link ManagementOperationNotification}.
     * @param operationId                       The {@link Device} id of the request {@link ManagementOperationNotification}.
     * @param managementOperationNotificationId The id of the requested ManagementOperationNotification.
     * @return The requested ManagementOperationNotification object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "ManagementOperationNotificationFind", value = "Get an ManagementOperationNotification", notes = "Returns the ManagementOperationNotification specified by the \"ManagementOperationNotificationId\" path parameter.", response = ManagementOperationNotification.class)
    @GET
    @Path("{managementOperationNotificationId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ManagementOperationNotification find(
            @ApiParam(value = "The ScopeId of the requested ManagementOperationNotification.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The device id to filter results.") @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The id of the requested Device", required = true) @PathParam("operationId") EntityId operationId,
            @ApiParam(value = "The id of the requested ManagementOperationNotification", required = true) @PathParam("managementOperationNotificationId") EntityId managementOperationNotificationId) throws Exception {
        ManagementOperationNotificationQuery query = managementOperationNotificationFactory.newQuery(scopeId);

        AndPredicateImpl andPredicate = new AndPredicateImpl();
        andPredicate.and(new AttributePredicateImpl<>(ManagementOperationNotificationAttributes.OPERATION_ID, operationId));
        andPredicate.and(new AttributePredicateImpl<>(ManagementOperationNotificationAttributes.ENTITY_ID, managementOperationNotificationId));

        query.setPredicate(andPredicate);
        query.setOffset(0);
        query.setLimit(1);

        ManagementOperationNotificationListResult results = managementOperationNotificationRegistryService.query(query);

        if (!results.isEmpty()) {
            return results.getFirstItem();
        } else {
            throw new KapuaEntityNotFoundException(ManagementOperationNotification.TYPE, managementOperationNotificationId);
        }
    }

    /**
     * Deletes the ManagementOperationNotification specified by the "ManagementOperationNotificationId" path parameter.
     *
     * @param operationId                       The id of the Device in which to delete the ManagementOperation
     * @param managementOperationNotificationId The id of the ManagementOperationNotification to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "ManagementOperationNotificationDelete", value = "Delete a ManagementOperationNotification", notes = "Deletes the ManagementOperationNotification specified by the \"ManagementOperationNotificationId\" path parameter.")
    @DELETE
    @Path("{managementOperationNotificationId}")
    public Response deleteManagementOperationNotification(
            @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The device id to filter results.") @PathParam("deviceId") EntityId deviceId,
            @ApiParam(value = "The id of the Device in which to delete the ManagementOperation.", required = true) @PathParam("operationId") EntityId operationId,
            @ApiParam(value = "The id of the ManagementOperationNotification to be deleted", required = true) @PathParam("managementOperationNotificationId") EntityId managementOperationNotificationId) throws Exception {
        managementOperationNotificationRegistryService.delete(scopeId, managementOperationNotificationId);

        return returnOk();
    }
}
