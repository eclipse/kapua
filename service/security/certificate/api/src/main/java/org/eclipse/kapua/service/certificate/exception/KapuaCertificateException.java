/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.certificate.exception;

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.KapuaException;

public class KapuaCertificateException extends KapuaException {

    public KapuaCertificateException(KapuaErrorCode code) {
        super(code);
    }

    public KapuaCertificateException(KapuaErrorCode code, Object... arguments) {
        super(code, arguments);
    }

    public KapuaCertificateException(KapuaErrorCode code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }
}
