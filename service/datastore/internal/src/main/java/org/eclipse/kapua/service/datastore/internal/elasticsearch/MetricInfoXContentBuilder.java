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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoCreator;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.elasticsearch.common.Base64;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.Hashing;

/**
 * Metrics information object content builder.<br>
 * This object creates an ElasticSearch {@link XContentBuilder} from the Kapua metrics information object (marshal).<br>
 * It uses {@link MetricXContentBuilder} as a container for the content builders used to build every metric.
 * 
 * @since 1.0
 *
 */
public class MetricInfoXContentBuilder
{

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(MetricInfoXContentBuilder.class);

    private List<MetricXContentBuilder> metricBuilders;

    private void init()
    {
        metricBuilders = null;
    }

    private static String getHashCode(String aString)
    {
        byte[] hashCode = Hashing.sha256()
                                 .hashString(aString, StandardCharsets.UTF_8)
                                 .asBytes();

        return Base64.encodeBytes(hashCode);
    }

    /**
     * Get the metric identifier (return the hash code of the string obtained by combining accountName, clientId, channel and metricMappedName with the slash).<br>
     * <b>NOTE: metricMappedName is the metric name concatenated with its type (see {@link EsUtils#convertToEsType(String kapuaType)}</b>
     * 
     * @param accountName
     * @param clientId
     * @param channel
     * @param metricMappedName
     * @return
     */
    private static String getMetricKey(String accountName, String clientId, String channel, String metricMappedName)
    {
        String channelMetricFullName = String.format("%s/%s/%s/%s", accountName, clientId, channel, metricMappedName);
        String channelMetricHashCode = getHashCode(channelMetricFullName);
        return channelMetricHashCode;
    }

    /**
     * Get the metric identifier (return the hash code of the string obtained by combining accountName, clientId, channel and metricMappedName with the slash).<br>
     * <b>NOTE: metricMappedName is the metric name concatenated with its type (see {@link EsUtils#convertToEsType(String kapuaType)}</b>
     * <b>If the id is null then it is generated.</b>
     * 
     * @param id
     * @param accountName
     * @param clientId
     * @param channel
     * @param metricMappedName
     * @return
     */
    private static String getOrDeriveId(StorableId id, String accountName, String clientId, String channel, String metricMappedName)
    {
        if (id == null)
            return getMetricKey(accountName, clientId, channel, metricMappedName);
        else
            return id.toString();
    }

    /**
     * Get the metric identifier (return the hash code of the string obtained by combining accountName, clientId, channel and the converted metricName and metricType).<br>
     * <b>If the id is null then it is generated.</b>
     * 
     * @param id
     * @param accountName
     * @param clientId
     * @param channel
     * @param metricName
     * @param metricType
     * @return
     * @throws EsDocumentBuilderException
     */
    private static String getOrDeriveId(StorableId id, String accountName, String clientId, String channel, String metricName, String metricType)
        throws EsDocumentBuilderException
    {
        if (id == null) {
            String metricMappedName = EsUtils.getMetricValueQualifier(metricName, EsUtils.convertToEsType(metricType));
            return getMetricKey(accountName, clientId, channel, metricMappedName);
        }
        else
            return id.toString();
    }

    /**
     * Get the metric identifier getting parameters from the metricInfoCreator. Then it calls {@link getOrDeriveId(StorableId id, String accountName, String clientId, String channel, String
     * metricName, String metricType)}
     * 
     * @param id
     * @param metricInfoCreator
     * @return
     * @throws EsDocumentBuilderException
     */
    public static String getOrDeriveId(StorableId id, MetricInfoCreator metricInfoCreator)
        throws EsDocumentBuilderException
    {
        return getOrDeriveId(id,
                             metricInfoCreator.getAccount(),
                             metricInfoCreator.getClientId(),
                             metricInfoCreator.getChannel(),
                             metricInfoCreator.getName(),
                             metricInfoCreator.getType());
    }

