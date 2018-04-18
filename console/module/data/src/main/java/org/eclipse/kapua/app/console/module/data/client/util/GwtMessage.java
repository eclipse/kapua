/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.data.client.util;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

public class GwtMessage extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = 5344433827767946887L;

    public GwtMessage() {
        setAllowNestedValues(false);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("timestampFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime(getTimestamp()));
        } else {
            return super.get(property);
        }
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

    public String getChannel() {
        return get("channel");
    }

    public void setChannel(String channel) {
        set("channel", channel);
    }

    public String getClientId() {
        return get("clientId");
    }

    public void setClientId(String clientId) {
        set("clientId", clientId);
    }
}
