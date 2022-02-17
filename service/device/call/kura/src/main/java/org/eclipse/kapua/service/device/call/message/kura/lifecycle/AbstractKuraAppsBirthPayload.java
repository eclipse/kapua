/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecyclePayload;
import org.eclipse.kapua.service.device.registry.Device;

/**
 * {@code abstract} base class for {@link KuraAppsPayload} and {@link KuraBirthPayload}.
 * <p>
 * {@link KuraAppsPayload} and {@link KuraBirthPayload} have the same format.
 *
 * @since 1.0.0
 */
public class AbstractKuraAppsBirthPayload extends AbstractKuraLifecyclePayload implements DeviceLifecyclePayload {

    /**
     * {@link Device} uptime metric name.
     *
     * @since 1.0.0
     */
    protected static final String UPTIME = "uptime";

    /**
     * {@link Device} display ma,e metric name.
     *
     * @since 1.0.0
     */
    protected static final String DISPLAY_NAME = "display_name";

    /**
     * {@link Device} model name metric name.
     *
     * @since 1.0.0
     */
    protected static final String MODEL_NAME = "model_name";

    /**
     * {@link Device} model id metric name.
     *
     * @since 1.0.0
     */
    protected static final String MODEL_ID = "model_id";

    /**
     * {@link Device} part number metric name.
     *
     * @since 1.0.0
     */
    protected static final String PART_NUMBER = "part_number";

    /**
     * {@link Device} serial number metric name.
     *
     * @since 1.0.0
     */
    protected static final String SERIAL_NUMBER = "serial_number";

    /**
     * {@link Device} available processors metric name.
     *
     * @since 1.0.0
     */
    protected static final String AVAILABLE_PROCESSORS = "available_processors";

    /**
     * {@link Device} total memory metric name.
     *
     * @since 1.0.0
     */
    protected static final String TOTAL_MEMORY = "total_memory";

    /**
     * {@link Device} firmware version metric name.
     *
     * @since 1.0.0
     */
    protected static final String FIRMWARE_VERSION = "firmware_version";

    /**
     * {@link Device} bios version metric name.
     *
     * @since 1.0.0
     */
    protected static final String BIOS_VERSION = "bios_version";

    /**
     * {@link Device} os name metric name.
     *
     * @since 1.0.0
     */
    protected static final String OS = "os";

    /**
     * {@link Device} os version metric name.
     *
     * @since 1.0.0
     */
    protected static final String OS_VERSION = "os_version";

    /**
     * {@link Device} os architecture metric name.
     *
     * @since 1.0.0
     */
    protected static final String OS_ARCH = "os_arch";

    /**
     * {@link Device} JVM name metric name.
     *
     * @since 1.0.0
     */
    protected static final String JVM_NAME = "jvm_name";

    /**
     * {@link Device} JVM version metric name.
     *
     * @since 1.0.0
     */
    protected static final String JVM_VERSION = "jvm_version";

    /**
     * {@link Device} JVM profile metric name.
     *
     * @since 1.0.0
     */
    protected static final String JVM_PROFILE = "jvm_profile";

    /**
     * {@link Device} application framework ESF version metric name.
     *
     * @since 1.0.0
     */
    protected static final String ESF_VERSION = "esf_version";

    /**
     * {@link Device} application framework Kura version metric name.
     *
     * @since 1.0.0
     */
    protected static final String KURA_VERSION = "kura_version";

    /**
     * {@link Device} application framework name metric name.
     *
     * @since 1.0.0
     */
    protected static final String APPLICATION_FRAMEWORK = "application_framework";

    /**
     * {@link Device} application framework version metric name.
     *
     * @since 1.0.0
     */
    protected static final String APPLICATION_FRAMEWORK_VERSION = "application_framework_version";

    /**
     * {@link Device} osgi framework name metric name.
     *
     * @since 1.0.0
     */
    protected static final String OSGI_FRAMEWORK = "osgi_framework";

    /**
     * {@link Device} osgi framework version metric name.
     *
     * @since 1.0.0
     */
    protected static final String OSGI_FRAMEWORK_VERSION = "osgi_framework_version";

    /**
     * {@link Device} connection interfaces metric name.
     *
     * @since 1.0.0
     */
    protected static final String CONNECTION_INTERFACE = "connection_interface";

    /**
     * {@link Device} connection ips metric name.
     *
     * @since 1.0.0
     */
    protected static final String CONNECTION_IP = "connection_ip";

    /**
     * {@link Device} accepted encoding metric name.
     *
     * @since 1.0.0
     */
    protected static final String ACCEPT_ENCODING = "accept_encoding";

    /**
     * {@link Device} application ids  metric name.
     *
     * @since 1.0.0
     */
    protected static final String APPLICATION_IDS = "application_ids";

    /**
     * {@link Device} modem IMEI metric name.
     *
     * @since 1.0.0
     */
    protected static final String MODEM_IMEI = "modem_imei";

