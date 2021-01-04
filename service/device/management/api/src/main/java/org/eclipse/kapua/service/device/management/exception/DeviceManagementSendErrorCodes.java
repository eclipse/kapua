/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.exception;

public enum DeviceManagementSendErrorCodes implements DeviceManagementErrorCodes {

    /**
     * An error occurred when sending the {@link org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage}.
     *
     * @since 1.1.0
     */
    SEND_ERROR

}
