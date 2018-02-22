/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.internal.exception;

import org.eclipse.kapua.service.device.management.commons.exception.DeviceManagementResponseErrorCodes;

public enum PackageManagementResponseErrorCodes implements DeviceManagementResponseErrorCodes {

    /**
     * The device has returned an error when getting the list of installed packages
     */
    PACKAGE_GET_ERROR,

    /**
     * The device has returned an error when getting status of download
     */
    PACKAGE_DOWNLOAD_STATUS_ERROR,

    /**
     * The device has returned an error when executing download
     */
    PACKAGE_DOWNLOAD_EXECUTE_ERROR,

    /**
     * The device has returned an error when stopping download
     */
    PACKAGE_DOWNLOAD_STOP_ERROR,

    /**
     * The device has returned an error when getting status of install
     */
    PACKAGE_INSTALL_STATUS_ERROR,

    /**
     * The device has returned an error when executing install
     */
    PACKAGE_INSTALL_EXECUTE_ERROR,

    /**
     * The device has returned an error when getting status of uninstall
     */
    PACKAGE_UNINSTALL_STATUS_ERROR,

    /**
     * The device has returned an error when executing uninstall
     */
    PACKAGE_UNINSTALL_EXECUTE_ERROR,
}
