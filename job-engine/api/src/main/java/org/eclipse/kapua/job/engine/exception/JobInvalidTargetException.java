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

import java.util.Set;

public class JobInvalidTargetException extends JobEngineException {

    private final Set<KapuaId> targetSublist;

    public JobInvalidTargetException(KapuaId scopeId, KapuaId jobId, Set<KapuaId> targetSublist) {
        super(KapuaJobEngineErrorCodes.JOB_TARGET_INVALID, scopeId, jobId, targetSublist.toArray());
        this.targetSublist = targetSublist;
    }

    public Set<KapuaId> getTargetSublist() {
        return targetSublist;
    }

}
