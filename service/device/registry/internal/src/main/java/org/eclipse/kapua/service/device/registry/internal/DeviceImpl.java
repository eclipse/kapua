/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.internal;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionImpl;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventImpl;
import org.eclipse.kapua.service.tag.Taggable;

/**
 * {@link Device} implementation.
 *
 * @since 1.0.0
 */
@Entity(name = "Device")
@Table(name = "dvc_device")
public class DeviceImpl extends AbstractKapuaUpdatableEntity implements Device, Taggable {

    private static final long serialVersionUID = 7688047426522474413L;

    @ElementCollection
    @CollectionTable(name = "dvc_device_tag", joinColumns = @JoinColumn(name = "device_id", referencedColumnName = "id"))
    // @Column(name = "tag_id", nullable = false)
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "tag_id", nullable = false, updatable = false))
    })
    private Set<KapuaEid> tagIds;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "group_id", nullable = true, updatable = true))
    })
    private KapuaEid groupId;

    @Basic
    @Column(name = "client_id", nullable = false, updatable = false)
    private String clientId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "connection_id", nullable = true, updatable = true))
    })
    private KapuaEid connectionId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connection_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DeviceConnectionImpl connection;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DeviceStatus status;

    @Basic
    @Column(name = "display_name")
    private String displayName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "last_event_id", nullable = true, updatable = true))
    })
    private KapuaEid lastEventId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_event_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DeviceEventImpl lastEvent;

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
    @Column(name = "connection_interface")
    private String connectionInterface;

    @Basic
    @Column(name = "connection_ip")
    private String connectionIp;

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

    /**
     * Constructor
     */
    protected DeviceImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public DeviceImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public void setTagIds(Set<KapuaId> tagIds) {
        this.tagIds = new HashSet<>();

        for (KapuaId id : tagIds) {
            this.tagIds.add(KapuaEid.parseKapuaId(id));
        }
    }

    @Override
    public Set<KapuaId> getTagIds() {
        Set<KapuaId> tagIds = new HashSet<>();

        if (this.tagIds != null) {
            for (KapuaId deviceTagId : this.tagIds) {
                tagIds.add(new KapuaEid(deviceTagId));
            }
        }

        return tagIds;
    }

    @Override
    public KapuaId getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(KapuaId groupId) {
        this.groupId = KapuaEid.parseKapuaId(groupId);
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public KapuaId getConnectionId() {
        return connectionId;
    }

    @Override
    public void setConnectionId(KapuaId connectionId) {
        this.connectionId = KapuaEid.parseKapuaId(connectionId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public DeviceConnection getConnection() {
        return connection;
    }

    public void setConnection(DeviceConnection connection) throws KapuaException {
        this.connection = connection != null ? new DeviceConnectionImpl(connection) : null;
    }

    @Override
    public DeviceStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public KapuaId getLastEventId() {
        return lastEventId;
    }

    @Override
    public void setLastEventId(KapuaId lastEventId) {
        this.lastEventId = KapuaEid.parseKapuaId(lastEventId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public DeviceEvent getLastEvent() {
        return lastEvent;
    }

    public void setLastEvent(DeviceEvent lastEvent) throws KapuaException {
        this.lastEvent = lastEvent != null ? new DeviceEventImpl(lastEvent) : null;
    }

    @Override
    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String getModelId() {
        return modelId;
    }

    @Override
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    @Override
    public String getImei() {
        return imei;
    }

    @Override
    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public String getImsi() {
        return imsi;
    }

    @Override
    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    @Override
    public String getIccid() {
        return iccid;
    }

    @Override
    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    @Override
    public String getBiosVersion() {
        return biosVersion;
    }

    @Override
    public void setBiosVersion(String biosVersion) {
        this.biosVersion = biosVersion;
    }

    @Override
    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    @Override
    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    @Override
    public String getOsVersion() {
        return osVersion;
    }

    @Override
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    @Override
    public String getJvmVersion() {
        return jvmVersion;
    }

    @Override
    public void setJvmVersion(String jvmVersion) {
        this.jvmVersion = jvmVersion;
    }

    @Override
    public String getOsgiFrameworkVersion() {
        return osgiFrameworkVersion;
    }

    @Override
    public void setOsgiFrameworkVersion(String osgiFrameworkVersion) {
        this.osgiFrameworkVersion = osgiFrameworkVersion;
    }

    @Override
    public String getApplicationFrameworkVersion() {
        return applicationFrameworkVersion;
    }

    @Override
    public void setApplicationFrameworkVersion(String applicationFrameworkVersion) {
        this.applicationFrameworkVersion = applicationFrameworkVersion;
    }

    @Override
    public String getConnectionInterface() {
        return connectionInterface;
    }

    @Override
    public void setConnectionInterface(String connectionInterface) {
        this.connectionInterface = connectionInterface;
    }

    @Override
    public String getConnectionIp() {
        return connectionIp;
    }

    @Override
    public void setConnectionIp(String connectionIp) {
        this.connectionIp = connectionIp;
    }

    @Override
    public String getApplicationIdentifiers() {
        return applicationIdentifiers;
    }

    @Override
    public void setApplicationIdentifiers(String applicationIdentifiers) {
        this.applicationIdentifiers = applicationIdentifiers;
    }

    @Override
    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    @Override
    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    @Override
    public String getCustomAttribute1() {
        return customAttribute1;
    }

    @Override
    public void setCustomAttribute1(String customAttribute1) {
        this.customAttribute1 = customAttribute1;
    }

    @Override
    public String getCustomAttribute2() {
        return customAttribute2;
    }

    @Override
    public void setCustomAttribute2(String customAttribute2) {
        this.customAttribute2 = customAttribute2;
    }

    @Override
    public String getCustomAttribute3() {
        return customAttribute3;
    }

    @Override
    public void setCustomAttribute3(String customAttribute3) {
        this.customAttribute3 = customAttribute3;
    }

    @Override
    public String getCustomAttribute4() {
        return customAttribute4;
    }

    @Override
    public void setCustomAttribute4(String customAttribute4) {
        this.customAttribute4 = customAttribute4;
    }

    @Override
    public String getCustomAttribute5() {
        return customAttribute5;
    }

    @Override
    public void setCustomAttribute5(String customAttribute5) {
        this.customAttribute5 = customAttribute5;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