    /**
     * {@link Device} modem IMSI metric name.
     *
     * @since 1.0.0
     */
    protected static final String MODEM_IMSI = "modem_imsi";

    /**
     * {@link Device} modem ICCID metric name.
     *
     * @since 1.0.0
     */
    protected static final String MODEM_ICCID = "modem_iccid";

    /**
     * {@link Device} extended properties metric name.
     *
     * @since 1.5.0
     */
    protected static final String EXTENDED_PROPERTIES = "extended_properties";

    /**
     * {@link Device} default application framework metric name.
     *
     * @since 1.0.0
     */
    protected static final String DEFAULT_APPLICATION_FRAMEWORK = "Kura";
    private static final long serialVersionUID = 5490945197263668115L;


    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public AbstractKuraAppsBirthPayload() {
        super();
    }

    /**
     * Constructor.
     *
     * @param uptime                      {@link Device} uptime.
     * @param displayName                 {@link Device} display ma,e.
     * @param modelName                   {@link Device} model name.
     * @param modelId                     {@link Device} model id.
     * @param partNumber                  {@link Device} part number.
     * @param serialNumber                {@link Device} serial number.
     * @param firmwareVersion             {@link Device} available processors.
     * @param biosVersion                 {@link Device} total memory.
     * @param os                          {@link Device} firmware version.
     * @param osVersion                   {@link Device} bios version.
     * @param jvmName                     {@link Device} os name.
     * @param jvmVersion                  {@link Device} os version.
     * @param jvmProfile                  {@link Device} os architecture.
     * @param applicationFramework        {@link Device} JVM name.
     * @param applicationFrameworkVersion {@link Device} JVM version.
     * @param connectionInterface         {@link Device} JVM profile.
     * @param connectionIp                {@link Device} application framework name.
     * @param acceptEncoding              {@link Device} application framework version.
     * @param applicationIdentifiers      {@link Device} osgi framework name.
     * @param availableProcessors         {@link Device} osgi framework version.
     * @param totalMemory                 {@link Device} connection interfaces.
     * @param osArch                      {@link Device} connection ips.
     * @param osgiFramework               {@link Device} accepted encoding.
     * @param osgiFrameworkVersion        {@link Device} application ids .
     * @param modemImei                   {@link Device} modem IMEI.
     * @param modemImsi                   {@link Device} modem IMSI.
     * @param modemIccid                  {@link Device} modem ICCID.
     * @param extendedProperties          {@link Device} extended properties.
     * @since 1.0.0
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
                                        String modemIccid,
                                        String extendedProperties) {
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
        if (extendedProperties != null) {
            getMetrics().put(EXTENDED_PROPERTIES, extendedProperties);
        }
    }

    /**
     * Gets the {@link Device} uptime.
     *
     * @return The {@link Device} uptime.
     * @since 1.0.0
     */
    public String getUptime() {
        return (String) getMetrics().get(UPTIME);
    }

    /**
     * Gets the {@link Device} display name.
     *
     * @return The {@link Device} display name.
     * @since 1.0.0
     */
    public String getDisplayName() {
        return (String) getMetrics().get(DISPLAY_NAME);
    }

    /**
     * Gets the {@link Device} model name.
     *
     * @return The {@link Device} model name.
     * @since 1.0.0
     */
    public String getModelName() {
        return (String) getMetrics().get(MODEL_NAME);
    }

    /**
     * Gets the {@link Device} model id.
     *
     * @return The {@link Device} model id.
     * @since 1.0.0
     */
    public String getModelId() {
        return (String) getMetrics().get(MODEL_ID);
    }

    /**
     * Gets the {@link Device} part number.
     *
     * @return The {@link Device} part number.
     * @since 1.0.0
     */
    public String getPartNumber() {
        return (String) getMetrics().get(PART_NUMBER);
    }

    /**
     * Gets the {@link Device} serial number.
     *
     * @return The {@link Device} serial number.
     * @since 1.0.0
     */
    public String getSerialNumber() {
        return (String) getMetrics().get(SERIAL_NUMBER);
    }

    /**
     * Gets the {@link Device} firmware name.
     *
     * @return The {@link Device} firmware name.
     * @since 1.0.0
     */
    public String getFirmware() {
        return (String) getMetrics().get(FIRMWARE_VERSION);
    }

    /**
     * Gets the {@link Device} firmware version.
     *
     * @return The {@link Device} firmware version.
     * @since 1.0.0
     */
    public String getFirmwareVersion() {
        return (String) getMetrics().get(FIRMWARE_VERSION);
    }

    /**
     * Gets the {@link Device} bios name.
     *
     * @return The {@link Device} bios name.
     * @since 1.0.0
     */
    public String getBios() {
        return (String) getMetrics().get(BIOS_VERSION);
    }

    /**
     * Gets the {@link Device} bios version.
     *
     * @return The {@link Device} bios version.
     * @since 1.0.0
     */
    public String getBiosVersion() {
        return (String) getMetrics().get(BIOS_VERSION);
    }

