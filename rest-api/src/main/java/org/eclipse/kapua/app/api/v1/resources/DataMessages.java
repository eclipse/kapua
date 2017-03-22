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
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Data Messages")
@Path("{scopeId}/data/messages")
public class DataMessages extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final MessageStoreService messageRegistryService = locator.getService(MessageStoreService.class);
    private final DatastoreObjectFactory datastoreObjectFactory = locator.getFactory(DatastoreObjectFactory.class);

    /**
     * Gets the {@link DatastoreMessage} list in the scope.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param offset The result set offset.
     * @param limit The result set limit.
     * @return The {@link MessageListResult} of all the datastoreMessages associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the DatastoreMessage list in the scope", //
            notes = "Returns the list of all the datastoreMessages associated to the current selected scope.", //
            response = DatastoreMessage.class, //
            responseContainer = "DatastoreMessageListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public MessageListResult simpleQuery( @PathParam("scopeId") ScopeId scopeId,//
                                        @QueryParam("offset") @DefaultValue("0") int offset,//
                                        @QueryParam("limit") @DefaultValue("50") int limit) //
    {
        MessageListResult datastoreMessageListResult = datastoreObjectFactory.newDatastoreMessageListResult();
        try {
            MessageQuery query = datastoreObjectFactory.newDatastoreMessageQuery(scopeId);
                        
            query.setOffset(offset);
            query.setLimit(limit);
            
            datastoreMessageListResult = query(scopeId, query);
        } catch (Throwable t) {
            handleException(t);
        }
        return datastoreMessageListResult;
    }
    
    /**
     * Queries the results with the given {@link DatastorMessageQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to search results. 
     * @param query The {@link DatastorMessageQuery} to used to filter results.
     * @return The {@link MessageListResult} of all the result matching the given {@link DatastorMessageQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public MessageListResult query(@PathParam("scopeId") ScopeId scopeId, MessageQuery query) {
        MessageListResult datastoreMessageListResult = null;
        try {
            query.setScopeId(scopeId);
            datastoreMessageListResult = messageRegistryService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(datastoreMessageListResult);
    }
    
    /**
     * Counts the results with the given {@link DatastorMessageQuery} parameter.
     * 
     * @param scopeId The {@link ScopeId} in which to search results. 
     * @param query The {@link DatastorMessageQuery} to used to filter results.
     * @return The count of all the result matching the given {@link DatastorMessageQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CountResult count(@PathParam("scopeId") ScopeId scopeId, MessageQuery query) {
        CountResult countResult = null;
        try {
            query.setScopeId(scopeId);
            countResult = new CountResult(messageRegistryService.count(query));
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(countResult);
    }
    

    /**
     * Returns the DatastoreMessage specified by the "datastoreMessageId" path parameter.
     *
     * @param datastoreMessageId
     *            The id of the requested DatastoreMessage.
     * @return The requested DatastoreMessage object.
     */
    @ApiOperation(value = "Get an DatastoreMessage", notes = "Returns the DatastoreMessage specified by the \"datastoreMessageId\" path parameter.", response = DatastoreMessage.class)
    @GET
    @Path("{datastoreMessageId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DatastoreMessage find(@PathParam("scopeId") ScopeId scopeId, 
            @ApiParam(value = "The id of the requested DatastoreMessage", required = true) @PathParam("datastoreMessageId") StorableEntityId datastoreMessageId) {
        DatastoreMessage datastoreMessage = null;
        try {
            datastoreMessage = messageRegistryService.find(scopeId, datastoreMessageId, StorableFetchStyle.SOURCE_FULL);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(datastoreMessage);
    }
}
