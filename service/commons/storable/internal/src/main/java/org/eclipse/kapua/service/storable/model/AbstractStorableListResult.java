/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.storable.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link StorableListResult} {@code abstract} implementation.
 * <p>
 * This is the base for all {@link StorableListResult}'s implementations.
 *
 * @param <E> The {@link Storable} for which this is a {@link StorableListResult} for.
 * @since 1.0.0
 */
public class AbstractStorableListResult<E extends Storable> implements StorableListResult<E> {

    private static final long serialVersionUID = -6792613517586602315L;

    private boolean limitExceeded;
    private ArrayList<E> items;
    private Object nextKey;
    private Long totalCount;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public AbstractStorableListResult() {
    }

    /**
     * Constructor.
     *
     * @param storables  The {@link Storable}s to add to the {@link StorableListResult}.
     * @param totalCount The total count of the {@link Storable}s matched.
     * @since 1.3.0
     */
    public AbstractStorableListResult(List<E> storables, Long totalCount) {
        this();

        addItems(storables);
        setTotalCount(totalCount);
    }

    /**
     * Constructors.
     *
     * @param nextKey The {@link StorableListResult#getNextKey()}.
     * @since 1.0.0
     * @deprecated Since 1.3.0, this is not used!
     */
    @Deprecated
    public AbstractStorableListResult(Object nextKey) {
        this();
        this.nextKey = nextKey;
    }

    /**
     * Constructor.
     *
     * @param nextKeyOffset The {@link StorableListResult#getNextKey()}.
     * @param totalCount    The {@link StorableListResult#getTotalCount()}.
     * @since 1.0.0
     * @deprecated Since 1.3.0, this is not used!
     */
    @Deprecated
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
    public Object getNextKey() {
        return nextKey;
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
