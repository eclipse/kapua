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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.kapua.KapuaException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * {@link KuraAssets} list definition.
 *
 * @since 1.0.0
 */
public class KuraAssets {

    private List<KuraAsset> assets;

    /**
     * Get the assets list
     *
     * @return
     *
     * @since 1.0.0
     */
    public List<KuraAsset> getAssets() {
        if (assets == null) {
            assets = new ArrayList<>();
        }

        return assets;
    }

    /**
     * Set the assets list
     *
     * @param assets
     *
     * @since 1.0.0
     */
    public void setAssets(List<KuraAsset> assets) {
        this.assets = assets;
    }

    /**
     * Parse a {@link JsonNode} that represent the {@link KuraAssets} object.
     *
     * @param jsonKuraAssets
     *            The {@link JsonNode} to parse
     * @return The parsed {@link KuraAssets} result.
     *
     * @throws KapuaException
     *
     * @since 1.0.0
     */
    public static KuraAssets readJsonNode(JsonNode jsonKuraAssets) throws KapuaException {

        KuraAssets kuraAssets = new KuraAssets();

        Iterator<JsonNode> jsonNodeIterator = jsonKuraAssets.elements();
        while (jsonNodeIterator.hasNext()) {
            JsonNode jsonNode = jsonNodeIterator.next();
            kuraAssets.getAssets().add(KuraAsset.readJsonNode(jsonNode));
        }

        return kuraAssets;
    }

    /**
     * Serialize {@code  this} {@link KuraAssets} into json using the given {@link JsonGenerator}.
     *
     * @param jsonGenerator
     *            The {@link JsonGenerator} to put serialized {@link KuraAssets}.
     * @throws IOException
     * @since 1.0.0
     */
    public void writeJsonNode(JsonGenerator jsonGenerator) throws IOException {

        jsonGenerator.writeStartArray();
        for (KuraAsset kuraAsset : getAssets()) {
            kuraAsset.writeJsonNode(jsonGenerator);
        }
        jsonGenerator.writeEndArray();

    }
}
