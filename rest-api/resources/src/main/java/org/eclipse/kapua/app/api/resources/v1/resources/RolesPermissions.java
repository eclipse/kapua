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

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionAttributes;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionFactory;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;
import org.eclipse.kapua.service.authorization.role.RolePermissionQuery;
import org.eclipse.kapua.service.authorization.role.RolePermissionService;

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

@Path("{scopeId}/roles/{roleId}/permissions")
public class RolesPermissions extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final RolePermissionService rolePermissionService = locator.getService(RolePermissionService.class);
    private final RolePermissionFactory rolePermissionFactory = locator.getFactory(RolePermissionFactory.class);

    /**
     * Gets the {@link RolePermission} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param roleId  The id of the {@link Role} in which to search results.
     * @param domain  The domain name to filter results.
     * @param action  The action to filter results.
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link RolePermissionListResult} of all the rolePermissions associated to the current selected scope.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public RolePermissionListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId,
            @QueryParam("name") String domain,
            @QueryParam("action") Actions action,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        RolePermissionQuery query = rolePermissionFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        query.setPredicate(query.attributePredicate(RolePermissionAttributes.ROLE_ID, roleId));
        if (!Strings.isNullOrEmpty(domain)) {
            andPredicate.and(query.attributePredicate(RolePermissionAttributes.PERMISSION_DOMAIN, domain));
        }
        if (action != null) {
            andPredicate.and(query.attributePredicate(RolePermissionAttributes.PERMISSION_ACTION, action));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, roleId, query);
    }

    /**
     * Queries the results with the given {@link RolePermissionQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param roleId  The {@link Role} id in which to search results.
     * @param query   The {@link RolePermissionQuery} to use to filter results.
     * @return The {@link RolePermissionListResult} of all the result matching the given {@link RolePermissionQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public RolePermissionListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId,
            RolePermissionQuery query) throws Exception {
        query.setScopeId(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        andPredicate.and(query.attributePredicate(RolePermissionAttributes.ROLE_ID, roleId));
        if (query.getPredicate() != null) {
            andPredicate.and(query.getPredicate());
        }
        query.setPredicate(andPredicate);

        return rolePermissionService.query(query);
    }

    /**
     * Counts the results with the given {@link RolePermissionQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to count results.
     * @param roleId  The {@link Role} id in which to count results.
     * @param query   The {@link RolePermissionQuery} to use to filter results.
     * @return The count of all the result matching the given {@link RolePermissionQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId,
            RolePermissionQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.setPredicate(query.attributePredicate(RolePermissionAttributes.ROLE_ID, roleId));

        return new CountResult(rolePermissionService.count(query));
    }

    /**
     * Creates a new RolePermission based on the information provided in RolePermissionCreator
     * parameter.
     *
     * @param scopeId               The {@link ScopeId} in which to create the {@link RolePermission}
     * @param roleId                The {@link Role} id in which to create the RolePermission.
     * @param rolePermissionCreator Provides the information for the new RolePermission to be created.
     * @return The newly created RolePermission object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public RolePermission create(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId,
            RolePermissionCreator rolePermissionCreator) throws Exception {
        rolePermissionCreator.setScopeId(scopeId);
        rolePermissionCreator.setRoleId(roleId);

        return rolePermissionService.create(rolePermissionCreator);
    }

    /**
     * Returns the RolePermission specified by the "rolePermissionId" path parameter.
     *
     * @param scopeId          The {@link ScopeId} of the requested {@link RolePermission}.
     * @param roleId           The {@link Role} id of the requested {@link RolePermission}.
     * @param rolePermissionId The id of the requested RolePermission.
     * @return The requested RolePermission object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{rolePermissionId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public RolePermission find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId,
            @PathParam("rolePermissionId") EntityId rolePermissionId) throws Exception {
        RolePermissionQuery query = rolePermissionFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate(
                query.attributePredicate(RolePermissionAttributes.ROLE_ID, roleId),
                query.attributePredicate(RolePermissionAttributes.ENTITY_ID, rolePermissionId)
        );

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
     * @param scopeId          The {@link ScopeId} of the {@link RolePermission} to delete.
     * @param roleId           The {@link Role} id of the {@link RolePermission} to delete.
     * @param rolePermissionId The id of the RolePermission to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @DELETE
    @Path("{rolePermissionId}")
    public Response deleteRolePermission(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("roleId") EntityId roleId,
            @PathParam("rolePermissionId") EntityId rolePermissionId) throws Exception {
        rolePermissionService.delete(scopeId, rolePermissionId);

        return returnOk();
    }
}
