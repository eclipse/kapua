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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message object content builder.<br>
 * This object creates an ElasticSearch {@link XContentBuilder} from the Kapua message object (marshal).
 * 
 * @since 1.0
 *
 */
public class MessageXContentBuilder
{

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(MessageXContentBuilder.class);

    private StorableIdImpl  messageId;
    private String          accountName;
    private String          clientId;
    private String          channel;
    private String[]        channelParts;
    private Date            timestamp;
    private Date            indexedOn;
    private Date            capturedOn;
    private Date            receivedOn;
    private Date            sentOn;
    private XContentBuilder messageBuilder;

    private Map<String, EsMetric> metricMappings;

    private void init()
    {

        messageId = null;
        messageBuilder = null;
        metricMappings = null;
    }

    private XContentBuilder build(KapuaMessage<?, ?> message, String messageId,
                                  Date timestamp, Date indexedOn, Date receivedOn)
        throws EsDocumentBuilderException
    {
        try {
            String accountIdStr = message.getScopeId() == null ? null : message.getScopeId().toCompactId();
            String deviceIdStr = message.getDeviceId() == null ? null : message.getDeviceId().toCompactId();

            XContentBuilder messageBuilder = XContentFactory.jsonBuilder()
                                                            .startObject()
                                                            .field(EsSchema.MESSAGE_TIMESTAMP, timestamp)
                                                            .field(EsSchema.MESSAGE_RECEIVED_ON, receivedOn) // TODO Which field ??
                                                            .field(EsSchema.MESSAGE_IP_ADDRESS, "127.0.0.1")
                                                            .field(EsSchema.MESSAGE_ACCOUNT_ID, accountIdStr)
                                                            .field(EsSchema.MESSAGE_ACCOUNT, this.getAccountName())
                                                            .field(EsSchema.MESSAGE_DEVICE_ID, deviceIdStr)
                                                            .field(EsSchema.MESSAGE_CLIENT_ID, this.getClientId())
                                                            .field(EsSchema.MESSAGE_CHANNEL, this.getChannel())
                                                            .field(EsSchema.MESSAGE_CHANNEL_PARTS, this.getChannelParts())
                                                            .field(EsSchema.MESSAGE_CAPTURED_ON, message.getCapturedOn())
                                                            .field(EsSchema.MESSAGE_SENT_ON, message.getSentOn());

            KapuaPosition kapuaPosition = message.getPosition();
            if (kapuaPosition != null) {

                Map<String, Object> location = null;
                if (kapuaPosition.getLongitude() != null && kapuaPosition.getLatitude() != null) {
                    location = new HashMap<String, Object>();
                    location.put("lon", kapuaPosition.getLongitude());
                    location.put("lat", kapuaPosition.getLatitude());
                }

                Map<String, Object> position = new HashMap<String, Object>();
                position.put(EsSchema.MESSAGE_POS_LOCATION, location);
                position.put(EsSchema.MESSAGE_POS_ALT, kapuaPosition.getAltitude());
                position.put(EsSchema.MESSAGE_POS_PRECISION, kapuaPosition.getPrecision());
                position.put(EsSchema.MESSAGE_POS_HEADING, kapuaPosition.getHeading());
                position.put(EsSchema.MESSAGE_POS_SPEED, kapuaPosition.getSpeed());
                position.put(EsSchema.MESSAGE_POS_TIMESTAMP, kapuaPosition.getTimestamp());
                position.put(EsSchema.MESSAGE_POS_SATELLITES, kapuaPosition.getSatellites());
                position.put(EsSchema.MESSAGE_POS_STATUS, kapuaPosition.getStatus());
                messageBuilder.field(EsSchema.MESSAGE_POSITION, position);
            }

            KapuaPayload payload = message.getPayload();
            if (payload == null) {
                messageBuilder.endObject();
                return messageBuilder;
            }

            messageBuilder.field(EsSchema.MESSAGE_BODY, payload.getBody());

            Map<String, EsMetric> metricMappings = new HashMap<String, EsMetric>();

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
                    metricMappings.put(mappedName, esMetric);
                }
                messageBuilder.field(EsSchema.MESSAGE_METRICS, metrics);
            }

            messageBuilder.endObject();

