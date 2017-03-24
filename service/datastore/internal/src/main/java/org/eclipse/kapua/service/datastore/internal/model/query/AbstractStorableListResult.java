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
package org.eclipse.kapua.service.datastore.internal.model.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.kapua.service.datastore.model.Storable;
import org.eclipse.kapua.service.datastore.model.StorableListResult;

/**
 * Storable object list implementation
 * 
 * @since 1.0.0
 *
 * @param <E>
 */
public class AbstractStorableListResult<E extends Storable> implements StorableListResult<E> {

    private boolean limitExceeded;
    private ArrayList<E> items;
    private Object nextKey;
    private Long totalCount;

    /**
     * Default constructor.
     * 
     * @since 1.0.0
     */
    public AbstractStorableListResult() {
        items = new ArrayList<>();
        nextKey = null;
        totalCount = null;
    }

    /**
     * Construct a result list linking the next result list
     * 
     * @param nextKey
     * 
     * @since 1.0.0
     */
    public AbstractStorableListResult(Object nextKey) {
        this();
        this.nextKey = nextKey;
    }

    /**
     * Construct a result list linking the next result list and setting the total count
     * 
     * @param nextKeyOffset
     * @param totalCount
     * 
     * @since 1.0.0
     */
    public AbstractStorableListResult(Object nextKeyOffset, Long totalCount) {
        this(nextKeyOffset);
        this.totalCount = totalCount;
    }

    @Override
    public boolean isLimitExceeded() {
        return limitExceeded;
    }

    @Override
    public void setLimitExceeded(boolean limitExceeded) {
        this.limitExceeded = limitExceeded;
    }

    @Override
    public E getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public E getFirstItem() {
        return this.isEmpty() ? null : getItem(0);
    }

    @Override
    public int getSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public List<E> getItems() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public void addItems(Collection<? extends E> items) {
        this.items.addAll(items);
    }

    @Override
    public void clearItems() {
        this.items.clear();
    }

    @Override
    public Object getNextKey() {
        return nextKey;
    }

    @Override
    public Long getTotalCount() {
        return totalCount;
    }

    /**
     * Set the total count
     * 
     * @param totalCount
     */
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

}
