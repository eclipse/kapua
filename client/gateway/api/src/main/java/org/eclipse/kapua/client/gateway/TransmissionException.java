/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway;

import java.io.IOException;

/**
 * An exception indicating a temporary transmission error.
 *
 * This exception indicates that the message could not be transmitted
 * at the moment. Re-trying at a later time is a valid option though.
 */
public class TransmissionException extends IOException {

    private static final long serialVersionUID = 1L;

    public TransmissionException() {
    }

    public TransmissionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TransmissionException(final String message) {
        super(message);
    }

    public TransmissionException(final Throwable cause) {
        super(cause);
    }

}
