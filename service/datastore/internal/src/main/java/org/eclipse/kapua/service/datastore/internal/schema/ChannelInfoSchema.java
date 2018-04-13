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

import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.service.datastore.client.DatamodelMappingException;
import org.eclipse.kapua.service.datastore.client.SchemaKeys;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Channel info schema definition
 * 
 * @since 1.0
 */
public class ChannelInfoSchema {

    private ChannelInfoSchema() {

    }

    /**
     * Channel information schema name
     */
    public final static String CHANNEL_TYPE_NAME = "channel";
    /**
     * Channel information - channel
     */
    public final static String CHANNEL_NAME = "channel";
    /**
     * Channel information - client identifier
     */
    public final static String CHANNEL_CLIENT_ID = "client_id";
    /**
     * Channel information - scope id
     */
    public static final String CHANNEL_SCOPE_ID = "scope_id";
    /**
     * Channel information - message timestamp (of the first message published in this channel)
     */
    public final static String CHANNEL_TIMESTAMP = "timestamp";
    /**
     * Channel information - message identifier (of the first message published in this channel)
     */
    public final static String CHANNEL_MESSAGE_ID = "message_id";

    /**
     * Create and return the Json representation of the channel info schema
     * 
     * @param allEnable
     * @param sourceEnable
     * @return
     * @throws DatamodelMappingException
     */
    public static JsonNode getChannelTypeSchema(boolean allEnable, boolean sourceEnable) throws DatamodelMappingException {
        ObjectNode rootNode = SchemaUtil.getObjectNode();

        ObjectNode channelNode = SchemaUtil.getObjectNode();
        ObjectNode sourceChannel = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_ENABLED, sourceEnable) });
        channelNode.set(SchemaKeys.KEY_SOURCE, sourceChannel);

        ObjectNode allChannel = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_ENABLED, allEnable) });
        channelNode.set(SchemaKeys.KEY_ALL, allChannel);

        ObjectNode propertiesNode = SchemaUtil.getObjectNode();
        ObjectNode channelScopeId = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
        propertiesNode.set(CHANNEL_SCOPE_ID, channelScopeId);
        ObjectNode channelClientId = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
        propertiesNode.set(CHANNEL_CLIENT_ID, channelClientId);
        ObjectNode channelName = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX,SchemaKeys. VALUE_TRUE) });
        propertiesNode.set(CHANNEL_NAME, channelName);
        ObjectNode channelTimestamp = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_DATE), new KeyValueEntry(SchemaKeys.KEY_FORMAT, KapuaDateUtils.ISO_DATE_PATTERN) });
        propertiesNode.set(CHANNEL_TIMESTAMP, channelTimestamp);
        ObjectNode channelMessageId = SchemaUtil.getField(
                new KeyValueEntry[] { new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE) });
        propertiesNode.set(CHANNEL_MESSAGE_ID, channelMessageId);
        channelNode.set("properties", propertiesNode);
        rootNode.set(CHANNEL_TYPE_NAME, channelNode);
        return rootNode;
    }

}
