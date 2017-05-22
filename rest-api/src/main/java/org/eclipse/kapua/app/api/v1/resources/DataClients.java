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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.kapua.app.api.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.v1.resources.model.ScopeId;
import org.eclipse.kapua.app.api.v1.resources.model.StorableEntityId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.internal.mediator.ClientInfoField;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicateFactory;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;

import com.google.common.base.Strings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Data Clients")
@Path("{scopeId}/data/clients")
public class DataClients extends AbstractKapuaResource {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final ClientInfoRegistryService CLIENT_INFO_REGISTRY_SERVICE = LOCATOR.getService(ClientInfoRegistryService.class);
    private static final DatastoreObjectFactory DATASTORE_OBJECT_FACTORY = LOCATOR.getFactory(DatastoreObjectFactory.class);
    private static final StorablePredicateFactory STORABLE_PREDICATE_FACTORY = LOCATOR.getFactory(StorablePredicateFactory.class);

    /**
     * Gets the {@link ClientInfo} list in the scope.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param clientId
     *            The client id to filter results
     * @param offset
     *            The result set offset.
     * @param limit
     *            The result set limit.
     * @return The {@link ClientInfoListResult} of all the clientInfos associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the ClientInfo list in the scope", //
            notes = "Returns the list of all the clientInfos associated to the current selected scope.", //
            response = ClientInfo.class, //
            responseContainer = "ClientInfoListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ClientInfoListResult simpleQuery( //
            @ApiParam(value = "The ScopeId in which to search results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,//
            @ApiParam(value = "The client id to filter results") @QueryParam("clientId") String clientId, //
            @ApiParam(value = "The result set offset", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,//
            @ApiParam(value = "The result set limit", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) {
        ClientInfoListResult clientInfoListResult = DATASTORE_OBJECT_FACTORY.newClientInfoListResult();
        try {
            AndPredicate andPredicate = new AndPredicateImpl();
            if (!Strings.isNullOrEmpty(clientId)) {
                TermPredicate clientIdPredicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(ClientInfoField.CLIENT_ID, clientId);
                andPredicate.getPredicates().add(clientIdPredicate);
            }

            ClientInfoQuery query = DATASTORE_OBJECT_FACTORY.newClientInfoQuery(scopeId);
            query.setPredicate(andPredicate);
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
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link ClientInfoQuery} to used to filter results.
     * @return The {@link ClientInfoListResult} of all the result matching the given {@link ClientInfoQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Queries the ClientInfos", //
            notes = "Queries the ClientInfos with the given ClientInfoQuery parameter returning all matching ClientInfos",  //
            response = ClientInfo.class, //
            responseContainer = "ClientInfoListResult")  //
    public ClientInfoListResult query( //
            @ApiParam(value = "The ScopeId in which to search results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The ClientInfoQuery to use to filter results", required = true) ClientInfoQuery query) {
        ClientInfoListResult clientInfoListResult = null;
        try {
            query.setScopeId(scopeId);
            clientInfoListResult = CLIENT_INFO_REGISTRY_SERVICE.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(clientInfoListResult);
    }

    /**
     * Counts the results with the given {@link ClientInfoQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link ClientInfoQuery} to used to filter results.
     * @return The count of all the result matching the given {@link ClientInfoQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Counts the ClientInfos", //
            notes = "Counts the ClientInfos with the given ClientInfoQuery parameter returning the number of matching ClientInfos", //
            response = CountResult.class)
    public CountResult count( //
            @ApiParam(value = "The ScopeId in which to search results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The ClientInfoQuery to use to filter count results", required = true) ClientInfoQuery query) {
        CountResult countResult = null;
        try {
            query.setScopeId(scopeId);
            countResult = new CountResult(CLIENT_INFO_REGISTRY_SERVICE.count(query));
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
    @GET
    @Path("{clientInfoId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(value = "Gets an ClientInfo", //
            notes = "Gets the ClientInfo specified by the clientInfoId path parameter", //
            response = ClientInfo.class)
    public ClientInfo find( //
            @ApiParam(value = "The ScopeId of the requested ClientInfo.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The id of the requested ClientInfo", required = true) @PathParam("clientInfoId") StorableEntityId clientInfoId) {
        ClientInfo clientInfo = null;
        try {
            clientInfo = CLIENT_INFO_REGISTRY_SERVICE.find(scopeId, clientInfoId);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(clientInfo);
    }
}
