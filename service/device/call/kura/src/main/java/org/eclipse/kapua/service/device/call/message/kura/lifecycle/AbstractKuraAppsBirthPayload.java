/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecyclePayload;

public class AbstractKuraAppsBirthPayload extends KuraPayload implements DeviceLifecyclePayload {

    protected static final String UPTIME = "uptime";
    protected static final String DISPLAY_NAME = "display_name";
    protected static final String MODEL_NAME = "model_name";
    protected static final String MODEL_ID = "model_id";
    protected static final String PART_NUMBER = "part_number";
    protected static final String SERIAL_NUMBER = "serial_number";
    protected static final String AVAILABLE_PROCESSORS = "available_processors";
    protected static final String TOTAL_MEMORY = "total_memory";
    protected static final String FIRMWARE_VERSION = "firmware_version";
    protected static final String BIOS_VERSION = "bios_version";
    protected static final String OS = "os";
    protected static final String OS_VERSION = "os_version";
    protected static final String OS_ARCH = "os_arch";
    protected static final String JVM_NAME = "jvm_name";
    protected static final String JVM_VERSION = "jvm_version";
    protected static final String JVM_PROFILE = "jvm_profile";
    protected static final String ESF_VERSION = "esf_version";
    protected static final String KURA_VERSION = "kura_version";
    protected static final String APPLICATION_FRAMEWORK = "application_framework";
    protected static final String APPLICATION_FRAMEWORK_VERSION = "application_framework_version";
    protected static final String OSGI_FRAMEWORK = "osgi_framework";
    protected static final String OSGI_FRAMEWORK_VERSION = "osgi_framework_version";
    protected static final String CONNECTION_INTERFACE = "connection_interface";
    protected static final String CONNECTION_IP = "connection_ip";
    protected static final String ACCEPT_ENCODING = "accept_encoding";
    protected static final String APPLICATION_IDS = "application_ids";
    protected static final String MODEM_IMEI = "modem_imei";
    protected static final String MODEM_IMSI = "modem_imsi";
    protected static final String MODEM_ICCID = "modem_iccid";

    protected static final String DEFAULT_APPLICATION_FRAMEWORK = "Kura";

    /**
     * Constructor
     */
    public AbstractKuraAppsBirthPayload() {
        super();
    }

