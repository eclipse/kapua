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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.service.datastore.model.Storable;
import org.eclipse.kapua.service.datastore.model.query.MessageFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.SortDirection;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;
import org.eclipse.kapua.service.datastore.model.query.StorableQuery;

public abstract class AbstractStorableQuery<S extends Storable> implements StorableQuery<S>
{
    private StorablePredicate predicate = null;

    private int               limit;
    private Object            keyOffset;
    private int               indexOffset;
    private boolean           askTotalCount = false;
    private SortDirection     sortStyle     = SortDirection.DESC;
    private MessageFetchStyle fetchStyle    = MessageFetchStyle.METADATA_HEADERS_PAYLOAD;


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
    public MessageFetchStyle getFetchStyle()
    {
        return this.fetchStyle;
    }

    @Override
    public void setFetchStyle(MessageFetchStyle fetchStyle)
    {
        this.fetchStyle = fetchStyle;
    }

    @Override
    public SortDirection getSort()
    {
        return this.sortStyle;
    }

    @Override
    public void setSort(SortDirection sortStyle)
    {
        this.sortStyle = sortStyle;
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
        this.setSort(query.getSort());
    }
}
