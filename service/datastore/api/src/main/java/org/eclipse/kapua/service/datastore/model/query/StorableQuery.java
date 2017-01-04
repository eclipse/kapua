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
package org.eclipse.kapua.service.datastore.model.query;

import java.util.List;

import org.eclipse.kapua.service.datastore.model.Storable;

/**
 * Storable query definition.<br>
 * It defines the queries applicable to the persisted objects (such as messages, channeles information...)
 * 
 * @since 1.0
 *
 * @param <S> persisted object type (such as messages, channeles information...)
 */
public interface StorableQuery<S extends Storable>
{
    /**
     * Get the predicate
     * 
     * @return
     */
    public StorablePredicate getPredicate();

    /**
     * Set the predicate
     * 
     * @param predicate
     */
    public void setPredicate(StorablePredicate predicate);

    /**
     * Get the query result list offset
     * 
     * @return
     */
    public int getOffset();

    /**
     * Set the query result list offset
     * 
     * @param offset
     */
    public void setOffset(int offset);

    /**
     * Get the result list limit count
     * 
     * @return
     */
    public int getLimit();

    /**
     * Set the result list limit count
     * 
     * @param limit
     */
    public void setLimit(int limit);

    /**
     * Get the ask for the total count matching query objects
     * 
     * @return
     */
    public boolean isAskTotalCount();

    /**
     * Set the ask for the total count matching query objects
     * 
     * @param askTotalCount
     */
    public void setAskTotalCount(boolean askTotalCount);

    /**
     * Get the fetch style
     * 
     * @return
     */
    public StorableFetchStyle getFetchStyle();

    /**
     * Set the fetch style
     * 
     * @param fetchStyle
     */
    public void setFetchStyle(StorableFetchStyle fetchStyle);

    /**
     * Get the sort fields list
     * 
     * @return
     */
    public List<SortField> getSortFields();

    /**
     * Set the sort fields list
     * 
     * @param sortFields
     */
    public void setSortFields(List<SortField> sortFields);

    /**
     * Create (and keep it internally) a copy of the given query
     * 
     * @param query
     */
    public void copy(StorableQuery<S> query);
}
