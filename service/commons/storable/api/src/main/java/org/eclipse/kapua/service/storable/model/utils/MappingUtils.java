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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * Utilities to manage the generation of the {@link ObjectNode}
 *
 * @since 1.3.0
 */
public class MappingUtils {

    private final static JsonNodeFactory JSON_NODE_FACTORY = JsonNodeFactory.instance;
    private static final Logger LOG = LoggerFactory.getLogger(MappingUtils.class);

    private MappingUtils() {
    }

    //
    // Appends
    //

    /**
     * Appends the provided field/value to the object node
     *
     * @param node  The {@link ObjectNode} to appends the field to.
     * @param name  The name of the node to append.
     * @param value The value of the node to append.
     * @throws UnsupportedTypeMappingException if the type of the given value is not one of the supported ones.
     * @throws InvalidValueMappingException    if the object is a {@link Date} and its value is not compatible with a Date.
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
                LOG.warn(
                    "The value of mapping {} of value {} is not compatible with type {}. Error: {}",
                    name,
                    value,
                    Date.class,
                    e.getMessage()
                );
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
    // New
    //

    /**
     * Instantiates a new {@link ArrayNode}.
     *
     * @return The newly instantiated {@link ArrayNode}.
     * @since 1.3.0
     */
    public static ArrayNode newArrayNode() {
        return JSON_NODE_FACTORY.arrayNode();
    }

    /**
     * Instantiates a new {@link ArrayNode}.
     *
     * @param fields The fields to add to the {@link ArrayNode}.
     * @return The newly instantiated {@link ArrayNode}.
     * @since 1.3.0
     */
    public static ArrayNode newArrayNode(Object[] fields) {
        return newArrayNode(Arrays.asList(fields));
    }

    /**
     * Instantiates a new {@link ArrayNode}.
     *
     * @param collection The {@link Collection} to add to the {@link ArrayNode}.
     * @return The newly instantiated {@link ArrayNode}.
     * @since 1.3.0
     */
    public static ArrayNode newArrayNode(Collection<?> collection) {
        ArrayNode arrayNode = newArrayNode();

        collection.stream().map(Object::toString).forEach(arrayNode::add);

        return arrayNode;
    }

    /**
     * Instantiates a new {@link ArrayNode}.
     *
     * @param storablePredicates The {@link Collection} of {@link StorablePredicate} to add to the {@link ArrayNode}.
     * @return The newly instantiated {@link ArrayNode}.
     * @since 1.3.0
     */
    public static ArrayNode newArrayNodeFromPredicates(Collection<StorablePredicate> storablePredicates) throws MappingException {
        ArrayNode arrayNode = newArrayNode();

        for (StorablePredicate predicate : storablePredicates) {
            arrayNode.add(predicate.toSerializedMap());
        }

        return arrayNode;
    }

    /**
     * Instantiates a new {@link ObjectNode}
     *
     * @return The newly instantiated {@link ObjectNode}
     * @since 1.3.0
     */
    public static ObjectNode newObjectNode() {
        return JSON_NODE_FACTORY.objectNode();
    }

    /**
     * Instantiates a new {@link ObjectNode} adding the given {@link KeyValueEntry}es.
     *
     * @param entries The {@link KeyValueEntry}es to be added.
     * @return A newly instantiated {@link ObjectNode} with the {@link KeyValueEntry}es added.
     * @throws MappingException if {@link #appendField(ObjectNode, String, Object)} {@code throws} {@link MappingException}
     * @since 1.3.0
     */
    public static ObjectNode newObjectNode(KeyValueEntry[] entries) throws MappingException {
        ObjectNode objectNode = newObjectNode();

        for (KeyValueEntry entry : entries) {
            appendField(objectNode, entry.getKey(), entry.getValue());
        }

        return objectNode;
    }

    /**
     * Instantiates a new {@link ObjectNode} with the given name and value added as a field.
     *
     * @param name  The name of the field to add.
     * @param value The value of the field to add.
     * @return A newly instantiated {@link ObjectNode} with the field added.
     * @throws MappingException if {@link #appendField(ObjectNode, String, Object)} {@code throws} {@link MappingException}
     * @since 1.3.0
     */
    public static ObjectNode newObjectNode(String name, Object value) throws MappingException {
        ObjectNode objectNode = newObjectNode();

        appendField(objectNode, name, value);

        return objectNode;
    }

    /**
     * Instantiates a new {@link NumericNode} with the given value.
     *
     * @param number The value to add to the {@link NumericNode}.
     * @return The The newly instantiated {@link NumericNode}
     * @since 1.3.0
     */
    public static NumericNode newNumericNode(long number) {
        return JSON_NODE_FACTORY.numberNode(number);
    }

    /**
     * Instantiates a new {@link TextNode} with the given text.
     *
     * @param text The text to add to the {@link NumericNode}.
     * @return The The newly instantiated {@link NumericNode}
     * @since 1.3.0
     */
    public static TextNode newTextNode(String text) {
        return JSON_NODE_FACTORY.textNode(text);
    }
}
