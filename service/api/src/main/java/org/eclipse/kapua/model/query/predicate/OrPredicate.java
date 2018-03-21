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
 *******************************************************************************/
package org.eclipse.kapua.model.query.predicate;

import java.util.List;

/**
 * Kapua sql or predicate definition.
 * 
 * @since 1.0
 * 
 */
public interface OrPredicate extends QueryPredicate {

    /**
     * Creates an "or predicate" from a generic predicate
     * 
     * @param predicate
     * @return
     */
    public QueryPredicate or(QueryPredicate predicate);

    /**
     * Returns a list of predicates
     * 
     * @return
     */
    public List<QueryPredicate> getPredicates();

}
