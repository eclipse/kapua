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
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MetricInfoField;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelMatchPredicateImpl;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.ChannelMatchPredicate;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;

import com.google.common.base.Strings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("Data Metrics")
@Path("{scopeId}/data/metrics")
public class DataMetrics extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final MetricInfoRegistryService metricInfoRegistryService = locator.getService(MetricInfoRegistryService.class);
    private final DatastoreObjectFactory datastoreObjectFactory = locator.getFactory(DatastoreObjectFactory.class);

    /**
     * Gets the {@link MetricInfo} list in the scope.
     *
     * @param scopeId  The {@link ScopeId} in which to search results.
     * @param clientId The client id to filter results.
     * @param channel  The channel id to filter results. It allows '#' wildcard in last channel level
     * @param name     The metric name to filter results
     * @param offset   The result set offset.
     * @param limit    The result set limit.
     * @return The {@link MetricInfoListResult} of all the metricInfos associated to the current selected scope.
     * @since 1.0.0
     */
    @ApiOperation(value = "Gets the MetricInfo list in the scope", //
            notes = "Returns the list of all the metricInfos associated to the current selected scope.", //
            response = MetricInfo.class, //
            responseContainer = "MetricInfoListResult")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public MetricInfoListResult simpleQuery( //
            @ApiParam(value = "The ScopeId in which to search results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId,//
            @ApiParam(value = "The client id to filter results") @QueryParam("clientId") String clientId, //
            @ApiParam(value = "The channel to filter results. It allows '#' wildcard in last channel level") @QueryParam("channel") String channel,
            @ApiParam(value = "The metric name to filter results") @QueryParam("name") String name,
            @ApiParam(value = "The result set offset", defaultValue = "0") @QueryParam("offset") @DefaultValue("0") int offset,//
            @ApiParam(value = "The result set limit", defaultValue = "50") @QueryParam("limit") @DefaultValue("50") int limit) //
    {
        MetricInfoListResult metricInfoListResult = datastoreObjectFactory.newMetricInfoListResult();
        try {
            AndPredicate andPredicate = new AndPredicateImpl();
            if (!Strings.isNullOrEmpty(clientId)) {
                TermPredicate clientIdPredicate = datastoreObjectFactory.newTermPredicate(MetricInfoField.CLIENT_ID, clientId);
                andPredicate.getPredicates().add(clientIdPredicate);
            }

            if (!Strings.isNullOrEmpty(channel)) {
                ChannelMatchPredicate channelPredicate = new ChannelMatchPredicateImpl(channel);
                andPredicate.getPredicates().add(channelPredicate);
            }

            if (!Strings.isNullOrEmpty(name)) {
                TermPredicate clientIdPredicate = datastoreObjectFactory.newTermPredicate(MetricInfoField.NAME_FULL, name);
                andPredicate.getPredicates().add(clientIdPredicate);
            }

            MetricInfoQuery query = datastoreObjectFactory.newMetricInfoQuery(scopeId);
            query.setPredicate(andPredicate);
            query.setOffset(offset);
            query.setLimit(limit);

            metricInfoListResult = query(scopeId, query);
        } catch (Throwable t) {
            handleException(t);
        }
        return metricInfoListResult;
    }

    /**
     * Queries the results with the given {@link MetricInfoQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link MetricInfoQuery} to used to filter results.
     * @return The {@link MetricInfoListResult} of all the result matching the given {@link MetricInfoQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Queries the MetricInfos", //
            notes = "Queries the MetricInfos with the given MetricInfoQuery parameter returning all matching MetricInfos",  //
            response = MetricInfo.class, //
            responseContainer = "MetricInfoListResult")
    public MetricInfoListResult query( //
            @ApiParam(value = "The ScopeId in which to search results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The MetricInfoQuery to use to filter results", required = true) MetricInfoQuery query) {
        MetricInfoListResult metricInfoListResult = null;
        try {
            query.setScopeId(scopeId);
            metricInfoListResult = metricInfoRegistryService.query(query);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(metricInfoListResult);
    }

    /**
     * Counts the results with the given {@link MetricInfoQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link MetricInfoQuery} to used to filter results.
     * @return The count of all the result matching the given {@link MetricInfoQuery} parameter.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @ApiOperation(value = "Counts the MetricInfos", //
            notes = "Counts the MetricInfos with the given MetricInfoQuery parameter returning the number of matching MetricInfos", //
            response = CountResult.class)
    public CountResult count( //
            @ApiParam(value = "The ScopeId in which to search results", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The MetricInfoQuery to use to filter count results", required = true) MetricInfoQuery query) {
        CountResult countResult = null;
        try {
            query.setScopeId(scopeId);
            countResult = new CountResult(metricInfoRegistryService.count(query));
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(countResult);
    }

    /**
     * Returns the MetricInfo specified by the "metricInfoId" path parameter.
     *
     * @param metricInfoId The id of the requested MetricInfo.
     * @return The requested MetricInfo object.
     */
    @GET
    @Path("{metricInfoId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ApiOperation(value = "Gets an MetricInfo", //
            notes = "Gets the MetricInfo specified by the metricInfoId path parameter", //
            response = MetricInfo.class)
    public MetricInfo find( //
            @ApiParam(value = "The ScopeId of the requested MetricInfo.", required = true, defaultValue = DEFAULT_SCOPE_ID) @PathParam("scopeId") ScopeId scopeId, //
            @ApiParam(value = "The id of the requested MetricInfo", required = true) @PathParam("metricInfoId") StorableEntityId metricInfoId) {
        MetricInfo metricInfo = null;
        try {
            metricInfo = metricInfoRegistryService.find(scopeId, metricInfoId);
        } catch (Throwable t) {
            handleException(t);
        }
        return returnNotNullEntity(metricInfo);
    }
}
