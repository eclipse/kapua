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

import org.eclipse.kapua.service.datastore.client.DatamodelMappingException;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.schema.KeyValueEntry;
import org.eclipse.kapua.service.datastore.internal.schema.SchemaUtil;
import org.eclipse.kapua.service.datastore.model.query.ChannelMatchPredicate;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Implementation of query predicate for matching the channel value
 * 
 * @since 1.0
 *
 */
public class ChannelMatchPredicateImpl implements ChannelMatchPredicate {

    private String field;
    private String expression;

    /**
     * Construct a channel match predicate for the given expression. The field name is {@link MessageField#CHANNEL}
     * 
     * @param expression
     *            the channel expression (may use wildcard)
     */
    public ChannelMatchPredicateImpl(String expression) {
        this.field = MessageField.CHANNEL.field();
        this.expression = expression;
    }

    /**
     * Construct a channel match predicate for the given expression
     * 
     * @param field
     *            the field name
     * @param expression
     *            the channel expression (may use wildcard)
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
    public ObjectNode toSerializedMap() throws DatamodelMappingException {
        ObjectNode rootNode = SchemaUtil.getObjectNode();
        ObjectNode expressionNode = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(field.toString(), (String) expression) });
        rootNode.set(PredicateConstants.PREFIX_KEY, expressionNode);
        return rootNode;
    }

}
