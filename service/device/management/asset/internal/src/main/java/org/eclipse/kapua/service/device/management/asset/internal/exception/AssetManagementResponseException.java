/*******************************************************************************
 * Copyright (c) 2018, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.asset.internal.exception;

import org.eclipse.kapua.service.device.management.exception.DeviceManagementResponseException;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;

public class AssetManagementResponseException extends DeviceManagementResponseException {

    private static final long serialVersionUID = 1558850908553078621L;

    public AssetManagementResponseException(AssetManagementResponseErrorCodes code, KapuaResponseCode responseCode, String exceptionMessage, String exceptionStack, Object... arguments) {
        super(code, responseCode, exceptionMessage, exceptionStack, arguments);
    }
}
