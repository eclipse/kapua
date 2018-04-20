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

}
