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
package org.eclipse.kapua.service.datastore.internal.model.query;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.datastore.model.query.JsonMessageQuery;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;
import org.eclipse.kapua.service.datastore.model.query.XmlAdaptedSortField;

/**
 * Message query implementation
 *
 * @since 1.0.0
 *
 */
public class JsonMessageQueryImpl implements JsonMessageQuery {

    private StorablePredicate predicate;

    private KapuaId scopeId;
    private Integer limit;
    private Integer indexOffset;
    private boolean askTotalCount;
    private List<XmlAdaptedSortField> sortFields;
    private StorableFetchStyle fetchStyle;
    private List<String> fetchAttributes;

    /**
     * Default constructor
     *
     * @since 1.0.0
     */
    public JsonMessageQueryImpl() {
        super();

        fetchStyle = StorableFetchStyle.SOURCE_FULL;
        fetchAttributes = new ArrayList<>();
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
    public JsonMessageQueryImpl(KapuaId scopeId) {
        this();

        setScopeId(scopeId);
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
    public StorableFetchStyle getFetchStyle() {
        return this.fetchStyle;
    }

    @Override
    public void setFetchStyle(StorableFetchStyle fetchStyle) {
        this.fetchStyle = fetchStyle;
    }

    @Override
    public List<String> getFetchAttributes() {
        return fetchAttributes;
    }

    @Override
    public void addFetchAttributes(String fetchAttribute) {
        fetchAttributes.add(fetchAttribute);
    }

    @Override
    public void setFetchAttributes(List<String> fetchAttributeNames) {
        fetchAttributes = fetchAttributeNames;
    }

    @Override
    public List<XmlAdaptedSortField> getSortFields() {
        return sortFields;
    }

    public void setSortFields(List<XmlAdaptedSortField> sortFields) {
        this.sortFields = sortFields;
    }

    @Override
    public String[] getIncludes(StorableFetchStyle fetchStyle) {
        // Fetch mode
        String[] includeSource = null;
        switch (fetchStyle) {
        case FIELDS:
            includeSource = getFields();
            break;
        case SOURCE_SELECT:
            includeSource = new String[] { MessageSchema.MESSAGE_CAPTURED_ON, MessageSchema.MESSAGE_POSITION + ".*", MessageSchema.MESSAGE_METRICS + ".*" };
            break;
        case SOURCE_FULL:
            includeSource = new String[] { "*" };
        }
        return includeSource;
    }

    @Override
    public String[] getExcludes(StorableFetchStyle fetchStyle) {
        // Fetch mode
        String[] excludeSource = null;
        switch (fetchStyle) {
        case FIELDS:
            excludeSource = new String[] { "" };
            break;
        case SOURCE_SELECT:
            excludeSource = new String[] { MessageSchema.MESSAGE_BODY };
            break;
        case SOURCE_FULL:
            excludeSource = new String[] { "" };
        }
        return excludeSource;
    }

    @Override
    public String[] getFields() {
        return new String[] {
                MessageField.MESSAGE_ID.field(),
                MessageField.SCOPE_ID.field(),
                MessageField.DEVICE_ID.field(),
                MessageField.CLIENT_ID.field(),
                MessageField.CHANNEL.field(),
                MessageField.TIMESTAMP.field() };
    }

}
