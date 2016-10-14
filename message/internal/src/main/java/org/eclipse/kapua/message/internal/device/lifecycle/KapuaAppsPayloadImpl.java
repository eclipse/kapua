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
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.message.device.lifecycle.KapuaAppsPayload;
import org.eclipse.kapua.message.internal.KapuaPayloadImpl;

public class KapuaAppsPayloadImpl extends KapuaPayloadImpl implements KapuaAppsPayload {
	
	private String uptime;
    private String displayName;
    private String modelName;
    private String modelId;
    private String partNumber;
    private String serialNumber;
    private String firmware;
    private String firmwareVersion;
    private String bios;
    private String biosVersion;
    private String os;
    private String osVersion;
    private String jvm;
    private String jvmVersion;
    private String jvmProfile;
    private String containerFramework;
    private String containerFrameworkVersion;
    private String applicationFramework;
    private String applicationFrameworkVersion;
    private String connectionInterface;
    private String connectionIp;
    private String acceptEncoding;
    private String applicationIdentifiers;
    private String availableProcessors;
    private String totalMemory;
    private String osArch;
    private String modemImei;
    private String modemImsi;
    private String modemIccid;

    public KapuaAppsPayloadImpl(String uptime,
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
    	this.uptime = uptime;
    	this.displayName = displayName;
    	this.modelName = modelName;
    	this.modelId = modelId;
    	this.partNumber = partNumber;
    	this.serialNumber = serialNumber;
    	this.firmware = firmware;
    	this.firmwareVersion = firmwareVersion;
    	this.bios = bios;
    	this.biosVersion = biosVersion;
    	this.os = os;
    	this.osVersion = osVersion;
    	this.jvm = jvm;
    	this.jvmVersion = jvmVersion;
    	this.jvmProfile = jvmProfile;
    	this.containerFramework = containerFramework;
    	this.containerFrameworkVersion = containerFrameworkVersion;
    	this.applicationFramework = applicationFramework;
    	this.applicationFrameworkVersion = applicationFrameworkVersion;
    	this.connectionInterface = connectionInterface;
    	this.connectionIp = connectionIp;
    	this.acceptEncoding = acceptEncoding;
    	this.applicationIdentifiers = applicationIdentifiers;
    	this.availableProcessors = availableProcessors;
    	this.totalMemory = totalMemory;
    	this.osArch = osArch;
    	this.modemImei = modemImei;
    	this.modemImsi = modemImsi;
    	this.modemIccid = modemIccid;
    }
    
    public String toDisplayString() {
    	//TODO to be implemented
    	return "";
    }

	public String getUptime() {
		return uptime;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getModelName() {
		return modelName;
	}

	public String getModelId() {
		return modelId;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public String getFirmware() {
		return firmware;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public String getBios() {
		return bios;
	}

	public String getBiosVersion() {
		return biosVersion;
	}

	public String getOs() {
		return os;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public String getJvm() {
		return jvm;
	}

	public String getJvmVersion() {
		return jvmVersion;
	}

	public String getJvmProfile() {
		return jvmProfile;
	}

	public String getContainerFramework() {
		return containerFramework;
	}

	public String getContainerFrameworkVersion() {
		return containerFrameworkVersion;
	}

	public String getApplicationFramework() {
		return applicationFramework;
	}

	public String getApplicationFrameworkVersion() {
		return applicationFrameworkVersion;
	}

	public String getConnectionInterface() {
		return connectionInterface;
	}

	public String getConnectionIp() {
		return connectionIp;
	}

	public String getAcceptEncoding() {
		return acceptEncoding;
	}

	public String getApplicationIdentifiers() {
		return applicationIdentifiers;
	}

	public String getAvailableProcessors() {
		return availableProcessors;
	}

	public String getTotalMemory() {
		return totalMemory;
	}

	public String getOsArch() {
		return osArch;
	}

	public String getModemImei() {
		return modemImei;
	}

	public String getModemImsi() {
		return modemImsi;
	}

	public String getModemIccid() {
		return modemIccid;
	}

}
