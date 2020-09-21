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
package org.eclipse.kapua.service.storable.model.utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.storable.exception.InvalidValueMappingException;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.exception.UnsupportedTypeMappingException;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicate;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class MappingUtils {

    private final static JsonNodeFactory JSON_NODE_FACTORY = JsonNodeFactory.instance;

    private MappingUtils() {
    }

    //
    // Appends
    //

    /**
     * Append the provided field/value to the object node
     *
     * @param node
     * @param name
     * @param value
     * @throws MappingException
     * @since 1.3.0
     */
    public static void appendField(ObjectNode node, String name, Object value) throws MappingException {
        if (value instanceof String) {
            node.set(name, JSON_NODE_FACTORY.textNode((String) value));
        } else if (value instanceof Boolean) {
            node.set(name, JSON_NODE_FACTORY.booleanNode((Boolean) value));
        } else if (value instanceof Integer) {
            node.set(name, JSON_NODE_FACTORY.numberNode((Integer) value));
        } else if (value instanceof Long) {
            node.set(name, JSON_NODE_FACTORY.numberNode((Long) value));
        } else if (value instanceof Double) {
            node.set(name, JSON_NODE_FACTORY.numberNode((Double) value));
        } else if (value instanceof Float) {
            node.set(name, JSON_NODE_FACTORY.numberNode((Float) value));
        } else if (value instanceof byte[]) {
            node.set(name, JSON_NODE_FACTORY.binaryNode((byte[]) value));
        } else if (value instanceof Date) {
            try {
                node.set(name, JSON_NODE_FACTORY.textNode(KapuaDateUtils.formatDate((Date) value)));
            } catch (ParseException e) {
                throw new InvalidValueMappingException(e, name, value, Date.class);
            }
        } else if (value instanceof StorableId) {
            node.set(name, JSON_NODE_FACTORY.textNode(value.toString()));
        } else if (value instanceof KapuaId) {
            node.set(name, JSON_NODE_FACTORY.textNode(value.toString()));
        } else {
            throw new UnsupportedTypeMappingException(name, value);
        }
    }

    //
    // Getters
    //

    /**
     * Create a new object node with the provided fields/values
     *
     * @param entries
     * @return
     * @throws MappingException
     * @since 1.3.0
     */
    public static ObjectNode getField(KeyValueEntry[] entries) throws MappingException {
        ObjectNode objectNode = newObjectNode();

        for (KeyValueEntry entry : entries) {
            appendField(objectNode, entry.getKey(), entry.getValue());
        }
        return objectNode;
    }

    /**
     * Create a new object node with the provided field/value
     *
     * @param name
     * @param value
     * @return
     * @throws MappingException
     * @since 1.3.0
     */
    public static ObjectNode getField(String name, Object value) throws MappingException {
        ObjectNode objectNode = newObjectNode();

        appendField(objectNode, name, value);

        return objectNode;
    }

    //
    // New
    //

    public static ArrayNode newArrayNode() {
        return JSON_NODE_FACTORY.arrayNode();
    }

    public static ArrayNode newArrayNode(Object[] fields) {
        return newArrayNode(Arrays.asList(fields));
    }

    public static ArrayNode newArrayNode(Collection<?> collections) {
        ArrayNode arrayNode = newArrayNode();

        collections.stream().map(Object::toString).forEach(arrayNode::add);

        return arrayNode;
    }

    public static ArrayNode newArrayNodeFromPredicates(Collection<StorablePredicate> storablePredicates) throws MappingException {
        ArrayNode arrayNode = newArrayNode();

        for (StorablePredicate predicate : storablePredicates) {
            arrayNode.add(predicate.toSerializedMap());
        }

        return arrayNode;
    }

    public static ObjectNode newObjectNode() {
        return JSON_NODE_FACTORY.objectNode();
    }

    public static NumericNode newNumericNode(long number) {
        return JSON_NODE_FACTORY.numberNode(number);
    }

    public static TextNode newTextNode(String value) {
        return JSON_NODE_FACTORY.textNode(value);
    }
}
