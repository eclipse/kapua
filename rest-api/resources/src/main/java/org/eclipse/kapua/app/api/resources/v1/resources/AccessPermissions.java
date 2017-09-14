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
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionQuery;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionPredicates;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * {@link AccessPermission} REST API resource.
 *
 * @since 1.0.0
 */
@Api(value = "Access Info", authorizations = { @Authorization(value = "kapuaAccessToken") })
@Path("{scopeId}/accessinfos/{accessInfoId}/permissions")
public class AccessPermissions extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccessPermissionService accessPermissionService = locator.getService(AccessPermissionService.class);
    private final AccessPermissionFactory accessPermissionFactory = locator.getFactory(AccessPermissionFactory.class);

    /**
     * Gets the {@link AccessPermission} list in the scope.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param accessInfoId
     *            The optional {@link AccessInfo} id to filter results.
     * @param offset
     *            The result set offset.
     * @param limit
     *            The result set limit.
     * @return The {@link AccessPermissionListResult} of all the {@link AccessPermission}s associated to the current selected scope.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "accessPermissionSimpleQuery", value = "Gets the AccessPermission list in the scope", notes = "Gets the AccessPermission list in the scope. The query parameter accessInfoId is optional and can be used to filter results", response = AccessPermissionListResult.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccessPermissionListResult simpleQuery(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The optional id to filter results.") @PathParam("accessInfoId") EntityId accessInfoId,
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,
            @ApiParam(value = "The result set limit.", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        AccessPermissionQuery query = accessPermissionFactory.newQuery(scopeId);

        query.setPredicate(new AttributePredicate<>(AccessPermissionPredicates.ACCESS_INFO_ID, accessInfoId));

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, accessInfoId, query);
    }

    /**
     * Queries the {@link AccessPermission}s with the given {@link AccessPermissionQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param accessInfoId
     *            The {@link AccessInfo} id in which to search results.
     * @param query
     *            The {@link AccessPermissionQuery} to use to filter results.
     * @return The {@link AccessPermissionListResult} of all the result matching the given {@link AccessPermissionQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "accessPermissionQuery", value = "Queries the AccessPermissions", notes = "Queries the AccessPermissions with the given AccessPermissionQuery parameter returning all matching AccessPermissions", response = AccessPermissionListResult.class)
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public AccessPermissionListResult query(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The AccessInfo id in which to search results.") @PathParam("accessInfoId") EntityId accessInfoId,
            @ApiParam(value = "The AccessPermissionQuery to use to filter results.", required = true) AccessPermissionQuery query) throws Exception {

        query.setScopeId(scopeId);

        query.setPredicate(new AttributePredicate<>(AccessPermissionPredicates.ACCESS_INFO_ID, accessInfoId));

        return accessPermissionService.query(query);
    }

    /**
     * Counts the {@link AccessPermission}s with the given {@link AccessPermissionQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to count results.
     * @param accessInfoId
     *            The {@link AccessInfo} id in which to count results.
     * @param query
     *            The {@link AccessPermissionQuery} to use to filter count results.
     * @return The count of all the result matching the given {@link AccessPermissionQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "accessPermissionCount", value = "Counts the AccessPermissions", notes = "Counts the AccessPermissions with the given AccessPermissionQuery parameter returning the number of matching AccessPermissions", response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(
            @ApiParam(value = "The ScopeId in which to count results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The AccessInfo id in which to count results.") @PathParam("accessInfoId") EntityId accessInfoId,
            @ApiParam(value = "The AccessPermissionQuery to use to filter count results", required = true) AccessPermissionQuery query) throws Exception {
        query.setScopeId(scopeId);

        query.setPredicate(new AttributePredicate<>(AccessPermissionPredicates.ACCESS_INFO_ID, accessInfoId));

        return new CountResult(accessPermissionService.count(query));
    }

    /**
     * Creates a new {@link AccessPermission} based on the information provided in {@link AccessPermissionCreator}
     * parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to create the AccessPermission.
     * @param accessInfoId
     *            The {@link AccessInfo} id in which to create the AccessPermission.
     * @param accessPermissionCreator
     *            Provides the information for the new {@link AccessPermission} to be created.
     * @return The newly created {@link AccessPermission} object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "accessPermissionCreate", value = "Create an AccessPermission", notes = "Creates a new AccessPermission based on the information provided in AccessPermissionCreator parameter.", response = AccessPermission.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public AccessPermission create(
            @ApiParam(value = "The ScopeId in which to create the AccessPermission", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The AccessInfo id in which to create the AccessPermission.", required = true) @PathParam("accessInfoId") EntityId accessInfoId,
            @ApiParam(value = "Provides the information for the new AccessPermission to be created", required = true) AccessPermissionCreator accessPermissionCreator) throws Exception {
        accessPermissionCreator.setScopeId(scopeId);
        accessPermissionCreator.setAccessInfoId(accessInfoId);

        return accessPermissionService.create(accessPermissionCreator);
    }

    /**
     * Returns the AccessPermission specified by the "accessPermissionId" path parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the requested {@link AccessPermission}.
     * @param accessInfoId
     *            The {@link AccessInfo} id of the requested {@link AccessPermission}.
     * @param accessPermissionId
     *            The id of the requested AccessPermission.
     * @return The requested AccessPermission object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "accessPermissionFind", value = "Get an AccessPermission", notes = "Returns the AccessPermission specified by the \"accessPermissionId\" path parameter.", response = AccessPermission.class)
    @GET
    @Path("{accessPermissionId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccessPermission find(
            @ApiParam(value = "The ScopeId of the requested AccessPermission.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Specifies the AccessPermissionId for the requested AccessPermission", required = true) @PathParam("accessInfoId") EntityId accessInfoId,
            @ApiParam(value = "The id of the requested AccessPermission", required = true) @PathParam("accessPermissionId") EntityId accessPermissionId) throws Exception {
        AccessPermissionQuery query = accessPermissionFactory.newQuery(scopeId);

        AndPredicate andPredicate = new AndPredicate();
        andPredicate.and(new AttributePredicate<>(AccessPermissionPredicates.ACCESS_INFO_ID, accessInfoId));
        andPredicate.and(new AttributePredicate<>(AccessPermissionPredicates.ENTITY_ID, accessPermissionId));

        query.setPredicate(andPredicate);
        query.setOffset(0);
        query.setLimit(1);

        AccessPermissionListResult results = accessPermissionService.query(query);

        if (results.isEmpty()) {
            throw new KapuaEntityNotFoundException(AccessPermission.TYPE, accessPermissionId);
        }

        return results.getFirstItem();
    }

    /**
     * Deletes the {@link AccessPermission} specified by the "accessPermissionId" path parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link AccessPermission} to delete.
     * @param accessInfoId
     *            The {@link AccessInfo} id of the {@link AccessPermission} to delete.
     * @param accessPermissionId
     *            The id of the AccessPermission to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "accessPermissionDelete", value = "Delete an AccessPermission", notes = "Deletes the AccessPermission specified by the \"accessPermissionId\" path parameter.")
    @DELETE
    @Path("{accessPermissionId}")
    public Response deleteAccessPermission(
            @ApiParam(value = "The ScopeId of the AccessPermission to delete.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Specifies the AccessInfo Id for the requested AccessPermission", required = true) @PathParam("accessInfoId") EntityId accessInfoId,
            @ApiParam(value = "The id of the AccessPermission to be deleted", required = true) @PathParam("accessPermissionId") EntityId accessPermissionId) throws Exception {
        accessPermissionService.delete(scopeId, accessPermissionId);

        return returnOk();
    }
}
