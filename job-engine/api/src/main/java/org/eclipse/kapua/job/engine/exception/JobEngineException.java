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
package org.eclipse.kapua.job.engine.exception;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * @since 1.0.0
 */
public abstract class JobEngineException extends KapuaException {

    private final KapuaId scopeId;
    private final KapuaId jobId;

    private static final String ERROR_MESSAGES_BUNDLE_NAME = "job-engine-service-error-messages";

    /**
     * @since 1.0.0
     */
    protected JobEngineException(KapuaJobEngineErrorCodes code) {
        this(code, null, null, null);
    }

    /**
     * @since 1.0.0
     */
    protected JobEngineException(KapuaJobEngineErrorCodes code, KapuaId scopeId, KapuaId jobId, Object... arguments) {
        this(code, null, scopeId, jobId, arguments);
    }

    /**
     * @since 1.0.0
     */
    protected JobEngineException(KapuaJobEngineErrorCodes code, Throwable cause, KapuaId scopeId, KapuaId jobId, Object... arguments) {
        super(code, cause, scopeId, jobId, arguments);
        this.scopeId = scopeId;
        this.jobId = jobId;
    }

    /**
     * @since 1.5.0
     */
    @Override
    protected String getKapuaErrorMessagesBundle() {
        return ERROR_MESSAGES_BUNDLE_NAME;
    }

    public KapuaId getScopeId() {
        return scopeId;
    }

    public KapuaId getJobId() {
        return jobId;
    }
}
