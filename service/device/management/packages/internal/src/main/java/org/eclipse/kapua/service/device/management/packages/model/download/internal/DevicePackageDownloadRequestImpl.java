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
package org.eclipse.kapua.service.device.management.packages.model.download.internal;

import org.eclipse.kapua.service.device.management.packages.model.FileType;
import org.eclipse.kapua.service.device.management.packages.model.download.AdvancedPackageDownloadOptions;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadRequest;

import java.net.URI;

/**
 * {@link DevicePackageDownloadRequest} implementation.
 *
 * @since 1.0.0
 */
public class DevicePackageDownloadRequestImpl implements DevicePackageDownloadRequest {

    private URI uri;
    private String name;
    private String version;

    private String username;
    private String password;

    private String fileHash;
    private FileType fileType;

    private Boolean install;

    private Boolean reboot;
    private Integer rebootDelay;

    private AdvancedPackageDownloadOptionsImpl advancedOptions;

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public Boolean getInstall() {
        return install;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getFileHash() {
        return fileHash;
    }

    @Override
    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    @Override
    public FileType getFileType() {
        return fileType;
    }

    @Override
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public void setInstall(Boolean install) {
        this.install = install;
    }

    @Override
    public Boolean getReboot() {
        return reboot;
    }

    @Override
    public void setReboot(Boolean reboot) {
        this.reboot = reboot;
    }

    @Override
    public Integer getRebootDelay() {
        return rebootDelay;
    }

    @Override
    public void setRebootDelay(Integer rebootDelay) {
        this.rebootDelay = rebootDelay;
    }

    @Override
    public AdvancedPackageDownloadOptions getAdvancedOptions() {
        if (advancedOptions == null) {
            advancedOptions = new AdvancedPackageDownloadOptionsImpl();
        }

        return advancedOptions;
    }

    @Override
    public void setAdvancedOptions(AdvancedPackageDownloadOptions advancedOptions) {
        this.advancedOptions = AdvancedPackageDownloadOptionsImpl.parse(advancedOptions);
    }
}
