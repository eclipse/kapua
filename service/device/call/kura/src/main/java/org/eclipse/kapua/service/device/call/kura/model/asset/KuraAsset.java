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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.kapua.KapuaException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * {@link KuraAsset} definition.
 * 
 * @since 1.0.0
 */
public class KuraAsset {

    private String name;
    private List<KuraAssetChannel> channels;

    /**
     * Gets name.
     * 
     * @return The name.
     * 
     * @since 1.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * Set asset name
     * 
     * @param name
     *            The name to set.
     * 
     * @since 1.0.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the channels available for this {@link KuraAsset}
     * 
     * @return The channels available for this {@link KuraAsset}
     * 
     * @since 1.0.0
     */
    public List<KuraAssetChannel> getChannels() {
        if (channels == null) {
            channels = new ArrayList<>();
        }

        return channels;
    }

    /**
     * Sets the channels for this {@link KuraAsset}.
     * 
     * @param channels
     *            The channels to set for this {@link KuraAsset}.
     * 
     * @since 1.0.0
     */
    public void setChannels(List<KuraAssetChannel> channels) {
        this.channels = channels;
    }

    /**
     * Parse a {@link JsonNode} that represent the {@link KuraAsset} object.
     * 
     * @param jsonKuraAsset
     *            The {@link JsonNode} to parse
     * @return The parsed {@link KuraAsset} result.
     * 
     * @throws KapuaException
     * 
     * @since 1.0.0
     */
    public static KuraAsset readJsonNode(JsonNode jsonKuraAsset) throws KapuaException {

        KuraAsset kuraAsset = new KuraAsset();
        kuraAsset.setName(jsonKuraAsset.get("name").asText());

        JsonNode jsonKuraAssetChannels = jsonKuraAsset.get("channels");
        if (jsonKuraAssetChannels != null) {
            Iterator<JsonNode> jsonNodeIterator = jsonKuraAssetChannels.elements();
            while (jsonNodeIterator.hasNext()) {
                JsonNode jsonNode = jsonNodeIterator.next();
                kuraAsset.getChannels().add(KuraAssetChannel.readJsonNode(jsonNode));
            }
        }
        return kuraAsset;
    }

    /**
     * Serialize {@code  this} {@link KuraAsset} into json using the given {@link JsonGenerator}.
     * 
     * @param jsonGenerator
     *            The {@link JsonGenerator} to put serialized {@link KuraAsset}.
     * @throws IOException
     * @since 1.0.0
     */
    public void writeJsonNode(JsonGenerator jsonGenerator) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("name", getName());

        if (!getChannels().isEmpty()) {
            jsonGenerator.writeArrayFieldStart("channels");
            for (KuraAssetChannel kuraAssetChannel : getChannels()) {
                kuraAssetChannel.writeJsonNode(jsonGenerator);
            }
            jsonGenerator.writeEndArray();
        }

        jsonGenerator.writeEndObject();
    }
}
