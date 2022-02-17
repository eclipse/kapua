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
package org.eclipse.kapua.service.device.management.packages.model.download.internal;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOperation;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadStatus;

/**
 * Device download package operation entity.
 *
 * @since 1.0
 */
public class DevicePackageDownloadOperationImpl implements DevicePackageDownloadOperation {


    private KapuaId id;
    private Integer size;
    private Integer progress;
    private DevicePackageDownloadStatus status;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public DevicePackageDownloadOperationImpl() {
    }

    @Override
    public KapuaId getId() {
        return id;
    }

    @Override
    public void setId(KapuaId id) {
        this.id = id;
    }

    @Override
    public Integer getSize() {
        return size;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public Integer getProgress() {
        return progress;
    }

    @Override
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    @Override
    public DevicePackageDownloadStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(DevicePackageDownloadStatus status) {
        this.status = status;
    }
}
