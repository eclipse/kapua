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

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * Storable query definition.<br>
 * It defines the queries applicable to the persisted objects (such as messages, channeles information...)
 * 
 * @param <S> persisted object type (such as messages, channeles information...)
 * @since 1.0.0
 */
public interface StorableQuery<S extends Object> {

    /**
     * Gets the fetch attribute names list.
     *
     * @return The fetch attribute names list.
     */
    @XmlElementWrapper(name = "fetchAttributeName")
    @XmlElement(name = "fetchAttributeName")
    public List<String> getFetchAttributes();

    /**
     * Adds an attribute to the fetch attribute names list
     *
     * @param fetchAttribute
     *            The fetch attribute to add to the list.
     * @since 1.0.0
     */
    public void addFetchAttributes(String fetchAttribute);

    /**
     * Sets the fetch attribute names list.<br>
     * This list is a list of optional attributes of a {@link KapuaEntity} that can be fetched when querying.
     *
     * @param fetchAttributeNames
     *            The fetch attribute names list.
     * @since 1.0.0
     */
    public void setFetchAttributes(List<String> fetchAttributeNames);

    /**
     * Gets the scope id
     * 
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getScopeId();

    /**
     * Sets the scope id
     * 
     * @param scopeId
     * @since 1.0.0
     */
    void setScopeId(KapuaId scopeId);

    /**
     * Get the predicate
     * 
     * @return
     * @since 1.0.0
     */
    @XmlTransient
    StorablePredicate getPredicate();

    /**
     * Set the predicate
     * 
     * @param predicate
     * @since 1.0.0
     */
    void setPredicate(StorablePredicate predicate);

    /**
     * Get the query result list offset
     * 
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "offset")
    Integer getOffset();

    /**
     * Set the query result list offset
     * 
     * @param offset
     * @since 1.0.0
     */
    void setOffset(Integer offset);

    /**
     * Get the result list limit count
     * 
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "limit")
    Integer getLimit();

    /**
     * Set the result list limit count
     * 
     * @param limit
     * @since 1.0.0
     */
    void setLimit(Integer limit);

    /**
     * Get the ask for the total count matching query objects
     * 
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "askTotalCount")
    boolean isAskTotalCount();

    /**
     * Set the ask for the total count matching query objects
     * 
     * @param askTotalCount
     * @since 1.0.0
     */
    void setAskTotalCount(boolean askTotalCount);

    /**
     * Get the fetch style
     * 
     * @return
     * @since 1.0.0
     */
    @XmlTransient
    StorableFetchStyle getFetchStyle();

    /**
     * Set the fetch style
     * 
     * @param fetchStyle
     * @since 1.0.0
     */
    void setFetchStyle(StorableFetchStyle fetchStyle);

    /**
     * Get the sort fields list
     * 
     * @return
     * @since 1.0.0
     */
    @XmlTransient
    List<SortField> getSortFields();

    /**
     * Set the sort fields list
     * 
     * @param sortFields
     * @since 1.0.0
     */
    void setSortFields(List<SortField> sortFields);

}
