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

public class DeviceNeverConnectedException extends DeviceManagementException {

    private static final long serialVersionUID = -8225341071813065890L;

    public DeviceNeverConnectedException() {
        super(DeviceManagementConnectionErrorCodes.DEVICE_NEVER_CONNECTED);
    }
}
