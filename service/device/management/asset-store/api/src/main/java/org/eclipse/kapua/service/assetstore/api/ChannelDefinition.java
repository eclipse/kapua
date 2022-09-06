/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.assetstore.api;

import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannelMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("ChannelDefinition")
public class ChannelDefinition {

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private Class<?> type;
    /*
     * read/write/read-write
     */
    @JsonProperty("mode")
    private DeviceAssetChannelMode mode;

    public ChannelDefinition() {
    }

    public ChannelDefinition(String name, Class<?> type, DeviceAssetChannelMode mode) {
        this.name = name;
        this.type = type;
        this.mode = mode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public DeviceAssetChannelMode getMode() {
        return mode;
    }

    public void setMode(DeviceAssetChannelMode mode) {
        this.mode = mode;
    }

}
