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
package org.eclipse.kapua.service.datastore.internal.model.query;

import java.net.UnknownHostException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsClient;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDatastoreException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.PredicateConverter;
import org.eclipse.kapua.service.datastore.model.Storable;
import org.eclipse.kapua.service.datastore.model.query.MessageFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.StorableQuery;
import org.elasticsearch.action.search.SearchRequestBuilder;

public abstract class AbstractStorableQueryConverter<S extends Storable, Q extends StorableQuery<S>>
{
    public SearchRequestBuilder toCountRequestBuilder(String indices, String type, Q query) throws KapuaException, EsDatastoreException, UnknownHostException
    {
        if (query == null)
            throw new EsDatastoreException(String.format("Query parameter is undefined"));

        PredicateConverter pc = new PredicateConverter();
        SearchRequestBuilder searchReqBuilder = EsClient.getcurrent().prepareSearch(indices);
        searchReqBuilder.setTypes(type)
                        .setQuery(pc.toElasticsearchQuery(query.getPredicate()))
                        .setSize(0);
        
        return searchReqBuilder;
    }

    public SearchRequestBuilder toSearchRequestBuilder(String indices, String type, Q query) throws KapuaException, EsDatastoreException, UnknownHostException
    {
        if (query == null)
            throw new EsDatastoreException(String.format("Query parameter is undefined"));

        PredicateConverter pc = new PredicateConverter();
        SearchRequestBuilder searchReqBuilder = EsClient.getcurrent().prepareSearch(indices);
        searchReqBuilder.setTypes(type)
                        .setQuery(pc.toElasticsearchQuery(query.getPredicate()))
                        .setFrom(query.getOffset())
                        .setSize(query.getLimit());
        
        String[] includes = this.getIncludes(query.getFetchStyle());
        String[] excludes = this.getExcludes(query.getFetchStyle());
        if (includes != null || excludes != null)
            searchReqBuilder.setFetchSource(includes, excludes);
        
        if (this.getFields() != null && this.getFields().length > 0)
            searchReqBuilder.addFields(this.getFields());
            
        
        return searchReqBuilder;
    }
    
    protected abstract String[] getIncludes(MessageFetchStyle fetchStyle);
    
    protected abstract String[] getExcludes(MessageFetchStyle fetchStyle);
    
    protected abstract String[] getFields();
}
