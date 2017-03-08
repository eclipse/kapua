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

import org.eclipse.kapua.service.datastore.model.query.ChannelMatchPredicate;

/**
 * Implementation of query predicate for matching the channel value
 * 
 * @since 1.0
 *
 */
public class ChannelMatchPredicateImpl implements ChannelMatchPredicate
{
    private String expression;

    /**
     * Default constructor
     */
    public ChannelMatchPredicateImpl()
    {}

    /**
     * Construct a channel match predicate for the given expression
     * 
     * @param expression
     */
    public ChannelMatchPredicateImpl(String expression)
    {
        this.expression = expression;
    }

    @Override
    public String getExpression()
    {
        return this.expression;
    }

    /**
     * Set the channel expression (may use wildcard)
     * 
     * @param expression
     * @return
     */
    public ChannelMatchPredicate setExpression(String expression)
    {
        this.expression = expression;
        return this;
    }
}