    /**
     * Get the metric identifier getting parameters from the metricInfo. Then it calls {@link getOrDeriveId(StorableId id, String accountName, String clientId, String channel, String
     * metricName, String metricType)}
     * @param id
     * @param metricInfo
     * @return
     * @throws EsDocumentBuilderException
     */
    public static String getOrDeriveId(StorableId id, MetricInfo metricInfo)
        throws EsDocumentBuilderException
    {
        return getOrDeriveId(id,
                             metricInfo.getAccount(),
                             metricInfo.getClientId(),
                             metricInfo.getChannel(),
                             metricInfo.getName(),
                             metricInfo.getType());
    }

    /**
     * Get the {@link XContentBuilder} initialized with the provided parameters
     * 
     * @param account
     * @param clientId
     * @param channel
     * @param metricMappedName
     * @param value
     * @param msgTimestamp
     * @param msgId
     * @return
     * @throws EsDocumentBuilderException
     */
    private XContentBuilder build(String account, String clientId, String channel, String metricMappedName, Object value, Date msgTimestamp, String msgId)
        throws EsDocumentBuilderException
    {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder()
                                                     .startObject()
                                                     .field(EsSchema.METRIC_ACCOUNT, account)
                                                     .field(EsSchema.METRIC_CLIENT_ID, clientId)
                                                     .field(EsSchema.METRIC_CHANNEL, channel)
                                                     .startObject(EsSchema.METRIC_MTR)
                                                     .field(EsSchema.METRIC_MTR_NAME, metricMappedName)
                                                     .field(EsSchema.METRIC_MTR_TYPE, EsUtils.getEsTypeFromValue(value))
                                                     .field(EsSchema.METRIC_MTR_VALUE, value)
                                                     .field(EsSchema.METRIC_MTR_TIMESTAMP, msgTimestamp)
                                                     .field(EsSchema.METRIC_MTR_MSG_ID, msgId)
                                                     .endObject()
                                                     .endObject();

            return builder;
        }
        catch (IOException e) {
            throw new EsDocumentBuilderException(String.format("Unable to build metric info document"), e);
        }
    }

    private void getMessageBuilder(String account, String clientId,
                                   KapuaMessage<?, ?> message, String messageId,
                                   Date indexedOn, Date receivedOn)
        throws EsDocumentBuilderException
    {
        KapuaPayload payload = message.getPayload();
        if (payload == null)
            return;

        List<MetricXContentBuilder> metricBuilders = new ArrayList<MetricXContentBuilder>();

        Map<String, Object> kapuaMetrics = payload.getProperties();
        if (kapuaMetrics != null) {

            Map<String, Object> metrics = new HashMap<String, Object>();
            String[] metricNames = kapuaMetrics.keySet().toArray(new String[] {});
            for (String kapuaMetricName : metricNames) {

                Object metricValue = kapuaMetrics.get(kapuaMetricName);

                // Sanitize field names: '.' is not allowed
                String esMetricName = EsUtils.normalizeMetricName(kapuaMetricName);
                String esType = EsUtils.getEsTypeFromValue(metricValue);

                String esTypeAcronim = EsUtils.getEsTypeAcronym(esType);
                EsMetric esMetric = new EsMetric();
                esMetric.setName(esMetricName);
                esMetric.setType(esType);

                Map<String, Object> field = new HashMap<String, Object>();
                field.put(esTypeAcronim, metricValue);
                metrics.put(esMetricName, field);

                // each metric is potentially a dynamic field so report it a new mapping
                String mappedName = EsUtils.getMetricValueQualifier(esMetricName, esType);
                String channel = DatastoreChannel.getChannel(message.getChannel().getSemanticParts());

                MetricXContentBuilder metricBuilder = new MetricXContentBuilder();
                String metricId = getOrDeriveId(null, account,
                                                clientId,
                                                channel,
                                                mappedName);
                metricBuilder.setId(metricId);

                // TODO retrieve the uuid field
                metricBuilder.setContent(this.build(account,
                                                    clientId,
                                                    channel,
                                                    mappedName,
                                                    metricValue,
                                                    indexedOn,
                                                    messageId));
                metricBuilders.add(metricBuilder);
            }
        }

        this.setBuilders(metricBuilders);
    }

    /**
     * Initialize (clean all the instance field) and return the {@link MetricInfoXContentBuilder}
     * 
     * @return
     */
    public MetricInfoXContentBuilder clear()
    {
        this.init();
        return this;
    }

    /**
     * Get the {@link MetricInfoXContentBuilder} initialized with the provided parameters
     * 
     * @param metricInfoCreator
     * @return
     * @throws EsDocumentBuilderException
     */
    public MetricInfoXContentBuilder build(MetricInfoCreator metricInfoCreator)
        throws EsDocumentBuilderException
    {
        String idStr = getOrDeriveId(null, metricInfoCreator);
        StorableId id = new StorableIdImpl(idStr);
        MetricInfoImpl metricInfo = new MetricInfoImpl(metricInfoCreator.getAccount(), id);
        metricInfo.setClientId(metricInfoCreator.getClientId());
        metricInfo.setChannel(metricInfoCreator.getChannel());
        metricInfo.setFirstPublishedMessageId(metricInfoCreator.getMessageId());
        metricInfo.setFirstPublishedMessageTimestamp(metricInfoCreator.getMessageTimestamp());
        metricInfo.setName(metricInfoCreator.getName());
        metricInfo.setType(metricInfoCreator.getType());
        metricInfo.setValue(metricInfoCreator.getValue(Object.class));

        return this.build(metricInfo);
    }

    /**
     * Get the {@link MetricInfoXContentBuilder} initialized with the provided parameters
     * 
     * @param metricInfo
     * @return
     * @throws EsDocumentBuilderException
     */
    public MetricInfoXContentBuilder build(MetricInfo metricInfo)
        throws EsDocumentBuilderException
    {
        StorableId msgId = metricInfo.getFirstPublishedMessageId();
        Date msgTimestamp = metricInfo.getFirstPublishedMessageTimestamp();
        String metricName = metricInfo.getName();
        Object value = metricInfo.getValue(Object.class);

        String metricMappedName = EsUtils.getMetricValueQualifier(metricName, EsUtils.convertToEsType(metricInfo.getType()));

        XContentBuilder metricContentBuilder;
        metricContentBuilder = this.build(metricInfo.getAccount(),
                                          metricInfo.getClientId(),
                                          metricInfo.getChannel(),
                                          metricMappedName,
                                          value,
                                          msgTimestamp,
                                          msgId.toString());

        MetricXContentBuilder metricBuilder = new MetricXContentBuilder();
        metricBuilder.setId(getOrDeriveId(metricInfo.getId(), metricInfo));
        metricBuilder.setContent(metricContentBuilder);
        List<MetricXContentBuilder> metricBuilders = new ArrayList<MetricXContentBuilder>();
        metricBuilders.add(metricBuilder);
        this.setBuilders(metricBuilders);
        return this;
    }

    /**
     * Get the {@link MetricInfoXContentBuilder} initialized with the provided parameters
     * 
     * @param account
     * @param clientId
     * @param messageId
     * @param message
     * @param indexedOn
     * @param receivedOn
     * @return
     * @throws EsDocumentBuilderException
     */
    public MetricInfoXContentBuilder build(String account, String clientId, StorableId messageId, DatastoreMessage message, Date indexedOn, Date receivedOn)
        throws EsDocumentBuilderException
    {
        this.getMessageBuilder(account, clientId, message, messageId.toString(),
                               indexedOn, receivedOn);
        return this;
    }

    /**
     * Get the {@link MetricXContentBuilder} list
     * 
     * @return
     */
    public List<MetricXContentBuilder> getBuilders()
    {
        return metricBuilders;
    }

    /**
     * Set the {@link MetricXContentBuilder} list
     * 
     * @param metricBuilders
     */
    private void setBuilders(List<MetricXContentBuilder> metricBuilders)
    {
        this.metricBuilders = metricBuilders;
    }
}
