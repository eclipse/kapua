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
package org.eclipse.kapua.service.device.registry;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Device is an object representing a device or gateway connected to the Kapua platform.
 * The Device object contains several attributes regarding the Device itself and its software configuration.
 */
@XmlRootElement(name = "device")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "clientId",
                       "connectionId",
                       "status",
                       "displayName",
                       "lastEventOn",
                       "lastEventType",
                       "serialNumber",
                       "modelId",
                       "imei",
                       "imsi",
                       "iccid",
                       "biosVersion",
                       "firmwareVersion",
                       "osVersion",
                       "jvmVersion",
                       "osgiFrameworkVersion",
                       "applicationFrameworkVersion",
                       "applicationIdentifiers",
                       "acceptEncoding",
                       "gpsLongitude",
                       "gpsLatitude",
                       "customAttribute1",
                       "customAttribute2",
                       "customAttribute3",
                       "customAttribute4",
                       "customAttribute5",
                       "credentialsMode",
                       "preferredUserId"
        // // derived attributes
        // "uptime",
        // "modelName",
        // "partNumber",
        // "availableProcessors",
        // "totalMemory",
        // "os",
        // "osArch",
        // "jvmName",
        // "jvmProfile",
        // "osgiFramework",
        // "connectionInterface",
        // "gpsAltitude"
},
factoryClass = DeviceXmlRegistry.class, 
factoryMethod = "newDevice")
public interface Device extends KapuaUpdatableEntity
{
    public static final String TYPE = "dvce";

    default public String getType()
    {
        return TYPE;
    }

    @XmlElement(name = "clientId")
    public String getClientId();

    public void setClientId(String clientId);

    @XmlElement(name = "connectionId")
    public KapuaId getConnectionId();

    public void setConnectionId(KapuaId connectionId);

    @XmlElement(name = "status")
    public DeviceStatus getStatus();

    public void setStatus(DeviceStatus status);

    @XmlElement(name = "displayName")
    public String getDisplayName();

    public void setDisplayName(String diplayName);

    @XmlElement(name = "lastEventOn")
    public Date getLastEventOn();

    public void setLastEventOn(Date lastEventOn);

    @XmlElement(name = "lastEventType")
    public DeviceEventType getLastEventType();

    public void setLastEventType(DeviceEventType lastEventType);

    @XmlElement(name = "serialNumber")
    public String getSerialNumber();

    public void setSerialNumber(String serialNumber);

    @XmlElement(name = "modelId")
    public String getModelId();

    public void setModelId(String modelId);

    @XmlElement(name = "imei")
    public String getImei();

    public void setImei(String imei);

    @XmlElement(name = "imsi")
    public String getImsi();

    public void setImsi(String imsi);

    @XmlElement(name = "iccid")
    public String getIccid();

    public void setIccid(String iccid);

    @XmlElement(name = "biosVersion")
    public String getBiosVersion();

    public void setBiosVersion(String biosVersion);

    @XmlElement(name = "firmwareVersion")
    public String getFirmwareVersion();

    public void setFirmwareVersion(String firmwareVersion);

    @XmlElement(name = "osVersion")
    public String getOsVersion();

    public void setOsVersion(String osVersion);

    @XmlElement(name = "jvmVersion")
    public String getJvmVersion();

    public void setJvmVersion(String jvmVersion);

    @XmlElement(name = "osgiFrameworkVersion")
    public String getOsgiFrameworkVersion();

    public void setOsgiFrameworkVersion(String osgiFrameworkVersion);

    @XmlElement(name = "applicationFrameworkVersion")
    public String getApplicationFrameworkVersion();

    public void setApplicationFrameworkVersion(String appFrameworkVersion);

    @XmlElement(name = "applicationIdentifiers")
    public String getApplicationIdentifiers();

    public void setApplicationIdentifiers(String applicationIdentifiers);

    @XmlElement(name = "acceptEncoding")
    public String getAcceptEncoding();

    public void setAcceptEncoding(String acceptEncoding);

    @XmlElement(name = "gpsLongitude")
    public Double getGpsLongitude();

    public void setGpsLongitude(Double gpsLongitude);

    @XmlElement(name = "gpsLatitude")
    public Double getGpsLatitude();

    public void setGpsLatitude(Double gpsLatitude);

    @XmlElement(name = "customAttribute1")
    public String getCustomAttribute1();

    public void setCustomAttribute1(String customAttribute1);

    @XmlElement(name = "customAttribute2")
    public String getCustomAttribute2();

    public void setCustomAttribute2(String customAttribute2);

    @XmlElement(name = "customAttribute3")
    public String getCustomAttribute3();

    public void setCustomAttribute3(String customAttribute3);

    @XmlElement(name = "customAttribute4")
    public String getCustomAttribute4();

    public void setCustomAttribute4(String customAttribute4);

    @XmlElement(name = "customAttribute5")
    public String getCustomAttribute5();

    public void setCustomAttribute5(String customAttribute5);

    @XmlElement(name = "devoceCredentialsMode")
    public DeviceCredentialsMode getCredentialsMode();

    public void setCredentialsMode(DeviceCredentialsMode credentialsMode);

    @XmlElement(name = "preferredUserId")
    public KapuaId getPreferredUserId();

    public void setPreferredUserId(KapuaId deviceUserId);

    /*
     * // --------------------------
     * //
     * // non-indexed properties
     * //
     * // --------------------------
     * public long getUptime();
     * 
     * public void setUptime(long uptime);
     * 
     * public String getModelName();
     * 
     * public void setModelName(String modelName);
     * 
     * public String getPartNumber();
     * 
     * public void setPartNumber(String partNumber);
     * 
     * public String getAvailableProcessors();
     * 
     * public void setAvailableProcessors(String availableProcessors);
     * 
     * public String getTotalMemory();
     * 
     * public void setTotalMemory(String totalMemory);
     * 
     * public String getOs();
     * 
     * public void setOs(String os);
     * 
     * public String getOsArch();
     * 
     * public void setOsArch(String osArch);
     * 
     * public String getJvmName();
     * 
     * public void setJvmName(String jvmName);
     * 
     * public String getJvmProfile();
     * 
     * public void setJvmProfile(String jvmProfile);
     * 
     * public String getOsgiFramework();
     * 
     * public void setOsgiFramework(String osgiFramework);
     * 
     * public String getConnectionInterface();
     * 
     * public void setConnectionInterface(String connectionInterface);
     * 
     * public Double getGpsAltitude();
     * 
     * public void setGpsAltitude(Double gpsAltitude);
     */
}
