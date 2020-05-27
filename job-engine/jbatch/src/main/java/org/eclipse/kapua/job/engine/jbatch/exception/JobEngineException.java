/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch.exception;

import org.eclipse.kapua.KapuaException;

public abstract class JobEngineException extends KapuaException {

    protected JobEngineException(KapuaJobEngineErrorCodes code) {
        super(code);
    }

    protected JobEngineException(KapuaJobEngineErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    protected JobEngineException(KapuaJobEngineErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

}
