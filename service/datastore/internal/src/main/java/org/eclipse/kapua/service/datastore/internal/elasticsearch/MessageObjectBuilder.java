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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.message.internal.KapuaPositionImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataChannelImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataPayloadImpl;
import org.eclipse.kapua.service.datastore.internal.model.DatastoreMessageImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

import com.fasterxml.jackson.core.Base64Variants;

/**
 * Message object builder.<br>
 * This object converts the schema coming from an Elasticsearch search hit to a Kapua message object (unmarshal).
 * 
 * @since 1.0
 *
 */
public class MessageObjectBuilder
{

    private DatastoreMessage message;

    /**
     * Build a {@link MessageObjectBuilder} from the Elasticsearch search hit
     * 
     * @param searchHit
     * @param fetchStyle
     * @return
     * @throws EsObjectBuilderException
     */
    public MessageObjectBuilder build(SearchHit searchHit, StorableFetchStyle fetchStyle)
        throws EsObjectBuilderException
    {
        Map<String, SearchHitField> searchHitFields = searchHit.getFields();
        String accountId = searchHitFields.get(EsSchema.MESSAGE_ACCOUNT_ID).getValue();
        String deviceId = searchHitFields.get(EsSchema.MESSAGE_DEVICE_ID).getValue();
        String clientId = searchHitFields.get(EsSchema.MESSAGE_CLIENT_ID).getValue();

        DatastoreMessageImpl tmpMessage = new DatastoreMessageImpl();
        tmpMessage.setId(UUID.fromString(searchHit.getId()));
        KapuaDataChannelImpl dataChannel = new KapuaDataChannelImpl();
        tmpMessage.setChannel(dataChannel);

        SearchHitField timestampObj = searchHitFields.get(EsSchema.MESSAGE_TIMESTAMP);
        tmpMessage.setTimestamp((Date) (timestampObj == null ? null : EsUtils.convertToKapuaObject("date", (String) timestampObj.getValue())));

        tmpMessage.setScopeId((accountId == null ? null : KapuaEid.parseCompactId(accountId)));
        tmpMessage.setDeviceId(deviceId == null ? null : KapuaEid.parseCompactId(deviceId));
        tmpMessage.setClientId(clientId);
        tmpMessage.setDatastoreId(new StorableIdImpl(searchHit.getId()));

        if (fetchStyle.equals(StorableFetchStyle.FIELDS)) {
            this.message = tmpMessage;
            return this;
        }

        Map<String, Object> source = searchHit.getSource();

        @SuppressWarnings("unchecked")
        List<String> channelParts = (List<String>) source.get(EsSchema.MESSAGE_CHANNEL_PARTS);
        dataChannel.setSemanticParts(channelParts);

        KapuaDataPayloadImpl payload = new KapuaDataPayloadImpl();
        KapuaPositionImpl position = null;
        if (source.get(EsSchema.MESSAGE_POSITION) != null) {

            @SuppressWarnings("unchecked")
            Map<String, Object> positionMap = (Map<String, Object>) source.get(EsSchema.MESSAGE_POSITION);

            @SuppressWarnings("unchecked")
            Map<String, Object> locationMap = (Map<String, Object>) positionMap.get(EsSchema.MESSAGE_POS_LOCATION);

            position = new KapuaPositionImpl();
            if (locationMap != null && locationMap.get("lat") != null)
                position.setLatitude((double) locationMap.get("lat"));

            if (locationMap != null && locationMap.get("lon") != null)
                position.setLongitude((double) locationMap.get("lon"));

            Object obj = positionMap.get(EsSchema.MESSAGE_POS_ALT);
            if (obj != null)
                position.setAltitude((double) obj);

            obj = positionMap.get(EsSchema.MESSAGE_POS_HEADING);
            if (obj != null)
                position.setHeading((double) obj);

            obj = positionMap.get(EsSchema.MESSAGE_POS_PRECISION);
            if (obj != null)
                position.setPrecision((double) obj);

            obj = positionMap.get(EsSchema.MESSAGE_POS_SATELLITES);
            if (obj != null)
                position.setSatellites((int) obj);

            obj = positionMap.get(EsSchema.MESSAGE_POS_SPEED);
            if (obj != null)
                position.setSpeed((double) obj);

            obj = positionMap.get(EsSchema.MESSAGE_POS_STATUS);
            if (obj != null)
                position.setStatus((int) obj);

            obj = positionMap.get(EsSchema.MESSAGE_POS_TIMESTAMP);
            if (obj != null)
                position.setTimestamp((Date) EsUtils.convertToKapuaObject("date", (String) obj));

            tmpMessage.setPosition(position);
        }

        Object capturedOnFld = source.get(EsSchema.MESSAGE_CAPTURED_ON);
        if (capturedOnFld != null)
            tmpMessage.setCapturedOn((Date) (capturedOnFld == null ? null : EsUtils.convertToKapuaObject("date", (String) capturedOnFld)));
        Object sentOnFld = source.get(EsSchema.MESSAGE_SENT_ON);
        if (sentOnFld != null)
            tmpMessage.setSentOn((Date) (sentOnFld == null ? null : EsUtils.convertToKapuaObject("date", (String) sentOnFld)));
        Object receivedOnFld = source.get(EsSchema.MESSAGE_RECEIVED_ON);
        if (receivedOnFld != null)
            tmpMessage.setReceivedOn((Date) (receivedOnFld == null ? null : EsUtils.convertToKapuaObject("date", (String) receivedOnFld)));

        if (source.get(EsSchema.MESSAGE_METRICS) != null) {

            @SuppressWarnings("unchecked")
            Map<String, Object> metrics = (Map<String, Object>) source.get(EsSchema.MESSAGE_METRICS);

            Map<String, Object> payloadMetrics = new HashMap<String, Object>();

            String[] metricNames = metrics.keySet().toArray(new String[] {});
            for (String metricsName : metricNames) {
                @SuppressWarnings("unchecked")
                Map<String, Object> metricValue = (Map<String, Object>) metrics.get(metricsName);
                if (metricValue.size() > 0) {
                    String[] valueTypes = metricValue.keySet().toArray(new String[] {});
                    Object value = metricValue.get(valueTypes[0]);
                    // since elasticsearch doesn't return always the same type of the saved field
                    // (usually due to some promotion of the field type)
                    // we need to check the metric type returned by elasticsearch and, if needed, convert to the proper type
                    payloadMetrics.put(EsUtils.restoreMetricName(metricsName), EsUtils.convertToCorrectType(valueTypes[0], value));
                }
            }

            payload.setProperties(payloadMetrics);
        }

        if (fetchStyle.equals(StorableFetchStyle.SOURCE_SELECT)) {
            this.message = tmpMessage;
        }

        if (source.get(EsSchema.MESSAGE_BODY) != null) {
            byte[] body = Base64Variants.getDefaultVariant().decode((String) source.get(EsSchema.MESSAGE_BODY));
            payload.setBody(body);
        }

        if (payload != null)
            tmpMessage.setPayload(payload);

        this.message = tmpMessage;
        return this;
    }

    /**
     * Get the built Kapua message object
     * 
     * @return
     */
    public DatastoreMessage getMessage()
    {
        return message;
    }
}
