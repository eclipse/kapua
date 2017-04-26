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
package org.eclipse.kapua.service.device.call.kura.model.asset;

import java.io.IOException;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.service.device.call.kura.model.type.KuraObjectTypeConverter;
import org.eclipse.kapua.service.device.call.kura.model.type.KuraObjectValueConverter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

public class KuraAssetChannel {

    private String name;
    private KuraAssetChannelMode mode;
    private Class<?> type;
    private Object value;
    private Long timestamp;
    private String error;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KuraAssetChannelMode getMode() {
        return mode;
    }

    public void setMode(KuraAssetChannelMode mode) {
        this.mode = mode;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static KuraAssetChannel readJsonNode(JsonNode jsonKuraAssets) throws KapuaException {
        KuraAssetChannel kuraAssetChannel = new KuraAssetChannel();

        // Name
        JsonNode jsonName = jsonKuraAssets.get("name");
        if (jsonName != null) {
            kuraAssetChannel.setName(jsonName.asText());
        }

        // Type
        JsonNode jsonType = jsonKuraAssets.get("type");
        if (jsonType != null) {
            try {
                kuraAssetChannel.setType(KuraObjectTypeConverter.fromString(jsonType.asText()));
            } catch (ClassNotFoundException e) {
                throw new KapuaIllegalArgumentException("channel.type", jsonType.asText());
            }
        }

        // Mode
        JsonNode jsonMode = jsonKuraAssets.get("mode");
        if (jsonMode != null) {
            kuraAssetChannel.setMode(KuraAssetChannelMode.valueOf(jsonMode.asText()));
        }

        // Timestamp
        JsonNode jsonTimestamp = jsonKuraAssets.get("timestamp");
        if (jsonTimestamp != null) {
            kuraAssetChannel.setTimestamp(jsonTimestamp.asLong());
        }

        // Value
        JsonNode jsonValue = jsonKuraAssets.get("value");
        if (jsonValue != null) {
            try {
                kuraAssetChannel.setValue(KuraObjectValueConverter.fromString(jsonValue.asText(), kuraAssetChannel.getType()));
            } catch (ClassNotFoundException e) {
                throw new KapuaIllegalArgumentException("channel.value", jsonValue.asText());
            }
        }

        // Error
        JsonNode jsonError = jsonKuraAssets.get("error");
        if (jsonError != null) {
            kuraAssetChannel.setError(jsonError.asText());
        }

        return kuraAssetChannel;
    }

    public void writeJsonNode(JsonGenerator jsonGenerator) throws IOException {
        jsonGenerator.writeStartObject();

        // Name
        String name = getName();
        if (name != null) {
            jsonGenerator.writeStringField("name", name);
        }

        // Type
        Class<?> type = getType();
        if (type != null) {
            jsonGenerator.writeStringField("type", KuraObjectTypeConverter.toString(type));
        }

        // Mode
        KuraAssetChannelMode mode = getMode();
        if (mode != null) {
            jsonGenerator.writeStringField("mode", mode.name());
        }

        // Timestamp
        Long timestamp = getTimestamp();
        if (timestamp != null && timestamp > 0) {
            jsonGenerator.writeNumberField("timestamp", timestamp);
        }

        // Value
        Object value = getValue();
        if (value != null) {
            jsonGenerator.writeStringField("value", KuraObjectValueConverter.toString(value));
        }

        // Error
        String error = getError();
        if (error != null) {
            jsonGenerator.writeStringField("error", error);
        }

        jsonGenerator.writeEndObject();
    }
}