            this.setMetricMappings(metricMappings);
            return messageBuilder;
        }
        catch (IOException e) {
            throw new EsDocumentBuilderException(String.format("Unable to build message document"), e);
        }
    }

    /**
     * Initialize (clean all the instance field) and return the {@link ClientInfoXContentBuilder}
     * 
     * @return
     */
    public MessageXContentBuilder clear()
    {
        this.init();
        return this;
    }

    /**
     * Get the {@link MessageXContentBuilder} initialized with the provided parameters
     * 
     * @param account
     * @param message
     * @param indexedOn
     * @param receivedOn
     * @return
     * @throws EsDocumentBuilderException
     */
    public MessageXContentBuilder build(String account, KapuaMessage<?, ?> message, Date indexedOn, Date receivedOn)
        throws EsDocumentBuilderException
    {
        StorableId messageId;
        UUID uuid = UUID.randomUUID();
        messageId = new StorableIdImpl(uuid.toString());

        this.setAccountName(account);
        this.setClientId(message.getClientId());

        List<String> parts = message.getChannel().getSemanticParts();
        this.setChannel(DatastoreChannel.getChannel(parts));
        this.setChannelParts(parts.toArray(new String[] {}));

        XContentBuilder messageBuilder;
        messageBuilder = this.build(message, messageId.toString(),
                                    indexedOn, indexedOn, receivedOn);

        this.setTimestamp(indexedOn);
        this.setIndexedOn(indexedOn);
        this.setReceivedOn(receivedOn);
        this.setSentOn(message.getSentOn());
        this.setCapturedOn(message.getCapturedOn());

        this.setMessageId(messageId);
        this.setBuilder(messageBuilder);
        return this;
    }

    /**
     * Get the message identifier
     * 
     * @return
     */
    public StorableId getMessageId()
    {
        return messageId;
    }

    /**
     * Set the message identifier
     * 
     * @param esMessageId
     */
    private void setMessageId(StorableId esMessageId)
    {
        this.messageId = (StorableIdImpl) esMessageId;
    }

    /**
     * Get the account name
     * 
     * @return
     */
    public String getAccountName()
    {
        return accountName;
    }

    /**
     * Set the account name
     * 
     * @param accountName
     */
    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    /**
     * Get the client identifier
     * 
     * @return
     */
    public String getClientId()
    {
        return clientId;
    }

    /**
     * Set the client identifier
     * 
     * @param clientId
     */
    private void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    /**
     * Get the channel
     * 
     * @return
     */
    public String getChannel()
    {
        return channel;
    }

    /**
     * Set the channel
     * 
     * @param channel
     */
    private void setChannel(String channel)
    {
        this.channel = channel;
    }

    /**
     * Get the channel parts
     * 
     * @return
     */
    public String[] getChannelParts()
    {
        return channelParts;
    }

    /**
     * Set the channel parts
     * 
     * @param channelParts
     */
    private void setChannelParts(String[] channelParts)
    {
        this.channelParts = channelParts;
    }

    /**
     * Get the message timestamp
     * 
     * @return
     */
    public Date getTimestamp()
    {
        return timestamp;
    }

    /**
     * Set the message timestamp
     * 
     * @param timestamp
     */
    private void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    /**
     * Get the message indexed on date
     * 
     * @return
     */
    public Date getIndexedOn()
    {
        return indexedOn;
    }

    /**
     * Set the message indexed on date
     * 
     * @param indexedOn
     */
    private void setIndexedOn(Date indexedOn)
    {
        this.indexedOn = indexedOn;
    }

    /**
     * Get the message received on date
     * 
     * @return
     */
    public Date getReceivedOn()
    {
        return receivedOn;
    }

    /**
     * Set the received on date
     * 
     * @param message receivedOn
     */
    private void setReceivedOn(Date receivedOn)
    {
        this.receivedOn = receivedOn;
    }

    /**
     * Get the message captured on date
     * 
     * @return
     */
    public Date getCapturedOn()
    {
        return capturedOn;
    }

    /**
     * Set the message captured on date
     * 
     * @param capturedOn
     */
    public void setCapturedOn(Date capturedOn)
    {
        this.capturedOn = capturedOn;
    }

    /**
     * Get the message sent on date
     * 
     * @return
     */
    public Date getSentOn()
    {
        return sentOn;
    }

    /**
     * Set the message sent on date
     * 
     * @param sentOn
     */
    public void setSentOn(Date sentOn)
    {
        this.sentOn = sentOn;
    }

    /**
     * Get the {@link XContentBuilder}
     * 
     * @return
     */
    public XContentBuilder getBuilder()
    {
        return messageBuilder;
    }

    /**
     * Set the {@link XContentBuilder}
     * 
     * @param esMessage
     */
    private void setBuilder(XContentBuilder esMessage)
    {
        this.messageBuilder = esMessage;
    }

    /**
     * Get the metrics map
     * 
     * @return
     */
    public Map<String, EsMetric> getMetricMappings()
    {
        return metricMappings;
    }

    /**
     * Set the metrics map
     * 
     * @param metricMappings
     */
    private void setMetricMappings(Map<String, EsMetric> metricMappings)
    {
        this.metricMappings = metricMappings;
    }
}
