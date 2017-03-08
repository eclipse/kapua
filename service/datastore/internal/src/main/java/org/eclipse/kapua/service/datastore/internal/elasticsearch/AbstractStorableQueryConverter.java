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

import org.eclipse.kapua.service.datastore.model.Storable;
import org.eclipse.kapua.service.datastore.model.query.SortDirection;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.StorableQuery;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

/**
 * Abstract storable query converter implementation.<br>
 * This object defines common method to all the query converter used by Kapua.
 *
 * @param <S> persisted object type (such as messages, channeles information...)
 * @param <Q>
 */
public abstract class AbstractStorableQueryConverter<S extends Storable, Q extends StorableQuery<S>>
{

    /**
     * Convert to a count query
     * 
     * @param indices
     * @param type
     * @param query
     * @return
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     */
    public SearchRequestBuilder toCountRequestBuilder(String indices, String type, Q query)
        throws EsQueryConversionException, EsClientUnavailableException
    {
        if (query == null)
            throw new NullPointerException(String.format("Query parameter is undefined"));

        PredicateConverter pc = new PredicateConverter();
        SearchRequestBuilder searchReqBuilder = ElasticsearchClient.getInstance().prepareSearch(indices);
        searchReqBuilder.setTypes(type)
                        .setQuery(pc.toElasticsearchQuery(query.getPredicate()))
                        .setSize(0);

        return searchReqBuilder;
    }

    /**
     * Convert to a search query
     * 
     * @param indices
     * @param type
     * @param query
     * @return
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     */
    public SearchRequestBuilder toSearchRequestBuilder(String indices, String type, Q query)
        throws EsQueryConversionException, EsClientUnavailableException
    {
        if (query == null)
            throw new NullPointerException(String.format("Query parameter is undefined"));

        PredicateConverter pc = new PredicateConverter();
        SearchRequestBuilder searchReqBuilder = ElasticsearchClient.getInstance().prepareSearch(indices);
        searchReqBuilder.setTypes(type)
                        .setQuery(pc.toElasticsearchQuery(query.getPredicate()));
        if (query.getSortFields() != null) {
            for (SortField sf : query.getSortFields()) {
                if (sf.getSortDirection() == null) {
                    throw new NullPointerException(String.format("The order for the field [%s] is undefined!", sf.getField()));
                }
                FieldSortBuilder fsb = SortBuilders.fieldSort(sf.getField());
                if (SortDirection.ASC.equals(sf.getSortDirection())) {
                    fsb.order(SortOrder.ASC);
                }
                else {
                    fsb.order(SortOrder.DESC);
                }
                searchReqBuilder.addSort(fsb);
            }
        }
        searchReqBuilder.setFrom(query.getOffset())
                        .setSize(query.getLimit());

        String[] includes = this.getIncludes(query.getFetchStyle());
        String[] excludes = this.getExcludes(query.getFetchStyle());
        if (includes != null || excludes != null)
            searchReqBuilder.setFetchSource(includes, excludes);

        if (this.getFields() != null && this.getFields().length > 0)
            searchReqBuilder.addFields(this.getFields());

        return searchReqBuilder;
    }

    /**
     * Query included fields list.<br>
     * The list may use the fetchStyle parameter to differentiate the included fields list (depending on the type of the entity)
     * 
     * @param fetchStyle
     * @return
     */
    protected abstract String[] getIncludes(StorableFetchStyle fetchStyle);

    /**
     * Query excluded fields list.<br>
     * The list may use the fetchStyle parameter to differentiate the excluded fields list (depending on the type of the entity)
     * 
     * @param fetchStyle
     * @return
     */
    protected abstract String[] getExcludes(StorableFetchStyle fetchStyle);

    /**
     * Query fields list
     * 
     * @return
     */
    protected abstract String[] getFields();
}
