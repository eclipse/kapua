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
package org.eclipse.kapua.service.datastore.internal.schema;

import org.eclipse.kapua.service.datastore.client.DatamodelMappingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.eclipse.kapua.service.datastore.client.SchemaKeys;

import org.eclipse.kapua.commons.util.KapuaDateUtils;


/**
 * Client info schema
 * 
 * @since 1.0
 */
public class ClientInfoSchema {

    private ClientInfoSchema() {

    }

    /**
     * Client information schema name
     */
    public final static String CLIENT_TYPE_NAME = "client";
    /**
     * Client information - client identifier
     */
    public final static String CLIENT_ID = "client_id";
    /**
     * Client information - scope id
     */
    public static final String CLIENT_SCOPE_ID = "scope_id";
    /**
     * Client information - message timestamp (of the first message published in this channel)
     */
    public final static String CLIENT_TIMESTAMP = "timestamp";
    /**
     * Client information - message identifier (of the first message published in this channel)
     */
    public final static String CLIENT_MESSAGE_ID = "message_id";

    /**
     * Create and return the Json representation of the client info schema
     * 
     * @param allEnable
     * @param sourceEnable
     * @return
     * @throws DatamodelMappingException
     */
    public static JsonNode getClientTypeSchema(boolean allEnable, boolean sourceEnable) throws DatamodelMappingException {
        ObjectNode rootNode = SchemaUtil.getObjectNode();

        ObjectNode clientNodeName = SchemaUtil.getObjectNode();
        ObjectNode sourceClient = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_ENABLED, sourceEnable)});
        clientNodeName.set(SchemaKeys.KEY_SOURCE, sourceClient);

        ObjectNode allClient = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_ENABLED, allEnable)});
        clientNodeName.set(SchemaKeys.KEY_ALL, allClient);

        ObjectNode propertiesNode = SchemaUtil.getObjectNode();
        ObjectNode clientId = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
        propertiesNode.set(CLIENT_ID, clientId);
        ObjectNode clientTimestamp = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_DATE), new KeyValueEntry(SchemaKeys.KEY_FORMAT, KapuaDateUtils.ISO_DATE_PATTERN) });
        propertiesNode.set(CLIENT_TIMESTAMP, clientTimestamp);
        ObjectNode clientScopeId = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
        propertiesNode.set(CLIENT_SCOPE_ID, clientScopeId);
        ObjectNode clientMessageId = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
        propertiesNode.set(CLIENT_MESSAGE_ID, clientMessageId);
        clientNodeName.set(SchemaKeys.FIELD_NAME_PROPERTIES, propertiesNode);
        rootNode.set(CLIENT_TYPE_NAME, clientNodeName);
        return rootNode;
    }

}
