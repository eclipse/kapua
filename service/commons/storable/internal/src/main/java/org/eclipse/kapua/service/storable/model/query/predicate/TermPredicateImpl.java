/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
 * {@link TermPredicate} implementation.
 *
 * @since 1.0.0
 */
public class TermPredicateImpl extends StorablePredicateImpl implements TermPredicate {

    private StorableField field;
    private Object value;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public TermPredicateImpl() {
    }

    /**
     * Constructor.
     *
     * @param field The {@link StorableField}.
     * @param value The value to match.
     * @since 1.0.0
     */
    public <V> TermPredicateImpl(StorableField field, V value) {
        this();

        setField(field);
        setValue(value);
    }

    @Override
    public StorableField getField() {
        return this.field;
    }

    @Override
    public TermPredicate setField(StorableField field) {
        this.field = field;

        return this;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public <V> V getValue(Class<V> clazz) {
        return clazz.cast(value);
    }

    @Override
    public TermPredicate setValue(Object value) {
        this.value = value;

        return this;
    }

    @Override
    /**
     * <pre>
     *  {
     *    "query": {
     *      "term" : { "user" : "kapua" }
     *    }
     *  }
     * </pre>
     */
    public ObjectNode toSerializedMap() throws MappingException {
        ObjectNode termNode = newObjectNode(new KeyValueEntry[]{new KeyValueEntry(getField().field(), value)});

        ObjectNode rootNode = newObjectNode();
        rootNode.set(PredicateConstants.TERM_KEY, termNode);
        return rootNode;
    }

}
