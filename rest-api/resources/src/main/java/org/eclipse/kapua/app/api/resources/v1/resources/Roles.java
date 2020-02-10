/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoAttributes;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleAttributes;
import org.eclipse.kapua.service.authorization.access.AccessRoleFactory;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;
import org.eclipse.kapua.service.authorization.access.AccessRoleQuery;
import org.eclipse.kapua.service.authorization.access.AccessRoleService;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleAttributes;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserAttributes;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserService;

import com.google.common.base.Strings;

@Path("{scopeId}/roles")
public class Roles extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final RoleService roleService = locator.getService(RoleService.class);
    private final RoleFactory roleFactory = locator.getFactory(RoleFactory.class);
    private final AccessRoleService accessRoleService = locator.getService(AccessRoleService.class);
    private final AccessRoleFactory accessRoleFactory = locator.getFactory(AccessRoleFactory.class);
    private final AccessInfoService accessInfoService = locator.getService(AccessInfoService.class);
    private final AccessInfoFactory accessInfoFactory = locator.getFactory(AccessInfoFactory.class);
    private final UserService userService = locator.getService(UserService.class);
    private final UserFactory userFactory = locator.getFactory(UserFactory.class);

    /**
     * Gets the {@link Role} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param name    The {@link Role} name in which to search results.
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link RoleListResult} of all the roles associated to the current selected scope.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RoleListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @QueryParam("name") String name,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        RoleQuery query = roleFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        if (!Strings.isNullOrEmpty(name)) {
            andPredicate.and(query.attributePredicate(RoleAttributes.NAME, name));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link RoleQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link RoleQuery} to use to filter results.
     * @return The {@link RoleListResult} of all the result matching the given {@link RoleQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public RoleListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            RoleQuery query) throws Exception {
        query.setScopeId(scopeId);

        return roleService.query(query);
    }

    /**
     * Counts the results with the given {@link RoleQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link RoleQuery} to use to filter results.
     * @return The count of all the result matching the given {@link RoleQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(
            @PathParam("scopeId") ScopeId scopeId,
            RoleQuery query) throws Exception {
        query.setScopeId(scopeId);

        return new CountResult(roleService.count(query));
    }

    /**
     * Creates a new Role based on the information provided in RoleCreator
     * parameter.
     *
     * @param scopeId     The {@link ScopeId} in which to create the {@link Role}
     * @param roleCreator Provides the information for the new {@link Role} to be created.
     * @return The newly created {@link Role} object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Role create(
            @PathParam("scopeId") ScopeId scopeId,
            RoleCreator roleCreator) throws Exception {
        roleCreator.setScopeId(scopeId);

        return roleService.create(roleCreator);
    }

    /**
     * Returns the Role specified by the "roleId" path parameter.
     *
     * @param scopeId The {@link ScopeId} of the requested {@link Role}.
     * @param roleId  The id of the requested {@link Role}.
     * @return The requested {@link Role} object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{roleId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Role find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId) throws Exception {
        Role role = roleService.find(scopeId, roleId);

        if (role == null) {
            throw new KapuaEntityNotFoundException(Role.TYPE, roleId);
        }

        return role;
    }

    /**
     * Updates the Role based on the information provided in the Role parameter.
     *
     * @param scopeId The ScopeId of the requested {@link Role}.
     * @param roleId  The id of the requested {@link Role}
     * @param role    The modified Role whose attributed need to be updated.
     * @return The updated {@link Role}.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @PUT
    @Path("{roleId}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Role update(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId,
            Role role) throws Exception {
        role.setScopeId(scopeId);
        role.setId(roleId);

        return roleService.update(role);
    }

    /**
     * Deletes the Role specified by the "roleId" path parameter.
     *
     * @param scopeId The ScopeId of the requested {@link Role}.
     * @param roleId  The id of the Role to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @DELETE
    @Path("{roleId}")
    public Response deleteRole(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId) throws Exception {
        roleService.delete(scopeId, roleId);

        return returnOk();
    }

    /**
     * Gets all the {@link User}s for a given {@link Role}
     *
     * @param scopeId The ScopeId of the requested {@link Role}.
     * @param roleId The id of the Role to be deleted.
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return An {@link UserListResult} containing the {@link User}s for the given {@link Role}
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.2.0
     */
    @GET
    @Path("{roleId}/users")
    public UserListResult usersForRole(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        AccessRoleQuery accessRoleQuery = accessRoleFactory.newQuery(scopeId);
        accessRoleQuery.setPredicate(accessRoleQuery.attributePredicate(AccessRoleAttributes.ROLE_ID, roleId));
        accessRoleQuery.setLimit(limit);
        accessRoleQuery.setOffset(offset);
        AccessRoleListResult accessRoleListResult = accessRoleService.query(accessRoleQuery);

        AccessInfoQuery accessInfoQuery = accessInfoFactory.newQuery(scopeId);
        accessInfoQuery.setPredicate(accessInfoQuery.attributePredicate(AccessInfoAttributes.ENTITY_ID, accessRoleListResult.getItems().stream().map(AccessRole::getAccessInfoId).toArray(KapuaId[]::new)));
        accessRoleQuery.setLimit(limit);
        accessRoleQuery.setOffset(offset);
        AccessInfoListResult accessInfoListResult = accessInfoService.query(accessInfoQuery);

        UserQuery userQuery = userFactory.newQuery(scopeId);
        userQuery.setPredicate(userQuery.attributePredicate(UserAttributes.ENTITY_ID, accessInfoListResult.getItems().stream().map(AccessInfo::getUserId).toArray(KapuaId[]::new)));
        userQuery.setLimit(limit);
        userQuery.setOffset(offset);
        return userService.query(userQuery);
    }

}
