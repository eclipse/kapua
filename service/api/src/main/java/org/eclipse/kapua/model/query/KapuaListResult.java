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
package org.eclipse.kapua.model.query;

import org.eclipse.kapua.KapuaSerializable;
import org.eclipse.kapua.model.KapuaEntity;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * {@link KapuaListResult} definition.
 *
 * @param <E> {@link KapuaEntity} domain
 * @since 1.0
 */
@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {"limitExceeded", "size", "items", "totalCount"})
public interface KapuaListResult<E extends KapuaEntity> extends KapuaSerializable {

    /**
     * Gets the limit exceeded flag.
     * <p>
     * This flag is {@code true} if there are more results that exceded the {@link KapuaQuery#getLimit()}.
     * Increasing the {@link KapuaQuery#getLimit()} or moving the {@link KapuaQuery#getOffset()} will return more results
     *
     * @return The limit exceeded flag
     * @since 1.0.0
     */
    @XmlElement(name = "limitExceeded")
    boolean isLimitExceeded();

    /**
     * Sets the limit exceeded flag.
     * <p>
     * To be set to {@code true} if the {@link KapuaQuery} matching elements are more than {@link KapuaQuery#getLimit()}.
     *
     * @param limitExceeded The limit exceeded flag.
     * @since 1.0.0
     */
    void setLimitExceeded(boolean limitExceeded);

    /**
     * Gets the {@link KapuaEntity}s that matched the {@link KapuaQuery#getPredicate()}.
     * <p>
     * The result are ordered according to the {@link KapuaQuery#getSortCriteria()}
     *
     * @return The {@link KapuaEntity}s that matched the {@link KapuaQuery#getPredicate()}.
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    List<E> getItems();

    /**
     * Gets the {@link KapuaEntity} at the given position in the {@link KapuaListResult}.
     *
     * @param i The position in the {@link KapuaListResult}
     * @return The {@link KapuaEntity} at the position
     * @throws IndexOutOfBoundsException If position is not available.
     * @see List#get(int)
     * @since 1.0.0
     */
    E getItem(int i);

    /**
     * Gets the first {@link KapuaEntity} in the {@link KapuaListResult}.<br>
     * It returns {@code null} if {@link KapuaListResult#isEmpty()}.
     *
     * @return The first element in the {@link KapuaListResult} or {@code null} if not present.
     * @since 1.0.0
     */
    E getFirstItem();

    /**
     * Gets the result {@link List} size
     *
     * @return The result list size
     * @see List#size()
     * @since 1.0.0
     */
    @XmlElement(name = "size")
    int getSize();

    /**
     * Checks if the result {@link List} is empty
     *
     * @return {@code true} if the result list is empty, {@code false} otherwise
     * @see List#isEmpty()
     * @since 1.0.0
     */
    boolean isEmpty();

    /**
     * Adds {@link KapuaEntity}s to the result {@link List}
     *
     * @param items The {@link KapuaEntity}s to add.
     * @see List#addAll(Collection)
     * @since 1.0.0
     */
    void addItems(@NotNull Collection<? extends E> items);

    /**
     * Add the item to the result list
     *
     * @param item The item to add.
     */
    void addItem(@NotNull E item);

    /**
     * Clears {@link KapuaEntity} result {@link List}
     *
     * @see List#clear()
     * @since 1.0.0
     */
    void clearItems();

    /**
     * Sorts the result {@link List} according to the given {@link Comparator}.
     *
     * @param comparator The {@link Comparator} used to compare items.
     * @see List#sort(Comparator)
     * @see Comparator
     * @since 1.0.0
     */
    void sort(@NotNull Comparator<E> comparator);

    /**
     * Gets the total count of entries that match the predicate regardless of {@code limit} and {@code offset}
     *
     * @since 1.2.0
     * @return The total count
     */
    Long getTotalCount();

    /**
     * Sets the total count of entries that match the predicate regardless of {@code limit} and {@code offset}
     *
     * @since 1.2.0
     */
    void setTotalCount(Long totalCount);
}
