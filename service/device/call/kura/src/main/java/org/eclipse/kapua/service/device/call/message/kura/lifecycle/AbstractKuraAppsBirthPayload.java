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

import org.eclipse.kapua.service.device.call.kura.KuraMetricsNames;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecyclePayload;

public class AbstractKuraAppsBirthPayload extends KuraPayload implements DeviceLifecyclePayload {

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
            getMetrics().put(KuraMetricsNames.UPTIME, uptime);
        }
        if (displayName != null) {
            getMetrics().put(KuraMetricsNames.DISPLAY_NAME, displayName);
        }
        if (modelName != null) {
            getMetrics().put(KuraMetricsNames.MODEL_NAME, modelName);
        }
        if (modelId != null) {
            getMetrics().put(KuraMetricsNames.MODEL_ID, modelId);
        }
        if (partNumber != null) {
            getMetrics().put(KuraMetricsNames.PART_NUMBER, partNumber);
        }
        if (serialNumber != null) {
            getMetrics().put(KuraMetricsNames.SERIAL_NUMBER, serialNumber);
        }
        if (firmwareVersion != null) {
            getMetrics().put(KuraMetricsNames.FIRMWARE_VERSION, firmwareVersion);
        }
        if (biosVersion != null) {
            getMetrics().put(KuraMetricsNames.BIOS_VERSION, biosVersion);
        }
        if (os != null) {
            getMetrics().put(KuraMetricsNames.OS, os);
        }
        if (osVersion != null) {
            getMetrics().put(KuraMetricsNames.OS_VERSION, osVersion);
        }
        if (jvmName != null) {
            getMetrics().put(KuraMetricsNames.JVM_NAME, jvmName);
        }
        if (jvmVersion != null) {
            getMetrics().put(KuraMetricsNames.JVM_VERSION, jvmVersion);
        }
        if (jvmProfile != null) {
            getMetrics().put(KuraMetricsNames.JVM_PROFILE, jvmProfile);
        }
        if (applicationFramework != null) {
            getMetrics().put(KuraMetricsNames.APPLICATION_FRAMEWORK, applicationFramework);
        }
        if (applicationFrameworkVersion != null) {
            getMetrics().put(KuraMetricsNames.APPLICATION_FRAMEWORK_VERSION, applicationFrameworkVersion);
        }
        if (connectionInterface != null) {
            getMetrics().put(KuraMetricsNames.CONNECTION_INTERFACE, connectionInterface);
        }
        if (connectionIp != null) {
            getMetrics().put(KuraMetricsNames.CONNECTION_IP, connectionIp);
        }
        if (acceptEncoding != null) {
            getMetrics().put(KuraMetricsNames.ACCEPT_ENCODING, acceptEncoding);
        }
        if (applicationIdentifiers != null) {
            getMetrics().put(KuraMetricsNames.APPLICATION_IDS, applicationIdentifiers);
        }
        if (availableProcessors != null) {
            getMetrics().put(KuraMetricsNames.AVAILABLE_PROCESSORS, availableProcessors);
        }
        if (totalMemory != null) {
            getMetrics().put(KuraMetricsNames.TOTAL_MEMORY, totalMemory);
        }
        if (osArch != null) {
            getMetrics().put(KuraMetricsNames.OS_ARCH, osArch);
        }
        if (osgiFramework != null) {
            getMetrics().put(KuraMetricsNames.OSGI_FRAMEWORK, osgiFramework);
        }
        if (osgiFrameworkVersion != null) {
            getMetrics().put(KuraMetricsNames.OSGI_FRAMEWORK_VERSION, osgiFrameworkVersion);
        }
        if (modemImei != null) {
            getMetrics().put(KuraMetricsNames.MODEM_IMEI, modemImei);
        }
        if (modemImsi != null) {
            getMetrics().put(KuraMetricsNames.MODEM_IMSI, modemImsi);
        }
        if (modemIccid != null) {
            getMetrics().put(KuraMetricsNames.MODEM_ICCID, modemIccid);
        }
    }

    /**
     * Get the device uptime
     *
     * @return
     */
    public String getUptime() {
        return (String) getMetrics().get(KuraMetricsNames.UPTIME);
    }

    /**
     * Get the device display name
     *
     * @return
     */
    public String getDisplayName() {
        return (String) getMetrics().get(KuraMetricsNames.DISPLAY_NAME);
    }

    /**
     * Get the model name
     *
     * @return
     */
    public String getModelName() {
        return (String) getMetrics().get(KuraMetricsNames.MODEL_NAME);
    }

    /**
     * Get the model identifier
     *
     * @return
     */
    public String getModelId() {
        return (String) getMetrics().get(KuraMetricsNames.MODEL_ID);
    }

    /**
     * Get the part number
     *
     * @return
     */
    public String getPartNumber() {
        return (String) getMetrics().get(KuraMetricsNames.PART_NUMBER);
    }

    /**
     * Get the serial number
     *
     * @return
     */
    public String getSerialNumber() {
        return (String) getMetrics().get(KuraMetricsNames.SERIAL_NUMBER);
    }

    /**
     * Get the firmware
     *
     * @return
     */
    public String getFirmware() {
        return (String) getMetrics().get(KuraMetricsNames.FIRMWARE_VERSION);
    }

    /**
     * Get the firmware version
     *
     * @return
     */
    public String getFirmwareVersion() {
        return (String) getMetrics().get(KuraMetricsNames.FIRMWARE_VERSION);
    }

    /**
     * Get the bios
     *
     * @return
     */
    public String getBios() {
        return (String) getMetrics().get(KuraMetricsNames.BIOS_VERSION);
    }

    /**
     * Get the biuos version
     *
     * @return
     */
    public String getBiosVersion() {
        return (String) getMetrics().get(KuraMetricsNames.BIOS_VERSION);
    }

    /**
     * Get the operating system
     *
     * @return
     */
    public String getOs() {
        return (String) getMetrics().get(KuraMetricsNames.OS);
    }

    /**
     * Get the operating system version
     *
     * @return
     */
    public String getOsVersion() {
        return (String) getMetrics().get(KuraMetricsNames.OS_VERSION);
    }

    /**
     * Get the java virtual machine
     *
     * @return
     */
    public String getJvm() {
        return (String) getMetrics().get(KuraMetricsNames.JVM_NAME);
    }

    /**
     * Get the java virtual machine version
     *
     * @return
     */
    public String getJvmVersion() {
        return (String) getMetrics().get(KuraMetricsNames.JVM_VERSION);
    }

    /**
     * Get the java virtual machine profile
     *
     * @return
     */
    public String getJvmProfile() {
        return (String) getMetrics().get(KuraMetricsNames.JVM_PROFILE);
    }

    /**
     * Get the container framework
     *
     * @return
     */
    public String getContainerFramework() {
        return (String) getMetrics().get(KuraMetricsNames.OSGI_FRAMEWORK);
    }

    /**
     * Get the container framework version
     *
     * @return
     */
    public String getContainerFrameworkVersion() {
        return (String) getMetrics().get(KuraMetricsNames.OSGI_FRAMEWORK_VERSION);
    }

    /**
     * Get the application framework
     *
     * @return
     */
    public String getApplicationFramework() {
        String value = (String) getMetrics().get(KuraMetricsNames.APPLICATION_FRAMEWORK);
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
        String value = (String) getMetrics().get(KuraMetricsNames.APPLICATION_FRAMEWORK_VERSION);
        if (value != null) {
            return value;
        }
        value = (String) getMetrics().get(KuraMetricsNames.KURA_VERSION);
        if (value != null) {
            return value;
        }
        return (String) getMetrics().get(KuraMetricsNames.ESF_VERSION);
    }

    /**
     * Get connection interface
     *
     * @return
     */
    public String getConnectionInterface() {
        return (String) getMetrics().get(KuraMetricsNames.CONNECTION_INTERFACE);
    }

    /**
     * Get the connection interface ip
     *
     * @return
     */
    public String getConnectionIp() {
        return (String) getMetrics().get(KuraMetricsNames.CONNECTION_IP);
    }

    /**
     * Get accept encoding
     *
     * @return
     */
    public String getAcceptEncoding() {
        return (String) getMetrics().get(KuraMetricsNames.ACCEPT_ENCODING);
    }

    /**
     * Get application identifiers
     *
     * @return
     */
    public String getApplicationIdentifiers() {
        return (String) getMetrics().get(KuraMetricsNames.APPLICATION_IDS);
    }

    /**
     * Get available processor
     *
     * @return
     */
    public String getAvailableProcessors() {
        return (String) getMetrics().get(KuraMetricsNames.AVAILABLE_PROCESSORS);
    }

    /**
     * Get total memory
     *
     * @return
     */
    public String getTotalMemory() {
        return (String) getMetrics().get(KuraMetricsNames.TOTAL_MEMORY);
    }

    /**
     * Get operating system architecture
     *
     * @return
     */
    public String getOsArch() {
        return (String) getMetrics().get(KuraMetricsNames.OS_ARCH);
    }

    /**
     * Get modem imei
     *
     * @return
     */
    public String getModemImei() {
        return (String) getMetrics().get(KuraMetricsNames.MODEM_IMEI);
    }

    /**
     * Get modem imsi
     *
     * @return
     */
    public String getModemImsi() {
        return (String) getMetrics().get(KuraMetricsNames.MODEM_IMSI);
    }

    /**
     * Get modem iccid
     *
     * @return
     */
    public String getModemIccid() {
        return (String) getMetrics().get(KuraMetricsNames.MODEM_ICCID);
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
