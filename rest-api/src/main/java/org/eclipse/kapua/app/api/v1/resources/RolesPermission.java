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

import javax.persistence.EntityNotFoundException;
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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
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
import org.eclipse.kapua.service.authorization.role.RolePermissionQuery;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.role.shiro.RoleImpl;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionPredicates;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.internal.UserImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Roles")
@Path("{scopeId}/roles/{roleId}/permissions")
public class RolesPermission extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
    private final RolePermissionFactory rolePermissionFactory = locator.getFactory(RolePermissionFactory.class);

    /**
     * Gets the {@link RolePermission} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param offset The result set offset.
     * @param limit The result set limit.
     * @return The {@link RolePermissionListResult} of all the rolePermissions associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the RolePermission list in the scope", //
            notes = "Returns the list of all the rolePermissions associated to the current selected scope.", //
            response = RolePermission.class, //
            responseContainer = "RolePermissionListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RolePermissionListResult simpleQuery(  @PathParam("scopeId") ScopeId scopeId,//
                                        @PathParam("roleId") EntityId roleId,//
                                        @QueryParam("offset") @DefaultValue("0") int offset,//
                                        @QueryParam("limit") @DefaultValue("50") int limit) //
    {
        RolePermissionListResult rolePermissionListResult = rolePermissionFactory.newRolePermissionListResult();
        try {
            RolePermissionQuery query = rolePermissionFactory.newQuery(scopeId);
            query.setPredicate(new AttributePredicate<>(RolePermissionPredicates.ROLE_ID, roleId));
            query.setOffset(offset);
            query.setLimit(limit);
            
            rolePermissionListResult = query(scopeId, roleId, query);
        } catch (Throwable t) {
            handleException(t);
        }
        return rolePermissionListResult;
    }
    
    /**
     * Queries the results with the given {@link RolePermissionQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to search results. 
     * @param query The {@link RolePermissionQuery} to used to filter results.
     * @return The {@link RolePermissionListResult} of all the result matching the given {@link RolePermissionQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public RolePermissionListResult query(@PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId,//
            RolePermissionQuery query) {
        RolePermissionListResult rolePermissionListResult = null;
        try {
            query.setScopeId(scopeId);
            query.setPredicate(new AttributePredicate<>(RolePermissionPredicates.ROLE_ID, roleId));
            rolePermissionListResult = rolePermissionService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(rolePermissionListResult);
    }
    

    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(@PathParam("scopeId") ScopeId scopeId, 
            @PathParam("roleId") EntityId roleId,//
            RolePermissionQuery query) {
        CountResult countResult = null;
        try {
            query.setScopeId(scopeId);
            query.setPredicate(new AttributePredicate<>(RolePermissionPredicates.ROLE_ID, roleId));
            countResult = new CountResult(rolePermissionService.count(query));
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(countResult);
    }
    
    /**
     * Creates a new RolePermission based on the information provided in RolePermissionCreator
     * parameter.
     *
     * @param rolePermissionCreator
     *            Provides the information for the new RolePermission to be created.
     * @return The newly created RolePermission object.
     */
    @ApiOperation(value = "Create an RolePermission", notes = "Creates a new RolePermission based on the information provided in RolePermissionCreator parameter.", response = RolePermission.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public RolePermission create(@PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId,//
            @ApiParam(value = "Provides the information for the new RolePermission to be created", required = true) RolePermissionCreator rolePermissionCreator) {
        RolePermission rolePermission = null;
        try {
            rolePermissionCreator.setScopeId(scopeId);
            rolePermissionCreator.setRoleId(roleId);
            rolePermission = rolePermissionService.create(rolePermissionCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(rolePermission);
    }

    /**
     * Returns the RolePermission specified by the "rolePermissionId" path parameter.
     *
     * @param rolePermissionId
     *            The id of the requested RolePermission.
     * @return The requested RolePermission object.
     */
    @ApiOperation(value = "Get an RolePermission", notes = "Returns the RolePermission specified by the \"rolePermissionId\" path parameter.", response = RolePermission.class)
    @GET
    @Path("{rolePermissionId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RolePermission find(@PathParam("scopeId") ScopeId scopeId, 
            @PathParam("roleId") EntityId roleId,//
            @ApiParam(value = "The id of the requested RolePermission", required = true) @PathParam("rolePermissionId") EntityId rolePermissionId) {
        RolePermission rolePermission = null;
        try {
            RolePermissionQuery query = rolePermissionFactory.newQuery(scopeId);
            
            AndPredicate andPredicate = new AndPredicate();
            andPredicate.and(new AttributePredicate<>(RolePermissionPredicates.ROLE_ID, roleId));
            andPredicate.and(new AttributePredicate<>(RolePermissionPredicates.ENTITY_ID, rolePermissionId));
            
            query.setOffset(0);
            query.setLimit(1);
            
            RolePermissionListResult results = rolePermissionService.query(query);
            
            if (!results.isEmpty()) {
                rolePermission = results.getFirstItem(); 
            }
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(rolePermission);
    }

    /**
     * Deletes the RolePermission specified by the "rolePermissionId" path parameter.
     *
     * @param rolePermissionId
     *            The id of the RolePermission to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     */
    @ApiOperation(value = "Delete an RolePermission", notes = "Deletes the RolePermission specified by the \"rolePermissionId\" path parameter.")
    @DELETE
    @Path("{rolePermissionId}")
    public Response deleteRolePermission(@PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId,//
            @ApiParam(value = "The id of the RolePermission to be deleted", required = true) @PathParam("rolePermissionId") EntityId rolePermissionId) {
        try {
            RolePermission rolePermission = find(scopeId, roleId, rolePermissionId);
            if (rolePermission == null) {
                throw new EntityNotFoundException();
            }
            
            rolePermissionService.delete(scopeId, rolePermissionId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }
}
