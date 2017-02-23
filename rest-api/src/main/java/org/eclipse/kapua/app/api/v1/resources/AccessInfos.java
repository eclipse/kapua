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
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessPermission;
import org.eclipse.kapua.service.authorization.access.AccessPermissionCreator;
import org.eclipse.kapua.service.authorization.access.AccessPermissionFactory;
import org.eclipse.kapua.service.authorization.access.AccessPermissionListResult;
import org.eclipse.kapua.service.authorization.access.AccessPermissionService;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Access Info")
@Path("{scopeId}/accessinfos")
public class AccessInfos extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
    private final AccessInfoFactory accessInfoFactory = locator.getFactory(AccessInfoFactory.class);
//    private final AccessPermissionService accessPermissionService = locator.getService(AccessPermissionService.class);
//    private final AccessPermissionFactory accessPermissionFactory = locator.getFactory(AccessPermissionFactory.class);
//    private final AccessRoleService accessRoleService = locator.getService(AccessRoleService.class);
//    private final AccessRoleFactory accessRoleFactory = locator.getFactory(AccessRoleFactory.class);

    /**
     * Gets the {@link AccessInfo} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param offset The result set offset.
     * @param limit The result set limit.
     * @return The {@link AccessInfoListResult} of all the accessInfos associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the AccessInfo list in the scope", //
            notes = "Returns the list of all the accessInfos associated to the current selected scope.", //
            response = AccessInfo.class, //
            responseContainer = "AccessInfoListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccessInfoListResult simpleQuery(  @PathParam("scopeId") ScopeId scopeId,//
                                        @QueryParam("offset") @DefaultValue("0") int offset,//
                                        @QueryParam("limit") @DefaultValue("50") int limit) //
    {
        AccessInfoListResult accessInfoListResult = accessInfoFactory.newAccessInfoListResult();
        try {
            AccessInfoQuery query = accessInfoFactory.newQuery(scopeId);
            query.setOffset(offset);
            query.setLimit(limit);
            
            accessInfoListResult = query(scopeId, query);
        } catch (Throwable t) {
            handleException(t);
        }
        return accessInfoListResult;
    }

    /**
     * Queries the results with the given {@link AccessInfoQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to search results. 
     * @param query The {@link AccessInfoQuery} to used to filter results.
     * @return The {@link AccessInfoListResult} of all the result matching the given {@link AccessInfoQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public AccessInfoListResult query(@PathParam("scopeId") ScopeId scopeId, AccessInfoQuery query) {
        AccessInfoListResult accessInfoListResult = null;
        try {
            query.setScopeId(scopeId);
            accessInfoListResult = accessInfoService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(accessInfoListResult);
    }
    

    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(@PathParam("scopeId") ScopeId scopeId, AccessInfoQuery query) {
        CountResult countResult = null;
        try {
            query.setScopeId(scopeId);
            countResult = new CountResult(accessInfoService.count(query));
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(countResult);
    }
    
    /**
     * Creates a new AccessInfo based on the information provided in AccessInfoCreator
     * parameter.
     *
     * @param accessInfoCreator
     *            Provides the information for the new AccessInfo to be created.
     * @return The newly created AccessInfo object.
     */
    @ApiOperation(value = "Create an AccessInfo", notes = "Creates a new AccessInfo based on the information provided in AccessInfoCreator parameter.", response = AccessInfo.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public AccessInfo create(@PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Provides the information for the new AccessInfo to be created", required = true) AccessInfoCreator accessInfoCreator) {
        AccessInfo accessInfo = null;
        try {
            accessInfoCreator.setScopeId(scopeId);
            accessInfo = accessInfoService.create(accessInfoCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(accessInfo);
    }

    /**
     * Returns the AccessInfo specified by the "accessInfoId" path parameter.
     *
     * @param accessInfoId
     *            The id of the requested AccessInfo.
     * @return The requested AccessInfo object.
     */
    @ApiOperation(value = "Get an AccessInfo", notes = "Returns the AccessInfo specified by the \"accessInfoId\" path parameter.", response = AccessInfo.class)
    @GET
    @Path("{accessInfoId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public AccessInfo find(@PathParam("scopeId") ScopeId scopeId, 
            @ApiParam(value = "The id of the requested AccessInfo", required = true) @PathParam("accessInfoId") EntityId accessInfoId) {
        AccessInfo accessInfo = null;
        try {
            accessInfo = accessInfoService.find(scopeId, accessInfoId);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(accessInfo);
    }
    
    /**
     * Deletes the AccessInfo specified by the "accessInfoId" path parameter.
     *
     * @param accessInfoId
     *            The id of the AccessInfo to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     */
    @ApiOperation(value = "Delete an AccessInfo", notes = "Deletes the AccessInfo specified by the \"accessInfoId\" path parameter.")
    @DELETE
    @Path("{accessInfoId}")
    public Response deleteAccessInfo(@PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the AccessInfo to be deleted", required = true) @PathParam("accessInfoId") EntityId accessInfoId) {
        try {
            accessInfoService.delete(scopeId, accessInfoId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }
}
