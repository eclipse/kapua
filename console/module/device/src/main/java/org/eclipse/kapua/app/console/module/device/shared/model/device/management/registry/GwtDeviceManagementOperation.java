/*******************************************************************************
 * Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.shared.model.device.management.registry;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDeviceEvent;

import java.util.Date;

public class GwtDeviceManagementOperation extends GwtUpdatableEntityModel {

    @Override
    public <X> X get(String property) {
        if ("startedOnFormatted".equals(property)) {
            return (X) DateUtils.formatDateTime(getStartedOn());
        } else if ("endedOnFormatted".equals(property)) {
            return getEndedOn() != null ? (X) DateUtils.formatDateTime(getEndedOn()) : null;
        }

        return super.get(property);
    }

    public Date getStartedOn() {
        return get("startedOn");
    }

    public void setStartedOn(Date startedOn) {
        set("startedOn", startedOn);
    }

    public Date getEndedOn() {
        return get("endedOn");
    }

    public void setEndedOn(Date endedOn) {
        set("endedOn", endedOn);
    }

    public String getResource() {
        return get("resource");
    }

    public void setResource(String resource) {
        set("resource", resource);
    }

    public String getDeviceId() {
        return get("deviceId");
    }

    public void setDeviceId(String deviceId) {
        set("deviceId", deviceId);
    }

    public String getOperationId() {
        return get("operationId");
    }

    public void setOperationId(String operationId) {
        set("operationId", operationId);
    }

    public String getAppId() {
        return get("appId");
    }

    public void setAppId(String appId) {
        set("appId", appId);
    }

    public String getActionType() {
        return get("actionType");
    }

    public GwtDeviceEvent.GwtActionType getActionTypeEnum() {
        return get("actionTypeEnum");
    }

    public void setActionType(String actionType) {
        set("actionType", actionType);
    }

    public String getStatus() {
        return get("status");
    }

    public GwtOperationStatus getStatusEnum() {
        return getStatus() != null ? GwtOperationStatus.valueOf(getStatus()) : null;
    }

    public String getLog() {
        return get("log");
    }

    public String getUnescapedLog() {
        return getUnescaped("log");
    }

    public void setLog(String log) {
        set("log", log);
    }

    public void setStatus(String status) {
        set("status", status);
    }

    public void addInputProperty(String name, String value) {
        set("inputProperty_" + name.replace(".", ""), value);
    }
}
