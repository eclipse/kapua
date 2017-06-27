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
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.kapua.app.console.commons.client.util.DateUtils;

import com.extjs.gxt.ui.client.data.BaseModel;

public class GwtBrokerNode extends BaseModel implements Serializable {

    private static final long serialVersionUID = -921367833245581297L;

    public GwtBrokerNode() {
    }

    public String getId() {
        return (String) get("id");
    }

    public void setId(String id) {
        set("id", id);
    }

    public Long getBrokerClusterId() {
        return (Long) get("brokerClusterId");
    }

    public void setBrokerClusterId(Long brokerClusterId) {
        set("brokerClusterId", brokerClusterId);
    }

    public String getStatus() {
        return (String) get("status");
    }

    public void setStatus(String status) {
        set("status", status);
    }

    public String getBrokerVersion() {
        return (String) get("brokerVersion");
    }

    public void setBrokerVersion(String brokerVersion) {
        set("brokerVersion", brokerVersion);
    }

    public String getMachineImage() {
        return (String) get("machineImage");
    }

    public void setMachineImage(String machineImage) {
        set("machineImage", machineImage);
    }

    public String getKeyName() {
        return (String) get("keyName");
    }

    public void setKeyName(String keyName) {
        set("keyName", keyName);
    }

    public String getInstanceType() {
        return (String) get("instanceType");
    }

    public void setInstanceType(String instanceType) {
        set("instanceType", instanceType);
    }

    public String getAvailabilityZone() {
        return (String) get("availabilityZone");
    }

    public void setAvailabilityZone(String availabilityZone) {
        set("availabilityZone", availabilityZone);
    }

    public String getInstanceId() {
        return (String) get("instanceId");
    }

    public void setInstanceId(String instanceId) {
        set("instanceId", instanceId);
    }

    public String getDnsName() {
        return (String) get("dnsName");
    }

    public void setDnsName(String dnsName) {
        set("dnsName", dnsName);
    }

    public String getPublicIp() {
        return (String) get("publicIp");
    }

    public void setPublicIp(String publicIp) {
        set("publicIp", publicIp);
    }

    public String getPrivateIp() {
        return (String) get("privateIp");
    }

    public void setPrivateIp(String privateIp) {
        set("privateIp", privateIp);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("modifiedOnFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime((Date) get("modifiedOn")));
        } else if ("createdOnFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime((Date) get("createdOn")));
        } else {
            return super.get(property);
        }
    }

}
