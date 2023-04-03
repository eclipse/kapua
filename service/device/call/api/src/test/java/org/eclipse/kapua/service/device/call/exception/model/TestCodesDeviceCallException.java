/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.exception.model;

import org.eclipse.kapua.service.device.call.exception.DeviceCallErrorCodes;
import org.eclipse.kapua.service.device.call.exception.DeviceCallException;
import org.eclipse.kapua.service.device.call.exception.DeviceCallExceptionTest;
import org.eclipse.kapua.transport.exception.TransportException;

/**
 * {@link TransportException} for testing.
 *
 * @see DeviceCallExceptionTest#testDeviceCallErrorCodesHaveMessages()
 * @since 2.0.0
 */
public class TestCodesDeviceCallException extends DeviceCallException {

    /**
     * Constructor.
     *
     * @param code The {@link DeviceCallErrorCodes} to test.
     * @since 2.0.0
     */
    public TestCodesDeviceCallException(DeviceCallErrorCodes code) {
        super(code);
    }
}
