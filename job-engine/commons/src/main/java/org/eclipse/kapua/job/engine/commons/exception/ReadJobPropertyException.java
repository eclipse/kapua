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
package org.eclipse.kapua.job.engine.commons.exception;

public class ReadJobPropertyException extends JobCommonsRuntimeException {

    public ReadJobPropertyException(Throwable t, String parameterName, String parameterValue) {
        super(JobCommonsErrorCodes.READ_JOB_PROPERTY, t, parameterName, parameterValue);
    }
}
