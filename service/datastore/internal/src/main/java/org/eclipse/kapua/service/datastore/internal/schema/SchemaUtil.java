/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.schema;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.client.DatamodelMappingException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.model.StorableId;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

/**
 * Schema utility class
 * 
 * @since 1.0
 */
public class SchemaUtil {

    final static JsonNodeFactory factory = JsonNodeFactory.instance;

    private static final String UNSUPPORTED_OBJECT_TYPE_ERROR_MSG = "The conversion of object [%s] is not supported!";
    private static final String NOT_VALID_OBJECT_TYPE_ERROR_MSG = "Cannot convert date [%s]";

    /**
     * Return a map of map. The contained map has, as entries, the couples subKeys-values.<br>
     * <b>NOTE! No arrays subKeys-values coherence will be done (length or null check)!</b>
     * 
     * @param key
     * @param subKeys
     * @param values
     * @return
     */
    public static Map<String, Object> getMapOfMap(String key, String[] subKeys, String[] values) {
        Map<String, String> mapChildren = new HashMap<>();
        for (int i=0; i<subKeys.length; i++) {
            mapChildren.put(subKeys[i], values[i]);
        }
        Map<String, Object> map = new HashMap<>();
        map.put(key, mapChildren);
        return map;
    }

    /**
     * Get the Elasticsearch data index name
     * 
     * @param scopeId
     * @return
     */
    public static String getDataIndexName(KapuaId scopeId) {
        return DatastoreUtils.getDataIndexName(scopeId);
    }

    /**
     * Get the Kapua data index name
     * 
     * @param scopeId
     * @return
     */
    public static String getKapuaIndexName(KapuaId scopeId) {
        return DatastoreUtils.getRegistryIndexName(scopeId);
    }

    /**
     * Create a new object node with the provided fields/values
     * 
     * @param entries
     * @return
     * @throws DatamodelMappingException
     */
    public static ObjectNode getField(KeyValueEntry[] entries) throws DatamodelMappingException {
        ObjectNode rootNode = factory.objectNode();
        for (int i = 0; i < entries.length; i++) {
            appendField(rootNode, entries[i].getKey(), entries[i].getValue());
        }
        return rootNode;
    }

    /**
     * Create a new object node with the provided field/value
     * 
     * @param name
     * @param value
     * @return
     * @throws DatamodelMappingException
     */
    public static ObjectNode getField(String name, Object value) throws DatamodelMappingException {
        ObjectNode rootNode = factory.objectNode();
        appendField(rootNode, name, value);
        return rootNode;
    }

    /**
     * Append the provided field/value to the object node
     * 
     * @param node
     * @param name
     * @param value
     * @throws DatamodelMappingException
     */
    public static void appendField(ObjectNode node, String name, Object value) throws DatamodelMappingException {
        if (value instanceof String) {
            node.set(name, factory.textNode((String) value));
        } else if (value instanceof Boolean) {
            node.set(name, factory.booleanNode((Boolean) value));
        } else if (value instanceof Integer) {
            node.set(name, factory.numberNode((Integer) value));
        } else if (value instanceof Long) {
            node.set(name, factory.numberNode((Long) value));
        } else if (value instanceof Double) {
            node.set(name, factory.numberNode((Double) value));
        } else if (value instanceof Float) {
            node.set(name, factory.numberNode((Float) value));
        } else if (value instanceof byte[]) {
            node.set(name, factory.binaryNode((byte[]) value));
        } else if (value instanceof byte[]) {
            node.set(name, factory.binaryNode((byte[]) value));
        } else if (value instanceof Date) {
            try {
                node.set(name, factory.textNode(KapuaDateUtils.formatDate((Date) value)));
            } catch (ParseException e) {
                throw new DatamodelMappingException(String.format(NOT_VALID_OBJECT_TYPE_ERROR_MSG, value), e);
            }
        } else if (value instanceof StorableId) {
            node.set(name, factory.textNode(((StorableId) value).toString()));
        } else {
            throw new DatamodelMappingException(String.format(UNSUPPORTED_OBJECT_TYPE_ERROR_MSG, value.getClass()));
        }
    }

    /**
     * Create a new object node
     * 
     * @return
     */
    public static ObjectNode getObjectNode() {
        return factory.objectNode();
    }

    /**
     * Create a new numeric node
     * 
     * @param number
     * @return
     */
    public static NumericNode getNumericNode(long number) {
        return factory.numberNode(number);
    }

    /**
     * Create a new array node
     * 
     * @return
     */
    public static ArrayNode getArrayNode() {
        return factory.arrayNode();
    }

    /**
     * Create a new text node
     * 
     * @param value
     * @return
     */
    public static TextNode getTextNode(String value) {
        return factory.textNode(value);
    }

    /**
     * Convert the provided array to an array node
     * 
     * @param fields
     * @return
     */
    public static ArrayNode getAsArrayNode(String[] fields) {
        ArrayNode rootNode = factory.arrayNode(fields.length);
        for (String str : fields) {
            rootNode.add(str);
        }
        return rootNode;
    }

}
