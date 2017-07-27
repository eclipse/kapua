/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

    public TransmissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransmissionException(String message) {
        super(message);
    }

    public TransmissionException(Throwable cause) {
        super(cause);
    }

}
