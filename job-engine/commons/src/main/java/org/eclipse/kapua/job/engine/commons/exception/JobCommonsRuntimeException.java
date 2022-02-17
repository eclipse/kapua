/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.commons.exception;

import org.eclipse.kapua.KapuaRuntimeException;

public class JobCommonsRuntimeException extends KapuaRuntimeException {

    public JobCommonsRuntimeException(JobCommonsErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }
}
