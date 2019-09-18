/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.service.endpoint.EndpointInfo;
import org.eclipse.kapua.service.endpoint.EndpointInfoAttributes;
import org.eclipse.kapua.service.endpoint.EndpointInfoCreator;
import org.eclipse.kapua.service.endpoint.EndpointInfoFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfoListResult;
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

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public EndpointInfoListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @QueryParam("usage") String usage,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        EndpointInfoQuery query = endpointInfoFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        if (!Strings.isNullOrEmpty(usage)) {
            andPredicate.and(query.attributePredicate(EndpointInfoAttributes.USAGES, endpointInfoFactory.newEndpointUsage(usage)));
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

    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public EndpointInfoListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            EndpointInfoQuery query) throws Exception {
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

    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @PathParam("scopeId") ScopeId scopeId,
            EndpointInfoQuery query) throws Exception {
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

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public EndpointInfo create(
            @PathParam("scopeId") ScopeId scopeId,
            EndpointInfoCreator endpointInfoCreator) throws Exception {
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

    @GET
    @Path("{endpointInfoId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public EndpointInfo find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("endpointInfoId") EntityId endpointInfoId) throws Exception {
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

    @PUT
    @Path("{endpointInfoId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public EndpointInfo update(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("endpointInfoId") EntityId endpointInfoId,
            EndpointInfo endpointInfo) throws Exception {
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

    @DELETE
    @Path("{endpointInfoId}")
    public Response deleteEndpointInfo(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("endpointInfoId") EntityId endpointInfoId) throws Exception {
        endpointInfoService.delete(scopeId, endpointInfoId);

        return returnOk();
    }
}
