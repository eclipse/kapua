/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.query;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.query.KapuaListResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * {@link KapuaListResult} implementation.
 *
 * @param <E> {@link KapuaEntity} domain
 * @since 1.0.0
 */
public class KapuaListResultImpl<E extends KapuaEntity> implements KapuaListResult<E> {

    private static final long serialVersionUID = 8939666089540269261L;

    private ArrayList<E> items;
    private boolean limitExceeded;
    private Long totalCount;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public KapuaListResultImpl() {
        limitExceeded = false;
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
        return getItems().get(index);
    }

    @Override
    public E getFirstItem() {
        return this.isEmpty() ? null : getItem(0);
    }

    @Override
    public int getSize() {
        return getItems().size();
    }

    @Override
    public boolean isEmpty() {
        return getItems().isEmpty();
    }

    @Override
    public List<E> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }

        return items;
    }

    @Override
    public void addItems(Collection<? extends E> items) {
        getItems().addAll(items);
    }

    @Override
    public void addItem(E item) {
        getItems().add(item);
    }

    @Override
    public void clearItems() {
        getItems().clear();
    }

    @Override
    public void sort(Comparator<E> comparator) {
        getItems().sort(comparator);
    }

    @Override
    public Long getTotalCount() {
        return totalCount;
    }

    @Override
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

}
