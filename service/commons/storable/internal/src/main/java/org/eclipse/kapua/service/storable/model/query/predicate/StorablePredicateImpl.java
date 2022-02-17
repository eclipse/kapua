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

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.utils.KeyValueEntry;
import org.eclipse.kapua.service.storable.model.utils.MappingUtils;

import java.util.Collection;

/**
 * {@link StorablePredicate} {@code abstract} implementation.
 * <p>
 * Is the base for all {@link StorablePredicate} implementations.
 *
 * @since 1.3.0
 */
public abstract class StorablePredicateImpl implements StorablePredicate {

    //
    // Appends
    //

    /**
     * Appends a field to the given {@link ObjectNode} using {@link MappingUtils#appendField(ObjectNode, String, Object)}.
     *
     * @param node  The {@link ObjectNode} to appends the field to.
     * @param name  The name of the node to append.
     * @param value The value of the node to append.
     * @throws MappingException See {@link MappingUtils#appendField(ObjectNode, String, Object)}.
     * @see MappingUtils#appendField(ObjectNode, String, Object)
     * @since 1.3.0
     */
    protected void appendField(ObjectNode node, String name, Object value) throws MappingException {
        MappingUtils.appendField(node, name, value);
    }

    //
    // New
    //

    /**
     * Instantiates a new {@link ArrayNode} using {@link MappingUtils#newArrayNode()}
     *
     * @return The newly instantiated {@link ArrayNode}.
     * @since 1.3.0
     */
    protected ArrayNode newArrayNode() {
        return MappingUtils.newArrayNode();
    }

    /**
     * Instantiates a new {@link ArrayNode} using {@link MappingUtils#newArrayNode(Object[])}.
     *
     * @param fields The fields to add to the {@link ArrayNode}.
     * @return The newly instantiated {@link ArrayNode}.
     * @since 1.3.0
     */
    protected ArrayNode newArrayNode(Object[] fields) {
        return MappingUtils.newArrayNode(fields);
    }

    /**
     * Instantiates a new {@link ArrayNode} using {@link MappingUtils#newArrayNode(Collection)}.
     *
     * @param collection The {@link Collection} to add to the {@link ArrayNode}.
     * @return The newly instantiated {@link ArrayNode}.
     * @since 1.3.0
     */
    protected ArrayNode newArrayNode(Collection<?> collection) {
        return MappingUtils.newArrayNode(collection);
    }

    /**
     * Instantiates a new {@link ArrayNode} using {@link MappingUtils#newArrayNodeFromPredicates(Collection)}.
     *
     * @param storablePredicates The {@link Collection} of {@link StorablePredicate} to add to the {@link ArrayNode}.
     * @return The newly instantiated {@link ArrayNode}.
     * @throws MappingException See {@link MappingUtils#newArrayNodeFromPredicates(Collection)}
     * @since 1.3.0
     */
    protected ArrayNode newArrayNodeFromPredicates(Collection<StorablePredicate> storablePredicates) throws MappingException {
        return MappingUtils.newArrayNodeFromPredicates(storablePredicates);
    }

    /**
     * Instantiates a new {@link ObjectNode} using {@link MappingUtils#newObjectNode()}.
     *
     * @return The newly instantiated {@link ObjectNode}
     * @since 1.3.0
     */
    protected ObjectNode newObjectNode() {
        return MappingUtils.newObjectNode();
    }

    /**
     * Creates a new {@link ObjectNode} using {@link MappingUtils#newObjectNode(KeyValueEntry[])}.
     *
     * @param entries The {@link KeyValueEntry}es to be added.
     * @return A newly instantiated {@link ObjectNode} with the {@link KeyValueEntry}es added.
     * @throws MappingException See {@link MappingUtils#newObjectNode(KeyValueEntry[])}.
     * @see MappingUtils#newObjectNode(KeyValueEntry[])
     * @since 1.3.0
     */
    protected ObjectNode newObjectNode(KeyValueEntry[] entries) throws MappingException {
        return MappingUtils.newObjectNode(entries);
    }

    /**
     * Creates a new {@link ObjectNode} using {@link MappingUtils#newObjectNode(String, Object)}.
     *
     * @param name  The name of the field to add.
     * @param value The value of the field to add.
     * @return A newly instantiated {@link ObjectNode} with the field added.
     * @throws MappingException See {@link MappingUtils#newObjectNode(String, Object)}.
     * @see MappingUtils#newObjectNode(String, Object)
     * @since 1.3.0
     */
    protected ObjectNode newObjectNode(String name, Object value) throws MappingException {
        return MappingUtils.newObjectNode(name, value);
    }

    /**
     * Instantiates a new {@link NumericNode} using {@link MappingUtils#newNumericNode(long)}
     *
     * @param number The value to add to the {@link NumericNode}.
     * @return The The newly instantiated {@link NumericNode}
     * @since 1.3.0
     */
    protected NumericNode newNumericNode(long number) {
        return MappingUtils.newNumericNode(number);
    }

    /**
     * Instantiates a new {@link TextNode} using {@link MappingUtils#newTextNode(String)}
     *
     * @param text The text to add to the {@link NumericNode}.
     * @return The The newly instantiated {@link NumericNode}
     * @since 1.3.0
     */
    protected TextNode newTextNode(String text) {
        return MappingUtils.newTextNode(text);
    }

}
