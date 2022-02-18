/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayloadAttibutes;

/**
 * {@link KapuaBirthPayload} implementation.
 *
 * @since 1.0.0
 */
public class KapuaBirthPayloadImpl extends AbstractLifecyclePayloadImpl implements KapuaBirthPayload {

    private static final long serialVersionUID = 304433271740125817L;

    /**
     * Constructor.
     *
     * @since 1.1.0
     */
    public KapuaBirthPayloadImpl() {
        super();
    }

    /**
     * Constructor.
     * <p>
     * Sets all available properties of the {@link KapuaBirthPayload} at once.
     *
     * @param uptime                      The {@link KapuaBirthPayloadAttibutes#UPTIME} of the {@link KapuaBirthMessage}
     * @param displayName                 The {@link KapuaBirthPayloadAttibutes#DISPLAY_NAME} of the {@link KapuaBirthMessage}
     * @param modelName                   The {@link KapuaBirthPayloadAttibutes#MODEL_NAME} of the {@link KapuaBirthMessage}
     * @param modelId                     The {@link KapuaBirthPayloadAttibutes#MODEL_ID} of the {@link KapuaBirthMessage}
     * @param partNumber                  The {@link KapuaBirthPayloadAttibutes#PART_NUMBER} of the {@link KapuaBirthMessage}
     * @param serialNumber                The {@link KapuaBirthPayloadAttibutes#SERIAL_NUMBER} of the {@link KapuaBirthMessage}
     * @param firmware                    The {@link KapuaBirthPayloadAttibutes#FIRMWARE} of the {@link KapuaBirthMessage}
     * @param firmwareVersion             The {@link KapuaBirthPayloadAttibutes#FIRMWARE_VERSION} of the {@link KapuaBirthMessage}
     * @param bios                        The {@link KapuaBirthPayloadAttibutes#BIOS} of the {@link KapuaBirthMessage}
     * @param biosVersion                 The {@link KapuaBirthPayloadAttibutes#BIOS_VERSION} of the {@link KapuaBirthMessage}
     * @param os                          The {@link KapuaBirthPayloadAttibutes#OS} of the {@link KapuaBirthMessage}
     * @param osVersion                   The {@link KapuaBirthPayloadAttibutes#OS_VERSION} of the {@link KapuaBirthMessage}
     * @param jvm                         The {@link KapuaBirthPayloadAttibutes#JVM} of the {@link KapuaBirthMessage}
     * @param jvmVersion                  The {@link KapuaBirthPayloadAttibutes#JVM_VERSION} of the {@link KapuaBirthMessage}
     * @param jvmProfile                  The {@link KapuaBirthPayloadAttibutes#JVM_PROFILE} of the {@link KapuaBirthMessage}
     * @param containerFramework          The {@link KapuaBirthPayloadAttibutes#CONTAINER_FRAMEWORK} of the {@link KapuaBirthMessage}
     * @param containerFrameworkVersion   The {@link KapuaBirthPayloadAttibutes#CONTAINER_FRAMEWORK_VERSION} of the {@link KapuaBirthMessage}
     * @param applicationFramework        The {@link KapuaBirthPayloadAttibutes#APPLICATION_FRAMEWORK} of the {@link KapuaBirthMessage}
     * @param applicationFrameworkVersion The {@link KapuaBirthPayloadAttibutes#APPLICATION_FRAMEWORK_VERSION} of the {@link KapuaBirthMessage}
     * @param connectionInterface         The {@link KapuaBirthPayloadAttibutes#CONNECTION_INTERFACE} of the {@link KapuaBirthMessage}
     * @param connectionIp                The {@link KapuaBirthPayloadAttibutes#CONNECTION_IP} of the {@link KapuaBirthMessage}
     * @param acceptEncoding              The {@link KapuaBirthPayloadAttibutes#ACCEPT_ENCODING} of the {@link KapuaBirthMessage}
     * @param applicationIdentifiers      The {@link KapuaBirthPayloadAttibutes#APPLICATION_IDENTIFIERS} of the {@link KapuaBirthMessage}
     * @param availableProcessors         The {@link KapuaBirthPayloadAttibutes#AVAILABLE_PROCESSORS} of the {@link KapuaBirthMessage}
     * @param totalMemory                 The {@link KapuaBirthPayloadAttibutes#TOTAL_MEMORY} of the {@link KapuaBirthMessage}
     * @param osArch                      The {@link KapuaBirthPayloadAttibutes#OS_ARCH} of the {@link KapuaBirthMessage}
     * @param modemImei                   The {@link KapuaBirthPayloadAttibutes#MODEM_IMEI} of the {@link KapuaBirthMessage}
     * @param modemImsi                   The {@link KapuaBirthPayloadAttibutes#MODEM_IMSI} of the {@link KapuaBirthMessage}
     * @param modemIccid                  The {@link KapuaBirthPayloadAttibutes#MODEM_ICCID} of the {@link KapuaBirthMessage}
     * @param extendedProperties          The {@link KapuaBirthPayloadAttibutes#EXTENDED_PROPERTIES} of the {@link KapuaBirthMessage}
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
                                 String modemIccid,
                                 String extendedProperties) {

        setUptime(uptime);
        setDisplayName(displayName);
        setModelName(modelName);
        setModelId(modelId);
        setPartNumber(partNumber);
        setSerialNumber(serialNumber);
        setFirmware(firmware);
        setFirmwareVersion(firmwareVersion);
        setBios(bios);
        setBiosVersion(biosVersion);
        setOs(os);
        setOsVersion(osVersion);
        setJvm(jvm);
        setJvmVersion(jvmVersion);
        setJvmProfile(jvmProfile);
        setContainerFramework(containerFramework);
        setContainerFrameworkVersion(containerFrameworkVersion);
        setApplicationFramework(applicationFramework);
        setApplicationFrameworkVersion(applicationFrameworkVersion);
        setConnectionInterface(connectionInterface);
        setConnectionIp(connectionIp);
        setAcceptEncoding(acceptEncoding);
        setApplicationIdentifiers(applicationIdentifiers);
        setAvailableProcessors(availableProcessors);
        setTotalMemory(totalMemory);
        setOsArch(osArch);
        setModemImei(modemImei);
        setModemImsi(modemImsi);
        setModemIccid(modemIccid);
        setExtendedProperties(extendedProperties);
    }

    @Override
    public String getUptime() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.UPTIME);
    }

    @Override
    public void setUptime(String uptime) {
        getMetrics().put(KapuaBirthPayloadAttibutes.UPTIME, uptime);
    }

    @Override
    public String getDisplayName() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.DISPLAY_NAME);
    }

    @Override
    public void setDisplayName(String displayName) {
        getMetrics().put(KapuaBirthPayloadAttibutes.DISPLAY_NAME, displayName);
    }

    @Override
    public String getModelName() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.MODEL_NAME);
    }

    @Override
    public void setModelName(String moodelyName) {
        getMetrics().put(KapuaBirthPayloadAttibutes.MODEL_NAME, moodelyName);
    }

    @Override
    public String getModelId() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.MODEL_ID);
    }

    @Override
    public void setModelId(String modelId) {
        getMetrics().put(KapuaBirthPayloadAttibutes.MODEL_ID, modelId);
    }

    @Override
    public String getPartNumber() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.PART_NUMBER);
    }

    @Override
    public void setPartNumber(String partNumber) {
        getMetrics().put(KapuaBirthPayloadAttibutes.PART_NUMBER, partNumber);
    }

    @Override
    public String getSerialNumber() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.SERIAL_NUMBER);
    }

    @Override
    public void setSerialNumber(String serialNumber) {
        getMetrics().put(KapuaBirthPayloadAttibutes.SERIAL_NUMBER, serialNumber);
    }

    @Override
    public String getFirmware() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.FIRMWARE);
    }

    @Override
    public void setFirmware(String firmware) {
        getMetrics().put(KapuaBirthPayloadAttibutes.FIRMWARE, firmware);
    }

    @Override
    public String getFirmwareVersion() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.FIRMWARE_VERSION);
    }

    @Override
    public void setFirmwareVersion(String firmwareVersion) {
        getMetrics().put(KapuaBirthPayloadAttibutes.FIRMWARE_VERSION, firmwareVersion);
    }

    @Override
    public String getBios() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.BIOS);
    }

    @Override
    public void setBios(String bios) {
        getMetrics().put(KapuaBirthPayloadAttibutes.BIOS, bios);
    }

    @Override
    public String getBiosVersion() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.BIOS_VERSION);
    }

    @Override
    public void setBiosVersion(String biosVersion) {
        getMetrics().put(KapuaBirthPayloadAttibutes.BIOS_VERSION, biosVersion);
    }

    @Override
    public String getOs() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.OS);
    }

    @Override
    public void setOs(String os) {
        getMetrics().put(KapuaBirthPayloadAttibutes.OS, os);
    }

    @Override
    public String getOsArch() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.OS_ARCH);
    }

    @Override
    public void setOsArch(String osArch) {
        getMetrics().put(KapuaBirthPayloadAttibutes.OS_ARCH, osArch);
    }

    @Override
    public String getOsVersion() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.OS_VERSION);
    }

    @Override
    public void setOsVersion(String osVersion) {
        getMetrics().put(KapuaBirthPayloadAttibutes.OS_VERSION, osVersion);
    }

    @Override
    public String getJvm() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.JVM);
    }

    @Override
    public void setJvm(String jvm) {
        getMetrics().put(KapuaBirthPayloadAttibutes.JVM, jvm);
    }

    @Override
    public String getJvmVersion() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.JVM_VERSION);
    }

    @Override
    public void setJvmVersion(String jvmVersion) {
        getMetrics().put(KapuaBirthPayloadAttibutes.JVM_VERSION, jvmVersion);
    }

    @Override
    public String getJvmProfile() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.JVM_PROFILE);
    }

    @Override
    public void setJvmProfile(String jvmProfile) {
        getMetrics().put(KapuaBirthPayloadAttibutes.JVM_PROFILE, jvmProfile);
    }

    @Override
    public String getContainerFramework() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.CONTAINER_FRAMEWORK);
    }

    @Override
    public void setContainerFramework(String containerFramework) {
        getMetrics().put(KapuaBirthPayloadAttibutes.CONTAINER_FRAMEWORK, containerFramework);
    }

    @Override
    public String getContainerFrameworkVersion() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.CONTAINER_FRAMEWORK_VERSION);
    }

    @Override
    public void setContainerFrameworkVersion(String containerFrameworkVersion) {
        getMetrics().put(KapuaBirthPayloadAttibutes.CONTAINER_FRAMEWORK_VERSION, containerFrameworkVersion);
    }

    @Override
    public String getApplicationFramework() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.APPLICATION_FRAMEWORK);
    }

    @Override
    public void setApplicationFramework(String applicationFramework) {
        getMetrics().put(KapuaBirthPayloadAttibutes.APPLICATION_FRAMEWORK, applicationFramework);
    }

    @Override
    public String getApplicationFrameworkVersion() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.APPLICATION_FRAMEWORK_VERSION);
    }

    @Override
    public void setApplicationFrameworkVersion(String applicationFrameworkVersion) {
        getMetrics().put(KapuaBirthPayloadAttibutes.APPLICATION_FRAMEWORK_VERSION, applicationFrameworkVersion);
    }

    @Override
    public String getConnectionInterface() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.CONNECTION_INTERFACE);
    }

    @Override
    public void setConnectionInterface(String connectionInterface) {
        getMetrics().put(KapuaBirthPayloadAttibutes.CONNECTION_INTERFACE, connectionInterface);
    }

    @Override
    public String getConnectionIp() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.CONNECTION_IP);
    }

    @Override
    public void setConnectionIp(String connectionIp) {
        getMetrics().put(KapuaBirthPayloadAttibutes.CONNECTION_IP, connectionIp);
    }

    @Override
    public String getAcceptEncoding() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.ACCEPT_ENCODING);
    }

    @Override
    public void setAcceptEncoding(String acceptEncoding) {
        getMetrics().put(KapuaBirthPayloadAttibutes.ACCEPT_ENCODING, acceptEncoding);
    }

    @Override
    public String getApplicationIdentifiers() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.APPLICATION_IDENTIFIERS);
    }

    @Override
    public void setApplicationIdentifiers(String applicationIdentifiers) {
        getMetrics().put(KapuaBirthPayloadAttibutes.APPLICATION_IDENTIFIERS, applicationIdentifiers);
    }

    @Override
    public String getAvailableProcessors() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.AVAILABLE_PROCESSORS);
    }

    @Override
    public void setAvailableProcessors(String availableProcessors) {
        getMetrics().put(KapuaBirthPayloadAttibutes.AVAILABLE_PROCESSORS, availableProcessors);
    }

    @Override
    public String getTotalMemory() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.TOTAL_MEMORY);
    }

    @Override
    public void setTotalMemory(String totalMemory) {
        getMetrics().put(KapuaBirthPayloadAttibutes.TOTAL_MEMORY, totalMemory);
    }

    @Override
    public String getModemImei() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.MODEM_IMEI);
    }

    @Override
    public void setModemImei(String modemImei) {
        getMetrics().put(KapuaBirthPayloadAttibutes.MODEM_IMEI, modemImei);
    }

    @Override
    public String getModemImsi() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.MODEM_IMSI);
    }

    @Override
    public void setModemImsi(String modemImsi) {
        getMetrics().put(KapuaBirthPayloadAttibutes.MODEM_IMSI, modemImsi);
    }

    @Override
    public String getModemIccid() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.MODEM_ICCID);
    }

    @Override
    public void setModemIccid(String modemIccid) {
        getMetrics().put(KapuaBirthPayloadAttibutes.MODEM_ICCID, modemIccid);
    }

    @Override
    public String getExtendedProperties() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.EXTENDED_PROPERTIES);
    }

    @Override
    public void setExtendedProperties(String extendedProperties) {
        getMetrics().put(KapuaBirthPayloadAttibutes.EXTENDED_PROPERTIES, extendedProperties);
    }
}
