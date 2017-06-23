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
package org.eclipse.kapua.service.datastore.steps;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.schema.ChannelInfoSchema;
import org.eclipse.kapua.service.datastore.internal.schema.ClientInfoSchema;
import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.datastore.internal.schema.MetricInfoSchema;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;

import java.util.ArrayList;
import java.util.List;

import static org.eclipse.kapua.service.datastore.model.query.SortField.descending;

/**
 * Factory methods for creating base queries for Data store services.
 * It is used in steps of Datastore Gherkin scenarios.
 */
public class DatastoreQueryFactory {

    private DatastoreQueryFactory() {
        // Hiding utility class constructor.
    }

    /**
     * Creating query for data messages with reasonable defaults.
     *
     * @param scopeId
     *            scope
     * @param limit
     *            limit results
     * @return query
     */
    public static MessageQuery createBaseMessageQuery(KapuaId scopeId, int limit) {

        MessageQuery query = new MessageQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(limit);
        query.setOffset(0);
        List<SortField> order = new ArrayList<>();
        order.add(descending(MessageSchema.MESSAGE_TIMESTAMP));
        query.setSortFields(order);

        return query;
    }

    /**
     * Creating query for data messages with reasonable defaults and user specified ordering.
     *
     * @param scopeId scope
     * @param limit limit results
     * @param order the required result ordering
     * @return query
     */
    public static MessageQuery createBaseMessageQuery(KapuaId scopeId, int limit, List<SortField> order) {

        MessageQuery query = new MessageQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(limit);
        query.setOffset(0);
        query.setSortFields(order);

        return query;
    }

    /**
     * Creating query for channel info with reasonable defaults.
     *
     * @param scopeId
     *            scope
     * @param limit
     *            limit results
     * @return query
     */
    public static ChannelInfoQuery createBaseChannelInfoQuery(KapuaId scopeId, int limit) {

        ChannelInfoQuery query = new ChannelInfoQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(limit);
        query.setOffset(0);
        List<SortField> order = new ArrayList<>();
        order.add(descending(ChannelInfoSchema.CHANNEL_TIMESTAMP));
        query.setSortFields(order);

        return query;
    }

    /**
     * Creating query for metric info with reasonable defaults.
     *
     * @param scopeId
     *            scope
     * @param limit
     *            limit results
     * @return query
     */
    public static MetricInfoQuery createBaseMetricInfoQuery(KapuaId scopeId, int limit) {

        MetricInfoQuery query = new MetricInfoQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(limit);
        query.setOffset(0);
        List<SortField> order = new ArrayList<>();
        order.add(descending(MetricInfoSchema.METRIC_MTR_TIMESTAMP));
        query.setSortFields(order);

        return query;
    }

    /**
     * Creating query for client info with reasonable defaults.
     *
     * @param scopeId
     *            scope
     * @param limit
     *            limit results
     * @return query
     */
    public static ClientInfoQuery createBaseClientInfoQuery(KapuaId scopeId, int limit) {

        ClientInfoQuery query = new ClientInfoQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(limit);
        query.setOffset(0);
        List<SortField> order = new ArrayList<>();
        order.add(descending(ClientInfoSchema.CLIENT_TIMESTAMP));
        query.setSortFields(order);

        return query;
    }
}
