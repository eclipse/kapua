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

public class GwtAccountUsages implements Serializable {

    private static final long serialVersionUID = -5934974615091866991L;

    private Long              deviceCount;
    private Long              dataCount;
    private Long              ruleCount;
    private Long              childCount;
    private Long              provisionRequestCount;
    private Long              deviceJobsCount;
    private Long              vpnConnectionCount;

    private Long              childDeviceCount;
    private Long              childDataCount;
    private Long              childRuleCount;
    private Long              childAccountCount;
    private Long              childProvisionRequestCount;
    private Long              childDeviceJobsCount;
    private Long              childVpnConnectionCount;

    private Long              availableDevice;
    private Long              availableData;
    private Long              availableRule;
    private Long              availableAccount;
    private Long              availableProvisioRequest;
    private Long              availableDeviceJobs;
    private Long              availableVpnConnection;

    public GwtAccountUsages() {
    }

    public Long getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Long deviceCount) {
        this.deviceCount = deviceCount;
    }

    public Long getDataCount() {
        return dataCount;
    }

    public void setDataCount(Long dataCount) {
        this.dataCount = dataCount;
    }

    public Long getRuleCount() {
        return ruleCount;
    }

    public void setRuleCount(Long ruleCount) {
        this.ruleCount = ruleCount;
    }

    public Long getChildCount() {
        return childCount;
    }

    public void setChildCount(Long childCount) {
        this.childCount = childCount;
    }

    public Long getProvisionRequestCount() {
        return provisionRequestCount;
    }

    public void setProvisionRequestCount(Long provisionRequestCount) {
        this.provisionRequestCount = provisionRequestCount;
    }

    public Long getVpnConnectionCount() {
        return vpnConnectionCount;
    }

    public void setVpnConnectionCount(Long vpnConnectionCount) {
        this.vpnConnectionCount = vpnConnectionCount;
    }

    public Long getChildDeviceCount() {
        return childDeviceCount;
    }

    public void setChildDeviceCount(Long childDeviceCount) {
        this.childDeviceCount = childDeviceCount;
    }

    public Long getChildDataCount() {
        return childDataCount;
    }

    public void setChildDataCount(Long childDataCount) {
        this.childDataCount = childDataCount;
    }

    public Long getChildRuleCount() {
        return childRuleCount;
    }

    public void setChildRuleCount(Long childRuleCount) {
        this.childRuleCount = childRuleCount;
    }

    public Long getChildAccountCount() {
        return childAccountCount;
    }

    public void setChildAccountCount(Long childAccountCount) {
        this.childAccountCount = childAccountCount;
    }

    public Long getChildProvisionRequestCount() {
        return childProvisionRequestCount;
    }

    public void setChildProvisionRequestCount(Long childProvisionRequestCount) {
        this.childProvisionRequestCount = childProvisionRequestCount;
    }

    public Long getChildVpnConnectionCount() {
        return childVpnConnectionCount;
    }

    public void setChildVpnConnectionCount(Long childVpnConnectionCount) {
        this.childVpnConnectionCount = childVpnConnectionCount;
    }

    public Long getAvailableDevice() {
        return availableDevice;
    }

    public void setAvailableDevice(Long availableDevice) {
        this.availableDevice = availableDevice;
    }

    public Long getAvailableData() {
        return availableData;
    }

    public void setAvailableData(Long availableData) {
        this.availableData = availableData;
    }

    public Long getAvailableRule() {
        return availableRule;
    }

    public void setAvailableRule(Long availableRule) {
        this.availableRule = availableRule;
    }

    public Long getAvailableAccount() {
        return availableAccount;
    }

    public void setAvailableAccount(Long availableAccount) {
        this.availableAccount = availableAccount;
    }

    public Long getAvailableProvisioRequest() {
        return availableProvisioRequest;
    }

    public void setAvailableProvisioRequest(Long availableProvisioRequest) {
        this.availableProvisioRequest = availableProvisioRequest;
    }

    public Long getAvailableVpnConnection() {
        return availableVpnConnection;
    }

    public void setAvailableVpnConnection(Long availableVpnConnection) {
        this.availableVpnConnection = availableVpnConnection;
    }

    public Number[] getAccountCounts() {
        return new Number[] { dataCount, deviceCount, ruleCount, childCount, provisionRequestCount, deviceJobsCount, vpnConnectionCount };
    }

    public Number[] getChildCounts() {
        return new Number[] { childDataCount, childDeviceCount, childRuleCount, childAccountCount, childProvisionRequestCount, childDeviceJobsCount, childVpnConnectionCount };
    }

    public Number[] getAvailableCounts() {
        return new Number[] { availableData, availableDevice, availableRule, availableAccount, availableProvisioRequest, availableDeviceJobs, availableVpnConnection };
    }

    public void setAccountCounts(Number[] accountCounts) {
        dataCount = (Long) (accountCounts[0] == null ? accountCounts[0] : accountCounts[0].longValue());
        deviceCount = (Long) (accountCounts[1] == null ? accountCounts[1] : accountCounts[1].longValue());
        ruleCount = (Long) (accountCounts[2] == null ? accountCounts[2] : accountCounts[2].longValue());
        childCount = (Long) (accountCounts[3] == null ? accountCounts[3] : accountCounts[3].longValue());
        provisionRequestCount = (Long) (accountCounts[4] == null ? accountCounts[4] : accountCounts[4].longValue());
        deviceJobsCount = (Long) (accountCounts[5] == null ? accountCounts[5] : accountCounts[5].longValue());
        vpnConnectionCount = (Long) (accountCounts[6] == null ? accountCounts[6] : accountCounts[6].longValue());
    }

    public void setChildCounts(Number[] childCounts) {
        childDataCount = (Long) (childCounts[0] == null ? childCounts[0] : childCounts[0].longValue());
        childDeviceCount = (Long) (childCounts[1] == null ? childCounts[1] : childCounts[1].longValue());
        childRuleCount = (Long) (childCounts[2] == null ? childCounts[2] : childCounts[2].longValue());
        childAccountCount = (Long) (childCounts[3] == null ? childCounts[3] : childCounts[3].longValue());
        childProvisionRequestCount = (Long) (childCounts[4] == null ? childCounts[4] : childCounts[4].longValue());
        childDeviceJobsCount = (Long) (childCounts[5] == null ? childCounts[5] : childCounts[5].longValue());
        childVpnConnectionCount = (Long) (childCounts[6] == null ? childCounts[6] : childCounts[6].longValue());
    }

    public void setAvailableCounts(Number[] availableCounts) {
        availableData = (Long) (availableCounts[0] == null ? availableCounts[0] : availableCounts[0].longValue());
        availableDevice = (Long) (availableCounts[1] == null ? availableCounts[1] : availableCounts[1].longValue());
        availableRule = (Long) (availableCounts[2] == null ? availableCounts[2] : availableCounts[2].longValue());
        availableAccount = (Long) (availableCounts[3] == null ? availableCounts[3] : availableCounts[3].longValue());
        availableProvisioRequest = (Long) (availableCounts[4] == null ? availableCounts[4] : availableCounts[4].longValue());
        availableDeviceJobs = (Long) (availableCounts[5] == null ? availableCounts[5] : availableCounts[5].longValue());
        availableVpnConnection = (Long) (availableCounts[6] == null ? availableCounts[6] : availableCounts[6].longValue());
    }
}
