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
package org.eclipse.kapua.service.datastore.internal.model.query.predicate;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.model.query.predicate.ChannelMatchPredicate;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.query.StorableField;
import org.eclipse.kapua.service.storable.model.query.predicate.MatchPredicate;
import org.eclipse.kapua.service.storable.model.query.predicate.PredicateConstants;
import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicateImpl;
import org.eclipse.kapua.service.storable.model.utils.KeyValueEntry;

/**
 * Implementation of query predicate for matching the channel value
 *
 * @since 1.0
 */
public class ChannelMatchPredicateImpl extends StorablePredicateImpl implements ChannelMatchPredicate {

    private StorableField field;
    private String expression;

    /**
     * Construct a channel match predicate for the given expression. The field name is {@link MessageField#CHANNEL}
     *
     * @param expression the channel expression (may use wildcard)
     */
    public ChannelMatchPredicateImpl(String expression) {
        this(MessageField.CHANNEL, expression);
    }

    /**
     * Construct a channel match predicate for the given expression
     *
     * @param field      the field name
     * @param expression the channel expression (may use wildcard)
     */
    public ChannelMatchPredicateImpl(StorableField field, String expression) {
        setField(field);
        setExpression(expression);
    }

    @Override
    public StorableField getField() {
        return field;
    }

    @Override
    public MatchPredicate setField(StorableField field) {
        this.field = field;

        return this;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }

    @Override
    public MatchPredicate setExpression(String expression) {
        this.expression = expression;

        return this;
    }

    @Override
    public ObjectNode toSerializedMap() throws MappingException {
        ObjectNode expressionNode = newObjectNode(new KeyValueEntry[]{new KeyValueEntry(getField().field(), getExpression())});

        ObjectNode rootNode = newObjectNode();
        rootNode.set(PredicateConstants.PREFIX_KEY, expressionNode);
        return rootNode;
    }

}
