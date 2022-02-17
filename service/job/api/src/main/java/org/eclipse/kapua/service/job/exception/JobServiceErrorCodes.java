/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * {@link KapuaErrorCode}s for {@link JobServiceException}
 *
 * @since 1.5.0
 */
public enum JobServiceErrorCodes implements KapuaErrorCode {

    /**
     * See {@link CannotModifyJobStepsException}.
     *
     * @since 1.5.0
     */
    CANNOT_MODIFY_JOB_STEPS,
}
