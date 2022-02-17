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

import org.eclipse.kapua.model.id.KapuaId;

public class CleanJobDataException extends JobEngineException {

    public CleanJobDataException(Throwable t, KapuaId scopeId, KapuaId jobId) {
        super(KapuaJobEngineErrorCodes.CANNOT_CLEANUP_JOB_DATA, t, scopeId, jobId);
    }

    public CleanJobDataException(KapuaId scopeId, KapuaId jobId) {
        this(null, scopeId, jobId);
    }

}
