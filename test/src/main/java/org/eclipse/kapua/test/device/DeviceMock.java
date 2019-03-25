/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.test.device;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;

import java.math.BigInteger;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

public class DeviceMock implements Device {

    private static final long serialVersionUID = -7521204246371846263L;

    private static long longId = 1;

    private KapuaEid id;
    private KapuaId scopeId;
    private String clientId;

    public DeviceMock(KapuaId scopeId, String clientId) {
        setId(new KapuaEid(BigInteger.valueOf(longId++)));
        setScopeId(scopeId);
        setClientId(clientId);
    }

    @Override
    public KapuaId getId() {
        return this.id;
    }

    @Override
    public void setId(KapuaId id) {
        this.id = KapuaEid.parseKapuaId(id);
    }

    @Override
    public KapuaId getScopeId() {
        return this.scopeId;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = KapuaEid.parseKapuaId(scopeId);
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public void setTagIds(Set<KapuaId> tagIds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<KapuaId> getTagIds() {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaId getGroupId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setGroupId(KapuaId groupId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaId getConnectionId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setConnectionId(KapuaId connectionId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <C extends DeviceConnection> C getConnection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public DeviceStatus getStatus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatus(DeviceStatus status) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getDisplayName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDisplayName(String diplayName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaId getLastEventId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLastEventId(KapuaId lastEventId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E extends DeviceEvent> E getLastEvent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSerialNumber() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSerialNumber(String serialNumber) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getModelId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setModelId(String modelId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getModelName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setModelName(String modelName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getImei() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setImei(String imei) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getImsi() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setImsi(String imsi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getIccid() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIccid(String iccid) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getBiosVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBiosVersion(String biosVersion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFirmwareVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFirmwareVersion(String firmwareVersion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOsVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOsVersion(String osVersion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJvmVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setJvmVersion(String jvmVersion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOsgiFrameworkVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOsgiFrameworkVersion(String osgiFrameworkVersion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getApplicationFrameworkVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setApplicationFrameworkVersion(String appFrameworkVersion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getConnectionInterface() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setConnectionInterface(String connectionInterface) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getConnectionIp() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setConnectionIp(String connectionIp) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getApplicationIdentifiers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setApplicationIdentifiers(String applicationIdentifiers) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAcceptEncoding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAcceptEncoding(String acceptEncoding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCustomAttribute1() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCustomAttribute1(String customAttribute1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCustomAttribute2() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCustomAttribute2(String customAttribute2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCustomAttribute3() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCustomAttribute3(String customAttribute3) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCustomAttribute4() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCustomAttribute4(String customAttribute4) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCustomAttribute5() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCustomAttribute5(String customAttribute5) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getModifiedOn() {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaId getModifiedBy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getOptlock() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setOptlock(int optlock) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getEntityAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEntityAttributes(Properties props) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Properties getEntityProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setEntityProperties(Properties props) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getCreatedOn() {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaId getCreatedBy() {
        throw new UnsupportedOperationException();
    }
}
