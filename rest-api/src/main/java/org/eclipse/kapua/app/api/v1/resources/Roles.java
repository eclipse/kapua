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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.api.v1.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.app.api.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.role.shiro.RoleImpl;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.internal.UserImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Roles")
@Path("{scopeId}/roles")
public class Roles extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final RoleService roleService = locator.getService(RoleService.class);
    private final RoleFactory roleFactory = locator.getFactory(RoleFactory.class);
    private final RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
    private final RolePermissionFactory rolePermissionFactory = locator.getFactory(RolePermissionFactory.class);

    /**
     * Gets the {@link Role} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param offset The result set offset.
     * @param limit The result set limit.
     * @return The {@link RoleListResult} of all the roles associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the Role list in the scope", //
            notes = "Returns the list of all the roles associated to the current selected scope.", //
            response = Role.class, //
            responseContainer = "RoleListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RoleListResult simpleQuery(  @PathParam("scopeId") ScopeId scopeId,//
                                        @QueryParam("offset") @DefaultValue("0") int offset,//
                                        @QueryParam("limit") @DefaultValue("50") int limit) //
    {
        RoleListResult roleListResult = roleFactory.newRoleListResult();
        try {
            RoleQuery query = roleFactory.newQuery(scopeId);
            query.setOffset(offset);
            query.setLimit(limit);
            
            roleListResult = query(scopeId, query);
        } catch (Throwable t) {
            handleException(t);
        }
        return roleListResult;
    }
    
    /**
     * Queries the results with the given {@link RoleQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to search results. 
     * @param query The {@link RoleQuery} to used to filter results.
     * @return The {@link RoleListResult} of all the result matching the given {@link RoleQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public RoleListResult query(@PathParam("scopeId") ScopeId scopeId, RoleQuery query) {
        RoleListResult roleListResult = null;
        try {
            query.setScopeId(scopeId);
            roleListResult = roleService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(roleListResult);
    }
    

    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(@PathParam("scopeId") ScopeId scopeId, RoleQuery query) {
        CountResult countResult = null;
        try {
            query.setScopeId(scopeId);
            countResult = new CountResult(roleService.count(query));
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(countResult);
    }
    
    /**
     * Creates a new Role based on the information provided in RoleCreator
     * parameter.
     *
     * @param roleCreator
     *            Provides the information for the new Role to be created.
     * @return The newly created Role object.
     */
    @ApiOperation(value = "Create an Role", notes = "Creates a new Role based on the information provided in RoleCreator parameter.", response = Role.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Role create(@PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Provides the information for the new Role to be created", required = true) RoleCreator roleCreator) {
        Role role = null;
        try {
            roleCreator.setScopeId(scopeId);
            role = roleService.create(roleCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(role);
    }

    /**
     * Returns the Role specified by the "roleId" path parameter.
     *
     * @param roleId
     *            The id of the requested Role.
     * @return The requested Role object.
     */
    @ApiOperation(value = "Get an Role", notes = "Returns the Role specified by the \"roleId\" path parameter.", response = Role.class)
    @GET
    @Path("{roleId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Role find(@PathParam("scopeId") ScopeId scopeId, 
            @ApiParam(value = "The id of the requested Role", required = true) @PathParam("roleId") EntityId roleId) {
        Role role = null;
        try {
            role = roleService.find(scopeId, roleId);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(role);
    }
    
    /**
     * Updates the Role based on the information provided in the Role parameter.
     *
     * @param role
     *            The modified Role whose attributed need to be updated.
     * @return The updated role.
     */
    @ApiOperation(value = "Update an Role", notes = "Updates a new Role based on the information provided in the Role parameter.", response = Role.class)
    @PUT
    @Path("{roleId}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Role update(@PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId,
            @ApiParam(value = "The modified Role whose attributed need to be updated", required = true) Role role) {
        Role roleUpdated = null;
        try {
            ((RoleImpl) role).setScopeId(scopeId);
            role.setId(roleId);
            
            roleUpdated = roleService.update(role);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(roleUpdated);
    }

    /**
     * Deletes the Role specified by the "roleId" path parameter.
     *
     * @param roleId
     *            The id of the Role to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     */
    @ApiOperation(value = "Delete an Role", notes = "Deletes the Role specified by the \"roleId\" path parameter.")
    @DELETE
    @Path("{roleId}")
    public Response deleteRole(@PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Role to be deleted", required = true) @PathParam("roleId") EntityId roleId) {
        try {
            roleService.delete(scopeId, roleId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }
}
