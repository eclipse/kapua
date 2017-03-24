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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

/**
 * Storable query definition.<br>
 * It defines the queries applicable to the persisted objects (such as messages, channeles information...)
 * 
 * @since 1.0.0
 *
 * @param <S>
 *            persisted object type (such as messages, channeles information...)
 */
public interface StorableQuery<S extends Object> {

    /**
     * Gets the scope id
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getScopeId();

    /**
     * Sets the scope id
     * 
     * @param scopeId
     * 
     * @since 1.0.0
     */
    public void setScopeId(KapuaId scopeId);

    /**
     * Get the predicate
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlTransient
    public StorablePredicate getPredicate();

    /**
     * Set the predicate
     * 
     * @param predicate
     * 
     * @since 1.0.0
     */
    public void setPredicate(StorablePredicate predicate);

    /**
     * Get the query result list offset
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "offset")
    public int getOffset();

    /**
     * Set the query result list offset
     * 
     * @param offset
     * 
     * @since 1.0.0
     */
    public void setOffset(int offset);

    /**
     * Get the result list limit count
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "limit")
    public int getLimit();

    /**
     * Set the result list limit count
     * 
     * @param limit
     * 
     * @since 1.0.0
     */
    public void setLimit(int limit);

    /**
     * Get the ask for the total count matching query objects
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlElement(name = "askTotalCount")
    public boolean isAskTotalCount();

    /**
     * Set the ask for the total count matching query objects
     * 
     * @param askTotalCount
     * 
     * @since 1.0.0
     */
    public void setAskTotalCount(boolean askTotalCount);

    /**
     * Get the fetch style
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlTransient
    public StorableFetchStyle getFetchStyle();

    /**
     * Set the fetch style
     * 
     * @param fetchStyle
     * 
     * @since 1.0.0
     */
    public void setFetchStyle(StorableFetchStyle fetchStyle);

    /**
     * Get the sort fields list
     * 
     * @return
     * 
     * @since 1.0.0
     */
    @XmlTransient
    public List<SortField> getSortFields();

    /**
     * Set the sort fields list
     * 
     * @param sortFields
     * 
     * @since 1.0.0
     */
    public void setSortFields(List<SortField> sortFields);

}
