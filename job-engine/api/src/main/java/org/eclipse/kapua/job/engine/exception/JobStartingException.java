/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.exception;

import org.eclipse.kapua.model.id.KapuaId;

public class JobStartingException extends JobEngineException {

    private final KapuaId scopeId;
    private final KapuaId jobId;

    public JobStartingException(Throwable t, KapuaId scopeId, KapuaId jobId) {
        super(KapuaJobEngineErrorCodes.JOB_STARTING, t, scopeId, jobId);
        this.scopeId = scopeId;
        this.jobId = jobId;
    }

    public KapuaId getScopeId() {
        return scopeId;
    }

    public KapuaId getJobId() {
        return jobId;
    }
}
