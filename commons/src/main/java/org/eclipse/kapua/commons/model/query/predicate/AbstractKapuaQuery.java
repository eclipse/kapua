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
package org.eclipse.kapua.commons.model.query.predicate;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria.SortOrder;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaEntityPredicates;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.KapuaSortCriteria;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;

/**
 * {@link KapuaQuery} reference abstract implementation.
 *
 * @param <E>
 *            {@link KapuaEntity} domain for this query.
 * 
 * @since 1.0.0
 * 
 */
public abstract class AbstractKapuaQuery<E extends KapuaEntity> implements KapuaQuery<E> {

    private KapuaId scopeId;

    private KapuaPredicate predicate;
    private KapuaSortCriteria sortCriteria;
    private List<String> fetchAttributes;

    private Integer offset;
    private Integer limit;

    /**
     * Constructor.
     * 
     * It defaults the {@link #sortCriteria} to order by the {@link KapuaEntity#getCreatedOn()} {@link SortOrder#ASCENDING}.
     * 
     * @since 1.0.0
     */
    public AbstractKapuaQuery() {
        setSortCriteria(new FieldSortCriteria(KapuaEntityPredicates.CREATED_ON, SortOrder.ASCENDING));
    }

    /**
     * Constructor.
     * 
     * @param scopeId
     *            The scope id of the {@link KapuaQuery}
     * @since 1.0.0
     */
    public AbstractKapuaQuery(KapuaId scopeId) {
        this();

        setScopeId(scopeId);
    }

    @Override
    public void addFetchAttributes(String fetchAttribute) {
        if (getFetchAttributes() == null) {
            setFetchAttributes(new ArrayList<>());
        }

        getFetchAttributes().add(fetchAttribute);
    }

    @Override
    public void setFetchAttributes(List<String> fetchAttributes) {
        this.fetchAttributes = fetchAttributes;
    }

    @Override
    public List<String> getFetchAttributes() {
        return fetchAttributes;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId != null ? (scopeId instanceof KapuaEid ? (KapuaEid) scopeId : new KapuaEid(scopeId)) : null;
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

    @Override
    public void setSortCriteria(KapuaSortCriteria sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    @Override
    public KapuaSortCriteria getSortCriteria() {
        return this.sortCriteria;
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
