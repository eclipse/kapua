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
package org.eclipse.kapua.message.device.lifecycle;

import org.eclipse.kapua.message.KapuaPayload;

/**
 * Kapua data message payload object definition.
 * 
 * @since 1.0
 *
 */
public interface KapuaAppsPayload extends KapuaPayload {

    /**
     * Get the device uptime
     * 
     * @return
     */
    public String getUptime();

    /**
     * Get the device display name
     * 
     * @return
     */
    public String getDisplayName();

    /**
     * Get the model name
     * 
     * @return
     */
    public String getModelName();

    /**
     * Get the model identifier
     * 
     * @return
     */
    public String getModelId();

    /**
     * Get the part number
     * 
     * @return
     */
    public String getPartNumber();

    /**
     * Get the serial number
     * 
     * @return
     */
    public String getSerialNumber();

    /**
     * Get the firmware
     * 
     * @return
     */
    public String getFirmware();

    /**
     * Get the firmware version
     * 
     * @return
     */
    public String getFirmwareVersion();

    /**
     * Get the bios
     * 
     * @return
     */
    public String getBios();

    /**
     * Get the biuos version
     * 
     * @return
     */
    public String getBiosVersion();

    /**
     * Get the operating system
     * 
     * @return
     */
    public String getOs();

    /**
     * Get the operating system version
     * 
     * @return
     */
    public String getOsVersion();

    /**
     * Get the java virtual machine
     * 
     * @return
     */
    public String getJvm();

    /**
     * Get the java virtual machine version
     * 
     * @return
     */
    public String getJvmVersion();

    /**
     * Get the java virtual machine profile
     * 
     * @return
     */
    public String getJvmProfile();

    /**
     * Get the container framework
     * 
     * @return
     */
    public String getContainerFramework();

    /**
     * Get the container framework version
     * 
     * @return
     */
    public String getContainerFrameworkVersion();

    /**
     * Get the application framework
     * 
     * @return
     */
    public String getApplicationFramework();

    /**
     * Get the application framework version
     * 
     * @return
     */
    public String getApplicationFrameworkVersion();

    /**
     * Get connection interface
     * 
     * @return
     */
    public String getConnectionInterface();

    /**
     * Get the connection interface ip
     * 
     * @return
     */
    public String getConnectionIp();

    /**
     * Get accept encoding
     * 
     * @return
     */
    public String getAcceptEncoding();

    /**
     * Get application identifiers
     * 
     * @return
     */
    public String getApplicationIdentifiers();

    /**
     * Get available processor
     * 
     * @return
     */
    public String getAvailableProcessors();

    /**
     * Get total memory
     * 
     * @return
     */
    public String getTotalMemory();

    /**
     * Get operating system architecture
     * 
     * @return
     */
    public String getOsArch();

    /**
     * Get modem imei
     * 
     * @return
     */
    public String getModemImei();

    /**
     * Get modem imsi
     * 
     * @return
     */
    public String getModemImsi();

    /**
     * Get modem iccid
     * 
     * @return
     */
    public String getModemIccid();
}
