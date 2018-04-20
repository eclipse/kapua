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
 *******************************************************************************/
package org.eclipse.kapua.commons.model.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.query.KapuaListResult;

/**
 * Query list result reference implementation.
 * 
 * @param <E>
 *            query entity domain
 * 
 * @since 1.0
 * 
 */
public class KapuaListResultImpl<E extends KapuaEntity> implements KapuaListResult<E> {

    private static final long serialVersionUID = 8939666089540269261L;
    private boolean limitExceeded;
    private ArrayList<E> items;

    /**
     * Constructor
     */
    public KapuaListResultImpl() {
        limitExceeded = false;
        items = new ArrayList<>();
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
    public void sort(Comparator<E> comparator) {
        this.items.sort(comparator);
    }
}
