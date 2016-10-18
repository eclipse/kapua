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
package org.eclipse.kapua.commons.model.query.predicate;

import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaFetchStyle;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.KapuaSortCriteria;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;

/**
 * Kapua query abstract reference implementation.
 *
 * @param <E> query entity domain
 * 
 * @since 1.0
 * 
 */
public abstract class AbstractKapuaQuery<E extends KapuaEntity> implements KapuaQuery<E> {

    private KapuaId scopeId;

    private KapuaPredicate predicate;
    private KapuaSortCriteria sortCriteria;
    private KapuaFetchStyle fetchStyle;

    private Integer offset;
    private Integer limit;

    /**
     * Constructor
     */
    public AbstractKapuaQuery() {
        sortCriteria = new FieldSortCriteria("id", SortOrder.DESCENDING);
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    @Override
    public void setPredicate(KapuaPredicate queryPredicate) {
        this.predicate = queryPredicate;
    }

    @Override
    public KapuaPredicate getPredicate() {
        return this.predicate;
    }

    // sort
    @Override
    public void setSortCriteria(KapuaSortCriteria sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    @Override
    public KapuaSortCriteria getSortCriteria() {
        return this.sortCriteria;
    }

    @Override
    public KapuaFetchStyle getFetchStyle() {
        return fetchStyle;
    }

    @Override
    public void setFetchStyle(KapuaFetchStyle fetchStyle) {
        this.fetchStyle = fetchStyle;
    }

    @Override
    public Integer getOffset() {
        return offset;
    }

    @Override
    public Integer getLimit() {
        return limit;
    }

    @Override
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
