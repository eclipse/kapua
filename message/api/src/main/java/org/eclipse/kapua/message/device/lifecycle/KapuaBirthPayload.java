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
package org.eclipse.kapua.message.device.lifecycle;

/**
 * {@link KapuaBirthPayload} definition.
 *
 * @since 1.0.0
 */
public interface KapuaBirthPayload extends KapuaLifecyclePayload {

    /**
     * Gets the uptime.
     *
     * @return The uptime.
     * @since 1.0.0
     */
    String getUptime();

    /**
     * Sets the uptime.
     *
     * @param uptime The uptime.
     * @since 1.1.0
     */
    void setUptime(String uptime);

    /**
     * Gets the display name.
     *
     * @return The display name.
     * @since 1.0.0
     */
    String getDisplayName();

    /**
     * Sets the display name.
     *
     * @param displayName The display name.
     * @since 1.1.0
     */
    void setDisplayName(String displayName);

    /**
     * Gets the model name.
     *
     * @return The model name.
     * @since 1.0.0
     */
    String getModelName();

    /**
     * Sets the model name.
     *
     * @param modelName The model name.
     * @since 1.1.0
     */
    void setModelName(String modelName);

    /**
     * Gets the model identifier.
     *
     * @return The model identifier.
     * @since 1.0.0
     */
    String getModelId();

    /**
     * Sets the model identifier.
     *
     * @param modelId The model identifier.
     * @since 1.1.0
     */
    void setModelId(String modelId);

    /**
     * Gets the part number.
     *
     * @return The part number.
     * @since 1.0.0
     */
    String getPartNumber();

    /**
     * Sets the part number.
     *
     * @param partNumber The part number.
     * @since 1.1.0
     */
    void setPartNumber(String partNumber);

    /**
     * Gets the serial number.
     *
     * @return The serial number.
     * @since 1.0.0
     */
    String getSerialNumber();

    /**
     * Sets the serial number.
     *
     * @param serialNumber The serial number.
     * @since 1.1.0
     */
    void setSerialNumber(String serialNumber);

    /**
     * Gets the firmware name.
     *
     * @return The firmware name.
     * @since 1.0.0
     */
    String getFirmware();

    /**
     * Sets the firmware name.
     *
     * @param firmware The firmware name.
     * @since 1.1.0
     */
    void setFirmware(String firmware);

    /**
     * Gets the firmware version.
     *
     * @return The firmware version.
     * @since 1.0.0
     */
    String getFirmwareVersion();

    /**
     * Sets the firmware version.
     *
     * @param firmwareVersion The firmware version.
     * @since 1.1.0
     */
    void setFirmwareVersion(String firmwareVersion);

    /**
     * Gets the bios name
     *
     * @return The bios name.
     * @since 1.0.0
     */
    String getBios();

    /**
     * Sets the bios name.
     *
     * @param bios The bios name.
     * @since 1.1.0
     */
    void setBios(String bios);

    /**
     * Gets the bios version.
     *
     * @return The bios version.
     * @since 1.0.0
     */
    String getBiosVersion();

    /**
     * Sets the bios version.
     *
     * @param biosVersion The bios version.
     * @since 1.1.0
     */
    void setBiosVersion(String biosVersion);

    /**
     * Gets the operating system name.
     *
     * @return The operating system name.
     * @since 1.0.0
     */
    String getOs();

    /**
     * Sets the operating system name.
     *
     * @param os The operating system name.
     * @since 1.1.0
     */
    void setOs(String os);

    /**
     * Gets the operating system architecture.
     *
     * @return The operating system architecture.
     * @since 1.0.0
     */
    String getOsArch();

    /**
     * Sets the operating system architecture.
     *
     * @param osArch The operating system architecture.
     * @since 1.1.0
     */
    void setOsArch(String osArch);

    /**
     * Gets the operating system version.
     *
     * @return The operating system version.
     * @since 1.0.0
     */
    String getOsVersion();

    /**
     * Sets the operating system version.
     *
     * @param osVersion The operating system version.
     * @since 1.0.0
     */
    void setOsVersion(String osVersion);

    /**
     * Gets the java virtual machine name/vendor.
     *
     * @return The java virtual machine name/vendor.
     * @since 1.0.0
     */
    String getJvm();

    /**
     * Sets the java virtual machine name/vendor.
     *
     * @param jvm The java virtual machine name/vendor.
     * @since 1.1.0
     */
    void setJvm(String jvm);

    /**
     * Gets the java virtual machine version.
     *
     * @return The java virtual machine version.
     * @since 1.0.0
     */
    String getJvmVersion();

    /**
     * Sets the java virtual machine version.
     *
     * @param jvmVersion The java virtual machine version.
     * @since 1.1.0
     */
    void setJvmVersion(String jvmVersion);

