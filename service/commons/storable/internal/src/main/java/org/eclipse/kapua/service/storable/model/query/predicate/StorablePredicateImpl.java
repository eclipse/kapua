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

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.utils.KeyValueEntry;
import org.eclipse.kapua.service.storable.model.utils.MappingUtils;

import java.util.Collection;

public abstract class StorablePredicateImpl implements StorablePredicate {

    //
    // Appends
    //

    /**
     * Append the provided field/value to the object node
     *
     * @param node
     * @param name
     * @param value
     * @throws KapuaException
     * @since 1.3.0
     */
    protected void appendField(ObjectNode node, String name, Object value) throws MappingException {
        MappingUtils.appendField(node, name, value);
    }

    //
    // Getters
    //

    /**
     * Create a new object node with the provided fields/values
     *
     * @param entries
     * @return
     * @throws KapuaException
     * @since 1.3.0
     */
    protected ObjectNode getField(KeyValueEntry[] entries) throws MappingException {
        return MappingUtils.getField(entries);
    }

    /**
     * Create a new object node with the provided field/value
     *
     * @param name
     * @param value
     * @return
     * @throws KapuaException
     * @since 1.3.0
     */
    protected ObjectNode getField(String name, Object value) throws MappingException {
        return MappingUtils.getField(name, value);
    }

    //
    // New
    //

    protected ArrayNode newArrayNode() {
        return MappingUtils.newArrayNode();
    }

    protected ArrayNode newArrayNode(Object[] fields) {
        return MappingUtils.newArrayNode(fields);
    }

    protected ArrayNode newArrayNode(Collection<?> collection) {
        return MappingUtils.newArrayNode(collection);
    }

    protected ArrayNode newArrayNodeFromPredicates(Collection<StorablePredicate> storablePredicates) throws MappingException {
        return MappingUtils.newArrayNodeFromPredicates(storablePredicates);
    }

    protected ObjectNode newObjectNode() {
        return MappingUtils.newObjectNode();
    }

    protected NumericNode newNumericNode(long number) {
        return MappingUtils.newNumericNode(number);
    }

    protected TextNode newTextNode(String value) {
        return MappingUtils.newTextNode(value);
    }

}
