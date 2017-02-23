/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.kapua.service.datastore.internal.model.MessageImpl;
import org.eclipse.kapua.service.datastore.internal.model.PayloadImpl;
import org.eclipse.kapua.service.datastore.internal.model.PositionImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.model.Message;
import org.eclipse.kapua.service.datastore.model.query.MessageFetchStyle;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

public class MessageBuilder {

    private Message message;

    public MessageBuilder build(SearchHit searchHit, MessageFetchStyle fetchStyle)
            throws KapuaInvalidTopicException, ParseException {

        String account = searchHit.getFields().get(EsSchema.MESSAGE_ACCOUNT).getValue();
        String asset = searchHit.getFields().get(EsSchema.MESSAGE_AS_NAME).getValue();
        String topic = searchHit.getFields().get(EsSchema.MESSAGE_SEM_TOPIC).getValue();
        KapuaTopic kapuaTopic = new KapuaTopic(account, asset, topic);

        MessageImpl tmpMessage = new MessageImpl();
        tmpMessage.setTopic(kapuaTopic.getFullTopic());

        SearchHitField timestampObj = searchHit.getFields().get(EsSchema.MESSAGE_TIMESTAMP);
        tmpMessage.setTimestamp((Date) (timestampObj == null ? null : EsUtils.convertToKapuaObject("date", (String) timestampObj.getValue())));
        tmpMessage.setId(new StorableIdImpl(searchHit.getId()));

        if (fetchStyle.equals(MessageFetchStyle.METADATA)) {
            this.message = tmpMessage;
            return this;
        }

        Map<String, Object> source = searchHit.getSource();

        PayloadImpl payload = new PayloadImpl();
        PositionImpl position = null;
        if (source.get(EsSchema.MESSAGE_POS) != null) {

            position = new PositionImpl();
            Map<String, Object> positionMap = (Map<String, Object>) source.get(EsSchema.MESSAGE_POS);

            Map<String, Object> locationMap = (Map<String, Object>) positionMap.get(EsSchema.MESSAGE_POS_LOCATION);
            if (locationMap != null && locationMap.get("lat") != null)
                position.setLatitude((double) locationMap.get("lat"));

            if (locationMap != null && locationMap.get("lon") != null)
                position.setLatitude((double) locationMap.get("lon"));

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
        }

        Object collectedOnFld = source.get(EsSchema.MESSAGE_COLLECTED_ON);
        if (collectedOnFld != null) {
            payload.setCollectedOn((Date) (collectedOnFld == null ? null : EsUtils.convertToKapuaObject("date", (String) collectedOnFld)));
        }

        if (source.get(EsSchema.MESSAGE_MTR) != null) {

            Map<String, Object> metrics = (Map<String, Object>) source.get(EsSchema.MESSAGE_MTR);

            Map<String, Object> payloadMetrics = new HashMap<String, Object>();

            String[] metricNames = metrics.keySet().toArray(new String[] {});
            for (String metricsName : metricNames) {
                Map<String, Object> metricValue = (Map<String, Object>) metrics.get(metricsName);
                if (metricValue.size() > 0) {
                    String[] valueTypes = metricValue.keySet().toArray(new String[] {});
                    Object value = metricValue.get(valueTypes[0]);
                    if (value != null && value instanceof Integer)
                        payloadMetrics.put(EsUtils.restoreMetricName(metricsName), value);
                }
            }

            payload.setMetrics(payloadMetrics);
        }

        if (fetchStyle.equals(MessageFetchStyle.METADATA_HEADERS)) {
            this.message = tmpMessage;
        }

        if (source.get(EsSchema.MESSAGE_BODY) != null) {
            byte[] body = ((String) source.get(EsSchema.MESSAGE_BODY)).getBytes();
            payload.setBody(body);
        }

        tmpMessage.setPayload(payload);

        this.message = tmpMessage;
        return this;
    }

    public Message getMessage() {
        return message;
    }
}
