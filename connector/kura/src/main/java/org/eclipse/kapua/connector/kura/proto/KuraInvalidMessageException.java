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
package org.eclipse.kapua.connector.kura.proto;

/**
 * This class is not intended to be subclassed by clients.
 */
public class KuraInvalidMessageException extends RuntimeException {

    private static final long serialVersionUID = -3636897647706575102L;

    public KuraInvalidMessageException(Throwable cause) {
        super(cause);
    }
}
