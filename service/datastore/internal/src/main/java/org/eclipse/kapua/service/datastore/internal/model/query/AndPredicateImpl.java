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
import java.util.Collection;
import java.util.List;

import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;

/**
 * Implementation of query "and" aggregation
 * 
 * @since 1.0
 *
 */
public class AndPredicateImpl implements AndPredicate
{
    private List<StorablePredicate> predicates = new ArrayList<StorablePredicate>();

    /**
     * Default constructor
     */
    public AndPredicateImpl()
    {}

    /**
     * Creates an and predicate for the given predicates collection
     * 
     * @param predicates
     */
    public AndPredicateImpl(Collection<StorablePredicate> predicates)
    {
        predicates.addAll(predicates);
    }

    @Override
    public List<StorablePredicate> getPredicates()
    {
        return this.predicates;
    }

    /**
     * Add the storable predicate to the predicates collection
     * 
     * @param predicate
     * @return
     */
    public AndPredicate addPredicate(StorablePredicate predicate)
    {
        this.predicates.add(predicate);
        return this;

    }

    /**
     * Clear the predicates collection
     * 
     * @return
     */
    public AndPredicate clearPredicates()
    {
        this.predicates.clear();
        return this;
    }

}
