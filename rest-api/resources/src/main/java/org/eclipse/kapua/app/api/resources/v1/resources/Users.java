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
import javax.ws.rs.PUT;
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
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.UserPredicates;

import com.google.common.base.Strings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "Users", authorizations = { @Authorization(value = "kapuaAccessToken") })
@Path("{scopeId}/users")
public class Users extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final UserService userService = locator.getService(UserService.class);
    private final UserFactory userFactory = locator.getFactory(UserFactory.class);

    /**
     * Gets the {@link User} list in the scope.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param name
     *            The {@link User} name in which to search results.
     * @param offset
     *            The result set offset.
     * @param limit
     *            The result set limit.
     * @return The {@link UserListResult} of all the users associated to the current selected scope.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "userSimpleQuery", value = "Gets the User list in the scope", notes = "Returns the list of all the users associated to the current selected scope.", response = UserListResult.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public UserListResult simpleQuery(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The user name to filter results.") @QueryParam("name") String name,
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,
            @ApiParam(value = "The result set limit.", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        UserQuery query = userFactory.newQuery(scopeId);

        AndPredicate andPredicate = new AndPredicate();
        if (!Strings.isNullOrEmpty(name)) {
            andPredicate.and(new AttributePredicate<>(UserPredicates.NAME, name));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link UserQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link UserQuery} to use to filter results.
     * @return The {@link UserListResult} of all the result matching the given {@link UserQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "userQuery", value = "Queries the Users", notes = "Queries the Users with the given UserQuery parameter returning all matching Users", response = UserListResult.class)
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public UserListResult query(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The UserQuery to use to filter results.", required = true) UserQuery query) throws Exception {
        query.setScopeId(scopeId);

        return userService.query(query);
    }

    /**
     * Counts the results with the given {@link UserQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to count results.
     * @param query
     *            The {@link UserQuery} to use to filter results.
     * @return The count of all the result matching the given {@link UserQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "userCount", value = "Counts the Users", notes = "Counts the Users with the given UserQuery parameter returning the number of matching Users", response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(
            @ApiParam(value = "The ScopeId in which to count results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The UserQuery to use to filter count results", required = true) UserQuery query) throws Exception {
        query.setScopeId(scopeId);

        return new CountResult(userService.count(query));
    }

    /**
     * Creates a new User based on the information provided in UserCreator
     * parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to create the {@link User}
     * @param userCreator
     *            Provides the information for the new User to be created.
     * @return The newly created User object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "userCreate", value = "Create an User", notes = "Creates a new User based on the information provided in UserCreator parameter.", response = User.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public User create(
            @ApiParam(value = "The ScopeId in which to create the User", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Provides the information for the new User to be created", required = true) UserCreator userCreator) throws Exception {
        userCreator.setScopeId(scopeId);

        return userService.create(userCreator);
    }

    /**
     * Returns the User specified by the "userId" path parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the requested {@link User}.
     * @param userId
     *            The id of the requested User.
     * @return The requested User object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "userFind", value = "Get an User", notes = "Returns the User specified by the \"userId\" path parameter.", response = User.class)
    @GET
    @Path("{userId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public User find(
            @ApiParam(value = "The ScopeId of the requested User.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested User", required = true) @PathParam("userId") EntityId userId) throws Exception {
        User user = userService.find(scopeId, userId);

        if (user == null) {
            throw new KapuaEntityNotFoundException(User.TYPE, userId);
        }

        return user;
    }

    /**
     * Updates the User based on the information provided in the User parameter.
     *
     * @param scopeId
     *            The ScopeId of the requested {@link User}.
     * @param userId
     *            The id of the requested {@link User}
     * @param user
     *            The modified User whose attributed need to be updated.
     * @return The updated user.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "userUpdate", value = "Update an User", notes = "Updates a new User based on the information provided in the User parameter.", response = User.class)
    @PUT
    @Path("{userId}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public User update(
            @ApiParam(value = "The ScopeId of the requested User.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested User", required = true) @PathParam("userId") EntityId userId,
            @ApiParam(value = "The modified User whose attributes needs to be updated", required = true) User user) throws Exception {
        user.setScopeId(scopeId);
        user.setId(userId);

        return userService.update(user);
    }

    /**
     * Deletes the User specified by the "userId" path parameter.
     *
     * @param scopeId
     *            The ScopeId of the requested {@link User}.
     * @param userId
     *            The id of the User to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "userDelete", value = "Delete an User", notes = "Deletes the User specified by the \"userId\" path parameter.")
    @DELETE
    @Path("{userId}")
    public Response deleteUser(
            @ApiParam(value = "The ScopeId of the User to delete.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the User to be deleted", required = true) @PathParam("userId") EntityId userId) throws Exception {
        userService.delete(scopeId, userId);

        return returnOk();
    }
}
