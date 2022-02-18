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
package org.eclipse.kapua.job.engine.jbatch.driver.exception;

import org.eclipse.kapua.KapuaErrorCode;

/**
 * Job Engine error codes
 * <p>
 * since 1.0
 */
public enum JbatchDriverErrorCodes implements KapuaErrorCode {

    JOB_EXECUTION_NOT_FOUND,

    JOB_EXECUTION_NOT_RUNNING,

    JOB_STARTING,

    CANNOT_CREATE_TMP_DIRECTORY,

    CANNOT_CLEAN_XML_JOB_DEFINITION_FILE,

    CANNOT_WRITE_XML_JOB_DEFINITION_FILE,

    CANNOT_BUILD_JOB_DEFINITION,

    JOB_EXECUTION_IS_RUNNING,

    CANNOT_CLEAN_JOB_DATA;
}
