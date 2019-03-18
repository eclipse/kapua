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
     * Get the device uptime
     *
     * @return
     */
    String getUptime();

    /**
     * Get the device display name
     *
     * @return
     */
    String getDisplayName();

    /**
     * Get the model name
     *
     * @return
     */
    String getModelName();

    /**
     * Get the model identifier
     *
     * @return
     */
    String getModelId();

    /**
     * Get the part number
     *
     * @return
     */
    String getPartNumber();

    /**
     * Get the serial number
     *
     * @return
     */
    String getSerialNumber();

    /**
     * Get the firmware
     *
     * @return
     */
    String getFirmware();

    /**
     * Get the firmware version
     *
     * @return
     */
    String getFirmwareVersion();

    /**
     * Get the bios
     *
     * @return
     */
    String getBios();

    /**
     * Get the biuos version
     *
     * @return
     */
    String getBiosVersion();

    /**
     * Get the operating system
     *
     * @return
     */
    String getOs();

    /**
     * Get the operating system version
     *
     * @return
     */
    String getOsVersion();

    /**
     * Get the java virtual machine
     *
     * @return
     */
    String getJvm();

    /**
     * Get the java virtual machine version
     *
     * @return
     */
    String getJvmVersion();

    /**
     * Get the java virtual machine profile
     *
     * @return
     */
    String getJvmProfile();

    /**
     * Get the container framework
     *
     * @return
     */
    String getContainerFramework();

    /**
     * Get the container framework version
     *
     * @return
     */
    String getContainerFrameworkVersion();

    /**
     * Get the application framework
     *
     * @return
     */
    String getApplicationFramework();

    /**
     * Get the application framework version
     *
     * @return
     */
    String getApplicationFrameworkVersion();

    /**
     * Get connection interface
     *
     * @return
     */
    String getConnectionInterface();

    /**
     * Get the connection interface ip
     *
     * @return
     */
    String getConnectionIp();

    /**
     * Get accept encoding
     *
     * @return
     */
    String getAcceptEncoding();

    /**
     * Get application identifiers
     *
     * @return
     */
    String getApplicationIdentifiers();

    /**
     * Get available processor
     *
     * @return
     */
    String getAvailableProcessors();

    /**
     * Get total memory
     *
     * @return
     */
    String getTotalMemory();

    /**
     * Get operating system architecture
     *
     * @return
     */
    String getOsArch();

    /**
     * Get modem imei
     *
     * @return
     */
    String getModemImei();

    /**
     * Get modem imsi
     *
     * @return
     */
    String getModemImsi();

    /**
     * Get modem iccid
     *
     * @return
     */
    String getModemIccid();

}
