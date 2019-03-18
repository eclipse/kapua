/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayload;

/**
 * {@link KapuaBirthPayload} implementation.
 *
 * @since 1.0.0
 */
public class KapuaBirthPayloadImpl extends AbstractLifecyclePayloadImpl implements KapuaBirthPayload {

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
     * Constructor.
     * <p>
     * Sets all available properties of the {@link KapuaBirthPayload} at once.
     *
     * @param uptime                      The uptime of the {@link KapuaBirthMessage}
     * @param displayName                 The display name of the {@link KapuaBirthMessage}
     * @param modelName                   The model name of the {@link KapuaBirthMessage}
     * @param modelId                     The model id of the {@link KapuaBirthMessage}
     * @param partNumber                  The part number of the {@link KapuaBirthMessage}
     * @param serialNumber                The serial of the {@link KapuaBirthMessage}
     * @param firmware                    The firmware name of the {@link KapuaBirthMessage}
     * @param firmwareVersion             The firmware name of the {@link KapuaBirthMessage}
     * @param bios                        The bios name of the {@link KapuaBirthMessage}
     * @param biosVersion                 The bios version of the {@link KapuaBirthMessage}
     * @param os                          The os name of the {@link KapuaBirthMessage}
     * @param osVersion                   The os version of the {@link KapuaBirthMessage}
     * @param jvm                         The JVM name of the {@link KapuaBirthMessage}
     * @param jvmVersion                  The JVM version of the {@link KapuaBirthMessage}
     * @param jvmProfile                  The JVM profile of the {@link KapuaBirthMessage}
     * @param containerFramework          The container framework name of the {@link KapuaBirthMessage}
     * @param containerFrameworkVersion   The container framework version of the {@link KapuaBirthMessage}
     * @param applicationFramework        The application framework name of the {@link KapuaBirthMessage}
     * @param applicationFrameworkVersion The framework version of the {@link KapuaBirthMessage}
     * @param connectionInterface         The connection interface name of the {@link KapuaBirthMessage}
     * @param connectionIp                The connection interface IP of the {@link KapuaBirthMessage}
     * @param acceptEncoding              The encoding accepted of the {@link KapuaBirthMessage}
     * @param applicationIdentifiers      The application identifiers of the {@link KapuaBirthMessage}
     * @param availableProcessors         The available processors of the {@link KapuaBirthMessage}
     * @param totalMemory                 The total memory of the {@link KapuaBirthMessage}
     * @param osArch                      The os architecture of the {@link KapuaBirthMessage}
     * @param modemImei                   The modem IMEI of the {@link KapuaBirthMessage}
     * @param modemImsi                   The modem IMSI of the {@link KapuaBirthMessage}
     * @param modemIccid                  The modem ICCID of the {@link KapuaBirthMessage}
     * @since 1.0.0
     */
    public KapuaBirthPayloadImpl(String uptime,
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