    /**
     * Constructor
     *
     * @param uptime
     * @param displayName
     * @param modelName
     * @param modelId
     * @param partNumber
     * @param serialNumber
     * @param firmwareVersion
     * @param biosVersion
     * @param os
     * @param osVersion
     * @param jvmName
     * @param jvmVersion
     * @param jvmProfile
     * @param applicationFramework
     * @param connectionInterface
     * @param connectionIp
     * @param acceptEncoding
     * @param applicationIdentifiers
     * @param availableProcessors
     * @param totalMemory
     * @param osArch
     * @param osgiFramework
     * @param osgiFrameworkVersion
     * @param modemImei
     * @param modemImsi
     * @param modemIccid
     */
    public AbstractKuraAppsBirthPayload(String uptime,
            String displayName,
            String modelName,
            String modelId,
            String partNumber,
            String serialNumber,
            String firmwareVersion,
            String biosVersion,
            String os,
            String osVersion,
            String jvmName,
            String jvmVersion,
            String jvmProfile,
            String applicationFramework,
            String applicationFrameworkVersion,
            String connectionInterface,
            String connectionIp,
            String acceptEncoding,
            String applicationIdentifiers,
            String availableProcessors,
            String totalMemory,
            String osArch,
            String osgiFramework,
            String osgiFrameworkVersion,
            String modemImei,
            String modemImsi,
            String modemIccid) {
        super();

        if (uptime != null) {
            getMetrics().put(UPTIME, uptime);
        }
        if (displayName != null) {
            getMetrics().put(DISPLAY_NAME, displayName);
        }
        if (modelName != null) {
            getMetrics().put(MODEL_NAME, modelName);
        }
        if (modelId != null) {
            getMetrics().put(MODEL_ID, modelId);
        }
        if (partNumber != null) {
            getMetrics().put(PART_NUMBER, partNumber);
        }
        if (serialNumber != null) {
            getMetrics().put(SERIAL_NUMBER, serialNumber);
        }
        if (firmwareVersion != null) {
            getMetrics().put(FIRMWARE_VERSION, firmwareVersion);
        }
        if (biosVersion != null) {
            getMetrics().put(BIOS_VERSION, biosVersion);
        }
        if (os != null) {
            getMetrics().put(OS, os);
        }
        if (osVersion != null) {
            getMetrics().put(OS_VERSION, osVersion);
        }
        if (jvmName != null) {
            getMetrics().put(JVM_NAME, jvmName);
        }
        if (jvmVersion != null) {
            getMetrics().put(JVM_VERSION, jvmVersion);
        }
        if (jvmProfile != null) {
            getMetrics().put(JVM_PROFILE, jvmProfile);
        }
        if (applicationFramework != null) {
            getMetrics().put(APPLICATION_FRAMEWORK, applicationFramework);
        }
        if (applicationFrameworkVersion != null) {
            getMetrics().put(APPLICATION_FRAMEWORK_VERSION, applicationFrameworkVersion);
        }
        if (connectionInterface != null) {
            getMetrics().put(CONNECTION_INTERFACE, connectionInterface);
        }
        if (connectionIp != null) {
            getMetrics().put(CONNECTION_IP, connectionIp);
        }
        if (acceptEncoding != null) {
            getMetrics().put(ACCEPT_ENCODING, acceptEncoding);
        }
        if (applicationIdentifiers != null) {
            getMetrics().put(APPLICATION_IDS, applicationIdentifiers);
        }
        if (availableProcessors != null) {
            getMetrics().put(AVAILABLE_PROCESSORS, availableProcessors);
        }
        if (totalMemory != null) {
            getMetrics().put(TOTAL_MEMORY, totalMemory);
        }
        if (osArch != null) {
            getMetrics().put(OS_ARCH, osArch);
        }
        if (osgiFramework != null) {
            getMetrics().put(OSGI_FRAMEWORK, osgiFramework);
        }
        if (osgiFrameworkVersion != null) {
            getMetrics().put(OSGI_FRAMEWORK_VERSION, osgiFrameworkVersion);
        }
        if (modemImei != null) {
            getMetrics().put(MODEM_IMEI, modemImei);
        }
        if (modemImsi != null) {
            getMetrics().put(MODEM_IMSI, modemImsi);
        }
        if (modemIccid != null) {
            getMetrics().put(MODEM_ICCID, modemIccid);
        }
    }

    /**
     * Get the device uptime
     *
     * @return
     */
    public String getUptime() {
        return (String) getMetrics().get(UPTIME);
    }

    /**
     * Get the device display name
     *
     * @return
     */
    public String getDisplayName() {
        return (String) getMetrics().get(DISPLAY_NAME);
    }

    /**
     * Get the model name
     *
     * @return
     */
    public String getModelName() {
        return (String) getMetrics().get(MODEL_NAME);
    }

    /**
     * Get the model identifier
     *
     * @return
     */
    public String getModelId() {
        return (String) getMetrics().get(MODEL_ID);
    }

    /**
     * Get the part number
     *
     * @return
     */
    public String getPartNumber() {
        return (String) getMetrics().get(PART_NUMBER);
    }

    /**
     * Get the serial number
     *
     * @return
     */
    public String getSerialNumber() {
        return (String) getMetrics().get(SERIAL_NUMBER);
    }

    /**
     * Get the firmware
     *
     * @return
     */
    public String getFirmware() {
        return (String) getMetrics().get(FIRMWARE_VERSION);
    }

    /**
     * Get the firmware version
     *
     * @return
     */
    public String getFirmwareVersion() {
        return (String) getMetrics().get(FIRMWARE_VERSION);
    }

    /**
     * Get the bios
     *
     * @return
     */
    public String getBios() {
        return (String) getMetrics().get(BIOS_VERSION);
    }

