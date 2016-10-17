/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.UserImpl;

@Path("/users")
public class Users extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final UserService userService = locator.getService(UserService.class);
    private final UserFactory userFactory = locator.getFactory(UserFactory.class);

    /**
     * Returns the list of all the users associated to the account of the
     * currently connected user.
     *
     * @return The list of requested User objects.
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public UserListResult getUsers() {
        UserListResult userResult = userFactory.newUserListResult();
        try {
            UserQuery query = userFactory.newQuery(KapuaSecurityUtils.getSession().getScopeId());
            userResult = (UserListResult) userService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return userResult;
    }

    /**
     * Returns the User specified by the "userId" path parameter.
     *
     * @param userId
     *            The id of the User requested.
     * @return The requested User object.
     */
    @GET
    @Path("{userId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public User getAccount(@PathParam("userId") String userId) {
        User user = null;
        try {
            KapuaId id = KapuaEid.parseShortId(userId);
            user = userService.find(KapuaSecurityUtils.getSession().getScopeId(), id);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(user);
    }

    /**
     * Returns the User specified by the "username" query parameter.
     *
     * @param username
     *            The username of the User requested.
     * @return The requested User object.
     */
    @GET
    @Path("findByName")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public User getUserByName(@QueryParam("username") String username) {
        User user = null;
        try {
            user = userService.findByName(username);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(user);
    }

    /**
     * Unlock a User based on the userId provided in the path request.
     *
     * @param userId
     *            The userId that refer to the user to unlock.
     *
     * @return The user unlocked.
     */
    // FIXME see https://github.com/eurotech/kapua/issues/193
    // @POST
    // @Path("{userId}/unlock")
    // @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    // public User unlockUser(@PathParam("userId") long userId) {
    // User user = null;
    //
    // try {
    // long targetAccountId = getTargetAccountId();
    //
    // ServiceLocator sl = ServiceLocator.getInstance();
    // UserService us = sl.getUserService();
    // user = us.unlockUser(targetAccountId, userId);
    // user = us.findWithAccessInfo(targetAccountId, userId);
    // } catch (Throwable t) {
    // handleException(t);
    // }
    // return returnNotNullEntity(user);
    // }

    /**
     * Creates a new User based on the information provided in UserCreator
     * parameter.
     *
     * @param userCreator
     *            Provides the information for the new User to be created.
     * @return The newly created User object.
     */
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public User postUser(UserCreator userCreator) {
        User user = null;
        try {
            userCreator.setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            user = userService.create(userCreator);
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
    @PUT
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public User putUser(User user) {
        User userUpdated = null;
        try {
            ((UserImpl)user).setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            userUpdated = userService.update(user);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(userUpdated);
    }

    /**
     * Deletes the User specified by the "id" path parameter.
     *
     * @param userId
     *            The id of the User to be deleted.
     */
    @DELETE
    @Path("{userId}")
    public Response deleteUser(@PathParam("userId") String userId) {
        try {
            KapuaId id = KapuaEid.parseShortId(userId);
            userService.delete(KapuaSecurityUtils.getSession().getScopeId(), id);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }
}
