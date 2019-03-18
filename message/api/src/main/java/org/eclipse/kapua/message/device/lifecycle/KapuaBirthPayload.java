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
     * Gets the display name.
     *
     * @return The display name.
     * @since 1.0.0
     */
    String getDisplayName();

    /**
     * Gets the model name.
     *
     * @return The model name.
     * @since 1.0.0
     */
    String getModelName();

    /**
     * Gets the model identifier.
     *
     * @return The model identifier.
     * @since 1.0.0
     */
    String getModelId();

    /**
     * Gets the part number.
     *
     * @return The part number.
     * @since 1.0.0
     */
    String getPartNumber();

    /**
     * Gets the serial number.
     *
     * @return The serial number.
     * @since 1.0.0
     */
    String getSerialNumber();

    /**
     * Gets the firmware name.
     *
     * @return The firmware name.
     * @since 1.0.0
     */
    String getFirmware();

    /**
     * Gets the firmware version.
     *
     * @return The firmware version.
     * @since 1.0.0
     */
    String getFirmwareVersion();

    /**
     * Gets the bios name
     *
     * @return The bios name.
     * @since 1.0.0
     */
    String getBios();

    /**
     * Gets the bios version.
     *
     * @return The bios version.
     * @since 1.0.0
     */
    String getBiosVersion();

    /**
     * Gets the operating system name.
     *
     * @return The operating system name.
     * @since 1.0.0
     */
    String getOs();

    /**
     * Gets the operating system version.
     *
     * @return The operating system version.
     * @since 1.0.0
     */
    String getOsVersion();

    /**
     * Gets the java virtual machine name/vendor.
     *
     * @return The java virtual machine name/vendor.
     * @since 1.0.0
     */
    String getJvm();

    /**
     * Gets the java virtual machine version.
     *
     * @return The java virtual machine version.
     * @since 1.0.0
     */
    String getJvmVersion();

    /**
     * Gets the java virtual machine profile.
     *
     * @return The java virtual machine profile.
     * @since 1.0.0
     */
    String getJvmProfile();

    /**
     * Gets the container framework name.
     *
     * @return The container framework name.
     * @since 1.0.0
     */
    String getContainerFramework();

    /**
     * Gets the container framework version.
     *
     * @return The container framework version.
     * @since 1.0.0
     */
    String getContainerFrameworkVersion();

    /**
     * Gets the application framework name.
     *
     * @return The application framework name.
     * @since 1.0.0
     */
    String getApplicationFramework();

    /**
     * Gets the application framework version
     *
     * @return The application framework version
     * @since 1.0.0
     */
    String getApplicationFrameworkVersion();

    /**
     * Gets the connection interface.
     *
     * @return The connection interface.
     * @since 1.0.0
     */
    String getConnectionInterface();

    /**
     * Gets the connection interface ip.
     *
     * @return The connection interface ip.
     * @since 1.0.0
     */
    String getConnectionIp();

    /**
     * Gets the accepted encoding.
     *
     * @return The accepted encoding.
     * @since 1.0.0
     */
    String getAcceptEncoding();

    /**
     * Gets the application identifiers.
     *
     * @return The application identifiers.
     * @since 1.0.0
     */
    String getApplicationIdentifiers();

    /**
     * Gets the available processors.
     *
     * @return The available processors.
     * @since 1.0.0
     */
    String getAvailableProcessors();

    /**
     * Gets the total memory.
     *
     * @return The total memory.
     * @since 1.0.0
     */
    String getTotalMemory();

    /**
     * Gets the operating system architecture.
     *
     * @return The operating system architecture.
     * @since 1.0.0
     */
    String getOsArch();

    /**
     * Gets the modem IMEI.
     *
     * @return The modem IMEI.
     * @since 1.0.0
     */
    String getModemImei();

    /**
     * Gets the modem IMSI.
     *
     * @return The modem IMSI.
     * @since 1.0.0
     */
    String getModemImsi();

    /**
     * Gets the modem ICCID.
     *
     * @return The modem ICCID.
     * @since 1.0.0
     */
    String getModemIccid();

}
