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
package org.eclipse.kapua.service.job.commons.exception;

public class ReadJobPropertyException extends JobCommonsRuntimeException {

    public ReadJobPropertyException(Throwable t, String parameterName, String parameterValue) {
        super(JobCommonsErrorCodes.READ_JOB_PROPERTY, t, parameterName, parameterValue);
    }
}
