/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.EntityId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.endpoint.EndpointInfo;
import org.eclipse.kapua.service.endpoint.EndpointInfoCreator;
import org.eclipse.kapua.service.endpoint.EndpointInfoFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfoListResult;
import org.eclipse.kapua.service.endpoint.EndpointInfoPredicates;
import org.eclipse.kapua.service.endpoint.EndpointInfoQuery;
import org.eclipse.kapua.service.endpoint.EndpointInfoService;

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

@Api(value = "EndpointInfos", authorizations = { @Authorization(value = "kapuaAccessToken") })
@Path("{scopeId}/endpointInfos")
public class EndpointInfos extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final EndpointInfoService endpointInfoService = locator.getService(EndpointInfoService.class);
    private final EndpointInfoFactory endpointInfoFactory = locator.getFactory(EndpointInfoFactory.class);

    /**
     * Gets the {@link EndpointInfo} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param usage   The {@link EndpointInfo} usage to filter results
     * @param offset  The result set offset.
     * @param limit   The result set limit.
     * @return The {@link EndpointInfoListResult} of all the endpointInfos associated to the current selected scope.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "endpointInfoSimpleQuery",
            value = "Gets the EndpointInfo list in the scope", //
            notes = "Returns the list of all the endpointInfos associated to the current selected scope.", //
            response = EndpointInfoListResult.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public EndpointInfoListResult simpleQuery(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The endpointInfo usage to filter results.") @QueryParam("usage") String usage,
            @ApiParam(value = "The result set offset.", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,
            @ApiParam(value = "The result set limit.", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        EndpointInfoQuery query = endpointInfoFactory.newQuery(scopeId);

        AndPredicateImpl andPredicate = new AndPredicateImpl();
        if (!Strings.isNullOrEmpty(usage)) {
            andPredicate.and(new AttributePredicateImpl<>(EndpointInfoPredicates.USAGES, endpointInfoFactory.newEndpointUsage(usage)));
        }
        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link EndpointInfoQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link EndpointInfoQuery} to use to filter results.
     * @return The {@link EndpointInfoListResult} of all the result matching the given {@link EndpointInfoQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "endpointInfoQuery",
            value = "Queries the EndpointInfos", //
            notes = "Queries the EndpointInfos with the given EndpointInfoQuery parameter returning all matching EndpointInfos", //
            response = EndpointInfoListResult.class)
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public EndpointInfoListResult query(
            @ApiParam(value = "The ScopeId in which to search results.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The EndpointInfoQuery to use to filter results.", required = true) EndpointInfoQuery query) throws Exception {
        query.setScopeId(scopeId);

        return endpointInfoService.query(query);
    }

    /**
     * Counts the results with the given {@link EndpointInfoQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link EndpointInfoQuery} to use to filter results.
     * @return The count of all the result matching the given {@link EndpointInfoQuery} parameter.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "endpointInfoCount",
            value = "Counts the EndpointInfos", //
            notes = "Counts the EndpointInfos with the given EndpointInfoQuery parameter returning the number of matching EndpointInfos", //
            response = CountResult.class)
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(
            @ApiParam(value = "The ScopeId in which to count results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The EndpointInfoQuery to use to filter count results", required = true) EndpointInfoQuery query) throws Exception {
        query.setScopeId(scopeId);

        return new CountResult(endpointInfoService.count(query));
    }

    /**
     * Creates a new EndpointInfo based on the information provided in EndpointInfoCreator
     * parameter.
     *
     * @param scopeId             The {@link ScopeId} in which to create the {@link EndpointInfo}
     * @param endpointInfoCreator Provides the information for the new {@link EndpointInfo} to be created.
     * @return The newly created {@link EndpointInfo} object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "createEndpointInfo",
            value = "Create a EndpointInfo", //
            notes = "Creates a new EndpointInfo based on the information provided in EndpointInfoCreator parameter.", //
            response = EndpointInfo.class)
    @POST
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public EndpointInfo create(
            @ApiParam(value = "The ScopeId in which to create the EndpointInfo", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "Provides the information for the new EndpointInfo to be created", required = true) EndpointInfoCreator endpointInfoCreator) throws Exception {
        endpointInfoCreator.setScopeId(scopeId);

        return endpointInfoService.create(endpointInfoCreator);
    }

    /**
     * Returns the EndpointInfo specified by the "endpointInfoId" path parameter.
     *
     * @param scopeId        The {@link ScopeId} of the requested {@link EndpointInfo}.
     * @param endpointInfoId The id of the requested EndpointInfo.
     * @return The requested EndpointInfo object.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "endpointInfoFind",
            value = "Get a EndpointInfo", //
            notes = "Returns the EndpointInfo specified by the \"endpointInfoId\" path parameter.", //
            response = EndpointInfo.class)
    @GET
    @Path("{endpointInfoId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public EndpointInfo find(
            @ApiParam(value = "The ScopeId of the requested EndpointInfo.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested EndpointInfo", required = true) @PathParam("endpointInfoId") EntityId endpointInfoId) throws Exception {
        EndpointInfo endpointInfo = endpointInfoService.find(scopeId, endpointInfoId);

        if (endpointInfo == null) {
            throw new KapuaEntityNotFoundException(EndpointInfo.TYPE, endpointInfoId);
        }

        return endpointInfo;
    }

    /**
     * Updates the EndpointInfo based on the information provided in the EndpointInfo parameter.
     *
     * @param scopeId        The ScopeId of the requested {@link EndpointInfo}.
     * @param endpointInfoId The id of the requested {@link EndpointInfo}
     * @param endpointInfo   The modified EndpointInfo whose attributed need to be updated.
     * @return The updated endpointInfo.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "endpointInfoUpdate",
            value = "Update a EndpointInfo", //
            notes = "Updates a new EndpointInfo based on the information provided in the EndpointInfo parameter.", //
            response = EndpointInfo.class)
    @PUT
    @Path("{endpointInfoId}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public EndpointInfo update(
            @ApiParam(value = "The ScopeId of the requested EndpointInfo.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested EndpointInfo", required = true) @PathParam("endpointInfoId") EntityId endpointInfoId,
            @ApiParam(value = "The modified EndpointInfo whose attributed need to be updated", required = true) EndpointInfo endpointInfo) throws Exception {
        endpointInfo.setScopeId(scopeId);
        endpointInfo.setId(endpointInfoId);

        return endpointInfoService.update(endpointInfo);
    }

    /**
     * Deletes the EndpointInfo specified by the "endpointInfoId" path parameter.
     *
     * @param scopeId        The ScopeId of the requested {@link EndpointInfo}.
     * @param endpointInfoId The id of the EndpointInfo to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws Exception Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @ApiOperation(nickname = "endpointInfoDelete",
            value = "Delete an EndpointInfo", //
            notes = "Deletes the EndpointInfo specified by the \"endpointInfoId\" path parameter.")
    @DELETE
    @Path("{endpointInfoId}")
    public Response deleteEndpointInfo(
            @ApiParam(value = "The ScopeId of the EndpointInfo to delete.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the EndpointInfo to be deleted", required = true) @PathParam("endpointInfoId") EntityId endpointInfoId) throws Exception {
        endpointInfoService.delete(scopeId, endpointInfoId);

        return returnOk();
    }
}
