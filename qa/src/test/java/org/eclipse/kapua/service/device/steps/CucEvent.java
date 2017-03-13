/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.internal.KapuaPositionImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.response.KapuaResponseCode;

/**
 * Data object used in Gherkin to input Device Event parameters.
 * The data setters intentionally use only cucumber-friendly data types and
 * generate the resulting Kapua types internally.
 * Note: The Date parameters are defined as yyyy-MM-ddTHH:mm:ss
 * (e.g. 2017-01-25T12:08:56)
 */
public class CucEvent {

    KapuaId scopeId;
    KapuaId deviceId;
    Date receivedOn;
    Date sentOn;
    String resource;
    KapuaMethod action;
    KapuaResponseCode responseCode;
    String eventMessage;
    KapuaPosition position;

    public CucEvent() {
        scopeId = null;
        deviceId = null;
        receivedOn = null;
        sentOn = null;
        resource = null;
        action = null;
        responseCode = null;
        eventMessage = null;
        position = new KapuaPositionImpl();
        position.setLatitude(0.0);
        position.setLongitude(0.0);
        position.setAltitude(0.0);
        position.setHeading(0.0);
        position.setSpeed(0.0);
        position.setPrecision(0.0);
        position.setSatellites(0);
        position.setStatus(0);
        position.setTimestamp(new Date(0L));
    }

    public KapuaId getScopeId() {
        return scopeId;
    }

    public void setScopeId(long scopeId) {
        this.scopeId = new KapuaEid(BigInteger.valueOf(scopeId));
    }

    public KapuaId getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = new KapuaEid(BigInteger.valueOf(deviceId));
        ;
    }

    public Date getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(String receivedOn) {
        this.receivedOn = string2date(receivedOn);
    }

    public Date getSentOn() {
        return sentOn;
    }

    public void setSentOn(String sentOn) {
        this.sentOn = string2date(sentOn);
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public KapuaMethod getAction() {
        return action;
    }

    public void setAction(String action) {

        this.action = null;

        switch (action.trim().toUpperCase()) {
        case "READ":
            this.action = KapuaMethod.READ;
            break;
        case "CREATE":
            this.action = KapuaMethod.CREATE;
            break;
        case "WRITE":
            this.action = KapuaMethod.WRITE;
            break;
        case "DELETE":
            this.action = KapuaMethod.DELETE;
            break;
        case "OPTIONS":
            this.action = KapuaMethod.OPTIONS;
            break;
        case "EXECUTE":
            this.action = KapuaMethod.EXECUTE;
            break;
        }
    }

    public KapuaResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = null;
        switch (responseCode.trim().toUpperCase()) {
        case "ACCEPTED":
            this.responseCode = KapuaResponseCode.ACCEPTED;
            break;
        case "BAD_REQUEST":
            this.responseCode = KapuaResponseCode.BAD_REQUEST;
            break;
        case "NOT_FOUND":
            this.responseCode = KapuaResponseCode.NOT_FOUND;
            break;
        case "INTERNAL_ERROR":
            this.responseCode = KapuaResponseCode.INTERNAL_ERROR;
            break;
        }
    }

    public String getEventMessage() {
        return eventMessage;
    }

    public void setEventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
    }

    public void setLattitude(double lat) {
        position.setLatitude(lat);
    }

    public void setLongitude(double lon) {
        position.setLongitude(lon);
    }

    public void setAltitude(double alt) {
        position.setAltitude(alt);
    }

    public void setHeading(double head) {
        position.setHeading(head);
    }

    public void setSpeed(double speed) {
        position.setSpeed(speed);
    }

    public void setPrecision(double prec) {
        position.setPrecision(prec);
    }

    public void setSatellites(int num) {
        position.setSatellites(num);
    }

    public void setStatus(int stat) {
        position.setStatus(stat);
    }

    public void setTimestamp(String date) {
        position.setTimestamp(string2date(date));
    }

    public KapuaPosition getPosition() {
        return position;
    }

    // Private helper functions

    private Date string2date(String timestamp) {
        Date tmpDate = null;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        try {
            tmpDate = fmt.parse(timestamp);
        } catch (Exception ex) {
            // Do nothing. Just leave the date as null!
        }
        return tmpDate;
    }
}
