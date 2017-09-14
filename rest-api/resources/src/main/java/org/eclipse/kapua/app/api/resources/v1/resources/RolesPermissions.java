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
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionQuery;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionPredicates;

import com.google.common.base.Strings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "Roles", authorizations = { @Authorization(value = "kapuaAccessToken") })
@Path("{scopeId}/roles/{roleId}/permissions")
public class RolesPermissions extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
    private final RolePermissionFactory rolePermissionFactory = locator.getFactory(RolePermissionFactory.class);

    /**
     * Gets the {@link RolePermission} list in the scope.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param roleId
     *            The id of the {@link Role} in which to search results.
     * @param domain
     *            The domain name to filter results.
     * @param action
     *            The action to filter results.
     * @param offset
     *            The result set offset.
     * @param limit
     *            The result set limit.
     * @return The {@link RolePermissionListResult} of all the rolePermissions associated to the current selected scope.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "rolePermissionSimpleQuery", value = "Gets the RolePermission list in the scope", notes = "Returns the list of all the rolePermissions associated to the current selected scope.", response = RolePermissionListResult.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RolePermissionListResult simpleQuery(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Role to filter results.") @PathParam("roleId") EntityId roleId,
            @ApiParam(value = "The domain name to filter results.") @QueryParam("name") String domain,
            @ApiParam(value = "The action to filter results.") @QueryParam("action") Actions action,
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,
            @ApiParam(value = "The result set limit.", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        RolePermissionQuery query = rolePermissionFactory.newQuery(scopeId);

        AndPredicate andPredicate = new AndPredicate();
        query.setPredicate(new AttributePredicate<>(RolePermissionPredicates.ROLE_ID, roleId));
        if (!Strings.isNullOrEmpty(domain)) {
            andPredicate.and(new AttributePredicate<>(RolePermissionPredicates.DOMAIN, domain));
        }
        if (action != null) {
            andPredicate.and(new AttributePredicate<>(RolePermissionPredicates.ACTION, action));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, roleId, query);
    }

    /**
     * Queries the results with the given {@link RolePermissionQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param roleId
     *            The {@link Role} id in which to search results.
     * @param query
     *            The {@link RolePermissionQuery} to use to filter results.
     * @return The {@link RolePermissionListResult} of all the result matching the given {@link RolePermissionQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "rolePermissionQuery", value = "Queries the RolePermissions", notes = "Queries the RolePermissions with the given RolePermissionQuery parameter returning all matching RolePermissions", response = RolePermissionListResult.class)
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public RolePermissionListResult query(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The Role id in which to search results.") @PathParam("roleId") EntityId roleId,
            @ApiParam(value = "The RolePermissionQuery to use to filter results.", required = true) RolePermissionQuery query) throws Exception {
        query.setScopeId(scopeId);

        AndPredicate andPredicate = new AndPredicate();
        andPredicate.and(new AttributePredicate<>(RolePermissionPredicates.ROLE_ID, roleId));
        andPredicate.and(query.getPredicate());

        query.setPredicate(andPredicate);

        return rolePermissionService.query(query);
    }

    /**
     * Counts the results with the given {@link RolePermissionQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to count results.
     * @param roleId
     *            The {@link Role} id in which to count results.
     * @param query
     *            The {@link RolePermissionQuery} to use to filter results.
     * @return The count of all the result matching the given {@link RolePermissionQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "rolePermissionCount", value = "Counts the RolePermissions", notes = "Counts the RolePermissions with the given RolePermissionQuery parameter returning the number of matching RolePermissions", response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(
            @ApiParam(value = "The ScopeId in which to count results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The Role id in which to count results.") @PathParam("roleId") EntityId roleId,
            @ApiParam(value = "The RolePermissionQuery to use to filter results.", required = true) RolePermissionQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(new AttributePredicate<>(RolePermissionPredicates.ROLE_ID, roleId));

        return new CountResult(rolePermissionService.count(query));
    }

    /**
     * Creates a new RolePermission based on the information provided in RolePermissionCreator
     * parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to create the {@link RolePermission}
     * @param roleId
     *            The {@link Role} id in which to create the RolePermission.
     * @param rolePermissionCreator
     *            Provides the information for the new RolePermission to be created.
     * @return The newly created RolePermission object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "rolePermissionCreate", value = "Create a RolePermission", notes = "Creates a new RolePermission based on the information provided in RolePermissionCreator parameter.", response = RolePermission.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public RolePermission create(
            @ApiParam(value = "The ScopeId in which to create the RolePermission", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The Role id in which to create the RolePermission.", required = true) @PathParam("roleId") EntityId roleId,
            @ApiParam(value = "Provides the information for the new RolePermission to be created", required = true) RolePermissionCreator rolePermissionCreator) throws Exception {
        rolePermissionCreator.setScopeId(scopeId);
        rolePermissionCreator.setRoleId(roleId);

        return rolePermissionService.create(rolePermissionCreator);
    }

    /**
     * Returns the RolePermission specified by the "rolePermissionId" path parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the requested {@link RolePermission}.
     * @param roleId
     *            The {@link Role} id of the requested {@link RolePermission}.
     * @param rolePermissionId
     *            The id of the requested RolePermission.
     * @return The requested RolePermission object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "rolePermissionFind", value = "Get a RolePermission", notes = "Returns the RolePermission specified by the \"rolePermissionId\" path parameter.", response = RolePermission.class)
    @GET
    @Path("{rolePermissionId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RolePermission find(
            @ApiParam(value = "The ScopeId of the requested RolePermission.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Specifies the RoleId for the requested RolePermission", required = true) @PathParam("roleId") EntityId roleId,
            @ApiParam(value = "The id of the requested RolePermission", required = true) @PathParam("rolePermissionId") EntityId rolePermissionId) throws Exception {
        RolePermissionQuery query = rolePermissionFactory.newQuery(scopeId);

        AndPredicate andPredicate = new AndPredicate();
        andPredicate.and(new AttributePredicate<>(RolePermissionPredicates.ROLE_ID, roleId));
        andPredicate.and(new AttributePredicate<>(RolePermissionPredicates.ENTITY_ID, rolePermissionId));

        query.setPredicate(andPredicate);
        query.setOffset(0);
        query.setLimit(1);

        RolePermissionListResult results = rolePermissionService.query(query);

        if (results.isEmpty()) {
            throw new KapuaEntityNotFoundException(RolePermission.TYPE, rolePermissionId);
        }

        return results.getFirstItem();
    }

    /**
     * Deletes the RolePermission specified by the "rolePermissionId" path parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link RolePermission} to delete.
     * @param roleId
     *            The {@link Role} id of the {@link RolePermission} to delete.
     * @param rolePermissionId
     *            The id of the RolePermission to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "rolePermissionDelete", value = "Delete an RolePermission", notes = "Deletes the RolePermission specified by the \"rolePermissionId\" path parameter.")
    @DELETE
    @Path("{rolePermissionId}")
    public Response deleteRolePermission(
            @ApiParam(value = "The ScopeId of the RolePermission to delete.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Specifies the Role Id for the requested RolePermission", required = true) @PathParam("roleId") EntityId roleId,
            @ApiParam(value = "The id of the RolePermission to be deleted", required = true) @PathParam("rolePermissionId") EntityId rolePermissionId) throws Exception {
        rolePermissionService.delete(scopeId, rolePermissionId);

        return returnOk();
    }
}
