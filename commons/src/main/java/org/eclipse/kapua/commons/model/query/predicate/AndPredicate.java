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

public class AndPredicate implements KapuaAndPredicate
{
    private List<KapuaPredicate> predicates;

    public AndPredicate()
    {
        this.predicates = new ArrayList<KapuaPredicate>();
    }

    public AndPredicate and(KapuaPredicate predicate)
    {
        this.predicates.add(predicate);
        return this;
    }

    public List<KapuaPredicate> getPredicates()
    {
        return this.predicates;
    }
}
