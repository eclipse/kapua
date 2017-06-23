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

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceStatus;

/**
 * {@link DeviceCreator} implementation.
 *
 * @since 1.0.0
 */
public class DeviceCreatorImpl extends AbstractKapuaEntityCreator<Device> implements DeviceCreator {

    private static final long serialVersionUID = 8628137091890107296L;

    private KapuaId groupId;
    private String clientId;
    private DeviceStatus status = DeviceStatus.ENABLED;
    private KapuaId connectionId;
    private KapuaId lastEventId;
    private String displayName;
    private String serialNumber;
    private String modelId;
    private String imei;
    private String imsi;
    private String iccid;
    private String biosVersion;
    private String firmwareVersion;
    private String osVersion;
    private String jvmVersion;
    private String osgiFrameworkVersion;
    private String applicationFrameworkVersion;
    private String connectionInterface;
    private String connectionIp;
    private String applicationIdentifiers;
    private String acceptEncoding;
    private Double gpsLongitude;
    private Double gpsLatitude;
    private String customAttribute1;
    private String customAttribute2;
    private String customAttribute3;
    private String customAttribute4;
    private String customAttribute5;

    /**
     * Constructor.
     *
     * @param scopeId
     * @since 1.0.0
     */
    protected DeviceCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(KapuaId groupId) {
        this.groupId = groupId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    @Override
    public KapuaId getConnectionId() {
        return connectionId;
    }

    @Override
    public void setConnectionId(KapuaId connectionId) {
        this.connectionId = connectionId;
    }

    @Override
    public KapuaId getLastEventId() {
        return lastEventId;
    }

    @Override
    public void setLastEventId(KapuaId lastEventId) {
        this.lastEventId = lastEventId;
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

    /**
     * Get the gps longitude
     *
     * @return
     */
    public Double getGpsLongitude() {
        return gpsLongitude;
    }

    /**
     * Set the gps longitude
     *
     * @param gpsLongitude
     */
    public void setGpsLongitude(Double gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    /**
     * Get the gps latitude
     *
     * @return
     */
    public Double getGpsLatitude() {
        return gpsLatitude;
    }

    /**
     * Set the gps latitude
     *
     * @param gpsLatitude
     */
    public void setGpsLatitude(Double gpsLatitude) {
        this.gpsLatitude = gpsLatitude;
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
}
