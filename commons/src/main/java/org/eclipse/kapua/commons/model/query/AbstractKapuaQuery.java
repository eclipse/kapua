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
package org.eclipse.kapua.commons.model.query;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.model.query.predicate.OrPredicateImpl;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.FieldSortCriteria;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.KapuaSortCriteria;
import org.eclipse.kapua.model.query.SortOrder;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.model.query.predicate.OrPredicate;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link KapuaQuery} {@code abstract} implementation.
 *
 * @since 1.0.0
 */
public abstract class AbstractKapuaQuery implements KapuaQuery {

    private KapuaId scopeId;

    private QueryPredicate predicate;
    private KapuaSortCriteria sortCriteria;
    private List<String> fetchAttributes;

    private Integer offset;
    private Integer limit;
    private Boolean askTotalCount;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public AbstractKapuaQuery() {
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} of the {@link KapuaQuery}
     * @since 1.0.0
     */
    public AbstractKapuaQuery(KapuaId scopeId) {
        this();

        setScopeId(scopeId);
    }

    /**
     * Constructor.
     * <p>
     * It deeply clones the given {@link KapuaQuery}
     *
     * @param query the query to clone.
     */
    public AbstractKapuaQuery(@NotNull KapuaQuery query) {
        setFetchAttributes(query.getFetchAttributes());
        setPredicate(query.getPredicate());
        setLimit(query.getLimit());
        setOffset(query.getOffset());
        setSortCriteria(query.getSortCriteria());
        setAskTotalCount(query.getAskTotalCount());
    }

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = KapuaEid.parseKapuaId(scopeId);
    }

    @Override
    public void addFetchAttributes(String fetchAttribute) {
        getFetchAttributes().add(fetchAttribute);
    }

    @Override
    public List<String> getFetchAttributes() {
        if (fetchAttributes == null) {
            fetchAttributes = new ArrayList<>();
        }

        return fetchAttributes;
    }

    @Override
    public void setFetchAttributes(List<String> fetchAttributes) {
        this.fetchAttributes = fetchAttributes;
    }

    @Override
    public QueryPredicate getPredicate() {
        return this.predicate;
    }

    @Override
    public void setPredicate(QueryPredicate queryPredicate) {
        this.predicate = queryPredicate;
    }

    @Override
    public KapuaSortCriteria getSortCriteria() {
        return sortCriteria;
    }

    @Override
    public void setSortCriteria(KapuaSortCriteria sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    @Override
    public KapuaSortCriteria getDefaultSortCriteria() {
        return fieldSortCriteria(KapuaEntityAttributes.ENTITY_ID, SortOrder.ASCENDING);
    }

    @Override
    public Integer getOffset() {
        return offset;
    }

    @Override
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public Integer getLimit() {
        return limit;
    }

    @Override
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public Boolean getAskTotalCount() {
        return askTotalCount;
    }

    @Override
    public void setAskTotalCount(Boolean askTotalCount) {
        this.askTotalCount = askTotalCount;
    }

    //
    // Predicate factory
    @Override
    public <T> AttributePredicate<T> attributePredicate(String attributeName, T attributeValue) {
        return new AttributePredicateImpl<>(attributeName, attributeValue);
    }

    @Override
    public <T> AttributePredicate<T> attributePredicate(String attributeName, T attributeValue, AttributePredicate.Operator operator) {
        return new AttributePredicateImpl<>(attributeName, attributeValue, operator);
    }

    @Override
    public AndPredicate andPredicate() {
        return new AndPredicateImpl();
    }

    @Override
    public AndPredicate andPredicate(QueryPredicate... queryPredicates) {
        return new AndPredicateImpl(queryPredicates);
    }

    @Override
    public OrPredicate orPredicate() {
        return new OrPredicateImpl();
    }

    @Override
    public OrPredicate orPredicate(QueryPredicate... queryPredicates) {
        return new OrPredicateImpl(queryPredicates);
    }

    @Override
    public FieldSortCriteria fieldSortCriteria(String attributeName, SortOrder sortOrder) {
        return new FieldSortCriteriaImpl(attributeName, sortOrder);
    }
}
