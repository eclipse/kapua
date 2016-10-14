/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.service.datastore.model.AssetInfo;
import org.eclipse.kapua.service.datastore.model.Message;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.Payload;
import org.eclipse.kapua.service.datastore.model.Position;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.TopicInfo;
import org.elasticsearch.common.Base64;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.hash.Hashing;

public class EsDocumentBuilder
{

    @SuppressWarnings("unused")
    private static final Logger   s_logger = LoggerFactory.getLogger(EsDocumentBuilder.class);

    private String                messageId;
    private XContentBuilder       messageBuilder;

    private String                topicId;
    private XContentBuilder       topicBuilder;
    
    private List<EsMetricDocumentBuilder>   metricBuilders;
    
    private String                assetId;
    private XContentBuilder       assetBuilder;
    
    private Map<String, EsMetric> messageMetrics;

    private void init()
    {

        messageId = null;
        messageBuilder = null;
        topicId = null;
        topicBuilder = null;
        metricBuilders = null;
        assetId = null;
        assetBuilder = null;
        messageMetrics = null;
    }

    private String getHashCode(String aString)
    {
        byte[] hashCode = Hashing.sha256()
                                 .hashString(aString, StandardCharsets.UTF_8)
                                 .asBytes();

        return Base64.encodeBytes(hashCode);
        // return aString;
    }

    private String getTopicKey(String topicFullName)
    {
        return this.getHashCode(topicFullName);
    }

    private String getAssetKey(String accountName, String assetName)
    {
        String assetFullName = String.format("%s/%s", accountName, assetName);
        String assetHashCode = this.getHashCode(assetFullName);
        return assetHashCode;
    }

    private String getTopicMetricKey(String topicFullName, String metricMappedName)
    {
        String topicMetricFullName = String.format("%s/%s", topicFullName, metricMappedName);
        String topicMetricHashCode = this.getHashCode(topicMetricFullName);
        return topicMetricHashCode;
    }

    private XContentBuilder getAssetBuilder(String asset, String msgId, Date msgTimestamp, String account)
        throws IOException
    {
        XContentBuilder builder = XContentFactory.jsonBuilder()
                                                 .startObject()
                                                     .field(EsSchema.ASSET_NAME, asset)
                                                     .field(EsSchema.ASSET_MESSAGE_ID, msgId)
                                                     .field(EsSchema.ASSET_TIMESTAMP, msgTimestamp)
                                                     .field(EsSchema.ASSET_ACCOUNT, account)
                                                 .endObject();
        return builder;
    }

    private XContentBuilder getTopicBuilder(String semTopic, String msgId, Date msgTimestamp, String asset, String account)
        throws IOException
    {

        XContentBuilder builder = XContentFactory.jsonBuilder()
                                                 .startObject()
                                                     .field(EsSchema.TOPIC_SEM_NAME, semTopic)
                                                     .field(EsSchema.TOPIC_TIMESTAMP, msgTimestamp)
                                                     .field(EsSchema.TOPIC_ASSET, asset)
                                                     .field(EsSchema.TOPIC_ACCOUNT, account)
                                                     .field(EsSchema.TOPIC_MESSAGE_ID, msgId)
                                                 .endObject();

        return builder;
    }

    private XContentBuilder getMetricBuilder(String account, String asset, String topic, String metricName, Object value, Date msgTimestamp, String msgId)
        throws IOException
    {

        XContentBuilder builder = XContentFactory.jsonBuilder()
                                                 .startObject()
                                                     .field(EsSchema.METRIC_ACCOUNT, account)
                                                     .field(EsSchema.METRIC_ASSET, asset)
                                                     .field(EsSchema.METRIC_SEM_NAME, topic)
                                                     .startObject(EsSchema.METRIC_MTR)
                                                         .field(EsSchema.METRIC_MTR_NAME, metricName)
                                                         .field(EsSchema.METRIC_MTR_TYPE, EsUtils.getEsTypeFromValue(value))
                                                         .field(EsSchema.METRIC_MTR_VALUE, value)
                                                         .field(EsSchema.METRIC_MTR_TIMESTAMP, msgTimestamp)
                                                         .field(EsSchema.METRIC_MTR_MSG_ID, msgId)
                                                     .endObject()
                                                 .endObject();

        return builder;
    }

