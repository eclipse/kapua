/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.exception.model;

import org.eclipse.kapua.job.engine.exception.JobEngineErrorCodes;
import org.eclipse.kapua.job.engine.exception.JobEngineException;
import org.eclipse.kapua.job.engine.exception.JobEngineExceptionTest;

/**
 * {@link JobEngineException} for testing.
 *
 * @see JobEngineExceptionTest#testJobEngineErrorCodesHaveMessages()
 * @since 2.0.0
 */
public class TestCodesJobEngineException extends JobEngineException {

    private static final long serialVersionUID = 2784536347522419174L;

    /**
     * Constructor.
     *
     * @param code The {@link JobEngineErrorCodes} to test.
     * @since 2.0.0
     */
    public TestCodesJobEngineException(JobEngineErrorCodes code) {

        super(code);
    }
}
