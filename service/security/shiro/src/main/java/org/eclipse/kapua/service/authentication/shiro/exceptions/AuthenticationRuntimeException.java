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
package org.eclipse.kapua.service.authentication.shiro.exceptions;

import org.eclipse.kapua.KapuaRuntimeException;

public class AuthenticationRuntimeException extends KapuaRuntimeException {

    public AuthenticationRuntimeException(AuthenticationErrorCodes code) {
        super(code);
    }

    public AuthenticationRuntimeException(AuthenticationErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    public AuthenticationRuntimeException(AuthenticationErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }
}
