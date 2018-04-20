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

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.swagger.annotations.Authorization;

import org.eclipse.kapua.app.api.resources.v1.resources.model.CountResult;
import org.eclipse.kapua.app.api.resources.v1.resources.model.DateParam;
import org.eclipse.kapua.app.api.resources.v1.resources.model.MetricType;
import org.eclipse.kapua.app.api.resources.v1.resources.model.ScopeId;
import org.eclipse.kapua.app.api.resources.v1.resources.model.StorableEntityId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.client.model.InsertResponse;
import org.eclipse.kapua.service.datastore.internal.mediator.ChannelInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.MetricPredicate;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicateFactory;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;

import com.google.common.base.Strings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "Data Messages", authorizations = { @Authorization(value = "kapuaAccessToken") })
@Path("{scopeId}/data/messages")
public class DataMessages extends AbstractKapuaResource {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final MessageStoreService MESSAGE_STORE_SERVICE = LOCATOR.getService(MessageStoreService.class);
    private static final DatastoreObjectFactory DATASTORE_OBJECT_FACTORY = LOCATOR.getFactory(DatastoreObjectFactory.class);
    private static final StorablePredicateFactory STORABLE_PREDICATE_FACTORY = LOCATOR.getFactory(StorablePredicateFactory.class);

    /**
     * Gets the {@link DatastoreMessage} list in the scope.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param clientId
     *            The client id to filter results.
     * @param channel
     *            The channel id to filter results. It allows '#' wildcard in last channel level.
     * @param strictChannel
     *            Restrict the search only to this channel ignoring its children. Only meaningful if channel is set.
     * @param startDateParam
     *            The start date to filter the results. Must come before endDate parameter.
     * @param endDateParam
     *            The end date to filter the results. Must come after startDate parameter
     * @param offset
     *            The result set offset.
     * @param limit
     *            The result set limit.
     * @return The {@link MessageListResult} of all the datastoreMessages associated to the current selected scope.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(nickname = "dataMessageSimpleQuery",
            value = "Gets the DatastoreMessage list in the scope", //
            notes = "Returns the list of all the datastoreMessages associated to the current selected scope.", //
            response = MessageListResult.class)
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public <V extends Comparable<V>> MessageListResult simpleQuery(  //
            @ApiParam(value = "The ScopeId in which to search results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,//
            @ApiParam(value = "The client id to filter results") @QueryParam("clientId") String clientId, //
            @ApiParam(value = "The channel to filter results.") @QueryParam("channel") String channel,
            @ApiParam(value = "Restrict the search only to this channel ignoring its children. Only meaningful if channel is set.") @QueryParam("strictChannel") boolean strictChannel,
            @ApiParam(value = "The start date to filter the results. Must come before endDate parameter") @QueryParam("startDate") DateParam startDateParam,
            @ApiParam(value = "The end date to filter the results. Must come after startDate parameter") @QueryParam("endDate") DateParam endDateParam,
            @ApiParam(value = "The metric name to filter results") @QueryParam("metricName") String metricName, //
            @ApiParam(value = "The metric type to filter results") @QueryParam("metricType") MetricType<V> metricType, //
            @ApiParam(value = "The min metric value to filter results") @QueryParam("metricMin") String metricMinValue, //
            @ApiParam(value = "The max metric value to filter results") @QueryParam("metricMax") String metricMaxValue, //
            @ApiParam(value = "The result set offset", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,//
            @ApiParam(value = "The result set limit", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) throws Exception {

        AndPredicate andPredicate = STORABLE_PREDICATE_FACTORY.newAndPredicate();
        if (!Strings.isNullOrEmpty(clientId)) {
            TermPredicate clientIdPredicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(MessageField.CLIENT_ID, clientId);
            andPredicate.getPredicates().add(clientIdPredicate);
        }

        if (!Strings.isNullOrEmpty(channel)) {
            StorablePredicate channelPredicate = null;
            if (strictChannel) {
                channelPredicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(ChannelInfoField.CHANNEL, channel);
            } else {
                channelPredicate = STORABLE_PREDICATE_FACTORY.newChannelMatchPredicate(channel);
            }
            andPredicate.getPredicates().add(channelPredicate);
        }

        Date startDate = startDateParam != null ? startDateParam.getDate() : null;
        Date endDate = endDateParam != null ? endDateParam.getDate() : null;
        if (startDate != null || endDate != null) {
            RangePredicate timestampPredicate = STORABLE_PREDICATE_FACTORY.newRangePredicate(ChannelInfoField.TIMESTAMP.field(), startDate, endDate);
            andPredicate.getPredicates().add(timestampPredicate);
        }

        if (!Strings.isNullOrEmpty(metricName)) {
            V minValue = (V) ObjectValueConverter.fromString(metricMinValue, metricType.getType());
            V maxValue = (V) ObjectValueConverter.fromString(metricMaxValue, metricType.getType());

            MetricPredicate metricPredicate = STORABLE_PREDICATE_FACTORY.newMetricPredicate(metricName, metricType.getType(), minValue, maxValue);
            andPredicate.getPredicates().add(metricPredicate);
        }

        MessageQuery query = DATASTORE_OBJECT_FACTORY.newDatastoreMessageQuery(scopeId);
        query.setPredicate(andPredicate);
        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Stores a new Message under the account of the currently connected user.
     * In this case, the provided message will only be stored in the back-end
     * database and it will not be forwarded to the message broker.
     *
     * @param message The {@link KapuaDataMessage } to be stored
     * @return an {@link InsertResponse} object encapsulating the response from
     *         the datastore
     * @throws Exception
     *         Whenever something bad happens. See specific
     *         {@link KapuaService} exceptions.
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(nickname = "dataMessageStore",
            value = "Stores a new KapuaDataMessage", //
            notes = "Stores a new KapuaDataMessage under the account of the currently connected user. In this case, the provided message will only be stored in the back-end database and it will not be forwarded to the message broker.", //
            response = StorableId.class)
    public StorableEntityId storeMessage(
            @ApiParam(value = "The ScopeId in which to store the message", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,//
            @ApiParam(value = "The KapuaDataMessage to be stored") KapuaDataMessage message) throws Exception {
        message.setScopeId(scopeId);
        return new StorableEntityId(MESSAGE_STORE_SERVICE.store(message).toString());
    }

    /**
     * Queries the results with the given {@link MessageQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link MessageQuery} to used to filter results.
     * @return The {@link MessageListResult} of all the result matching the given {@link MessageQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @ApiOperation(nickname = "dataMessageQuery",
            value = "Queries the DatastoreMessages", //
            notes = "Queries the DatastoreMessages with the given DatastoreMessageQuery parameter returning all matching DatastoreMessages",  //
            response = MessageListResult.class)
    public MessageListResult query( //
            @ApiParam(value = "The ScopeId in which to search results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The DatastoreMessageQuery to use to filter results", required = true) MessageQuery query) throws Exception {
        query.setScopeId(scopeId);

        return MESSAGE_STORE_SERVICE.query(query);
    }

    /**
     * Counts the results with the given {@link MessageQuery} parameter.
     *
     * @param scopeId
     *            The {@link ScopeId} in which to search results.
     * @param query
     *            The {@link MessageQuery} to used to filter results.
     * @return The count of all the result matching the given {@link MessageQuery} parameter.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @ApiOperation(nickname = "dataMessageCount",
            value = "Counts the DatastoreMessages", //
            notes = "Counts the DatastoreMessages with the given DatastoreMessageQuery parameter returning the number of matching DatastoreMessages", //
            response = CountResult.class)
    public CountResult count( //
            @ApiParam(value = "The ScopeId in which to search results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The DatastoreMessageQuery to use to filter count results", required = true) MessageQuery query) throws Exception {
        query.setScopeId(scopeId);

        return new CountResult(MESSAGE_STORE_SERVICE.count(query));
    }

    /**
     * Returns the DatastoreMessage specified by the "datastoreMessageId" path parameter.
     *
     * @param datastoreMessageId
     *            The id of the requested DatastoreMessage.
     * @return The requested DatastoreMessage object.
     * @throws Exception
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{datastoreMessageId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(nickname = "dataMessageFind",
            value = "Gets an DatastoreMessage", //
            notes = "Gets the DatastoreMessage specified by the datastoreMessageId path parameter", //
            response = DatastoreMessage.class)
    public DatastoreMessage find( //
            @ApiParam(value = "The ScopeId of the requested DatastoreMessage.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,
            @ApiParam(value = "The id of the requested DatastoreMessage", required = true) @PathParam("datastoreMessageId") StorableEntityId datastoreMessageId) throws Exception {
        DatastoreMessage datastoreMessage = MESSAGE_STORE_SERVICE.find(scopeId, datastoreMessageId, StorableFetchStyle.SOURCE_FULL);

        return returnNotNullEntity(datastoreMessage);
    }
}
