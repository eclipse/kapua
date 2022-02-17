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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.storable.model.query;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicate;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link StorableQuery} {@code abstract} implementation.
 * <p>
 * Is the base for all {@link StorableQuery} implementations.
 *
 * @since 1.0.0
 */
public abstract class AbstractStorableQuery implements StorableQuery {

    private StorablePredicate predicate;

    private KapuaId scopeId;
    private Integer limit;
    private Integer indexOffset;
    private boolean askTotalCount;
    private List<SortField> sortFields;
    private StorableFetchStyle fetchStyle;
    private List<String> fetchAttributes;

    /**
     * Constructor.
     * <p>
     * Forces the {@link StorableFetchStyle} to {@link StorableFetchStyle#SOURCE_FULL}
     *
     * @since 1.0.0
     */
    public AbstractStorableQuery() {
        super();

        setFetchStyle(StorableFetchStyle.SOURCE_FULL);
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope KapuaId.
     * @since 1.0.0
     */
    public AbstractStorableQuery(KapuaId scopeId) {
        this();

        setScopeId(scopeId);
    }

    /**
     * Gets the {@link StorableField}s.
     *
     * @return The {@link StorableField}s.
     * @since 1.0.0
     */
    public abstract String[] getFields();

    @Override
    public KapuaId getScopeId() {
        return scopeId;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = KapuaEid.parseKapuaId(scopeId);
    }

    @Override
    public StorablePredicate getPredicate() {
        return this.predicate;
    }

    @Override
    public void setPredicate(StorablePredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public Integer getOffset() {
        return indexOffset;
    }

    @Override
    public void setOffset(Integer offset) {
        this.indexOffset = offset;
    }

    @Override
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public Integer getLimit() {
        return limit;
    }

    @Override
    public boolean isAskTotalCount() {
        return askTotalCount;
    }

    @Override
    public void setAskTotalCount(boolean askTotalCount) {
        this.askTotalCount = askTotalCount;
    }

    @Override
    public List<SortField> getSortFields() {
        if (sortFields == null) {
            sortFields = new ArrayList<>();
        }

        return sortFields;
    }

    @Override
    public void setSortFields(List<SortField> sortFields) {
        this.sortFields = sortFields;
    }

    @Override
    public StorableFetchStyle getFetchStyle() {
        return this.fetchStyle;
    }

    @Override
    public void setFetchStyle(StorableFetchStyle fetchStyle) {
        this.fetchStyle = fetchStyle;
    }

    @Override
    public List<String> getFetchAttributes() {
        if (fetchAttributes == null) {
            fetchAttributes = new ArrayList<>();
        }

        return fetchAttributes;
    }

    @Override
    public void addFetchAttributes(String fetchAttribute) {
        getFetchAttributes().add(fetchAttribute);
    }

    @Override
    public void setFetchAttributes(List<String> fetchAttributeNames) {
        fetchAttributes = fetchAttributeNames;
    }
}
