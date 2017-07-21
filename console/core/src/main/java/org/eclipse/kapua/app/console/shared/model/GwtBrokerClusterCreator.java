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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GwtBrokerClusterCreator implements Serializable {

    private static final long serialVersionUID = 8077100502549884186L;

    private String name;
    private String description;
    private String dnsSubdomain;
    private List<String> dnsAliases;
    private Integer autoscaleCooldown;
    private Integer autoscaleMaxInstances;
    private Integer autoscaleMinInstances;
    private GwtBrokerType brokerType;
    private Collection<String> iaasAvailabilityZones;
    private String iaasKeyName;
    private String iaasMachineImage;
    private String userData;
    private String iaasInstanceType;
    private String iptablesRules;
    private Integer idleTimeout;
    private boolean enableMqtt;
    private boolean enableMqtts;
    private boolean enableWebSocket;
    private String sslCertificateId;

    public GwtBrokerClusterCreator() {
        super();
    }

    public GwtBrokerClusterCreator(String name, String dnsSubdomain, GwtBrokerType type) {
        this.name = name;
        this.description = "";
        this.dnsAliases = new ArrayList<String>();
        this.dnsSubdomain = dnsSubdomain;
        this.brokerType = type;
        this.iaasKeyName = "";
        this.iaasMachineImage = "";
        this.iptablesRules = "";
        this.enableMqtt = true;
        this.enableMqtts = true;
        this.enableWebSocket = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDnsSubdomain() {
        return dnsSubdomain;
    }

    public void setDnsSubdomain(String dnsSubdomain) {
        this.dnsSubdomain = dnsSubdomain;
    }

    public List<String> getDnsAliases() {
        return dnsAliases;
    }

    public void setDnsAliases(List<String> dnsAliases) {
        this.dnsAliases = new ArrayList<String>(dnsAliases);
    }

    public Integer getAutoscaleCooldown() {
        return autoscaleCooldown;
    }

    public void setAutoscaleCooldown(Integer autoscaleCooldown) {
        this.autoscaleCooldown = autoscaleCooldown;
    }

    public Integer getAutoscaleMaxInstances() {
        return autoscaleMaxInstances;
    }

    public void setAutoscaleMaxInstances(Integer autoscaleMaxInstances) {
        this.autoscaleMaxInstances = autoscaleMaxInstances;
    }

    public Integer getAutoscaleMinInstances() {
        return autoscaleMinInstances;
    }

    public void setAutoscaleMinInstances(Integer autoscaleMinInstances) {
        this.autoscaleMinInstances = autoscaleMinInstances;
    }

    public Collection<String> getIaasAvailabilityZones() {
        return iaasAvailabilityZones;
    }

    public void setIaasAvailabilityZones(Collection<String> iaasAvailabilityZones) {
        this.iaasAvailabilityZones = iaasAvailabilityZones;
    }

    public GwtBrokerType getBrokerType() {
        return brokerType;
    }

    public void setBrokerType(GwtBrokerType brokerType) {
        this.brokerType = brokerType;
    }

    public String getIaasKeyName() {
        return iaasKeyName;
    }

    public void setIaasKeyName(String iaasKeyName) {
        this.iaasKeyName = iaasKeyName;
    }

    public String getIaasMachineImage() {
        return iaasMachineImage;
    }

    public void setIaasMachineImage(String iaasMachineImage) {
        this.iaasMachineImage = iaasMachineImage;
    }

    public String getIaasInstanceType() {
        return iaasInstanceType;
    }

    public void setIaasInstanceType(String iaasInstanceType) {
        this.iaasInstanceType = iaasInstanceType;
    }

    public String getIptablesRules() {
        return iptablesRules;
    }

    public void setIptablesRules(String iptablesRules) {
        this.iptablesRules = iptablesRules;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public Integer getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(Integer idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public boolean getEnableMqtt() {
        return this.enableMqtt;
    }

    public void setEnableMqtt(boolean enableMqtt) {
        this.enableMqtt = enableMqtt;
    }

    public boolean getEnableMqtts() {
        return this.enableMqtts;
    }

    public void setEnableMqtts(boolean enableMqtts) {
        this.enableMqtts = enableMqtts;
    }

    public void setEnableWebSocket(boolean enableWebSocket) {
        this.enableWebSocket = enableWebSocket;
    }

    public boolean getEnableWebSocket() {
        return this.enableWebSocket;
    }

    public String getSslCertificateId() {
        return sslCertificateId;
    }

    public void setSslCertificateId(String sslCertificateId) {
        this.sslCertificateId = sslCertificateId;
    }

}
