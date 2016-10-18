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
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.model.query.predicate;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.model.query.predicate.KapuaAndPredicate;
import org.eclipse.kapua.model.query.predicate.KapuaPredicate;

/**
 * Kapua sql and predicate reference implementation.
 * 
 * @since 1.0
 *
 */
public class AndPredicate implements KapuaAndPredicate
{
    private List<KapuaPredicate> predicates;

    /**
     * Constructor
     */
    public AndPredicate()
    {
        this.predicates = new ArrayList<KapuaPredicate>();
    }

    @Override
    public AndPredicate and(KapuaPredicate predicate)
    {
        this.predicates.add(predicate);
        return this;
    }

    @Override
    public List<KapuaPredicate> getPredicates()
    {
        return this.predicates;
    }
}
