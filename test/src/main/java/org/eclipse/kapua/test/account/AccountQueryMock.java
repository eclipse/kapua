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
package org.eclipse.kapua.test.account;

import java.util.List;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaSortCriteria;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;
import org.eclipse.kapua.service.account.AccountQuery;

public class AccountQueryMock implements AccountQuery {

    @Override
    public void addFetchAttributes(String fetchAttribute) {

    }

    @Override
    public void setFetchAttributes(List<String> fetchAttributeNames) {

    }

    @Override
    public List<String> getFetchAttributes() {
        return null;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {

    }

    @Override
    public KapuaId getScopeId() {
        return null;
    }

    @Override
    public void setPredicate(QueryPredicate queryPredicate) {

    }

    @Override
    public QueryPredicate getPredicate() {
        return null;
    }

    @Override
    public void setSortCriteria(KapuaSortCriteria sortCriteria) {

    }

    @Override
    public KapuaSortCriteria getSortCriteria() {
        return null;
    }

    @Override
    public void setOffset(Integer offset) {

    }

    @Override
    public Integer getOffset() {
        return null;
    }

    @Override
    public void setLimit(Integer limit) {

    }

    @Override
    public Integer getLimit() {
        return null;
    }
}
