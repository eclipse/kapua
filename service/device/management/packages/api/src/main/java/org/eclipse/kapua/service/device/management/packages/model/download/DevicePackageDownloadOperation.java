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
package org.eclipse.kapua.service.device.management.packages.model.download;

import org.eclipse.kapua.model.id.KapuaId;

/**
 * Device download package operation entity definition.
 *
 * @since 1.0
 */
public interface DevicePackageDownloadOperation {

    /**
     * Get the download package identifier
     *
     * @return
     */
    KapuaId getId();

    /**
     * Set the download package identifier
     *
     * @param id
     */
    void setId(KapuaId id);

    /**
     * Get the package size
     *
     * @return
     */
    Integer getSize();

    /**
     * Set the package size
     *
     * @param downloadSize
     */
    void setSize(Integer downloadSize);

    /**
     * Get the download progress
     *
     * @return
     */
    Integer getProgress();

    /**
     * Set the download progress
     *
     * @param downloadProgress
     */
    void setProgress(Integer downloadProgress);

    /**
     * Get the download status
     *
     * @return
     */
    DevicePackageDownloadStatus getStatus();

    /**
     * Set the download status
     *
     * @param status
     */
    void setStatus(DevicePackageDownloadStatus status);

}
