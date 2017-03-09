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
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupCreator;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.GroupQuery;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.group.shiro.GroupImpl;
import org.eclipse.kapua.service.authorization.group.shiro.GroupPredicates;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Groups")
@Path("{scopeId}/groups")
public class Groups extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final GroupService groupService = locator.getService(GroupService.class);
    private final GroupFactory groupFactory = locator.getFactory(GroupFactory.class);

    /**
     * Gets the {@link Group} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param offset The result set offset.
     * @param limit The result set limit.
     * @return The {@link GroupListResult} of all the groups associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the Group list in the scope", //
            notes = "Returns the list of all the groups associated to the current selected scope.", //
            response = Group.class, //
            responseContainer = "GroupListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public GroupListResult simpleQuery(  @PathParam("scopeId") ScopeId scopeId,//
            @QueryParam("name") String name,//
                                        @QueryParam("offset") @DefaultValue("0") int offset,//
                                        @QueryParam("limit") @DefaultValue("50") int limit) //
    {
        GroupListResult groupListResult = groupFactory.newGroupListResult();
        try {
            GroupQuery query = groupFactory.newQuery(scopeId);
            
            AndPredicate andPredicate = new AndPredicate();
            if (name != null) {
                andPredicate.and(new AttributePredicate<>(GroupPredicates.NAME, name));
            }
            query.setPredicate(andPredicate);
            
            query.setOffset(offset);
            query.setLimit(limit);
            
            groupListResult = query(scopeId, query);
        } catch (Throwable t) {
            handleException(t);
        }
        return groupListResult;
    }
    
    /**
     * Queries the results with the given {@link GroupQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to search results. 
     * @param query The {@link GroupQuery} to used to filter results.
     * @return The {@link GroupListResult} of all the result matching the given {@link GroupQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public GroupListResult query(@PathParam("scopeId") ScopeId scopeId, GroupQuery query) {
        GroupListResult groupListResult = null;
        try {
            query.setScopeId(scopeId);
            groupListResult = groupService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(groupListResult);
    }
    
    /**
     * Counts the results with the given {@link GroupQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to search results. 
     * @param query The {@link GroupQuery} to used to filter results.
     * @return The count of all the result matching the given {@link GroupQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(@PathParam("scopeId") ScopeId scopeId, GroupQuery query) {
        CountResult countResult = null;
        try {
            query.setScopeId(scopeId);
            countResult = new CountResult(groupService.count(query));
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(countResult);
    }
    
    /**
     * Creates a new Group based on the information provided in GroupCreator
     * parameter.
     *
     *@param scopeId The {@link ScopeId} in which to create the {@link Group}
     * @param groupCreator
     *            Provides the information for the new Group to be created.
     * @return The newly created Group object.
     */
    @ApiOperation(value = "Create an Group", notes = "Creates a new Group based on the information provided in GroupCreator parameter.", response = Group.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Group create(@PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Provides the information for the new Group to be created", required = true) GroupCreator groupCreator) {
        Group group = null;
        try {
            groupCreator.setScopeId(scopeId);
            group = groupService.create(groupCreator);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(group);
    }

    /**
     * Returns the Group specified by the "groupId" path parameter.
     *
     * @param groupId
     *            The id of the requested Group.
     * @return The requested Group object.
     */
    @ApiOperation(value = "Get an Group", notes = "Returns the Group specified by the \"groupId\" path parameter.", response = Group.class)
    @GET
    @Path("{groupId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Group find(@PathParam("scopeId") ScopeId scopeId, 
            @ApiParam(value = "The id of the requested Group", required = true) @PathParam("groupId") EntityId groupId) {
        Group group = null;
        try {
            group = groupService.find(scopeId, groupId);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(group);
    }
    
    /**
     * Updates the Group based on the information provided in the Group parameter.
     *
     * @param group
     *            The modified Group whose attributed need to be updated.
     * @return The updated group.
     */
    @ApiOperation(value = "Update an Group", notes = "Updates a new Group based on the information provided in the Group parameter.", response = Group.class)
    @PUT
    @Path("{groupId}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Group update(@PathParam("scopeId") ScopeId scopeId,
            @PathParam("groupId") EntityId groupId,
            @ApiParam(value = "The modified Group whose attributed need to be updated", required = true) Group group) {
        Group groupUpdated = null;
        try {
            ((GroupImpl) group).setScopeId(scopeId);
            group.setId(groupId);
            
            groupUpdated = groupService.update(group);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(groupUpdated);
    }

    /**
     * Deletes the Group specified by the "groupId" path parameter.
     *
     * @param groupId
     *            The id of the Group to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     */
    @ApiOperation(value = "Delete an Group", notes = "Deletes the Group specified by the \"groupId\" path parameter.")
    @DELETE
    @Path("{groupId}")
    public Response deleteGroup(@PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the Group to be deleted", required = true) @PathParam("groupId") EntityId groupId) {
        try {
            groupService.delete(scopeId, groupId);
        } catch (Throwable t) {
            handleException(t);
        }
        return Response.ok().build();
    }
}
