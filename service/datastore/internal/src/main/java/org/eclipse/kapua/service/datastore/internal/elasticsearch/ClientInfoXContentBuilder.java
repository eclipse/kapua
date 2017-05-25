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
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoCreator;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.Hashing;

/**
 * Client information object content builder.<br>
 * This object creates an ElasticSearch {@link XContentBuilder} from the Kapua client information object (marshal).
 *
 * @since 1.0.0
 */
public class ClientInfoXContentBuilder {

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(ClientInfoXContentBuilder.class);

    private String clientId;
    private XContentBuilder clientBuilder;

    private void init() {
        clientId = null;
        clientBuilder = null;
    }

    private static String getHashCode(String aString) {
        byte[] hashCode = Hashing.sha256()
                .hashString(aString, StandardCharsets.UTF_8)
                .asBytes();

        return Base64.getUrlEncoder().withoutPadding().encodeToString(hashCode);
    }

    private void setClientBuilder(XContentBuilder esClient) {
        this.clientBuilder = esClient;
    }

    /**
     * Get the client identifier (combining accountName and clientId).<br>
     * <b>If the id is null then it is generated</b>
     *
     * @param id
     * @param scopeId
     * @param clientId
     * @return
     * @since 1.0.0
     */
    public static String getOrDeriveId(StorableId id, KapuaId scopeId, String clientId) {
        return id == null ? getClientKey(scopeId, clientId) : id.toString();
    }

    /**
     * Get the client key (return the hash code of the string obtained by concatenating the accountName and the clientName with the slash)
     *
     * @param clientName
     * @return
     * @since 1.0.0
     */
    private static String getClientKey(KapuaId scopeId, String clientName) {
        String clientFullName = String.format("%s/%s", scopeId.toStringId(), clientName);
        return getHashCode(clientFullName);
    }

    /**
     * Get the {@link XContentBuilder} initialized with the provided parameters
     *
     * @param clientId
     * @param msgId
     * @param msgTimestamp
     * @return
     * @throws EsDocumentBuilderException
     * @since 1.0.0
     */
    private XContentBuilder getClientBuilder(String clientId, String msgId, Date msgTimestamp, KapuaId scopeId)
            throws EsDocumentBuilderException {
        try {
            return XContentFactory.jsonBuilder()
                    .startObject()
                    .field(EsSchema.CLIENT_ID, clientId)
                    .field(EsSchema.CLIENT_MESSAGE_ID, msgId)
                    .field(EsSchema.CLIENT_TIMESTAMP, msgTimestamp)
                    .field(EsSchema.CLIENT_SCOPE_ID, scopeId.toCompactId())
                    .endObject();
        } catch (IOException e) {
            throw new EsDocumentBuilderException("Unable to build client info document", e);
        }
    }

    /**
     * Initialize (clean all the instance field) and return the {@link ClientInfoXContentBuilder}
     *
     * @return
     * @since 1.0.0
     */
    public ClientInfoXContentBuilder clear() {
        this.init();
        return this;
    }

    /**
     * Build the {@link ClientInfoXContentBuilder} from the Kapua {@link ClientInfoCreator}
     *
     * @param clientInfo
     * @return
     * @throws EsDocumentBuilderException
     * @since 1.0.0
     */
    public ClientInfoXContentBuilder build(ClientInfoCreator clientInfo)
            throws EsDocumentBuilderException {
        KapuaId scopeId = clientInfo.getScopeId();
        String clientId = clientInfo.getClientId();
        StorableId msgId = clientInfo.getMessageId();
        Date msgTimestamp = clientInfo.getMessageTimestamp();

        XContentBuilder clientBuilder = this.getClientBuilder(clientId, msgId.toString(), msgTimestamp, scopeId);

        this.setClientId(getClientKey(scopeId, clientId));
        this.setClientBuilder(clientBuilder);

        return this;
    }

    /**
     * Build the {@link ClientInfoXContentBuilder} from the Kapua {@link ClientInfo}
     *
     * @param clientInfo
     * @return
     * @throws EsDocumentBuilderException
     * @since 1.0.0
     */
    public ClientInfoXContentBuilder build(ClientInfo clientInfo)
            throws EsDocumentBuilderException {
        KapuaId scopeId = clientInfo.getScopeId();
        String clientId = clientInfo.getClientId();
        StorableId msgId = clientInfo.getFirstMessageId();
        Date msgTimestamp = clientInfo.getFirstMessageOn();

        clientBuilder = this.getClientBuilder(clientId, msgId.toString(), msgTimestamp, scopeId);

        this.setClientId(getOrDeriveId(clientInfo.getId(), scopeId, clientId));
        this.setClientBuilder(clientBuilder);

        return this;
    }

    /**
     * Get the client identifier
     *
     * @return
     * @since 1.0.0
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Set the client identifier
     *
     * @param esClientId
     * @since 1.0.0
     */
    private void setClientId(String esClientId) {
        this.clientId = esClientId;
    }

    /**
     * Get the content builder
     *
     * @return
     * @since 1.0.0
     */
    public XContentBuilder getClientBuilder() {
        return clientBuilder;
    }
}
