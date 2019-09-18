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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.StorableEntityId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.internal.mediator.ClientInfoField;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicateFactory;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;

import com.google.common.base.Strings;

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
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public ClientInfoListResult simpleQuery( //
            @PathParam("scopeId") ScopeId scopeId,//
            @QueryParam("clientId") String clientId, //
            @QueryParam("offset") @DefaultValue("0") int offset,//
            @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {
        AndPredicate andPredicate = STORABLE_PREDICATE_FACTORY.newAndPredicate();
        if (!Strings.isNullOrEmpty(clientId)) {
            TermPredicate clientIdPredicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(ClientInfoField.CLIENT_ID, clientId);
            andPredicate.getPredicates().add(clientIdPredicate);
        }

        ClientInfoQuery query = DATASTORE_OBJECT_FACTORY.newClientInfoQuery(scopeId);

        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link ClientInfoQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link ClientInfoQuery} to used to filter results.
     * @return The {@link ClientInfoListResult} of all the result matching the given {@link ClientInfoQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public ClientInfoListResult query(
            @PathParam("scopeId") ScopeId scopeId, //
            ClientInfoQuery query) throws Exception {
        query.setScopeId(scopeId);
        query.addFetchAttributes(ClientInfoField.TIMESTAMP.field());
        return CLIENT_INFO_REGISTRY_SERVICE.query(query);
    }

    /**
     * Counts the results with the given {@link ClientInfoQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link ClientInfoQuery} to used to filter results.
     * @return The count of all the result matching the given {@link ClientInfoQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })

    public CountResult count( //
            @PathParam("scopeId") ScopeId scopeId, //
            ClientInfoQuery query) throws Exception {
        query.setScopeId(scopeId);

        return new CountResult(CLIENT_INFO_REGISTRY_SERVICE.count(query));
    }

    /**
     * Returns the ClientInfo specified by the "clientInfoId" path parameter.
     *
     * @param clientInfoId
     *            The id of the requested ClientInfo.
     * @return The requested ClientInfo object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{clientInfoId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })

    public ClientInfo find( //
            @PathParam("scopeId") ScopeId scopeId, //
            @PathParam("clientInfoId") StorableEntityId clientInfoId) throws Exception {
        ClientInfo clientInfo = CLIENT_INFO_REGISTRY_SERVICE.find(scopeId, clientInfoId);

        return returnNotNullEntity(clientInfo);
    }
}