    private XContentBuilder getMessageBuilder(String accountName,
                                              Message message, String asset, String topicFull,
                                              String semTopic, String[] topicParts, String messageId,
                                              Date indexedOn, Date receivedOn)
        throws IOException, ParseException
    {

        XContentBuilder messageBuilder = XContentFactory.jsonBuilder()
                                                        .startObject()
                                                            .field(EsSchema.MESSAGE_TIMESTAMP, indexedOn)
                                                            .field(EsSchema.MESSAGE_RECEIVED_ON, receivedOn) // TODO Which field ??
                                                            .field(EsSchema.MESSAGE_IP_ADDRESS, "127.0.0.1")
                                                            .field(EsSchema.MESSAGE_ACCOUNT, accountName)
                                                            .field(EsSchema.MESSAGE_AS_NAME, asset)
                                                            .field(EsSchema.MESSAGE_SEM_TOPIC, semTopic)
                                                            .field(EsSchema.MESSAGE_TOPIC_PARTS, topicParts);

        Payload payload = message.getPayload();
        if (payload == null) {
            messageBuilder.endObject();
            return messageBuilder;
        }

        messageBuilder.field(EsSchema.MESSAGE_COLLECTED_ON, payload.getCollectedOn());

        Position kapuaPosition = payload.getPosition();
        if (kapuaPosition != null) {

            Map<String, Object> location = new HashMap<String, Object>();
            location.put("lon", kapuaPosition.getLongitude());
            location.put("lat", kapuaPosition.getLatitude());

            Map<String, Object> position = new HashMap<String, Object>();
            position.put(EsSchema.MESSAGE_POS_LOCATION, location);
            position.put(EsSchema.MESSAGE_POS_ALT, kapuaPosition.getAltitude());
            position.put(EsSchema.MESSAGE_POS_PRECISION, kapuaPosition.getPrecision());
            position.put(EsSchema.MESSAGE_POS_HEADING, kapuaPosition.getHeading());
            position.put(EsSchema.MESSAGE_POS_SPEED, kapuaPosition.getSpeed());
            position.put(EsSchema.MESSAGE_POS_TIMESTAMP, kapuaPosition.getTimestamp());
            position.put(EsSchema.MESSAGE_POS_SATELLITES, kapuaPosition.getSatellites());
            position.put(EsSchema.MESSAGE_POS_STATUS, kapuaPosition.getStatus());
            messageBuilder.field(EsSchema.MESSAGE_POS, position);
        }

        messageBuilder.field(EsSchema.MESSAGE_BODY, payload.getBody());

        Map<String, EsMetric> metricMappings = new HashMap<String, EsMetric>();
        List<EsMetricDocumentBuilder> metricBuilders = new ArrayList<EsMetricDocumentBuilder>();

        Map<String, Object> kapuaMetrics = payload.getMetrics();
        if (kapuaMetrics != null) {

            Map<String, Object> metrics = new HashMap<String, Object>();
            String[] metricNames = kapuaMetrics.keySet().toArray(new String[] {});
            for (String kapuaMetricName : metricNames) {

                Object metricValue = kapuaMetrics.get(kapuaMetricName);
                //////////////////////
                // Sanitize field names. '.' is not allowed
                String esMetricName = EsUtils.normalizeMetricName(kapuaMetricName);
                String esType = EsUtils.getEsTypeFromValue(metricValue);
                String esTypeAcronim = EsUtils.getEsTypeAcronym(esType);
                EsMetric esMetric = new EsMetric();
                esMetric.setName(esMetricName);
                esMetric.setType(esType);
                //////////////////////

				Map<String, Object> field = new HashMap<String, Object>();
				field.put(esTypeAcronim, metricValue);
				metrics.put(esMetricName, field);

                // each metric is potentially a dynamic field so report it a new mapping
                String mappedName = EsUtils.getMetricValueQualifier(esMetricName, esType);
                metricMappings.put(mappedName, esMetric);

                EsMetricDocumentBuilder metricBuilder = new EsMetricDocumentBuilder();
                String metric = getTopicMetricKey(topicFull, mappedName);
                metricBuilder.setId(metric);

                // TODO retrieve the uuid field
                metricBuilder.setContent(this.getMetricBuilder(accountName, asset, semTopic, mappedName, metricValue, indexedOn, messageId));
                metricBuilders.add(metricBuilder);
            }
            messageBuilder.field(EsSchema.MESSAGE_MTR, metrics);
        }

        messageBuilder.endObject();

        this.setMessageMetrics(metricMappings);
        this.setMetricBuilders(metricBuilders);
        return messageBuilder;
    }

    public EsDocumentBuilder clear()
    {
        this.init();
        return this;
    }

    // TODO move to a dedicated EsAssetBuilder Class
    public EsDocumentBuilder build(String scopeName, AssetInfo assetInfo) throws IOException
    {
        String asset = assetInfo.getAsset();
        StorableId msgId = assetInfo.getLastMessageId();
        Date msgTimestamp = assetInfo.getLastMessageTimestamp();
         
        XContentBuilder assetBuilder = this.getAssetBuilder(asset, msgId.toString(), msgTimestamp, scopeName);
        
        this.setAssetId(this.getAssetKey(scopeName, asset));
        this.setAssetBuilder(assetBuilder);
        
        return this;
    }

