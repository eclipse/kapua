/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
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

import java.util.List;

/**
 * Kapua sql {@link AndPredicate} definition.
 *
 * @since 1.0
 */
public interface AndPredicate extends QueryPredicate {

    /**
     * Creates an "and predicate" from a generic predicate
     *
     * @param predicate
     * @return
     */
    AndPredicate and(QueryPredicate predicate);

    /**
     * Returns a list of predicates
     *
     * @return
     */
    List<QueryPredicate> getPredicates();
}
