/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Set;

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
        "modelName",
        "imei",
        "imsi",
        "iccid",
        "biosVersion",
        "firmwareVersion",
        "osVersion",
        "jvmVersion",
        "osgiFrameworkVersion",
        "applicationFrameworkVersion",
        "connectionInterface",
        "connectionIp",
        "applicationIdentifiers",
        "acceptEncoding",
        "customAttribute1",
        "customAttribute2",
        "customAttribute3",
        "customAttribute4",
        "customAttribute5",
        "tagIds"
}, factoryClass = DeviceXmlRegistry.class, factoryMethod = "newDevice")
public interface Device extends KapuaUpdatableEntity {

    String TYPE = "device";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Sets the set of Tag id of this entity.
     *
     * @param tagIds The set Tag id to assign.
     * @since 1.0.0
     */
    void setTagIds(Set<KapuaId> tagIds);

    /**
     * Gets the set of Tag id assigned to this entity.
     *
     * @return The set Tag id assigned to this entity.
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "tagIds")
    @XmlElement(name = "tagId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    Set<KapuaId> getTagIds();

    /**
     * Get the group identifier
     *
     * @return
     */
    @XmlElement(name = "groupId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getGroupId();

    /**
     * Set the group identifier
     *
     * @param groupId
     */
    void setGroupId(KapuaId groupId);

    /**
     * Get the client identifier
     *
     * @return
     */
    @XmlElement(name = "clientId")
    String getClientId();

    /**
     * Set the client identifier
     *
     * @param clientId
     */
    void setClientId(String clientId);

    /**
     * Get the connection identifier
     *
     * @return
     */
    @XmlElement(name = "connectionId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getConnectionId();

    /**
     * Set the connection identifier
     *
     * @param connectionId
     */
    void setConnectionId(KapuaId connectionId);

    /**
     * Gets the {@link DeviceConnection}
     *
     * @return
     */
    @XmlElement(name = "connection")
    <C extends DeviceConnection> C getConnection();

    /**
     * Get the connection status
     *
     * @return
     */
    @XmlElement(name = "status")
    DeviceStatus getStatus();

    /**
     * Set the connection status
     *
     * @param status
     */
    void setStatus(DeviceStatus status);

    /**
     * Get the display name
     *
     * @return
     */
    @XmlElement(name = "displayName")
    String getDisplayName();

    /**
     * Set the display name
     *
     * @param diplayName
     */
    void setDisplayName(String diplayName);

    /**
     * Gets the last {@link DeviceEvent} {@link KapuaId}.
     *
     * @return
     */
    @XmlElement(name = "lastEventId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getLastEventId();

    /**
     * Set the last {@link DeviceEvent} {@link KapuaId}.
     *
     * @param lastEventId
     */
    void setLastEventId(KapuaId lastEventId);

    /**
     * Get the last {@link DeviceEvent} for this {@link Device}.
     *
     * @return The last {@link DeviceEvent} for this {@link Device}.
     */
    @XmlElement(name = "lastEvent")
    <E extends DeviceEvent> E getLastEvent();

    /**
     * Get the serial number
     *
     * @return
     */
    @XmlElement(name = "serialNumber")
    String getSerialNumber();

    /**
     * Set the serial number
     *
     * @param serialNumber
     */
    void setSerialNumber(String serialNumber);

    /**
     * Get the model identifier
     *
     * @return
     */
    @XmlElement(name = "modelId")
    String getModelId();

    /**
     * Set the model identifier
     *
     * @param modelId
     */
    void setModelId(String modelId);

    /**
     * Get the model name
     *
     * @return
     */
    @XmlElement(name = "modelName")
    String getModelName();

    /**
     * Set the model name
     *
     * @param modelName
     */
    void setModelName(String modelName);

    /**
     * Get the imei
     *
     * @return
     */
    @XmlElement(name = "imei")
    String getImei();

    /**
     * Set the imei
     *
     * @param imei
     */
    void setImei(String imei);

    /**
     * Get the imsi
     *
     * @return
     */
    @XmlElement(name = "imsi")
    String getImsi();

    /**
     * Set the imsi
     *
     * @param imsi
     */
    void setImsi(String imsi);

    /**
     * Get the iccid
     *
     * @return
     */
    @XmlElement(name = "iccid")
    String getIccid();

    /**
     * Set the iccid
     *
     * @param iccid
     */
    void setIccid(String iccid);

    /**
     * Get bios version
     *
     * @return
     */
    @XmlElement(name = "biosVersion")
    String getBiosVersion();

    /**
     * Set bios version
     *
     * @param biosVersion
     */
    void setBiosVersion(String biosVersion);

    /**
     * Get firmware version
     *
     * @return
     */
    @XmlElement(name = "firmwareVersion")
    String getFirmwareVersion();

    /**
     * Set firmware version
     *
     * @param firmwareVersion
     */
    void setFirmwareVersion(String firmwareVersion);

    /**
     * Get os version
     *
     * @return
     */
    @XmlElement(name = "osVersion")
    String getOsVersion();

    /**
     * Set os version
     *
     * @param osVersion
     */
    void setOsVersion(String osVersion);

    /**
     * Get jvm version
     *
     * @return
     */
    @XmlElement(name = "jvmVersion")
    String getJvmVersion();

    /**
     * Set jvm version
     *
     * @param jvmVersion
     */
    void setJvmVersion(String jvmVersion);

    /**
     * Get osgi framework version
     *
     * @return
     */
    @XmlElement(name = "osgiFrameworkVersion")
    String getOsgiFrameworkVersion();

    /**
     * Set osgi framework version
     *
     * @param osgiFrameworkVersion
     */
    void setOsgiFrameworkVersion(String osgiFrameworkVersion);

    /**
     * Get application framework version
     *
     * @return
     */
    @XmlElement(name = "applicationFrameworkVersion")
    String getApplicationFrameworkVersion();

    /**
     * Set application framework version
     *
     * @param appFrameworkVersion
     */
    void setApplicationFrameworkVersion(String appFrameworkVersion);

    /**
     * Gets the device's primary connection interface name
     *
     * @return The device's primary connection interface name
     */
    @XmlElement(name = "connectionInterface")
    String getConnectionInterface();

    /**
     * Sets the device's primary connection interface name
     *
     * @param connectionInterface The device's primary connection interface name
     */
    void setConnectionInterface(String connectionInterface);

    /**
     * Gets the device's primary connection interface IP address
     *
     * @return The device's primary connection interface IP address
     */
    @XmlElement(name = "connectionIp")
    String getConnectionIp();

    /**
     * Sets the device's primary connection interface IP address
     *
     * @param connectionIp The device's primary connection interface IP address
     */
    void setConnectionIp(String connectionIp);

    /**
     * Get application identifiers
     *
     * @return
     */
    @XmlElement(name = "applicationIdentifiers")
    String getApplicationIdentifiers();

    /**
     * Set application identifiers
     *
     * @param applicationIdentifiers
     */
    void setApplicationIdentifiers(String applicationIdentifiers);

    /**
     * Get accept encoding flag
     *
     * @return
     */
    @XmlElement(name = "acceptEncoding")
    String getAcceptEncoding();

    /**
     * Set accept encoding flag
     *
     * @param acceptEncoding
     */
    void setAcceptEncoding(String acceptEncoding);

    /**
     * Get custom attribute 1
     *
     * @return
     */
    @XmlElement(name = "customAttribute1")
    String getCustomAttribute1();

    /**
     * Set custom attribute 1
     *
     * @param customAttribute1
     */
    void setCustomAttribute1(String customAttribute1);

    /**
     * Get custom attribute 2
     *
     * @return
     */
    @XmlElement(name = "customAttribute2")
    String getCustomAttribute2();

    /**
     * Set custom attribute 2
     *
     * @param customAttribute2
     */
    void setCustomAttribute2(String customAttribute2);

    /**
     * Get custom attribute 3
     *
     * @return
     */
    @XmlElement(name = "customAttribute3")
    String getCustomAttribute3();

    /**
     * Set custom attribute 3
     *
     * @param customAttribute3
     */
    void setCustomAttribute3(String customAttribute3);

    /**
     * Get custom attribute 4
     *
     * @return
     */
    @XmlElement(name = "customAttribute4")
    String getCustomAttribute4();

    /**
     * Set custom attribute 4
     *
     * @param customAttribute4
     */
    void setCustomAttribute4(String customAttribute4);

    /**
     * Get custom attribute 5
     *
     * @return
     */
    @XmlElement(name = "customAttribute5")
    String getCustomAttribute5();

    /**
     * Set custom attribute 5
     *
     * @param customAttribute5
     */
    void setCustomAttribute5(String customAttribute5);
}
