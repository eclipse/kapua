/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.storable.model.query.predicate;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.utils.KeyValueEntry;


public class MatchPredicateImpl extends StorablePredicateImpl implements MatchPredicate {

    private String field;
    private String expression;

    public MatchPredicateImpl(String field, String expression) {
        this.field = field;
        this.expression = expression;
    }

    @Override
    public String getExpression() {
        return this.expression;
    }

    @Override
    public ObjectNode toSerializedMap() throws MappingException {
        ObjectNode rootNode = newObjectNode();
        ObjectNode expressionNode = getField(new KeyValueEntry[]{new KeyValueEntry(field, expression)});
        rootNode.set(PredicateConstants.PREFIX_KEY, expressionNode);
        return rootNode;
    }

}