    /**
     * Gets the java virtual machine profile.
     *
     * @return The java virtual machine profile.
     * @since 1.0.0
     */
    String getJvmProfile();

    /**
     * Sets the java virtual machine profile.
     *
     * @param jvmProfile The java virtual machine profile.
     * @since 1.1.0
     */
    void setJvmProfile(String jvmProfile);

    /**
     * Gets the container framework name.
     *
     * @return The container framework name.
     * @since 1.0.0
     */
    String getContainerFramework();

    /**
     * Sets the container framework name.
     *
     * @param containerFramework The container framework name.
     * @since 1.1.0
     */
    void setContainerFramework(String containerFramework);

    /**
     * Gets the container framework version.
     *
     * @return The container framework version.
     * @since 1.0.0
     */
    String getContainerFrameworkVersion();

    /**
     * Sets the container framework version.
     *
     * @param containerFrameworkVersion The container framework version.
     * @since 1.1.0
     */
    void setContainerFrameworkVersion(String containerFrameworkVersion);

    /**
     * Gets the application framework name.
     *
     * @return The application framework name.
     * @since 1.0.0
     */
    String getApplicationFramework();

    /**
     * Sets the application framework name.
     *
     * @param applicationFramework The application framework name.
     * @since 1.1.0
     */
    void setApplicationFramework(String applicationFramework);

    /**
     * Gets the application framework version
     *
     * @return The application framework version
     * @since 1.0.0
     */
    String getApplicationFrameworkVersion();

    /**
     * Sets the application framework version
     *
     * @param applicationFrameworkVersion The application framework version
     * @since 1.1.0
     */
    void setApplicationFrameworkVersion(String applicationFrameworkVersion);

    /**
     * Gets the connection interface.
     *
     * @return The connection interface.
     * @since 1.0.0
     */
    String getConnectionInterface();

    /**
     * Sets the connection interface.
     *
     * @param connectionInterface The connection interface.
     * @since 1.1.0
     */
    void setConnectionInterface(String connectionInterface);

    /**
     * Gets the connection interface ip.
     *
     * @return The connection interface ip.
     * @since 1.0.0
     */
    String getConnectionIp();

    /**
     * Sets the connection interface ip.
     *
     * @param connectionIp The connection interface ip.
     * @since 1.1.0
     */
    void setConnectionIp(String connectionIp);

    /**
     * Gets the accepted encoding.
     *
     * @return The accepted encoding.
     * @since 1.0.0
     */
    String getAcceptEncoding();

    /**
     * Sets the accepted encoding.
     *
     * @param acceptEncoding The accepted encoding.
     * @since 1.1.0
     */
    void setAcceptEncoding(String acceptEncoding);

    /**
     * Gets the application identifiers.
     *
     * @return The application identifiers.
     * @since 1.0.0
     */
    String getApplicationIdentifiers();

    /**
     * Sets the application identifiers.
     *
     * @param applicationIdentifiers The application identifiers.
     * @since 1.1.0
     */
    void setApplicationIdentifiers(String applicationIdentifiers);

    /**
     * Gets the available processors.
     *
     * @return The available processors.
     * @since 1.0.0
     */
    String getAvailableProcessors();

    /**
     * Sets the available processors.
     *
     * @param availableProcessors The available processors.
     * @since 1.1.0
     */
    void setAvailableProcessors(String availableProcessors);

    /**
     * Gets the total memory.
     *
     * @return The total memory.
     * @since 1.0.0
     */
    String getTotalMemory();

    /**
     * Sets the total memory.
     *
     * @param totalMemory The total memory.
     * @since 1.1.0
     */
    void setTotalMemory(String totalMemory);

    /**
     * Gets the modem IMEI.
     *
     * @return The modem IMEI.
     * @since 1.0.0
     */
    String getModemImei();

    /**
     * Sets the modem IMEI.
     *
     * @param modemImei The modem IMEI.
     * @since 1.1.0
     */
    void setModemImei(String modemImei);

    /**
     * Gets the modem IMSI.
     *
     * @return The modem IMSI.
     * @since 1.0.0
     */
    String getModemImsi();

    /**
     * Sets the modem IMSI.
     *
     * @param modemImsi The modem IMSI.
     * @since 1.1.0
     */
    void setModemImsi(String modemImsi);

    /**
     * Gets the modem ICCID.
     *
     * @return The modem ICCID.
     * @since 1.0.0
     */
    String getModemIccid();

    /**
     * Sets the modem ICCID.
     *
     * @param modemIccid The modem ICCID.
     * @since 1.1.0
     */
    void setModemIccid(String modemIccid);

    /**
     * Gets the extended properties.
     *
     * @return The extended properties.
     * @since 1.5.0
     */
    String getExtendedProperties();

    /**
     * Sets the extended properties.
     *
     * @param extendedProperties The extended properties.
     * @since 1.5.0
     */
    void setExtendedProperties(String extendedProperties);
}
