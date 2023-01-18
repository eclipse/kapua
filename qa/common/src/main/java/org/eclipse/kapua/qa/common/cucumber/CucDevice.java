/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.common.cucumber;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.DeviceStatus;

import java.math.BigInteger;

/**
 * Data object used in Gherkin to input Device parameters.
 * The data setters intentionally use only cucumber-friendly data types and
 * generate the resulting Kapua types internally.
 */
public class CucDevice {

    Integer scopeId;
    KapuaId kScopeId;
    Integer groupId;
    KapuaId kGroupId;
    Integer connectionId;
    KapuaId kConnectionId;
    Integer preferredUserId;
    KapuaId kPreferredUserId;
    String clientId;
    String displayName;
    String status;
    DeviceStatus kStatus;
    String modelId;
    String modelName;
    String serialNumber;
    String imei;
    String imsi;
    String iccid;
    String biosVersion;
    String firmwareVersion;
    String osVersion;
    String jvmVersion;
    String osgiFrameworkVersion;
    String applicationFrameworkVersion;
    String connectionInterface;
    String connectionIp;
    String applicationIdentifiers;
    String acceptEncoding;
    String customAttribute1;
    String customAttribute2;
    String customAttribute3;
    String customAttribute4;
    String customAttribute5;

    public CucDevice(Integer scopeId,
                     Integer groupId,
                     Integer connectionId,
                     Integer preferredUserId,
                     String clientId,
                     String displayName,
                     String status,
                     String modelId,
                     String modelName,
                     String serialNumber,
                     String imei,
                     String imsi,
                     String iccid,
                     String biosVersion,
                     String firmwareVersion,
                     String osVersion,
                     String jvmVersion,
                     String osgiFrameworkVersion,
                     String applicationFrameworkVersion,
                     String connectionInterface,
                     String connectionIp,
                     String applicationIdentifiers,
                     String acceptEncoding,
                     String customAttribute1,
                     String customAttribute2,
                     String customAttribute3,
                     String customAttribute4,
                     String customAttribute5) {
        this.scopeId = scopeId;
        this.groupId = groupId;
        this.connectionId = connectionId;
        this.preferredUserId = preferredUserId;
        this.clientId = clientId;
        this.displayName = displayName;
        this.status = status;
        this.modelId = modelId;
        this.modelName = modelName;
        this.serialNumber = serialNumber;
        this.imei = imei;
        this.imsi = imsi;
        this.iccid = iccid;
        this.biosVersion = biosVersion;
        this.firmwareVersion = firmwareVersion;
        this.osVersion = osVersion;
        this.jvmVersion = jvmVersion;
        this.osgiFrameworkVersion = osgiFrameworkVersion;
        this.applicationFrameworkVersion = applicationFrameworkVersion;
        this.connectionInterface = connectionInterface;
        this.connectionIp = connectionIp;
        this.applicationIdentifiers = applicationIdentifiers;
        this.acceptEncoding = acceptEncoding;
        this.customAttribute1 = customAttribute1;
        this.customAttribute2 = customAttribute2;
        this.customAttribute3 = customAttribute3;
        this.customAttribute4 = customAttribute4;
        this.customAttribute5 = customAttribute5;

        setStatus(this.status);
    }

    public void parse() {
        if (scopeId != null) {
            kScopeId = new KapuaEid(BigInteger.valueOf(scopeId));
        }

        if (groupId != null) {
            kGroupId = new KapuaEid(BigInteger.valueOf(groupId));
        }

        if (connectionId != null) {
            kConnectionId = new KapuaEid(BigInteger.valueOf(connectionId));
        }

        if (preferredUserId != null) {
            kPreferredUserId = new KapuaEid(BigInteger.valueOf(preferredUserId));
        }
        setStatus(this.status);
    }

    public void setStatus(String status) {
        if (status != null) {
            switch (status.trim().toUpperCase()) {
                case "DISABLED":
                    kStatus = DeviceStatus.DISABLED;
                    break;
                case "ENABLED":
                    kStatus = DeviceStatus.ENABLED;
                    break;
                default:
                    kStatus = null;
                    break;
            }
        }
    }

    public KapuaId getScopeId() {
        return kScopeId;
    }

    public void setScopeId(KapuaId scopeId) {
        kScopeId = scopeId;
    }

    public KapuaId getGroupId() {
        return kGroupId;
    }

    public void setGroupId(KapuaId groupId) {
        kGroupId = groupId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public KapuaId getConnectionId() {
        return kConnectionId;
    }

    public void setConnectionId(KapuaId connectionId) {
        kConnectionId = connectionId;
    }

    public KapuaId getPreferredUserId() {
        return kPreferredUserId;
    }

    public void setPreferredUserId(KapuaId preferredUserId) {
        kPreferredUserId = preferredUserId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public DeviceStatus getStatus() {
        return kStatus;
    }

    public void setStatus(DeviceStatus status) {
        this.kStatus = status;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
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

    public String getConnectionInterface() {
        return connectionInterface;
    }

    public void setConnectionInterface(String connectionInterface) {
        this.connectionInterface = connectionInterface;
    }

    public String getConnectionIp() {
        return connectionIp;
    }

    public void setConnectionIp(String connectionIp) {
        this.connectionIp = connectionIp;
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

    public String getCustomAttribute1() {
        return customAttribute1;
    }

    public void setCustomAttribute1(String customAttribute1) {
        this.customAttribute1 = customAttribute1;
    }

    public String getCustomAttribute2() {
        return customAttribute2;
    }

    public void setCustomAttribute2(String customAttribute2) {
        this.customAttribute2 = customAttribute2;
    }

    public String getCustomAttribute3() {
        return customAttribute3;
    }

    public void setCustomAttribute3(String customAttribute3) {
        this.customAttribute3 = customAttribute3;
    }

    public String getCustomAttribute4() {
        return customAttribute4;
    }

    public void setCustomAttribute4(String customAttribute4) {
        this.customAttribute4 = customAttribute4;
    }

    public String getCustomAttribute5() {
        return customAttribute5;
    }

    public void setCustomAttribute5(String customAttribute5) {
        this.customAttribute5 = customAttribute5;
    }
}
