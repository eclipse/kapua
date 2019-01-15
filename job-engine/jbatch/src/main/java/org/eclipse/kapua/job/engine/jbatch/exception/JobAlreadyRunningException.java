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
package org.eclipse.kapua.job.engine.jbatch.exception;

import org.eclipse.kapua.model.id.KapuaId;

import java.util.Set;

public class JobAlreadyRunningException extends JobEngineException {

    public JobAlreadyRunningException(KapuaId scopeId, KapuaId jobId, Set<KapuaId> jobTargetIdSubset) {
        super(KapuaJobEngineErrorCodes.JOB_ALREADY_RUNNING, scopeId, jobId, jobTargetIdSubset);
    }
}
