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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * DeviceCreator encapsulates all the information needed to create a new Device in the system.
 * The data provided will be used to seed the new Device and its related information.
 * The fields of the DeviceCreator presents the attributes that are searchable for a given device.
 * The DeviceCreator Properties field can be used to provide additional properties associated to the Device;
 * those properties will not be searchable through Device queries.
 * The clientId field of the Device is used to store the MAC address of the primary network interface of the device.
 */
@XmlRootElement(name = "deviceCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "clientId",
        "connectionId",
        "displayName",
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
        "customAttribute1",
        "customAttribute2",
        "customAttribute3",
        "customAttribute4",
        "customAttribute5",
        "credentialsMode",
        "preferredUserId" }, factoryClass = DeviceXmlRegistry.class, factoryMethod = "newDeviceCreator")
public interface DeviceCreator extends KapuaUpdatableEntityCreator<Device> {

    @XmlElement(name = "clientId")
    public String getClientId();

    public void setClientId(String clientId);

    @XmlElement(name = "connectionId")
    public KapuaId getConnectionId();

    public void setConnectionId(KapuaId connectionId);

    @XmlElement(name = "displayName")
    public String getDisplayName();

    public void setDisplayName(String displayName);

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

    @XmlElement(name = "credentialsMode")
    public DeviceCredentialsMode getCredentialsMode();

    public void setCredentialsMode(DeviceCredentialsMode credentialsMode);

    @XmlElement(name = "preferredUserId")
    public KapuaId getPreferredUserId();

    public void setPreferredUserId(KapuaId preferredUserId);
}
