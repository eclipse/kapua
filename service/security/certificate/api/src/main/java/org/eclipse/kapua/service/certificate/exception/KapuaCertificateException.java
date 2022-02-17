/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
