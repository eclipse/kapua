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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.eclipse.kapua.app.api.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.access.shiro.AccessRolePredicates;

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

@Api("Access Info")
@Path("{scopeId}/accessinfos/{accessInfoId}/roles")
public class AccessRoles extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccessRoleService accessRoleService = locator.getService(AccessRoleService.class);
    private final AccessRoleFactory accessRoleFactory = locator.getFactory(AccessRoleFactory.class);

    /**
     * Gets the {@link AccessRole} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link AccessRoleListResult} of all the accessRoles associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the AccessRole list in the scope",
            notes = "Returns the list of all the accessRoles associated to the current selected scope.",
            response = AccessRole.class,
            responseContainer = "AccessRoleListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccessRoleListResult simpleQuery(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The optional id to filter results.") @PathParam("accessInfoId") EntityId accessInfoId,
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,
            @ApiParam(value = "The result set limit.", defaultValue = "50", required = true) @QueryParam("limit") @DefaultValue("50") int limit) {
        AccessRoleListResult accessRoleListResult = accessRoleFactory.newListResult();
        try {
            AccessRoleQuery query = accessRoleFactory.newQuery(scopeId);
            query.setPredicate(new AttributePredicate<>(AccessRolePredicates.ACCESS_INFO_ID, accessInfoId));
            query.setOffset(offset);
            query.setLimit(limit);

            accessRoleListResult = query(scopeId, accessInfoId, query);
        } catch (Throwable t) {
            handleException(t);
        }
        return accessRoleListResult;
    }

    /**
     * Queries the results with the given {@link AccessRoleQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link AccessRoleQuery} to use to filter results.
     * @return The {@link AccessRoleListResult} of all the result matching the given {@link AccessRoleQuery} parameter.
     * @since 1.0.0
     */
    @ApiOperation(value = "Queries the AccessRoles",
            notes = "Queries the AccessRoles with the given AccessRoleQuery parameter returning all matching AccessRoles",
            response = AccessRole.class,
            responseContainer = "AccessRoleListResult")
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public AccessRoleListResult query(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The AccessInfo id in which to search results.") @PathParam("accessInfoId") EntityId accessInfoId,
            @ApiParam(value = "The AccessRoleQuery to use to filter results.", required = true) AccessRoleQuery query) {
        AccessRoleListResult accessRoleListResult = null;
        try {
            query.setScopeId(scopeId);
            query.setPredicate(new AttributePredicate<>(AccessRolePredicates.ACCESS_INFO_ID, accessInfoId));
            accessRoleListResult = accessRoleService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(accessRoleListResult);
    }

    /**
     * Counts the results with the given {@link AccessRoleQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link AccessRoleQuery} to use to filter results.
     * @return The count of all the result matching the given {@link AccessRoleQuery} parameter.
     * @since 1.0.0
     */
    @ApiOperation(value = "Counts the AccessRoles",
            notes = "Counts the AccessRoles with the given AccessRoleQuery parameter returning the number of matching AccessRoles",
            response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(
            @ApiParam(value = "The ScopeId in which to count results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The AccessInfo id in which to count results.") @PathParam("accessInfoId") EntityId accessInfoId,
            @ApiParam(value = "The AccessRoleQuery to use to filter count results", required = true) AccessRoleQuery query) {
        CountResult countResult = null;
        try {
            query.setScopeId(scopeId);
            query.setPredicate(new AttributePredicate<>(AccessRolePredicates.ACCESS_INFO_ID, accessInfoId));
            countResult = new CountResult(accessRoleService.count(query));
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(countResult);
    }

    /**
     * Creates a new AccessRole based on the information provided in AccessRoleCreator
     * parameter.
     *
     * @param scopeId           The {@link ScopeId} in which to create the {@link AccessRole}.
     * @param accessRoleCreator Provides the information for the new AccessRole to be created.
     * @return The newly created AccessRole object.
     */
    @ApiOperation(value = "Create an AccessRole",
            notes = "Creates a new AccessRole based on the information provided in AccessRoleCreator parameter.",
            response = AccessRole.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public AccessRole create(
            @ApiParam(value = "The ScopeId in which to create the AccessRole", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The AccessInfo id in which to create the AccessRole.", required = true) @PathParam("accessInfoId") EntityId accessInfoId,
            @ApiParam(value = "Provides the information for the new AccessRole to be created", required = true) AccessRoleCreator accessRoleCreator) {
        AccessRole accessRole = null;
        try {
            accessRoleCreator.setScopeId(scopeId);
            accessRoleCreator.setAccessInfoId(accessInfoId);
            accessRole = accessRoleService.create(accessRoleCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(accessRole);
    }

    /**
     * Returns the AccessRole specified by the "accessRoleId" path parameter.
     *
     * @param scopeId      The {@link ScopeId} of the requested {@link AccessRole}.
     * @param accessRoleId The id of the requested {@link AccessRole}.
     * @return The requested {@link AccessRole} object.
     */
    @ApiOperation(value = "Get an AccessRole",
            notes = "Returns the AccessRole specified by the \"accessRoleId\" path parameter.",
            response = AccessRole.class)
    @GET
    @Path("{accessRoleId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccessRole find(
            @ApiParam(value = "The ScopeId of the requested AccessRole.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Specifies the AccessRoleId for the requested AccessRole", required = true) @PathParam("accessInfoId") EntityId accessInfoId,
            @ApiParam(value = "The id of the requested AccessRole", required = true) @PathParam("accessRoleId") EntityId accessRoleId) {
        AccessRole accessRole = null;
        try {
            AccessRoleQuery query = accessRoleFactory.newQuery(scopeId);

            AndPredicate andPredicate = new AndPredicate();
            andPredicate.and(new AttributePredicate<>(AccessRolePredicates.ACCESS_INFO_ID, accessInfoId));
            andPredicate.and(new AttributePredicate<>(AccessRolePredicates.ENTITY_ID, accessRoleId));

            query.setPredicate(andPredicate);
            query.setOffset(0);
            query.setLimit(1);

            AccessRoleListResult results = accessRoleService.query(query);

            if (!results.isEmpty()) {
                accessRole = results.getFirstItem();
            }
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(accessRole);
    }

    /**
     * Deletes the AccessRole specified by the "accessRoleId" path parameter.
     *
     * @param accessRoleId The id of the AccessRole to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     */
    @ApiOperation(value = "Delete an AccessRole",
            notes = "Deletes the AccessRole specified by the \"accessRoleId\" path parameter.")
    @DELETE
    @Path("{accessRoleId}")
    public Response deleteAccessRole(
            @ApiParam(value = "The ScopeId of the AccessRole to delete.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Specifies the AccessInfoId for the requested AccessPermission", required = true) @PathParam("accessInfoId") EntityId accessInfoId,
            @ApiParam(value = "The id of the AccessRole to be deleted", required = true) @PathParam("accessRoleId") EntityId accessRoleId) {
        try {
            AccessRole accessRole = find(scopeId, accessInfoId, accessRoleId);
            if (accessRole == null) {
                throw new EntityNotFoundException();
            }

            accessRoleService.delete(scopeId, accessRoleId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }
}
