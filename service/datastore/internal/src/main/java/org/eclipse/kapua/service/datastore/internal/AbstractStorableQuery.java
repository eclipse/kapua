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
package org.eclipse.kapua.service.datastore.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.datastore.internal.model.query.SortFieldImpl;
import org.eclipse.kapua.service.datastore.model.Storable;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;
import org.eclipse.kapua.service.datastore.model.query.StorableQuery;

/**
 * Abstract storable query implementation.
 * 
 * @since 1.0
 *
 * @param <S> persisted object type (such as messages, channeles information...)
 */
public abstract class AbstractStorableQuery<S extends Storable> implements StorableQuery<S>
{
    private StorablePredicate predicate = null;

    private int                limit;
    private Object             keyOffset;
    private int                indexOffset;
    private boolean            askTotalCount = false;
    private List<SortField>    sortFields;
    private StorableFetchStyle fetchStyle    = StorableFetchStyle.SOURCE_FULL;

    /**
     * Default constructor
     */
    public AbstractStorableQuery()
    {
        limit = 50;
        keyOffset = null;
        indexOffset = 0;
    }

    @Override
    public StorablePredicate getPredicate()
    {
        return this.predicate;
    }

    @Override
    public void setPredicate(StorablePredicate predicate)
    {
        this.predicate = predicate;
    }

    public Object getKeyOffset()
    {
        return keyOffset;
    }

    public void setKeyOffset(Object offset)
    {
        this.keyOffset = offset;
    }

    @Override
    public int getOffset()
    {
        return indexOffset;
    }

    @Override
    public void setOffset(int offset)
    {
        this.indexOffset = offset;
    }

    public int addIndexOffset(int delta)
    {
        indexOffset += delta;
        return indexOffset;
    }

    @Override
    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    @Override
    public int getLimit()
    {
        return limit;
    }

    @Override
    public boolean isAskTotalCount()
    {
        return askTotalCount;
    }

    @Override
    public void setAskTotalCount(boolean askTotalCount)
    {
        this.askTotalCount = askTotalCount;
    }

    @Override
    public List<SortField> getSortFields()
    {
        return sortFields;
    }

    @Override
    public void setSortFields(List<SortField> sortFields)
    {
        this.sortFields = sortFields;
    }

    @Override
    public StorableFetchStyle getFetchStyle()
    {
        return this.fetchStyle;
    }

    @Override
    public void setFetchStyle(StorableFetchStyle fetchStyle)
    {
        this.fetchStyle = fetchStyle;
    }

    @Override
    public void copy(StorableQuery<S> query)
    {
        this.setAskTotalCount(query.isAskTotalCount());
        this.setLimit(query.getLimit());
        this.setOffset(query.getOffset());
        this.setPredicate(query.getPredicate());
        // TODO extend copy to predicate (not by ref as now)
        this.setPredicate(query.getPredicate());
        this.setFetchStyle(query.getFetchStyle());
        this.setSortFields(copy(query.getSortFields()));
    }

    private List<SortField> copy(List<SortField> sortFields)
    {
        if (sortFields == null) {
            return null;
        }
        else {
            List<SortField> copySortFields = new ArrayList<SortField>();
            for (int i = 0; i < sortFields.size(); i++) {
                SortField original = sortFields.get(i);
                SortField copy = new SortFieldImpl();
                copy.setField(original.getField());
                copy.setSortDirection(original.getSortDirection());
                copySortFields.add(copy);
            }
            return copySortFields;
        }
    }

}
