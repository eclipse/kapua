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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.DevicePayload;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;

public class KuraBirthPayload extends KuraPayload implements DevicePayload
{
    private final static String UPTIME                 = "uptime";
    private final static String DISPLAY_NAME           = "display_name";
    private final static String MODEL_NAME             = "model_name";
    private final static String MODEL_ID               = "model_id";
    private final static String PART_NUMBER            = "part_number";
    private final static String SERIAL_NUMBER          = "serial_number";
    private final static String AVAILABLE_PROCESSORS   = "available_processors";
    private final static String TOTAL_MEMORY           = "total_memory";
    private final static String FIRMWARE_VERSION       = "firmware_version";
    private final static String BIOS_VERSION           = "bios_version";
    private final static String OS                     = "os";
    private final static String OS_VERSION             = "os_version";
    private final static String OS_ARCH                = "os_arch";
    private final static String JVM_NAME               = "jvm_name";
    private final static String JVM_VERSION            = "jvm_version";
    private final static String JVM_PROFILE            = "jvm_profile";
    private final static String ESF_VERSION            = "esf_version";
    private final static String KURA_VERSION           = "kura_version";
    private final static String ESF_PREFIX             = "ESF_";
    private final static String ESFKURA_VERSION        = "esf_version";
    private final static String OSGI_FRAMEWORK         = "osgi_framework";
    private final static String OSGI_FRAMEWORK_VERSION = "osgi_framework_version";
    private final static String CONNECTION_INTERFACE   = "connection_interface";
    private final static String CONNECTION_IP          = "connection_ip";
    private final static String ACCEPT_ENCODING        = "accept_encoding";
    private final static String APPLICATION_IDS        = "application_ids";
    private final static String MODEM_IMEI             = "modem_imei";
    private final static String MODEM_IMSI             = "modem_imsi";
    private final static String MODEM_ICCID            = "modem_iccid";
    
    public KuraBirthPayload() {
		super();
	}

    public KuraBirthPayload(String uptime,
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
                            String esfkuraVersion,
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
                            String modemIccid)
    {
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
        if (esfkuraVersion != null) {
            getMetrics().put(ESFKURA_VERSION, esfkuraVersion);
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

	public String getUptime()
    {
        return (String) getMetrics().get(UPTIME);
    }

    public String getDisplayName()
    {
        return (String) getMetrics().get(DISPLAY_NAME);
    }

    public String getModelName()
    {
        return (String) getMetrics().get(MODEL_NAME);
    }

    public String getModelId()
    {
        return (String) getMetrics().get(MODEL_ID);
    }

    public String getPartNumber()
    {
        return (String) getMetrics().get(PART_NUMBER);
    }

    public String getSerialNumber()
    {
        return (String) getMetrics().get(SERIAL_NUMBER);
    }

    public String getFirmware()
    {
        return (String) getMetrics().get(FIRMWARE_VERSION);
    }

    public String getFirmwareVersion()
    {
        return (String) getMetrics().get(FIRMWARE_VERSION);
    }

    public String getBios()
    {
        return (String) getMetrics().get(BIOS_VERSION);
    }

    public String getBiosVersion()
    {
        return (String) getMetrics().get(BIOS_VERSION);
    }

    public String getOs()
    {
        return (String) getMetrics().get(OS);
    }

    public String getOsVersion()
    {
        return (String) getMetrics().get(OS_VERSION);
    }

    public String getJvm()
    {
        return (String) getMetrics().get(JVM_NAME);
    }

    public String getJvmVersion()
    {
        return (String) getMetrics().get(JVM_VERSION);
    }

    public String getJvmProfile()
    {
        return (String) getMetrics().get(JVM_PROFILE);
    }

    public String getContainerFramework()
    {
        return (String) getMetrics().get(OSGI_FRAMEWORK);
    }

    public String getContainerFrameworkVersion()
    {
        return (String) getMetrics().get(OSGI_FRAMEWORK_VERSION);
    }

    public String getApplicationFramework()
    {
        return (String) getMetrics().get(ESFKURA_VERSION);
    }

    public String getApplicationFrameworkVersion()
    {
        return (String) getMetrics().get(ESFKURA_VERSION);
    }

    public String getConnectionInterface()
    {
        return (String) getMetrics().get(CONNECTION_INTERFACE);
    }

    public String getConnectionIp()
    {
        return (String) getMetrics().get(CONNECTION_IP);
    }

    public String getAcceptEncoding()
    {
        return (String) getMetrics().get(ACCEPT_ENCODING);
    }

    public String getApplicationIdentifiers()
    {
        return (String) getMetrics().get(APPLICATION_IDS);
    }

    public String getAvailableProcessors()
    {
        return (String) getMetrics().get(AVAILABLE_PROCESSORS);
    }

    public String getTotalMemory()
    {
        return (String) getMetrics().get(TOTAL_MEMORY);
    }

    public String getOsArch()
    {
        return (String) getMetrics().get(OS_ARCH);
    }

    public String getModemImei()
    {
        return (String) getMetrics().get(MODEM_IMEI);
    }

    public String getModemImsi()
    {
        return (String) getMetrics().get(MODEM_IMSI);
    }

    public String getModemIccid()
    {
        return (String) getMetrics().get(MODEM_ICCID);
    }

    public String toDisplayString()
    {
        return new StringBuilder().append("[ getUptime()=").append(getUptime())
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
                                  .append(", getEsfKuraVersion()=").append(getApplicationFrameworkVersion())
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
