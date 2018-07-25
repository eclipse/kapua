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
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.message.internal;

import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponsePayloadImpl;
import org.eclipse.kapua.service.device.management.packages.model.download.DevicePackageDownloadStatus;

/**
 * Package response message payload.
 * 
 * @since 1.0
 *
 */
public class PackageResponsePayload extends KapuaResponsePayloadImpl implements KapuaPayload {

    /**
     * Set the package download operation identifier
     * 
     * @param operationId
     */
    public void setPackageDownloadOperationId(KapuaId operationId) {
        if (operationId != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID.getValue(), operationId);
        }
    }

    /**
     * Get the package download operation identifier
     * 
     * @return
     */
    public KapuaId getPackageDownloadOperationId() {
        return (KapuaId) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_OPERATION_ID.getValue());
    }

    /**
     * Set the package download operation status
     * 
     * @param status
     */
    public void setPackageDownloadOperationStatus(DevicePackageDownloadStatus status) {
        if (status != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_STATUS.getValue(), status);
        }
    }

    /**
     * Get the package download operation status
     * 
     * @return
     */
    public DevicePackageDownloadStatus getPackageDownloadOperationStatus() {
        return (DevicePackageDownloadStatus) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_STATUS.getValue());
    }

    /**
     * Get the package download size
     * 
     * @param size
     */
    public void setPackageDownloadOperationSize(Integer size) {
        if (size != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_SIZE.getValue(), size);
        }
    }

    /**
     * Set the package download size
     * 
     * @return
     */
    public Integer getPackageDownloadOperationSize() {
        return (Integer) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_SIZE.getValue());
    }

    /**
     * Set the package download progress
     * 
     * @param progress
     */
    public void setPackageDownloadOperationProgress(Integer progress) {
        if (progress != null) {
            getMetrics().put(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PROGRESS.getValue(), progress);
        }
    }

    /**
     * Get the package download progress
     * 
     * @return
     */
    public Integer getPackageDownloadOperationProgress() {
        return (Integer) getMetrics().get(PackageAppProperties.APP_PROPERTY_PACKAGE_DOWNLOAD_PROGRESS.getValue());
    }
}
