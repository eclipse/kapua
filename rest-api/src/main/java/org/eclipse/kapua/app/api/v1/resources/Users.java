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
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.UserImpl;
import org.eclipse.kapua.service.user.internal.UserQueryImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Users")
@Path("{scopeId}/users")
public class Users extends AbstractKapuaResource {
    
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final UserService userService = locator.getService(UserService.class);
    private final UserFactory userFactory = locator.getFactory(UserFactory.class);

    /**
     * Gets the {@link User} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param offset The result set offset.
     * @param limit The result set limit.
     * @return The {@link UserListResult} of all the users associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the User list in the scope", //
            notes = "Returns the list of all the users associated to the current selected scope.", //
            response = User.class, //
            responseContainer = "UserListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public UserListResult simpleQuery(  @PathParam("scopeId") ScopeId scopeId,//
                                        @QueryParam("offset") @DefaultValue("0") int offset,//
                                        @QueryParam("limit") @DefaultValue("50") int limit) //
    {
        UserListResult userListResult = userFactory.newUserListResult();
        try {
            UserQuery query = userFactory.newQuery(scopeId);
            query.setOffset(offset);
            query.setLimit(limit);
            
            userListResult = query(scopeId, query);
        } catch (Throwable t) {
            handleException(t);
        }
        return userListResult;
    }

    /**
     * Queries the results with the given {@link UserQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to search results. 
     * @param query The {@link UserQuery} to used to filter results.
     * @return The {@link UserListResult} of all the result matching the given {@link UserQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public UserListResult query(@PathParam("scopeId") ScopeId scopeId, UserQuery query) {
        UserListResult userListResult = null;
        try {
            query.setScopeId(scopeId);
            userListResult = userService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(userListResult);
    }
    

    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(@PathParam("scopeId") ScopeId scopeId, UserQuery query) {
        CountResult countResult = null;
        try {
            query.setScopeId(scopeId);
            countResult = new CountResult(userService.count(query));
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(countResult);
    }
    
    /**
     * Creates a new User based on the information provided in UserCreator
     * parameter.
     *
     * @param userCreator
     *            Provides the information for the new User to be created.
     * @return The newly created User object.
     */
    @ApiOperation(value = "Create an User", notes = "Creates a new User based on the information provided in UserCreator parameter.", response = User.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public User create(@PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Provides the information for the new User to be created", required = true) UserCreator userCreator) {
        User user = null;
        try {
            userCreator.setScopeId(scopeId);
            user = userService.create(userCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(user);
    }

    /**
     * Returns the User specified by the "userId" path parameter.
     *
     * @param userId
     *            The id of the requested User.
     * @return The requested User object.
     */
    @ApiOperation(value = "Get an User", notes = "Returns the User specified by the \"userId\" path parameter.", response = User.class)
    @GET
    @Path("{userId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public User find(@PathParam("scopeId") ScopeId scopeId, 
            @ApiParam(value = "The id of the requested User", required = true) @PathParam("userId") EntityId userId) {
        User user = null;
        try {
            user = userService.find(scopeId, userId);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(user);
    }
    
    /**
     * Updates the User based on the information provided in the User parameter.
     *
     * @param user
     *            The modified User whose attributed need to be updated.
     * @return The updated user.
     */
    @ApiOperation(value = "Update an User", notes = "Updates a new User based on the information provided in the User parameter.", response = User.class)
    @PUT
    @Path("{userId}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public User update(@PathParam("scopeId") ScopeId scopeId,
            @PathParam("userId") EntityId userId,
            @ApiParam(value = "The modified User whose attributed need to be updated", required = true) User user) {
        User userUpdated = null;
        try {
            ((UserImpl) user).setScopeId(scopeId);
            user.setId(userId);
            
            userUpdated = userService.update(user);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(userUpdated);
    }

    /**
     * Deletes the User specified by the "userId" path parameter.
     *
     * @param userId
     *            The id of the User to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     */
    @ApiOperation(value = "Delete an User", notes = "Deletes the User specified by the \"userId\" path parameter.")
    @DELETE
    @Path("{userId}")
    public Response deleteUser(@PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the User to be deleted", required = true) @PathParam("userId") EntityId userId) {
        try {
            userService.delete(scopeId, userId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }
}
