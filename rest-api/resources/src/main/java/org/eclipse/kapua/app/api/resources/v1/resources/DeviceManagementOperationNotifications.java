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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationAttributes;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationQuery;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationService;
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

//@Path("{scopeId}/devices/{deviceId}/operations/{operationId}/notifications")
public class DeviceManagementOperationNotifications extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final ManagementOperationNotificationService managementOperationNotificationService = locator.getService(ManagementOperationNotificationService.class);
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
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ManagementOperationNotificationListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @PathParam("operationId") EntityId operationId,
            @QueryParam("resource") String resource,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        ManagementOperationNotificationQuery query = managementOperationNotificationFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate(query.attributePredicate(ManagementOperationNotificationAttributes.OPERATION_ID, operationId));

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
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ManagementOperationNotificationListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @PathParam("operationId") EntityId operationId,
            ManagementOperationNotificationQuery query) throws Exception {
        query.setScopeId(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        andPredicate.and(query.attributePredicate(ManagementOperationNotificationAttributes.OPERATION_ID, operationId));
        query.setPredicate(andPredicate);

        return managementOperationNotificationService.query(query);
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
    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @PathParam("operationId") EntityId operationId,
            ManagementOperationNotificationQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(query.attributePredicate(ManagementOperationNotificationAttributes.OPERATION_ID, operationId));

        return new CountResult(managementOperationNotificationService.count(query));
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
    @GET
    @Path("{managementOperationNotificationId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ManagementOperationNotification find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @PathParam("operationId") EntityId operationId,
            @PathParam("managementOperationNotificationId") EntityId managementOperationNotificationId) throws Exception {
        ManagementOperationNotificationQuery query = managementOperationNotificationFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate(
                query.attributePredicate(ManagementOperationNotificationAttributes.OPERATION_ID, operationId),
                query.attributePredicate(ManagementOperationNotificationAttributes.ENTITY_ID, managementOperationNotificationId)
        );

        query.setPredicate(andPredicate);
        query.setOffset(0);
        query.setLimit(1);

        ManagementOperationNotificationListResult results = managementOperationNotificationService.query(query);

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
    @DELETE
    @Path("{managementOperationNotificationId}")
    public Response deleteManagementOperationNotification(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @PathParam("operationId") EntityId operationId,
            @PathParam("managementOperationNotificationId") EntityId managementOperationNotificationId) throws Exception {
        managementOperationNotificationService.delete(scopeId, managementOperationNotificationId);

        return returnOk();
    }
}
