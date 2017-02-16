/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import java.math.BigInteger;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.DeviceCredentialsMode;
import org.eclipse.kapua.service.device.registry.DeviceStatus;

/**
 * Data object used in Gherkin to input Device parameters.
 * The data setters intentionally use only cucumber-friendly data types and
 * generate the resulting Kapua types internally.
 */
public class CucDevice {

    KapuaId scopeId = null;
    KapuaId groupId = null;
    KapuaId connectionId = null;
    KapuaId preferredUserId = null;
    String clientId = null;
    String displayName = null;
    DeviceStatus status = null;
    String modelId = null;
    String serialNumber = null;
    String imei = null;
    String imsi = null;
    String iccid = null;
    String biosVersion = null;
    String firmwareVersion = null;
    String osVersion = null;
    String jvmVersion = null;
    String osgiFrameworkVersion = null;
    String applicationFrameworkVersion = null;
    String applicationIdentifiers = null;
    String acceptEncoding = null;
    DeviceCredentialsMode credentialsMode = null;

    public KapuaId getScopeId() {
        return scopeId;
    }

    public void setScopeId(long scopeId) {
        this.scopeId = new KapuaEid(BigInteger.valueOf(scopeId));
    }

    public KapuaId getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = new KapuaEid(BigInteger.valueOf(groupId));
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public KapuaId getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(long connectionId) {
        this.connectionId = new KapuaEid(BigInteger.valueOf(connectionId));
    }

    public KapuaId getPreferredUserId() {
        return preferredUserId;
    }

    public void setPreferredUserId(long preferredUserId) {
        this.preferredUserId = new KapuaEid(BigInteger.valueOf(preferredUserId));
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {

        this.status = null;

        switch (status.trim().toUpperCase()) {
        case "DISABLED":
            this.status = DeviceStatus.DISABLED;
            break;
        case "ENABLED":
            this.status = DeviceStatus.ENABLED;
            break;
        }
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getBiosVersion() {
        return biosVersion;
    }

    public void setBiosVersion(String biosVersion) {
        this.biosVersion = biosVersion;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getJvmVersion() {
        return jvmVersion;
    }

    public void setJvmVersion(String jvmVersion) {
        this.jvmVersion = jvmVersion;
    }

    public String getOsgiFrameworkVersion() {
        return osgiFrameworkVersion;
    }

    public void setOsgiFrameworkVersion(String osgiFrameworkVersion) {
        this.osgiFrameworkVersion = osgiFrameworkVersion;
    }

    public String getApplicationFrameworkVersion() {
        return applicationFrameworkVersion;
    }

    public void setApplicationFrameworkVersion(String applicationFrameworkVersion) {
        this.applicationFrameworkVersion = applicationFrameworkVersion;
    }

    public String getApplicationIdentifiers() {
        return applicationIdentifiers;
    }

    public void setApplicationIdentifiers(String applicationIdentifiers) {
        this.applicationIdentifiers = applicationIdentifiers;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public DeviceCredentialsMode getCredentialsMode() {
        return credentialsMode;
    }

    public void setCredentialsMode(String credentialsMode) {

        switch (credentialsMode.trim().toUpperCase()) {
        case "INHERITED":
            this.credentialsMode = DeviceCredentialsMode.INHERITED;
            break;
        case "LOOSE":
            this.credentialsMode = DeviceCredentialsMode.LOOSE;
            break;
        case "STRICT":
            this.credentialsMode = DeviceCredentialsMode.STRICT;
            break;
        default:
            break;
        }
    }
}
