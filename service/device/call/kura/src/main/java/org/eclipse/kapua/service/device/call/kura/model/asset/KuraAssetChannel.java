/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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

/**
 * Kura channel asset definition
 *
 * @since 1.0.0
 */
public class KuraAssetChannel {

    private String name;
    private KuraAssetChannelMode mode;
    private Class<?> type;
    private Object value;
    private Long timestamp;
    private String error;

    /**
     * Gets the name of the channel.
     *
     * @return The name of the channel.
     *
     * @since 1.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the channel.
     *
     * @param name
     *
     * @since 1.0.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the channel mode.
     *
     * @return The channel mode.
     *
     * @since 1.0.0
     */
    public KuraAssetChannelMode getMode() {
        return mode;
    }

    /**
     * Sets the channel mode.
     * A {@link KuraAssetChannel} can have modes available from {@link KuraAssetChannelMode}.
     *
     * @param mode
     *            The channel mode to set.
     *
     * @since 1.0.0
     */
    public void setMode(KuraAssetChannelMode mode) {
        this.mode = mode;
    }

    /**
     * Gets the channel type.
     * This is the type returned by {@link #getValue()}.
     *
     * @return The channel value type.
     *
     * @since 1.0.0
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Sets the channel type.
     * This type must be coherent with the value given to {@link #setValue(Object)}.
     * If not errors will occur during the interaction with the device.
     *
     * @param type
     *            The channel type.
     *
     * @since 1.0.0
     */
    public void setType(Class<?> type) {
        this.type = type;
    }

    /**
     * Gets the value of the channel.
     * Depending on the {@link KuraAssetChannelMode} this can be a value {@link KuraAssetChannelMode#READ} from the channel or
     * to {@link KuraAssetChannelMode#WRITE} into the channel.
     * This is mutually exclusive with {@link #getError()}
     *
     * @return The value of the channel.
     *
     * @since 1.0.0
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value of the channel.
     * Depending on the {@link KuraAssetChannelMode} this can be a value {@link KuraAssetChannelMode#READ} from the channel or
     * to {@link KuraAssetChannelMode#WRITE} into the channel.
     *
     * @param value
     *            The value of the channel to set.
     *
     * @since 1.0.0
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Gets the timestamp in millisecond of the time when the value was read from the channel.
     *
     * @return The timestamp in millisecond of the time when the value was read from the channel.
     *
     * @since 1.0.0
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp in millisecond of the time when the value was read from the channel.
     *
     * @param timestamp
     *            The timestamp in millisecond of the time when the value was read from the channel.
     *
     * @since 1.0.0
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the error message for this channel
     * When reading from or writing to a channel, if any error occurs it will be reported here.
     * This is mutually exclusive with {@link #getValue()}
     *
     * @return The error message, if error has occurred.
     *
     * @since 1.0.0
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message for this channel.
     * This must be set if error has occurred during reading from/wrtiting to
     *
     * @param error
     *            The error message.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Parse a {@link JsonNode} that represent the {@link KuraAssetChannel} object.
     *
     * @param jsonKuraAssetChannel
     *            The {@link JsonNode} to parse
     * @return The parsed {@link KuraAssetChannel} result.
     *
     * @throws KapuaException
     *
     * @since 1.0.0
     */
    public static KuraAssetChannel readJsonNode(JsonNode jsonKuraAssetChannel) throws KapuaException {
        KuraAssetChannel kuraAssetChannel = new KuraAssetChannel();

        // Name
        JsonNode jsonName = jsonKuraAssetChannel.get("name");
        if (jsonName != null) {
            kuraAssetChannel.setName(jsonName.asText());
        }

        // Type
        JsonNode jsonType = jsonKuraAssetChannel.get("type");
        if (jsonType != null) {
            try {
                kuraAssetChannel.setType(KuraObjectTypeConverter.fromString(jsonType.asText()));
            } catch (ClassNotFoundException e) {
                throw new KapuaIllegalArgumentException("channel.type", jsonType.asText());
            }
        }

        // Mode
        JsonNode jsonMode = jsonKuraAssetChannel.get("mode");
        if (jsonMode != null) {
            kuraAssetChannel.setMode(KuraAssetChannelMode.valueOf(jsonMode.asText()));
        }

        // Timestamp
        JsonNode jsonTimestamp = jsonKuraAssetChannel.get("timestamp");
        if (jsonTimestamp != null) {
            kuraAssetChannel.setTimestamp(jsonTimestamp.asLong());
        }

        // Value
        JsonNode jsonValue = jsonKuraAssetChannel.get("value");
        if (jsonValue != null) {
            try {
                kuraAssetChannel.setValue(KuraObjectValueConverter.fromString(jsonValue.asText(), kuraAssetChannel.getType()));
            } catch (ClassNotFoundException e) {
                throw new KapuaIllegalArgumentException("channel.value", jsonValue.asText());
            }
        }

        // Error
        JsonNode jsonError = jsonKuraAssetChannel.get("error");
        if (jsonError != null) {
            kuraAssetChannel.setError(jsonError.asText());
        }

        return kuraAssetChannel;
    }

    /**
     * Serialize {@code  this} {@link KuraAssetChannel} into json using the given {@link JsonGenerator}.
     *
     * @param jsonGenerator
     *            The {@link JsonGenerator} to put serialized {@link KuraAssetChannel}.
     * @throws IOException
     * @since 1.0.0
     */
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
