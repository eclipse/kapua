/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.data.shared.model;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

public class GwtDatastoreDevice extends KapuaBaseModel implements Serializable {
    private static final long serialVersionUID = 5756712401178232349L;

    public GwtDatastoreDevice() {
    }

    public GwtDatastoreDevice(String device, Date timestamp) {
        set("device", device);
        set("timestamp", timestamp);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <X> X get(String property) {
        if ("timestampFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime(getTimestamp()));
        } else {
            return super.get(property);
        }
    }

    public String getDevice() {
        return (String) get("device");
    }

    public String getUnescapedDevice() {
        return (String) getUnescaped("device");
    }

    public String getFriendlyDevice() {
        return (String) get("friendlyDevice");
    }

    public void setFriendlyDevice(String friendlyDevice) {
        set("friendlyDevice", friendlyDevice);
    }

    public Date getTimestamp() {
        return (Date) get("timestamp");
    }

    public String getTimestampFormatted() {
        return (String) get("timestampFormatted");
    }
}
