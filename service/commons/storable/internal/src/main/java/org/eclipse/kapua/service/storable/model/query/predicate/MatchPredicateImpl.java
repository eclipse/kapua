/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.storable.model.query.predicate;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.query.StorableField;
import org.eclipse.kapua.service.storable.model.utils.KeyValueEntry;

/**
 * {@link MatchPredicate} implementation.
 *
 * @since 1.3.0
 */
public class MatchPredicateImpl extends StorablePredicateImpl implements MatchPredicate {

    private StorableField storableField;
    private String expression;

    public MatchPredicateImpl(StorableField field, String expression) {
        this.storableField = field;
        this.expression = expression;
    }

    @Override
    public StorableField getField() {
        return storableField;
    }

    @Override
    public MatchPredicate setField(StorableField storableField) {
        this.storableField = storableField;

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
        ObjectNode expressionNode = newObjectNode(new KeyValueEntry[]{new KeyValueEntry(storableField.field(), expression)});

        ObjectNode rootNode = newObjectNode();
        rootNode.set(PredicateConstants.PREFIX_KEY, expressionNode);
        return rootNode;
    }

}
