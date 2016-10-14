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
package org.eclipse.kapua.service.datastore.internal.model.query;

import org.eclipse.kapua.service.datastore.model.query.TopicMatchPredicate;

public class TopicMatchPredicateImpl implements TopicMatchPredicate
{
    private String expression;
    
    public TopicMatchPredicateImpl()
    {
    }
    
    public TopicMatchPredicateImpl(String expression)
    {
        this.expression = expression;
    }

    @Override
    public String getExpression()
    {
        return this.expression;
    }

    public TopicMatchPredicate setExpression(String expression)
    {
        this.expression = expression;
        return this;
    }
}
