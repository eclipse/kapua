/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Job Engine error codes
 * <p>
 * since 1.0
 */
public enum KapuaJobEngineErrorCodes implements KapuaErrorCode {
    /**
     * Internal error
     */
    INTERNAL_ERROR,
    /**
     * Job Step Missing
     */
    JOB_STEP_MISSING,
    /**
     * Job Step Missing
     */
    JOB_TARGET_MISSING

}