    /**
     * Get the biuos version
     *
     * @return
     */
    public String getBiosVersion() {
        return (String) getMetrics().get(BIOS_VERSION);
    }

    /**
     * Get the operating system
     *
     * @return
     */
    public String getOs() {
        return (String) getMetrics().get(OS);
    }

    /**
     * Get the operating system version
     *
     * @return
     */
    public String getOsVersion() {
        return (String) getMetrics().get(OS_VERSION);
    }

    /**
     * Get the java virtual machine
     *
     * @return
     */
    public String getJvm() {
        return (String) getMetrics().get(JVM_NAME);
    }

    /**
     * Get the java virtual machine version
     *
     * @return
     */
    public String getJvmVersion() {
        return (String) getMetrics().get(JVM_VERSION);
    }

    /**
     * Get the java virtual machine profile
     *
     * @return
     */
    public String getJvmProfile() {
        return (String) getMetrics().get(JVM_PROFILE);
    }

    /**
     * Get the container framework
     *
     * @return
     */
    public String getContainerFramework() {
        return (String) getMetrics().get(OSGI_FRAMEWORK);
    }

    /**
     * Get the container framework version
     *
     * @return
     */
    public String getContainerFrameworkVersion() {
        return (String) getMetrics().get(OSGI_FRAMEWORK_VERSION);
    }

    /**
     * Get the application framework
     *
     * @return
     */
    public String getApplicationFramework() {
        String value = (String) getMetrics().get(APPLICATION_FRAMEWORK);
        if (value != null) {
            return value;
        }
        return (String) getMetrics().get(DEFAULT_APPLICATION_FRAMEWORK);
    }

    /**
     * Get the application framework version
     *
     * @return
     */
    public String getApplicationFrameworkVersion() {
        String value = (String) getMetrics().get(APPLICATION_FRAMEWORK_VERSION);
        if (value != null) {
            return value;
        }
        value = (String) getMetrics().get(KURA_VERSION);
        if (value != null) {
            return value;
        }
        return (String) getMetrics().get(ESF_VERSION);
    }

    /**
     * Get connection interface
     *
     * @return
     */
    public String getConnectionInterface() {
        return (String) getMetrics().get(CONNECTION_INTERFACE);
    }

    /**
     * Get the connection interface ip
     *
     * @return
     */
    public String getConnectionIp() {
        return (String) getMetrics().get(CONNECTION_IP);
    }

    /**
     * Get accept encoding
     *
     * @return
     */
    public String getAcceptEncoding() {
        return (String) getMetrics().get(ACCEPT_ENCODING);
    }

    /**
     * Get application identifiers
     *
     * @return
     */
    public String getApplicationIdentifiers() {
        return (String) getMetrics().get(APPLICATION_IDS);
    }

    /**
     * Get available processor
     *
     * @return
     */
    public String getAvailableProcessors() {
        return (String) getMetrics().get(AVAILABLE_PROCESSORS);
    }

    /**
     * Get total memory
     *
     * @return
     */
    public String getTotalMemory() {
        return (String) getMetrics().get(TOTAL_MEMORY);
    }

    /**
     * Get operating system architecture
     *
     * @return
     */
    public String getOsArch() {
        return (String) getMetrics().get(OS_ARCH);
    }

    /**
     * Get modem imei
     *
     * @return
     */
    public String getModemImei() {
        return (String) getMetrics().get(MODEM_IMEI);
    }

    /**
     * Get modem imsi
     *
     * @return
     */
    public String getModemImsi() {
        return (String) getMetrics().get(MODEM_IMSI);
    }

    /**
     * Get modem iccid
     *
     * @return
     */
    public String getModemIccid() {
        return (String) getMetrics().get(MODEM_ICCID);
    }

    /**
     * Returns a displayable representation string
     *
     * @return
     */
    public String toDisplayString() {
        return new StringBuilder()
                .append("[ getUptime()=").append(getUptime())
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
                .append(", getApplicationFramework()=").append(getApplicationFramework())
                .append(", getApplicationFrameworkVersion()=").append(getApplicationFrameworkVersion())
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
}
