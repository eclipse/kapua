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
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.message.device.lifecycle.KapuaAppsPayload;
import org.eclipse.kapua.message.internal.KapuaPayloadImpl;

/**
 * Kapua data message payload object reference implementation.
 * 
 * @since 1.0
 *
 */
public class KapuaAppsPayloadImpl extends KapuaPayloadImpl implements KapuaAppsPayload {

    private String uptime;
    private String displayName;
    private String modelName;
    private String modelId;
    private String partNumber;
    private String serialNumber;
    private String firmware;
    private String firmwareVersion;
    private String bios;
    private String biosVersion;
    private String os;
    private String osVersion;
    private String jvm;
    private String jvmVersion;
    private String jvmProfile;
    private String containerFramework;
    private String containerFrameworkVersion;
    private String applicationFramework;
    private String applicationFrameworkVersion;
    private String connectionInterface;
    private String connectionIp;
    private String acceptEncoding;
    private String applicationIdentifiers;
    private String availableProcessors;
    private String totalMemory;
    private String osArch;
    private String modemImei;
    private String modemImsi;
    private String modemIccid;

    /**
     * Constructor
     * 
     * @param uptime
     * @param displayName
     * @param modelName
     * @param modelId
     * @param partNumber
     * @param serialNumber
     * @param firmware
     * @param firmwareVersion
     * @param bios
     * @param biosVersion
     * @param os
     * @param osVersion
     * @param jvm
     * @param jvmVersion
     * @param jvmProfile
     * @param containerFramework
     * @param containerFrameworkVersion
     * @param applicationFramework
     * @param applicationFrameworkVersion
     * @param connectionInterface
     * @param connectionIp
     * @param acceptEncoding
     * @param applicationIdentifiers
     * @param availableProcessors
     * @param totalMemory
     * @param osArch
     * @param modemImei
     * @param modemImsi
     * @param modemIccid
     */
    public KapuaAppsPayloadImpl(String uptime,
            String displayName,
            String modelName,
            String modelId,
            String partNumber,
            String serialNumber,
            String firmware,
            String firmwareVersion,
            String bios,
            String biosVersion,
            String os,
            String osVersion,
            String jvm,
            String jvmVersion,
            String jvmProfile,
            String containerFramework,
            String containerFrameworkVersion,
            String applicationFramework,
            String applicationFrameworkVersion,
            String connectionInterface,
            String connectionIp,
            String acceptEncoding,
            String applicationIdentifiers,
            String availableProcessors,
            String totalMemory,
            String osArch,
            String modemImei,
            String modemImsi,
            String modemIccid) {
        this.uptime = uptime;
        this.displayName = displayName;
        this.modelName = modelName;
        this.modelId = modelId;
        this.partNumber = partNumber;
        this.serialNumber = serialNumber;
        this.firmware = firmware;
        this.firmwareVersion = firmwareVersion;
        this.bios = bios;
        this.biosVersion = biosVersion;
        this.os = os;
        this.osVersion = osVersion;
        this.jvm = jvm;
        this.jvmVersion = jvmVersion;
        this.jvmProfile = jvmProfile;
        this.containerFramework = containerFramework;
        this.containerFrameworkVersion = containerFrameworkVersion;
        this.applicationFramework = applicationFramework;
        this.applicationFrameworkVersion = applicationFrameworkVersion;
        this.connectionInterface = connectionInterface;
        this.connectionIp = connectionIp;
        this.acceptEncoding = acceptEncoding;
        this.applicationIdentifiers = applicationIdentifiers;
        this.availableProcessors = availableProcessors;
        this.totalMemory = totalMemory;
        this.osArch = osArch;
        this.modemImei = modemImei;
        this.modemImsi = modemImsi;
        this.modemIccid = modemIccid;
    }

    @Override
    public String toDisplayString() {
        return new StringBuilder().append("[ getUptime()=").append(getUptime())
                .append(", getDisplayName()=").append(getDisplayName())
                .append(", getModelName()=").append(getModelName())
                .append(", getModelId()=").append(getModelId())
                .append(", getPartNumber()=").append(getPartNumber())
                .append(", getSerialNumber()=").append(getSerialNumber())
                .append(", getFirmwareVersion()=").append(getFirmwareVersion())
                .append(", getBiosVersion()=").append(getBiosVersion())
                .append(", getOs()=").append(getOs())
                .append(", getOsVersion()=").append(getOsVersion())
                .append(", getJvmName()=").append(getJvm())
                .append(", getJvmVersion()=").append(getJvmVersion())
                .append(", getJvmProfile()=").append(getJvmProfile())
                .append(", getOsgiFramework()=").append(getContainerFramework())
                .append(", getOsgiFrameworkVersion()=").append(getContainerFrameworkVersion())
                .append(", getEsfKuraVersion()=").append(getApplicationFrameworkVersion())
                .append(", getConnectionInterface()=").append(getConnectionInterface())
                .append(", getConnectionIp()=").append(getConnectionIp())
                .append(", getAcceptEncoding()=").append(getAcceptEncoding())
                .append(", getApplicationIdentifiers()=").append(getApplicationIdentifiers())
                .append(", getAvailableProcessors()=").append(getAvailableProcessors())
                .append(", getTotalMemory()=").append(getTotalMemory())
                .append(", getOsArch()=").append(getOsArch())
                .append(", getModemImei()=").append(getModemImei())
                .append(", getModemImsi()=").append(getModemImsi())
                .append(", getModemIccid()=").append(getModemIccid())
                .append("]")
                .toString();
    }

    @Override
    public String getUptime() {
        return uptime;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getModelName() {
        return modelName;
    }

    @Override
    public String getModelId() {
        return modelId;
    }

    @Override
    public String getPartNumber() {
        return partNumber;
    }

    @Override
    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public String getFirmware() {
        return firmware;
    }

    @Override
    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    @Override
    public String getBios() {
        return bios;
    }

    @Override
    public String getBiosVersion() {
        return biosVersion;
    }

    @Override
    public String getOs() {
        return os;
    }

    @Override
    public String getOsVersion() {
        return osVersion;
    }

    @Override
    public String getJvm() {
        return jvm;
    }

    @Override
    public String getJvmVersion() {
        return jvmVersion;
    }

    @Override
    public String getJvmProfile() {
        return jvmProfile;
    }

    @Override
    public String getContainerFramework() {
        return containerFramework;
    }

    @Override
    public String getContainerFrameworkVersion() {
        return containerFrameworkVersion;
    }

    @Override
    public String getApplicationFramework() {
        return applicationFramework;
    }

    @Override
    public String getApplicationFrameworkVersion() {
        return applicationFrameworkVersion;
    }

    @Override
    public String getConnectionInterface() {
        return connectionInterface;
    }

    @Override
    public String getConnectionIp() {
        return connectionIp;
    }

    @Override
    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    @Override
    public String getApplicationIdentifiers() {
        return applicationIdentifiers;
    }

    @Override
    public String getAvailableProcessors() {
        return availableProcessors;
    }

    @Override
    public String getTotalMemory() {
        return totalMemory;
    }

    @Override
    public String getOsArch() {
        return osArch;
    }

    @Override
    public String getModemImei() {
        return modemImei;
    }

    @Override
    public String getModemImsi() {
        return modemImsi;
    }

    @Override
    public String getModemIccid() {
        return modemIccid;
    }

}
