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
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.model.ChannelInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ChannelInfoCreator;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.Hashing;

/**
 * Channel information object content builder.<br>
 * This object creates an ElasticSearch {@link XContentBuilder} from the Kapua channel information object (marshal).
 *
 * @since 1.0.0
 */
public class ChannelInfoXContentBuilder {

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(ChannelInfoXContentBuilder.class);

    private String channelId;
    private XContentBuilder channelBuilder;

    private void init() {
        channelId = null;
        channelBuilder = null;
    }

    private static String getHashCode(KapuaId scopeId, String clientId, String channel) {
        byte[] hashCode = Hashing.sha256()
                .hashString(String.format("%s/%s/%s", scopeId.toStringId(), clientId, channel), StandardCharsets.UTF_8)
                .asBytes();

        return Base64.getUrlEncoder().withoutPadding().encodeToString(hashCode);
    }

    /**
     * Get the channel key (return the hash code of the string obtained by concatenating the accountName, clientId and channel with the slash)
     *
     * @param clientId
     * @param channel
     * @return
     * @since 1.0.0
     */
    private static String getChannelKey(KapuaId scopeId, String clientId, String channel) {
        return getHashCode(scopeId, clientId, channel);
    }

    private XContentBuilder build(String semChannel, String msgId, Date msgTimestamp, String clientId, KapuaId scopeId)
            throws EsDocumentBuilderException {
        try {
            return XContentFactory.jsonBuilder()
                    .startObject()
                    .field(EsSchema.CHANNEL_NAME, semChannel)
                    .field(EsSchema.CHANNEL_TIMESTAMP, msgTimestamp)
                    .field(EsSchema.CHANNEL_CLIENT_ID, clientId)
                    .field(EsSchema.CHANNEL_SCOPE_ID, scopeId.toCompactId())
                    .field(EsSchema.CHANNEL_MESSAGE_ID, msgId)
                    .endObject();
        } catch (IOException e) {
            throw new EsDocumentBuilderException(String.format("Unable to build channel info document"), e);
        }
    }

    /**
     * Get the channel identifier (combining accountName clientId and c).<br>
     * <b>If the id is null then it is generated</b>
     *
     * @param id
     * @param clientId
     * @param channel
     * @return
     * @since 1.0.0
     */
    private static String getOrDeriveId(StorableId id, KapuaId scopeId, String clientId, String channel) {
        if (id == null) {
            return getChannelKey(scopeId, clientId, channel);
        } else {
            return id.toString();
        }
    }

    /**
     * Get the channel identifier getting parameters from the metricInfoCreator. Then it calls {@link #getOrDeriveId(StorableId, KapuaId, String, String)}
     *
     * @param id
     * @param channelInfoCreator
     * @return
     * @since 1.0.0
     */
    public static String getOrDeriveId(StorableId id, ChannelInfoCreator channelInfoCreator) {
        return getOrDeriveId(id,
                channelInfoCreator.getScopeId(),
                channelInfoCreator.getClientId(),
                channelInfoCreator.getName());
    }

    /**
     * Get the channel identifier getting parameters from the channelInfo. Then it calls {@link #getOrDeriveId(StorableId, KapuaId, String, String)}
     *
     * @param id
     * @param channelInfo
     * @return
     * @since 1.0.0
     */
    public static String getOrDeriveId(StorableId id, ChannelInfo channelInfo) {
        return getOrDeriveId(id,
                channelInfo.getScopeId(),
                channelInfo.getClientId(),
                channelInfo.getName());
    }

    /**
     * Initialize (clean all the instance field) and return the {@link ChannelInfoXContentBuilder}
     *
     * @return
     * @since 1.0.0
     */
    public ChannelInfoXContentBuilder clear() {
        init();
        return this;
    }

    /**
     * Get the {@link ChannelInfoXContentBuilder} initialized with the provided parameters
     *
     * @param channelInfoCreator
     * @return
     * @throws EsDocumentBuilderException
     * @since 1.0.0
     */
    public ChannelInfoXContentBuilder build(ChannelInfoCreator channelInfoCreator)
            throws EsDocumentBuilderException {
        StorableId id = new StorableIdImpl(getOrDeriveId(null, channelInfoCreator.getScopeId(),
                channelInfoCreator.getClientId(),
                channelInfoCreator.getName()));

        ChannelInfoImpl channelInfo = new ChannelInfoImpl(channelInfoCreator.getScopeId(), id);
        channelInfo.setClientId(channelInfoCreator.getClientId());
        channelInfo.setName(channelInfoCreator.getName());
        channelInfo.setFirstMessageId(channelInfoCreator.getMessageId());
        channelInfo.setFirstMessageOn(channelInfoCreator.getMessageTimestamp());

        return this.build(channelInfo);
    }

    // TODO move to a dedicated EsChannelBuilder Class

    /**
     * Get the {@link XContentBuilder} initialized with the provided parameters
     *
     * @param channelInfo
     * @return
     * @throws EsDocumentBuilderException
     * @since 1.0.0
     */
    public ChannelInfoXContentBuilder build(ChannelInfo channelInfo)
            throws EsDocumentBuilderException {
        KapuaId scopeId = channelInfo.getScopeId();
        String clientId = channelInfo.getClientId();
        String channel = channelInfo.getName();

        StorableId msgId = channelInfo.getFirstMessageId();
        Date msgTimestamp = channelInfo.getFirstMessageOn();

        XContentBuilder channelBuilder;
        channelBuilder = this.build(channel, msgId.toString(), msgTimestamp, clientId, scopeId);

        setChannelId(getOrDeriveId(channelInfo.getId(), channelInfo.getScopeId(),
                channelInfo.getClientId(),
                channelInfo.getName()));

        setBuilder(channelBuilder);
        return this;
    }

    /**
     * Get the channel identifier
     *
     * @return
     * @since 1.0.0
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * Set the channel identifier
     *
     * @param esChannelId
     * @since 1.0.0
     */
    private void setChannelId(String esChannelId) {
        channelId = esChannelId;
    }

    /**
     * Get the {@link XContentBuilder}
     *
     * @return
     * @since 1.0.0
     */
    public XContentBuilder getBuilder() {
        return channelBuilder;
    }

    /**
     * Set the {@link XContentBuilder}
     *
     * @param esChannel
     * @since 1.0.0
     */
    private void setBuilder(XContentBuilder esChannel) {
        channelBuilder = esChannel;
    }
}
