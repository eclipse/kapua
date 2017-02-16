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

import org.eclipse.kapua.service.device.registry.event.DeviceEvent;

/**
 * Wrapper around DeviceEvent to make the DeviceEvent class comparable by its attributes.
 * It provides equals() method.
 */
public class CompEvent {

    private final DeviceEvent devEvnt;

    public CompEvent(DeviceEvent devEvnt) {
        this.devEvnt = devEvnt;
    }

    public DeviceEvent getEvent() {
        return devEvnt;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CompEvent) {
            CompEvent other = (CompEvent) obj;
            return compareAllEventAtt(devEvnt, other.getEvent());
        } else {
            return false;
        }
    }

    private boolean compareAllEventAtt(DeviceEvent thisEvnt, DeviceEvent othEvnt) {

        if (thisEvnt.getScopeId() == null != (othEvnt.getScopeId() == null)) {
            return false;
        }
        if (thisEvnt.getScopeId() != null && othEvnt.getScopeId() != null) {
            if (!thisEvnt.getScopeId().equals(othEvnt.getScopeId())) {
                return false;
            }
        }

        if (thisEvnt.getDeviceId() == null != (othEvnt.getDeviceId() == null)) {
            return false;
        }
        if (thisEvnt.getDeviceId() != null && othEvnt.getDeviceId() != null) {
            if (!thisEvnt.getDeviceId().equals(othEvnt.getDeviceId())) {
                return false;
            }
        }

        if (thisEvnt.getReceivedOn() == null != (othEvnt.getReceivedOn() == null)) {
            return false;
        }
        if (thisEvnt.getReceivedOn() != null && othEvnt.getReceivedOn() != null) {
            if (!thisEvnt.getReceivedOn().equals(othEvnt.getReceivedOn())) {
                return false;
            }
        }

        if (thisEvnt.getSentOn() == null != (othEvnt.getSentOn() == null)) {
            return false;
        }
        if (thisEvnt.getSentOn() != null && othEvnt.getSentOn() != null) {
            if (!thisEvnt.getSentOn().equals(othEvnt.getSentOn())) {
                return false;
            }
        }

        if (thisEvnt.getResource() == null != (othEvnt.getResource() == null)) {
            return false;
        }
        if (thisEvnt.getResource() != null && othEvnt.getResource() != null) {
            if (!thisEvnt.getResource().equals(othEvnt.getResource())) {
                return false;
            }
        }

        if (thisEvnt.getEventMessage() == null != (othEvnt.getEventMessage() == null)) {
            return false;
        }
        if (thisEvnt.getEventMessage() != null && othEvnt.getEventMessage() != null) {
            if (!thisEvnt.getEventMessage().equals(othEvnt.getEventMessage())) {
                return false;
            }
        }

        if (thisEvnt.getAction() == null != (othEvnt.getAction() == null)) {
            return false;
        }
        if (thisEvnt.getAction() != null && othEvnt.getAction() != null) {
            if (!thisEvnt.getAction().equals(othEvnt.getAction())) {
                return false;
            }
        }

        if (thisEvnt.getResponseCode() == null != (othEvnt.getResponseCode() == null)) {
            return false;
        }
        if (thisEvnt.getResponseCode() != null && othEvnt.getResponseCode() != null) {
            if (!thisEvnt.getResponseCode().equals(othEvnt.getResponseCode())) {
                return false;
            }
        }

        if (thisEvnt.getPosition().getAltitude() == null != (othEvnt.getPosition().getAltitude() == null)) {
            return false;
        }
        if (thisEvnt.getPosition().getAltitude() != null && othEvnt.getPosition() != null) {
            if (!thisEvnt.getPosition().getAltitude().equals(othEvnt.getPosition().getAltitude())) {
                return false;
            }
        }

        if (thisEvnt.getPosition().getHeading() == null != (othEvnt.getPosition().getHeading() == null)) {
            return false;
        }
        if (thisEvnt.getPosition().getHeading() != null && othEvnt.getPosition().getHeading() != null) {
            if (!thisEvnt.getPosition().getHeading().equals(othEvnt.getPosition().getHeading())) {
                return false;
            }
        }

        if (thisEvnt.getPosition().getLatitude() == null != (othEvnt.getPosition().getLatitude() == null)) {
            return false;
        }
        if (thisEvnt.getPosition().getLatitude() != null && othEvnt.getPosition().getLatitude() != null) {
            if (!thisEvnt.getPosition().getLatitude().equals(othEvnt.getPosition().getLatitude())) {
                return false;
            }
        }

        if (thisEvnt.getPosition().getLongitude() == null != (othEvnt.getPosition().getLongitude() == null)) {
            return false;
        }
        if (thisEvnt.getPosition().getLongitude() != null && othEvnt.getPosition().getLongitude() != null) {
            if (!thisEvnt.getPosition().getLongitude().equals(othEvnt.getPosition().getLongitude())) {
                return false;
            }
        }

        if (thisEvnt.getPosition().getSpeed() == null != (othEvnt.getPosition().getSpeed() == null)) {
            return false;
        }
        if (thisEvnt.getPosition().getSpeed() != null && othEvnt.getPosition().getSpeed() != null) {
            if (!thisEvnt.getPosition().getSpeed().equals(othEvnt.getPosition().getSpeed())) {
                return false;
            }
        }

        if (thisEvnt.getPosition().getPrecision() == null != (othEvnt.getPosition().getPrecision() == null)) {
            return false;
        }
        if (thisEvnt.getPosition().getPrecision() != null && othEvnt.getPosition().getPrecision() != null) {
            if (!thisEvnt.getPosition().getPrecision().equals(othEvnt.getPosition().getPrecision())) {
                return false;
            }
        }

        if (thisEvnt.getPosition().getSatellites() == null != (othEvnt.getPosition().getSatellites() == null)) {
            return false;
        }
        if (thisEvnt.getPosition().getSatellites() != null && othEvnt.getPosition().getSatellites() != null) {
            if (!thisEvnt.getPosition().getSatellites().equals(othEvnt.getPosition().getSatellites())) {
                return false;
            }
        }

        if (thisEvnt.getPosition().getStatus() == null != (othEvnt.getPosition().getStatus() == null)) {
            return false;
        }
        if (thisEvnt.getPosition().getStatus() != null && othEvnt.getPosition().getStatus() != null) {
            if (!thisEvnt.getPosition().getStatus().equals(othEvnt.getPosition().getStatus())) {
                return false;
            }
        }

        if (thisEvnt.getPosition().getTimestamp() == null != (othEvnt.getPosition().getTimestamp() == null)) {
            return false;
        }
        if (thisEvnt.getPosition().getTimestamp() != null && othEvnt.getPosition().getTimestamp() != null) {
            if (!thisEvnt.getPosition().getTimestamp().equals(othEvnt.getPosition().getTimestamp())) {
                return false;
            }
        }

        return true;
    }
}