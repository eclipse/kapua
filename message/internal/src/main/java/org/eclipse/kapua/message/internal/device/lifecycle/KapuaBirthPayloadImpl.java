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
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayloadAttibutes;

/**
 * {@link KapuaBirthPayload} implementation.
 *
 * @since 1.0.0
 */
public class KapuaBirthPayloadImpl extends AbstractLifecyclePayloadImpl implements KapuaBirthPayload {

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
        getMetrics();

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
    }

    @Override
    public String getUptime() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.UPTIME);
    }

    private void setUptime(String uptime) {
        getMetrics().put(KapuaBirthPayloadAttibutes.UPTIME, uptime);
    }

    @Override
    public String getDisplayName() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.DISPLAY_NAME);
    }

    private void setDisplayName(String displayName) {
        getMetrics().put(KapuaBirthPayloadAttibutes.DISPLAY_NAME, displayName);
    }

    @Override
    public String getModelName() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.MODEL_NAME);
    }

    private void setModelName(String moodelyName) {
        getMetrics().put(KapuaBirthPayloadAttibutes.MODEL_NAME, moodelyName);
    }

    @Override
    public String getModelId() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.MODEL_ID);
    }

    private void setModelId(String modelId) {
        getMetrics().put(KapuaBirthPayloadAttibutes.MODEL_ID, modelId);
    }

    @Override
    public String getPartNumber() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.PART_NUMBER);
    }

    private void setPartNumber(String partNumber) {
        getMetrics().put(KapuaBirthPayloadAttibutes.PART_NUMBER, partNumber);
    }

    @Override
    public String getSerialNumber() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.SERIAL_NUMBER);
    }

    private void setSerialNumber(String serialNumber) {
        getMetrics().put(KapuaBirthPayloadAttibutes.SERIAL_NUMBER, serialNumber);
    }

    @Override
    public String getFirmware() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.FIRMWARE);
    }

    private void setFirmware(String firmware) {
        getMetrics().put(KapuaBirthPayloadAttibutes.FIRMWARE, firmware);
    }

    @Override
    public String getFirmwareVersion() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.FIRMWARE_VERSION);
    }

    private void setFirmwareVersion(String firmwareVersion) {
        getMetrics().put(KapuaBirthPayloadAttibutes.FIRMWARE_VERSION, firmwareVersion);
    }

    @Override
    public String getBios() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.BIOS);
    }

    private void setBios(String bios) {
        getMetrics().put(KapuaBirthPayloadAttibutes.BIOS, bios);
    }

    @Override
    public String getBiosVersion() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.BIOS_VERSION);
    }

    private void setBiosVersion(String biosVersion) {
        getMetrics().put(KapuaBirthPayloadAttibutes.BIOS_VERSION, biosVersion);
    }

    @Override
    public String getOs() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.OS);
    }

    private void setOs(String os) {
        getMetrics().put(KapuaBirthPayloadAttibutes.OS, os);
    }

    @Override
    public String getOsVersion() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.OS_VERSION);
    }

    private void setOsVersion(String osVersion) {
        getMetrics().put(KapuaBirthPayloadAttibutes.OS_VERSION, osVersion);
    }

    @Override
    public String getJvm() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.JVM);
    }

    private void setJvm(String jvm) {
        getMetrics().put(KapuaBirthPayloadAttibutes.JVM, jvm);
    }

    @Override
    public String getJvmVersion() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.JVM_VERSION);
    }

    private void setJvmVersion(String jvmVersion) {
        getMetrics().put(KapuaBirthPayloadAttibutes.JVM_VERSION, jvmVersion);
    }

    @Override
    public String getJvmProfile() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.JVM_PROFILE);
    }

    private void setJvmProfile(String jvmProfile) {
        getMetrics().put(KapuaBirthPayloadAttibutes.JVM_PROFILE, jvmProfile);
    }

    @Override
    public String getContainerFramework() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.CONTAINER_FRAMEWORK);
    }

    private void setContainerFramework(String containerFramework) {
        getMetrics().put(KapuaBirthPayloadAttibutes.CONTAINER_FRAMEWORK, containerFramework);
    }

    @Override
    public String getContainerFrameworkVersion() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.CONTAINER_FRAMEWORK_VERSION);
    }

    private void setContainerFrameworkVersion(String containerFrameworkVersion) {
        getMetrics().put(KapuaBirthPayloadAttibutes.CONTAINER_FRAMEWORK_VERSION, containerFrameworkVersion);
    }

    @Override
    public String getApplicationFramework() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.APPLICATION_FRAMEWORK);
    }

    private void setApplicationFramework(String applicationFramework) {
        getMetrics().put(KapuaBirthPayloadAttibutes.APPLICATION_FRAMEWORK, applicationFramework);
    }

    @Override
    public String getApplicationFrameworkVersion() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.APPLICATION_FRAMEWORK_VERSION);
    }

    private void setApplicationFrameworkVersion(String applicationFrameworkVersion) {
        getMetrics().put(KapuaBirthPayloadAttibutes.APPLICATION_FRAMEWORK_VERSION, applicationFrameworkVersion);
    }

    @Override
    public String getConnectionInterface() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.CONNECTION_INTERFACE);
    }

    private void setConnectionInterface(String connectionInterface) {
        getMetrics().put(KapuaBirthPayloadAttibutes.CONNECTION_INTERFACE, connectionInterface);
    }

    @Override
    public String getConnectionIp() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.CONNECTION_IP);
    }

    private void setConnectionIp(String connectionIp) {
        getMetrics().put(KapuaBirthPayloadAttibutes.CONNECTION_IP, connectionIp);
    }

    @Override
    public String getAcceptEncoding() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.ACCEPT_ENCODING);
    }

    private void setAcceptEncoding(String acceptEncoding) {
        getMetrics().put(KapuaBirthPayloadAttibutes.ACCEPT_ENCODING, acceptEncoding);
    }

    @Override
    public String getApplicationIdentifiers() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.APPLICATION_IDENTIFIERS);
    }

    private void setApplicationIdentifiers(String applicationIdentifiers) {
        getMetrics().put(KapuaBirthPayloadAttibutes.APPLICATION_IDENTIFIERS, applicationIdentifiers);
    }

    @Override
    public String getAvailableProcessors() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.AVAILABLE_PROCESSORS);
    }

    private void setAvailableProcessors(String availableProcessors) {
        getMetrics().put(KapuaBirthPayloadAttibutes.AVAILABLE_PROCESSORS, availableProcessors);
    }

    @Override
    public String getTotalMemory() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.TOTAL_MEMORY);
    }

    private void setTotalMemory(String totalMemory) {
        getMetrics().put(KapuaBirthPayloadAttibutes.TOTAL_MEMORY, totalMemory);
    }

    @Override
    public String getOsArch() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.OS_ARCH);
    }

    private void setOsArch(String osArch) {
        getMetrics().put(KapuaBirthPayloadAttibutes.OS_ARCH, osArch);
    }

    @Override
    public String getModemImei() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.MODEM_IMEI);
    }

    private void setModemImei(String modemImei) {
        getMetrics().put(KapuaBirthPayloadAttibutes.MODEM_IMEI, modemImei);
    }

    @Override
    public String getModemImsi() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.MODEM_IMSI);
    }

    private void setModemImsi(String modemImsi) {
        getMetrics().put(KapuaBirthPayloadAttibutes.MODEM_IMSI, modemImsi);
    }

    @Override
    public String getModemIccid() {
        return (String) getMetrics().get(KapuaBirthPayloadAttibutes.MODEM_ICCID);
    }

    private void setModemIccid(String modemIccid) {
        getMetrics().put(KapuaBirthPayloadAttibutes.MODEM_ICCID, modemIccid);
    }

}
