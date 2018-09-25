/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.connector.kura;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.commons.util.GZipUtil;
import org.eclipse.kapua.connector.Converter;
import org.eclipse.kapua.connector.ConverterErrorCodes;
import org.eclipse.kapua.connector.KapuaConverterException;
import org.eclipse.kapua.connector.MessageContext;
import org.eclipse.kapua.connector.Properties;
import org.eclipse.kapua.connector.kura.proto.KuraInvalidMessageException;
import org.eclipse.kapua.connector.kura.proto.KuraInvalidMetricTypeException;
import org.eclipse.kapua.connector.kura.proto.KuraPayloadProto;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.internal.KapuaPositionImpl;
import org.eclipse.kapua.message.internal.transport.TransportChannelImpl;
import org.eclipse.kapua.message.internal.transport.TransportMessageImpl;
import org.eclipse.kapua.message.internal.transport.TransportPayloadImpl;
import org.eclipse.kapua.message.transport.TransportChannel;
import org.eclipse.kapua.message.transport.TransportMessage;
import org.eclipse.kapua.message.transport.TransportPayload;
import org.eclipse.kapua.message.transport.TransportQos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

public class KuraPayloadProtoConverter implements Converter<byte[], TransportMessage> {

    protected final static Logger logger = LoggerFactory.getLogger(KuraPayloadProtoConverter.class);

    @Override
    public MessageContext<TransportMessage> convert(MessageContext<byte[]> message) throws KapuaConverterException {
        // convert the payload
        KuraPayload kuraPayload = null;
        try {
            kuraPayload = buildKuraPayloadFromByteArray(message.getMessage());
        } catch (KuraInvalidMessageException | IOException e) {
            throw new KapuaConverterException(ConverterErrorCodes.CONVERTION_ERROR, e);
        }

        TransportMessage transportMessage = new TransportMessageImpl();

        // topic and everything related: topic, scope id, device id, client id, channel
        String transportTopic = (String) message.getProperty(Properties.MESSAGE_DESTINATION);
        if (Strings.isNullOrEmpty(transportTopic)) {
            new KapuaConverterException(ConverterErrorCodes.CONVERTION_NO_TOPIC);
        }

        // scopeName, clientId, originalDestination, semanticParts
        processTransportTopic(transportMessage, transportTopic);

        // Qos
        Object qos = message.getProperty(Properties.MESSAGE_QOS);
        if (qos != null && qos instanceof TransportQos) {
            TransportQos transporQos = (TransportQos) message.getProperty(Properties.MESSAGE_QOS);
            transportMessage.setQos(transporQos);
        }

        // timestamps
        transportMessage.setReceivedOn(new Date());
        transportMessage.setSentOn(kuraPayload.getTimestamp());

        // position
        transportMessage.setPosition(buildPosition(kuraPayload));

        // payload
        transportMessage.setPayload(buildTransportPayload(kuraPayload));

        return new MessageContext<TransportMessage>(transportMessage, message.getProperties());
    }

    private void processTransportTopic(TransportMessage transportMessage, String transportTopic) {

        TransportChannel transportChannel = new TransportChannelImpl();
        transportMessage.setChannel(transportChannel);
        transportChannel.setOriginalDestination(transportTopic);

        int topicPartIndex = 0;
        String[] topicParts = transportTopic.split("/");

        String scopeName = topicParts[topicPartIndex++];
        transportMessage.setScopeName(scopeName);

        String clientId = topicParts[topicPartIndex++];
        transportMessage.setClientId(clientId);

        List<String> semanticParts = new ArrayList<String>();
        for (int i = topicPartIndex; i < topicParts.length; i++) {
            semanticParts.add(topicParts[i]);
        }
        transportChannel.setSemanticParts(semanticParts);

    }

    private KapuaPosition buildPosition(KuraPayload kuraPayload) {
        KapuaPosition kapuaPosition = new KapuaPositionImpl();
        KuraPosition kuraPosition = kuraPayload.getPosition();
        if (kuraPosition != null) {
            kapuaPosition.setLatitude(kuraPosition.getLatitude());
            kapuaPosition.setLongitude(kuraPosition.getLongitude());
            kapuaPosition.setAltitude(kuraPosition.getAltitude());
            kapuaPosition.setHeading(kuraPosition.getHeading());
            kapuaPosition.setSpeed(kuraPosition.getSpeed());
            kapuaPosition.setTimestamp(kuraPosition.getTimestamp());
            kapuaPosition.setSatellites(kuraPosition.getSatellites());
            kapuaPosition.setStatus(kuraPosition.getStatus());
            kapuaPosition.setPrecision(kuraPosition.getPrecision());
        }
        return kapuaPosition;
    }

