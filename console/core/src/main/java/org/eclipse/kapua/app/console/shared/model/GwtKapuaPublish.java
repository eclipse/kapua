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
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GwtKapuaPublish implements Serializable {

    private static final long serialVersionUID = 2039487473051904185L;

    private GwtMqttTopic gwtMqttTopic;
    private Date timestamp;
    private Map<String, Number> numberValues;
    private Map<String, String> stringValues;
    private byte[] payload;

    public GwtKapuaPublish() {
        this.numberValues = new HashMap<String, Number>();
        this.stringValues = new HashMap<String, String>();
    }

    public GwtKapuaPublish(GwtMqttTopic topic, Date timestamp, byte[] payload) {
        this.gwtMqttTopic = topic;
        this.timestamp = timestamp;
        this.payload = payload;
        this.numberValues = new HashMap<String, Number>();
        this.stringValues = new HashMap<String, String>();
    }

    public GwtKapuaPublish(GwtMqttTopic topic, Date timestamp, Map<String, Number> numberValues, Map<String, String> stringValues) {
        this.gwtMqttTopic = topic;
        this.timestamp = timestamp;
        this.numberValues = numberValues;
        this.stringValues = stringValues;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public GwtMqttTopic getGwtMqttTopic() {
        return gwtMqttTopic;
    }

    public void setGwtMqttTopic(GwtMqttTopic topic) {
        this.gwtMqttTopic = topic;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public Map<String, Number> getNumberValues() {
        return numberValues;
    }

    public Map<String, String> getStringValues() {
        return stringValues;
    }

    @Override
    public String toString() {
        return "GwtKapuaPublish [topic=" + gwtMqttTopic + "]";
    }
}
