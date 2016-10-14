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

public abstract class AbstractKapuaQuery<E extends KapuaEntity> implements KapuaQuery<E> {

    private KapuaId scopeId;

    private KapuaPredicate predicate;
    private KapuaSortCriteria sortCriteria;
    private KapuaFetchStyle fetchStyle;

    private Integer offset;
    private Integer limit;

    public AbstractKapuaQuery() {
        sortCriteria = new FieldSortCriteria("id", SortOrder.DESCENDING);
    }

    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
    }

    public KapuaId getScopeId() {
        return scopeId;
    }

    public void setPredicate(KapuaPredicate queryPredicate) {
        this.predicate = queryPredicate;
    }

    public KapuaPredicate getPredicate() {
        return this.predicate;
    }

    // sort
    public void setSortCriteria(KapuaSortCriteria sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public KapuaSortCriteria getSortCriteria() {
        return this.sortCriteria;
    }

    public KapuaFetchStyle getFetchStyle() {
        return fetchStyle;
    }

    public void setFetchStyle(KapuaFetchStyle fetchStyle) {
        this.fetchStyle = fetchStyle;
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
