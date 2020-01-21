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
package org.eclipse.kapua.model.query.predicate;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.query.FieldSortCriteria;
import org.eclipse.kapua.model.query.SortOrder;

public interface PredicateFactory extends KapuaObjectFactory {

    <T> AttributePredicate<T> attributePredicate(String attributeName, T attributeValue);

    <T> AttributePredicate<T> attributePredicate(String attributeName, T attributeValue, AttributePredicate.Operator operator);

    AndPredicate andPredicate();

    AndPredicate andPredicate(QueryPredicate... queryPredicates);

    OrPredicate orPredicate();

    OrPredicate orPredicate(QueryPredicate... queryPredicates);

    FieldSortCriteria fieldSortCriteria(String attributeName, SortOrder sortOrder);

}
