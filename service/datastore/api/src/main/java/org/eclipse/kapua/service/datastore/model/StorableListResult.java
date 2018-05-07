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
package org.eclipse.kapua.service.datastore.model;

import org.eclipse.kapua.KapuaSerializable;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.List;

/**
 * Storable object list definition
 *
 * @param <E>
 * @since 1.0.0
 */
@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "limitExceeded", "size", "items", "nextKey", "totalCount" })
public interface StorableListResult<E extends Storable> extends KapuaSerializable {

    /**
     * Get the limit exceeded flag
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "limitExceeded")
    boolean isLimitExceeded();

    /**
     * Set the limit exceeded flag
     *
     * @param limitExceeded true if the query matching elements are more than {@link KapuaQuery#setLimit(Integer)} value
     * @since 1.0.0
     */
    void setLimitExceeded(boolean limitExceeded);

    /**
     * Return the result list
     *
     * @return
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    List<E> getItems();

    /**
     * Return the element at i position (zero based)
     *
     * @param i
     * @return
     * @since 1.0.0
     */
    E getItem(int i);

    /**
     * Returns the first element in the {@link KapuaListResult}.<br>
     * It returns {@code null} if first element does not exist.
     *
     * @return The first element in the {@link KapuaListResult} or {@code null} if not present.
     * @since 1.0.0
     */
    E getFirstItem();

    /**
     * Return the result list size
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "size")
    int getSize();

    /**
     * Check if the result list is empty
     *
     * @return
     * @since 1.0.0
     */
    boolean isEmpty();

    /**
     * Add items to the result list
     *
     * @param items
     * @since 1.0.0
     */
    void addItems(Collection<? extends E> items);

    /**
     * Clear item result list
     *
     * @since 1.0.0
     */
    void clearItems();

    /**
     * Get the next key.<br>
     * If a limit is set into the query parameters (limit) and the messages count matching the query is higher than the limit, so the next key is the key of the first next object not included in the
     * result set.
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "nextKey")
    Object getNextKey();

    /**
     * Get the total count.<br>
     * The total count may be higher that the result set since the extracted result set can be limited by the query (limit) parameter
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "totalCount")
    Long getTotalCount();

    /**
     * Set the total count.<br>
     * The total count may be higher that the result set since the extracted result set can be limited by the query (limit) parameter
     *
     * @param totalCount
     * @since 1.0.0
     */
    @XmlElement(name = "totalCount")
    void setTotalCount(Long totalCount);

}
