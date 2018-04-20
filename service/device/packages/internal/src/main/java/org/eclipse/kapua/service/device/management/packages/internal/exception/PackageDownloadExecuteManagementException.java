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

import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;

public class PackageDownloadExecuteManagementException extends PackageManagementResponseException {

    public PackageDownloadExecuteManagementException(KapuaResponseCode responseCode, String exceptionMessage, String exceptionStack) {
        super(PackageManagementResponseErrorCodes.PACKAGE_DOWNLOAD_EXECUTE_ERROR, responseCode, exceptionMessage, exceptionStack);
    }
}
