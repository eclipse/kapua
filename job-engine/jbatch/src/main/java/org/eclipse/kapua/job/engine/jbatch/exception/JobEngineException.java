/*******************************************************************************
 * Copyright (c) 2018, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.jbatch.exception;

import org.eclipse.kapua.KapuaException;

/**
 * @since 1.0.0
 */
public abstract class JobEngineException extends KapuaException {

    private static final String ERROR_MESSAGES_BUNDLE_NAME = "job-engine-service-error-messages";

    /**
     * @since 1.0.0
     */
    protected JobEngineException(KapuaJobEngineErrorCodes code) {
        super(code);
    }

    /**
     * @since 1.0.0
     */
    protected JobEngineException(KapuaJobEngineErrorCodes code, Object... arguments) {
        super(code, arguments);
    }

    /**
     * @since 1.0.0
     */
    protected JobEngineException(KapuaJobEngineErrorCodes code, Throwable cause, Object... arguments) {
        super(code, cause, arguments);
    }

    /**
     * @since 1.5.0
     */
    @Override
    protected String getKapuaErrorMessagesBundle() {
        return ERROR_MESSAGES_BUNDLE_NAME;
    }
}
