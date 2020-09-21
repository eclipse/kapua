/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.model.query.ChannelMatchPredicate;
import org.eclipse.kapua.service.storable.model.query.predicate.PredicateConstants;
import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicateImpl;
import org.eclipse.kapua.service.storable.model.utils.KeyValueEntry;

/**
 * Implementation of query predicate for matching the channel value
 *
 * @since 1.0
 */
public class ChannelMatchPredicateImpl extends StorablePredicateImpl implements ChannelMatchPredicate {

    private String field;
    private String expression;

    /**
     * Construct a channel match predicate for the given expression. The field name is {@link MessageField#CHANNEL}
     *
     * @param expression the channel expression (may use wildcard)
     */
    public ChannelMatchPredicateImpl(String expression) {
        this.field = MessageField.CHANNEL.field();
        this.expression = expression;
    }

    /**
     * Construct a channel match predicate for the given expression
     *
     * @param field      the field name
     * @param expression the channel expression (may use wildcard)
     */
    public ChannelMatchPredicateImpl(String field, String expression) {
        this.field = field;
        this.expression = expression;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }

    @Override
    public ObjectNode toSerializedMap() throws KapuaException {
        ObjectNode rootNode = newObjectNode();
        ObjectNode expressionNode = getField(new KeyValueEntry[]{new KeyValueEntry(field, expression)});
        rootNode.set(PredicateConstants.PREFIX_KEY, expressionNode);
        return rootNode;
    }

}
