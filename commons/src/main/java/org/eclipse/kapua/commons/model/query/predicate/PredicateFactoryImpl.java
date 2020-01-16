/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.model.query.FieldSortCriteriaImpl;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.query.FieldSortCriteria;
import org.eclipse.kapua.model.query.SortOrder;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.model.query.predicate.OrPredicate;
import org.eclipse.kapua.model.query.predicate.PredicateFactory;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;

@KapuaProvider
public class PredicateFactoryImpl implements PredicateFactory {


    @Override
    public <T> AttributePredicate<T> attributePredicate(String attributeName, T attributeValue) {
        return new AttributePredicateImpl<>(attributeName, attributeValue);
    }

    @Override
    public <T> AttributePredicate<T> attributePredicate(String attributeName, T attributeValue, Operator operator) {
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
