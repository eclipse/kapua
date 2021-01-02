/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadOperation;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadStatus;

/**
 * Device download package operation entity.
 *
 * @since 1.0
 *
 */
@XmlRootElement(name = "packageDownloadOperation")
public class DevicePackageDownloadOperationImpl implements DevicePackageDownloadOperation {

    @XmlElement(name = "id")
    private KapuaEid id;

    @XmlElement(name = "size")
    private Integer size;

    @XmlElement(name = "progress")
    private Integer progress;

    @XmlElement(name = "status")
    private DevicePackageDownloadStatus status;

    /**
     * Constructor
     */
    public DevicePackageDownloadOperationImpl() {
    }

    @Override
    public KapuaEid getId() {
        return id;
    }

    @Override
    public void setId(KapuaId id) {
        if (id != null) {
            this.id = new KapuaEid(id.getId());
        }
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
