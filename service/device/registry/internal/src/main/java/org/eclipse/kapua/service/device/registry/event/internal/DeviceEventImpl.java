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
package org.eclipse.kapua.service.device.registry.event.internal;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.internal.KapuaPositionImpl;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;

/**
 * {@link DeviceEvent} implementation.
 * 
 * @since 1.0.0
 *
 */
@Entity(name = "DeviceEvent")
@Table(name = "dvc_device_event")
public class DeviceEventImpl extends AbstractKapuaEntity implements DeviceEvent {

    private static final long serialVersionUID = 7142819355352738950L;

    @Embedded
    @AttributeOverrides({
                          @AttributeOverride(name = "eid", column = @Column(name = "device_id", nullable = false, updatable = false))
    })
    private KapuaEid          deviceId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "received_on", updatable = false, nullable = false)
    private Date              receivedOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent_on", updatable = false)
    private Date              sentOn;

    @Embedded
    @AttributeOverrides({
                          @AttributeOverride(name = "longitude", column = @Column(name = "pos_longitude", updatable = false)),
                          @AttributeOverride(name = "latitude", column = @Column(name = "pos_latitude", updatable = false)),
                          @AttributeOverride(name = "altitude", column = @Column(name = "pos_altitude", updatable = false)),
                          @AttributeOverride(name = "precision", column = @Column(name = "pos_precision", updatable = false)),
                          @AttributeOverride(name = "heading", column = @Column(name = "pos_heading", updatable = false)),
                          @AttributeOverride(name = "speed", column = @Column(name = "pos_speed", updatable = false)),
                          @AttributeOverride(name = "timestamp", column = @Column(name = "pos_timestamp", updatable = false)),
                          @AttributeOverride(name = "satellites", column = @Column(name = "pos_satellites", updatable = false)),
                          @AttributeOverride(name = "status", column = @Column(name = "pos_status", updatable = false))
    })
    private KapuaPositionImpl position;

    @Basic
    @Column(name = "resource", updatable = false, nullable = false)
    private String            resource;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", updatable = false, nullable = false)
    private KapuaMethod       action;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_code", updatable = false, nullable = false)
    private KapuaResponseCode responseCode;

    @Lob
    @Column(name = "event_message", updatable = false, nullable = false)
    private String            eventMessage;

    /**
     * Constructor
     */
    protected DeviceEventImpl() {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public DeviceEventImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Constructor
     * 
     * @param deviceEvent
     */
    public DeviceEventImpl(DeviceEvent deviceEvent) throws KapuaException {
        super((KapuaEntity) deviceEvent);

        setDeviceId(deviceEvent.getDeviceId());
        setReceivedOn(deviceEvent.getReceivedOn());
        setSentOn(deviceEvent.getSentOn());
        setPosition(deviceEvent.getPosition());
        setResource(deviceEvent.getResource());
        setAction(deviceEvent.getAction());
        setResponseCode(deviceEvent.getResponseCode());
        setEventMessage(deviceEvent.getEventMessage());
    }

    @Override
    public KapuaId getDeviceId() {
        return deviceId;
    }

    @Override
    public void setDeviceId(KapuaId deviceId) {
        this.deviceId = (KapuaEid) deviceId;
    }

    @Override
    public Date getSentOn() {
        return sentOn;
    }

    @Override
    public void setSentOn(Date sentOn) {
        this.sentOn = sentOn;
    }

    @Override
    public Date getReceivedOn() {
        return receivedOn;
    }

    @Override
    public void setReceivedOn(Date receivedOn) {
        this.receivedOn = receivedOn;
    }

    @Override
    public KapuaPosition getPosition() {
        return position;
    }

    @Override
    public void setPosition(KapuaPosition position) {
        if (position != null) {
            this.position = new KapuaPositionImpl();
            this.position.setAltitude(position.getAltitude());
            this.position.setHeading(position.getHeading());
            this.position.setLatitude(position.getLatitude());
            this.position.setLongitude(position.getLongitude());
            this.position.setPrecision(position.getPrecision());
            this.position.setSatellites(position.getSatellites());
            this.position.setSpeed(position.getSpeed());
            this.position.setStatus(position.getStatus());
            this.position.setTimestamp(position.getTimestamp());
        }
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public KapuaMethod getAction() {
        return action;
    }

    @Override
    public void setAction(KapuaMethod action) {
        this.action = action;
    }

    @Override
    public KapuaResponseCode getResponseCode() {
        return responseCode;
    }

    @Override
    public void setResponseCode(KapuaResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String getEventMessage() {
        return eventMessage;
    }

    @Override
    public void setEventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
    }

}
