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
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.kapua.app.console.client.util.DateUtils;
import org.eclipse.kapua.app.console.shared.model.GwtDeviceQueryPredicates.GwtDeviceConnectionStatus;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtDevice extends GwtUpdatableEntityModel implements Serializable {

    private static final long serialVersionUID = -7670819589361945552L;

    public enum GwtDeviceApplication implements IsSerializable {

        APP_CONFIGURATION("CONF-V1"), //
        APP_COMMAND("CMD-V1"), //
        APP_DEPLOY_V1("DEPLOY-V1"), //
        APP_DEPLOY_V2("DEPLOY-V2"), //
        APP_PROV_V1("PROV-V1"), //
        APP_PROV_V2("PROV-V2"), //
        APP_PROV_V3("PROV-V3"), //
        APP_VPN_CLIENT("VPNCLIENT-V1"), //
        APP_CERTIFICATES("CERT-V1");

        private final String appId;

        GwtDeviceApplication(String appId) {
            this.appId = appId;
        }

        public String getAppId() {
            return appId;
        }
    }

    public enum GwtDeviceCredentialsTight implements IsSerializable {
        LOOSE("Unbound"), //
        STRICT("Device-bound"), //
        INHERITED("Account Default");

        private final String label;

        GwtDeviceCredentialsTight(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public static GwtDeviceCredentialsTight getEnumFromLabel(String label) {
            GwtDeviceCredentialsTight gdct = null;

            for (GwtDeviceCredentialsTight e : GwtDeviceCredentialsTight.values()) {
                if (e.getLabel().equals(label)) {
                    gdct = e;
                }
            }

            return gdct;
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("lastEventOnFormatted".equals(property)) {
            Date lastEventOn = getLastEventOn();
            if (lastEventOn != null)
                return (X) (DateUtils.formatDateTime(lastEventOn));
            return (X) "";
        } else if ("uptimeFormatted".equals(property)) {
            if (getUptime() != null) {
                return (X) String.valueOf(getUptime());
            } else {
                return null;
            }
        } else if ("credentialsTightEnum".equals(property)) {
            return (X) GwtDeviceCredentialsTight.valueOf(getCredentialsTight());
        } else if ("deviceConnectionStatusEnum".equals(property)) {
            return (X) GwtDeviceConnectionStatus.valueOf(getGwtDeviceConnectionStatus());
        } else {
            return super.get(property);
        }
    }

    public GwtDevice() {
    }

    public String getClientId() {
        return (String) get("clientId");
    }

    public String getUnescapedClientId() {
        return (String) getUnescaped("clientId");
    }

    public void setClientId(String clientId) {
        set("clientId", clientId);
    }

    public Long getUptime() {
        return (Long) get("uptime");
    }

    public String getUptimeFormatted() {
        return (String) get("uptimeFormatted");
    }

    public void setUptime(Long uptime) {
        set("uptime", uptime);
    }

    public String getGwtDeviceStatus() {
        return (String) get("gwtDeviceStatus");
    }

    public void setGwtDeviceStatus(String gwtDeviceStatus) {
        set("gwtDeviceStatus", gwtDeviceStatus);
    }

    public String getGwtDeviceConnectionStatus() {
        return get("deviceConnectionStatus");
    }

    public GwtDeviceConnectionStatus getGwtDeviceConnectionStatusEnum() {
        return get("deviceConnectionStatusEnum");
    }

    public void setGwtDeviceConnectionStatus(String deviceConnectionStatus) {
        set("deviceConnectionStatus", deviceConnectionStatus);
    }

    public String getDisplayName() {
        return (String) get("displayName");
    }

    public String getUnescapedDisplayName() {
        return (String) getUnescaped("displayName");
    }

    public void setDisplayName(String displayName) {
        set("displayName", displayName);
    }

    public String getModelName() {
        return (String) get("modelName");
    }

    public void setModelName(String modelName) {
        set("modelName", modelName);
    }

    public String getModelId() {
        return (String) get("modelId");
    }

    public void setModelId(String modelId) {
        set("modelId", modelId);
    }

    public String getPartNumber() {
        return (String) get("partNumber");
    }

    public void setPartNumber(String partNumber) {
        set("partNumber", partNumber);
    }

    public String getSerialNumber() {
        return (String) get("serialNumber");
    }

    public void setSerialNumber(String serialNumber) {
        set("serialNumber", serialNumber);
    }

    public String getAvailableProcessors() {
        return (String) get("availableProcessors");
    }

    public void setAvailableProcessors(String availableProcessors) {
        set("availableProcessors", availableProcessors);
    }

    public String getTotalMemory() {
        return (String) get("totalMemory");
    }

    public void setTotalMemory(String totalMemory) {
        set("totalMemory", totalMemory);
    }

    public String getFirmwareVersion() {
        return (String) get("firmwareVersion");
    }

    public void setFirmwareVersion(String firmwareVersion) {
        set("firmwareVersion", firmwareVersion);
    }

    public String getBiosVersion() {
        return (String) get("biosVersion");
    }

    public void setBiosVersion(String biosVersion) {
        set("biosVersion", biosVersion);
    }

    public String getOs() {
        return (String) get("os");
    }

    public void setOs(String os) {
        set("os", os);
    }

    public String getOsVersion() {
        return (String) get("osVersion");
    }

    public void setOsVersion(String osVersion) {
        set("osVersion", osVersion);
    }

    public String getOsArch() {
        return (String) get("osArch");
    }

    public void setOsArch(String osArch) {
        set("osArch", osArch);
    }

    public String getJvmName() {
        return (String) get("jvmName");
    }

    public void setJvmName(String jvmName) {
        set("jvmName", jvmName);
    }

    public String getJvmVersion() {
        return (String) get("jvmVersion");
    }

    public void setJvmVersion(String jvmVersion) {
        set("jvmVersion", jvmVersion);
    }

    public String getJvmProfile() {
        return (String) get("jvmProfile");
    }

    public void setJvmProfile(String jvmProfile) {
        set("jvmProfile", jvmProfile);
    }

    public String getEsfKuraVersion() {
        return (String) get("esfKuraVersion");
    }

    public void setEsfKuraVersion(String esfKuraVersion) {
        set("esfKuraVersion", esfKuraVersion);
    }

    public String getOsgiFramework() {
        return (String) get("osgiFramework");
    }

    public void setOsgiFramework(String osgiFramework) {
        set("osgiFramework", osgiFramework);
    }

    public String getOsgiVersion() {
        return (String) get("osgiVersion");
    }

    public void setOsgiVersion(String osgiFrameworkVersion) {
        set("osgiVersion", osgiFrameworkVersion);
    }

    public String getConnectionInterface() {
        return (String) get("connectionInterface");
    }

    public void setConnectionInterface(String connectionInterface) {
        set("connectionInterface", connectionInterface);
    }

    public String getConnectionIp() {
        return (String) get("connectionIp");
    }

    public void setConnectionIp(String connectionIp) {
        set("connectionIp", connectionIp);
    }

    public String getAcceptEncoding() {
        return (String) get("acceptEncoding");
    }

    public void setAcceptEncoding(String acceptEncoding) {
        set("acceptEncoding", acceptEncoding);
    }

    public String getApplicationIdentifiers() {
        return (String) get("applicationIdentifiers");
    }

    public void setApplicationIdentifiers(String applicationIdentifiers) {
        set("applicationIdentifiers", applicationIdentifiers);
    }

    public Double getGpsLatitude() {
        return (Double) get("gpsLatitude");
    }

    public void setGpsLatitude(Double gpsLatitude) {
        set("gpsLatitude", gpsLatitude);
    }

    public Double getGpsLongitude() {
        return (Double) get("gpsLongitude");
    }

    public void setGpsLongitude(Double gpsLongitude) {
        set("gpsLongitude", gpsLongitude);
    }

    public Double getGpsAltitude() {
        return (Double) get("gpsAltitude");
    }

    public void setGpsAltitude(Double gpsAltitude) {
        set("gpsAltitude", gpsAltitude);
    }

    public String getGpsAddress() {
        return (String) get("gpsAddress");
    }

    public void setGpsAddress(String gpsAddress) {
        set("gpsAddress", gpsAddress);
    }

    public Date getLastEventOn() {
        return (Date) get("lastEventOn");
    }

    public String getLastEventOnFormatted() {
        return (String) get("lastEventOnFormatted");
    }

    public void setLastEventOn(Date lastEventDate) {
        set("lastEventOn", lastEventDate);
    }

    public String getLastEventType() {
        return (String) get("lastEventType");
    }

    public void setLastEventType(String lastEventType) {
        set("lastEventType", lastEventType);
    }

    public String getDeviceUserId() {
        return (String) get("deviceUserId");
    }

    public void setDeviceUserId(String deviceUserId) {
        set("deviceUserId", deviceUserId);
    }

    public String getIccid() {
        return (String) get("iccid");
    }

    public void setIccid(String iccid) {
        set("iccid", iccid);
    }

    public String getImei() {
        return (String) get("imei");
    }

    public void setImei(String imei) {
        set("imei", imei);

    }

    public String getImsi() {
        return (String) get("imsi");
    }

    public void setCertificateCommonName(String certificateCommonName) {
        set("certificateCommonName", certificateCommonName);

    }

    public String getCertificateCommonName() {
        return (String) get("certificateCommonName");
    }

    public void setImsi(String imsi) {
        set("imsi", imsi);
    }

    public Long getBrokerClusterId() {
        return (Long) get("brokerClusterId");
    }

    public void setBrokerClusterId(long brokerClusterId) {
        set("brokerClusterId", brokerClusterId);
    }

    public String getBrokerNodeId() {
        return (String) get("brokerNodeId");
    }

    public void setBrokerNodeId(String brokerNodeId) {
        set("brokerNodeId", brokerNodeId);
    }

    public String getCustomAttribute1() {
        return (String) get("customAttribute1");
    }

    public String getUnescapedCustomAttribute1() {
        return (String) getUnescaped("customAttribute1");
    }

    public void setCustomAttribute1(String customAttribute1) {
        set("customAttribute1", customAttribute1);
    }

    public String getCustomAttribute2() {
        return (String) get("customAttribute2");
    }

    public String getUnescapedCustomAttribute2() {
        return (String) getUnescaped("customAttribute2");
    }

    public void setCustomAttribute2(String customAttribute2) {
        set("customAttribute2", customAttribute2);
    }

    public String getCustomAttribute3() {
        return (String) get("customAttribute3");
    }

    public String getUnescapedCustomAttribute3() {
        return (String) getUnescaped("customAttribute3");
    }

    public void setCustomAttribute3(String customAttribute3) {
        set("customAttribute3", customAttribute3);
    }

    public String getCustomAttribute4() {
        return (String) get("customAttribute4");
    }

    public String getUnescapedCustomAttribute4() {
        return (String) getUnescaped("customAttribute4");
    }

    public void setCustomAttribute4(String customAttribute4) {
        set("customAttribute4", customAttribute4);
    }

    public String getCustomAttribute5() {
        return (String) get("customAttribute5");
    }

    public String getUnescapedCustomAttribute5() {
        return (String) getUnescaped("customAttribute5");
    }

    public void setCustomAttribute5(String customAttribute5) {
        set("customAttribute5", customAttribute5);
    }

    public String getCredentialsTight() {
        return (String) get("credentialsTight");
    }

    public GwtDeviceCredentialsTight getCredentialTightEnum() {
        return get("credentialsTightEnum");
    }

    public void setCredentialsTight(String credentialsTight) {
        set("credentialsTight", credentialsTight);
    }

    public boolean getCredentialsAllowChange() {
        return get("credentialsAllowChange");
    }

    public void setCredentialsAllowChange(boolean credentialsAllowChange) {
        set("credentialsAllowChange", credentialsAllowChange);
    }

    public boolean isOnline() {
        return "CONNECTED".equals(getGwtDeviceConnectionStatus());
    }

    // CertificateID

    public Long getSignedCertificateId() {
        return (Long) get("signedCertificateId");
    }

    public void setSignedCertificateId(Long accountId) {
        set("signedCertificateId", accountId);
    }

    //
    // Utility methods
    //

    public boolean hasApplication(GwtDeviceApplication application) {
        String applicationIdentifiers = getApplicationIdentifiers();

        if (applicationIdentifiers == null || applicationIdentifiers.isEmpty()) {
            return false;
        }

        for (String deviceApplication : applicationIdentifiers.split(",")) {
            if (deviceApplication.equals(application.getAppId())) {
                return true;
            }
        }

        return false;
    }
}
