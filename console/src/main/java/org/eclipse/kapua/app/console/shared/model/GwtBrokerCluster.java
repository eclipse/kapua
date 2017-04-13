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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.kapua.app.console.client.util.DateUtils;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtBrokerCluster extends BaseModel implements Serializable
{

    private static final long   serialVersionUID = -7684741685354762692L;

    private GwtBrokerType       brokerType;
    private List<GwtBrokerNode> brokerNodes;
    private Set<String>         availabilityZones;
    private List<String>        dnsAliases;

    public enum GwtBrokerClusterStatus implements Serializable, IsSerializable
    {

        CREATED,
        PROVISIONING,
        PROVISIONING_FAILED,
        UPDATING,
        UPDATE_FAILED,
        READY,
        DEPROVISIONING,
        DEPROVISIONING_FAILED,
        DEPROVISIONED;

        GwtBrokerClusterStatus()
        {
        }
    }

    public GwtBrokerCluster()
    {
    }

    public Long getId()
    {
        return (Long) get("id");
    }

    public void setId(Long id)
    {
        set("id", id);
    }

    public String getDeploymentId()
    {
        return (String) get("deploymentId");
    }

    public void setDeploymentId(String deploymentId)
    {
        set("deploymentId", deploymentId);
    }

    public String getName()
    {
        return (String) get("name");
    }

    public void setName(String name)
    {
        set("name", name);
        setDisplayName();
    }

    public String getDescription()
    {
        return (String) get("description");
    }

    public void setDescription(String description)
    {
        set("description", description);
    }

    public String getDnsName()
    {
        return (String) get("dnsName");
    }

    public void setDnsName(String dnsName)
    {
        set("dnsName", dnsName);
        setDisplayName();
    }

    public List<String> getDnsAliases()
    {
        return this.dnsAliases;
    }

    public void setDnsAliases(Collection<String> dnsAliases)
    {
        if (dnsAliases != null) {
            this.dnsAliases = new ArrayList<String>(dnsAliases);
        }
    }

    public Integer getAutoscaleCooldown()
    {
        return (Integer) get("autoscaleCooldown");
    }

    public void setAutoscaleCooldown(Integer autoscaleCooldown)
    {
        set("autoscaleCooldown", autoscaleCooldown);
    }

    public Integer getAutoscaleMaxInstances()
    {
        return (Integer) get("autoscaleMaxInstances");
    }

    public void setAutoscaleMaxInstances(Integer autoscaleMaxInstances)
    {
        set("autoscaleMaxInstances", autoscaleMaxInstances);
    }

    public Integer getAutoscaleMinInstances()
    {
        return (Integer) get("autoscaleMinInstances");
    }

    public void setAutoscaleMinInstances(Integer autoscaleMinInstances)
    {
        set("autoscaleMinInstances", autoscaleMinInstances);
    }

    public Set<String> getAvailabilityZones()
    {
        return availabilityZones;
    }

    public void setAvailabilityZones(Collection<String> availabilityZones)
    {
        if (availabilityZones != null) {
            this.availabilityZones = new TreeSet<String>(availabilityZones);
        }
    }

    public String getBrokerType()
    {
        return (String) get("brokerType");
    }

    public GwtBrokerType getBrokerTypeEnum()
    {
        return (GwtBrokerType) get("brokerTypeEnum");
    }

    public void setBrokerType(GwtBrokerType brokerType)
    {
        set("brokerType", brokerType.name());
    }

    public String getMqttBrokerUrl()
    {
        return (String) get("mqttBrokerUrl");
    }

    public void setMqttBrokerUrl(String mqttBrokerUrl)
    {
        set("mqttBrokerUrl", mqttBrokerUrl);
    }

    public String getIaasInstanceType()
    {
        return (String) get("iaasInstanceType");
    }

    public void setIaasInstanceType(String iaasInstanceType)
    {
        set("iaasInstanceType", iaasInstanceType);
    }

    public String getIaasKeyName()
    {
        return (String) get("iaasKeyName");
    }

    public void setIaasKeyName(String iaasKeyName)
    {
        set("iaasKeyName", iaasKeyName);
    }

    public String getIaasMachineImage()
    {
        return (String) get("iaasMachineImage");
    }

    public void setIaasMachineImage(String iaasMachineImage)
    {
        set("iaasMachineImage", iaasMachineImage);
    }

    public String getIptablesRules()
    {
        return (String) get("iptablesRules");
    }

    public void setIptablesRules(String iptablesRules)
    {
        set("iptablesRules", iptablesRules);
    }

    public String getUserData()
    {
        return (String) get("userData");
    }

    public void setUserData(String userData)
    {
        set("userData", userData);
    }

    public List<GwtBrokerNode> getBrokerNodes()
    {
        return this.brokerNodes;
    }

    public void setBrokerNodes(List<GwtBrokerNode> brokerNodes)
    {
        this.brokerNodes = brokerNodes;
    }

    public Integer getIdleTimeout()
    {
        return (Integer) get("idleTimeout");
    }

    public void setIdleTimeout(Integer idleTimeout)
    {
        set("idleTimeout", idleTimeout);
    }

    public Boolean getEnableMqtt()
    {
        return (Boolean) get("enableMqtt");
    }

    public void setEnableMqtt(Boolean enableMqtt)
    {
        set("enableMqtt", enableMqtt);
    }

    public Boolean getEnableMqtts()
    {
        return (Boolean) get("enableMqtts");
    }

    public void setEnableMqtts(Boolean enableMqtts)
    {
        set("enableMqtts", enableMqtts);
    }

    public Boolean getEnableWebSocket()
    {
        return (Boolean) get("enableWebSocket");
    }

    public void setEnableWebSocket(Boolean enableWebSocket)
    {
        set("enableWebSocket", enableWebSocket);
    }

    public String getStatus()
    {
        return (String) get("status");
    }

    public GwtBrokerClusterStatus getStatusEnum()
    {
        return (GwtBrokerClusterStatus) get("statusEnum");
    }

    public void setStatus(String status)
    {
        set("status", status);
    }

    public Date getModifiedOn()
    {
        return (Date) get("modifiedOn");
    }

    public String getModifiedOnFormatted()
    {
        return DateUtils.formatDateTime((Date) get("modifiedOn"));
    }

    public void setModifiedOn(Date modifiedOn)
    {
        set("modifiedOn", modifiedOn);
    }

    public Date getCreatedOn()
    {
        return (Date) get("createdOn");
    }

    public String getCreatedOnFormatted()
    {
        return DateUtils.formatDateTime((Date) get("createdOn"));
    }

    public void setCreatedOn(Date createdOn)
    {
        set("createdOn", createdOn);
    }

    private void setDisplayName()
    {
        set("displayName", getDnsName());
    }

    public String getDisplayName()
    {
        return get("displayName");
    }

    @Override
    public String toString()
    {
        return getDisplayName();
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property)
    {
        if ("modifiedOnFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime((Date) get("modifiedOn")));
        }
        else if ("createdOnFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime((Date) get("createdOn")));
        }
        else if ("brokerTypeEnum".equals(property)) {
            return (X) (GwtBrokerType.valueOf(getBrokerType()));
        }
        else if ("statusEnum".equals(property)) {
            return (X) (GwtBrokerClusterStatus.valueOf(getStatus()));
        }
        else if ("availabilityZones".equals(property)) {
            return (X) (toString(availabilityZones));
        }
        else {
            return super.get(property);
        }
    }

    // Show collection as a string
    private String toString(Collection<String> collection)
    {
        StringBuffer sb = new StringBuffer();
        String delim = "";

        if (collection != null) {
            for (String item : collection) {
                sb.append(delim).append(item);
                delim = ",";
            }
        }

        return sb.toString();
    }

}
