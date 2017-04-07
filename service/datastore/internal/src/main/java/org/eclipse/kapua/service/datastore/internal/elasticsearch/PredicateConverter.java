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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.ChannelMatchPredicate;
import org.eclipse.kapua.service.datastore.model.query.ExistsPredicate;
import org.eclipse.kapua.service.datastore.model.query.IdsPredicate;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;
import org.eclipse.kapua.service.datastore.model.query.StorableQuery;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.ExistsQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import com.google.common.base.Strings;

/**
 * Query predicate converter from Kapua abstraction layer to Elasticsearch domain.
 *
 * @since 1.0.0
 */
public class PredicateConverter {

    private static final DatastoreObjectFactory datastoreObjectFactory = KapuaLocator.getInstance().getFactory(DatastoreObjectFactory.class);

    /**
     * Converts the Kapua {@link StorablePredicate}s in the {@link StorableQuery} parameter in Elasticsearch {@link QueryBuilder}.
     *
     * @param query
     *            The {@link StorableQuery} the contains the {@link StorablePredicate}s to convert.
     * @return The converted {@link QueryBuilder}.
     * @throws EsQueryConversionException
     * @since 1.0.0
     */
    public static QueryBuilder convertQueryPredicates(StorableQuery<?> query)
            throws EsQueryConversionException {

        //
        // Force the ScopeId predicate in order to partition data by it.
        AndPredicate andPredicate = new AndPredicateImpl();
        andPredicate.getPredicates().add(datastoreObjectFactory.newTermPredicate(ChannelInfoField.SCOPE_ID, query.getScopeId().toCompactId()));
        if(query.getPredicate() != null){
            andPredicate.getPredicates().add(query.getPredicate());
        }
        return toElasticsearchQuery(andPredicate);
    }

    /**
     * Convert the Kapua {@link StorablePredicate} to Elasticsearch {@link QueryBuilder}
     *
     * @param predicate
     * @return
     * @throws EsQueryConversionException
     *             if the predicate is unknown or some exception is raised in the specific conversion operation
     * @since 1.0.0
     */
    private static QueryBuilder toElasticsearchQuery(StorablePredicate predicate)
            throws EsQueryConversionException {
        if (predicate == null)
            throw new EsQueryConversionException("Predicate parameter is undefined");

        if (predicate instanceof AndPredicate)
            return toElasticsearchQuery((AndPredicate) predicate);

        if (predicate instanceof IdsPredicate)
            return toElasticsearchQuery((IdsPredicate) predicate);

        if (predicate instanceof ChannelMatchPredicate)
            return toElasticsearchQuery((ChannelMatchPredicate) predicate);

        if (predicate instanceof RangePredicate)
            return toElasticsearchQuery((RangePredicate) predicate);

        if (predicate instanceof TermPredicate)
            return toElasticsearchQuery((TermPredicate) predicate);

        if (predicate instanceof ExistsPredicate)
            return toElasticsearchQuery((ExistsPredicate) predicate);

        throw new EsQueryConversionException(String.format("Unknown predicate type %s", predicate.getClass().getName()));
    }

    /**
     * Convert the Kapua {@link AndPredicate} to the specific Elasticsearch {@link QueryBuilder}
     *
     * @param predicate
     * @return
     * @throws EsQueryConversionException
     * @since 1.0.0
     */
    private static QueryBuilder toElasticsearchQuery(AndPredicate predicate)
            throws EsQueryConversionException {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (StorablePredicate subPredicate : predicate.getPredicates())
            boolQuery.must(toElasticsearchQuery(subPredicate));

        return boolQuery;
    }

    /**
     * Convert the Kapua {@link IdsPredicate} to the specific Elasticsearch {@link QueryBuilder}
     *
     * @param predicate
     * @return
     * @throws EsQueryConversionException
     * @since 1.0.0
     */
    private static QueryBuilder toElasticsearchQuery(IdsPredicate predicate)
            throws EsQueryConversionException {
        if (predicate == null)
            throw new EsQueryConversionException(String.format("Predicate parameter is undefined"));

        Set<StorableId> ids = predicate.getIdSet();
        Set<String> stringIds = new HashSet<String>(ids.size());
        for (StorableId id : ids)
            stringIds.add(id.toString());

        QueryBuilder idsQuery = QueryBuilders.idsQuery(predicate.getType()).addIds(stringIds.toArray(new String[] {}));

        return idsQuery;
    }

    /**
     * Convert the Kapua {@link ChannelMatchPredicate} to the specific Elasticsearch {@link QueryBuilder}
     *
     * @param predicate
     * @return
     * @throws EsQueryConversionException
     * @since 1.0.0
     */
    private static QueryBuilder toElasticsearchQuery(ChannelMatchPredicate predicate)
            throws EsQueryConversionException {
        if (predicate == null || Strings.isNullOrEmpty(predicate.getExpression()))
            throw new EsQueryConversionException("Predicate parameter is undefined");

        String channelString = predicate.getExpression();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (DatastoreChannel.isWildcardChannel(channelString)) {
            PrefixQueryBuilder prefixFilter = QueryBuilders.prefixQuery(MessageField.CHANNEL.field(), channelString.substring(0, channelString.length() - 1));
            boolQuery.filter(prefixFilter);
        } else if (!DatastoreChannel.isAnyChannel(channelString)) {
            boolQuery.must(QueryBuilders.termQuery(MessageField.CHANNEL.field(), channelString));
        }

        return boolQuery;
    }

    /**
     * Convert the Kapua {@link RangePredicate} to the specific Elasticsearch {@link QueryBuilder}
     *
     * @param predicate
     * @return
     * @throws EsQueryConversionException
     * @since 1.0.0
     */
    private static QueryBuilder toElasticsearchQuery(RangePredicate predicate)
            throws EsQueryConversionException {
        if (predicate == null)
            throw new EsQueryConversionException("Predicate parameter is undefined");

        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(predicate.getField().field());
        if (predicate.getMinValue() != null)
            rangeQuery.from(predicate.getMinValue());
        if (predicate.getMaxValue() != null)
            rangeQuery.to(predicate.getMaxValue());

        return rangeQuery;
    }

    /**
     * Convert the Kapua {@link TermPredicate} to the specific Elasticsearch {@link QueryBuilder}
     *
     * @param predicate
     * @return
     * @throws EsQueryConversionException
     * @since 1.0.0
     */
    private static QueryBuilder toElasticsearchQuery(TermPredicate predicate)
            throws EsQueryConversionException {
        if (predicate == null)
            throw new EsQueryConversionException("Predicate parameter is undefined");

        TermQueryBuilder termQuery = QueryBuilders.termQuery(predicate.getField().field(), predicate.getValue());

        return termQuery;
    }

    /**
     * Convert the Kapua {@link ExistsPredicate} to the specific Elasticsearch {@link QueryBuilder}
     *
     * @param predicate
     * @return
     * @throws EsQueryConversionException
     * @since 1.0.0
     */
    private static QueryBuilder toElasticsearchQuery(ExistsPredicate predicate)
            throws EsQueryConversionException {
        if (predicate == null)
            throw new EsQueryConversionException(String.format("Predicate parameter is undefined"));

        ExistsQueryBuilder existsQuery = QueryBuilders.existsQuery(predicate.getName());

        return existsQuery;
    }

}
