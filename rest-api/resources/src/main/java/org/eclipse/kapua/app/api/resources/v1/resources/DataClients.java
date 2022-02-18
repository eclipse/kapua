/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources;

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.core.resources.AbstractKapuaResource;
import org.eclipse.kapua.app.api.core.model.CountResult;
import org.eclipse.kapua.app.api.core.model.ScopeId;
import org.eclipse.kapua.app.api.core.model.StorableEntityId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.datastore.ClientInfoFactory;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.internal.mediator.ClientInfoField;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.predicate.DatastorePredicateFactory;
import org.eclipse.kapua.service.storable.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.storable.model.query.predicate.TermPredicate;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("{scopeId}/data/clients")
public class DataClients extends AbstractKapuaResource {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final ClientInfoRegistryService CLIENT_INFO_REGISTRY_SERVICE = LOCATOR.getService(ClientInfoRegistryService.class);
    private static final ClientInfoFactory CLIENT_INFO_FACTORY = LOCATOR.getFactory(ClientInfoFactory.class);
    private static final DatastorePredicateFactory DATASTORE_PREDICATE_FACTORY = LOCATOR.getFactory(DatastorePredicateFactory.class);

    /**
     * Gets the {@link ClientInfo} list in the scope.
     *
     * @param scopeId  The {@link ScopeId} in which to search results.
     * @param clientId The client id to filter results
     * @param offset   The result set offset.
     * @param limit    The result set limit.
     * @return The {@link ClientInfoListResult} of all the clientInfos associated to the current selected scope.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ClientInfoListResult simpleQuery(@PathParam("scopeId") ScopeId scopeId,
                                            @QueryParam("clientId") String clientId,
                                            @QueryParam("offset") @DefaultValue("0") int offset,
                                            @QueryParam("limit") @DefaultValue("50") int limit)
            throws KapuaException {
        AndPredicate andPredicate = DATASTORE_PREDICATE_FACTORY.newAndPredicate();
        if (!Strings.isNullOrEmpty(clientId)) {
            TermPredicate clientIdPredicate = DATASTORE_PREDICATE_FACTORY.newTermPredicate(ClientInfoField.CLIENT_ID, clientId);
            andPredicate.getPredicates().add(clientIdPredicate);
        }

        ClientInfoQuery query = CLIENT_INFO_FACTORY.newQuery(scopeId);

        query.setPredicate(andPredicate);

        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link ClientInfoQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link ClientInfoQuery} to used to filter results.
     * @return The {@link ClientInfoListResult} of all the result matching the given {@link ClientInfoQuery} parameter.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ClientInfoListResult query(@PathParam("scopeId") ScopeId scopeId,
                                      ClientInfoQuery query)
            throws KapuaException {
        query.setScopeId(scopeId);
        query.addFetchAttributes(ClientInfoField.TIMESTAMP.field());
        return CLIENT_INFO_REGISTRY_SERVICE.query(query);
    }

    /**
     * Counts the results with the given {@link ClientInfoQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link ClientInfoQuery} to used to filter results.
     * @return The count of all the result matching the given {@link ClientInfoQuery} parameter.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(@PathParam("scopeId") ScopeId scopeId,
                             ClientInfoQuery query)
            throws KapuaException {
        query.setScopeId(scopeId);

        return new CountResult(CLIENT_INFO_REGISTRY_SERVICE.count(query));
    }

    /**
     * Returns the ClientInfo specified by the "clientInfoId" path parameter.
     *
     * @param clientInfoId The id of the requested ClientInfo.
     * @return The requested ClientInfo object.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{clientInfoId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ClientInfo find(@PathParam("scopeId") ScopeId scopeId,
                           @PathParam("clientInfoId") StorableEntityId clientInfoId)
            throws KapuaException {
        ClientInfo clientInfo = CLIENT_INFO_REGISTRY_SERVICE.find(scopeId, clientInfoId);

        return returnNotNullEntity(clientInfo);
    }
}
