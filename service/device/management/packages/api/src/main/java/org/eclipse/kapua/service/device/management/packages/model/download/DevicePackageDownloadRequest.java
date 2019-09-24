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
package org.eclipse.kapua.service.device.management.packages.model.download;

import org.eclipse.kapua.service.device.management.packages.model.DevicePackageXmlRegistry;
import org.eclipse.kapua.service.device.management.packages.model.FileType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.net.URI;

/**
 * {@link DevicePackageDownloadRequest} definition.
 * <p>
 * All the available options to perform a download (and optionally install).
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "downloadRequest")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newDevicePackageDownloadRequest")
public interface DevicePackageDownloadRequest {

    /**
     * Gets the download URI of the file.
     *
     * @return The download URI of the file.
     * @since 1.0.0
     */
    @XmlElement(name = "uri")
    URI getUri();

    /**
     * Sets the download URI of the file.
     *
     * @param uri The download URI of the file.
     * @since 1.0.0
     */
    void setUri(URI uri);

    /**
     * Gets the package name.
     *
     * @return The package name.
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    String getName();

    /**
     * Sets the package name.
     *
     * @param name The package name.
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Gets the package version.
     *
     * @return The package version.
     * @since 1.0.0
     */
    @XmlElement(name = "version")
    String getVersion();

    /**
     * Set the package version.
     *
     * @param version The package version.
     * @since 1.0.0
     */
    void setVersion(String version);

    /**
     * Gets the username to provide as a credential when accessing the URI.
     *
     * @return The username to provide as a credential when accessing the URI.
     * @since 1.1.0
     */
    String getUsername();

    /**
     * Sets the username to provide as a credential when accessing the URI.
     *
     * @param username The username to provide as a credential when accessing the URI.
     * @since 1.1.0
     */
    void setUsername(String username);

    /**
     * Gets the password to provide as a credential when accessing the URI.
     *
     * @return The password to provide as a credential when accessing the URI.
     * @since 1.1.0
     */
    String getPassword();

    /**
     * Sets the password to provide as a credential when accessing the URI.
     *
     * @param password The password to provide as a credential when accessing the URI.
     * @since 1.1.0
     */
    void setPassword(String password);

    /**
     * Gets the file hash to verify the downloaded file.
     * <p>
     * It must be specifies as {@code {HASH_ALGORITHM}:{HASH_VALUE}}
     * <p>
     * Example:
     * MD5:46cbc7f212b94187cb6480fe9429a89c
     *
     * @return The file hash to verify the downloaded file.
     * @since 1.1.0
     */
    String getFileHash();

    /**
     * Sets the file hash to verify the downloaded file.
     *
     * @param fileHash The file hash to verify the downloaded file.
     * @see DevicePackageDownloadRequest#getFileHash()
     * @since 1.1.0
     */
    void setFileHash(String fileHash);

    /**
     * Gets the {@link FileType} of the target file to download.
     *
     * @return The {@link FileType} of the target file to download.
     * @since 1.1.0
     */
    FileType getFileType();

    /**
     * Sets the {@link FileType} of the target file to download.
     *
     * @param fileType The {@link FileType} of the target file to download.
     * @since 1.1.0
     */
    void setFileType(FileType fileType);

    /**
     * Gets whether or not install the file right after it has been downloaded.
     *
     * @return Whether or not install the file right after it has been downloaded.
     * @since 1.0.0
     */
    @XmlElement(name = "install")
    Boolean getInstall();

    /**
     * Sets whether or not install the file right after it has been downloaded.
     *
     * @param install Whether or not install the file right after it has been downloaded.
     * @since 1.0.0
     */
    void setInstall(Boolean install);

    /**
     * Gets whether or not reboot the device after the operation has been completed.
     *
     * @return Whether or not reboot the device after the operation has been completed.
     * @since 1.0.0
     */
    @XmlElement(name = "reboot")
    Boolean getReboot();

    /**
     * Sets whether or not reboot the device after the operation has been completed.
     *
     * @param reboot Whether or not reboot the device after the operation has been completed.
     * @since 1.0.0
     */
    void setReboot(Boolean reboot);

    /**
     * Gets the delay after which the device is rebooted when the operation has been completed.
     *
     * @return The delay after which the device is rebooted when the operation has been completed.
     * @since 1.0.0
     */
    @XmlElement(name = "rebootDelay")
    Integer getRebootDelay();

    /**
     * Sets the delay after which the device is rebooted when the operation has been completed.
     *
     * @param rebootDelay The delay after which the device is rebooted when the operation has been completed.
     * @since 1.0.0
     */
    void setRebootDelay(Integer rebootDelay);

    /**
     * Gets the {@link AdvancedPackageDownloadOptions} to tune the download operation.
     *
     * @return The {@link AdvancedPackageDownloadOptions} to tune the download operation.
     * @since 1.1.0
     */
    @XmlElement(name = "advancedOptions")
    AdvancedPackageDownloadOptions getAdvancedOptions();

    /**
     * Sets the {@link AdvancedPackageDownloadOptions} to tune the download operation.
     *
     * @param advancedOptions The {@link AdvancedPackageDownloadOptions} to tune the download operation.
     * @since 1.1.0
     */
    void setAdvancedOptions(AdvancedPackageDownloadOptions advancedOptions);
}
