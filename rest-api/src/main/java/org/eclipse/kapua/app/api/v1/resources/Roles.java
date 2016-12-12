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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RoleListResult;
import org.eclipse.kapua.service.authorization.role.RoleQuery;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.role.shiro.RoleImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Roles")
@Path("/roles")
public class Roles extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final RoleService roleService = locator.getService(RoleService.class);
    private final RoleFactory roleFactory = locator.getFactory(RoleFactory.class);

    /**
     * Returns the list of all the roles for the current account.
     *
     * @return The list of requested Roles objects.
     */
    @ApiOperation(value = "Get the Roles for the current account", notes = "Returns the list of all the roles available for the current account.", response = Role.class, responseContainer = "RoleListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public RoleListResult getRoles() {
        RoleListResult rolesList = roleFactory.newRoleListResult();
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            RoleQuery rolesQuery = roleFactory.newQuery(scopeId);
            rolesList = roleService.query(rolesQuery);
        } catch (Throwable t) {
            handleException(t);
        }
        return rolesList;
    }

    /**
     * Returns the role for the given id.
     *
     * @return The requested dole.
     */
    @ApiOperation(value = "Get the Role for the given id", notes = "Returns the role for the given id.", response = Role.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("{id}")
    public Role getRoles(@PathParam("id") String id) {
        Role role = null;
        try {
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            KapuaId roleKapuaId = KapuaEid.parseCompactId(id);
            role = roleService.find(scopeId, roleKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return role;
    }

    /**
     * Creates a new Role based on the information provided in RoleCreator parameter.
     *
     * @param roleCreator
     *            Provides the information for the new Role to be created.
     * @return The newly created Role object.
     */
    @ApiOperation(value = "Create a Role", notes = "Creates a new Role based on the information provided in RoleCreator parameter.", response = Role.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Role createRole(
            @ApiParam(value = "Provides the information for the new Role to be created", required = true) RoleCreator roleCreator) {

        Role role = null;
        try {
            roleCreator.setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            role = roleService.create(roleCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(role);
    }

    /**
     * Deletes the Role specified by the "roleId" path parameter.
     *
     * @param roleId
     *            The id of the Role to be deleted.
     */
    @ApiOperation(value = "Delete a Role", notes = "Deletes a role based on the information provided in roleId parameter.")
    @DELETE
    @Path("{roleId}")
    public Response deleteRole(
            @ApiParam(value = "The id of the Role to be deleted", required = true) @PathParam("roleId") String roleId) {
        try {
            KapuaId roleKapuaId = KapuaEid.parseCompactId(roleId);
            KapuaId scopeId = KapuaSecurityUtils.getSession().getScopeId();
            roleService.delete(scopeId, roleKapuaId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }

    /**
     * Updates a role based on the information provided in Role parameter.
     *
     * @param role
     *            Provides the information to update the role.
     * @return The updated Role object.
     */
    @ApiOperation(value = "Update a Role", notes = "Updates a role based on the information provided in role parameter.", response = Role.class)
    @PUT
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Role updateRole(
            @ApiParam(value = "Provides the information to update the role", required = true) Role role) {
        try {
            ((RoleImpl) role).setScopeId(KapuaSecurityUtils.getSession().getScopeId());
            role = roleService.update(role);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(role);
    }
}
