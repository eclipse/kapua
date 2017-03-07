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
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionQuery;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.access.shiro.AccessPermissionPredicates;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.internal.UserImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * {@link AccessPermission} REST API resource.
 * 
 * @since 1.0.0
 */
@Api("Access Info")
@Path("{scopeId}/accessinfos/{accessInfoId}/permissions")
public class AccessPermissions extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccessPermissionService accessPermissionService = locator.getService(AccessPermissionService.class);
    private final AccessPermissionFactory accessPermissionFactory = locator.getFactory(AccessPermissionFactory.class);

    /**
     * Gets the {@link AccessPermission} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param accessInfoId
     *            The optional {@link AccessInfo} id to filter results.
     * @param offset The result set offset.
     * @param limit The result set limit.
     * @return The {@link AccessPermissionListResult} of all the {@link AccessPermission}s associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the AccessPermission list in the scope", //
            notes = "Returns the list of all the accessPermissions associated to the current selected scope.", //
            response = AccessPermission.class, //
            responseContainer = "AccessPermissionListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccessPermissionListResult simpleQuery(  @PathParam("scopeId") ScopeId scopeId,//
                                        @PathParam("accessInfoId") EntityId accessInfoId,//
                                        @QueryParam("offset") @DefaultValue("0") int offset,//
                                        @QueryParam("limit") @DefaultValue("50") int limit) //
    {
        AccessPermissionListResult accessPermissionListResult = accessPermissionFactory.newAccessPermissionListResult();
        try {
            AccessPermissionQuery query = accessPermissionFactory.newQuery(scopeId);
            query.setPredicate(new AttributePredicate<>(AccessPermissionPredicates.ACCESS_INFO_ID, accessInfoId));
            query.setOffset(offset);
            query.setLimit(limit);
            
            accessPermissionListResult = query(scopeId, accessInfoId, query);
        } catch (Throwable t) {
            handleException(t);
        }
        return accessPermissionListResult;
    }
    
    /**
     * Queries the {@link AccessPermission}s with the given {@link AccessPermissionQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param accessInfoId The  {@link AccessInfo} id in which to search results. 
     * @param query The {@link AccessPermissionQuery} to used to filter results.
     * @return The {@link AccessPermissionListResult} of all the result matching the given {@link AccessPermissionQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public AccessPermissionListResult query(@PathParam("scopeId") ScopeId scopeId,
            @PathParam("accessInfoId") EntityId accessInfoId,//
            AccessPermissionQuery query) {
        AccessPermissionListResult accessPermissionListResult = null;
        try {
            query.setScopeId(scopeId);
            query.setPredicate(new AttributePredicate<>(AccessPermissionPredicates.ACCESS_INFO_ID, accessInfoId));
            accessPermissionListResult = accessPermissionService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(accessPermissionListResult);
    }
    
    /**
     * Counts the {@link AccessPermission}s with the given {@link AccessPermissionQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to count results. 
     * @param accessInfoId The  {@link AccessInfo} id in which to search results.
     * @param query The {@link AccessPermissionQuery} to used to filter count results.
     * @return The count of all the result matching the given {@link AccessPermissionQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(@PathParam("scopeId") ScopeId scopeId, 
            @PathParam("accessInfoId") EntityId accessInfoId,//
            AccessPermissionQuery query) {
        CountResult countResult = null;
        try {
            query.setScopeId(scopeId);
            query.setPredicate(new AttributePredicate<>(AccessPermissionPredicates.ACCESS_INFO_ID, accessInfoId));
            countResult = new CountResult(accessPermissionService.count(query));
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(countResult);
    }
    
    /**
     * Creates a new {@link AccessPermission} based on the information provided in {@link AccessPermissionCreator}
     * parameter.
     *
     *@param scopeId The {@link ScopeId} in which to count results. 
     * @param accessInfoId The  {@link AccessInfo} id in which to search results.
     * @param accessPermissionCreator
     *            Provides the information for the new {@link AccessPermission} to be created.
     * @return The newly created {@link AccessPermission} object.
     */
    @ApiOperation(value = "Create an AccessPermission", notes = "Creates a new AccessPermission based on the information provided in AccessPermissionCreator parameter.", response = AccessPermission.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public AccessPermission create(@PathParam("scopeId") ScopeId scopeId,
            @PathParam("accessInfoId") EntityId accessInfoId,//
            @ApiParam(value = "Provides the information for the new AccessPermission to be created", required = true) AccessPermissionCreator accessPermissionCreator) {
        AccessPermission accessPermission = null;
        try {
            accessPermissionCreator.setScopeId(scopeId);
            accessPermissionCreator.setAccessInfoId(accessInfoId);
            accessPermission = accessPermissionService.create(accessPermissionCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(accessPermission);
    }

    /**
     * Returns the AccessPermission specified by the "accessPermissionId" path parameter.
     *
     *@param scopeId The {@link ScopeId} of the requested {@link AccessPermission}.
     *@param accessInfoId The {@link AccessInfo} id of the requested {@link AccessPermission}.
     * @param accessPermissionId
     *            The id of the requested AccessPermission.
     * @return The requested AccessPermission object.
     */
    @ApiOperation(value = "Get an AccessPermission", notes = "Returns the AccessPermission specified by the \"accessPermissionId\" path parameter.", response = AccessPermission.class)
    @GET
    @Path("{accessPermissionId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccessPermission find(@PathParam("scopeId") ScopeId scopeId, 
            @PathParam("accessInfoId") EntityId accessInfoId,//
            @ApiParam(value = "The id of the requested AccessPermission", required = true) @PathParam("accessPermissionId") EntityId accessPermissionId) {
        AccessPermission accessPermission = null;
        try {
            AccessPermissionQuery query = accessPermissionFactory.newQuery(scopeId);
            
            AndPredicate andPredicate = new AndPredicate();
            andPredicate.and(new AttributePredicate<>(AccessPermissionPredicates.ACCESS_INFO_ID, accessInfoId));
            andPredicate.and(new AttributePredicate<>(AccessPermissionPredicates.ENTITY_ID, accessPermissionId));
            
            query.setPredicate(andPredicate);
            query.setOffset(0);
            query.setLimit(1);
            
            AccessPermissionListResult results = accessPermissionService.query(query);
            
            if (!results.isEmpty()) {
                accessPermission = results.getFirstItem(); 
            }
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(accessPermission);
    }

    /**
     * Deletes the {@link AccessPermission} specified by the "accessPermissionId" path parameter.
     *
     * @param accessPermissionId
     *            The id of the AccessPermission to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     */
    @ApiOperation(value = "Delete an AccessPermission", notes = "Deletes the AccessPermission specified by the \"accessPermissionId\" path parameter.")
    @DELETE
    @Path("{accessPermissionId}")
    public Response deleteAccessPermission(@PathParam("scopeId") ScopeId scopeId,
            @PathParam("accessInfoId") EntityId accessInfoId,//
            @ApiParam(value = "The id of the AccessPermission to be deleted", required = true) @PathParam("accessPermissionId") EntityId accessPermissionId) {
        try {
            AccessPermission accessPermission = find(scopeId, accessInfoId, accessPermissionId);
            if (accessPermission == null) {
                throw new EntityNotFoundException();
            }
            
            accessPermissionService.delete(scopeId, accessPermissionId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }
}
