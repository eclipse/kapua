/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.shared.model.management.assets;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import java.util.Date;

public class GwtDeviceAssetChannel extends KapuaBaseModel implements IsSerializable {

    private static final long serialVersionUID = -8491421299465798124L;

    public enum GwtDeviceAssetChannelMode {
        WRITE, READ, READ_WRITE
    }

    public enum GwtDeviceAssetChannelType {
        STRING("string"), INTEGER("integer"), INT("int"), LONG("long"), FLOAT("float"), DOUBLE("double"), BOOLEAN("boolean"), DATE("date"), BINARY("binary");

        private String typeName;

        GwtDeviceAssetChannelType(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeName() {
            return typeName;
        }

        public static GwtDeviceAssetChannelType valueByTypeName(String lowercaseTypeName) {
            for (GwtDeviceAssetChannelType channelType : values()) {
                if (channelType.getTypeName().equals(lowercaseTypeName)) {
                    return channelType;
                }
            }
            return null;
        }
    }

    @Override
    public <X> X get(String property) {
        if ("timestampFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime(getTimestamp()));
        } else if ("modeEnum".equals(property)) {
            return (X) GwtDeviceAssetChannelMode.valueOf(getMode());
        } else if ("typeEnum".equals(property)) {
            return (X) GwtDeviceAssetChannelType.valueByTypeName(getType());
        } else {
            return super.get(property);
        }
    }

    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getType() {
        return get("type");
    }

    public GwtDeviceAssetChannelType getTypeEnum() {
        return get("typeEnum");
    }

    public void setType(String type) {
        set("type", type);
    }

    public String getError() {
        return get("error");
    }

    public void setError(String error) {
        set("error", error);
    }

    public String getValue() {
        return get("value");
    }

    public void setValue(String value) {
        set("value", value);
    }

    public Date getTimestamp() {
        return get("timestamp");
    }

    public String getTimestampFormatted() {
        return get("timestampFormatted");
    }

    public void setTimestamp(Date timestamp) {
        set("timestamp", timestamp);
    }

    public GwtDeviceAssetChannelMode getModeEnum() {
        return get("modeEnum");
    }

    public String getMode() {
        return get("mode");
    }

    public void setMode(String mode) {
        set("mode", mode);
    }
}
