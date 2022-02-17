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
package org.eclipse.kapua.app.console.module.data.shared.model;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import java.io.Serializable;
import java.util.Date;

public class GwtDatastoreDevice extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = 5756712401178232349L;

    public static final long NO_TIMESTAMP = new Date(0).getTime();

    private static final String DEVICE = "device";
    private static final String TIMESTAMP = "timestamp";

    public GwtDatastoreDevice() {
        super();
    }

    public GwtDatastoreDevice(String device, Date timestamp) {
        set(DEVICE, device);
        set(TIMESTAMP, timestamp);
    }

    @Override
    public <X> X get(String property) {
        if ("timestampFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime(getTimestamp()));
        } else {
            return super.get(property);
        }
    }

    public String getDevice() {
        return (String) get(DEVICE);
    }

    public String getUnescapedDevice() {
        return (String) getUnescaped(DEVICE);
    }

    public String getFriendlyDevice() {
        return (String) get("friendlyDevice");
    }

    public void setFriendlyDevice(String friendlyDevice) {
        set("friendlyDevice", friendlyDevice);
    }

    public Date getTimestamp() {
        return (Date) get(TIMESTAMP);
    }

    public String getTimestampFormatted() {
        return (String) get("timestampFormatted");
    }

    public void setTimestamp(Date timestamp) {
        set(TIMESTAMP, timestamp);
    }

    public String getClientId() {
        return get("clientId");
    }

    public void setClientId(String clientId) {
        set("clientId", clientId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GwtDatastoreDevice) {
            return getClientId().equals(((GwtDatastoreDevice) obj).getClientId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
