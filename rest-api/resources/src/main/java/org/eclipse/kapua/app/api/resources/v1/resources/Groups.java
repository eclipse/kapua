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
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupAttributes;
import org.eclipse.kapua.service.authorization.group.GroupCreator;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.GroupQuery;
import org.eclipse.kapua.service.authorization.group.GroupService;

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

@Path("{scopeId}/groups")
public class Groups extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final GroupService groupService = locator.getService(GroupService.class);
    private final GroupFactory groupFactory = locator.getFactory(GroupFactory.class);

    /**
     * Gets the {@link Group} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param name    The {@link Group} name to filter results
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link GroupListResult} of all the groups associated to the current selected scope.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public GroupListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @QueryParam("name") String name,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        GroupQuery query = groupFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        if (!Strings.isNullOrEmpty(name)) {
            andPredicate.and(query.attributePredicate(GroupAttributes.NAME, name));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link GroupQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link GroupQuery} to use to filter results.
     * @return The {@link GroupListResult} of all the result matching the given {@link GroupQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public GroupListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            GroupQuery query) throws Exception {
        query.setScopeId(scopeId);

        return groupService.query(query);
    }

    /**
     * Counts the results with the given {@link GroupQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link GroupQuery} to use to filter results.
     * @return The count of all the result matching the given {@link GroupQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @PathParam("scopeId") ScopeId scopeId,
            GroupQuery query) throws Exception {
        query.setScopeId(scopeId);

        return new CountResult(groupService.count(query));
    }

    /**
     * Creates a new Group based on the information provided in GroupCreator
     * parameter.
     *
     * @param scopeId      The {@link ScopeId} in which to create the {@link Group}
     * @param groupCreator Provides the information for the new {@link Group} to be created.
     * @return The newly created {@link Group} object.
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Group create(
            @PathParam("scopeId") ScopeId scopeId,
            GroupCreator groupCreator) throws Exception {
        groupCreator.setScopeId(scopeId);

        return groupService.create(groupCreator);
    }

    /**
     * Returns the Group specified by the "groupId" path parameter.
     *
     * @param scopeId The {@link ScopeId} of the requested {@link Group}.
     * @param groupId The id of the requested Group.
     * @return The requested Group object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{groupId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Group find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("groupId") EntityId groupId) throws Exception {
        Group group = groupService.find(scopeId, groupId);

        if (group == null) {
            throw new KapuaEntityNotFoundException(Group.TYPE, groupId);
        }

        return group;
    }

    /**
     * Updates the Group based on the information provided in the Group parameter.
     *
     * @param scopeId The ScopeId of the requested {@link Group}.
     * @param groupId The id of the requested {@link Group}
     * @param group   The modified Group whose attributed need to be updated.
     * @return The updated group.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @PUT
    @Path("{groupId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Group update(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("groupId") EntityId groupId,
            Group group) throws Exception {
        group.setScopeId(scopeId);
        group.setId(groupId);

        return groupService.update(group);
    }

    /**
     * Deletes the Group specified by the "groupId" path parameter.
     *
     * @param scopeId The ScopeId of the requested {@link Group}.
     * @param groupId The id of the Group to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @DELETE
    @Path("{groupId}")
    public Response deleteGroup(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("groupId") EntityId groupId) throws Exception {
        groupService.delete(scopeId, groupId);

        return returnOk();
    }
}