    // TODO move to a dedicated EsTopicBuilder Class
    public EsDocumentBuilder build(String scopeName, TopicInfo topicInfo) throws IOException, KapuaInvalidTopicException
    {
        String topicName = topicInfo.getFullTopicName();
        KapuaTopic topic = new KapuaTopic(topicName);
        
        String semTopic = topic.getSemanticTopic();
        StorableId msgId = topicInfo.getLastMessageId();
        Date msgTimestamp = topicInfo.getLastMessageTimestamp();
        String asset = topic.getAsset();
        
        XContentBuilder topicBuilder = this.getTopicBuilder(semTopic, msgId.toString(), msgTimestamp, asset, scopeName);
        
        this.setTopicId(this.getTopicKey(topicName));
        this.setTopicBuilder(topicBuilder);
        return this;
    }

    // TODO move to a dedicated EsMetricBuilder Class
    public EsDocumentBuilder build(String scopeName, MetricInfo metricInfo) throws IOException, KapuaInvalidTopicException, ParseException 
    {
        String topicName = metricInfo.getFullTopicName();
        KapuaTopic topic = new KapuaTopic(topicName);
        
        StorableId msgId = metricInfo.getLastMessageId();
        Date msgTimestamp = metricInfo.getLastMessageTimestamp();
        String asset = topic.getAsset();
        String metricName = metricInfo.getName();
        Object value = metricInfo.getValue(Object.class);
         
        String metricMappedName = EsUtils.getMetricValueQualifier(metricName, EsUtils.convertToEsType(metricInfo.getType()));
       
        XContentBuilder metricContentBuilder = this.getMetricBuilder(scopeName, asset, topicName, metricMappedName, value, msgTimestamp, msgId.toString());
        
        EsMetricDocumentBuilder metricBuilder = new EsMetricDocumentBuilder();
        metricBuilder.setId(this.getTopicMetricKey(topicName, metricMappedName));
        metricBuilder.setContent(metricContentBuilder);
        List<EsMetricDocumentBuilder> metricBuilders = new ArrayList<EsMetricDocumentBuilder>();
        metricBuilders.add(metricBuilder);
        this.setMetricBuilders(metricBuilders);
        return this;
    }
    
    public EsDocumentBuilder build(String accountName, StorableId messageId, Message message, Date indexedOn, Date receivedOn)
        throws ParseException, IOException, KapuaInvalidTopicException
    {

        assert accountName != null : "Account name must be supplied.";
        assert messageBuilder != null : "Message must be supplied.";
        assert messageId != null : "Message ID must be supplied.";

        String topicFull = message.getTopic();
        KapuaTopic kapuaTopic = new KapuaTopic(topicFull);
        String asset = kapuaTopic.getAsset();
        String semTopic = kapuaTopic.getSemanticTopic();
        String[] topicParts = kapuaTopic.getTopicParts();

        String topicId = this.getTopicKey(topicFull);
        this.setTopicId(topicId);
        this.setTopicBuilder(this.getTopicBuilder(semTopic, messageId.toString(), indexedOn, asset, accountName));

        String assetId = this.getAssetKey(accountName, asset);
        this.setAssetId(assetId);
        this.setAssetBuilder(this.getAssetBuilder(asset, messageId.toString(), indexedOn, accountName));

        XContentBuilder messageBuilder = this.getMessageBuilder(accountName,
                                                                message, asset, topicFull, 
                                                                semTopic, topicParts, messageId.toString(),
                                                                indexedOn, receivedOn);

        this.setMessageId(messageId.toString());
        this.setMessageBuilder(messageBuilder);
        return this;
    }

    public String getMessageId()
    {
        return messageId;
    }

    private void setMessageId(String esMessageId)
    {
        this.messageId = esMessageId;
    }

    public XContentBuilder getMessage()
    {
        return messageBuilder;
    }

    private void setMessageBuilder(XContentBuilder esMessage)
    {
        this.messageBuilder = esMessage;
    }

    public String getTopicId()
    {
        return topicId;
    }

    private void setTopicId(String esTopicId)
    {
        this.topicId = esTopicId;
    }

    public XContentBuilder getTopicBuilder()
    {
        return topicBuilder;
    }

    private void setTopicBuilder(XContentBuilder esTopic)
    {
        this.topicBuilder = esTopic;
    }

    public List<EsMetricDocumentBuilder> getTopicMetrics()
    {
        return metricBuilders;
    }

    private void setMetricBuilders(List<EsMetricDocumentBuilder> metricBuilders)
    {
        this.metricBuilders = metricBuilders;
    }

    public String getAssetId()
    {
        return assetId;
    }

    private void setAssetId(String esAssetId)
    {
        this.assetId = esAssetId;
    }

    public XContentBuilder getAssetBuilder()
    {
        return assetBuilder;
    }

    private void setAssetBuilder(XContentBuilder esAsset)
    {
        this.assetBuilder = esAsset;
    }

    public Map<String, EsMetric> getMessageMetrics()
    {
        return messageMetrics;
    }

    private void setMessageMetrics(Map<String, EsMetric> messageMetrics)
    {
        this.messageMetrics = messageMetrics;
    }
}
