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
package org.eclipse.kapua.service.device.registry.internal;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCredentialsMode;
import org.eclipse.kapua.service.device.registry.DeviceEventType;
import org.eclipse.kapua.service.device.registry.DeviceStatus;

@Entity(name = "Device")
@Table(name = "dvc_device")
public class DeviceImpl extends AbstractKapuaUpdatableEntity implements Device {

    private static final long serialVersionUID = 7688047426522474413L;

    @Basic
    @Column(name = "client_id", updatable = false)
    private String clientId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "connection_id", nullable = true, updatable = false))
    })
    private KapuaEid connectionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeviceStatus status;

    @Basic
    @Column(name = "display_name")
    private String displayName;

    @Basic
    @Column(name = "last_event_on")
    private Date lastEventOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "last_event_type")
    private DeviceEventType lastEventType;

    @Basic
    @Column(name = "serial_number")
    private String serialNumber;

    @Basic
    @Column(name = "model_id")
    private String modelId;

    @Basic
    @Column(name = "imei")
    private String imei;

    @Basic
    @Column(name = "imsi")
    private String imsi;

    @Basic
    @Column(name = "iccid")
    private String iccid;

    @Basic
    @Column(name = "bios_version")
    private String biosVersion;

    @Basic
    @Column(name = "firmware_version")
    private String firmwareVersion;

    @Basic
    @Column(name = "os_version")
    private String osVersion;

    @Basic
    @Column(name = "jvm_version")
    private String jvmVersion;

    @Basic
    @Column(name = "osgi_framework_version")
    private String osgiFrameworkVersion;

    @Basic
    @Column(name = "app_framework_version")
    private String applicationFrameworkVersion;

    @Basic
    @Column(name = "app_identifiers")
    private String applicationIdentifiers;

    @Basic
    @Column(name = "accept_encoding")
    private String acceptEncoding;

    @Basic
    @Column(name = "custom_attribute_1")
    private String customAttribute1;

    @Basic
    @Column(name = "custom_attribute_2")
    private String customAttribute2;

    @Basic
    @Column(name = "custom_attribute_3")
    private String customAttribute3;

    @Basic
    @Column(name = "custom_attribute_4")
    private String customAttribute4;

    @Basic
    @Column(name = "custom_attribute_5")
    private String customAttribute5;

    @Column(name = "credentials_mode")
    private String deviceCredentialsMode;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "preferred_user_id"))
    })
    private KapuaEid preferredUserId;

    protected DeviceImpl() {
        super();
    }

    public DeviceImpl(KapuaId scopeId) {
        super(scopeId);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public KapuaId getConnectionId() {
        return connectionId;
    }

    @Override
    public void setConnectionId(KapuaId connectionId) {
        if (connectionId != null) {
            this.connectionId = new KapuaEid(connectionId.getId());
        }
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getLastEventOn() {
        return lastEventOn;
    }

    public void setLastEventOn(Date lastEventOn) {
        this.lastEventOn = lastEventOn;
    }

    public DeviceEventType getLastEventType() {
        return lastEventType;
    }

    public void setLastEventType(DeviceEventType lastEventType) {
        this.lastEventType = lastEventType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
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

    public DeviceCredentialsMode getCredentialsMode() {
        return deviceCredentialsMode != null ? DeviceCredentialsMode.valueOf(deviceCredentialsMode) : null;
    }

    public void setCredentialsMode(DeviceCredentialsMode deviceCredentialsMode) {
        if (deviceCredentialsMode != null) {
            this.deviceCredentialsMode = deviceCredentialsMode.name();
        }
    }

    public org.eclipse.kapua.model.id.KapuaId getPreferredUserId() {
        return preferredUserId;
    }

    public void setPreferredUserId(KapuaId preferredUserId) {
        this.preferredUserId = (KapuaEid) preferredUserId;
    }

}
