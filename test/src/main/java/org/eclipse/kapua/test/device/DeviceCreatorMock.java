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
package org.eclipse.kapua.test.device;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceStatus;

public class DeviceCreatorMock implements DeviceCreator {

    private KapuaId scopeId;
    private String clientId;

    public DeviceCreatorMock(KapuaId scopeId, String clientId) {
        this.scopeId = scopeId;
        this.clientId = clientId;
    }

    @Override
    public KapuaId getScopeId() {
        return this.scopeId;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
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
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDisplayName(String displayName) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getSerialNumber() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSerialNumber(String serialNumber) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getModelId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setModelId(String modelId) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getImei() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setImei(String imei) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getImsi() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setImsi(String imsi) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getIccid() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setIccid(String iccid) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getBiosVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setBiosVersion(String biosVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getFirmwareVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setFirmwareVersion(String firmwareVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getOsVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOsVersion(String osVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getJvmVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setJvmVersion(String jvmVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getOsgiFrameworkVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOsgiFrameworkVersion(String osgiFrameworkVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getApplicationFrameworkVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setApplicationFrameworkVersion(String appFrameworkVersion) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getConnectionInterface() {
        return null;
    }

    @Override
    public void setConnectionInterface(String connectionInterface) {

    }

    @Override
    public String getConnectionIp() {
        return null;
    }

    @Override
    public void setConnectionIp(String connectionIp) {

    }

    @Override
    public String getApplicationIdentifiers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setApplicationIdentifiers(String applicationIdentifiers) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getAcceptEncoding() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAcceptEncoding(String acceptEncoding) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getCustomAttribute1() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCustomAttribute1(String customAttribute1) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getCustomAttribute2() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCustomAttribute2(String customAttribute2) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getCustomAttribute3() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCustomAttribute3(String customAttribute3) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getCustomAttribute4() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCustomAttribute4(String customAttribute4) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getCustomAttribute5() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCustomAttribute5(String customAttribute5) {
        // TODO Auto-generated method stub

    }

    @Override
    public DeviceStatus getStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setStatus(DeviceStatus status) {
        // TODO Auto-generated method stub

    }

    @Override
    public KapuaId getConnectionId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setConnectionId(KapuaId connectionId) {
        // TODO Auto-generated method stub

    }

    @Override
    public KapuaId getGroupId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setGroupId(KapuaId groupId) {
        // TODO Auto-generated method stub

    }

    @Override
    public KapuaId getLastEventId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLastEventId(KapuaId lastEventId) {
        // TODO Auto-generated method stub

    }

}