    /**
     * Gets the {@link Device} os name.
     *
     * @return The {@link Device} os name.
     * @since 1.0.0
     */
    public String getOs() {
        return (String) getMetrics().get(OS);
    }

    /**
     * Gets the {@link Device} os version.
     *
     * @return The {@link Device} os version.
     * @since 1.0.0
     */
    public String getOsVersion() {
        return (String) getMetrics().get(OS_VERSION);
    }

    /**
     * Gets the {@link Device} JVM name.
     *
     * @return The {@link Device} JVM name.
     * @since 1.0.0
     */
    public String getJvm() {
        return (String) getMetrics().get(JVM_NAME);
    }

    /**
     * Gets the {@link Device} JVM version.
     *
     * @return The {@link Device} JVM version.
     * @since 1.0.0
     */
    public String getJvmVersion() {
        return (String) getMetrics().get(JVM_VERSION);
    }

    /**
     * Gets the {@link Device} JVM profile.
     *
     * @return The {@link Device} JVM profile.
     * @since 1.0.0
     */
    public String getJvmProfile() {
        return (String) getMetrics().get(JVM_PROFILE);
    }

    /**
     * Gets the {@link Device} container framework name.
     *
     * @return The {@link Device} container framework name.
     * @since 1.0.0
     */
    public String getContainerFramework() {
        return (String) getMetrics().get(OSGI_FRAMEWORK);
    }

    /**
     * Gets the {@link Device} container framework version.
     *
     * @return The {@link Device} container framework version.
     * @since 1.0.0
     */
    public String getContainerFrameworkVersion() {
        return (String) getMetrics().get(OSGI_FRAMEWORK_VERSION);
    }

    /**
     * Gets the {@link Device} application framework name.
     *
     * @return The {@link Device} application framework name.
     * @since 1.0.0
     */
    public String getApplicationFramework() {
        String value = (String) getMetrics().get(APPLICATION_FRAMEWORK);
        if (value != null) {
            return value;
        }
        return (String) getMetrics().get(DEFAULT_APPLICATION_FRAMEWORK);
    }

    /**
     * Gets the {@link Device} application framework version.
     *
     * @return The {@link Device} application framework version.
     * @since 1.0.0
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
     * Gets the {@link Device} connection interfaces.
     *
     * @return The {@link Device} connection interfaces.
     * @since 1.0.0
     */
    public String getConnectionInterface() {
        return (String) getMetrics().get(CONNECTION_INTERFACE);
    }

    /**
     * Gets the {@link Device} connection ips.
     *
     * @return The {@link Device} connection ips.
     * @since 1.0.0
     */
    public String getConnectionIp() {
        return (String) getMetrics().get(CONNECTION_IP);
    }

    /**
     * Gets the {@link Device} accepted encoding.
     *
     * @return The {@link Device} accepted encoding.
     * @since 1.0.0
     */
    public String getAcceptEncoding() {
        return (String) getMetrics().get(ACCEPT_ENCODING);
    }

    /**
     * Gets the {@link Device} application identifiers.
     *
     * @return The {@link Device} application identifiers.
     * @since 1.0.0
     */
    public String getApplicationIdentifiers() {
        return (String) getMetrics().get(APPLICATION_IDS);
    }

    /**
     * Gets the {@link Device} available processors.
     *
     * @return The {@link Device} available processors.
     * @since 1.0.0
     */
    public String getAvailableProcessors() {
        return (String) getMetrics().get(AVAILABLE_PROCESSORS);
    }

    /**
     * Gets the {@link Device} total memory.
     *
     * @return The {@link Device} total memory.
     * @since 1.0.0
     */
    public String getTotalMemory() {
        return (String) getMetrics().get(TOTAL_MEMORY);
    }

    /**
     * Gets the {@link Device} os architecture.
     *
     * @return The {@link Device} os architecture.
     * @since 1.0.0
     */
    public String getOsArch() {
        return (String) getMetrics().get(OS_ARCH);
    }

    /**
     * Gets the {@link Device} modem IMEI.
     *
     * @return The {@link Device} modem IMEI.
     * @since 1.0.0
     */
    public String getModemImei() {
        return (String) getMetrics().get(MODEM_IMEI);
    }

    /**
     * Gets the {@link Device} modem IMSI.
     *
     * @return The {@link Device} modem IMSI.
     * @since 1.0.0
     */
    public String getModemImsi() {
        return (String) getMetrics().get(MODEM_IMSI);
    }

    /**
     * Gets the {@link Device} modem ICCID.
     *
     * @return The {@link Device} modem ICCID.
     * @since 1.0.0
     */
    public String getModemIccid() {
        return (String) getMetrics().get(MODEM_ICCID);
    }

    /**
     * Gets the {@link Device} extended properties.
     *
     * @return The {@link Device} extended properties.
     * @since 1.5.0
     */
    public String getExtendedProperties() {
        return (String) getMetrics().get(EXTENDED_PROPERTIES);
    }


}
