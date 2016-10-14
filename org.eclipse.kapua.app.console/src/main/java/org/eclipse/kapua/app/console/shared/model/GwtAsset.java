/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.kapua.app.console.client.util.DateUtils;

public class GwtAsset extends KapuaBaseModel implements Serializable {
    private static final long serialVersionUID = 5756712401178232349L;

    public GwtAsset() {
    }

    public GwtAsset(String asset, Date timestamp) {
        set("asset", asset);
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

    public String getAsset() {
        return (String) get("asset");
    }

    public String getUnescapedAsset() {
        return (String) getUnescaped("asset");
    }

    public String getFriendlyAsset() {
        return (String) get("friendlyAsset");
    }

    public void setFriendlyAsset(String friendlyAsset) {
        set("friendlyAsset", friendlyAsset);
    }

    public Date getTimestamp() {
        return (Date) get("timestamp");
    }

    public String getTimestampFormatted() {
        return (String) get("timestampFormatted");
    }
}
