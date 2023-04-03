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
package org.eclipse.kapua.transport.exception.model;

import org.eclipse.kapua.transport.exception.TransportErrorCodes;
import org.eclipse.kapua.transport.exception.TransportException;
import org.eclipse.kapua.transport.exception.TransportExceptionTest;

/**
 * {@link TransportException} for testing.
 *
 * @see TransportExceptionTest#testTransportErrorCodesHaveMessages()
 * @since 2.0.0
 */
public class TestCodesTransportClientException extends TransportException {

    /**
     * Constructor.
     *
     * @param code The {@link TransportErrorCodes} to test.
     * @since 2.0.0
     */
    public TestCodesTransportClientException(TransportErrorCodes code) {

        super(code);
    }
}
