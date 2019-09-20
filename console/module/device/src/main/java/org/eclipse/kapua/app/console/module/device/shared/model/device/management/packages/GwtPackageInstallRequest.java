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
package org.eclipse.kapua.app.console.module.device.shared.model.device.management.packages;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

public class GwtPackageInstallRequest extends KapuaBaseModel {

    private static final long serialVersionUID = 1L;

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

    public void setPackageURI(String packageURI) {
        set("packageURI", packageURI);
    }

    public String getPackageURI() {
        return get("packageURI");
    }

    public void setPackageName(String packageName) {
        set("packageName", packageName);
    }

    public String getPackageName() {
        return get("packageName");
    }

    public void setPackageVersion(String packageVersion) {
        set("packageVersion", packageVersion);
    }

    public String getPackageVersion() {
        return get("packageVersion");
    }

    public void setUsername(String username) {
        set("username", username);
    }

    public String getUsername() {
        return get("username");
    }

    public void setPassword(String password) {
        set("password", password);
    }

    public String getPassword() {
        return get("password");
    }

    public void setFileHash(String fileHash) {
        set("fileHash", fileHash);
    }

    public String getFileHash() {
        return get("fileHash");
    }

    public void setFileType(GwtFileType fileType) {
        setFileType(fileType.name());
    }

    public void setFileType(String fileType) {
        set("fileType", fileType);
    }

    public String getFileType() {
        return get("fileType");
    }

    public GwtFileType gwtFileTypeEnum() {
        return GwtFileType.valueOf(getFileType());
    }

    public void setReboot(Boolean reboot) {
        set("reboot", reboot);
    }

    public Boolean isReboot() {
        return get("reboot");
    }

    public void setRebootDelay(int rebootDelay) {
        set("rebootDelay", rebootDelay);
    }

    public Integer getRebootDelay() {
        return get("rebootDelay");
    }

    //
    // Advanced

    public void setBlockSize(Integer blockSize) {
        set("blockSize", blockSize);
    }

    public Integer getBlockSize() {
        return get("blockSize");
    }

    public void setBlockDelay(Integer blockDelay) {
        set("blockDelay", blockDelay);
    }

    public Integer getBlockDelay() {
        return get("blockDelay");
    }

    public void setBlockTimeout(Integer blockTimeout) {
        set("blockTimeout", blockTimeout);
    }

    public Integer getBlockTimeout() {
        return get("blockTimeout");
    }

    public void setNotifyBlockSize(Integer notifyBlockSize) {
        set("notifyBlockSize", notifyBlockSize);
    }

    public Integer getNotifyBlockSize() {
        return get("notifyBlockSize");
    }

    public void setInstallVerifierURI(String installVerifierURI) {
        set("installVerifierURI", installVerifierURI);
    }

    public String getInstallVerifierURI() {
        return get("installVerifierURI");
    }
}