    private TransportPayload buildTransportPayload(KuraPayload kuraPayload) {
        TransportPayload transportPayload = new TransportPayloadImpl();
        transportPayload.setBody(kuraPayload.getBody());
        Map<String, Object> transportMetrics = new HashMap<String, Object>();
        transportPayload.setMetrics(transportMetrics);
        for (String key : kuraPayload.metrics().keySet()) {
            transportMetrics.put(key, kuraPayload.getMetric(key));
        }
        return transportPayload;
    }

    /**
     * Factory method to build an KuraPayload instance from a byte array.
     *
     * @param message
     * @return
     * @throws KuraInvalidMessageException
     * @throws IOException
     */
    public KuraPayload buildKuraPayloadFromByteArray(byte[] message) throws KuraInvalidMessageException, IOException {
        // Check if a compressed payload and try to decompress it
        byte[] bytes = message;
        if (GZipUtil.isCompressed(message)) {
            try {
                bytes = GZipUtil.decompress(message);
            } catch (IOException e) {
                logger.info("Decompression failed");
                // do not rethrow the exception here as isCompressed may return some false positives
            }
        }

        // build the KuraPayload
        KuraPayload kuraMsg = new KuraPayload();

        // build the KuraPayloadProto.KuraPayload
        KuraPayloadProto.KuraPayload protoMsg = null;
        try {
            protoMsg = KuraPayloadProto.KuraPayload.parseFrom(bytes);
        } catch (InvalidProtocolBufferException ipbe) {
            ipbe.printStackTrace();
            // FIXME - must throw exception and register an exception handler
            return kuraMsg;
        }

        // set the timestamp
        if (protoMsg.hasTimestamp()) {
            kuraMsg.setTimestamp(new Date(protoMsg.getTimestamp()));
        }

        // set the position
        if (protoMsg.hasPosition()) {
            kuraMsg.setPosition(buildFromProtoBuf(protoMsg.getPosition()));
        }

        // set the metrics
        for (int i = 0; i < protoMsg.getMetricCount(); i++) {
            String name = protoMsg.getMetric(i).getName();
            try {
                Object value = getProtoKuraMetricValue(protoMsg.getMetric(i), protoMsg.getMetric(i).getType());
                kuraMsg.addMetric(name, value);
            } catch (KuraInvalidMetricTypeException ihte) {
                logger.warn("During deserialization, ignoring metric named: {}. Unrecognized value type: {}", name,
                        protoMsg.getMetric(i).getType(), ihte);
            }
        }

        // set the body
        if (protoMsg.hasBody()) {
            kuraMsg.setBody(protoMsg.getBody().toByteArray());
        }

        return kuraMsg;
    }

    private KuraPosition buildFromProtoBuf(KuraPayloadProto.KuraPayload.KuraPosition protoPosition) {
        KuraPosition position = new KuraPosition();

        if (protoPosition.hasLatitude()) {
            position.setLatitude(protoPosition.getLatitude());
        }
        if (protoPosition.hasLongitude()) {
            position.setLongitude(protoPosition.getLongitude());
        }
        if (protoPosition.hasAltitude()) {
            position.setAltitude(protoPosition.getAltitude());
        }
        if (protoPosition.hasPrecision()) {
            position.setPrecision(protoPosition.getPrecision());
        }
        if (protoPosition.hasHeading()) {
            position.setHeading(protoPosition.getHeading());
        }
        if (protoPosition.hasSpeed()) {
            position.setSpeed(protoPosition.getSpeed());
        }
        if (protoPosition.hasSatellites()) {
            position.setSatellites(protoPosition.getSatellites());
        }
        if (protoPosition.hasStatus()) {
            position.setStatus(protoPosition.getStatus());
        }
        if (protoPosition.hasTimestamp()) {
            position.setTimestamp(new Date(protoPosition.getTimestamp()));
        }
        return position;
    }

    private Object getProtoKuraMetricValue(KuraPayloadProto.KuraPayload.KuraMetric metric,
            KuraPayloadProto.KuraPayload.KuraMetric.ValueType type) throws KuraInvalidMetricTypeException {
        switch (type) {

        case DOUBLE:
            return metric.getDoubleValue();

        case FLOAT:
            return metric.getFloatValue();

        case INT64:
            return metric.getLongValue();

        case INT32:
            return metric.getIntValue();

        case BOOL:
            return metric.getBoolValue();

        case STRING:
            return metric.getStringValue();

        case BYTES:
            ByteString bs = metric.getBytesValue();
            return bs.toByteArray();

        default:
            throw new KuraInvalidMetricTypeException(type.name());
        }
    }
}
