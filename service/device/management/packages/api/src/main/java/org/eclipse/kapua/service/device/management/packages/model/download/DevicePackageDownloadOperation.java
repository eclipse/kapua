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
package org.eclipse.kapua.service.device.management.packages.model.download;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackageXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Device download package operation entity definition.
 *
 * @since 1.0
 */
@XmlRootElement(name = "packageDownloadOperation")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newDevicePackageDownloadOperation")
public interface DevicePackageDownloadOperation {

    /**
     * Get the download package identifier
     *
     * @return
     */
    @XmlElement(name = "id")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
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
    @XmlElement(name = "size")
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
    @XmlElement(name = "progress")
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
    @XmlElement(name = "status")
    DevicePackageDownloadStatus getStatus();

    /**
     * Set the download status
     *
     * @param status
     */
    void setStatus(DevicePackageDownloadStatus status);

}
