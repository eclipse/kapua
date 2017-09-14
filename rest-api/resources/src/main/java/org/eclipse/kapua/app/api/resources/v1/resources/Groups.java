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
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupCreator;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.GroupQuery;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.group.shiro.GroupPredicates;

import com.google.common.base.Strings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "Groups", authorizations = { @Authorization(value = "kapuaAccessToken") })
@Path("{scopeId}/groups")
public class Groups extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final GroupService groupService = locator.getService(GroupService.class);
    private final GroupFactory groupFactory = locator.getFactory(GroupFactory.class);

    /**
     * Gets the {@link Group} list in the scope.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param name
     *            The {@link Group} name to filter results
     * @param offset
     *            The result set offset.
     * @param limit
     *            The result set limit.
     * @return The {@link GroupListResult} of all the groups associated to the current selected scope.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "groupSimpleQuery", value = "Gets the Group list in the scope", notes = "Returns the list of all the groups associated to the current selected scope.", response = GroupListResult.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public GroupListResult simpleQuery(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The group name to filter results.") @QueryParam("name") String name,
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,
            @ApiParam(value = "The result set limit.", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        GroupQuery query = groupFactory.newQuery(scopeId);

        AndPredicate andPredicate = new AndPredicate();
        if (!Strings.isNullOrEmpty(name)) {
            andPredicate.and(new AttributePredicate<>(GroupPredicates.NAME, name));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link GroupQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link GroupQuery} to use to filter results.
     * @return The {@link GroupListResult} of all the result matching the given {@link GroupQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "groupQuery", value = "Queries the Groups", notes = "Queries the Groups with the given GroupQuery parameter returning all matching Groups", response = GroupListResult.class)
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public GroupListResult query(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The GroupQuery to use to filter results.", required = true) GroupQuery query) throws Exception {
        query.setScopeId(scopeId);

        return groupService.query(query);
    }

    /**
     * Counts the results with the given {@link GroupQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link GroupQuery} to use to filter results.
     * @return The count of all the result matching the given {@link GroupQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "groupCount", value = "Counts the Groups", notes = "Counts the Groups with the given GroupQuery parameter returning the number of matching Groups", response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(
            @ApiParam(value = "The ScopeId in which to count results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The GroupQuery to use to filter count results", required = true) GroupQuery query) throws Exception {
        query.setScopeId(scopeId);

        return new CountResult(groupService.count(query));
    }

    /**
     * Creates a new Group based on the information provided in GroupCreator
     * parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to create the {@link Group}
     * @param groupCreator
     *            Provides the information for the new {@link Group} to be created.
     * @return The newly created {@link Group} object.
     */
    @ApiOperation(nickname = "groupCreate", value = "Create a Group", notes = "Creates a new Group based on the information provided in GroupCreator parameter.", response = Group.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Group create(
            @ApiParam(value = "The ScopeId in which to create the Group", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Provides the information for the new Group to be created", required = true) GroupCreator groupCreator) throws Exception {
        groupCreator.setScopeId(scopeId);

        return groupService.create(groupCreator);
    }

    /**
     * Returns the Group specified by the "groupId" path parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} of the requested {@link Group}.
     * @param groupId
     *            The id of the requested Group.
     * @return The requested Group object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "groupFind", value = "Get an Group", notes = "Returns the Group specified by the \"groupId\" path parameter.", response = Group.class)
    @GET
    @Path("{groupId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Group find(
            @ApiParam(value = "The ScopeId of the requested Group.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested Group", required = true) @PathParam("groupId") EntityId groupId) throws Exception {
        Group group = groupService.find(scopeId, groupId);

        if (group == null) {
            throw new KapuaEntityNotFoundException(Group.TYPE, groupId);
        }

        return group;
    }

    /**
     * Updates the Group based on the information provided in the Group parameter.
     *
     * @param scopeId
     *            The ScopeId of the requested {@link Group}.
     * @param groupId
     *            The id of the requested {@link Group}
     * @param group
     *            The modified Group whose attributed need to be updated.
     * @return The updated group.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "groupUpdate", value = "Update an Group", notes = "Updates a new Group based on the information provided in the Group parameter.", response = Group.class)
    @PUT
    @Path("{groupId}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Group update(
            @ApiParam(value = "The ScopeId of the requested Group.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested Group", required = true) @PathParam("groupId") EntityId groupId,
            @ApiParam(value = "The modified Group whose attributed need to be updated", required = true) Group group) throws Exception {
        group.setScopeId(scopeId);
        group.setId(groupId);

        return groupService.update(group);
    }

    /**
     * Deletes the Group specified by the "groupId" path parameter.
     *
     * @param scopeId
     *            The ScopeId of the requested {@link Group}.
     * @param groupId
     *            The id of the Group to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "groupDelete", value = "Delete an Group", notes = "Deletes the Group specified by the \"groupId\" path parameter.")
    @DELETE
    @Path("{groupId}")
    public Response deleteGroup(
            @ApiParam(value = "The ScopeId of the Group to delete.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Group to be deleted", required = true) @PathParam("groupId") EntityId groupId) throws Exception {
        groupService.delete(scopeId, groupId);

        return returnOk();
    }
}
