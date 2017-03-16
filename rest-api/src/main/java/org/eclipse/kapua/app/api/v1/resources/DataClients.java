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

import java.lang.management.ClassLoadingMXBean;

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
import org.eclipse.kapua.app.api.v1.resources.model.StorableEntityId;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ClientInfoField;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Data Clients")
@Path("{scopeId}/data/clients")
public class DataClients extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final ClientInfoRegistryService clientInfoRegistryService = locator.getService(ClientInfoRegistryService.class);
    private final DatastoreObjectFactory datastoreObjectFactory = locator.getFactory(DatastoreObjectFactory.class);

    /**
     * Gets the {@link ClientInfo} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param offset The result set offset.
     * @param limit The result set limit.
     * @return The {@link ClientInfoListResult} of all the clientInfos associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the ClientInfo list in the scope", //
            notes = "Returns the list of all the clientInfos associated to the current selected scope.", //
            response = ClientInfo.class, //
            responseContainer = "ClientInfoListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ClientInfoListResult simpleQuery( @PathParam("scopeId") ScopeId scopeId,//
                                        @QueryParam("offset") @DefaultValue("0") int offset,//
                                        @QueryParam("limit") @DefaultValue("50") int limit) //
    {
        ClientInfoListResult clientInfoListResult = datastoreObjectFactory.newClientInfoListResult();
        try {
            ClientInfoQuery query = datastoreObjectFactory.newClientInfoQuery(scopeId);
                        
            query.setOffset(offset);
            query.setLimit(limit);
            
            clientInfoListResult = query(scopeId, query);
        } catch (Throwable t) {
            handleException(t);
        }
        return clientInfoListResult;
    }
    
    /**
     * Queries the results with the given {@link ClientInfoQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to search results. 
     * @param query The {@link ClientInfoQuery} to used to filter results.
     * @return The {@link ClientInfoListResult} of all the result matching the given {@link ClientInfoQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public ClientInfoListResult query(@PathParam("scopeId") ScopeId scopeId, ClientInfoQuery query) {
        ClientInfoListResult clientInfoListResult = null;
        try {
            query.setScopeId(scopeId);
            clientInfoListResult = clientInfoRegistryService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(clientInfoListResult);
    }
    
    /**
     * Counts the results with the given {@link ClientInfoQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to search results. 
     * @param query The {@link ClientInfoQuery} to used to filter results.
     * @return The count of all the result matching the given {@link ClientInfoQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(@PathParam("scopeId") ScopeId scopeId, ClientInfoQuery query) {
        CountResult countResult = null;
        try {
            query.setScopeId(scopeId);
            countResult = new CountResult(clientInfoRegistryService.count(query));
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(countResult);
    }
    

    /**
     * Returns the ClientInfo specified by the "clientInfoId" path parameter.
     *
     * @param clientInfoId
     *            The id of the requested ClientInfo.
     * @return The requested ClientInfo object.
     */
    @ApiOperation(value = "Get an ClientInfo", notes = "Returns the ClientInfo specified by the \"clientInfoId\" path parameter.", response = ClientInfo.class)
    @GET
    @Path("{clientInfoId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ClientInfo find(@PathParam("scopeId") ScopeId scopeId, 
            @ApiParam(value = "The id of the requested ClientInfo", required = true) @PathParam("clientInfoId") StorableEntityId clientInfoId) {
        ClientInfo clientInfo = null;
        try {
            clientInfo = clientInfoRegistryService.find(scopeId, clientInfoId);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(clientInfo);
    }
}
