/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Advanced options for the {@link org.eclipse.kapua.service.device.registry.Device} when performing a {@link DevicePackageDownloadOperation}
 *
 * @since 1.1.0
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newAdvancedPackageDownloadOptions")
public interface AdvancedPackageDownloadOptions {

    /**
     * Gets whether or not to restart the download from the beginning.
     *
     * @return {@code true} if the download must be restarted from the beginning, {@code false} otherwise.
     * @since 1.2.0
     */
    Boolean getRestart();

    /**
     * Sets whether or not to restart the download from the beginning.
     *
     * @param restart {@code true} if the download must be restarted from the beginning, {@code false} otherwise.
     * @since 1.2.0
     */
    void setRestart(Boolean restart);

    /**
     * Gets the size in {@code Byte}s of the blocks to transfer from the URI.
     *
     * @return The size in {@code Byte}s of the blocks to transfer from the URI.
     * @since 1.1.0
     */
    @XmlElement(name = "blockSize")
    Integer getBlockSize();

    /**
     * Sets the size in {@code Byte}s of the blocks to transfer from the URI.
     *
     * @param blockSize The size in {@code Byte}s of the blocks to transfer from the URI.
     * @since 1.1.0
     */
    void setBlockSize(Integer blockSize);

    /**
     * Gets the delay between each block transfer from the URI.
     *
     * @return The delay between each block transfer from the URI.
     * @since 1.1.0
     */
    @XmlElement(name = "blockDelay")
    Integer getBlockDelay();

    /**
     * Sets the delay between each block transfer from the URI.
     *
     * @param blockDelay The delay between each block transfer from the URI.
     * @since 1.1.0
     */
    void setBlockDelay(Integer blockDelay);

    /**
     * Gets the timeout for transferring a block from the URI.
     *
     * @return The timeout for transferring a block from the URI.
     * @since 1.1.0
     */
    @XmlElement(name = "blockTimeout")
    Integer getBlockTimeout();

    /**
     * Sets the timeout for transferring a block from the URI.
     *
     * @param blockTimeout The timeout for transferring a block from the URI.
     * @since 1.1.0
     */
    void setBlockTimeout(Integer blockTimeout);

    /**
     * Gets the size in {@code Byte}s of the blocks to be transfer to cause a {@link org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification} to be sent from the {@link org.eclipse.kapua.service.device.registry.Device}.
     *
     * @return The size in {@code Byte}s of the blocks to be transfer to cause a {@link org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification}.
     * @since 1.1.0
     */
    @XmlElement(name = "notifyBlockSize")
    Integer getNotifyBlockSize();

    /**
     * Sets the size in {@code Byte}s of the blocks to be transfer to cause a {@link org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification} to be sent from the {@link org.eclipse.kapua.service.device.registry.Device}.
     *
     * @param notifyBlockSize The size in {@code Byte}s of the blocks to be transfer to cause a {@link org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification}.
     * @since 1.1.0
     */
    void setNotifyBlockSize(Integer notifyBlockSize);

    /**
     * Gets the URI for the executable shell script to verify the installing of the downloaded file.
     *
     * @return The URI for the executable shell script to verify the installing of the downloaded file.
     * @since 1.1.0
     */
    @XmlElement(name = "installVerifierURI")
    String getInstallVerifierURI();

    /**
     * Sets the URI for the executable shell script to verify the installing of the downloaded file.
     *
     * @param installVerifiesURI The URI for the executable shell script to verify the installing of the downloaded file.
     * @since 1.1.0
     */
    void setInstallVerifierURI(String installVerifiesURI);
}
