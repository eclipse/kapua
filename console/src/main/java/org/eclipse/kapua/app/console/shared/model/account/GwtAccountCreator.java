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
package org.eclipse.kapua.app.console.shared.model.account;

import java.util.Date;

import org.eclipse.kapua.app.console.shared.model.GwtDevice.GwtDeviceCredentialsTight;
import org.eclipse.kapua.app.console.shared.model.GwtEntityCreator;

public class GwtAccountCreator extends GwtEntityCreator {

    private static final long serialVersionUID = 3644046497789586781L;

    private String accountName;
    private String accountPassword;
    private Long brokerClusterId;
    private Long vpnServerId;
    private Boolean lockoutPolicyEnabled;
    private Integer lockoutPolicyMaxFailures;
    private Integer lockoutPolicyResetAfter;
    private Integer lockoutPolicyLockDuration;
    private String organizationName;
    private String organizationPersonName;
    private String organizationEmail;
    private String organizationPhoneNumber;
    private String organizationAddressLine1;
    private String organizationAddressLine2;
    private String organizationCity;
    private String organizationZipPostCode;
    private String organizationStateProvinceCounty;
    private String organizationCountry;
    private Date expirationDate;

    // Health check monitor
    private long healthCheckInterval;
    private long mqttThreshold;
    private long restThreshold;
    private long restCommandThreshold;

    // Tight Coupling
    private boolean deviceNewAllowUnprovisioned;
    private GwtDeviceCredentialsTight deviceDefaultCredentialsTight;

    // Service plan
    private int maxNumberOfDevices;
    private String parentAccountId;
    private int maxNumberChildAccounts;
    private int maxNumberProvisionRequests;
    private int maxNumberDeviceJobs;
    private int maxNumberVpnConnections;
    private long txByteLimit;
    private long rxByteLimit;
    private boolean dataStorageEnabled;
    private int dataTimeToLive;
    private String dataIndexBy;
    private String metricsIndexBy;
    private int maxNumberOfRules;

    // SSL Auth
    private boolean simpleConnection;
    private boolean sslConnection;
    private boolean MutualSslConnection;

