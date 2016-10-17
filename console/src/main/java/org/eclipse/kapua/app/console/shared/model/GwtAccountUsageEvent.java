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
import java.util.Map;

public class GwtAccountUsageEvent extends KapuaBaseModel implements Serializable {


    private static final long serialVersionUID = 2626693661272183644L;

    private static final long KILO = 1024;

    public GwtAccountUsageEvent() {}

    public GwtAccountUsageEvent(Map<String,Object> props) {
        super();
        setProperties(props);
    }

    public Date getTimeStamp() {
        return (Date) get("timestamp");
    }


    public void setTimestamp(Date timestamp) {
        set("timestamp", timestamp);
    }

    public String getAccountName() {
        return (String) get("account_name");
    }


    public void setAccountName(String account_name) {
        set("account_name", account_name);
    }

    public String getAsset() {
        return (String) get("asset");
    }


    public void setAsset(String asset) {
        set("asset", asset);
    }


    public Long getEventTxCount() {
        return (Long) get("event_tx_count");
    }


    public void setEventTxCount(Long event_tx_count) {
        set("event_tx_count", event_tx_count);
        Float event_tx_count_kb = new Float(event_tx_count/KILO);
        set("event_tx_count_kb", event_tx_count_kb);
    }

    public Long getEventRxCount() {
        return (Long) get("event_rx_count");
    }


    public void setEventRxCount(Long event_rx_count) {
        set("event_rx_count", event_rx_count);
        Float event_rx_count_kb = new Float(event_rx_count/KILO);
        set("event_rx_count_kb", event_rx_count_kb);
    }


    public Float getEventTxCountKb() {
        return (Float) get("event_tx_count_kb");
    }


    public Float getEventRxCountKb() {
        return (Float) get("event_rx_count_kb");
    }
}
