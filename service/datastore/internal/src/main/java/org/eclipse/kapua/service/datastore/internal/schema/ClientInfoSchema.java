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

import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_ALL;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_ENABLED;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_FORMAT;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_INDEX;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_TYPE;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.KEY_SOURCE;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.TYPE_DATE;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.TYPE_STRING;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.VALUE_FIELD_INDEXING_NOT_ANALYZED;
import static org.eclipse.kapua.service.datastore.client.SchemaKeys.FIELD_NAME_PROPERTIES;

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
        ObjectNode sourceClient = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(KEY_ENABLED, sourceEnable)});
        clientNodeName.set(KEY_SOURCE, sourceClient);

        ObjectNode allClient = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(KEY_ENABLED, allEnable)});
        clientNodeName.set(KEY_ALL, allClient);

        ObjectNode propertiesNode = SchemaUtil.getObjectNode();
        ObjectNode clientId = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, TYPE_STRING), new KeyValueEntry(KEY_INDEX, VALUE_FIELD_INDEXING_NOT_ANALYZED) });
        propertiesNode.set(CLIENT_ID, clientId);
        ObjectNode clientTimestamp = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, TYPE_DATE), new KeyValueEntry(KEY_FORMAT, KapuaDateUtils.ISO_DATE_PATTERN) });
        propertiesNode.set(CLIENT_TIMESTAMP, clientTimestamp);
        ObjectNode clientScopeId = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, TYPE_STRING), new KeyValueEntry(KEY_INDEX, VALUE_FIELD_INDEXING_NOT_ANALYZED) });
        propertiesNode.set(CLIENT_SCOPE_ID, clientScopeId);
        ObjectNode clientMessageId = SchemaUtil.getField(new KeyValueEntry[] { new KeyValueEntry(KEY_TYPE, TYPE_STRING), new KeyValueEntry(KEY_INDEX, VALUE_FIELD_INDEXING_NOT_ANALYZED) });
        propertiesNode.set(CLIENT_MESSAGE_ID, clientMessageId);
        clientNodeName.set(FIELD_NAME_PROPERTIES, propertiesNode);
        rootNode.set(CLIENT_TYPE_NAME, clientNodeName);
        return rootNode;
    }

}
