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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import java.util.List;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.model.Storable;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;
import org.eclipse.kapua.service.datastore.model.query.StorableQuery;

/**
 * Abstract storable query implementation.
 * 
 * @since 1.0.0
 *
 * @param <S>
 *            persisted object type (such as messages, channels information...)
 */
public abstract class AbstractStorableQuery<S extends Storable> implements StorableQuery<S> {

    private StorablePredicate predicate;

    private KapuaId scopeId;
    private Integer limit;
    private Integer indexOffset;
    private boolean askTotalCount;
    private List<SortField> sortFields;
    private StorableFetchStyle fetchStyle;

    /**
     * Default constructor
     * 
     * @since 1.0.0
     */
    public AbstractStorableQuery() {
        super();

        fetchStyle = StorableFetchStyle.SOURCE_FULL;
        askTotalCount = false;
    }

    /**
     * Constructor.
     * 
     * @param scopeId
     *            The scopeId of the query
     * 
     * @since 1.0.0
     */
    public AbstractStorableQuery(KapuaId scopeId) {
        this();

        setScopeId(scopeId);
    }

    /**
     * Get the includes fields by fetchStyle
     * 
     * @param fetchStyle
     * @return
     */
    public abstract String[] getIncludes(StorableFetchStyle fetchStyle);

    /**
     * Get the excludes fields by fetchStyle
     * 
     * @param fetchStyle
     * @return
     */
    public abstract String[] getExcludes(StorableFetchStyle fetchStyle);

    /**
     * Get the fields list
     * 
     * @return
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

}