    public GwtAccountCreator() {
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationPersonName() {
        return organizationPersonName;
    }

    public void setOrganizationPersonName(String organizationPersonName) {
        this.organizationPersonName = organizationPersonName;
    }

    public String getOrganizationEmail() {
        return organizationEmail;
    }

    public void setOrganizationEmail(String organizationEmail) {
        this.organizationEmail = organizationEmail;
    }

    public String getOrganizationPhoneNumber() {
        return organizationPhoneNumber;
    }

    public void setOrganizationPhoneNumber(String organizationPhoneNumber) {
        this.organizationPhoneNumber = organizationPhoneNumber;
    }

    public String getOrganizationAddressLine1() {
        return organizationAddressLine1;
    }

    public void setOrganizationAddressLine1(String organizationAddressLine1) {
        this.organizationAddressLine1 = organizationAddressLine1;
    }

    public String getOrganizationAddressLine2() {
        return organizationAddressLine2;
    }

    public void setOrganizationAddressLine2(String organizationAddressLine2) {
        this.organizationAddressLine2 = organizationAddressLine2;
    }

    public String getOrganizationCity() {
        return organizationCity;
    }

    public void setOrganizationCity(String organizationCity) {
        this.organizationCity = organizationCity;
    }

    public String getOrganizationZipPostCode() {
        return organizationZipPostCode;
    }

    public void setOrganizationZipPostCode(String organizationZipPostCode) {
        this.organizationZipPostCode = organizationZipPostCode;
    }

    public String getOrganizationStateProvinceCounty() {
        return organizationStateProvinceCounty;
    }

    public void setOrganizationStateProvinceCounty(String organizationStateProvinceCounty) {
        this.organizationStateProvinceCounty = organizationStateProvinceCounty;
    }

    public String getOrganizationCountry() {
        return organizationCountry;
    }

    public void setOrganizationCountry(String organizationCountry) {
        this.organizationCountry = organizationCountry;
    }

    public Boolean getLockoutPolicyEnabled() {
        return lockoutPolicyEnabled;
    }

    public void setLockoutPolicyEnabled(Boolean lockoutPolicyEnabled) {
        this.lockoutPolicyEnabled = lockoutPolicyEnabled;
    }

    public Integer getLockoutPolicyMaxFailures() {
        return lockoutPolicyMaxFailures;
    }

    public void setLockoutPolicyMaxFailures(Integer lockoutPolicyMaxFailures) {
        this.lockoutPolicyMaxFailures = lockoutPolicyMaxFailures;
    }

    public Integer getLockoutPolicyResetAfter() {
        return lockoutPolicyResetAfter;
    }

    public void setLockoutPolicyResetAfter(Integer lockoutPolicyResetAfter) {
        this.lockoutPolicyResetAfter = lockoutPolicyResetAfter;
    }

    public Integer getLockoutPolicyLockDuration() {
        return lockoutPolicyLockDuration;
    }

    public void setLockoutPolicyLockDuration(Integer lockoutPolicyLockDuration) {
        this.lockoutPolicyLockDuration = lockoutPolicyLockDuration;
    }

    public Long getBrokerClusterId() {
        return brokerClusterId;
    }

    public void setBrokerClusterId(Long brokerClusterId) {
        this.brokerClusterId = brokerClusterId;
    }

    public Long getVpnServerId() {
        return vpnServerId;
    }

    public void setVpnServerId(Long vpnServerId) {
        this.vpnServerId = vpnServerId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public long getHealthCheckInterval() {
        return healthCheckInterval;
    }

    public void setHealthCheckInterval(long healthCheckInterval) {
        this.healthCheckInterval = healthCheckInterval;
    }

    public int getMaxNumberOfDevices() {
        return maxNumberOfDevices;
    }

    public void setMaxNumberOfDevices(int maxNumberOfDevices) {
        this.maxNumberOfDevices = maxNumberOfDevices;
    }

    public String getParentAccountId() {
        return parentAccountId;
    }

    public void setParentAccountId(String parentAccountId) {
        this.parentAccountId = parentAccountId;
    }

    public int getMaxNumberChildAccounts() {
        return maxNumberChildAccounts;
    }

    public void setMaxNumberChildAccounts(int maxNumberChildAccounts) {
        this.maxNumberChildAccounts = maxNumberChildAccounts;
    }

    public int getMaxNumberProvisionRequests() {
        return maxNumberProvisionRequests;
    }

    public int getMaxNumberDeviceJobs() {
        return maxNumberDeviceJobs;
    }

    public void setMaxNumberDeviceJobs(int maxNumberDeviceJobs) {
        this.maxNumberDeviceJobs = maxNumberDeviceJobs;
    }

    public void setMaxNumberProvisionRequest(int maxNumberProvisionRequests) {
        this.maxNumberProvisionRequests = maxNumberProvisionRequests;
    }

    public int getMaxNumberVpnConnections() {
        return maxNumberVpnConnections;
    }

    public void setMaxNumberVpnConnections(int maxNumberVpnConnections) {
        this.maxNumberVpnConnections = maxNumberVpnConnections;
    }

    public long getTxByteLimit() {
        return txByteLimit;
    }

    public void setTxByteLimit(long txByteLimit) {
        this.txByteLimit = txByteLimit;
    }

    public long getRxByteLimit() {
        return rxByteLimit;
    }

    public void setRxByteLimit(long rxByteLimit) {
        this.rxByteLimit = rxByteLimit;
    }

    public boolean getDataStorageEnabled() {
        return dataStorageEnabled;
    }

    public void setDataStorageEnabled(boolean dataStorageEnabled) {
        this.dataStorageEnabled = dataStorageEnabled;
    }

    public int getDataTimeToLive() {
        return dataTimeToLive;
    }

    public void setDataTimeToLive(int dataTimeToLive) {
        this.dataTimeToLive = dataTimeToLive;
    }

    public String getDataIndexBy() {
        return dataIndexBy;
    }

    public void setDataIndexBy(String dataIndexBy) {
        this.dataIndexBy = dataIndexBy;
    }

    public int getMaxNumberOfRules() {
        return maxNumberOfRules;
    }

    public void setMaxNumberOfRules(int maxNumberOfRules) {
        this.maxNumberOfRules = maxNumberOfRules;
    }

    public String getMetricsIndexBy() {
        return metricsIndexBy;
    }

    public void setMetricsIndexBy(String metricsIndexBy) {
        this.metricsIndexBy = metricsIndexBy;
    }

    public boolean isSimpleConnection() {
        return simpleConnection;
    }

    public void setSimpleConnection(boolean simpleConnection) {
        this.simpleConnection = simpleConnection;
    }

    public boolean isSslConnection() {
        return sslConnection;
    }

    public void setSslConnection(boolean sslConnection) {
        this.sslConnection = sslConnection;
    }

    public boolean isMutualSslConnection() {
        return MutualSslConnection;
    }

    public void setMutualSslConnection(boolean mutualSslConnection) {
        MutualSslConnection = mutualSslConnection;
    }

    public long getMqttThreshold() {
        return mqttThreshold;
    }

    public void setMqttThreshold(long mqttThreshold) {
        this.mqttThreshold = mqttThreshold;
    }

    public long getRestThreshold() {
        return restThreshold;
    }

    public void setRestThreshold(long restThreshold) {
        this.restThreshold = restThreshold;
    }

    public long getRestCommandThreshold() {
        return restCommandThreshold;
    }

    public void setRestCommandThreshold(long restCommandThreshold) {
        this.restCommandThreshold = restCommandThreshold;
    }

    // Tight Coupling
    public boolean getDeviceNewAllowUnprovisioned() {
        return deviceNewAllowUnprovisioned;
    }

    public void setDeviceNewAllowUnprovisioned(boolean deviceNewAllowUnprovisioned) {
        this.deviceNewAllowUnprovisioned = deviceNewAllowUnprovisioned;
    }

    public GwtDeviceCredentialsTight getDeviceDefaultCredentialsTight() {
        return deviceDefaultCredentialsTight;
    }

    public void setDeviceDefaultCredentialsTight(String deviceDefaultCredentialsTight) {
        setDeviceDefaultCredentialsTight(GwtDeviceCredentialsTight.getEnumFromLabel(deviceDefaultCredentialsTight));
    }

    public void setDeviceDefaultCredentialsTight(GwtDeviceCredentialsTight deviceDefaultCredentialsTight) {
        this.deviceDefaultCredentialsTight = deviceDefaultCredentialsTight;
    }
}