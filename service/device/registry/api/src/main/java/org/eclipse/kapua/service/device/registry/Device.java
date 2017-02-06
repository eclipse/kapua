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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;

/**
 * {@link Device} is an object representing a device or gateway connected to the Kapua platform.<br>
 * The {@link Device} object contains several attributes regarding the Device itself and its software configuration.<br>
 * {@link Device} contains also references to {@link DeviceConnection} and the last {@link DeviceEvent}.
 * 
 * @since 1.0.0
 */
@XmlRootElement(name = "device")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "groupId",
        "clientId",
        "connectionId",
        "connection",
        "status",
        "displayName",
        "lastEventId",
        "lastEvent",
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
}, factoryClass = DeviceXmlRegistry.class, factoryMethod = "newDevice")
public interface Device extends KapuaUpdatableEntity {

    public static final String TYPE = "dvce";

    public default String getType() {
        return TYPE;
    }

    /**
     * Get the group identifier
     * 
     * @return
     */
    @XmlElement(name = "groupId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getGroupId();

    /**
     * Set the group identifier
     * 
     * @param groupId
     */
    public void setGroupId(KapuaId groupId);

    /**
     * Get the client identifier
     * 
     * @return
     */
    @XmlElement(name = "clientId")
    public String getClientId();

    /**
     * Set the client identifier
     * 
     * @param clientId
     */
    public void setClientId(String clientId);

    /**
     * Get the connection identifier
     * 
     * @return
     */
    @XmlElement(name = "connectionId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getConnectionId();

    /**
     * Set the connection identifier
     * 
     * @param connectionId
     */
    public void setConnectionId(KapuaId connectionId);

    /**
     * Gets the {@link DeviceConnection}
     * 
     * @return
     */
    @XmlElement(name = "connection")
    public <C extends DeviceConnection> C getConnection();

    /**
     * Set the {@link DeviceConnection}
     * 
     * @param connection
     * @throws KapuaException
     */
    public <C extends DeviceConnection> void setConnection(C connection) throws KapuaException;

    /**
     * Get the connection status
     * 
     * @return
     */
    @XmlElement(name = "status")
    public DeviceStatus getStatus();

    /**
     * Set the connection status
     * 
     * @param status
     */
    public void setStatus(DeviceStatus status);

    /**
     * Get the display name
     * 
     * @return
     */
    @XmlElement(name = "displayName")
    public String getDisplayName();

    /**
     * Set the display name
     * 
     * @param diplayName
     */
    public void setDisplayName(String diplayName);

    /**
     * Gets the last {@link DeviceEvent} {@link KapuaId}.
     * 
     * @return
     */
    @XmlElement(name = "lastEventId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getLastEventId();

    /**
     * Set the last {@link DeviceEvent} {@link KapuaId}.
     * 
     * @param lastEventId
     */
    public void setLastEventId(KapuaId lastEventId);

    /**
     * Get the last {@link DeviceEvent} for this {@link Device}.
     * 
     * @return The last {@link DeviceEvent} for this {@link Device}.
     */
    @XmlElement(name = "lastEvent")
    public <E extends DeviceEvent> E getLastEvent();

    /**
     * Sets the last {@link DeviceEvent} for this {@link Device}.
     * 
     * @param lastEvent
     *            The last {@link DeviceEvent} for this {@link Device}.
     */
    public <E extends DeviceEvent> void setLastEvent(E lastEvent) throws KapuaException;

    /**
     * Get the serial number
     * 
     * @return
     */
    @XmlElement(name = "serialNumber")
    public String getSerialNumber();

    /**
     * Set the serial number
     * 
     * @param serialNumber
     */
    public void setSerialNumber(String serialNumber);

    /**
     * Get the model identifier
     * 
     * @return
     */
    @XmlElement(name = "modelId")
    public String getModelId();

    /**
     * Set the model identifier
     * 
     * @param modelId
     */
    public void setModelId(String modelId);

    /**
     * Get the imei
     * 
     * @return
     */
    @XmlElement(name = "imei")
    public String getImei();

    /**
     * Set the imei
     * 
     * @param imei
     */
    public void setImei(String imei);

    /**
     * Get the imsi
     * 
     * @return
     */
    @XmlElement(name = "imsi")
    public String getImsi();

    /**
     * Set the imsi
     * 
     * @param imsi
     */
    public void setImsi(String imsi);

    /**
     * Get the iccid
     * 
     * @return
     */
    @XmlElement(name = "iccid")
    public String getIccid();

    /**
     * Set the iccid
     * 
     * @param iccid
     */
    public void setIccid(String iccid);

    /**
     * Get bios version
     * 
     * @return
     */
    @XmlElement(name = "biosVersion")
    public String getBiosVersion();

    /**
     * Set bios version
     * 
     * @param biosVersion
     */
    public void setBiosVersion(String biosVersion);

    /**
     * Get firmware version
     * 
     * @return
     */
    @XmlElement(name = "firmwareVersion")
    public String getFirmwareVersion();

    /**
     * Set firmware version
     * 
     * @param firmwareVersion
     */
    public void setFirmwareVersion(String firmwareVersion);

    /**
     * Get os version
     * 
     * @return
     */
    @XmlElement(name = "osVersion")
    public String getOsVersion();

    /**
     * Set os version
     * 
     * @param osVersion
     */
    public void setOsVersion(String osVersion);

    /**
     * Get jvm version
     * 
     * @return
     */
    @XmlElement(name = "jvmVersion")
    public String getJvmVersion();

    /**
     * Set jvm version
     * 
     * @param jvmVersion
     */
    public void setJvmVersion(String jvmVersion);

    /**
     * Get osgi framework version
     * 
     * @return
     */
    @XmlElement(name = "osgiFrameworkVersion")
    public String getOsgiFrameworkVersion();

    /**
     * Set osgi framework version
     * 
     * @param osgiFrameworkVersion
     */
    public void setOsgiFrameworkVersion(String osgiFrameworkVersion);

    /**
     * Get application framework version
     * 
     * @return
     */
    @XmlElement(name = "applicationFrameworkVersion")
    public String getApplicationFrameworkVersion();

    /**
     * Set application framework version
     * 
     * @param appFrameworkVersion
     */
    public void setApplicationFrameworkVersion(String appFrameworkVersion);

    /**
     * Get application identifiers
     * 
     * @return
     */
    @XmlElement(name = "applicationIdentifiers")
    public String getApplicationIdentifiers();

    /**
     * Set application identifiers
     * 
     * @param applicationIdentifiers
     */
    public void setApplicationIdentifiers(String applicationIdentifiers);

    /**
     * Get accept encoding flag
     * 
     * @return
     */
    @XmlElement(name = "acceptEncoding")
    public String getAcceptEncoding();

    /**
     * Set accept encoding flag
     * 
     * @param acceptEncoding
     */
    public void setAcceptEncoding(String acceptEncoding);

    /**
     * Get custom attribute 1
     * 
     * @return
     */
    @XmlElement(name = "customAttribute1")
    public String getCustomAttribute1();

    /**
     * Set custom attribute 1
     * 
     * @param customAttribute1
     */
    public void setCustomAttribute1(String customAttribute1);

    /**
     * Get custom attribute 2
     * 
     * @return
     */
    @XmlElement(name = "customAttribute2")
    public String getCustomAttribute2();

    /**
     * Set custom attribute 2
     * 
     * @param customAttribute2
     */
    public void setCustomAttribute2(String customAttribute2);

    /**
     * Get custom attribute 3
     * 
     * @return
     */
    @XmlElement(name = "customAttribute3")
    public String getCustomAttribute3();

    /**
     * Set custom attribute 3
     * 
     * @param customAttribute3
     */
    public void setCustomAttribute3(String customAttribute3);

    /**
     * Get custom attribute 4
     * 
     * @return
     */
    @XmlElement(name = "customAttribute4")
    public String getCustomAttribute4();

    /**
     * Set custom attribute 4
     * 
     * @param customAttribute4
     */
    public void setCustomAttribute4(String customAttribute4);

    /**
     * Get custom attribute 5
     * 
     * @return
     */
    @XmlElement(name = "customAttribute5")
    public String getCustomAttribute5();

    /**
     * Set custom attribute 5
     * 
     * @param customAttribute5
     */
    public void setCustomAttribute5(String customAttribute5);

    /**
     * Get credentials mode.<br>
     * The device credential mode sets a security level for the devices, setting a specific user connection policy between the available policies.
     * 
     * @return
     */
    @XmlElement(name = "deviceCredentialsMode")
    public DeviceCredentialsMode getCredentialsMode();

    /**
     * Set credentials mode.<br>
     * The device credential mode sets a security level for the devices, setting a specific user connection policy between the available policies.
     * 
     * @param credentialsMode
     */
    public void setCredentialsMode(DeviceCredentialsMode credentialsMode);

    /**
     * Get preferred user identifier.<br>
     * Set the preferred user identifier that can connect to this device.
     * 
     * @return
     */
    @XmlElement(name = "preferredUserId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getPreferredUserId();

    /**
     * Set preferred user identifier.<br>
     * Set the preferred user identifier that can connect to this device.
     * 
     * @param deviceUserId
     */
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
