/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.IdsPredicate;
import org.eclipse.kapua.service.datastore.model.query.TopicMatchPredicate;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

public class PredicateConverter
{
    public QueryBuilder toElasticsearchQuery(StorablePredicate predicate)
        throws KapuaException
    {
        if (predicate == null)
            throw KapuaException.internalError(String.format("Predicate parameter is undefined"));

        if (predicate instanceof AndPredicate)
            return toElasticsearchQuery(predicate);

        if (predicate instanceof IdsPredicate)
            return toElasticsearchQuery(predicate);

        if (predicate instanceof TopicMatchPredicate)
            return toElasticsearchQuery(predicate);

        if (predicate instanceof RangePredicate)
            return toElasticsearchQuery(predicate);

        if (predicate instanceof TermPredicate)
            return toElasticsearchQuery(predicate);

        throw KapuaException.internalError(String.format("Unknown predicate type %s", this.getClass().getName()));
    }
    
    public QueryBuilder toElasticsearchQuery(AndPredicate predicate) throws KapuaException 
    {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (StorablePredicate subPredicate:predicate.getPredicates())
            boolQuery.must(this.toElasticsearchQuery(subPredicate));

        return boolQuery;
    }
    
    public QueryBuilder toElasticsearchQuery(IdsPredicate predicate) throws KapuaException 
    {
        if (predicate == null)
            throw KapuaException.internalError(String.format("Predicate parameter is undefined"));

        Set<StorableId> ids = predicate.getIdSet();
        Set<String> stringIds = new HashSet<String>(ids.size());
        for(StorableId id:ids)
            stringIds.add(id.toString());
        
        QueryBuilder idsQuery = QueryBuilders.idsQuery(stringIds.toArray(new String[] {}));

        return idsQuery;
    }
    
    public QueryBuilder toElasticsearchQuery(TopicMatchPredicate predicate) throws KapuaException
    {
        if (predicate == null)
            throw KapuaException.internalError(String.format("Predicate parameter is undefined"));

        KapuaTopic kapuaTopic = null;
        try 
        {
            kapuaTopic = new KapuaTopic(predicate.getExpression());
        }
        catch (KapuaInvalidTopicException e)
        {
            throw KapuaException.internalError(e);
        }
        
        if (!kapuaTopic.isKapuaTopic())
            throw KapuaException.internalError(String.format("Not a kapua topic %s", predicate.getExpression()));

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        
        if (kapuaTopic.isAnyAccount())
            boolQuery.must(QueryBuilders.termQuery(EsMessageField.ACCOUNT.toString(), kapuaTopic.getAccount()));

        if (kapuaTopic.isAnyAsset())
            boolQuery.must(QueryBuilders.termQuery(EsMessageField.ASSET.toString(), kapuaTopic.getAsset()));

        if (kapuaTopic.isAnySubtopic())
            boolQuery.must(QueryBuilders.termQuery(EsMessageField.SEMANTIC_TOPIC.toString(), kapuaTopic.getSemanticTopic()));

        return boolQuery;
    }
    
    public QueryBuilder toElasticsearchQuery(RangePredicate predicate) throws KapuaException 
    {
        if (predicate == null)
            throw KapuaException.internalError(String.format("Predicate parameter is undefined"));
       
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(predicate.getField().toString());
        if (predicate.getMinValue() != null)
            rangeQuery.from(predicate.getMinValue());
        if (predicate.getMaxValue() != null)
            rangeQuery.to(predicate.getMaxValue());
       
        return rangeQuery;
    }
    
    public QueryBuilder toElasticsearchQuery(TermPredicate predicate) throws KapuaException 
    {
        if (predicate == null)
            throw KapuaException.internalError(String.format("Predicate parameter is undefined"));
       
        TermQueryBuilder termQuery = QueryBuilders.termQuery(predicate.getField().toString(), predicate.getValue());
       
        return termQuery;
    }
}
