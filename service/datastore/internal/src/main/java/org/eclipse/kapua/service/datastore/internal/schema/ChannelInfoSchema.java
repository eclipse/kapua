/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.service.elasticsearch.client.SchemaKeys;
import org.eclipse.kapua.service.elasticsearch.client.exception.DatamodelMappingException;
import org.eclipse.kapua.service.storable.model.utils.KeyValueEntry;
import org.eclipse.kapua.service.storable.model.utils.MappingUtils;

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
    public static final String CHANNEL_TYPE_NAME = "channel";
    /**
     * Channel information - channel
     */
    public static final String CHANNEL_NAME = "channel";
    /**
     * Channel information - client identifier
     */
    public static final String CHANNEL_CLIENT_ID = "client_id";
    /**
     * Channel information - scope id
     */
    public static final String CHANNEL_SCOPE_ID = "scope_id";
    /**
     * Channel information - message timestamp (of the first message published in this channel)
     */
    public static final String CHANNEL_TIMESTAMP = "timestamp";
    /**
     * Channel information - message identifier (of the first message published in this channel)
     */
    public static final String CHANNEL_MESSAGE_ID = "message_id";

    /**
     * Create and return the Json representation of the channel info schema
     *
     * @param allEnable
     * @param sourceEnable
     * @return
     * @throws DatamodelMappingException
     */
    public static JsonNode getChannelTypeSchema(boolean allEnable, boolean sourceEnable) throws DatamodelMappingException, KapuaException {
        ObjectNode rootNode = MappingUtils.newObjectNode();

        ObjectNode channelNode = MappingUtils.newObjectNode();
        ObjectNode sourceChannel = MappingUtils.getField(
                new KeyValueEntry[]{new KeyValueEntry(SchemaKeys.KEY_ENABLED, sourceEnable)});
        channelNode.set(SchemaKeys.KEY_SOURCE, sourceChannel);

        ObjectNode allChannel = MappingUtils.getField(
                new KeyValueEntry[]{new KeyValueEntry(SchemaKeys.KEY_ENABLED, allEnable)});
        channelNode.set(SchemaKeys.KEY_ALL, allChannel);

        ObjectNode propertiesNode = MappingUtils.newObjectNode();
        ObjectNode channelScopeId = MappingUtils.getField(
                new KeyValueEntry[]{new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE)});
        propertiesNode.set(CHANNEL_SCOPE_ID, channelScopeId);
        ObjectNode channelClientId = MappingUtils.getField(
                new KeyValueEntry[]{new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE)});
        propertiesNode.set(CHANNEL_CLIENT_ID, channelClientId);
        ObjectNode channelName = MappingUtils.getField(
                new KeyValueEntry[]{new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE)});
        propertiesNode.set(CHANNEL_NAME, channelName);
        ObjectNode channelTimestamp = MappingUtils.getField(
                new KeyValueEntry[]{new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_DATE), new KeyValueEntry(SchemaKeys.KEY_FORMAT, KapuaDateUtils.ISO_DATE_PATTERN)});
        propertiesNode.set(CHANNEL_TIMESTAMP, channelTimestamp);
        ObjectNode channelMessageId = MappingUtils.getField(
                new KeyValueEntry[]{new KeyValueEntry(SchemaKeys.KEY_TYPE, SchemaKeys.TYPE_KEYWORD), new KeyValueEntry(SchemaKeys.KEY_INDEX, SchemaKeys.VALUE_TRUE)});
        propertiesNode.set(CHANNEL_MESSAGE_ID, channelMessageId);
        channelNode.set("properties", propertiesNode);
        rootNode.set(CHANNEL_TYPE_NAME, channelNode);
        return rootNode;
    }

}
