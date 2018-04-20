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
package org.eclipse.kapua.job.engine.commons.exception;

import org.eclipse.kapua.KapuaRuntimeException;

public class JobCommonsRuntimeException extends KapuaRuntimeException {

    public JobCommonsRuntimeException(JobCommonsErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }
}
