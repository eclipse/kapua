/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.device.shared.model.management.packages;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

public class GwtPackageUninstallRequest extends KapuaBaseModel {

    private static final long serialVersionUID = 7021896328090446806L;

    public void setScopeId(String scopeId) {
        set("scopeId", scopeId);
    }

    public String getScopeId() {
        return (String) get("scopeId");
    }

    public void setDeviceId(String deviceId) {
        set("deviceId", deviceId);
    }

    public String getDeviceId() {
        return (String) get("deviceId");
    }

    public void setPackageName(String packageName) {
        set("packageName", packageName);
    }

    public String getPackageName() {
        return (String) get("packageName");
    }

    public void setPackageVersion(String packageVersion) {
        set("packageVersion", packageVersion);
    }

    public String getPackageVersion() {
        return (String) get("packageVersion");
    }

    public void setReboot(Boolean reboot) {
        set("reboot", reboot);
    }

    public Boolean isReboot() {
        return (Boolean) get("reboot");
    }

    public void setRebootDelay(Integer rebootDelay) {
        set("rebootDelay", rebootDelay);
    }

    public Integer getRebootDelay() {
        return (Integer) get("rebootDelay");
    }
}
