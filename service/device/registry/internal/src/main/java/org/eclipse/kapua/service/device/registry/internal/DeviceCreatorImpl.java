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
package org.eclipse.kapua.service.device.registry.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceCredentialsMode;

/**
 * DeviceCreator encapsulates all the information needed to create a new Device in the system.
 * The data provided will be used to seed the new Device and its related information.
 * The fields of the DeviceCreator presents the attributes that are searchable for a given device.
 * The DeviceCreator Properties field can be used to provide additional properties associated to the Device;
 * those properties will not be searchable through Device queries.
 * The clientId field of the Device is used to store the MAC address of the primary network interface of the device.
 */
public class DeviceCreatorImpl extends AbstractKapuaEntityCreator<Device> implements DeviceCreator
{
    private static final long     serialVersionUID = 8628137091890107296L;
    private String                clientId;
    private KapuaId               connectionId;
    private String                displayName;
    private String                serialNumber;
    private String                modelId;
    private String                imei;
    private String                imsi;
    private String                iccid;
    private String                biosVersion;
    private String                firmwareVersion;
    private String                osVersion;
    private String                jvmVersion;
    private String                osgiFrameworkVersion;
    private String                applicationFrameworkVersion;
    private String                applicationIdentifiers;
    private String                acceptEncoding;
    private Double                gpsLongitude;
    private Double                gpsLatitude;
    private String                customAttribute1;    
    private String                customAttribute2;
    private String                customAttribute3;
    private String                customAttribute4;
    private String                customAttribute5;
    private DeviceCredentialsMode deviceCredentialsMode;
    private KapuaId               preferredUserId;

    protected DeviceCreatorImpl(KapuaId scopeId)
    {
        super(scopeId);
    }

    public String getClientId()
    {
        return clientId;
    }

    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    @Override
    public KapuaId getConnectionId()
    {
        return connectionId;
    }

    @Override
    public void setConnectionId(KapuaId connectionId)
    {
        this.connectionId = connectionId;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public String getModelId()
    {
        return modelId;
    }

    public void setModelId(String modelId)
    {
        this.modelId = modelId;
    }

    public String getImei()
    {
        return imei;
    }

    public void setImei(String imei)
    {
        this.imei = imei;
    }

    public String getImsi()
    {
        return imsi;
    }

    public void setImsi(String imsi)
    {
        this.imsi = imsi;
    }

    public String getIccid()
    {
        return iccid;
    }

    public void setIccid(String iccid)
    {
        this.iccid = iccid;
    }

    public String getBiosVersion()
    {
        return biosVersion;
    }

    public void setBiosVersion(String biosVersion)
    {
        this.biosVersion = biosVersion;
    }

    public String getFirmwareVersion()
    {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion)
    {
        this.firmwareVersion = firmwareVersion;
    }

    public String getOsVersion()
    {
        return osVersion;
    }

    public void setOsVersion(String osVersion)
    {
        this.osVersion = osVersion;
    }

    public String getJvmVersion()
    {
        return jvmVersion;
    }

    public void setJvmVersion(String jvmVersion)
    {
        this.jvmVersion = jvmVersion;
    }

    public String getOsgiFrameworkVersion()
    {
        return osgiFrameworkVersion;
    }

    public void setOsgiFrameworkVersion(String osgiFrameworkVersion)
    {
        this.osgiFrameworkVersion = osgiFrameworkVersion;
    }

    public String getApplicationFrameworkVersion()
    {
        return applicationFrameworkVersion;
    }

    public void setApplicationFrameworkVersion(String applicationFrameworkVersion)
    {
        this.applicationFrameworkVersion = applicationFrameworkVersion;
    }

    public String getApplicationIdentifiers()
    {
        return applicationIdentifiers;
    }

    public void setApplicationIdentifiers(String applicationIdentifiers)
    {
        this.applicationIdentifiers = applicationIdentifiers;
    }

    public String getAcceptEncoding()
    {
        return acceptEncoding;
    }

    public void setAcceptEncoding(String acceptEncoding)
    {
        this.acceptEncoding = acceptEncoding;
    }

    public Double getGpsLongitude()
    {
        return gpsLongitude;
    }

    public void setGpsLongitude(Double gpsLongitude)
    {
        this.gpsLongitude = gpsLongitude;
    }

    public Double getGpsLatitude()
    {
        return gpsLatitude;
    }

    public void setGpsLatitude(Double gpsLatitude)
    {
        this.gpsLatitude = gpsLatitude;
    }

    public String getCustomAttribute1()
    {
        return customAttribute1;
    }

    public void setCustomAttribute1(String customAttribute1)
    {
        this.customAttribute1 = customAttribute1;
    }

    public String getCustomAttribute2()
    {
        return customAttribute2;
    }

    public void setCustomAttribute2(String customAttribute2)
    {
        this.customAttribute2 = customAttribute2;
    }

    public String getCustomAttribute3()
    {
        return customAttribute3;
    }

    public void setCustomAttribute3(String customAttribute3)
    {
        this.customAttribute3 = customAttribute3;
    }

    public String getCustomAttribute4()
    {
        return customAttribute4;
    }

    public void setCustomAttribute4(String customAttribute4)
    {
        this.customAttribute4 = customAttribute4;
    }

    public String getCustomAttribute5()
    {
        return customAttribute5;
    }

    public void setCustomAttribute5(String customAttribute5)
    {
        this.customAttribute5 = customAttribute5;
    }

    public DeviceCredentialsMode getCredentialsMode()
    {
        return deviceCredentialsMode;
    }

    public void setCredentialsMode(DeviceCredentialsMode credentialsMode)
    {
        this.deviceCredentialsMode = credentialsMode;
    }

    public KapuaId getPreferredUserId()
    {
        return preferredUserId;
    }

    public void setPreferredUserId(KapuaId preferredUserId)
    {
        this.preferredUserId = preferredUserId;
    }
}
