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

import org.eclipse.kapua.service.device.registry.Device;

/**
 * Wrapper around Device to make the Device class comparable by its attributes.
 * It provides equals() method.
 */
public class CompDevice {

    private final Device device;

    public CompDevice(Device device) {
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CompDevice) {
            CompDevice other = (CompDevice) obj;
            return compareAllDeviceAtt(device, other.getDevice());
        } else {
            return false;
        }
    }

    private boolean compareAllDeviceAtt(Device thisDevice, Device otherDevice) {

        if (thisDevice.getScopeId() == null != (otherDevice.getScopeId() == null)) {
            return false;
        }
        if (thisDevice.getScopeId() != null && otherDevice.getScopeId() != null) {
            if (!thisDevice.getScopeId().equals(otherDevice.getScopeId())) {
                return false;
            }
        }

        if (thisDevice.getGroupId() == null != (otherDevice.getGroupId() == null)) {
            return false;
        }
        if (thisDevice.getGroupId() != null && otherDevice.getGroupId() != null) {
            if (!thisDevice.getGroupId().equals(otherDevice.getGroupId())) {
                return false;
            }
        }

        if (thisDevice.getClientId() == null != (otherDevice.getClientId() == null)) {
            return false;
        }
        if (thisDevice.getClientId() != null && otherDevice.getClientId() != null) {
            if (!thisDevice.getClientId().equals(otherDevice.getClientId())) {
                return false;
            }
        }

        if (thisDevice.getConnectionId() == null != (otherDevice.getConnectionId() == null)) {
            return false;
        }
        if (thisDevice.getConnectionId() != null && otherDevice.getConnectionId() != null) {
            if (!thisDevice.getConnectionId().equals(otherDevice.getConnectionId())) {
                return false;
            }
        }

        if (thisDevice.getDisplayName() == null != (otherDevice.getDisplayName() == null)) {
            return false;
        }
        if (thisDevice.getDisplayName() != null && otherDevice.getDisplayName() != null) {
            if (!thisDevice.getDisplayName().equals(otherDevice.getDisplayName())) {
                return false;
            }
        }

        if (thisDevice.getStatus() == null != (otherDevice.getStatus() == null)) {
            return false;
        }
        if (thisDevice.getStatus() != null && otherDevice.getStatus() != null) {
            if (!thisDevice.getStatus().equals(otherDevice.getStatus())) {
                return false;
            }
        }

        if (thisDevice.getSerialNumber() == null != (otherDevice.getSerialNumber() == null)) {
            return false;
        }
        if (thisDevice.getSerialNumber() != null && otherDevice.getSerialNumber() != null) {
            if (!thisDevice.getSerialNumber().equals(otherDevice.getSerialNumber())) {
                return false;
            }
        }

        if (thisDevice.getModelId() == null != (otherDevice.getModelId() == null)) {
            return false;
        }
        if (thisDevice.getModelId() != null && otherDevice.getModelId() != null) {
            if (!thisDevice.getModelId().equals(otherDevice.getModelId())) {
                return false;
            }
        }

        if (thisDevice.getImei() == null != (otherDevice.getImei() == null)) {
            return false;
        }
        if (thisDevice.getImei() != null && otherDevice.getImei() != null) {
            if (!thisDevice.getImei().equals(otherDevice.getImei())) {
                return false;
            }
        }

        if (thisDevice.getImsi() == null != (otherDevice.getImsi() == null)) {
            return false;
        }
        if (thisDevice.getImsi() != null && otherDevice.getImsi() != null) {
            if (!thisDevice.getImsi().equals(otherDevice.getImsi())) {
                return false;
            }
        }

        if (thisDevice.getIccid() == null != (otherDevice.getIccid() == null)) {
            return false;
        }
        if (thisDevice.getIccid() != null && otherDevice.getIccid() != null) {
            if (!thisDevice.getIccid().equals(otherDevice.getIccid())) {
                return false;
            }
        }

        if (thisDevice.getBiosVersion() == null != (otherDevice.getBiosVersion() == null)) {
            return false;
        }
        if (thisDevice.getBiosVersion() != null && otherDevice.getBiosVersion() != null) {
            if (!thisDevice.getBiosVersion().equals(otherDevice.getBiosVersion())) {
                return false;
            }
        }

        if (thisDevice.getFirmwareVersion() == null != (otherDevice.getFirmwareVersion() == null)) {
            return false;
        }
        if (thisDevice.getFirmwareVersion() != null && otherDevice.getFirmwareVersion() != null) {
            if (!thisDevice.getFirmwareVersion().equals(otherDevice.getFirmwareVersion())) {
                return false;
            }
        }

        if (thisDevice.getOsVersion() == null != (otherDevice.getOsVersion() == null)) {
            return false;
        }
        if (thisDevice.getOsVersion() != null && otherDevice.getOsVersion() != null) {
            if (!thisDevice.getOsVersion().equals(otherDevice.getOsVersion())) {
                return false;
            }
        }

        if (thisDevice.getJvmVersion() == null != (otherDevice.getJvmVersion() == null)) {
            return false;
        }
        if (thisDevice.getJvmVersion() != null && otherDevice.getJvmVersion() != null) {
            if (!thisDevice.getJvmVersion().equals(otherDevice.getJvmVersion())) {
                return false;
            }
        }

        if (thisDevice.getOsgiFrameworkVersion() == null != (otherDevice.getOsgiFrameworkVersion() == null)) {
            return false;
        }
        if (thisDevice.getOsgiFrameworkVersion() != null && otherDevice.getOsgiFrameworkVersion() != null) {
            if (!thisDevice.getOsgiFrameworkVersion().equals(otherDevice.getOsgiFrameworkVersion())) {
                return false;
            }
        }

        if (thisDevice.getApplicationFrameworkVersion() == null != (otherDevice.getApplicationFrameworkVersion() == null)) {
            return false;
        }
        if (thisDevice.getApplicationFrameworkVersion() != null && otherDevice.getApplicationFrameworkVersion() != null) {
            if (!thisDevice.getApplicationFrameworkVersion().equals(otherDevice.getApplicationFrameworkVersion())) {
                return false;
            }
        }

        if (thisDevice.getApplicationIdentifiers() == null != (otherDevice.getApplicationIdentifiers() == null)) {
            return false;
        }
        if (thisDevice.getApplicationIdentifiers() != null && otherDevice.getApplicationIdentifiers() != null) {
            if (!thisDevice.getApplicationIdentifiers().equals(otherDevice.getApplicationIdentifiers())) {
                return false;
            }
        }

        if (thisDevice.getAcceptEncoding() == null != (otherDevice.getAcceptEncoding() == null)) {
            return false;
        }
        if (thisDevice.getAcceptEncoding() != null && otherDevice.getAcceptEncoding() != null) {
            if (!thisDevice.getAcceptEncoding().equals(otherDevice.getAcceptEncoding())) {
                return false;
            }
        }

        if (thisDevice.getCredentialsMode() == null != (otherDevice.getCredentialsMode() == null)) {
            return false;
        }
        if (thisDevice.getCredentialsMode() != null && otherDevice.getCredentialsMode() != null) {
            if (!thisDevice.getCredentialsMode().equals(otherDevice.getCredentialsMode())) {
                return false;
            }
        }

        if (thisDevice.getPreferredUserId() == null != (otherDevice.getPreferredUserId() == null)) {
            return false;
        }
        if (thisDevice.getPreferredUserId() != null && otherDevice.getPreferredUserId() != null) {
            if (!thisDevice.getPreferredUserId().equals(otherDevice.getPreferredUserId())) {
                return false;
            }
        }

        if (thisDevice.getCustomAttribute1() == null != (otherDevice.getCustomAttribute1() == null)) {
            return false;
        }
        if (thisDevice.getCustomAttribute1() != null && otherDevice.getCustomAttribute1() != null) {
            if (!thisDevice.getCustomAttribute1().equals(otherDevice.getCustomAttribute1())) {
                return false;
            }
        }

        if (thisDevice.getCustomAttribute2() == null != (otherDevice.getCustomAttribute2() == null)) {
            return false;
        }
        if (thisDevice.getCustomAttribute2() != null && otherDevice.getCustomAttribute2() != null) {
            if (!thisDevice.getCustomAttribute2().equals(otherDevice.getCustomAttribute2())) {
                return false;
            }
        }

        if (thisDevice.getCustomAttribute3() == null != (otherDevice.getCustomAttribute3() == null)) {
            return false;
        }
        if (thisDevice.getCustomAttribute3() != null && otherDevice.getCustomAttribute3() != null) {
            if (!thisDevice.getCustomAttribute3().equals(otherDevice.getCustomAttribute3())) {
                return false;
            }
        }
        if (thisDevice.getCustomAttribute4() == null != (otherDevice.getCustomAttribute4() == null)) {
            return false;
        }
        if (thisDevice.getCustomAttribute4() != null && otherDevice.getCustomAttribute4() != null) {
            if (!thisDevice.getCustomAttribute4().equals(otherDevice.getCustomAttribute4())) {
                return false;
            }
        }
        if (thisDevice.getCustomAttribute5() == null != (otherDevice.getCustomAttribute5() == null)) {
            return false;
        }
        if (thisDevice.getCustomAttribute5() != null && otherDevice.getCustomAttribute5() != null) {
            if (!thisDevice.getCustomAttribute5().equals(otherDevice.getCustomAttribute5())) {
                return false;
            }
        }

        return true;
    }
}